package com.pentahelix.kanemuraproject.controller;

import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/api/auth/image/fileSystem")
    public ResponseEntity<?> uploadImageToFIleSystem(User user, @RequestParam("image")MultipartFile file, Integer id) throws IOException {
        String uploadImage = imageService.updateImageToFileSystem(user,file,id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/api/image/fileSystem/{Filename}")
    public ResponseEntity<?> getImageByFileName(@PathVariable String Filename) throws IOException {
        byte[] imageData=imageService.getImageByFileName(Filename);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

    @GetMapping("/api/image/fileSystem/images/{menu_id}")
    public ResponseEntity<?> getImageByMenuId(@PathVariable Integer menu_id) throws IOException {
        byte[] imageData=imageService.getImageByMenuId(menu_id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
}
