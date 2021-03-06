package com.example.vizsgaremek_asztali.cats;

import com.example.vizsgaremek_asztali.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CatApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/cat";
    private static Gson jsonConverted = new Gson();

    public static List<Cat> get() throws IOException {
        String json = Api.get(API_URL);
        Type type = new TypeToken<List<Cat>>(){}.getType();
        return jsonConverted.fromJson(json,type);
    }
    public static Cat post(Cat uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.post(API_URL, ujJson);
        return jsonConverted.fromJson(json, Cat.class);
    }

    public static boolean delete(int id) throws IOException {
        return Api.delete(API_URL, id).getResponseCode() == 204;
    }

    public static Cat put(Cat modosit) throws IOException {
        String modositandoJson = jsonConverted.toJson(modosit);
        String json = Api.put(API_URL,modosit.getId(), modositandoJson);
        return jsonConverted.fromJson(json, Cat.class);
    }

    public static int getCatCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/cat_count");
        return Integer.parseInt(countString);
    }

    public static int getAdoptedCatCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/adopted_cat_count");
        return Integer.parseInt(countString);
    }

    public static int getNotAdoptedCatCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/not_adopted_cat_count");
        return Integer.parseInt(countString);
    }
}
