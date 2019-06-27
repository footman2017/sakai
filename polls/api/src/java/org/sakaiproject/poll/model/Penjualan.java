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
public class Penjualan {
    private long id;
    private int Kd_Penjualan;
    private int Kd_Customer;
    private int Kd_Produk;
    private int Jumlah_Barang;
    private long Total_Biaya;

    public String toString() {
        return new ToStringBuilder(this)
        .append(this.getId())
        .append(this.getKd_Penjualan())
        .append(this.getKd_Customer())
        .append(this.getKd_Produk())
        .append(this.getJumlah_Barang())
        .append(this.getTotal_Biaya())
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
     * @return the Kd_Penjualan
     */
    public int getKd_Penjualan() {
        return Kd_Penjualan;
    }

    /**
     * @param Kd_Penjualan the Kd_Penjualan to set
     */
    public void setKd_Penjualan(int Kd_Penjualan) {
        this.Kd_Penjualan = Kd_Penjualan;
    }

    /**
     * @return the Kd_Customer
     */
    public int getKd_Customer() {
        return Kd_Customer;
    }

    /**
     * @param Kd_Customer the Kd_Customer to set
     */
    public void setKd_Customer(int Kd_Customer) {
        this.Kd_Customer = Kd_Customer;
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
     * @return the Jumlah_Barang
     */
    public int getJumlah_Barang() {
        return Jumlah_Barang;
    }

    /**
     * @param Jumlah_Barang the Jumlah_Barang to set
     */
    public void setJumlah_Barang(int Jumlah_Barang) {
        this.Jumlah_Barang = Jumlah_Barang;
    }

    /**
     * @return the Total_Biaya
     */
    public long getTotal_Biaya() {
        return Total_Biaya;
    }

    /**
     * @param Total_Biaya the Total_Biaya to set
     */
    public void setTotal_Biaya(long Total_Biaya) {
        this.Total_Biaya = Total_Biaya;
    }
    
}
