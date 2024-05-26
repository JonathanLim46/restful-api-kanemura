package com.pentahelix.kanemuraproject.repository;

import com.pentahelix.kanemuraproject.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
    Optional<Menu> findFirstById(Integer id);

    Optional<Menu> findByNameImg(String nameImg);
}
