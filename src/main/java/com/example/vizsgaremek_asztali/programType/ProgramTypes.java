package com.example.vizsgaremek_asztali.programType;

import com.google.gson.annotations.SerializedName;

public class ProgramTypes {
    private int id;
    @SerializedName("program_topic")
    private String megnevezes;

    public ProgramTypes(int id, String megnevezes) {
        this.id = id;
        this.megnevezes = megnevezes;
    }

    public int getId() {
        return id;
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }
}
