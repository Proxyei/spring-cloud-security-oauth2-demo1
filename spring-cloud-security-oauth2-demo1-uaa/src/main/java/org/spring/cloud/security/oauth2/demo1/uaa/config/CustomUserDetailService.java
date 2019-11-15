package org.spring.cloud.security.oauth2.demo1.uaa.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @Author future
 * @DateTime 2019/11/14 22:54
 * @Description
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("admin");

        User user = new User("a", encryPassword("a"), Arrays.asList(simpleGrantedAuthority));


        return user;
    }

    private static String encryPassword(String password) {
        String pwd = new BCryptPasswordEncoder().encode(password);
        System.out.println(pwd);
        return pwd;
    }

}
