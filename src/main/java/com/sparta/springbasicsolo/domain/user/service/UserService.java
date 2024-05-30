package com.sparta.springbasicsolo.domain.user.service;

import com.sparta.springbasicsolo.domain.user.dto.SignupRequestDto;
import com.sparta.springbasicsolo.domain.user.repository.UserRepository;
import com.sparta.springbasicsolo.domain.user.repository.entity.User;
import com.sparta.springbasicsolo.domain.user.repository.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${signup.admin.token}")
    private String ADMIN_TOKEN;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
//        String password = requestDto.getPassword();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        User user = new User(requestDto.getEmail(), username, password, UserRoleEnum.USER);
        userRepository.save(user);
    }
}