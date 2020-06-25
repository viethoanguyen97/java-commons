package com.hovispace.javacommons.springgraphql.resolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * So far, everything that we have done has been about retrieving data from the server.
 * GraphQL also has the ability to update the data stored on the server, by means of mutations.
 *
 * From the point of view of the code, there is no reason that a Query can't change data on the server.
 * We could easily write query resolvers that accept arguments, save new data and return those changes.
 * Doing this will cause surprising side effects for the API clients, and is considered bad practice.
 *
 * Instead, Mutations should be used to inform the client that this will cause a change to the data being stored.
 *
 * Mutations are defined in the Java code by using classes that implement GraphQLMutationResolver instead of GraphQLQueryResolver.
 *
 * Otherwise, all of the same rules apply as for queries. The return value from a Mutation field is then treated exactly the same as from a Query field,
 * allowing nested values to be retrieved as well.
 */
public class Mutation implements GraphQLMutationResolver {

    private final PostDao _postDao;

    @Autowired
    public Mutation(PostDao postDao) {
        _postDao = postDao;
    }

    public Post writePost(String title, String text, String category, String author) {
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
