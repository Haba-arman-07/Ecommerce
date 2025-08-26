package com.ecom.API.Gateway.services.impl;

import com.ecom.API.Gateway.security.utils.JwtUtil;
import com.ecom.API.Gateway.services.serviceInterface.AuthService;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.LoginDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.commonRepo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Override
    public ResponseModel registerUser(UserDto userDto) {
        try {
            // Check mobile already exist for ACTIVE user
            Optional<Users> existMobile = userDao.findUserByMobileAndStatus(
                    userDto.getMobile(), Status.ACTIVE);

            // Check email already exist
            Optional<Users> existEmail = userDao.findUsersByEmailAndStatus(userDto.getEmail(), Status.ACTIVE);

            if (existMobile.isPresent()) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Mobile Already Exist"
                );
            }

            if (existEmail.isPresent()) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Email Already Exist"
                );
            }

            // Convert DTO to entity and encode password
            Users user = UserDto.toEntity(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Users saveUser = userDao.saveUsers(user);

            return new ResponseModel(
                    HttpStatus.OK,
                    UserDto.toDto(saveUser),
                    "User Added Successfully"
            );

        } catch (DataIntegrityViolationException e) {
            return new ResponseModel(
                    HttpStatus.CONFLICT,
                    null,
                    "Email Already Exist"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something Went Wrong: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel loginUser(LoginDto loginDto) {
        try {
            Authentication authentication = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

            if (authentication != null && authentication.isAuthenticated()) {
                return new ResponseModel(
                        HttpStatus.OK,
                        jwtUtil.generateToken(loginDto.getEmail()),
                        "Login Successfully"
                );
            }

            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "Login Failed"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Error"
            );
        }
    }
}
