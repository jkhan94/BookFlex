package com.sparta.bookflex.domain.book.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.book.dto.BookRequestDto;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.category.entity.Category;
import com.sparta.bookflex.domain.category.service.CategoryService;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final PhotoImageService photoImageService;


    @Transactional
    public BookResponseDto registerProduct(BookRequestDto bookRequestDto, MultipartFile multipartFile) throws IOException {

        PhotoImage photoImage = photoImageService.savePhotoImage(multipartFile);

        Category category = categoryService.getCategoryByCategoryName(bookRequestDto.getCategory());

        Book book = bookRequestDto.toEntity(photoImage, category);

        book = bookRepository.save(book);

        category.getBookList().add(book);

        photoImage.updateBookId(book.getId());

        String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());

        return book.toResponseDto(photoImageUrl);
    }

    @Transactional
    public BookResponseDto getBookById(Long bookId) {

        Book book = bookRepository
                .findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());

        return book.toResponseDto(photoImageUrl);
    }

    @Transactional
    public List<BookResponseDto> getBooksByBookName(String bookName) {

        List<Book> bookList = bookRepository
                .findByBookName(bookName);

        List<BookResponseDto> bookResponseDtoList = new ArrayList<>();

        for (Book book : bookList) {
            String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());
            bookResponseDtoList.add(book.toResponseDto(photoImageUrl));
        }

        return bookResponseDtoList;
    }

    @Transactional
    public List<BookResponseDto> getBookList() {

        List<Book> bookList = bookRepository.findAll();

        List<BookResponseDto> bookResponseDtoList = new ArrayList<>();

        for (Book book : bookList) {
            String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());
            bookResponseDtoList.add(book.toResponseDto(photoImageUrl));
        }

        return bookResponseDtoList;
    }

    @Transactional
    public BookResponseDto modifyBookInfo(Long bookId,
                                          BookRequestDto bookRequestDto,
                                          MultipartFile multipartFile) throws IOException {

        Book book = bookRepository
                .findById(bookId).orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        PhotoImage photoImage = photoImageService.updatePhotoImage(multipartFile, book.getId());

        book.update(bookRequestDto);

        String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());

        return book.toResponseDto(photoImageUrl);
    }

    public String deleteBook(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        String bookName = book.getBookName();

        photoImageService.deletePhotoImageByBookId(book.getId());

        bookRepository.delete(book);

        return bookName;
    }

    public boolean isExistBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));
        return true;
    }

    public Book getBookByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));
        return book;
    }

}
