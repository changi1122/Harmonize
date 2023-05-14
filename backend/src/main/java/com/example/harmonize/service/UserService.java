package com.example.harmonize.service;

import com.example.harmonize.entity.User;
import com.example.harmonize.repository.UserRepository;
import com.example.harmonize.security.JwtTokenProvider;
import com.example.harmonize.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param username
     * @return username가 받은 값인 user를 찾아 User(UserDetails의 자식 클래스) 객체를 반환합니다.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }

    /**
     * @param id
     * @return id가 받은 값인 user를 찾아 User(UserDetails의 자식 클래스) 객체를 반환합니다.
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        return user;
    }


    /********************************************************************************
     * user를 생성합니다.
     * @param username
     * @param password
     * @param user_name
     */
    public void create(String username, String password, String user_name)
    throws Exception {

            User user = new User(
                    username,
                    passwordEncoder.encode(password),
                    user_name
            );
            userRepository.save(user);
    }


    /*******************************************************************************
     * username과 password로 로그인을 시도합니다. 100회 실패시 로그인이 잠깁니다. <- 안잠겨요
     * 성공시 엑세스 토큰을 반환합니다.
     * @param username
     * @param password
     * @return token 반환
     * @throws UsernameNotFoundException username 존재하지 않는 경우, user가 밴 또는 잠금 또는 삭제된 경우
     */
    public List<String> tryLogin(String username, String password) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));

        if (user.isBanned() || user.isLocked() || user.isDeleted())
            throw new UsernameNotFoundException("User not present");

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Password not matched");
        }

        Authentication authentication = new UserAuthentication(username, password, user.getAuthorities());
        String token = JwtTokenProvider.generateToken(authentication);

        List<String> list = new ArrayList<>();

        list.add(token);
        list.add(String.valueOf(user.getId()));

        return list;
    }

    /**
     * username 중복을 확인하여 사용할 수 있는지 반환합니다.
     * @param username
     * @return 사용할 수 있으면 true, 없으면 false
     */
    public boolean canUseAsUsername(String username) {
        return !userRepository.existsByUsername(username) && !username.equals("anonymousUser");
    }

    /**
     * user를 로그인할 수 없도록 잠급니다.
     * @param user
     */
    public void lock(User user) {
        user.setLocked(true);
        userRepository.save(user);
    }

    /**
     * user를 로그인할 수 없도록 밴 합니다.
     * @param user
     */
    public void ban(User user) {
        user.setBanned(true);
        userRepository.save(user);
    }
}