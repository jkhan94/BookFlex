/*
package com.sparta.bookflex.domain.basket.service;

import com.sparta.bookflex.domain.basket.dto.BasketCreateReqDto;
import com.sparta.bookflex.domain.basket.dto.BasketResDto;
import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.basket.repository.BasketRepository;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import com.sparta.bookflex.domain.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    private BasketRepository basketRepository;
    private AuthService authService;
    private BookService bookService;

    @Autowired
    public BasketService(BasketRepository basketRepository, AuthService authService, BookService bookService) {
        this.basketRepository = basketRepository;
        this.authService = authService;
        this.bookService = bookService;
    }
    private User getUser(String username) {
        return authService.findByUserName(username);
    }

    private Book getBook(Long bookId) {
        return bookService.getBookByBookId(bookId);
    }

    private boolean isBookExist(Long bookId) {
        return bookService.getBookByBookId(bookId) != null;
    }

    private boolean isUserExist(String username) {
        return authService.findByUserName(username) != null;

    }

    private boolean isBasketExist(Long basketId) {
        return basketRepository.existsById(basketId);
    }

    private boolean isBasketExistByBookIdAndUserId(Long bookId, Long userId) {
        return basketRepository.existsByBookIdAndUserId(bookId, userId);
    }



    public void createBasket(BasketCreateReqDto basketCreateDto, User user) {
        User selectedUser = getUser(user.getUsername());
        Book book = getBook(basketCreateDto.getBookId());

        if(!isBookExist(basketCreateDto.getBookId())) {
            throw new IllegalArgumentException("책이 존재하지 않습니다.");
        }

        if(isBasketExistByBookIdAndUserId(book.getId(), selectedUser.getId())) {
            throw new IllegalArgumentException("이미 장바구니에 책이 존재합니다.");
        }

        Basket basket = Basket.builder()
                .user(selectedUser)
                .build();

        basketRepository.save(basket);
    }

    public BasketResDto getBasket(User user,Long bookId) {
        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        Book book = getBook(bookId);

        Optional<Basket> basket = basketRepository.findByUserIdAndBookId(user.getId(), bookId);
        if(basket.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
        }


        return BasketResDto.builder()
                .baseketId(basket.get().getId())
                .bookName(book.getBookName())
                .price(book.getPrice())

                .build();

    }

    public Page<BasketResDto> getBaskets(User user, Pageable pageable) {
        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        Page<Basket> baskets = basketRepository.findAllByUserId(user.getId(),pageable);

        if(baskets.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
        }

        return baskets.map(basket -> BasketResDto.builder()
                .baseketId(basket.getId())

                .build());
    }

    public BasketResDto updateBasket(Long basketId, int quantity, User user) {

        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }



        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new IllegalArgumentException("장바구니에 책이 존재하지 않습니다."));

        basketRepository.save(basket);
        return BasketResDto.builder()
                .baseketId(basket.getId())

                .build();
    }

    public void deleteBasket(Long basketId, User user) {

        if(Boolean.FALSE.equals(isUserExist(user.getUsername()))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        if(Boolean.FALSE.equals(isBasketExist(basketId))) {
            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
        }

        basketRepository.deleteById(basketId);
    }







}
*/
