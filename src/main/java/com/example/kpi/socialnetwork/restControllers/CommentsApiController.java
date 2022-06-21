package com.example.kpi.socialnetwork.restControllers;

import com.example.kpi.socialnetwork.model.Responses.CommentReponse;
import com.example.kpi.socialnetwork.model.Responses.HtmlResponse;
import com.example.kpi.socialnetwork.model.dto.CommentDto;
import com.example.kpi.socialnetwork.model.json.CommentLight;
import com.example.kpi.socialnetwork.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@RestController
@RequestMapping("api/comments")
public class CommentsApiController {
    private final CommentService commentService;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public CommentsApiController(CommentService commentService, SpringTemplateEngine templateEngine) {
        this.commentService = commentService;
        this.templateEngine = templateEngine;
    }

    @PutMapping("/create")
    public ResponseEntity<Long> createComment(Model model, @RequestParam("postId") Long postId, @RequestParam("commentContent") String content){
        return new ResponseEntity<>(commentService.createComment(postId, content).getId(), HttpStatus.OK);
    }

    @MessageMapping("/comments")
    @SendTo({"/topic/comments"})
    public CommentReponse send(CommentDto commentDto)
    {
        var comment = commentService.findById(commentDto.getId());
        var context = new Context();
        context.setVariable("comment", new CommentLight(comment));
        return new CommentReponse(templateEngine.process("fragments/new-comment", context), commentDto.getPostId());
    }
}
