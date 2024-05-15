package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private MenuRepository menuRepository;

    private final String BASE_FOLDER_PATH = Paths.get("src/main/resources/").toAbsolutePath().toString();
    private final String IMAGES_FOLDER = "images/";

    public String updateImageToFileSystem(User user, MultipartFile file, Integer id) throws IOException {
        String relativeFilePath = IMAGES_FOLDER + file.getOriginalFilename();
        String filePath = BASE_FOLDER_PATH + File.separator + relativeFilePath;

        Menu existingMenu = menuRepository.findFirstById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu dengan id " + id + " tidak ditemukan"));

        existingMenu.setNameImg(file.getOriginalFilename());
        existingMenu.setType(file.getContentType());
        existingMenu.setFilepath(relativeFilePath);

        menuRepository.save(existingMenu);

        File directory = new File(BASE_FOLDER_PATH + File.separator + IMAGES_FOLDER);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        file.transferTo(new File(filePath));

        return "File berhasil terupload dengan id menu :  " + id;
    }

    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        Optional<Menu> fileData = menuRepository.findByNameImg(fileName);
        if (fileData.isPresent()) {
            String relativeFilePath = fileData.get().getFilepath();
            String filePath = BASE_FOLDER_PATH + File.separator + relativeFilePath;
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new FileNotFoundException("Gambar tidak ditemukan : " + fileName);
        }
    }

    public byte[] getImageByMenuId(Integer menu_id) throws IOException {
        Optional<Menu> fileDataOptional = menuRepository.findFirstById(menu_id);
        if (fileDataOptional.isPresent()) {
            Menu fileData = fileDataOptional.get();
            String relativeFilePath = fileData.getFilepath();
            String filePath = BASE_FOLDER_PATH + File.separator + relativeFilePath;
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new FileNotFoundException("Gambar tidak ditemukan dengan id menu : "  + menu_id);
        }
    }
}
