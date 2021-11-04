package com.sazzadur.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class UserPosts implements Serializable {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "fullname")
    String fullName;

    @NonNull
    String department;

    @NonNull
    String description;

    @NonNull
    String email;

    @NonNull
    @Column(name = "studylevel")
    String studyLevel;

    @NonNull
    private String image;

    String status = "PENDING";


}
