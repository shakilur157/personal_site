package com.sazzadur.models;

import com.sazzadur.enums.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
