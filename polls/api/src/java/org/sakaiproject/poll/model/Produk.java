/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author Asus
 */
public class Produk {
    private long id;
    private int Kd_Produk;
    private int Kd_Jenis;
    private String Nama_Produk;
    private int Harga_Produk;
    private int Stok_Produk;

    
        public String toString() {
        return new ToStringBuilder(this)
//        .append(this.id)
        .append(this.getKd_Produk())
        .append(this.getKd_Jenis())
        .append(this.getNama_Produk())
        .append(this.getHarga_Produk())
        .append(this.getStok_Produk())
        .toString();
    }
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the Kd_Produk
     */
    public int getKd_Produk() {
        return Kd_Produk;
    }

    /**
     * @param Kd_Produk the Kd_Produk to set
     */
    public void setKd_Produk(int Kd_Produk) {
        this.Kd_Produk = Kd_Produk;
    }

    /**
     * @return the Kd_Jenis
     */
    public int getKd_Jenis() {
        return Kd_Jenis;
    }

    /**
     * @param Kd_Jenis the Kd_Jenis to set
     */
    public void setKd_Jenis(int Kd_Jenis) {
        this.Kd_Jenis = Kd_Jenis;
    }

    /**
     * @return the Nama_Produk
     */
    public String getNama_Produk() {
        return Nama_Produk;
    }

    /**
     * @param Nama_Produk the Nama_Produk to set
     */
    public void setNama_Produk(String Nama_Produk) {
        this.Nama_Produk = Nama_Produk;
    }

    /**
     * @return the Harga_Produk
     */
    public int getHarga_Produk() {
        return Harga_Produk;
    }

    /**
     * @param Harga_Produk the Harga_Produk to set
     */
    public void setHarga_Produk(int Harga_Produk) {
        this.Harga_Produk = Harga_Produk;
    }

    /**
     * @return the Stok_Produk
     */
    public long getStok_Produk() {
        return Stok_Produk;
    }

    /**
     * @param Stok_Produk the Stok_Produk to set
     */
    public void setStok_Produk(int Stok_Produk) {
        this.Stok_Produk = Stok_Produk;
    }
    
}
