package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.AuthorDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

/**
 * Sometimes, the value of a field is non-trivial to load. This might involve database lookups, complex calculations, or anything else.
 * Spring for GraphQL uses @SchemaMapping to resolve fields on types.
 *
 * The @SchemaMapping annotation maps a handler method to a field in the GraphQL schema.
 * The method receives the parent/container object as a parameter and returns the field value.
 *
 * If a @SchemaMapping and a property on the data object both exist for the same GraphQL field then the @SchemaMapping will take precedence.
 *
 * Importantly, if the client does not request a field, then the GraphQL Server will never do the work to retrieve it.
 * This means that if a client retrieves a Post and does not ask for the Author, then the author() method above will never be executed, and the DAO call will never be made.
 */
@Controller
public class PostResolver {

    private final AuthorDao _authorDao;

    @Autowired
    public PostResolver(AuthorDao authorDao) {
        _authorDao = authorDao;
    }

    @SchemaMapping(typeName = "Post", field = "author")
    public Optional<Author> author(Post post) {
        return _authorDao.getAuthorById(post.getAuthorId());
    }
}
