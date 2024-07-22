package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.domain.user.dto.ProfileReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileResDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResDto getProfile(long userId, User user) {

        User inputUser = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 유저ID 입니다"));
        if (!inputUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("다른 유저입니다");
        }

        return User.of(inputUser);
    }

    @Transactional
    public ProfileResDto updateProfile(long userId, User user, ProfileReqDto reqDto) {

        User inputUser = userRepository.findById(userId).orElseThrow(() ->
            new IllegalArgumentException("존재하지 않는 유저ID 입니다"));
        if (!inputUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("다른 유저입니다");
        }

        inputUser.updateProfile(reqDto.getPassword(), reqDto.getNickname(), reqDto.getPhoneNumber(), reqDto.getAddress());

        return User.of(inputUser);
    }
}
