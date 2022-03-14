package com.example.vizsgaremek_asztali.event;

import com.google.gson.annotations.SerializedName;

public class Events {

    private int id;
    @SerializedName("event_name")
    private String elnevezes;
    @SerializedName("description")
    private String leiras;
    @SerializedName("date")
    private String datum;

    public Events(int id, String elnevezes, String leiras, String datum) {
        this.id = id;
        this.elnevezes = elnevezes;
        this.leiras = leiras;
        this.datum = datum;
    }

    public int getId() {
        return id;
    }

    public String getElnevezes() {
        return elnevezes;
    }

    public void setElnevezes(String elnevezes) {
        this.elnevezes = elnevezes;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
