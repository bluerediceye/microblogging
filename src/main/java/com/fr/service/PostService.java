package com.fr.service;

import com.fr.domain.Post;
import com.fr.domain.Rating;

import java.util.Date;
import java.util.List;

/**
 * Created on 19/05/2017
 *
 * @author Ming Li
 */
public interface PostService {
    String create(Post post);

    boolean update(String id, Post post);

    Post retrieve(String id);

    List<Post> retrieve(final String userId, final Date start, final Date end);

    boolean delete(String id);

    List<Post> search(String query);

    void rate(String id, Rating rating);

    List<Rating> retrieveRatings(final String postId);
}
