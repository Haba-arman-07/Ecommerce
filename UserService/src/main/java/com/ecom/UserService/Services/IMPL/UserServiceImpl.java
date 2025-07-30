package com.ecom.UserService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressFeedDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.UserService;
import com.ecom.UserService.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public ResponseModel addUser(UserDto userDto) {
        try {
            Optional<Users> existMobile = userDao.findUserByMobileAndStatus(userDto.getMobile(), Status.ACTIVE);
            if (existMobile.isPresent()) {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "Mobile Already Exist");
            }

            Users user = UserDto.toEntity(userDto);
            Users saveUser = userDao.saveUsers(user);

            return new ResponseModel(
                    HttpStatus.OK,
                    UserDto.toDto(saveUser),
                    "User Add Successfully");

        } catch (DataIntegrityViolationException e) {
            return new ResponseModel(
                    HttpStatus.CONFLICT,
                    null,
                    "Email Already Exist");
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something Went Wrong");
        }
    }

    @Override
    public ResponseModel getAllUsers() {
        try {
            List<AddressFeedDto> users = userDao.findAllUsersWithStatus(Status.ACTIVE);
//            List<UserDto> userDtos = users.stream()
//                    .map(UserDto::toDto)
//                    .toList();

            return new ResponseModel(
                    HttpStatus.OK,
                    users,
                    "SUCCESS");

        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed to fetch users");
        }
    }


    @Override
    public ResponseModel getUser(Long userId) {
        try {
            Optional<Users> findUser = userDao.findUserByIdAndStatus(userId, Status.ACTIVE);

            if (findUser.isPresent()) {
                Users user = findUser.get();

                return new ResponseModel(
                        HttpStatus.OK,
                        UserDto.toDto(user),
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
                    "Failed to fetch user");
        }
    }

    @Override
    public ResponseModel updateUser(UserDto userDto) {
        try {
            Optional<Users> existUser = userDao.findUserByIdAndStatus(userDto.getUserId(), Status.ACTIVE);

            if (existUser.isPresent()) {
                Optional<Users> existMobile = userDao.findUsersByMobile(userDto.getMobile());

                if (existMobile.isEmpty()) {
                    Users user = existUser.get();
                    user.setFirstName(userDto.getFirstName());
                    user.setLastName(userDto.getLastName());
                    user.setEmail(userDto.getEmail());
                    user.setPassword(userDto.getPassword());
                    user.setMobile(userDto.getMobile());
                    user.setGender(user.getGender());
                    user.setUpdatedAt(LocalDateTime.now());

                    Users updateUser = userDao.saveUsers(user);;

                    return new ResponseModel(
                            HttpStatus.OK,
                            UserDto.toDto(updateUser),
                            "Data Updated Successfully"
                    );
                } else {
                    return new ResponseModel(
                            HttpStatus.BAD_REQUEST,
                            null,
                            "Mobile Already Exist");
                }
            }
            return new ResponseModel(
                    HttpStatus.NOT_MODIFIED,
                    null,
                    "Please Entered Correct UserId And Status-(Active)"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "User Updation Failed");
        }
    }

    @Override
    public ResponseModel blockUser(Long userId) {
        try {
            Optional<Users> existUser = userDao.findUserById(userId);

            if (existUser.isPresent()) {
                Users user = existUser.get();

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
                            "Account Recover Success"
                    );
                }
            }
            return new ResponseModel(
                    HttpStatus.BAD_REQUEST,
                    null,
                    "Please Entered Valid userId"
            );
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed Block/Unblock User");
        }
    }

}
