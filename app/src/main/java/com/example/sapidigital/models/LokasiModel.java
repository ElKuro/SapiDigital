package com.example.sapidigital.models;

import java.util.HashMap;
import java.util.Map;


public class LokasiModel {
    String tempat;
    String berat_sapi;
    String tgl;
    String id_fl;

    public LokasiModel() {
    }

    public LokasiModel(String tempat,String berat_sapi, String tgl,String id_fl) {
        this.tempat = tempat;
        this.berat_sapi =berat_sapi;
        this.tgl = tgl;
        this.id_fl = id_fl;
    }

    public String getId_fl() {
        return id_fl;
    }

    public void setId_fl(String id_fl) {
        this.id_fl = id_fl;
    }

    public String getBerat_sapi() {
        return berat_sapi;
    }

    public void setBerat_sapi(String berat_sapi) {
        this.berat_sapi = berat_sapi;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public Map toMap() {
        HashMap result = new HashMap<>();
        result.put("tempat", this.tempat);
        result.put("berat_sapi", this.berat_sapi);
        result.put("tgl", this.tgl);
        result.put("id_fl", this.id_fl);
        return result;
    }

}
