package com.sparta.bookflex.domain.wish.service;

import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.user.repository.UserRepository;
import com.sparta.bookflex.domain.wish.dto.WishResDto;
import com.sparta.bookflex.domain.wish.entity.Wish;
import com.sparta.bookflex.domain.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public WishService(WishRepository wishRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
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

    private Boolean isWishExist(Long wishId) {
        return wishRepository.existsById(wishId);
    }

    public void createWish(Long bookId,Long userId) {
        User user = getUser(userId);
        Book book = getBook(bookId);

        Wish wish = Wish.builder()
                .user(user)
                .book(book)
                .build();
        wishRepository.save(wish);
    }

    public List<WishResDto> getWishList(Long userId) {
        if(Boolean.FALSE.equals(isUserExist(userId))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        if(Boolean.FALSE.equals(isWishExist(userId))) {
            throw new IllegalArgumentException("위시리스트가 존재하지 않습니다.");
        }
        List<Wish> wishList = wishRepository.findAllByUserId(userId);
        List<WishResDto> wishResDtoList = new ArrayList<>();

        for(Wish wish : wishList) {
            Book book = wish.getBook();
            wishResDtoList.add(WishResDto.builder()
                    .wishId(wish.getId())
                    .bookName(book.getBookName())
                    .build()
            );
        }
        return wishResDtoList;

    }

    public void deleteWish(Long wishId, Long userId) {
        if(Boolean.FALSE.equals(isUserExist(userId))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        if(Boolean.FALSE.equals(isWishExist(wishId))) {
            throw new IllegalArgumentException("위시리스트가 존재하지 않습니다.");
        }
        wishRepository.deleteById(wishId);
    }
}
