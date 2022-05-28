package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "document")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_name")
    private String documentName;

    @Column(name="file_type")
    private String fileType;

    @Lob
    private byte[] data;

    @Column(name = "created_at")
    private Timestamp createAt;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToMany
    @JoinTable(name = "excercise_document",joinColumns = @JoinColumn(name = "document_id"),inverseJoinColumns = @JoinColumn(name = "excercise_id"))
    private Set<Excercise> excercises = new HashSet<>();

    public Document(String documentName, String fileType, byte[] data, Timestamp createAt) {
        this.documentName = documentName;
        this.fileType = fileType;
        this.data = data;
        this.createAt = createAt;
    }
}
