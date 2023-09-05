package com.pccontactmanager.config;

import com.pccontactmanager.dao.UserRepo;
import com.pccontactmanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImplemet implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUserName = userRepo.getUserByUserName(username);

        if(userByUserName==null)
        {
            throw new UsernameNotFoundException("user does not exit");
        }
   CustomUserDetails customUserDetails=new CustomUserDetails(userByUserName);

        return customUserDetails;
    }
}
