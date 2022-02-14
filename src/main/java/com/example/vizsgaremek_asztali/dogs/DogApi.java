package com.example.vizsgaremek_asztali.dogs;

import com.example.vizsgaremek_asztali.ApiError;
import com.example.vizsgaremek_asztali.RequestHandler;
import com.example.vizsgaremek_asztali.Response;
import com.example.vizsgaremek_asztali.dogs.Dogs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class DogApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String ELETHANG_API_URL = BASE_URL + "/api/dog";

    public static List<Dogs> getDogs() throws IOException {
        Response response = RequestHandler.get(ELETHANG_API_URL);
        String json = response.getContent();
        Gson jsonConverted = new Gson();
        if (response.getResponseCode() >=400){
            System.out.println(json);
            String message = jsonConverted.fromJson(json, ApiError.class).getMessage();
            throw  new IOException(message);
        }
        Type type = new TypeToken<List<Dogs>>(){}.getType();
        return jsonConverted.fromJson(json,type);
    }

    public static boolean kutyaTorlese(int id) throws IOException {
        Response response=RequestHandler.delete(ELETHANG_API_URL+"/"+id);
        Gson jsonConverted = new Gson();
        String json = response.getContent();
        if (response.getResponseCode() >=400){
            System.out.println(json);
            String message = jsonConverted.fromJson(json, ApiError.class).getMessage();
            throw  new IOException(message);
        }
        return response.getResponseCode()==204;
    }
}
