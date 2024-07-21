package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.user.dto.LoginReqDto;
import com.sparta.bookflex.domain.user.dto.SignUpReqDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpReqDto signupReqDto) {
        String username = signupReqDto.getUsername();

        if (userRepository.findByUserName(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저ID입니다");
        }

        User user = User.builder()
            .username(signupReqDto.getUsername())
            .nickname(signupReqDto.getNickname())
            .grade(UserGrade.NORMAL)
            .address(signupReqDto.getAddress())
            .state(UserState.ACTIVE)
            .phoneNumber(signupReqDto.getPhoneNumber())
            .birthDay(signupReqDto.getBirthday())
            .email(signupReqDto.getEmail())
            .name(signupReqDto.getName())
            .password(signupReqDto.getPassword())
            .build();

        userRepository.save(user);
    }

    @Transactional
    public List<String> login(LoginReqDto loginReqDto) {
        Authentication authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginReqDto.getUsername(),
                loginReqDto.getPassword(),
                null
            )
        );

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        String accessToken = jwtProvider.createToken(user, JwtConfig.accessTokenTime);
        String refreshToken = jwtProvider.createToken(user, JwtConfig.refreshTokenTime);

        user.updateRefreshToken(refreshToken);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(accessToken);
        tokenList.add(refreshToken);

        return tokenList;
    }

    @Transactional
    public void signOut(User user) {

        user.updateRefreshToken("");
        user.deleteUser();
    }

    @Transactional
    public void logout(User user) {

        user.updateRefreshToken("");
    }

    public String refreshToken(User user, String refreshToken) {

        User curUser = findByUserName(user.getName());

        if(!curUser.getRefreshToken().equals(refreshToken)){
            throw new IllegalArgumentException("해당 유저와 다른 refresh Token 입니다");
        }

        String newAccessToken = jwtProvider.createToken(curUser, JwtConfig.accessTokenTime);

        return newAccessToken;
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username).orElseThrow(
            ()-> new IllegalArgumentException("존재하지 않는 유저입니다")
        );
    }
}
