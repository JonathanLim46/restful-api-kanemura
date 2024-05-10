package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.FileData;
import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.MenuResponse;
import com.pentahelix.kanemuraproject.model.UpdateMenuRequest;
import com.pentahelix.kanemuraproject.repository.FileDataRepository;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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

    public String updateImageToFileSystem(MultipartFile file, Integer menuId) throws IOException {
        Optional<FileData> existingFileDataOptional = fileDataRepository.findByMenuId(menuId);

        if (existingFileDataOptional.isPresent()) {
            FileData existingFileData = existingFileDataOptional.get();
            Files.deleteIfExists(new File(existingFileData.getFilepath()).toPath());
            fileDataRepository.delete(existingFileData);
        }

        String filePath = FOLDER_PATH + file.getOriginalFilename();
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found with ID: " + menuId));

        FileData newFileData = fileDataRepository.save(FileData.builder()
                .nameImg(file.getOriginalFilename())
                .type(file.getContentType())
                .filepath(filePath)
                .menu(menu)
                .build());
        file.transferTo(new File(filePath));

        return "File updated successfully: " + filePath;
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
