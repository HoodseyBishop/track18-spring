package ru.track;

/**
 * TASK:
 * POST request to  https://guarded-mesa-31536.herokuapp.com/track
 * fields: name,github,email
 *
 * LIB: http://unirest.io/java.html
 *
 *
 */

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class App {

    public static final String URL = "http://guarded-mesa-31536.herokuapp.com/track";
    public static final String FIELD_NAME = "Ilya Unisov";
    public static final String FIELD_GITHUB = "https://github.com/HoodseyBishop";
    public static final String FIELD_EMAIL = "ilya.unisov@gmail.com";

    public static void main(String[] args) throws Exception {
        HttpResponse<JsonNode> r =  Unirest.post(URL)
                .field("name", FIELD_NAME)
                .field("github", FIELD_GITHUB)
                .field("email", FIELD_EMAIL)
                .asJson();
        System.out.println(r.getBody().getObject().get("success"));


        boolean success = false;
    }

}
