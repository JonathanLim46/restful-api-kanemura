package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
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
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private MenuRepository menuRepository;

    @Value("${images.folder}")
    private String imagesFolder;

//    UPLOAD IMAGE
    public String updateImageToFileSystem(User user, MultipartFile file, Integer id) throws IOException {
//        String relativeFilePath = IMAGES_FOLDER + file.getOriginalFilename();
//        String filePath = BASE_FOLDER_PATH + File.separator + relativeFilePath;

//        CARI MENU DENGAN ID MENU
        Menu existingMenu = menuRepository.findFirstById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu dengan id " + id + " tidak ditemukan"));

        existingMenu.setNameImg(file.getOriginalFilename());
        existingMenu.setType(file.getContentType());

        String relativeFilePath = file.getOriginalFilename();
        existingMenu.setFilepath(relativeFilePath);
        existingMenu.setNameImg(file.getOriginalFilename());

        menuRepository.save(existingMenu);

//        CHECK FOLDER
        File directory = new File(imagesFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = Paths.get(imagesFolder,relativeFilePath).toString();
        file.transferTo(new File(filePath));

        return "File berhasil terupload dengan id menu :  " + id;
    }

    // GET IMAGE DARI DIRECTORY
    public byte[] getImageByFileName(String filename) throws IOException {
        Optional<Menu> fileDataOptional = menuRepository.findByNameImg(filename);
        if (fileDataOptional.isPresent()) {
            Menu fileData = fileDataOptional.get();
            String relativeFilePath = fileData.getFilepath();
            String filePath = Paths.get(imagesFolder, relativeFilePath).toString();
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar tidak ditemukan dengan nama file: " + filename);
        }
    }

    // GET IMAGE DENGAN ID MENU
    public byte[] getImageByMenuId(Integer menuId) throws IOException {
        Optional<Menu> fileDataOptional = menuRepository.findFirstById(menuId);
        if (fileDataOptional.isPresent()) {
            Menu fileData = fileDataOptional.get();
            String relativeFilePath = fileData.getFilepath();
            String filePath = Paths.get(imagesFolder, relativeFilePath).toString();
            return Files.readAllBytes(new File(filePath).toPath());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gambar tidak ditemukan dengan id menu: " + menuId);
        }
    }

}
