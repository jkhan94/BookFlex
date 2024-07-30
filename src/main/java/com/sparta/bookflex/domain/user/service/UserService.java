package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.user.dto.GradeReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileResDto;
import com.sparta.bookflex.domain.user.dto.StateReqDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResDto getProfile(User user) {

        User currentUser = getUser(user);

        return User.of(currentUser);
    }

    @Transactional
    public ProfileResDto updateProfile(User user, ProfileReqDto reqDto) {

        User currentUser = getUser(user);

        currentUser.updateProfile(reqDto.getPassword(), reqDto.getNickname(), reqDto.getPhoneNumber(), reqDto.getAddress());

        return User.of(currentUser);
    }

    @Transactional
    public void updateGrade(long userId, User user, GradeReqDto reqDto) {
        User inputUser = getUser(userId);

        inputUser.updateGrade(reqDto.getUserGrade());
    }

    @Transactional
    public void updateState(long userId, User user, StateReqDto reqDto) {
        User inputUser = getUser(userId);

        inputUser.updateState(reqDto.getState());
    }

    private User getUser(User user) {
        User curUser = userRepository.findById(user.getId()).orElseThrow(() ->
            new BusinessException(ErrorCode.USER_NOT_FOUND));

        return curUser;
    }

    private User getUser(long userId) {
        User curUser = userRepository.findById(userId).orElseThrow(() ->
            new BusinessException(ErrorCode.USER_NOT_FOUND));
        return curUser;
    }
}
