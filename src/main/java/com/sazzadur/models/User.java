package com.sazzadur.models;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
})
@Getter
@Setter
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String username;

    @NonNull
    private String password;


    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(columnDefinition = "DATE DEFAULT CURRENT_DATE",name = "created_at", updatable = false)
    private Date createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRoles> roles = new HashSet<>();


    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }
    public User() { }

}
