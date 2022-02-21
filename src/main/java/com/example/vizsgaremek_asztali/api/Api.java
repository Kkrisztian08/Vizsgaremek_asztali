package com.example.vizsgaremek_asztali.api;

import com.google.gson.Gson;

import java.io.IOException;

public class Api {public static String get(String url) throws IOException {
    Response response = RequestHandler.get(url);
    String json = response.getContent();
    Gson jsonConverter = new Gson();
    if (response.getResponseCode() > 400) {
        ApiError hiba = jsonConverter.fromJson(json, ApiError.class);
        String msg = hiba.getStatusCode() + " - " + hiba.getMessage();
        throw new IOException(msg);
    }
    return json;
}

    public static String post(String url, String ujJson) throws IOException {
        Gson jsonConverter = new Gson();
        Response response = RequestHandler.post(url, ujJson);
        String json = response.getContent();
        if (response.getResponseCode() > 400) {
            ApiError hiba = jsonConverter.fromJson(json, ApiError.class);
            String msg = hiba.getStatusCode() + " - " + hiba.getMessage();
            throw new IOException(msg);
        }
        return json;
    }

    public static Response delete(String url, int id) throws IOException {
        Response respone = RequestHandler.delete(url + "/" + id);
        Gson jsonConverter = new Gson();
        String json = respone.getContent();
        if (respone.getResponseCode() > 400) {
            ApiError hiba = jsonConverter.fromJson(json, ApiError.class);
            String msg = hiba.getStatusCode() + " - " + hiba.getMessage();
            throw new IOException(msg);
        }
        return respone;
    }
    public static String put(String url, String modositJson, int id) throws IOException {
        Gson jsonConverter = new Gson();
        Response response = RequestHandler.put(url + "/" + id, modositJson);
        String json = response.getContent();
        if (response.getResponseCode() > 400) {
            ApiError hiba = jsonConverter.fromJson(json, ApiError.class);
            String msg = hiba.getStatusCode() + " - " + hiba.getMessage();
            throw new IOException(msg);
        }
        return json;
    }
}
