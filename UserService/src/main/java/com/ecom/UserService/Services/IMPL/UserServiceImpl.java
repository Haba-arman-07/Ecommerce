package com.ecom.UserService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressResponseDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.UserService;
import com.ecom.UserService.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseModel getAllUsers() {
        try {
            List<AddressResponseDto> users = userDao.findAllUsersWithStatus(Status.ACTIVE);

            return new ResponseModel(
                    HttpStatus.OK,
                    users,
                    "SUCCESS"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed to fetch users: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel getUser(Long userId) {
        try {
            Optional<Users> findUser = userDao.findUserByIdAndStatus(userId, Status.ACTIVE);

            if (findUser.isPresent()) {
                return new ResponseModel(
                        HttpStatus.OK,
                        UserDto.toDto(findUser.get()),
                        "SUCCESS"
                );
            }
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    "User Not Found"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed to fetch user: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel updateUser(UserDto userDto) {
        try {
            Optional<Users> existUserOpt = userDao.findUserByIdAndStatus(
                    userDto.getUserId(), Status.ACTIVE);

            if (existUserOpt.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_MODIFIED,
                        null,
                        "Please Enter Correct UserId And Status-(Active)"
                );
            }

            Users existUser = existUserOpt.get();

            // Mobile number check (excluding self)
            Optional<Users> existMobile = userDao.findUsersByMobile(userDto.getMobile());
            if (existMobile.isPresent() && !existMobile.get().getUserId().equals(userDto.getUserId())) {

                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Mobile Already Exist"
                );
            }

            // Update details
            existUser.setFirstName(userDto.getFirstName());
            existUser.setLastName(userDto.getLastName());
            existUser.setEmail(userDto.getEmail());
            existUser.setMobile(userDto.getMobile());
            existUser.setGender(userDto.getGender());
            existUser.setUpdatedAt(LocalDateTime.now());

            // Encode password if changed
            if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
                existUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            Users updatedUser = userDao.saveUsers(existUser);

            return new ResponseModel(
                    HttpStatus.OK,
                    UserDto.toDto(updatedUser),
                    "Data Updated Successfully"
            );

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "User Updation Failed: " + e.getMessage()
            );
        }
    }

    @Override
    public ResponseModel blockUser(Long userId) {
        try {
            Optional<Users> existUserOpt = userDao.findUserById(userId);

            if (existUserOpt.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Please Enter Valid userId"
                );
            }

            Users user = existUserOpt.get();

            if (user.getStatus() == Status.ACTIVE) {
                user.setStatus(Status.INACTIVE);
                userDao.saveUsers(user);
                return new ResponseModel(
                        HttpStatus.OK,
                        null,
                        "Account Deleted Successfully"
                );
            } else {
                user.setStatus(Status.ACTIVE);
                userDao.saveUsers(user);
                return new ResponseModel(
                        HttpStatus.OK,
                        null,
                        "Account Recovered Successfully"
                );
            }

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed Block/Unblock User: " + e.getMessage()
            );
        }
    }
}
