# spring-graphql

**A simple application with SpringBoot and GraphQL.**

Testing the application:
- Running the Spring Boot application.
- Open localhost:9001/graphiql.
- These are some queries you could try on GraphiQL screen.
     
```
query {
    recentPosts(count: 100, offset:0) {
        id, title, text, category, authorId
    }
}
```

```
mutation {
    writePost(title:"title test", text: "text", category: "category", author: "newAuthor") {
        id
    }
}
```

```
mutation {
    createVehicle(type: "car", modelCode: "XYZ0192", brandName: "XYZ", launchDate: "2016-08-16") {
        id
    }
}
```

```
query {
    vehicles(count: 1) {
        id, type, modelCode
    }
}
```
```
query {
    vehicle(id: 1) {
        id, type, modelCode
    }
}
```
