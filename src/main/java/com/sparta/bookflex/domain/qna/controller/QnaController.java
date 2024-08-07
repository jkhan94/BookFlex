package com.sparta.bookflex.domain.qna.controller;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.qna.dto.QnaRequestDto;
import com.sparta.bookflex.domain.qna.dto.QnaResponseDto;
import com.sparta.bookflex.domain.qna.dto.ReplyRequestDto;
import com.sparta.bookflex.domain.qna.enums.QnaTypeCode;
import com.sparta.bookflex.domain.qna.service.QnaService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.bookflex.common.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qnas")
public class QnaController {

    private final QnaService qnaService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<QnaResponseDto> createQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody @Valid QnaRequestDto requestDto) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(QNA_CREATE_NOT_ALLOWED);
        }

        QnaResponseDto responseDto = qnaService.createQna(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaResponseDto> getSingleQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable long qnaId) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());

        QnaResponseDto responseDto = qnaService.getSingleQna(qnaId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<QnaResponseDto>> getUserQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(QNA_VIEW_NOT_ALLOWED);
        }

        Page<QnaResponseDto> responseList = qnaService.getUserQnas(user, page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<QnaResponseDto>> getAllQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(QNA_VIEW_NOT_ALLOWED);
        }

        Page<QnaResponseDto> responseList = qnaService.getAllQnas(page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @DeleteMapping("/{qnaId}")
    public ResponseEntity deleteQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @PathVariable long qnaId) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED);
        }

        qnaService.deleteQna(user, qnaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/{qnaId}")
    public ResponseEntity deleteQnaAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable long qnaId) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED);
        }

        qnaService.deleteQnaAdmin(qnaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/admin/{qnaId}")
    public ResponseEntity<QnaResponseDto> createQnaReply(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable long qnaId,
                                                         @RequestBody @Valid ReplyRequestDto requestDto) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(REPLY_CREATE_NOT_ALLOWED);
        }

        QnaResponseDto responseDto = qnaService.createQnaReply(requestDto, qnaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("/types")
    public ResponseEntity<List<String>> getQnaTypes() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Arrays.stream(QnaTypeCode.values())
                        .map(QnaTypeCode::getTypeName)
                        .collect(Collectors.toList()));
    }
}
