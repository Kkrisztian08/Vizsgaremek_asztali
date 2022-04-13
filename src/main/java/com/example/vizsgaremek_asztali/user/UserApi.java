package com.example.vizsgaremek_asztali.user;

import com.example.vizsgaremek_asztali.api.Api;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class UserApi {
    private static final String BASE_URL = "http://127.0.0.1:8000";
    private static final String API_URL = BASE_URL + "/api/users";
    private static Gson jsonConverted = new Gson();


    public static List<User> get() throws IOException {
        String json = Api.get(API_URL);
        Type type = new TypeToken<List<User>>(){}.getType();
        return jsonConverted.fromJson(json, type);
    }
    public static User post(User uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.post(API_URL, ujJson);
        return jsonConverted.fromJson(json, User.class);
    }

    public static boolean delete(int id) throws IOException {
        return Api.delete(API_URL, id).getResponseCode() == 204;
    }

    public static User put(User modosit) throws IOException {
        String modositandoJson = jsonConverted.toJson(modosit);
        String json = Api.put(API_URL,modosit.getId(), modositandoJson);
        return jsonConverted.fromJson(json, User.class);
    }


    public static int getUsersCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/user_count");
        return Integer.parseInt(countString);
    }

    public static int getAdminCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/admin_count");
        return Integer.parseInt(countString);
    }

    public static int getSuperAdminCount() throws IOException {
        String countString = Api.get(BASE_URL + "/api/super_admin_count");
        return Integer.parseInt(countString);
    }

    public static User adminJog(User uj) throws IOException {
        String ujJson = jsonConverted.toJson(uj);
        String json = Api.put(BASE_URL + "/api/adminPermission", uj.getId(), ujJson);
        return jsonConverted.fromJson(json, User.class);
    }
}
