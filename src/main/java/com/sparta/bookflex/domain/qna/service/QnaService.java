package com.sparta.bookflex.domain.qna.service;

import com.sparta.bookflex.domain.qna.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
}
