package com.sparta.bookflex.domain.photoimage.entity;

import com.sparta.bookflex.common.utill.Timestamped;
import com.sparta.bookflex.domain.book.entity.Book;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoImage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private int fileSize;

    @Column(unique = true)
    private Long bookId = null;

    @Builder
    public PhotoImage(String fileName, String filePath, int fileSize, Long bookId) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void updateBookId(Long id) {
        this.bookId = id;
    }

    public void update(String saveFileName, String savePath, int fileSize) {
        this.fileName = saveFileName;
        this.filePath = savePath;
        this.fileSize = fileSize;
    }
}
