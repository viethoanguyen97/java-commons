package com.hovispace.javacommons.springgraphql;

import com.hovispace.javacommons.springgraphql.dao.AuthorDao;
import com.hovispace.javacommons.springgraphql.entity.Author;
import com.hovispace.javacommons.springgraphql.entity.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthorDaoUnitTest {

    @Test
    public void test_that_getByAuthorId_returns_expected_data() throws Exception {
        //arrange
        Post post = mock(Post.class);
        Author author1 = spy(Author.class);
        author1.setId("id1");
        Author author2 = spy(Author.class);
        author2.setId("id2");
        AuthorDao authorDao = new AuthorDao(asList(author1, author2));

        //act
        Optional<Author> existingAuthor = authorDao.getAuthorById("id1");
        Optional<Author> nonexistentAuthor = authorDao.getAuthorById("id3");

        //assert
        assertThat(existingAuthor).isPresent();
        assertThat(nonexistentAuthor).isNotPresent();
    }
}
