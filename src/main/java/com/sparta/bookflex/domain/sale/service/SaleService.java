package com.sparta.bookflex.domain.sale.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.sale.dto.SaleCreateReqDto;
import com.sparta.bookflex.domain.sale.dto.SaleResDto;
import com.sparta.bookflex.domain.sale.entity.Sale;
import com.sparta.bookflex.domain.sale.repository.SaleRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public SaleService(SaleRepository saleRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.saleRepository = saleRepository;
    }

    public void createSale(SaleCreateReqDto saleCreateReqDto, Long userId) {
        User user = getUser(userId);
        Book book = getBook(saleCreateReqDto.getBookId());
        Sale sale = Sale.builder()
                .quantity(saleCreateReqDto.getQuantity())
                .book(book)
                .user(user)
                .build();
        saleRepository.save(sale);
    }


    public SaleResDto getSale(Long saleId, Long userId) {
        if(Boolean.FALSE.equals(existUser(userId))) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        Sale sale = getSale(saleId);
        return SaleResDto.builder()
                .saleId(sale.getId())
                .bookName(sale.getBook().getBookName())
                .price(sale.getBook().getPrice())
                .quantity(sale.getQuantity())
                .status(sale.getStatus())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    public void deleteSale(Long saleId, Long userId) {
        if(Boolean.FALSE.equals(existSale(saleId))) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }
        if(Boolean.FALSE.equals(existUser(userId))) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        Sale sale = getSale(saleId);
        saleRepository.delete(sale);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다."));
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new IllegalArgumentException("책이 없습니다."));
    }

    private Sale getSale(Long saleId) {
        return saleRepository.findById(saleId).orElseThrow(
                () -> new IllegalArgumentException("상품이 없습니다."));
    }

    private Boolean existSale(Long saleId) {
        return saleRepository.existsById(saleId);
    }

    private Boolean existBook(Long bookId) {
        return bookRepository.existsById(bookId);
    }

    private Boolean existUser(Long userId) {
        return userRepository.existsById(userId);
    }


}
