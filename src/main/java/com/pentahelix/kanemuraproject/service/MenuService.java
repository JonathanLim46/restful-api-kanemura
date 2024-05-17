    package com.pentahelix.kanemuraproject.service;

    import com.pentahelix.kanemuraproject.entity.Kategori;
    import com.pentahelix.kanemuraproject.entity.Menu;
    import com.pentahelix.kanemuraproject.entity.User;
    import com.pentahelix.kanemuraproject.model.CreateMenuRequest;
    import com.pentahelix.kanemuraproject.model.MenuResponse;
    import com.pentahelix.kanemuraproject.model.SearchMenuRequest;
    import com.pentahelix.kanemuraproject.model.UpdateMenuRequest;
    import com.pentahelix.kanemuraproject.repository.KategoriRepository;
    import com.pentahelix.kanemuraproject.repository.MenuRepository;
    import jakarta.persistence.criteria.Join;
    import jakarta.persistence.criteria.JoinType;
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
        private KategoriRepository kategoriRepository;

        @Autowired
        private ImageService imageService;

        @Autowired
        private ValidationService validationService;

        //    GET BASE FOLDER PATH
        private final String BASE_FOLDER_PATH = Paths.get("src/main/resources/").toAbsolutePath().toString();

        //    IMAGES DEFAULT FOLDER DI DATABASE
        private final String IMAGES_FOLDER = "images/";


    // Create Menu Service
        @Transactional
        public MenuResponse create(User user, String namaMenu, String description, Integer harga, Integer idkategori, MultipartFile file) throws IOException {
            String relativeFilePath = IMAGES_FOLDER + file.getOriginalFilename();
            String filePath = BASE_FOLDER_PATH + File.separator + relativeFilePath;

            Menu menu = new Menu();

            menu.setNamaMenu(namaMenu);
            menu.setDescription(description);
            menu.setHarga(harga);
            menu.setSignature(menu.isSignature());
            menu.setType(file.getContentType());
            menu.setFilepath(relativeFilePath);
            menu.setNameImg(file.getOriginalFilename());

    //        Cari Kategori dengan id kategori
            Kategori kategori = kategoriRepository.findFirstByIdKategori(idkategori)
                    .orElseThrow(() -> new IllegalArgumentException("Kategori Tidak Ditemukan"));

            menu.setKategori(kategori);


    //        simpan ke database
            menuRepository.save(menu);

            //        CHECK FOLDER
            File directory = new File(BASE_FOLDER_PATH + File.separator + IMAGES_FOLDER);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            file.transferTo(new File(filePath));

    //        throw id menu yang sudah terbuat untuk disimpan ke menu response
            menu = menuRepository.findFirstById(menu.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Failed to create menu"));


            return toMenuResponse(menu,kategori);
        }

    //    MenuResponse
        private MenuResponse toMenuResponse(Menu menu, Kategori kategori){
            return MenuResponse.builder()
                    .id(menu.getId())
                    .namaMenu(menu.getNamaMenu())
                    .description(menu.getDescription())
                    .harga(menu.getHarga())
                    .kategori(kategori.getIdKategori())
                    .nama_kategori(kategori.getNama_kategori())
                    .signature(menu.isSignature())
                    .nama_img(menu.getNameImg())
                    .build();
        }



    //    Get Menu By Id Service
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public MenuResponse get(Integer id){
            Menu menu = menuRepository.findFirstById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));

            Kategori kategori = menu.getKategori();

            return toMenuResponse(menu,kategori);
        }

    //    Update Menu Service
        @Transactional
        public MenuResponse update(User user, UpdateMenuRequest request){
            validationService.validate(request);

            Menu menu = menuRepository.findFirstById(request.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
            menu.setNamaMenu(request.getNamaMenu());
            menu.setDescription(request.getDescription());
            menu.setHarga(request.getHarga());
            menu.setSignature(request.isSignature());

    //        Kategori dari table kategori dengan id kategori
            Kategori kategori = kategoriRepository.findFirstByIdKategori(request.getKategori())
                    .orElseThrow(() -> new IllegalArgumentException("Kategori Tidak Ditemukan"));

            menu.setKategori(kategori);

            menuRepository.save(menu);

            return toMenuResponse(menu, kategori);
        }

    //    Delete Menu Service
        @Transactional
        public void delete(User user, Integer id) {
    //        Cari data menu berdasarkan id
            Menu menu = menuRepository.findFirstById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

            menuRepository.delete(menu);
        }

    //     Search Menu Service
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
                Join<Menu, Kategori> kategoriJoin = root.join("kategori", JoinType.INNER);
                predicates.add(builder.equal(kategoriJoin.get("idKategori"), request.getKategori()));
            }
            if(Objects.nonNull(request.getHarga())){
                predicates.add(builder.like(root.get("harga").as(String.class), "%" + request.getHarga() + "%"));
            }
            if(Objects.nonNull(request.getNama_img())){
                predicates.add(builder.like(root.get("nama_img"), "%" + request.getNama_img() + "%"));
            }
            if (Objects.nonNull(request.getSignature())) {
                predicates.add(builder.equal(root.get("signature"), request.getSignature()));

            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Menu> menus = menuRepository.findAll(specification, pageable);
        List<MenuResponse> menuResponses = menus.getContent().stream()
                .map(menu -> toMenuResponse(menu, menu.getKategori()))
                .toList();

        return new PageImpl<>(menuResponses, pageable, menus.getTotalElements());

        }



    }
