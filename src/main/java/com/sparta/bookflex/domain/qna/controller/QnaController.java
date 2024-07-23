package com.sparta.bookflex.domain.qna.controller;

import com.sparta.bookflex.common.aop.Envelop;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.qna.dto.QnaRequestDto;
import com.sparta.bookflex.domain.qna.dto.QnaResponseDto;
import com.sparta.bookflex.domain.qna.service.QnaService;
import com.sparta.bookflex.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qnas")
public class QnaController {

    private final QnaService qnaService;
    private final AuthService authService;

    /*    고객문의 작성
        POST /qnas

        User 권한만. ADMIN은 불가

        요청DTO
        email 답변 받을 이메일
        qnaType 문의 유형
        inquiry 문의 내용

                응답DTO
        statusCode 201
        message “문의가 접수되었습니다.”
        data	qnaType	문의 유형
                inquiry	문의 내용
                createAt	작성일자
                reply	답변부*/
    @PostMapping
    @Envelop("문의가 접수되었습니다.")
    public ResponseEntity<QnaResponseDto> createQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @RequestBody @Valid QnaRequestDto requestDto) {

        // User 권한만. ADMIN은 불가

    }

    /*    고객문의 조회
        GET /qnas

        page size=5
        User: userId로 조회되는 것만

                응답DTO
        statusCode	200
        message	“문의를 조회했습니다.”
        data	qnaType	문의 유형
                inquiry	문의 내용
                createAt	작성일자
                reply	답변*/
    @GetMapping
    @Envelop("문의를 조회했습니다.")
    public ResponseEntity<List<QnaResponseDto>> getUserQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        // page size=5
        // User: userId로 조회되는 것만
    }


    /*    고객문의 관리자 조회
        GET /qnas/admin

        page size=5
        ADMIN : findAll

                응답DTO
        statusCode	200
        message	“문의를 조회했습니다.”
        data	qnaType	문의 유형
                inquiry	문의 내용
                createAt	작성일자
                reply	답변*/
    @GetMapping("/admin")
    @Envelop("문의를 조회했습니다.")
    public ResponseEntity<List<QnaResponseDto>> getAllQnas(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

//        page size=5
//        ADMIN : findAll
    }

    /*   고객문의 유저 삭제
       DELETE /qnas/{qnaId}

       없는 문의 삭제 불가
       reply가 있으면 삭제 불가
       USER 본인 문의만 삭제 가능

       응답DTO
       statusCode	204
       message	“문의를 삭제했습니다.”*/
    @DeleteMapping("/{qnaId}")
    @Envelop("문의를 삭제했습니다.")
    public ResponseEntity deleteQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @PathVariable long qnaId) {

//        없는 문의 삭제 불가
//        reply가 있으면 삭제 불가
//        USER 본인 문의만 삭제 가능

    }

    /*   고객문의 유저 삭제
       DELETE /qnas/{qnaId}

       없는 문의 삭제 불가
       reply가 있으면 삭제 불가
       ADMIN 모든 문의 삭제 가능

       응답DTO
       statusCode	204
       message	“문의를 삭제했습니다.”*/
    @DeleteMapping("/admin/{qnaId}")
    @Envelop("문의를 삭제했습니다.")
    public ResponseEntity deleteQna(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @PathVariable long qnaId) {

//        없는 문의 삭제 불가
//        reply가 있으면 삭제 불가
//        ADMIN 모든 문의 삭제 가능

    }
}
