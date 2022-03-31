package com.example.vizsgaremek_asztali.programInfo;

import com.google.gson.annotations.SerializedName;

public class ProgramInfo {
    private int id;
    private String type;
    @SerializedName("date")
    private String valasztottDatum;
    @SerializedName("time")
    private String ido;

    public ProgramInfo(int id, String type, String valasztottDatum, String ido) {
        this.id = id;
        this.type = type;
        this.valasztottDatum = valasztottDatum;
        this.ido = ido;
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

    public String getValasztottDatum() {
        return valasztottDatum;
    }

    public void setValasztottDatum(String valasztottDatum) {
        this.valasztottDatum = valasztottDatum;
    }

    public String getIdo() {
        return ido;
    }

    public void setIdo(String ido) {
        this.ido = ido;
    }

    @Override
    public String toString() {
        return String.format("%s, %s",this.getValasztottDatum(),this.getIdo());
    }


}
