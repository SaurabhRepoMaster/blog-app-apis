package com.blog.repositories;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Integer> {

    //custom finder method to get all posts by a user
    List<Post> findByUser(User user);

    //custom finder method to get all posts of a specific category
    List<Post> findByCategory(Category category);
}
