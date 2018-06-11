package com.progmasters.webshop.controller;

import com.progmasters.webshop.domain.Comment;
import com.progmasters.webshop.domain.dto.CommentCreationData;
import com.progmasters.webshop.domain.dto.CommentListItem;
import com.progmasters.webshop.service.CommentService;
import com.progmasters.webshop.validator.CommentCreationDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final CommentCreationDataValidator commentCreationDataValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(commentCreationDataValidator);
    }

    @Autowired
    public CommentController(CommentService commentService, CommentCreationDataValidator commentCreationDataValidator) {
        this.commentService = commentService;
        this.commentCreationDataValidator = commentCreationDataValidator;
    }

    @GetMapping("/list") // - accessible to all registered users
    public ResponseEntity<List<CommentListItem>> findAll() {
        logger.info("Comment list page is requested");
        return new ResponseEntity<>(commentService.listAll(), HttpStatus.OK);
    }

    @GetMapping("/list/{productId}")
    public ResponseEntity<List<CommentListItem>> findAllByProductId(@PathVariable Long productId) {
        logger.info("Comment list for product page is requested");
        return new ResponseEntity<>(commentService.listAllByProductId(productId), HttpStatus.OK);
    }

    @PostMapping // - accessible to all registered users
    public ResponseEntity<?> saveComment(@Valid @RequestBody CommentCreationData commentCreationData) {
        logger.info("New comment is created");
        Comment savedComment = commentService.saveComment(commentCreationData);
        ResponseEntity<CommentListItem> result;

        if (savedComment != null) {
            result = new ResponseEntity<>(new CommentListItem(savedComment), HttpStatus.CREATED);
        } else {
            result = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return result;
    }
}
