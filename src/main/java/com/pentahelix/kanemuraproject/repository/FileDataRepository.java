package com.pentahelix.kanemuraproject.repository;


import com.pentahelix.kanemuraproject.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData,Integer> {
    Optional<FileData> findByNameImg(String name_img);

    Optional<FileData> findByMenuId(Integer menu_id);
}
