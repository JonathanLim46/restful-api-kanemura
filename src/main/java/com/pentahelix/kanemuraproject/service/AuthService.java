package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.LoginUserRequest;
import com.pentahelix.kanemuraproject.model.TokenResponse;
import com.pentahelix.kanemuraproject.repository.UserRepository;
import com.pentahelix.kanemuraproject.security.BCrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional // Manipulation to Database
    public TokenResponse login(LoginUserRequest request){

//        Validation
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));
        if(BCrypt.checkpw(request.getPassword(), user.getPassword())){
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user); // save to database

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }

    }

//    Token set selama 30 hari
    private Long next30Days() {
        return System.currentTimeMillis() + TimeUnit.DAYS.toMillis(30);
    }

//    Logout user dengan token null
    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);

        userRepository.save(user);

    }
}
