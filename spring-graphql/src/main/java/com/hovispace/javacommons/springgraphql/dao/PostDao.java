package com.hovispace.javacommons.springgraphql.dao;

import com.hovispace.javacommons.springgraphql.entity.Post;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PostDao {

    private List<Post> _posts;

    public PostDao(List<Post> posts) {
        _posts = posts;
    }

    public List<Post> getRecentPosts(int count, int offset) {
        return _posts.stream().skip(offset).limit(count).collect(toList());
    }

    public List<Post> getAuthorPosts(String author) {
        return _posts.stream().filter(post -> author.equals(post.getAuthorId())).collect(toList());
    }

    public void savePost(Post post) {
        _posts.add(0, post);
    }
}
