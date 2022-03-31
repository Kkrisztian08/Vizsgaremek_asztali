package com.example.vizsgaremek_asztali.programApplication;

import com.google.gson.annotations.SerializedName;

public class ProgramApplication {
    private int id;
    @SerializedName("program_info_id")
    private int programInfo;
    @SerializedName("user_id")
    private int userid;

    public ProgramApplication(int id, int programInfo, int userid) {
        this.id = id;
        this.programInfo = programInfo;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public int getProgramInfo() {
        return programInfo;
    }

    public void setProgramInfo(int programInfo) {
        this.programInfo = programInfo;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
