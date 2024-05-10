package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.FileData;
import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.repository.FileDataRepository;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Value("${spring.servlet.multipart.location}")
    private String FOLDER_PATH;

    public String uploadImageToFileSystem(MultipartFile file, Integer menu_id) throws IOException {
        String filePath=FOLDER_PATH+file.getOriginalFilename();

        Menu menu = menuRepository.findFirstById(menu_id).orElseThrow(() -> new RuntimeException("Menu Not FOund"));
        FileData fileData=fileDataRepository.save(FileData.builder()
                .nameImg(file.getOriginalFilename())
                .type(file.getContentType())
                .filepath(filePath)
                .menu(menu)
                .build());

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

    public byte[] getImageByMenuId(Integer menu_id) throws IOException {
        Optional<FileData> fileDataOptional = fileDataRepository.findByMenuId(menu_id);
        if (fileDataOptional.isPresent()) {
            FileData fileData = fileDataOptional.get();
            String filePath = fileData.getFilepath();
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new FileNotFoundException("Image not found for menu ID: " + menu_id);
        }
    }



}
