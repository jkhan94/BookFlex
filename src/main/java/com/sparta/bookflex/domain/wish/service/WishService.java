package com.sparta.bookflex.domain.wish.service;

import com.sparta.bookflex.domain.auth.service.AuthService;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.wish.dto.WishResDto;
import com.sparta.bookflex.domain.wish.entity.Wish;
import com.sparta.bookflex.domain.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final BookService bookService;
    private final AuthService authService;
    private final PhotoImageService photoImageService;



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

    private boolean isWishExist(Long wishId) {
        return wishRepository.existsById(wishId);
    }

    public void createWish(Long bookId,User user) {

        User selectedUser = getUser(user.getUsername());
        Book book = getBook(bookId);

        if (wishRepository.existsByUserAndBook(selectedUser, book)) {
            throw new IllegalArgumentException("이미 해당 책을 위시리스트에 추가했습니다.");
        }


        Wish wish = Wish.builder()
                .user(selectedUser)
                .book(book)
                .build();
        wishRepository.save(wish);
    }

    @Transactional
    public Page<WishResDto> getWishList(User user, Pageable pageable) {
        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        Page<Wish> wishPage = wishRepository.findAllByUserId(user.getId(), pageable);
        List<WishResDto> wishResDtoList = wishPage.getContent().stream()
                .map(wish -> WishResDto.builder()
                        .wishId(wish.getId())
                        .bookName(wish.getBook().getBookName())
                        .bookId(wish.getBook().getId())
                        .photoUrl(photoImageService.getPhotoImageUrl(wish.getBook().getPhotoImage().getFilePath()))
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(wishResDtoList, pageable, wishPage.getTotalElements());

    }

    public void deleteWish(Long wishId, User user) {
        if(Boolean.FALSE.equals(isUserExist(user.getUsername()))) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        if(Boolean.FALSE.equals(isWishExist(wishId))) {
            throw new IllegalArgumentException("위시리스트가 존재하지 않습니다.");
        }
        wishRepository.deleteById(wishId);
    }
}
