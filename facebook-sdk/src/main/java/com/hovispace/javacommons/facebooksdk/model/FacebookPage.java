package com.hovispace.javacommons.facebooksdk.model;

import javax.persistence.*;

@Entity
public class FacebookPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pageId;

    @Column(nullable = false)
    private String pageName;

    @Column(nullable = false)
    private String accessToken;

    public FacebookPage() {
    }

    public FacebookPage(String pageId, String pageName, String accessToken) {
        this.pageId = pageId;
        this.pageName = pageName;
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
