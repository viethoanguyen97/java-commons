package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * The root query needs to have special beans defined in the Spring context to handle the various fields in this root query.
 * Unlike the schema definition, there is no restriction that there only be a single Spring bean for the root query fields.
 *
 * With Spring for GraphQL, controllers use @QueryMapping to map methods to GraphQL query fields.
 * The method name must match the field name in the schema (or be specified via the annotation's name attribute).
 *
 * The method must have parameters that correspond to any parameters in the GraphQL schema, annotated with @Argument.
 * The method must also return the correct return type for the type in the GraphQL scheme, as we are about to see.
 * Any simple types – String, Int, List, etc. – can be used with the equivalent Java types, and the system just maps them automatically.
 */
@Controller
public class BlogQuery {

    private final PostDao _postDao;

    @Autowired
    public BlogQuery(PostDao postDao) {
        _postDao = postDao;
    }

    @QueryMapping
    public List<Post> recentPosts(@Argument int count, @Argument int offset) {
        return _postDao.getRecentPosts(count, offset);
    }
}
