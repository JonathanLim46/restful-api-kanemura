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

    private String nama_menu;

    private String description;

    private String kategori;

    private Integer harga;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    private boolean signature;
}
