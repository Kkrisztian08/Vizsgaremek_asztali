package com.example.vizsgaremek_asztali.adoption;

import com.example.vizsgaremek_asztali.api.Api;
import com.example.vizsgaremek_asztali.cats.Cats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AdoptionApi {
    private static final String BASE_URL="http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/adoption";
    private static Gson jsonConverted = new Gson();


}
