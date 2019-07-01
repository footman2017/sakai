/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sakaiproject.poll.model;

/**
 *
 * @author Nikita Nabila
 */

import java.util.Date;

import org.sakaiproject.event.api.UsageSession;
import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.tool.cover.SessionManager;

public class KategoriBarang {
    private int id;
    private String Kd_Jenis;
    private String Nama_Jenis_Produk;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KategoriBarang(){
        //needed by hibernate
    }

    public String getKd_Jenis() {
        return Kd_Jenis;
    }

    public void setKd_Jenis(String Kd_Jenis) {
        this.Kd_Jenis = Kd_Jenis;
    }

    public String getNama_Jenis_Produk() {
        return Nama_Jenis_Produk;
    }

    public void setNama_Jenis_Produk(String Nama_Jenis_Produk) {
        this.Nama_Jenis_Produk = Nama_Jenis_Produk;
    }
}
