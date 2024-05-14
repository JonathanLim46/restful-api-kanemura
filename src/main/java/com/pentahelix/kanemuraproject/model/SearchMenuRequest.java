package com.pentahelix.kanemuraproject.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMenuRequest {

    private String namaMenu;

    private String description;

    private Integer kategori;

    private Integer harga;

    private String filepath;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    private boolean signature;
}
