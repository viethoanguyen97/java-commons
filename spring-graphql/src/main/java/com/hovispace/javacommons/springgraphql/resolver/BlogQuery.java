package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * The root query needs to have special beans defined in the Spring context to handle the various fields in this root query.
 * Unlike the schema definition, there is no restriction that there only be a single Spring bean for the root query fields.
 *
 * The only requirements are that the beans implement GraphQLQueryResolver and
 * that every field in the root query from the scheme has a method in one of these classes with the same name.
 *
 * The names of the method must be one of the following, in this order:
 *
 * <field>
 * is<field> – only if the field is of type Boolean
 * get<field>
 *
 * The method must have parameters that correspond to any parameters in the GraphQL schema, and may optionally take a final parameter of type DataFetchingEnvironment.
 * The method must also return the correct return type for the type in the GraphQL scheme, as we are about to see.
 * Any simple types – String, Int, List, etc. – can be used with the equivalent Java types, and the system just maps them automatically.
 * The above defined the method getRecentPosts which will be used to handle any GraphQL queries for the recentPosts field in the schema defined earlier.
 */
public class BlogQuery implements GraphQLQueryResolver {

    private final PostDao _postDao;

    @Autowired
    public BlogQuery(PostDao postDao) {
        _postDao = postDao;
    }

    public List<Post> getRecentPosts(int count, int offset) {
        return _postDao.getRecentPosts(count, offset);
    }
}
