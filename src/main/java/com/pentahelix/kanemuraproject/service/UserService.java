package com.pentahelix.kanemuraproject.service;

import com.pentahelix.kanemuraproject.entity.Menu;
import com.pentahelix.kanemuraproject.entity.User;
import com.pentahelix.kanemuraproject.model.*;
import com.pentahelix.kanemuraproject.repository.FindUserRepository;
import com.pentahelix.kanemuraproject.repository.UserRepository;
import com.pentahelix.kanemuraproject.security.BCrypt;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

@Service
@Slf4j
public class UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FindUserRepository findUserRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
//    method register
    public void register(RegisterUserRequest request){

//        Validation Service
        validationService.validate(request);

//        USER REQUEST ID
        if(userRepository.existsById(request.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
        }

//        doll object untuk user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setName(request.getName());

        userRepository.save(user);
    }

    public UserResponse get(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
//        doll object get user
    }

    @Transactional
    public UserResponse update(User user, UpdateUserRequest request){
        validationService.validate(request);

        log.info("REQUEST : {}", request);

        if(Objects.nonNull(request.getName())){
            user.setName(request.getName());
        }

        if(Objects.nonNull(request.getPassword())){
            user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        }

        userRepository.save(user);

        log.info("USER : {} ", user.getName());

        return UserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

    private UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<UserResponse> getData(User user,SearchUserRequest request){
        Specification<User> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(Objects.nonNull(request.getUsername())){
                predicates.add(builder.like(root.get("username"), "%" + request.getUsername() + "%"));
            }
            if(Objects.nonNull(request.getName())){
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }


            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<User> users = findUserRepository.findAll(specification, pageable);
        List<UserResponse> userResponse = users.getContent().stream()
                .map(this::toUserResponse)
                .toList();

        return new PageImpl<>(userResponse,pageable,users.getTotalElements());
    }

    @Transactional
    public void delete(String username){
        User user = findUserRepository.findFirstByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        findUserRepository.delete(user);
    }
}