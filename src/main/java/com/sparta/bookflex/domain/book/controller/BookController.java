package com.sparta.bookflex.domain.book.controller;

import com.sparta.bookflex.common.dto.CommonDto;
import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.common.security.UserDetailsImpl;
import com.sparta.bookflex.domain.book.dto.BookRequestDto;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.book.service.BookService;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.sale.dto.SaleResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<CommonDto<BookResponseDto>> registerProduct(@RequestPart(value = "request") @Valid BookRequestDto bookRequestDto,
                                                                      @RequestPart(value = "multipartFile") MultipartFile multipartFile
    ) throws IOException {

        BookResponseDto bookResponseDto = bookService.registerProduct(bookRequestDto, multipartFile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CommonDto<>(HttpStatus.CREATED.value(), "상품 등록에 성공하였습니다.", bookResponseDto));

    }

    @GetMapping("/{productId}")
    public ResponseEntity<CommonDto<BookResponseDto>> getBookById(@PathVariable(value = "productId") Long bookId) {

        BookResponseDto bookResponseDto = bookService.getBookById(bookId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품 상세 조회에 성공하였습니다.", bookResponseDto));
    }

    @GetMapping
    public ResponseEntity<CommonDto<List<BookResponseDto>>> getBookList(@RequestParam(name = "page") int page,
                                                                        @RequestParam(name = "size") int size,
                                                                        @RequestParam(name = "direction") boolean isAsc,
                                                                        @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                                        @RequestParam(name = "status", required = false) BookStatus status,
                                                                        @RequestParam(name = "bookName", required = false) String bookName
    ) {

        List<BookResponseDto> bookResponseDtoList = bookService.getBookList(page, size, isAsc, sortBy, status, bookName);

        if (bookResponseDtoList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new CommonDto<>(HttpStatus.OK.value(), "등록된 상품이 존재하지 않습니다.", bookResponseDtoList));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품 조회에 성공하였습니다.", bookResponseDtoList));
    }

    @PutMapping("/{booksId}")
    public ResponseEntity<CommonDto<BookResponseDto>> modifyBookInfo(@PathVariable(value = "booksId") Long bookId,
                                                                     @RequestPart(value = "request") @Valid BookRequestDto bookRequestDto,
                                                                     @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile) throws IOException {

        BookResponseDto bookResponseDto = bookService.modifyBookInfo(bookId, bookRequestDto, multipartFile);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품 정보 수정에 성공하였습니다.", bookResponseDto));
    }

    @DeleteMapping("/{booksId}")
    public ResponseEntity<CommonDto<String>> DeleteBook(@PathVariable(value = "productId") Long bookId) {

        String bookName = bookService.deleteBook(bookId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.NO_CONTENT.value(), "상품 삭제에 성공하였습니다.", bookName + " 을 상품 목록에서 삭제하였습니다"));
    }

    @PutMapping("/{bookId}/decreaseStock/quantity/{quantity}")
    public void decreaseStock(@PathVariable Long bookId, @PathVariable int quantity) {

        bookService.decreaseStock(bookId, quantity);
    }

    @PutMapping("/{bookId}/increaseStock/quantity/{quantity}")
    public void increaseStock(@PathVariable Long bookId, @PathVariable int quantity) {

        bookService.increaseStock(bookId, quantity);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<CommonDto<Page<BookResponseDto>>> getBooksByCategory(
            @PathVariable (name = "category") String categoryName,
            Pageable pageable) {

        Page<BookResponseDto> bookPage = bookService.getBooksBySubCategory(categoryName, pageable);

        if (bookPage.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CommonDto<>(HttpStatus.OK.value(), "상품 조회에 성공하였습니다.", bookPage));
    }




}
