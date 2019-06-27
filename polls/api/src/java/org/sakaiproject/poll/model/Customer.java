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
public class Customer {
    private long id;
    private int Kd_Customer;
    private String Nama_Customer;
    private String Alamat_Customer;
    private String No_HP_Customer;

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

    public int getKd_Customer() {
        return Kd_Customer;
    }

    public void setKd_Customer(int Kd_Customer) {
        this.Kd_Customer = Kd_Customer;
    }

    public String getNama_Customer() {
        return Nama_Customer;
    }

    public void setNama_Customer(String Nama_Customer) {
        this.Nama_Customer = Nama_Customer;
    }

    public String getAlamat_Customer() {
        return Alamat_Customer;
    }

    public void setAlamat_Customer(String Alamat_Customer) {
        this.Alamat_Customer = Alamat_Customer;
    }

    public String getNo_HP_Customer() {
        return No_HP_Customer;
    }

    public void setNo_HP_Customer(String No_HP_Customer) {
        this.No_HP_Customer = No_HP_Customer;
    }

    
    
        public String toString() {
        return new ToStringBuilder(this)
        .append(this.id)
        .append(this.Kd_Customer)
        .append(this.Nama_Customer)
        .append(this.Alamat_Customer)
        .append(this.No_HP_Customer)
        .toString();
    }
    
}
