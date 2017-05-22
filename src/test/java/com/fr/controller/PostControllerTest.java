package com.fr.controller;

import com.fr.MicroBloggingApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Base64Utils;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created on 21/05/2017
 *
 * @author Ming Li
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroBloggingApplication.class)
@WebAppConfiguration
@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";

        mockMvc.perform(post("/post").content(post).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void retrieve() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String postId = result.getResponse().getContentAsString();


        mockMvc.perform(get("/post/" + postId).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void retrieveByUserId() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();


        mockMvc.perform(get("/user/1234").header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(result.getResponse().getContentAsString())));

    }

    @Test
    public void update() throws Exception {

        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String postId = result.getResponse().getContentAsString();

        String updatedPost = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a modified post\"," +
                "\"userId\": \"1234\"" +
                "}";
        mockMvc.perform(put("/post/" + postId).content(updatedPost)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/post/" + postId).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("This is a modified post")));
    }

    @Test
    public void deletePost() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String postId = result.getResponse().getContentAsString();

        mockMvc.perform(delete("/post/" + postId).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/post/" + postId).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    public void fulltextSearch() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        mockMvc.perform(get("/post/search").param("query", "good post")
                .header("AUTHORIZATION", basicAuth()))
                .andExpect(jsonPath("$.[0].id", is(result.getResponse().getContentAsString())));
    }

    @Test
    public void rate() throws Exception {
        String post = "{" +
                "\"title\": \"my first post\"," +
                "\"body\": \"This is a good post\"," +
                "\"userId\": \"1234\"" +
                "}";
        MvcResult result = mockMvc.perform(post("/post").content(post)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String postId = result.getResponse().getContentAsString();

        String rating = "{" +
                "\"userId\":\"9999\"," +
                "\"rate\": 3" +
                "}";
        mockMvc.perform(post("/post/" + postId +"/rating").content(rating)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        String ratingFromSameUser = "{" +
                "\"userId\":\"9999\"," +
                "\"rate\": 9" +
                "}";
        mockMvc.perform(post("/post/" + postId +"/rating").content(ratingFromSameUser)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        String rating2 = "{" +
                "\"userId\":\"0000\"," +
                "\"rate\": 4" +
                "}";
        mockMvc.perform(post("/post/" + postId +"/rating").content(rating2)
                .header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        mockMvc.perform(get("/post/" + postId).header("AUTHORIZATION", basicAuth())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$.ratings.[0].rate", is(9)))
                .andExpect(jsonPath("$.ratings.[0].userId", is("9999")))
                .andExpect(jsonPath("$.ratings.[1].rate", is(4)))
                .andExpect(jsonPath("$.ratings.[1].userId", is("0000")));
    }

    private String basicAuth(){
        return "Basic " +  Base64Utils.encodeToString("admin:admin".getBytes());
    }

}