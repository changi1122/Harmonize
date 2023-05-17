package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users") //DB에 저장될 table 의 이름
public class User implements UserDetails {


    @Id //@id 밑에 오는 id가 User 테이블의 Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;     //ID
    private String password;
    private int gender;          //남자 : 1, 여자 : 2
    private int age;          //나이 10대, 20대 ... -> string

    private String role;

    private boolean isDeleted;
    private boolean isBanned;
    private boolean isLocked;

    protected User() { }

    public User(String username, String password) {
        this.username = username;    //ID
        this.password = password;      //PW

        /* 따로 받음
        this.gender=gender;
        this.age=age;
        */

        this.role="유저"; //유저? 관리자?

        isLocked = false;
        isBanned = false;
        isDeleted = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(this.role));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return (!isLocked && !isBanned && !isDeleted);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return (!isLocked && !isBanned && !isDeleted);
    }
}
