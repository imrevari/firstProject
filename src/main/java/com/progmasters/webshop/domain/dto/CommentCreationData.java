package com.progmasters.webshop.domain.dto;


public class CommentCreationData {

    private String author;

    private String text;

    private Long productId;

    public CommentCreationData() {
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Long getProductId() {
        return productId;
    }
}
