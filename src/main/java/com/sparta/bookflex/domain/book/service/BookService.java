package com.sparta.bookflex.domain.book.service;

import com.sparta.bookflex.common.exception.BusinessException;
import com.sparta.bookflex.common.exception.ErrorCode;
import com.sparta.bookflex.domain.book.dto.BookRequestDto;
import com.sparta.bookflex.domain.book.dto.BookResponseDto;
import com.sparta.bookflex.domain.book.entity.Book;
import com.sparta.bookflex.domain.book.entity.BookStatus;
import com.sparta.bookflex.domain.book.repository.BookCustomRepositoryImpl;
import com.sparta.bookflex.domain.book.repository.BookRepository;
import com.sparta.bookflex.domain.category.enums.Category;
import com.sparta.bookflex.domain.category.service.CategoryService;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.photoimage.service.PhotoImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final PhotoImageService photoImageService;
    private final BookCustomRepositoryImpl bookCustomRepositoryImpl;


    @Transactional
    public BookResponseDto registerProduct(BookRequestDto bookRequestDto,
                                           MultipartFile multipartFile) throws IOException {

        PhotoImage photoImage = photoImageService.savePhotoImage(multipartFile);

        Book book = bookRequestDto.toEntity(photoImage);

        book = bookRepository.save(book);

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
    public List<BookResponseDto> getBookList(int page,
                                             int size,
                                             boolean isAsc,
                                             String sortBy,
                                             BookStatus bookStatus,
                                             String bookName) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortBy);

        Pageable pageble = PageRequest.of(page - 1, size, sort);

        return bookCustomRepositoryImpl.findBooks(bookName, bookStatus, pageble).stream()
                .map(book -> book.toResponseDto(photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath())))
                .toList();
    }

    @Transactional
    public BookResponseDto modifyBookInfo(Long bookId,
                                          BookRequestDto bookRequestDto,
                                          MultipartFile multipartFile) throws IOException {

        Book book = getBookByBookId(bookId);

        PhotoImage photoImage = photoImageService.updatePhotoImage(multipartFile, book.getId());

        book.update(bookRequestDto);

        String photoImageUrl = photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath());

        book.checkStock();

        return book.toResponseDto(photoImageUrl);
    }


    public String deleteBook(Long bookId) {

        Book book = getBookByBookId(bookId);

        String bookName = book.getBookName();

        photoImageService.deletePhotoImageByBookId(book.getId());

        bookRepository.delete(book);

        return bookName;
    }

    public Book getBookByBookId(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND));

        return book;
    }

    @Transactional
    public void decreaseStock(Long bookId, int quantity) {

        Book book = getBookByBookId(bookId);

        book.decreaseStock(quantity);
    }

    @Transactional
    public void increaseStock(Long bookId, int quantity) {

        Book book = getBookByBookId(bookId);

        book.increaseStock(quantity);
    }


    @Transactional
    public Page<BookResponseDto> getBooksBySubCategory(String categoryName , Pageable pageable) {

        if (categoryName == null) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id"));
        }

        Category category = categoryService.getCategoryByCategoryName(categoryName);

        Page<Book> bookPage = bookRepository.findBySubCategory(category, pageable);

        if (bookPage.isEmpty()) {
            throw new BusinessException(ErrorCode.BOOK_NOT_FOUND);
        }


        List<BookResponseDto> bookResponseDtos = bookPage.stream()
                .filter(book -> !book.getStatus().equals(BookStatus.SOLDOUT))
                .map(book -> {
                    String photoImageUrl = book.getPhotoImage() != null ? photoImageService.getPhotoImageUrl(book.getPhotoImage().getFilePath()) : null;
                    return book.toResponseDto(photoImageUrl);
                })
                .collect(Collectors.toList());

        return new PageImpl<>(bookResponseDtos, pageable, bookPage.getTotalElements());

    }
}



