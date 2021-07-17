package com.example.sapidigital.models;

import java.util.HashMap;
import java.util.Map;

public class PemeriksaanModel {
    String berat_sapi;
    String name;
    String tgl;
    String hasil_pemeriksaan;
    String surat_pemeriksaan;
    String ket;
    String fl_id;

    public PemeriksaanModel() {
    }

    public PemeriksaanModel(String berat_sapi, String name, String tgl, String hasil_pemeriksaan, String surat_pemeriksaan, String ket,  String fl_id) {
        this.berat_sapi = berat_sapi;
        this.name = name;
        this.tgl = tgl;
        this.hasil_pemeriksaan = hasil_pemeriksaan;
        this.surat_pemeriksaan = surat_pemeriksaan;
        this.ket = ket;
        this.fl_id = fl_id;
    }

    public String getBerat_sapi() {
        return berat_sapi;
    }

    public void setBerat_sapi(String berat_sapi) {
        this.berat_sapi = berat_sapi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getHasil_pemeriksaan() {
        return hasil_pemeriksaan;
    }

    public void setHasil_pemeriksaan(String hasil_pemeriksaan) {
        this.hasil_pemeriksaan = hasil_pemeriksaan;
    }

    public String getSurat_pemeriksaan() {
        return surat_pemeriksaan;
    }

    public void setSurat_pemeriksaan(String surat_pemeriksaan) {
        this.surat_pemeriksaan = surat_pemeriksaan;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }


    public String getFl_id() {
        return fl_id;
    }

    public void setFl_id(String fl_id) {
        this.fl_id = fl_id;
    }

    public Map toMap() {
        HashMap result = new HashMap<>();
        result.put("berat_sapi", this.berat_sapi);
        result.put("fl_id", this.fl_id);
        result.put("hasil_pemeriksaan", this.hasil_pemeriksaan);
        result.put("ket", this.ket);
        result.put("name", this.name);
        result.put("surat_pemeriksaan", this.surat_pemeriksaan);
        result.put("tgl", this.tgl);
        return result;
    }

}
