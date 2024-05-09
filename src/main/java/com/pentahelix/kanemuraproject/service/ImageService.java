package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.FileData;
import com.pentahelix.kanemuraproject.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Value("${spring.servlet.multipart.location}")
    private String FOLDER_PATH;

    public String uploadImageToFileSystem(MultipartFile file) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        FileData fileData=fileDataRepository.save(FileData.builder()
                .nameImg(file.getOriginalFilename())
                .type(file.getContentType())
                .filepath(filePath).build());

        file.transferTo(new File(filePath));

        if (fileData != null) {
            return "file uploaded successfully : " + filePath;
        }
        return null;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByNameImg(fileName);
        String filePath=fileData.get().getFilepath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

}
