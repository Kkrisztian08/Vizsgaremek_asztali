package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.api.Api;
import com.example.vizsgaremek_asztali.api.ApiError;
import com.example.vizsgaremek_asztali.api.RequestHandler;
import com.example.vizsgaremek_asztali.api.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DogApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/dog";
    private static Gson jsonConverted = new Gson();

    public static List<Dogs> get() throws IOException {
        String json = Api.get(API_URL);
        Type type = new TypeToken<List<Dogs>>(){}.getType();
        return jsonConverted.fromJson(json,type);
    }
    public static Dogs post(Dogs uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.post(API_URL, ujJson);
        return jsonConverted.fromJson(json, Dogs.class);
    }

    public static boolean delete(int id) throws IOException {
        return Api.delete(API_URL, id).getResponseCode() == 240;
    }

    public static Dogs put(Dogs modosit, int id) throws IOException {
        String modositandoJson = jsonConverted.toJson(modosit);
        String json = Api.put(API_URL, modositandoJson, id);
        return jsonConverted.fromJson(json, Dogs.class);
    }
}
