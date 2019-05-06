/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.sakaiproject.component.cover.ServerConfigurationService;

/**
 *
 * @author Asus
 */
public class Mahasiswa {
    private static final String ISO8601_DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(ISO8601_DATE_FORMAT_STRING);
    private long id;
    private String nim;
    private String nama;
    private String prodi;
    private String kelas;
    private String alamat;

    private Date tanggalLahir;
    
    private boolean isMale;
    private boolean DDP;
    private boolean SDA;
    private boolean SI;
    
    public Mahasiswa() {
        
        this.isMale = true;
        this.DDP = false;
        this.SDA = false;
        this.SI = false;
        this.tanggalLahir = new Date();
    }
    /**
     * @return the nim
     */
    public long getId() {
        return id;
    }

    /**
     * @param nim the nim to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the nim
     */
    public String getNim() {
        return nim;
    }

    /**
     * @param nim the nim to set
     */
    public void setNim(String nim) {
        this.nim = nim;
    }

    /**
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * @return the prodi
     */
    public String getProdi() {
        return prodi;
    }

    /**
     * @param prodi the prodi to set
     */
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    /**
     * @return the kelas
     */
    public String getKelas() {
        return kelas;
    }

    /**
     * @param kelas the kelas to set
     */
    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    /**
     * @return the alamat
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * @param alamat the alamat to set
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * @return the tanggalLahir
     */
    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    /**
     * @param tanggalLahir the tanggalLahir to set
     */
    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public void setTanggalLahirStr(String value) {
        try {
            Date parsedDate = DATE_FORMAT.parse(value);
            if (parsedDate != null) {
                tanggalLahir = parsedDate;
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTanggalLahirStr() {
        return DATE_FORMAT.format(tanggalLahir);
    }
    
    
    /**
     * @return the isMale
     */
    public boolean getIsMale() {
        return isMale;
    }

    /**
     * @param isMale the isMale to set
     */
    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    /**
     * @return the DDP
     */
    public boolean getDDP() {
        return DDP;
    }

    /**7
     * @param DDP the DDP to set
     */
    public void setDDP(boolean DDP) {
        this.DDP = DDP;
    }

    /**
     * @return the SDA
     */
    public boolean getSDA() {
        return SDA;
    }

    /**
     * @param SDA the SDA to set
     */
    public void setSDA(boolean SDA) {
        this.SDA = SDA;
    }

    /**
     * @return the SI
     */
    public boolean getSI() {
        return SI;
    }

    /**
     * @param SI the SI to set
     */
    public void setSI(boolean SI) {
        this.SI = SI;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
        .append(this.nim)
        .append(this.nama)
        .append(this.prodi)
        .append(this.kelas)
        .append(this.alamat)
        .append(this.tanggalLahir)
        .append(this.isMale)
        .append(this.DDP)
        .append(this.SDA)
        .append(this.SI)
        .toString();
    }
    
}

//
///********************************************************************************** 
// * $URL: $ 
// * $Id:  $ 
// *********************************************************************************** 
// * 
// * Copyright (c) 2006, 2007, 2008 The Sakai Foundation 
// * 
// * Licensed under the Educational Community License, Version 2.0 (the "License"); 
// * you may not use this file except in compliance with the License. 
// * You may obtain a copy of the License at 
// * 
// *       http://www.opensource.org/licenses/ECL-2.0 
// * 
// * Unless required by applicable law or agreed to in writing, software 
// * distributed under the License is distributed on an "AS IS" BASIS, 
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
// * See the License for the specific language governing permissions and 
// * limitations under the License. 
// * 
// **********************************************************************************/ 
// 
//package org.sakaiproject.poll.model; 
// 
//import java.util.Date; 
//import org.sakaiproject.event.api.UsageSession; 
//import org.sakaiproject.event.cover.UsageSessionService; 
//import org.sakaiproject.tool.cover.SessionManager; 
// 
//public class Mahasiswa { 
//     
//    private long nim; 
//    private String nama; 
//    private String kelas; 
//    private String jenis_kelamin; 
//    private String peminatan; 
//    private String alamat; 
// 
//    public Mahasiswa() { 
//        // needed by hibernate 
//    } 
// 
//    public long getNim() { 
//        return nim; 
//    } 
// 
//    public void setNim(long nim) { 
//        this.nim = nim; 
//    } 
// 
//    public String getNama() { 
//        return nama; 
//    } 
// 
//    public void setNama(String nama) { 
//        this.nama = nama; 
//    } 
// 
//    public String getKelas() { 
//        return kelas; 
//    } 
// 
//    public void setKelas(String kelas) { 
//        this.kelas = kelas; 
//    } 
// 
//    public String getJenis_kelamin() { 
//        return jenis_kelamin; 
//    } 
// 
//    public void setJenis_kelamin(String jenis_kelamin) { 
//        this.jenis_kelamin = jenis_kelamin; 
//    } 
// 
//    public String getPeminatan() { 
//        return peminatan; 
//    } 
// 
//    public void setPeminatan(String peminatan) { 
//        this.peminatan = peminatan; 
//    } 
// 
//    public String getAlamat() { 
//        return alamat; 
//    } 
// 
//    public void setAlamat(String alamat) { 
//        this.alamat = alamat; 
//    } 
// 
//} 
