package com.example.sapidigital.models;

import java.util.HashMap;
import java.util.Map;

public class FeedLotsModel {
    private String id;
    private String jenis_sapi;
    private String umur_sapi;
    private String gender;
    private String bobot_terakhir;
    private String tanggal_bobot;
    private String foto;
    private String riwayat;

    public FeedLotsModel(){

    }


    public FeedLotsModel(String id, String jenis_sapi, String umur_sapi, String gender, String bobot_terakhir, String tanggal_bobot, String foto, String riwayat) {
        this.id = id;
        this.jenis_sapi = jenis_sapi;
        this.umur_sapi = umur_sapi;
        this.gender = gender;
        this.bobot_terakhir = bobot_terakhir;
        this.tanggal_bobot = tanggal_bobot;
        this.foto = foto;
        this.riwayat = riwayat;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenis_sapi() {
        return jenis_sapi;
    }

    public void setJenis_sapi(String jenis_sapi) {
        this.jenis_sapi = jenis_sapi;
    }

    public String getUmur_sapi() {
        return umur_sapi;
    }

    public void setUmur_sapi(String umur_sapi) {
        this.umur_sapi = umur_sapi;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBobot_terakhir() {
        return bobot_terakhir;
    }

    public void setBobot_terakhir(String bobot_terakhir) {
        this.bobot_terakhir = bobot_terakhir;
    }

    public String getTanggal_bobot() {
        return tanggal_bobot;
    }

    public void setTanggal_bobot(String tanggal_bobot) {
        this.tanggal_bobot = tanggal_bobot;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRiwayat() {
        return riwayat;
    }

    public void setRiwayat(String riwayat) {
        this.riwayat = riwayat;
    }

    public Map toMap() {
        HashMap result = new HashMap<>();
        result.put("id", this.id);
        result.put("jenis_sapi", this.jenis_sapi);
        result.put("umur_sapi", this.umur_sapi);
        result.put("gender", this.gender);
        result.put("bobot_terakhir", this.bobot_terakhir);
        result.put("foto", this.foto);
        result.put("riwayat", this.riwayat);

        return result;
    }
}
