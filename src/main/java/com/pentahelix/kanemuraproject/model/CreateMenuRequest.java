package com.pentahelix.kanemuraproject.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMenuRequest {


    @NotBlank
    @Size(max = 100)
    private String namaMenu;

    @Size(max = 500)
    private String description;

    private Integer harga;

    private Integer kategori;

    private boolean signature;

}
