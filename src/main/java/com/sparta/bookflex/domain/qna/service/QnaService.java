package com.sparta.bookflex.domain.qna.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.qna.dto.QnaRequestDto;
import com.sparta.bookflex.domain.qna.dto.QnaResponseDto;
import com.sparta.bookflex.domain.qna.entity.Qna;
import com.sparta.bookflex.domain.qna.repository.QnaRepository;
import com.sparta.bookflex.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.bookflex.common.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class QnaService {

    private static final int PAGE_SIZE = 5;
    private static final String WAITING_FOR_REPLY = "답변대기";

    private final QnaRepository qnaRepository;

    @Transactional
    public QnaResponseDto createQna(QnaRequestDto requestDto, User user) {
        Qna qna = Qna.builder()
                .email(requestDto.getEmail())
                .qnaType(requestDto.getQnaType())
                .inquiry(requestDto.getInquiry())
                .reply(WAITING_FOR_REPLY)
                .user(user)
                .build();

        qnaRepository.save(qna);

        return QnaResponseDto.builder()
                .qnaType(qna.getQnaType())
                .inquiry(qna.getInquiry())
                .createdAt(qna.getCreatedAt())
                .reply(qna.getReply())
                .build();
    }

    // User: userId로 조회되는 것만
    @Transactional(readOnly = true)
    public List<QnaResponseDto> getUserQnas(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<QnaResponseDto> qnaPage = qnaRepository.findAllByUserId(user.getId(), pageable).map(
                qna -> QnaResponseDto.builder()
                        .qnaType(qna.getQnaType())
                        .inquiry(qna.getInquiry())
                        .createdAt(qna.getCreatedAt())
                        .reply(qna.getReply())
                        .build()
        );
        return qnaPage.getContent();
    }

    //        ADMIN : findAll
    @Transactional(readOnly = true)
    public List<QnaResponseDto> getAllQnas(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        Page<QnaResponseDto> qnaPage = qnaRepository.findAll(pageable).map(
                qna -> QnaResponseDto.builder()
                        .qnaType(qna.getQnaType())
                        .inquiry(qna.getInquiry())
                        .createdAt(qna.getCreatedAt())
                        .reply(qna.getReply())
                        .build()
        );

        return qnaPage.getContent();
    }

    @Transactional
    public void deleteQna(User user, long qnaId) {
        //        없는 문의 삭제 불가
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BusinessException(QNA_NOT_FOUND)
        );

        // USER는 본인이 작성한 문의만 삭제 가능
        if (user.getId() != qna.getUser().getId()) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED);
        }

        // reply가 있으면 삭제 불가
        if (!qna.getReply().equals(WAITING_FOR_REPLY)) {
            throw new BusinessException(REPLY_EXISTS);
        }

        qnaRepository.delete(qna);
    }

    @Transactional
    public void deleteQnaAdmin(long qnaId) {
        //        없는 문의 삭제 불가
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BusinessException(QNA_NOT_FOUND)
        );

        //        reply가 있으면 삭제 불가
        if (!qna.getReply().equals(WAITING_FOR_REPLY)) {
            throw new BusinessException(REPLY_EXISTS);
        }

        qnaRepository.delete(qna);
    }
}
