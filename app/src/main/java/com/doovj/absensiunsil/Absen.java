package com.doovj.absensiunsil;

/**
 * Created by asyaky on 13/12/2017.
 */

public class Absen {

    private int id;
    private String nip, nama, tanggal, jam, latitude, longitude;

    public Absen(int id, String nip, String nama, String tanggal, String jam, String latitude, String longitude) {
        this.id = id;
        this.nip = nip;
        this.nama = nama;
        this.tanggal = tanggal;
        this.jam = jam;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getIdabsen() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
