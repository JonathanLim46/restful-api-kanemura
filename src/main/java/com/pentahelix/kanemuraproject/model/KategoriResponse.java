package com.pentahelix.kanemuraproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KategoriResponse {

    private Integer idKategori;

    private String nama_kategori;
}
