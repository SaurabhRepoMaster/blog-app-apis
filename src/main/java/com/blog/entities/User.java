package com.blog.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
//below properties are from lombok
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name="user_name",nullable = false,length = 100)
    private String name;

    @Column(unique = true,updatable = false)
    private String email;
    private String password;
    private String about;

    //it means 1 user can have many posts.
    //lazy means parent will be loaded and child will not be loaded
    //mappedBy="user", this user we will find in Post Class.
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

}
