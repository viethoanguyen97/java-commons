package com.hovispace.javacommons.springgraphql.resolver;

import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorResolver {

    private final PostDao _postDao;

    @Autowired
    public AuthorResolver(PostDao postDao) {
        _postDao = postDao;
    }

    @SchemaMapping(typeName = "Author", field = "posts")
    public List<Post> posts(Author author) {
        return _postDao.getAuthorPosts(author.getId());
    }
}
