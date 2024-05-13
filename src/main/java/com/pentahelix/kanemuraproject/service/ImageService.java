package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Menu;
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
    private MenuRepository menuRepository;

    @Value("${spring.servlet.multipart.location}")
    private String FOLDER_PATH;

    public String updateImageToFileSystem(MultipartFile file, Integer id) throws IOException {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        Menu existingMenu = menuRepository.findFirstById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu with id " + id + " not found"));

        existingMenu.setNameImg(file.getOriginalFilename());
        existingMenu.setType(file.getContentType());
        existingMenu.setFilepath(filePath);

        menuRepository.save(existingMenu);

        file.transferTo(new File(filePath));

        return "File updated successfully and associated with menu ID: " + id;
    }


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Menu> fileData = menuRepository.findByNameImg(fileName);
        String filePath=fileData.get().getFilepath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }

    public byte[] getImageByMenuId(Integer menu_id) throws IOException {
        Optional<Menu> fileDataOptional = menuRepository.findFirstById(menu_id);
        if (fileDataOptional.isPresent()) {
            Menu fileData = fileDataOptional.get();
            String filePath = fileData.getFilepath();
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new FileNotFoundException("Image not found for menu ID: " + menu_id);
        }
    }




}
