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
    private long id;
    private String kodeJenisProduk;
    private String namaJenisProduk;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKodeJenisProduk() {
        return kodeJenisProduk;
    }

    public void setKodeJenisProduk(String kodeJenisProduk) {
        this.kodeJenisProduk = kodeJenisProduk;
    }

    public String getNamaJenisProduk() {
        return namaJenisProduk;
    }

    public void setNamaJenisProduk(String namaJenisProduk) {
        this.namaJenisProduk = namaJenisProduk;
    }
}
