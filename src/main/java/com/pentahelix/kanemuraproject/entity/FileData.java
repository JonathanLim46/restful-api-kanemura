package com.pentahelix.kanemuraproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "file_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name_img")
    private String nameImg;

    private String type;

    private String filepath;
}
