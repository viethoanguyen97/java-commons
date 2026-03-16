package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * GraphQL mutation resolver for blog-related mutations.
 * Uses Spring Boot's native GraphQL support (@MutationMapping) instead of graphql-java-kickstart.
 */
@Controller
public class BlogMutation {

    private final PostDao _postDao;

    @Autowired
    public BlogMutation(PostDao postDao) {
        _postDao = postDao;
    }

    @MutationMapping
    public Post writePost(@Argument String title, @Argument String text,
                          @Argument String category, @Argument String author) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setTitle(title);
        post.setText(text);
        post.setCategory(category);
        post.setAuthorId(author);
        _postDao.savePost(post);
        return post;
    }
}
