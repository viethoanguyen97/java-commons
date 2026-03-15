package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hovispace.javacommons.springgraphql.dao.AuthorDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Sometimes, the value of a field is non-trivial to load. This might involve database lookups, complex calculations, or anything else.
 * GraphQL Tools has a concept of a field resolver that is used for this purpose. These are Spring beans that can provide values in place of the data bean.
 *
 * The field resolver is any bean in the Spring Context that has the same name as the data bean, with the suffix Resolver, and that implements the GraphQLResolver interface.
 * Methods on the field resolver bean follow all of the same rules as on the data bean but are also provided the data bean itself as a first parameter.
 *
 * If a field resolver and the data bean both have methods for the same GraphQL field then the field resolver will take precedence.
 *
 * The fact that these field resolvers are loaded from the Spring context is important. This allows them to work with any other Spring managed beans – e.g., DAOs.
 *
 * Importantly, if the client does not request a field, then the GraphQL Server will never do the work to retrieve it.
 * This means that if a client retrieves a Post and does not ask for the Author, then the getAuthor() method above will never be executed, and the DAO call will never be made.
 */

public class PostResolver implements GraphQLResolver<Post> {

    private final AuthorDao _authorDao;

    @Autowired
    public PostResolver(AuthorDao authorDao) {
        _authorDao = authorDao;
    }

    public Optional<Author> getAuthor(Post post) {
        return _authorDao.getAuthorById(post.getAuthorId());
    }
}
