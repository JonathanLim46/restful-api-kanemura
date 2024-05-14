package com.pentahelix.kanemuraproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_kategori")
public class Kategori {

    @Id
    @Column(name = "id_kategori")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idKategori;

    private String nama_kategori;

    @OneToMany(mappedBy = "kategori")
    private List<Menu> menuList;
}
