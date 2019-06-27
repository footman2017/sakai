/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.component.app.syllabus;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.sakaiproject.api.app.syllabus.InputForm;
import org.sakaiproject.api.app.syllabus.SyllabusAttachment;
import org.sakaiproject.api.app.syllabus.SyllabusData;
import org.sakaiproject.api.app.syllabus.SyllabusItem;

/**
 * A syllabus item contains information relating to a syllabus and an order
 * within a particular context (site).
 */

@Data
@EqualsAndHashCode(of = "kodeMK")
@ToString(of = {"kodeProduk","namaProduk","hargaProduk","kodeJenisProduk"})
public class InputFormImpl implements InputForm {
    // UAS Proyek 4 - Penjualan ( attribut )

    private String kodeProduk;
    private String namaProduk;
    private String hargaProduk;
    private String kodeJenisProduk;

    // --- tambahanin variabel kalian disini ---

    // ==========

    // UAS Proyek 4 - Penjualan ( method )
        public String getKodeProduk() {
            return kodeProduk;
        }
        public void setKodeProduk(String kodeProduk) {
            this.kodeProduk = kodeProduk;
        }

        public String getNamaProduk() {
            return namaProduk;
        }
        public void setNamaProduk(String namaProduk) {
            this.namaProduk = namaProduk;
        }

        public String getHargaProduk() {
            return hargaProduk;
        }
        public void setHargaProduk(String hargaProduk) {
            this.hargaProduk = hargaProduk;
        }

        public String getKodeJenisProduk() {
            return kodeJenisProduk;
        }
        public void setKodeJenisProduk(String kodeJenisProduk) {
            this.kodeJenisProduk = kodeJenisProduk;
        }

    // =============



    private String nim;
    private String nama;
    private String kelas;
    
     //kebutuhan Quiz project4 smt 4
      private String glo;
      private String term;
      private String desc;
      private String kat;
      
      //Kebutuhan RPS
      private String kodeMK;
      private String namaMK;
      private String semester;
      private String statusMK;
      private String bentukPmbljr;
      private String dosen;
      private String descMK;
      private String prasyarat;
      private String Referensi;
      private String capaian;
      private String peta;
      private String hasilBljr;
      private String topic;
      private String metodePmbljr;
      private String jadwal;
      
//    public InputFormImpl(String nim, String nama, String kelas) {
//        this.nim = nim;
//        this.nama = nama;
//        this.kelas = kelas;
//    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

     //kebutuhan Quiz project4 smt 4

        public String getGlo() {
            return glo;
        }

        public void setGlo(String glo) {
            this.glo = glo;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getKat() {
            return kat;
        }

        public void setKat(String kat) {
            this.kat = kat;
        }
        
//        ======
        
        
        //Kebutuhan RPS

    public String getNamaMK() {
        return namaMK;
    }

    public void setNamaMK(String namaMK) {
        this.namaMK = namaMK;
    }

    public String getkodeMK() {
        return kodeMK;
    }

    public void setkodeMK(String kodeMK) {
        this.kodeMK = kodeMK;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStatusMK() {
        return statusMK;
    }

    public void setStatusMK(String statusMK) {
        this.statusMK = statusMK;
    }

    public String getBentukPmbljr() {
        return bentukPmbljr;
    }

    public void setBentukPmbljr(String bentukPmbljr) {
        this.bentukPmbljr = bentukPmbljr;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getDescMK() {
        return descMK;
    }

    public void setDescMK(String descMK) {
        this.descMK = descMK;
    }

    public String getPrasyarat() {
        return prasyarat;
    }

    public void setPrasyarat(String prasyarat) {
        this.prasyarat = prasyarat;
    }

    public String getReferensi() {
        return Referensi;
    }

    public void setReferensi(String Referensi) {
        this.Referensi = Referensi;
    }

    public String getCapaian() {
        return capaian;
    }

    public void setCapaian(String capaian) {
        this.capaian = capaian;
    }

    public String getPeta() {
        return peta;
    }

    public void setPeta(String peta) {
        this.peta = peta;
    }

    public String getHasilBljr() {
        return hasilBljr;
    }

    public void setHasilBljr(String hasilBljr) {
        this.hasilBljr = hasilBljr;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMetodePmbljr() {
        return metodePmbljr;
    }

    public void setMetodePmbljr(String metodePmbljr) {
        this.metodePmbljr = metodePmbljr;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
        
        
    
}
