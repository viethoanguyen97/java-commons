package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.AuthorDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * GraphQL field resolver for the Post type.
 * Resolves the 'author' field on Post using Spring Boot's native @SchemaMapping.
 * This is only invoked when the GraphQL client requests the author field on a Post.
 */
@Controller
public class PostResolver {

    private final AuthorDao _authorDao;

    @Autowired
    public PostResolver(AuthorDao authorDao) {
        _authorDao = authorDao;
    }

    @SchemaMapping(typeName = "Post")
    public Optional<Author> author(Post post) {
        return _authorDao.getAuthorById(post.getAuthorId());
    }
}
