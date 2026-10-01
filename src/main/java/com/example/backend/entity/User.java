package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor

@Data
@Entity

@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;



    // Bir kullanıcının birden çok rolü olabilir
//    Veritabanında user_roles tablosu oluşturulur.
//
//    Yeni üye kaydında (register) örneğin roles.add("USER") ile “USER” rolü atayın.
//
//    Admin kullanıcıyı el ile ya da ayrı bir “admin-atanma” endpoint’iyle roles.add("ADMIN") yaparak veritabanına ekleyin.
    @Builder.Default                              // <-- ekleyin
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user_id"))
    @Column(name="role")
    private Set<String> roles = new HashSet<>();  // builder da burayı kullanacak

}
