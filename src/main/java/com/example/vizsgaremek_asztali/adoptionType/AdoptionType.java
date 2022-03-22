package com.example.vizsgaremek_asztali.adoptionType;

import com.google.gson.annotations.SerializedName;

public class AdoptionType {
    private int id;
    @SerializedName("type")
    private String type;


    public AdoptionType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s",this.getType());
    }
}
