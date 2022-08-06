package com.amp.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String name;

    private String userId;

    private String password;

    // OAuth를 위해 구성한 추가 필드 2개
    private String provider;
    private String providerId;

    @CreationTimestamp
    private Timestamp createDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
