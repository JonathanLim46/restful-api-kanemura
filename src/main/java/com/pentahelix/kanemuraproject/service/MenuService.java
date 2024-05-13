package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.CreateMenuRequest;
import com.pentahelix.kanemuraproject.model.MenuResponse;
import com.pentahelix.kanemuraproject.model.SearchMenuRequest;
import com.pentahelix.kanemuraproject.model.UpdateMenuRequest;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ValidationService validationService;

    @Value("${spring.servlet.multipart.location}")
    private String FOLDER_PATH;


    @Transactional
    public MenuResponse create(User user, CreateMenuRequest request){
        validationService.validate(request);

        Menu menu = new Menu();

        menu.setNamaMenu(request.getNamaMenu());
        menu.setDescription(request.getDescription());
        menu.setHarga(request.getHarga());
        menu.setKategori(request.getKategori());
        menu.setSignature(request.isSignature());


        menuRepository.save(menu);

        return toMenuResponse(menu);
    }



    private MenuResponse toMenuResponse(Menu menu){
        return MenuResponse.builder()
                .id(menu.getId())
                .namaMenu(menu.getNamaMenu())
                .description(menu.getDescription())
                .harga(menu.getHarga())
                .kategori(menu.getKategori())
                .signature(menu.isSignature())
                .build();
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public MenuResponse get(Integer id){
        Menu menu = menuRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
        return toMenuResponse(menu);
    }

    @Transactional
    public MenuResponse update(User user,UpdateMenuRequest request){
        validationService.validate(request);

        Menu menu = menuRepository.findFirstById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
        menu.setNamaMenu(request.getNamaMenu());
        menu.setKategori(request.getKategori());
        menu.setDescription(request.getDescription());
        menu.setHarga(request.getHarga());
        menu.setSignature(request.isSignature());
        menuRepository.save(menu);

        return toMenuResponse(menu);
    }

    @Transactional
    public void delete(User user, Integer id) {
        Menu contact = menuRepository.findFirstById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

        menuRepository.delete(contact);
    }


    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<MenuResponse> search(SearchMenuRequest request){
        Specification<Menu> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(request.getNamaMenu())){
                predicates.add(builder.like(root.get("namaMenu"), "%" + request.getNamaMenu() + "%"));
            }
            if(Objects.nonNull(request.getDescription())){
                predicates.add(builder.like(root.get("description"), "%" + request.getDescription() + "%"));
            }
            if(Objects.nonNull(request.getKategori())){
                predicates.add(builder.like(root.get("kategori"), "%" + request.getKategori() + "%"));
            }
            if(Objects.nonNull(request.getHarga())){
                predicates.add(builder.like(root.get("harga"), "%" + request.getHarga() + "%"));
            }
            if (Objects.nonNull(request.isSignature())) {
                if (request.isSignature()) {
                    predicates.add(builder.isTrue(root.get("signature")));
                } else {
                    predicates.add(builder.or(
                            builder.isFalse(root.get("signature")),
                            builder.isNull(root.get("signature"))
                    ));
                }
            }



            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Menu> menus = menuRepository.findAll(specification, pageable);
        List<MenuResponse> menuResponses = menus.getContent().stream()
                .map(this::toMenuResponse)
                .toList();

        return new PageImpl<>(menuResponses,pageable,menus.getTotalElements());
    }
}
