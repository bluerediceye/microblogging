package com.fr.service;

import com.fr.domain.Post;
import com.fr.domain.Rating;
import com.fr.repository.PostRepository;
import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created on 19/05/2017
 *
 * @author Ming Lio
 */
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, MongoTemplate mongoTemplate) {
        this.postRepository = postRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String create(final Post post) {
        post.setTimestamp(new Date());
        post.setRatings(new ArrayList<>());
        Post tmp = postRepository.save(post);
        return tmp.getId();
    }

    @Override
    public boolean update(final String id, final Post post) {
        Post tmp = postRepository.findOne(id);

        if (tmp != null) {
            tmp.setBody(post.getBody());
            tmp.setTitle(post.getTitle());
            tmp.setTimestamp(new Date());
            postRepository.save(tmp);
            return true;
        }

        return false;
    }

    @Override
    public Post retrieve(final String id) {
        return postRepository.findOne(id);
    }

    @Override
    public List<Post> retrieve(final String userId, final Date start, final Date end) {

        if (start == null && end == null) {
            return postRepository.findPostsByUserIdOrderByTimestampDesc(userId);
        }

        if (start == null) {
            return postRepository.findPostsByUserIdAndTimestampIsLessThanEqualOrderByTimestampDesc(userId, end);
        }

        if (end == null) {
            return postRepository.findPostsByUserIdAndTimestampIsGreaterThanEqualOrderByTimestampDesc(userId, start);
        }

        return postRepository.findPostsByUserIdAndTimestampBetweenOrderByTimestampDesc(userId, start, end);
    }

    @Override
    public boolean delete(final String id) {
        if (postRepository.exists(id)) {
            postRepository.delete(id);
            return true;
        }
        return false;

    }

    @Override
    public List<Post> search(final String query) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingPhrase(query);
        return postRepository.findByOrderByTimestampDesc(criteria);
    }

    /**
     * Rate a post given a post id. A user only can have one rating for a post.
     * @param postId post id
     * @param rating rating from a user.
     */
    @Override
    public void rate(String postId, Rating rating) {
        Criteria criteria = Criteria.where("_id").is(postId);
        Query query = new Query(criteria);
        query.addCriteria(Criteria.where("ratings").elemMatch(Criteria.where("userId").is(rating.getUserId())));

        if (mongoTemplate.exists(query, Post.class)) {
            Update remove =
                    new Update().pull("ratings",
                            new BasicDBObject("userId", rating.getUserId()));
            mongoTemplate.updateMulti(query, remove, Post.class);
        }

        rating.setTimestamp(new Date());
        Update update = new Update();
        update.addToSet("ratings", rating);
        criteria = Criteria.where("_id").is(postId);
        mongoTemplate.updateFirst(Query.query(criteria), update, "posts");
    }

    @Override
    public List<Rating> retrieveRatings(String postId) {
        Post post = postRepository.findOne(postId);
        if (post != null) {
            return post.getRatings();
        }

        return new ArrayList<>();
    }
}
