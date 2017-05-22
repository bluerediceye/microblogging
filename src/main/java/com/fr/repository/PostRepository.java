package com.fr.repository;

import com.fr.domain.Post;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created on 19/05/2017
 *
 * @author Ming Li
 */
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findPostsByUserIdAndTimestampBetweenOrderByTimestampDesc(final String userId, final Date start, final Date end);

    List<Post> findPostsByUserIdAndTimestampIsLessThanEqualOrderByTimestampDesc(final String userId, final Date end);

    List<Post> findPostsByUserIdAndTimestampIsGreaterThanEqualOrderByTimestampDesc(final String userId, final Date start);

    List<Post> findPostsByUserIdOrderByTimestampDesc(final String userId);

    List<Post> findByOrderByTimestampDesc(final TextCriteria criteria);
}
