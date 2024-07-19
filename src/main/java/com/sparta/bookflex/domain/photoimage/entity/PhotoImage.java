package com.sparta.bookflex.domain.photoimage.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoImage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fineName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private int fileSize;

    @Column(nullable = false)
    private Long bookId;

    public PhotoImage(String fineName, String filePath, int fileSize, Book book) {
        this.fineName = fineName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.bookId = book.getId();
    }
}
