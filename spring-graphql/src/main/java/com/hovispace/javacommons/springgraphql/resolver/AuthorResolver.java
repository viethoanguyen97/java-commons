package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL field resolver for the Author type.
 * Resolves the 'posts' field on Author using Spring Boot's native @SchemaMapping.
 * This is only invoked when the GraphQL client requests the posts field on an Author.
 */
@Controller
public class AuthorResolver {

    private final PostDao _postDao;

    @Autowired
    public AuthorResolver(PostDao postDao) {
        _postDao = postDao;
    }

    @SchemaMapping(typeName = "Author")
    public List<Post> posts(Author author) {
        return _postDao.getAuthorPosts(author.getId());
    }
}
