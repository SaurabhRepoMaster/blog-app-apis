package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;


    @Override
    public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ","Id",userId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ","Id",categoryId));

        Post post = this.postDtoToPost(postDto);
        post.setCreationDate(new Date());
        post.setCategory(category);
        post.setUser(user);

        Post createdPost = postRepo.save(post);
        return postToPostDto(createdPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post"," Post Id ",postId));
        post.setContent(postDto.getContent());
        post.setTitle(postDto.getTitle());
        Post updatedPost = postRepo.save(post);
        return postToPostDto(updatedPost);

    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post"," Post Id ",postId));
        return postToPostDto(post);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<PostDto> list = postRepo.findAll().stream().map(post -> postToPostDto(post)).collect(Collectors.toList());
        return list;
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post"," Post Id ",postId));
        postRepo.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category ","Id",categoryId));
        List<PostDto> list = postRepo.findByCategory(category).stream().map(post -> postToPostDto(post)).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User ","Id",userId));
        List<PostDto> list = postRepo.findByUser(user).stream().map(post -> postToPostDto(post)).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<PostDto> searchPosts(String keyword) {
        return null;
    }

    private Post postDtoToPost(PostDto postDto)
    {
        return modelMapper.map(postDto, Post.class);
    }

    private PostDto postToPostDto(Post post)
    {
        return modelMapper.map(post, PostDto.class);
    }
}
