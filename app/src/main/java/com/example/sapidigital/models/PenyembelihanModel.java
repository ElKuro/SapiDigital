package com.example.sapidigital.models;

import java.util.HashMap;
import java.util.Map;


public class PenyembelihanModel {
    String name;
    String berat_daging;
    String tgl;
    String vidio;
    String fl_id;

    public PenyembelihanModel() {
    }

    public PenyembelihanModel( String name,String berat_daging, String tgl, String vidio,  String fl_id) {
        this.name = name;
        this.berat_daging = berat_daging;
        this.tgl = tgl;
        this.vidio = vidio;
        this.fl_id = fl_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBerat_daging() {
        return berat_daging;
    }

    public void setBerat_daging(String berat_daging) {
        this.berat_daging = berat_daging;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getVidio() {
        return vidio;
    }

    public void setVidio(String vidio) {
        this.vidio = vidio;
    }

    public String getFl_id() {
        return fl_id;
    }

    public void setFl_id(String fl_id) {
        this.fl_id = fl_id;
    }

    public Map toMap() {
        HashMap result = new HashMap<>();
        result.put("fl_id", this.fl_id);
        result.put("vidio", this.vidio);
        result.put("name", this.name);
        result.put("tgl", this.tgl);
        result.put("berat_daging", this.berat_daging);
        return result;
    }

}
