package com.progmasters.webshop.service;

import com.progmasters.webshop.domain.Comment;
import com.progmasters.webshop.domain.Product;
import com.progmasters.webshop.domain.dto.CommentCreationData;
import com.progmasters.webshop.domain.dto.CommentListItem;
import com.progmasters.webshop.repository.CommentRepository;
import com.progmasters.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private CommentRepository commentRepository;
    private ProductRepository productRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    public List<CommentListItem> listAll() {
        List<Comment> comments = commentRepository.findByOrderByCreatedAtDesc();
        List<CommentListItem> commentCreationDataList = new ArrayList<>();
        for (Comment comment : comments) {
            commentCreationDataList.add(new CommentListItem(comment));
        }

        return commentCreationDataList;
    }

    public List<CommentListItem> listAllByProductId(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        List<CommentListItem> commentCreationDataList = new ArrayList<>();

        for (Comment comment : comments) {
            commentCreationDataList.add(new CommentListItem(comment));
        }

        return commentCreationDataList;
    }

    public Comment saveComment(CommentCreationData commentCreationData) {
        Comment comment = new Comment();
        return updateValues(commentCreationData, comment);
    }

    public Comment updateComment(CommentCreationData commentCreationData, Long id) {
        Comment comment = commentRepository.findOne(id);
        if (comment != null) {
            updateValues(commentCreationData, comment);
        }

        return comment;
    }

    private Comment updateValues(CommentCreationData commentCreationData, Comment comment) {
        Product product = productRepository.findOne(commentCreationData.getProductId());
        comment.setAuthor(commentCreationData.getAuthor());
        comment.setText(commentCreationData.getText());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setProduct(product);

        Comment savedComment;
        if (comment.getId() != null) {
            savedComment = comment;
        } else {
            savedComment = commentRepository.save(comment);
        }

        return savedComment;
    }


    public void deleteAll(Long id) {
        commentRepository.deleteByProductId(id);
    }
}
