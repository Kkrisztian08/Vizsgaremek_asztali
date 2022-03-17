package com.example.vizsgaremek_asztali.login;

import com.example.vizsgaremek_asztali.Token;
import com.example.vizsgaremek_asztali.User;
import com.example.vizsgaremek_asztali.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

public class LoginApi {
    private static final String API_URL = "http://127.0.0.1:8000/api";
    private static Gson jsonConverter = new Gson();

    public static Token postLogin(Login login) throws IOException {
        String loginJson = jsonConverter.toJson(login);
        String json = Api.post(API_URL + "/user/login", loginJson);
        return jsonConverter.fromJson(json, Token.class);
    }

    public static User getLoginData(String token) throws IOException {
        String json = Api.getLogin(API_URL + "/user", token);
        Type type = new TypeToken<User>() {
        }.getType();
        return jsonConverter.fromJson(json, type);
    }


}
