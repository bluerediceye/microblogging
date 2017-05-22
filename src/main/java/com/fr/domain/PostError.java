package com.fr.domain;

/**
 * Created on 21/05/2017
 *
 * @author Ming Li
 */
public class PostError {

    private String message;

    public PostError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
