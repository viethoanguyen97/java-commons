package com.hovispace.javacommons.springgraphql.entity;

/**
 * Every complex type in the GraphQL server is represented by a Java bean – whether loaded from the root query or from anywhere else in the structure.
 * The same Java class must always represent the same GraphQL type, but the name of the class is not necessary.
 * Fields inside the Java bean will directly map onto fields in the GraphQL response based on the name of the field.
 *
 * Any fields or methods on the Java bean that do not map on to the GraphQL schema will be ignored, but will not cause problems. This is important for field resolvers to work.
 * For example, the field authorId here does not correspond to anything in our schema we defined earlier, but it will be available to use for the next step.
 */
public class Post {

    private String id;
    private String title;
    private String text;
    private String category;
    private String authorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
