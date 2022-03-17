package com.example.vizsgaremek_asztali.programApplication;

import com.google.gson.annotations.SerializedName;

public class ProgramApplication {
    private int id;
    @SerializedName("program_hour_and_day_id")
    private int programHDid;
    @SerializedName("user_id")
    private int userid;
    @SerializedName("program_type_id")
    private int programTypeid;

    public ProgramApplication(int id, int programHDid, int userid, int programTypeid) {
        this.id = id;
        this.programHDid = programHDid;
        this.userid = userid;
        this.programTypeid = programTypeid;
    }

    public int getId() {
        return id;
    }

    public int getProgramHDid() {
        return programHDid;
    }

    public void setProgramHDid(int programHDid) {
        this.programHDid = programHDid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getProgramTypeid() {
        return programTypeid;
    }

    public void setProgramTypeid(int programTypeid) {
        this.programTypeid = programTypeid;
    }
}
