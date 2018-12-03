package com.doovj.absensiunsil;

/**
 * Created by asyaky on 29/11/2017.
 */

public class User {
    private int id;
    private String nip, nama;

    public User(int id, String nip, String nama) {
        this.id = id;
        this.nip = nip;
        this.nama = nama;
    }

    public int getId() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }
}
