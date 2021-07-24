package com.example.sapidigital.models;

import java.util.HashMap;
import java.util.Map;


public class PenyembelihanModel {
    String warna_daging;
    String warna_lemak;
    String marbling;
    String name;
    String berat_daging;
    String tgl;
    String vidio;
    String fl_id;

    public PenyembelihanModel() {
    }

    public PenyembelihanModel( String warna_daging, String warna_lemak, String marbling, String name,String berat_daging, String tgl, String vidio,  String fl_id) {
        this.warna_daging = warna_daging;
        this.warna_lemak = warna_lemak;
        this.marbling = marbling;
        this.name = name;
        this.berat_daging = berat_daging;
        this.tgl = tgl;
        this.vidio = vidio;
        this.fl_id = fl_id;
    }

    public String getWarna_daging() {
        return warna_daging;
    }

    public void setWarna_daging(String warna_daging) {
        this.warna_daging = warna_daging;
    }

    public String getWarna_lemak() {
        return warna_lemak;
    }

    public void setWarna_lemak(String warna_lemak) {
        this.warna_lemak = warna_lemak;
    }

    public String getMarbling() {
        return marbling;
    }

    public void setMarbling(String marbling) {
        this.marbling = marbling;
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
        result.put("warna_daging", this.warna_daging);
        result.put("warna_lemak", this.warna_lemak);
        result.put("marbling", this.marbling);
        result.put("fl_id", this.fl_id);
        result.put("vidio", this.vidio);
        result.put("name", this.name);
        result.put("tgl", this.tgl);
        result.put("berat_daging", this.berat_daging);
        return result;
    }

}
