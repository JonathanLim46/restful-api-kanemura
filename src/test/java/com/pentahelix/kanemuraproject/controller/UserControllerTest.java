//package com.pentahelix.kanemuraproject.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pentahelix.kanemuraproject.entity.Menu;
//import com.pentahelix.kanemuraproject.entity.User;
//import com.pentahelix.kanemuraproject.model.*;
//import com.pentahelix.kanemuraproject.repository.UserRepository;
//import com.pentahelix.kanemuraproject.security.BCrypt;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void testRegisterSuccess() throws Exception {
//        RegisterUserRequest request = new RegisterUserRequest();
//        request.setUsername("Irham");
//        request.setPassword("rahasia2");
//        request.setName("Irham Maulana");
//
//        mockMvc.perform(
//                post("/api/users")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
//            });
//
//            assertEquals("OK", response.getData());
//        });
//    }
//
//    @Test
//    void testRegisterSuccess2() throws Exception {
//        RegisterUserRequest request = new RegisterUserRequest();
//        request.setUsername("Mawar");
//        request.setPassword("rahasia");
//        request.setName("Mawar Yemima Putri");
//
//        mockMvc.perform(
//                post("/api/users")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//        ).andExpect(
//                status().isOk()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertEquals("OK", response.getData());
//        });
//    }
//
//    @Test
//    void testRegisterBadRequest() throws Exception {
//        RegisterUserRequest request = new RegisterUserRequest();
//        request.setUsername("");
//        request.setPassword("");
//        request.setName("");
//
//        mockMvc.perform(
//                post("/api/users")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//        ).andExpect(
//                status().isBadRequest()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void testRegisterDuplicate() throws Exception {
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
//        user.setName("Test");
//        userRepository.save(user);
//
//        RegisterUserRequest request = new RegisterUserRequest();
//        request.setUsername("test");
//        request.setPassword("rahasia");
//        request.setName("Test");
//
//        mockMvc.perform(
//                post("/api/users")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//        ).andExpect(
//                status().isBadRequest()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void getUserUnauthorized() throws Exception{
//        mockMvc.perform(
//                get("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("X-API-TOKEN", "notfound")
//        ).andExpectAll(
//                status().isUnauthorized()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void getUserUnauthorizedTokenNotSend() throws Exception{
//        mockMvc.perform(
//                get("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isUnauthorized()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void getUserSuccess() throws Exception{
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
//        user.setName("Test");
//        user.setToken("test");
//        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
//        userRepository.save(user);
//
//        mockMvc.perform(
//                get("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("X-API-TOKEN","test")
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNull(response.getErrors());
//            assertEquals("test",response.getData().getUsername());
//            assertEquals("Test",response.getData().getName());
//        });
//    }
//
//    @Test
//    void getUserTokenExpired() throws Exception{
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
//        user.setName("Test");
//        user.setToken("test");
//        user.setTokenExpiredAt(System.currentTimeMillis() - 10000000000L);
//        userRepository.save(user);
//
//        mockMvc.perform(
//                get("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .header("X-API-TOKEN","test")
//        ).andExpectAll(
//                status().isUnauthorized()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void updateUserUnauthorized() throws Exception{
//        UpdateUserRequest request = new UpdateUserRequest();
//
//        mockMvc.perform(
//                patch("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isUnauthorized()
//        ).andDo(result -> {
//            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNotNull(response.getErrors());
//        });
//    }
//
//    @Test
//    void updateUserSuccess() throws Exception{
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
//        user.setName("Test");
//        user.setToken("test");
//        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
//        userRepository.save(user);
//
//        UpdateUserRequest request = new UpdateUserRequest();
//        request.setName("Mawar");
//        request.setPassword("yemima");
//
//        mockMvc.perform(
//                patch("/api/users/current")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .header("X-API-TOKEN", "test")
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//
//            assertNull(response.getErrors());
//            assertEquals("Mawar",response.getData().getName());
//            assertEquals("test", response.getData().getUsername());
//
//            User UserDb  = userRepository.findById("test").orElse(null);
//            assertNotNull(UserDb);
//            assertTrue(BCrypt.checkpw("yemima", UserDb.getPassword()));
//        });
//    }
//
//    @Test
//    void getDataNotFound() throws Exception{
//
//        mockMvc.perform(
//                get("/api/auth/users")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            WebResponse<List<UserResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
//            });
//            assertNull(response.getErrors());
//            assertEquals(0,response.getData().size());
//            assertEquals(0,response.getPaging().getTotalPage());
//            assertEquals(0,response.getPaging().getCurrentPage());
//            assertEquals(10,response.getPaging().getSize());
//        });
//    }
//}
