package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Kategori;
import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.*;
import com.pentahelix.kanemuraproject.repository.KategoriRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class KategoriService {

    @Autowired
    private KategoriRepository kategoriRepository;

    @Autowired
    private ValidationService validationService;

//    CREATE KATEGORI
    @Transactional
    public KategoriResponse create(User user, CreateKategoriRequest request){
        validationService.validate(request);

        Kategori kategori = new Kategori();

        kategori.setNama_kategori(request.getNama_kategori());

        kategoriRepository.save(kategori);


        return toKategoriResponse(kategori);
    }

//      KATEGORI RESPONSE
    private KategoriResponse toKategoriResponse(Kategori kategori){
        return KategoriResponse.builder()
                .idKategori(kategori.getIdKategori())
                .nama_kategori(kategori.getNama_kategori())
                .build();
    }

//    GET KATEGORI BERDASARKAN ID KATEGORI
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public KategoriResponse get(Integer idKategori){
        Kategori kategori = kategoriRepository.findFirstByIdKategori(idKategori)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategori Not Found"));
        return toKategoriResponse(kategori);
    }

//    UPDATE KATEGORI
    @Transactional
    public KategoriResponse update(User user, UpdateKategoriRequest request){
        validationService.validate(request);

//        CHECK ID KATEGORI
        Kategori kategori = kategoriRepository.findFirstByIdKategori(request.getIdKategori())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategori Not Found"));
        kategori.setNama_kategori(request.getNama_kategori());

        kategoriRepository.save(kategori);

        return toKategoriResponse(kategori);
    }

//    DELETE KATEGORI
    @Transactional
    public void delete(User user, Integer idKategori) {
        Kategori kategori = kategoriRepository.findFirstByIdKategori(idKategori)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategori not found"));

        kategoriRepository.delete(kategori);
    }

//    GET ALL KATEGORI
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<KategoriResponse> search(SearchKategoriRequest request){
        Specification<Menu> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(request.getNama_kategori())){
                predicates.add(builder.like(root.get("namaKategori"), "%" + request.getNama_kategori() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Kategori> kategoris = kategoriRepository.findAll(specification, pageable);
        List<KategoriResponse> kategoriResponses = kategoris.getContent().stream()
                .map(this::toKategoriResponse)
                .toList();

        return new PageImpl<>(kategoriResponses,pageable,kategoris.getTotalElements());
    }

}
