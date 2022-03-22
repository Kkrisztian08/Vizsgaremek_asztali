package com.example.vizsgaremek_asztali.programHourDay;

import com.google.gson.annotations.SerializedName;

public class ProgramHourDay {
    private int id;
    @SerializedName("selected_date")
    private String valasztottDatum;
    @SerializedName("time")
    private String ido;

    public ProgramHourDay(int id, String valasztottdatum, String ido) {
        this.id = id;
        this.valasztottDatum = valasztottdatum;
        this.ido = ido;
    }

    public int getId() {
        return id;
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
        return String.format("%s %s",this.getValasztottDatum(),this.getIdo());
    }
}
