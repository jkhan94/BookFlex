package com.sparta.bookflex.domain.auth.service;

import com.sparta.bookflex.common.config.JwtConfig;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.jwt.JwtProvider;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.common.utill.LoggingSingleton;
import com.sparta.bookflex.domain.systemlog.enums.ActionType;
import com.sparta.bookflex.domain.systemlog.repository.SystemLogRepository;
import com.sparta.bookflex.domain.auth.dto.LoginReqDto;
import com.sparta.bookflex.domain.auth.dto.SignUpReqDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final SystemLogRepository systemLogRepository;
    @Value("${ADMIN_TOKEN}")
    String adminToken;

    @Transactional
    public void signUp(SignUpReqDto signupReqDto) {
        String username = signupReqDto.getUsername();

        if (userRepository.findByUserName(username).isPresent()) {
            throw new BusinessException(ErrorCode.EXIST_USER);
        }

//        if(signupReqDto.getAuthType().equals(RoleType.ADMIN)){
//            if(signupReqDto.getAdminToken() == null || !signupReqDto.getAdminToken().equals(adminToken)){
//                throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
//            }
//        }

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
            .password(passwordEncoder.encode(signupReqDto.getPassword()))
            .auth(signupReqDto.getAuthType())
            .build();

        user.createCart();

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

        User curUser = findByUserName(user.getUsername());
        curUser.updateRefreshToken(refreshToken);

        List<String> tokenList = new ArrayList<>();
        tokenList.add(accessToken);
        tokenList.add(refreshToken);

        systemLogRepository.save(LoggingSingleton.Logging(ActionType.LOGIN, curUser));

        return tokenList;
    }

    @Transactional
    public void signOut(User user) {

        User curUser = findByUserName(user.getUsername());
        curUser.updateState(UserState.WITHDRAW);
        curUser.updateRefreshToken("");
    }

    @Transactional
    public void logout(User user) {

        User curUser = findByUserName(user.getUsername());
        curUser.updateRefreshToken("");
    }

    public String refreshToken(User user, String refreshToken) {

        User curUser = findByUserName(user.getUsername());

        if(!curUser.getRefreshToken().equals(refreshToken)){
            throw new BusinessException(ErrorCode.USER_NOT_AUTHENTICATED);
        }

        String newAccessToken = jwtProvider.createToken(curUser, JwtConfig.accessTokenTime);

        return newAccessToken;
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username).orElseThrow(
            ()-> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public User findById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public RoleType getUserRole(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(
            ()-> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        return user.getAuth();
    }
}
