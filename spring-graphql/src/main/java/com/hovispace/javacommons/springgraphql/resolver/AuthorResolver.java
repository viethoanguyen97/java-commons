package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AuthorResolver implements GraphQLResolver<Author> {

    private final PostDao _postDao;

    @Autowired
    public AuthorResolver(PostDao postDao) {
        _postDao = postDao;
    }

    public List<Post> getPosts(Author author) {
        return _postDao.getAuthorPosts(author.getId());
    }
}
