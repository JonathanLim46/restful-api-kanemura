package com.pentahelix.kanemuraproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {


    private Integer id;

    private String namaMenu;

    private String description;

    private Integer harga;

    private Integer kategori;

    private boolean signature;

    private String nama_kategori;

    private String nama_img;

}
