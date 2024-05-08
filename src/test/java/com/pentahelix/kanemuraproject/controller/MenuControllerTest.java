package com.pentahelix.kanemuraproject.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.CreateMenuRequest;
import com.pentahelix.kanemuraproject.model.MenuResponse;
import com.pentahelix.kanemuraproject.model.UpdateMenuRequest;
import com.pentahelix.kanemuraproject.model.WebResponse;
import com.pentahelix.kanemuraproject.repository.MenuRepository;
import com.pentahelix.kanemuraproject.repository.UserRepository;
import com.pentahelix.kanemuraproject.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        menuRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("test",BCrypt.gensalt()));
        user.setName("Test");
        user.setToken("test");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000);
        userRepository.save(user);
    }

    @Test
    void createMenuBadRequest() throws Exception{
        CreateMenuRequest request = new CreateMenuRequest();
        request.setNama_menu("");
        request.setDescription("");

        mockMvc.perform(
                post("/api/auth/menus")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void createMenuSuccess() throws Exception{
        CreateMenuRequest request = new CreateMenuRequest();

        request.setNama_menu("Ramen");
        request.setDescription("Makanan Mie Jepang");
        request.setHarga(15000);
        request.setKategori("Ramen");
        request.setSignature(true);

        mockMvc.perform(
                post("/api/auth/menus")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("Makanan Mie Jepang", response.getData().getDescription());
            assertEquals(15000,response.getData().getHarga());
            assertEquals("Ramen",response.getData().getKategori());
            assertEquals("Ramen",response.getData().getNama_menu());
            assertTrue(response.getData().isSignature());



            assertTrue(menuRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void getMenuNotFound() throws Exception{
        mockMvc.perform(
                get("/api/menus/234142323")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void getMenuSuccess() throws Exception{

        Menu menu = new Menu();
        menu.setNama_menu("Ramen Gyoza");
        menu.setKategori("Ramen");
        menu.setDescription("Kuah Gulai Sapi");
        menu.setHarga(15000);
        menuRepository.save(menu);

        mockMvc.perform(
                get("/api/menus/" + menu.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());

            assertEquals(menu.getDescription(), response.getData().getDescription());
            assertEquals(menu.getHarga(),response.getData().getHarga());
            assertEquals(menu.getKategori(),response.getData().getKategori());
            assertEquals(menu.getNama_menu(),response.getData().getNama_menu());

        });
    }

    @Test
    void updateMenuBadRequest() throws Exception{
        UpdateMenuRequest request = new UpdateMenuRequest();
        request.setNama_menu("");
        request.setDescription("");

        mockMvc.perform(
                put("/api/auth/menus/15")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void updateMenuSuccess() throws Exception{
        Menu menu = new Menu();
        menu.setNama_menu("Ramen Gyoza");
        menu.setKategori("Ramen");
        menu.setDescription("Kuah Gulai Sapi");
        menu.setHarga(15000);
        menuRepository.save(menu);

        CreateMenuRequest request = new CreateMenuRequest();

        request.setNama_menu("Ramen Wow");
        request.setDescription("Makanan Mie Jepang");
        request.setHarga(21000);
        request.setKategori("Ramen");

        mockMvc.perform(
                put("/api/auth/menus/" + menu.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<MenuResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(request.getDescription(), response.getData().getDescription());
            assertEquals(request.getHarga(),response.getData().getHarga());
            assertEquals(request.getKategori(),response.getData().getKategori());
            assertEquals(request.getNama_menu(),response.getData().getNama_menu());



            assertTrue(menuRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void deleteMenuNotFound() throws Exception{
        mockMvc.perform(
                delete("/api/auth/menus/234142323")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            assertNotNull(response.getErrors());

        });
    }

    @Test
    void deleteMenuSuccess() throws Exception{

        Menu menu = new Menu();
        menu.setNama_menu("Ramen Gyoza");
        menu.setKategori("Ramen");
        menu.setDescription("Kuah Gulai Sapi");
        menu.setHarga(15000);
        menuRepository.save(menu);

        mockMvc.perform(
                delete("/api/auth/menus/" + menu.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("OK",response.getData());

        });
    }

    @Test
    void searchNotFound() throws Exception{

        mockMvc.perform(
                get("/api/menus")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN","test")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(0,response.getData().size());
            assertEquals(0,response.getPaging().getTotalPage());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });
    }

    @Test
    void searchSuccess() throws Exception{

        for (int i = 0; i < 100; i++) {
            Menu menu = new Menu();
            menu.setDescription("Kuah Gulai Sapi");
            menu.setHarga(15000);
            menu.setKategori("Ramen");
            menu.setNama_menu("ramens" + i);
            menu.setSignature(true);
            menuRepository.save(menu);
        }

        mockMvc.perform(
                get("/api/menus")
                        .queryParam("signature", "true")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPage());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });

        mockMvc.perform(
                get("/api/menus")
                        .queryParam("nama_menu", "ramens")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(10,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPage());
            assertEquals(0,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });


        mockMvc.perform(
                get("/api/menus")
                        .queryParam("kategori", "Ramen")
                        .queryParam("page","1000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<MenuResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(0,response.getData().size());
            assertEquals(10,response.getPaging().getTotalPage());
            assertEquals(1000,response.getPaging().getCurrentPage());
            assertEquals(10,response.getPaging().getSize());
        });



    }
}