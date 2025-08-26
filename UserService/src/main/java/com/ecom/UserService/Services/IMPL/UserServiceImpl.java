package com.ecom.UserService.Services.IMPL;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dtos.AddressResponseDto;
import com.ecom.CommonEntity.dtos.UserDto;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.Services.ServiceInterface.UserService;
import com.ecom.commonRepo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

//    @Autowired
//    private PasswordEncoder passwordEncoder;


    @Override
    @Cacheable(value = "usersCache", key = "'allUsers'")
    public ResponseModel getAllUsers() {
        try {
            System.out.println("Data Fetch In DB");
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
    @Cacheable(value = "userCache", key = "#root.target.getAuthenticatedEmail()")
//    @CacheEvict(value = "usersCache", key = "'allUsers'")
    public ResponseModel getUser(Long userId) {
        try {
//            String email = getAuthenticatedEmail();
            Users findUser = userDao.findUserByIdAndStatus(userId, Status.ACTIVE).orElseThrow(
                    () -> new IllegalArgumentException("User Not Found"));
            System.out.println("get User");

                return new ResponseModel(
                        HttpStatus.OK,
                        UserDto.toDto(findUser),
                        "SUCCESS"
                );

        } catch (IllegalArgumentException e){
            return new ResponseModel(
                    HttpStatus.UNAUTHORIZED,
                    null,
                     e.getMessage()
            );
        }
        catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Failed to fetch user: " + e.getMessage()
            );
        }
    }

    @Override
    @CachePut(value = "userCache", key = "#root.target.getAuthenticatedEmail()")
    @CacheEvict(value = "usersCache", key = "'allUsers'")
    public ResponseModel updateUser(UserDto userDto) {
        try {
//            String email = getAuthenticatedEmail();
            Optional<Users> existUserOpt = userDao.findUserByIdAndStatus(userDto.getUserId(),
                    Status.ACTIVE);
            System.out.println("User Updated..");

            if (existUserOpt.isEmpty()) {
                return new ResponseModel(
                        HttpStatus.NOT_MODIFIED,
                        null,
                        "Please Enter Current Login User Email And Status-(Active)"
                );
            }

            Users existUser = existUserOpt.get();

            if (userDto.getUserId() == null || !userDto.getUserId().equals(existUser.getUserId())) {
                return new ResponseModel(
                        HttpStatus.FORBIDDEN,
                        null,
                        "You are not allowed to update another user's account"
                );
            }

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

            if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
//                existUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
                existUser.setPassword(userDto.getPassword());
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
    @CacheEvict(value = {"userCache", "usersCache"}, allEntries = true)
    public ResponseModel blockUser(Long userId) {
        try {
//            String email = getAuthenticatedEmail();
            Optional<Users> existUserOpt = userDao.findUserByIdAndStatus(userId, Status.ACTIVE);

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


//    public String getAuthenticatedEmail() {
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }

}
