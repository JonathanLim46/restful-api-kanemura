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

import java.util.List;

@RestController
@CrossOrigin
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping(
            path = "/api/auth/menus",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MenuResponse> create(User user, @RequestBody CreateMenuRequest request){
        MenuResponse menuResponse = menuService.create(user, request);
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
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<MenuResponse> update(User user,@RequestBody UpdateMenuRequest request, @PathVariable("id") Integer idMenu){

        request.setId(idMenu);
        MenuResponse menuResponse = menuService.update(user,request);
        return WebResponse.<MenuResponse>builder().data(menuResponse).build();
    }

    @DeleteMapping(
            path = "/api/auth/menus/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteMenu(User user,@PathVariable Integer id) {
        try {
            menuService.deleteMenuAndRelatedImage(user,id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete menu: " + e.getMessage());
        }
    }

    @GetMapping(
            path = "/api/menus",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<MenuResponse>> search(@RequestParam(value = "nama_menu", required = false) String nama_menu,
                                                  @RequestParam(value = "description", required = false) String description,
                                                  @RequestParam(value = "kategori", required = false) String kategori,
                                                  @RequestParam(value = "harga", required = false) Integer harga,
                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(value="signature",required = false) boolean signature){
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .nama_menu(nama_menu)
                .description(description)
                .kategori(kategori)
                .harga(harga)
                .signature(signature)
                .build();

        Page<MenuResponse> menuResponses = menuService.search(request);
        return WebResponse.<List<MenuResponse>>builder()
                .data(menuResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(menuResponses.getNumber())
                        .totalPage(menuResponses.getTotalPages())
                        .size(menuResponses.getSize())
                        .build())
                .build();
    }
}
