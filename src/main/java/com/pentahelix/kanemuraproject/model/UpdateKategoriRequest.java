package com.pentahelix.kanemuraproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateKategoriRequest {

    @JsonIgnore
    private Integer idKategori;

    private String nama_kategori;
}
