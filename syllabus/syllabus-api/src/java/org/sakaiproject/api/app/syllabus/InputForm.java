/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2008 The Sakai Foundation
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
package org.sakaiproject.api.app.syllabus;

public interface InputForm
{
  
  // UAS Proyek 4 - Penjualan 

  //produk
  /**
   * @return Returns the NIM.
   */
  public String getKodeProduk();

  /**
   * @param kodeProduk The status to set.
   */
  public void setKodeProduk(String kodeProduk);

  /**
   * @return Returns the NIM.
   */
  public String getNamaProduk();

  /**
   * @param namaProduk The status to set.
   */
  public void setNamaProduk(String namaProduk);

  /**
   * @return Returns the NIM.
   */
  public String getHargaProduk();

  /**
   * @param hargaProduk The status to set.
   */
  public void setHargaProduk(String hargaProduk);

  /**
   * @return Returns the NIM.
   */
  public String getKodeJenisProduk();

  /**
   * @param kodeJenisProduk The status to set.
   */
  public void setKodeJenisProduk(String kodeJenisProduk);

  // ============




  
  /**
   * @return Returns the NIM.
   */
  public String getNim();

  /**
   * @param nim The status to set.
   */
  public void setNim(String nim);

   /**
   * @return Returns the NIM.
   */
  public String getNama();

  /**
   * @param nama The status to set.
   */
  public void setNama(String nama);

   /**
   * @return Returns the NIM.
   */
  public String getKelas();

  /**
   * @param kelas The status to set.
   */
  public void setKelas(String kelas);
  
  
//  TAMBAHAN 
  
  /**
   * @return Returns the NIM.
   */
  public String getGlo();

  /**
   * @param kelas The status to set.
   */
  public void setGlo(String id);
 
  /**
   * @return Returns the NIM.
   */
  public String getTerm();

  /**
   * @param term The status to set.
   */
  public void setTerm(String term);
   /**
   * @return Returns the NIM.
   */
  public String getDesc();

  /**
   * @param kelas The status to set.
   */
  public void setDesc(String desc);
   /**
   * @return Returns the NIM.
   */
  public String getKat();

  /**
   * @param kelas The status to set.
   */
  public void setKat(String kat);

    
     //Kebutuhan RPS
    public String getNamaMK();


    public void setNamaMK(String namaMK);

    public String getkodeMK();

    public void setkodeMK(String kodeMK);

    public String getSemester();

    public void setSemester(String semester);

    public String getStatusMK();

    public void setStatusMK(String statusMK);

    public String getBentukPmbljr();

    public void setBentukPmbljr(String bentukPmbljr);

    public String getDosen();

    public void setDosen(String dosen);

    public String getDescMK();

    public void setDescMK(String descMK);

    public String getPrasyarat();

    public void setPrasyarat(String prasyarat);

    public String getReferensi();

    public void setReferensi(String Referensi);

    public String getCapaian();

    public void setCapaian(String capaian);

    public String getPeta();

    public void setPeta(String peta);  

    public String getHasilBljr();

    public void setHasilBljr(String hasilBljr);

    public String getTopic() ;

    public void setTopic(String topic);

    public String getMetodePmbljr();

    public void setMetodePmbljr(String metodePmbljr);

    public String getJadwal();

    public void setJadwal(String jadwal);
}