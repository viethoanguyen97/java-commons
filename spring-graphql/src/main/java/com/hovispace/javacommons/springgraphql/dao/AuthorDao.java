package com.hovispace.javacommons.springgraphql.dao;

import com.hovispace.javacommons.springgraphql.entity.Author;

import java.util.List;
import java.util.Optional;

public class AuthorDao {

    private final List<Author> authors;

    public AuthorDao(List<Author> authors) {
        this.authors = authors;
    }

    public Optional<Author> getAuthorById(String id) {
        return authors.stream().filter(author -> id.equals(author.getId())).findFirst();
    }
}
