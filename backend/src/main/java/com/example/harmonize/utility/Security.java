package com.example.harmonize.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Iterator;

@Configuration
@EnableWebSecurity
public class Security {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    /**
     * @return 현재 로그인한 사용자의 username을 반환합니다. (토큰이 없거나 잘못된 경우 "anonymousUser" 반환)
     */
    public static String getCurrentUsername()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    /**
     * @return 현재 로그인한 사용자의 role(권한)을 반환합니다. (토큰이 없거나 잘못된 경우 "ROLE_ANONYMOUS" 반환)
     */
    public static String getCurrentUserRole()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> it = authorities.iterator();
        return it.next().toString();
    }
}
