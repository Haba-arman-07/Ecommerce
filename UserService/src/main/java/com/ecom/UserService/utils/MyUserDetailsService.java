package com.ecom.UserService.utils;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entities.Users;
import com.ecom.UserService.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
            Users users = userDao.findByEmail(email, Status.ACTIVE).orElseThrow(() ->
                    new IllegalArgumentException("User Email Not Found " + email)
            );

        return new UserPrincipal(users);
    }
}
