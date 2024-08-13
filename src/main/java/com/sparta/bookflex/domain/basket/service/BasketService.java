package com.sparta.bookflex.domain.basket.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.basket.dto.BasketCreateReqestDto;
import com.sparta.bookflex.domain.basket.dto.BasketItemResponseDto;
import com.sparta.bookflex.domain.basket.entity.Basket;
import com.sparta.bookflex.domain.basket.entity.BasketItem;
import com.sparta.bookflex.domain.basket.repository.BasketItemRepository;
import com.sparta.bookflex.domain.basket.repository.BasketRepository;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import com.sparta.bookflex.domain.user.entity.User;
import com.sparta.bookflex.domain.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BasketService {

    private BasketRepository basketRepository;
    private BasketItemRepository basketItemRepository;
    private AuthService authService;
    private BookService bookService;

    private PhotoImageService photoImageService;

    @Autowired
    public BasketService(BasketRepository basketRepository, AuthService authService, BookService bookService,
                         PhotoImageService photoImageService, BasketItemRepository basketItemRepository) {
        this.basketItemRepository = basketItemRepository;
        this.photoImageService = photoImageService;
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

    private boolean isBasketExistByBookIdAndUserId(Long bookId, Long basketId) {
        return basketItemRepository.existsByBookIdAndBasketId(bookId, basketId);
    }

    private boolean isBasketExist(Long basketId, User user) {
        return basketRepository.existsByIdAndUser(basketId, user);
    }


    private Basket getOrCreateBasket(User user) {
        return basketRepository.findByUser(user)
                .orElseGet(() -> {
                    Basket newBasket = Basket.builder()
                            .user(user)
                            .build();
                    return basketRepository.save(newBasket);
                });
    }



    @Transactional
    public void createBasketItem(BasketCreateReqestDto basketCreateDto, User user) {
        User selectedUser = getUser(user.getUsername());
        Book book = getBook(basketCreateDto.getBookId());

        if(!isBookExist(basketCreateDto.getBookId())) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }

        if(isBasketExistByBookIdAndUserId(book.getId(), selectedUser.getBasket().getId())) {
            updateBasketById(book.getId(),basketCreateDto,selectedUser);
            //throw new BusinessException(ErrorCode.BASKET_ITEM_ALREADY_EXIST);
        }
        else {

            Basket basket = getOrCreateBasket(selectedUser);

            BasketItem basketItem = BasketItem.builder()
                .book(book)
                .basket(basket)
                .quantity(basketCreateDto.getQuantity())
                .price(book.getPrice())
                .build();

            basket.addBasketItem(basketItem);
            basketItemRepository.save(basketItem);
        }
    }

//    public BasketResDto getBasket(User user,Long bookId) {
//        if(!isUserExist(user.getUsername())) {
//            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
//        }
//
//        Book book = getBook(bookId);
//
//        Optional<Basket> basket = basketRepository.findByUserIdAndBookId(user.getId(), bookId);
//        if(basket.isEmpty()) {
//            throw new IllegalArgumentException("장바구니에 책이 존재하지 않습니다.");
//        }
//
//
//        return BasketResDto.builder()
//                .baseketId(basket.get().getId())
//                .bookName(book.getBookName())
//                .price(book.getPrice())
//
//                .build();
//
//    }

    @Transactional(readOnly=true)
    public Page<BasketItemResponseDto> getBasketItems(User user, Pageable pageable) {
        User selectedUser = getUser(user.getUsername());
        Long basketId = selectedUser.getBasket().getId();

        if (!isBasketExist(basketId, user)) {
            throw new BusinessException(ErrorCode.BASKET_NOT_FOUND);
        }

        Page<BasketItem> basketItems = basketItemRepository.findAllByBasketId(basketId,  pageable);

        if(basketItems.isEmpty()) {
            throw new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND);
        }

        return basketItems.map(basketItem -> BasketItemResponseDto.builder()
                .baskeItemid(basketItem.getId())
                .bookId(basketItem.getBook().getId())
                .quantity(basketItem.getQuantity())
                .price(basketItem.getPrice())
                .bookName(basketItem.getBook().getBookName())
                .photoImagePath(photoImageService.getPhotoImageUrl(basketItem.getBook().getPhotoImage().getFilePath()))
                .build()
        );
    }

    @Transactional
    public BasketItemResponseDto updateBasket(Long basketItemId, int quantity, User user) {

        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        BasketItem basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND));

        basketItem.updateQuantity(quantity);
        basketItemRepository.save(basketItem);
        return BasketItemResponseDto.builder()
                .bookId(basketItem.getBook().getId())
                .quantity(quantity)
                .bookName(basketItem.getBook().getBookName())
                .photoImagePath(photoImageService.getPhotoImageUrl(basketItem.getBook().getPhotoImage().getFilePath()))
                .build();
    }

    @Transactional
    public void deleteBasketItem(Long basketItemId, User user) {

        if(!isUserExist(user.getUsername())) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Optional<Basket> basket = basketRepository.findByUserId(user.getId());
        if(basket.isEmpty()) {
            throw new BusinessException(ErrorCode.BASKET_NOT_FOUND);
        }

        if(!isBasketItemExistByBasKetItemIdAndBasketId(basketItemId, basket.get().getId())) {
            throw new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND);
        }

        basketItemRepository.deleteById(basketItemId);
    }

    private boolean isBasketItemExistByBasKetItemIdAndBasketId(Long basketItemId, Long basketId) {
        return basketItemRepository.existsByIdAndBasketId(basketItemId, basketId);
    }


    @Transactional
    public void updateBasketById(Long bookId, BasketCreateReqestDto basketCreateDto, User user) {

        if(!isUserExist(user.getUsername())) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        BasketItem basketItem = basketItemRepository.findByBookId(bookId)
            .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND));

        basketItem.updateQuantity(basketCreateDto.getQuantity());
        basketItemRepository.save(basketItem);
    }

    @Transactional
    public void deleteBasketItemByBookId(Long bookId, User user) {


    }

    @Transactional
    public void clear(List<Long> bookIds, User user) {
        Basket basket = basketRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_NOT_FOUND));
        for(Long bookId : bookIds) {
            BasketItem basketItem = basketItemRepository.findByBasketIdAndBookId(basket.getId(), bookId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.BASKET_ITEM_NOT_FOUND));
            basketItemRepository.delete(basketItem);
        }

    }
}
