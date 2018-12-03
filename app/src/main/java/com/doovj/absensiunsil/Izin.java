package com.doovj.absensiunsil;

/**
 * Created by asyaky on 13/12/2017.
 */

public class Izin {

    private int id;
    private String nip, nama, tanggal_mulai, tanggal_berakhir, keterangan;

    public Izin(int id, String nip, String nama, String tanggal_mulai, String tanggal_berakhir, String keterangan) {
        this.id = id;
        this.nip = nip;
        this.nama = nama;
        this.tanggal_mulai = tanggal_mulai;
        this.tanggal_berakhir = tanggal_berakhir;
        this.keterangan = keterangan;
    }

    public int getIdizin() {
        return id;
    }

    public String getNip() {
        return nip;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal_mulai() { return tanggal_mulai; }

    public String getTanggal_berakhir() { return tanggal_berakhir; }

    public String getKeterangan() { return keterangan; }
}
