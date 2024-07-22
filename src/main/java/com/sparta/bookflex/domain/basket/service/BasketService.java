package com.sparta.bookflex.domain.basket.service;

import com.sparta.bookflex.domain.basket.dto.BasketCreateReqDto;
import com.sparta.bookflex.domain.basket.dto.BasketResDto;
import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.basket.repository.BasketRepository;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    private BasketRepository basketRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    public BasketService(BasketRepository basketRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


    public void createBasket(BasketCreateReqDto basketCreateDto, Long userId) {
        User user = getUser(userId);
        Book book = getBook(basketCreateDto.getBookId());

        Basket basket = Basket.builder()
                .quantity(basketCreateDto.getQuantity())
                .user(user)
                .book(book)
                .build();

        basketRepository.save(basket);
    }

    public List<BasketResDto> getBaskets(Long userId) {
        if(Boolean.FALSE.equals(isUserExist(userId))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        List<Basket> baskets = basketRepository.findAllByUserId(userId);

        if(baskets.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
        }

        List<BasketResDto> basketResDtos = new ArrayList<>();

        for(Basket basket : baskets) {
            BasketResDto basketResDto = BasketResDto.builder()
                    .baseketId(basket.getId())
                    .bookName(basket.getBook().getBookName())
                    .price(basket.getBook().getPrice())
                    .quantity(basket.getQuantity())
                    .build();
            basketResDtos.add(basketResDto);
        }
        return basketResDtos;
    }

    public BasketResDto updateBasket(Long basketId, int quantity, Long userId) {

        if(Boolean.FALSE.equals(isUserExist(userId))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        Basket basket = basketRepository.findById(basketId).orElseThrow(() -> new IllegalArgumentException("장바구니에 책이 존재하지 않습니다."));
        basket.updateQuantity(quantity);
        basketRepository.save(basket);
        return BasketResDto.builder()
                .baseketId(basket.getId())
                .bookName(basket.getBook().getBookName())
                .price(basket.getBook().getPrice())
                .quantity(basket.getQuantity())
                .build();
    }

    public void deleteBasket(Long basketId, Long userId) {

        if(Boolean.FALSE.equals(isUserExist(userId))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        if(Boolean.FALSE.equals(isBasketExist(basketId))) {
            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
        }

        basketRepository.deleteById(basketId);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    private Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("책이 존재하지 않습니다."));
    }

    private Boolean isBookExist(Long bookId) {
        return bookRepository.existsById(bookId);
    }

    private Boolean isUserExist(Long userId) {
        return userRepository.existsById(userId);
    }

    private Boolean isBasketExist(Long basketId) {
        return basketRepository.existsById(basketId);
    }



}
