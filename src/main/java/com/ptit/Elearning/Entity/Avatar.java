package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "avatar")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Avatar {
    @Id
    @Column(name = "avatar_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Lob
    private byte[] data;

    public Avatar(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
