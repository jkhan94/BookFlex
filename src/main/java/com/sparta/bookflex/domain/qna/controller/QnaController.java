package com.sparta.bookflex.domain.qna.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.qna.dto.QnaRequestDto;
import com.sparta.bookflex.domain.qna.dto.QnaResponseDto;
import com.sparta.bookflex.domain.qna.dto.ReplyRequestDto;
import com.sparta.bookflex.domain.qna.service.QnaService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.enums.RoleType;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qnas")
public class QnaController {

    private final QnaService qnaService;
    private final AuthService authService;

    @PostMapping
    @Envelop("문의가 접수되었습니다.")
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
    @Envelop("문의를 조회했습니다.")
    public ResponseEntity<QnaResponseDto> getSingleQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable long qnaId) {

//        User user = authService.findByUserName(userDetails.getUser().getUsername());

        QnaResponseDto responseDto = qnaService.getSingleQna(qnaId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping
    @Envelop("문의를 조회했습니다.")
    public ResponseEntity<List<QnaResponseDto>> getUserQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.USER) {
            throw new BusinessException(QNA_VIEW_NOT_ALLOWED);
        }

        List<QnaResponseDto> responseList = qnaService.getUserQnas(user, page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }


    @GetMapping("/admin")
    @Envelop("문의를 조회했습니다.")
    public ResponseEntity<List<QnaResponseDto>> getAllQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(QNA_VIEW_NOT_ALLOWED);
        }

        List<QnaResponseDto> responseList = qnaService.getAllQnas(page - 1, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @DeleteMapping("/{qnaId}")
    @Envelop("문의를 삭제했습니다.")
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
    @Envelop("문의를 삭제했습니다.")
    public ResponseEntity deleteQnaAdmin(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable long qnaId) {

        User user = authService.findByUserName(userDetails.getUser().getUsername());
        if (user.getAuth() != RoleType.ADMIN) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED);
        }

        qnaService.deleteQnaAdmin(qnaId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /*
        문의 답변 등록
        POST / anwers/{qnaId}

        요청DTO
        reply	문의 답변 내용

        응답DTO
        statusCode	201
        message	“답변을 등록했습니다.”
        data	qnaType	문의 유형
                inquiry	문의 내용
                createAt	작성일자
                reply	답변
     */
    @PostMapping("/admin/{qnaId}")
    @Envelop("답변을 등록했습니다.")
    public ResponseEntity<QnaResponseDto> createQnaReply(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @PathVariable long qnaId,
                                                         @RequestBody @Valid ReplyRequestDto requestDto) {

/*
    User user = authService.findByUserName(userDetails.getUser().getUsername());
    if (user.getAuth() != UserRole.ADMIN) {
        throw new BusinessException(QNA_CREATE_NOT_ALLOWED);
    }
*/

        QnaResponseDto responseDto = qnaService.createQnaReply(requestDto, qnaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}