package com.progmasters.webshop.domain.dto;

import com.progmasters.webshop.domain.Comment;

import java.time.format.DateTimeFormatter;

public class CommentListItem {

    private Long id;

    private String author;

    private String text;

    private String createdAt;

    public CommentListItem() {
    }

    public CommentListItem(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor();
        this.text = comment.getText();
        if (comment.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.createdAt = formatter.format(comment.getCreatedAt());
        } else {
            this.createdAt = "N/A";
        }
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
