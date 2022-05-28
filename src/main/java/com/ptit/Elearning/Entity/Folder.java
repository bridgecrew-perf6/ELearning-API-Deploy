package com.ptit.Elearning.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "folder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long folderId;

    @Column(name = "folder_name")
    private String folderName;

    @Column(name = "up_time")
    private Date upTime;

    @ManyToOne
    @JoinColumn(name = "credit_class_id",nullable = false)
    private CreditClass creditClass;

    //cực kỳ đặc biệt
    @OneToMany(mappedBy = "folder",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<Document> documents = new HashSet<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parents_folder",nullable = true)
    private Folder parentsFolder;

}
