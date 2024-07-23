package com.sparta.bookflex.domain.photoimage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.bookflex.domain.photoimage.entity.PhotoImage;
import com.sparta.bookflex.domain.photoimage.repository.PhotoImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoImageService {
    private final PhotoImageRepository photoImageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${upload.path}")
    private String uploadpath;


    public PhotoImage savePhotoImage(MultipartFile multipartFile) throws IOException {

        String originalFilename = multipartFile.getOriginalFilename();

        String saveFileName = createSaveFileName(originalFilename);

        String savePath = getFullPath(saveFileName);

        int fileSize = (int) multipartFile.getSize();

        File file = multiPartFileToFile(multipartFile);

        amazonS3.putObject(new PutObjectRequest(bucketName, savePath, file));

        return photoImageRepository.save(PhotoImage.builder()
                .fileName(saveFileName)
                .filePath(savePath)
                .fileSize(fileSize)
                .build());
    }

    public String getPhotoImageUrl(String path) {
        return amazonS3.getUrl(bucketName, path).toString();
    }

    @Transactional
    public PhotoImage updatePhotoImage(MultipartFile multipartFile, Long bookId) throws IOException {

        PhotoImage photoImage = deletePhotoImageByBookId(bookId);

        String originalFilename = multipartFile.getOriginalFilename();

        String saveFileName = createSaveFileName(originalFilename);

        String savePath = getFullPath(saveFileName);

        int fileSize = (int) multipartFile.getSize();

        File file = multiPartFileToFile(multipartFile);

        amazonS3.putObject(new PutObjectRequest(bucketName, savePath, file));

        photoImage.update(saveFileName, savePath, fileSize);

        return photoImage;
    }

    public PhotoImage deletePhotoImageByBookId(Long bookId) {

        PhotoImage photoImage = photoImageRepository.findByBookId(bookId);

        amazonS3.deleteObject(bucketName, photoImage.getFilePath());

        return photoImage;
    }

    private String createSaveFileName(String originalFilename) {

        String ext = extractExt(originalFilename);

        String uuid = UUID.randomUUID().toString();

        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {

        int pos = originalFilename.lastIndexOf(".");

        return originalFilename.substring(pos + 1);
    }

    private String getFullPath(String filename) {

        return uploadpath + filename;
    }

    private File multiPartFileToFile(MultipartFile file) throws IOException {

        File convertedFile = new File(file.getOriginalFilename());

        try (FileOutputStream fileOutputStream = new FileOutputStream(convertedFile)) {
            fileOutputStream.write(file.getBytes());
        }

        return convertedFile;
    }


}
