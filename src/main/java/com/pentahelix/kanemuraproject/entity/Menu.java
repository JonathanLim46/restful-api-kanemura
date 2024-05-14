package com.pentahelix.kanemuraproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menu")
@Builder
public class Menu {

    @Id
    @Column(name = "id_menu")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nama_menu")
    private String namaMenu;

    @Column(name = "description")
    private String description;

    private Integer harga;

    private boolean signature;

    @Column(name = "nama_img")
    private String nameImg;

    private String type;

    private String filepath;

    @ManyToOne
    @JoinColumn(name = "kategori",referencedColumnName = "id_kategori")
    private Kategori kategori;
}
