package com.example.vizsgaremek_asztali.user;

import com.example.vizsgaremek_asztali.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class UserApi {
    private static final String API_URL = "http://127.0.0.1:8000/api";
    private static Gson jsonConverter = new Gson();


    public static List<User> get() throws IOException {
        String json = Api.get(API_URL + "/users");
        Type type = new TypeToken<List<User>>() {
        }.getType();
        return jsonConverter.fromJson(json, type);
    }
}
