package com.progmasters.webshop.validator;

import com.progmasters.webshop.domain.dto.CommentCreationData;
import com.progmasters.webshop.service.CommentService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CommentCreationDataValidator implements Validator {

    private final CommentService commentService;

    public CommentCreationDataValidator(CommentService commentService) {
        this.commentService = commentService;
    }

    public boolean supports(Class clazz) {
        return CommentCreationData.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        if (obj != null) {
            CommentCreationData commentCreationData = (CommentCreationData) obj;

            if (commentCreationData.getAuthor().equals("")) {
                e.rejectValue("author", "author.mustGive");
            }

            if (commentCreationData.getText().equals("")) {
                e.rejectValue("text", "commentText.mustGive");
            }

        }
    }
}
