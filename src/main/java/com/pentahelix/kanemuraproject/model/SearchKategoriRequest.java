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
public class SearchKategoriRequest {

    private String nama_kategori;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

}
