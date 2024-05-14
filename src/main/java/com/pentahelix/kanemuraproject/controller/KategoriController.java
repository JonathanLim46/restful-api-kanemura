package com.pentahelix.kanemuraproject.controller;

import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.*;
import com.pentahelix.kanemuraproject.service.KategoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class KategoriController {

    @Autowired
    private KategoriService kategoriService;

    @PostMapping(
            path = "/api/auth/kategori",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<KategoriResponse> create(User user, @RequestBody CreateKategoriRequest request){
        KategoriResponse kategoriResponse = kategoriService.create(user, request);
        return WebResponse.<KategoriResponse>builder().data(kategoriResponse).build();
    }

    @GetMapping(
            path = "/api/kategori/{idKategori}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<KategoriResponse> get(@PathVariable("idKategori") Integer idKategori){
        KategoriResponse kategoriResponse = kategoriService.get(idKategori);
        return WebResponse.<KategoriResponse>builder().data(kategoriResponse).build();
    }


    @PutMapping(
            path = "/api/auth/kategori/{idKategori}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<KategoriResponse> update(User user, @RequestBody UpdateKategoriRequest request, @PathVariable("idKategori") Integer idKategori){

        request.setIdKategori(idKategori);
        KategoriResponse kategoriResponse = kategoriService.update(user,request);
        return WebResponse.<KategoriResponse>builder().data(kategoriResponse).build();
    }

    @DeleteMapping(
            path = "/api/auth/kategori/{idKategori}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("idKategori") Integer idKategori) {
        kategoriService.delete(user, idKategori);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/kategori",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<KategoriResponse>> search(@RequestParam(value = "nama_kategori", required = false) String namaKategori,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size){
        SearchKategoriRequest request = SearchKategoriRequest.builder()
                .page(page)
                .size(size)
                .nama_kategori(namaKategori)
                .build();

        Page<KategoriResponse> kategoriResponses = kategoriService.search(request);
        return WebResponse.<List<KategoriResponse>>builder()
                .data(kategoriResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(kategoriResponses.getNumber())
                        .totalPage(kategoriResponses.getTotalPages())
                        .size(kategoriResponses.getSize())
                        .build())
                .build();
    }

}
