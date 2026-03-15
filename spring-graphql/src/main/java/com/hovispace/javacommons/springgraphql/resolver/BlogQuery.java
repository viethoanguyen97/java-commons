package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * GraphQL query resolver for blog-related queries.
 * Uses Spring Boot's native GraphQL support (@QueryMapping) instead of graphql-java-kickstart.
 * Method names must match the GraphQL schema field names.
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
