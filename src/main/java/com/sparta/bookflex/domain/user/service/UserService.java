package com.sparta.bookflex.domain.user.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.user.dto.*;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.UserGrade;
import com.sparta.bookflex.domain.user.enums.UserState;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

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
    public void updateState(long userId, StateReqDto reqDto) {
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

    public Page<UserListResDto> getUsers(int page, int size, boolean isAsc, String sortBy, String username) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<UserListResDto> userList = userRepositoryQueryImpl.getUsers(username, pageable)
                .map(tuple -> UserListResDto
                        .builder()
                        .id(tuple.get(0, Long.class))
                        .createdAt(tuple.get(1, LocalDateTime.class))
                        .username(tuple.get(2,String.class))
                        .name(tuple.get(3, String.class))
                        .grade(tuple.get(4, UserGrade.class))
                        .purchaseTotal(tuple.get(5, BigDecimal.class).setScale(0, RoundingMode.FLOOR))
                        .state(tuple.get(6, UserState.class).getStateString())
                        .build());

        return userList;
    }

    public ProfileResDto getUserProfile(Long userId) {
        User user = getUser(userId);
        return getProfile(user);

    }

       public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void updateUserRoleBySaleAmount(Long userId, LocalDateTime currentDateTime) {
        User user = getUser(userId);

        BigDecimal saleAmount = userRepositoryQueryImpl.getSaleAmount(userId, currentDateTime);

        if(saleAmount.compareTo(new BigDecimal("100000"))>=1) {
            user.updateGrade(UserGrade.VIP);
        } else {
            user.updateGrade(UserGrade.VIP);
        }

    }
}
