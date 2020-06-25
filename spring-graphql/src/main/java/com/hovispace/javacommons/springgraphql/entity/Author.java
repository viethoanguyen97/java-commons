package com.hovispace.javacommons.springgraphql.entity;

import java.util.List;

/**
 * TODO: Describe what this class is for or delete this comment.
 */
public class Author {

    private String id;
    private String name;
    private String thumbnail;
    private List<Post> posts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
