package com.hovispace.javacommons.springgraphql.configuration;

import com.hovispace.javacommons.springgraphql.dao.AuthorDao;
import com.hovispace.javacommons.springgraphql.dao.PostDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import com.hovispace.javacommons.springgraphql.resolver.AuthorResolver;
import com.hovispace.javacommons.springgraphql.resolver.Mutation;
import com.hovispace.javacommons.springgraphql.resolver.PostResolver;
import com.hovispace.javacommons.springgraphql.resolver.Query;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * In order to make this module be easier to setup & run, there will be no actual established connection to the database
 */
@Configuration
public class SpringGraphQLConfiguration {

    @Bean
    public PostDao postDao() {
        List<Post> posts = new ArrayList<>();
        for (int postId = 0; postId < 10; ++postId) {
            for (int authorId = 0; authorId < 10; ++authorId) {
                Post post = new Post();
                post.setId("Post" + authorId + postId);
                post.setTitle("Post " + authorId + ":" + postId);
                post.setText("Post " + postId + " + by author " + authorId);
                post.setAuthorId("Author" + authorId);
                posts.add(post);
            }
        }
        return new PostDao(posts);
    }

    @Bean
    public AuthorDao authorDao() {
        List<Author> authors = new ArrayList<>();
        for (int authorId = 0; authorId < 10; ++authorId) {
            Author author = new Author();
            author.setId("Author" + authorId);
            author.setName("Author " + authorId);
            author.setThumbnail("http://example.com/authors/" + authorId);
            authors.add(author);
        }
        return new AuthorDao(authors);
    }

    @Bean
    public PostResolver postResolver(AuthorDao authorDao) {
        return new PostResolver(authorDao);
    }

    @Bean
    public AuthorResolver authorResolver(PostDao postDao) {
        return new AuthorResolver(postDao);
    }

    @Bean
    public Query query(PostDao postDao) {
        return new Query(postDao);
    }

    @Bean
    public Mutation mutation(PostDao postDao) {
        return new Mutation(postDao);
    }
}
