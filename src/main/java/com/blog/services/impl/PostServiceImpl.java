package com.blog.services.impl;

import com.blog.entities.Category;
import com.blog.entities.Post;
import com.blog.entities.User;
import com.blog.exceptions.ResourceNotFoundException;
import com.blog.payloads.PostDto;
import com.blog.payloads.PostResponse;
import com.blog.payloads.UserDto;
import com.blog.repositories.CategoryRepo;
import com.blog.repositories.PostRepo;
import com.blog.repositories.UserRepo;
import com.blog.services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//    @Override
//    public List<PostDto> getAllPosts(Integer pageNumber, Integer pageSize) {
//        //commenting this code as this will not find out pagination or anything
//        //List<Post> postList = postRepo.findAll();
//        //Implementing pagination:
//        Pageable page = PageRequest.of(pageNumber,pageSize);
//        Page<Post> pagePost= postRepo.findAll(page);
//        List<Post> postList = pagePost.getContent();
//
//        List<PostDto>  list = postList.stream().map(post -> postToPostDto(post)).collect(Collectors.toList());
//        return list;
//    }
    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy, String sortDir) {
        //commenting this code as this will not find out pagination or anything
        //List<Post> postList = postRepo.findAll();
        //Implementing pagination:
        Sort sort = null;
        if("ASC".equals(sortDir))
            sort = Sort.by(sortBy).ascending();
        else
            sort = Sort.by(sortBy).descending();
        //if sorting not required, we can use below commented to get object
        //Pageable page = PageRequest.of(pageNumber,pageSize);
        Pageable page = PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> pagePost= postRepo.findAll(page);
        List<Post> postList = pagePost.getContent();

        List<PostDto>  list = postList.stream().map(post -> postToPostDto(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(list);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());
        return postResponse;
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
        List<PostDto> list= this.postRepo.searchByTitleContaining("%"+keyword+"%").stream()
                                .map((post -> postToPostDto(post))).collect(Collectors.toList());
        return list;
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
