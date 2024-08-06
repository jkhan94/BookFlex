package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.user.dto.GradeReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileReqDto;
import com.sparta.bookflex.domain.user.dto.ProfileResDto;
import com.sparta.bookflex.domain.user.dto.StateReqDto;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import com.sparta.bookflex.domain.user.repository.UserRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryQueryImpl userRepositoryQueryImpl;

    public ProfileResDto getProfile(User user) {

        User currentUser = getUser(user);

        return User.of(currentUser);
    }

    @Transactional
    public ProfileResDto updateProfile(User user, ProfileReqDto reqDto) {

        User currentUser = getUser(user);
        String password = passwordEncoder.encode(reqDto.getPassword());
        currentUser.updateProfile(password, reqDto.getNickname(), reqDto.getPhoneNumber(), reqDto.getAddress());

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

    public Page<ProfileResDto> getUsers(int page, int size, boolean isAsc, String sortBy, String username) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<User> userList = userRepositoryQueryImpl.getUsers(username, pageable);

        return userList.map(User::of);
    }
}
