package com.pentahelix.kanemuraproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//user response untuk ketentuan get user response
public class UserResponse {

    private String username;

    private String name;

}
