package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.CommentDto;
import song.devlog1.dto.EditCommentDto;
import song.devlog1.dto.SaveCommentDto;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.CommentService;

@Slf4j
@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping("/save")
    public CommentDto postSaveComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestBody SaveCommentDto saveCommentDto) {
        Long commentId = commentService.saveComment(userDetails.getId(), saveCommentDto);

        CommentDto commentDto = commentService.findCommentById(commentId);

        return commentDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/{id}/edit")
    public CommentDto postEditComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable(value = "id") Long commentId,
                                      @RequestBody EditCommentDto editCommentDto) {
        Long id = commentService.editComment(userDetails.getId(), commentId, editCommentDto);

        CommentDto commentDto = commentService.findCommentById(id);

        return commentDto;
    }

    @PostMapping("{id}/delete")
    public void postDeleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @PathVariable(value = "id") Long commentId) {
        commentService.deleteComment(userDetails.getId(), commentId);

    }
}
