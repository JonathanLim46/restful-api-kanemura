package com.pentahelix.kanemuraproject.repository;

import com.pentahelix.kanemuraproject.entity.Kategori;
import com.pentahelix.kanemuraproject.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KategoriRepository extends JpaRepository<Kategori,Integer> {
    Optional<Kategori> findFirstByIdKategori(Integer id_kategori);

    Page<Kategori> findAll(Specification<Menu> specification, Pageable pageable);
}
