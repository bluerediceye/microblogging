package com.fr;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

/**
 * Created on 20/05/2017
 *
 * @author Ming Li
 */
@EnableMongoRepositories
public class MongoDBConfig {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    /*
   * Use the standard Mongo driver API to create a com.mongodb.Mongo instance.
   */
    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new MongoClient(host, port);
    }


    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "myMongoDB");
    }
}
