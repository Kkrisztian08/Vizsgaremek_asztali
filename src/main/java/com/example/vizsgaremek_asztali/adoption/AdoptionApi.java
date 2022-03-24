package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AdoptionApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/adoption";
    private static Gson jsonConverted = new Gson();

    public static List<Adoption> get() throws IOException {
        String json = Api.get(API_URL);
        Type type = new TypeToken<List<Adoption>>(){}.getType();
        return jsonConverted.fromJson(json,type);
    }
    public static Adoption post(Adoption uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.post(API_URL, ujJson);
        return jsonConverted.fromJson(json, Adoption.class);
    }

    public static boolean delete(int id) throws IOException {
        return Api.delete(API_URL, id).getResponseCode() == 204;
    }

    public static Adoption put(Adoption modosit) throws IOException {
        String modositandoJson = jsonConverted.toJson(modosit);
        String json = Api.put(API_URL,modosit.getId(), modositandoJson);
        return jsonConverted.fromJson(json, Adoption.class);
    }
}
