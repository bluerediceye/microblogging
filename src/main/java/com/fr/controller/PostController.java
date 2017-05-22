package com.fr.controller;

import com.fr.domain.Post;
import com.fr.domain.Rating;
import com.fr.exception.PostException;
import com.fr.exception.PostNotFoundException;
import com.fr.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created on 19/05/2017
 *
 * @author Ming Li
 */
@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String create(@RequestBody @Valid final Post post) throws PostException {
        String id = postService.create(post);
        if (id == null) {
            throw new PostException("Failed to create post");
        }
        return id;
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.GET)
    public Post retrieve(@PathVariable("postId") final String postId) throws PostNotFoundException {
        Post post = postService.retrieve(postId);

        if (post == null) {
            throw new PostNotFoundException("Post " + postId + " not found");
        }

        return postService.retrieve(postId);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<Post> retrieveByUserId(@PathVariable("userId") final String userId,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss") @RequestParam(name = "start", required = false) Date start,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss") @RequestParam(name = "end", required = false) Date end) {
        return postService.retrieve(userId, start, end);
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.PUT)
    public void update(@PathVariable("postId") final String postId, @RequestBody @Valid Post post) throws PostException {
        Post tmp = postService.retrieve(postId);

        if (tmp == null) {
            throw new PostNotFoundException("Post " + postId + " not found");
        }

        if (!postService.update(postId, post)) {
            throw new PostException("Post not updated");
        }
    }

    @RequestMapping(value = "/post/{postId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("postId") final String postId) throws PostException {

        Post tmp = postService.retrieve(postId);

        if (tmp == null) {
            throw new PostNotFoundException("Post " + postId + " not found");
        }

        if (!postService.delete(postId)) {
            throw new PostException("Post not deleted");
        }
    }

    @RequestMapping(value = "/post/search", method = RequestMethod.GET)
    public List<Post> fulltextSearch(@RequestParam(name = "query", required = true) final String query) {
        return postService.search(query);
    }

    @RequestMapping(value = "/post/{postId}/rating", method = RequestMethod.POST)
    public void rate(@PathVariable("postId") final String postId, @RequestBody @Valid final Rating rating) {
        Post tmp = postService.retrieve(postId);

        if (tmp == null) {
            throw new PostNotFoundException("Post " + postId + " not found");
        }

        postService.rate(postId, rating);
    }

    @RequestMapping(value = "/post/{postId}/ratings", method = RequestMethod.GET)
    public List<Rating> retrieveRatings(@PathVariable("postId") final String postId) throws PostNotFoundException {

        Post tmp = postService.retrieve(postId);

        if (tmp == null) {
            throw new PostNotFoundException("Post " + postId + " not found");
        }

        return postService.retrieveRatings(postId);
    }

}
