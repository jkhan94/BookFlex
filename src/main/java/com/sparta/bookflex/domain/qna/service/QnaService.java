package com.sparta.bookflex.domain.qna.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.domain.qna.dto.QnaRequestDto;
import com.sparta.bookflex.domain.qna.dto.QnaResponseDto;
import com.sparta.bookflex.domain.qna.dto.ReplyRequestDto;
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
import static com.sparta.bookflex.domain.qna.entity.Qna.toQnaResponseDto;

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

        return toQnaResponseDto(qna);
    }

    @Transactional(readOnly = true)
    public Page<QnaResponseDto> getUserQnas(User user, int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        return qnaRepository.findAllByUserId(user.getId(), pageable).map(Qna::toQnaResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<QnaResponseDto> getAllQnas(int page, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        return qnaRepository.findAll(pageable).map(Qna::toQnaResponseDto);
    }

    @Transactional
    public void deleteQna(User user, long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BusinessException(QNA_NOT_FOUND)
        );

        if (user.getId() != qna.getUser().getId()) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED);
        }

        if (!qna.getReply().equals(WAITING_FOR_REPLY)) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED_REPLY_EXISTS);
        }

        qnaRepository.delete(qna);
    }

    @Transactional
    public void deleteQnaAdmin(long qnaId) {
        Qna qna = qnaRepository.findById(qnaId).orElseThrow(
                () -> new BusinessException(QNA_NOT_FOUND)
        );

        if (!qna.getReply().equals(WAITING_FOR_REPLY)) {
            throw new BusinessException(QNA_DELETE_NOT_ALLOWED_REPLY_EXISTS);
        }

        qnaRepository.delete(qna);
    }

    @Transactional
    public QnaResponseDto createQnaReply(ReplyRequestDto requestDto, long qnaId) {
        Qna qna = qnaRepository.findByIdWithLock(qnaId).orElseThrow(
                () -> new BusinessException(QNA_NOT_FOUND)
        );

        if (!qna.getReply().equals(WAITING_FOR_REPLY)) {
            throw new BusinessException(REPLY_ALREADY_EXISTS);
        }

        qna.updateReply(qna, requestDto);
        qnaRepository.save(qna);

        return toQnaResponseDto(qna);
    }
}
