package com.example.vizsgaremek_asztali.programType;

import com.example.vizsgaremek_asztali.api.Api;
import com.example.vizsgaremek_asztali.event.Events;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ProgramTypeApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/type";
    private static Gson jsonConverted = new Gson();

    public static List<ProgramTypes> get() throws IOException {
        String json = Api.get(API_URL);
        Type type = new TypeToken<List<ProgramTypes>>(){}.getType();
        return jsonConverted.fromJson(json,type);
    }
    public static ProgramTypes post(ProgramTypes uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.post(API_URL, ujJson);
        return jsonConverted.fromJson(json, ProgramTypes.class);
    }

    public static boolean delete(int id) throws IOException {
        return Api.delete(API_URL, id).getResponseCode() == 240;
    }

    public static ProgramTypes put(ProgramTypes modosit) throws IOException {
        String modositandoJson = jsonConverted.toJson(modosit);
        String json = Api.put(API_URL,modosit.getId(), modositandoJson);
        return jsonConverted.fromJson(json, ProgramTypes.class);
    }
}
