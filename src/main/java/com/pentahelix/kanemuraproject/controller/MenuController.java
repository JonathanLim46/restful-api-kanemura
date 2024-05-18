package com.pentahelix.kanemuraproject.controller;

import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.*;
import com.pentahelix.kanemuraproject.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class MenuController {

    @Autowired
    private MenuService menuService;



    @PostMapping(
            path = "/api/auth/menus",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MenuResponse> create(User user, @RequestParam("namaMenu") String namaMenu, @RequestParam("description") String description, @RequestParam("harga") Integer harga, @RequestParam("idkategori") Integer idkategori, @RequestParam("signature") Boolean signature, @RequestParam("image") MultipartFile file) throws Exception{
        MenuResponse menuResponse = menuService.create(user, namaMenu, description, harga, idkategori, signature,file);
        return WebResponse.<MenuResponse>builder().data(menuResponse).build();
    }


    @GetMapping(
            path = "/api/menus/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MenuResponse> get(@PathVariable("id") Integer id){
        MenuResponse menuResponse = menuService.get(id);
        return WebResponse.<MenuResponse>builder().data(menuResponse).build();
    }


    @PutMapping(
            path = "/api/auth/menus/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MenuResponse> update(User user,@RequestParam("namaMenu") String namaMenu, @RequestParam("description") String description, @RequestParam("harga") Integer harga, @RequestParam("idkategori") Integer idkategori, @RequestParam("signature") Boolean signature, @PathVariable("id") Integer id) throws IOException {
        MenuResponse menuResponse = menuService.update(user,namaMenu,description,harga,idkategori,signature,id);
        return WebResponse.<MenuResponse>builder().data(menuResponse).build();
    }

    @DeleteMapping(
            path = "/api/auth/menus/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("id") Integer id) {
        menuService.delete(user, id);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/menus",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<MenuResponse>> search(@RequestParam(value = "namaMenu", required = false) String namaMenu,
                                                  @RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "kategori", required = false) Integer kategori,
                                                  @RequestParam(value = "harga", required = false) Integer harga,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(value = "nama_img",required = false) String nama_img,
                                                  @RequestParam(value = "signature",required = false) Boolean signature){
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .namaMenu(namaMenu)
                .description(description)
                .kategori(kategori)
                .harga(harga)
                .nama_img(nama_img)
                .signature(signature)
                .build();

        List<MenuResponse> menuResponses = menuService.search(request);
        return WebResponse.<List<MenuResponse>>builder()
                .data(menuResponses)
                .build();
    }



}
