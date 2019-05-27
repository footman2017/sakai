/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation
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

package org.sakaiproject.poll.dao.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;

import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.sakaiproject.poll.dao.PollDao;
import org.sakaiproject.poll.model.Poll;

@Slf4j
public class PollDoaImpl extends HibernateGeneralGenericDao implements PollDao {

    public void init() {
        log.debug("init");
    }
    
    @SuppressWarnings("unchecked")
    public int getDisctinctVotersForPoll(Poll poll) {

        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT VOTE_SUBMISSION_ID from POLL_VOTE where VOTE_POLL_ID = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0; 
    }
    
    public int getVotersForPoll(Poll poll) {

        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "select * from view_voter where poll_id = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0; 
    }
    
    public List<String> getVoterName(Poll poll){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "select FIRST_NAME from view_voter where poll_id = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> result = q.list();
        if (result != null)
            return result;

        return null; 
    }
    
    public List<String> getVoterLName(Poll poll){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "select LAST_NAME from view_voter where poll_id = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> result = q.list();
        if (result != null)
            return result;

        return null; 
    }
    
    public List<String> getVoterOption(Poll poll){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "select OPTION_TEXT from view_voter where poll_id = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> result = q.list();
        if (result != null)
            return result;

        return null; 
    }
    
    public List<String> getVoterUserId(Poll poll){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "select USER_ID from view_voter where poll_id = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> result = q.list();
        if (result != null)
            return result;

        return null; 
    }

   
    public List<Object[]> getListDosen(String rombel, String tahunAkademik, String semester){
//    public List<Object[]> getListDosen(String rombel, String prodi){
        Query q = null;
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
 
        String statement = "SELECT DISTINCT sj.Kd_Dosen, sd.Nama_Dosen, smk.Nama_Matkul FROM sch_jadwal sj, sch_dosen sd, sch_mata_kuliah smk\n" +
                           "WHERE sj.Kd_Rombel = '"+rombel+"' AND sj.Kd_Dosen = sd.Kd_Dosen AND sj.Kd_Matkul = smk.Kd_Matkul AND sj.Tahun_akademik = '"+tahunAkademik+"' AND sj.Semester = '"+semester+"'";
//        String statement = "SELECT DISTINCT sm.Kd_Dosen, sd.Nama_Dosen, smk.Nama_Matkul FROM sch_mengampu sm, sch_dosen sd, sch_mata_kuliah smk\n" +
//                           "WHERE sd.Kd_Dosen = sm.Kd_Dosen AND smk.Kd_Matkul = sm.Kd_Matkul";
//        String statement = "SELECT DISTINCT sj.Kd_Dosen, sd.Nama_Dosen, smk.Nama_Matkul FROM sch_jadwal sj, sch_dosen sd, sch_mata_kuliah smk\n" +
//                           "WHERE sj.Kd_Rombel = '"+rombel+"' AND sj.Kd_Dosen = sd.Kd_Dosen AND sj.Kd_Matkul = smk.Kd_Matkul";
//        String statement = "SELECT DISTINCT sm.Kd_Dosen, sd.Nama_Dosen, smk.Nama_Matkul FROM sch_mengampu sm, sch_dosen sd, sch_mata_kuliah smk\n" +
//                           "WHERE sd.Kd_Dosen = sm.Kd_Dosen AND smk.Kd_Matkul = sm.Kd_Matkul AND s_r.kd_rombel = '"+rombel+"' AND s_r.Prodi = '"+prodi+"'";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        
        if (list != null)
            return list;
 
        return null; 
    }
    
    public List<Object[]> getJadwalKuliah(String rombel, String prodi, String tahun_akademik, String semester){
        
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT s_j.hari, s_j.jam_mulai, s_j.jam_berakhir, s_mk.kd_matkul, "
                + "s_mk.nama_matkul, s_j.isTeori, s_d.kd_dosen, s_d.nama_dosen, s_r.nama_ruangan "
                + "FROM sch_jadwal s_j, sch_mengampu s_m, sch_mata_kuliah s_mk, sch_dosen s_d, "
                + "sch_ruangan s_r, sch_rombel s_ro "
                + "WHERE s_j.kd_matkul = s_m.kd_matkul AND s_m.kd_matkul = s_mk.kd_matkul "
                + "AND s_j.isTeori = s_m.isTeori AND s_j.kd_dosen = s_m.kd_dosen "
                + "AND s_m.kd_dosen = s_d.kd_dosen AND s_r.kd_ruangan = s_j.kd_ruangan "
                + "AND s_j.kd_rombel = '"+rombel+"' AND s_j.tahun_akademik = '"+tahun_akademik+"' "
                + "AND s_j.semester = '"+semester+"' AND s_ro.prodi = '"+prodi+"' "
                + "AND s_j.jenis_pertemuan = 'Kuliah' ";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        if (list != null)
            return list;

        return null; 
    }
    
    public List<Object[]> getJadwalUjian(String prodi, String semester, String tipeujian){
        
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT s_j.hari, s_j.jam_mulai, s_j.jam_berakhir, s_mk.nama_matkul, s_mk.isTeori, s_d.nama_dosen, s_r.nama_ruangan, s_j.kd_rombel "
                        + "FROM sch_jadwal s_j, sch_mata_kuliah s_mk, sch_dosen s_d, sch_ruangan s_r, sch_rombel s_ro "
                        + "WHERE s_j.kd_matkul = s_mk.kd_matkul AND s_j.kd_dosen = s_d.kd_dosen AND s_r.kd_ruangan = s_j.kd_ruangan AND "
                        + "s_j.semester = '"+semester+"' AND s_ro.prodi = '"+prodi+"' AND s_j.jenis_pertemuan = '"+tipeujian+"'";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        if (list != null)
            return list;

        return null; 
    }
    
    public List<Object[]> getListMataKuliah(String rombel, String prodi){
        Query q = null;
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT s_mk.kd_matkul, s_mk.nama_matkul, s_mk.jumlah_sks, s_t.semester_ke "
                + "FROM sch_mata_kuliah s_mk, sch_rombel s_r, sch_terdaftar s_t, sch_kurikulum s_k "
                + "WHERE s_r.Tahun_kurikulum = s_k.Tahun_kurikulum AND "
                + "s_k.Semester_ke = s_t.Semester_ke AND s_t.Kd_Matkul = s_mk.Kd_Matkul AND "
                + "s_r.kd_rombel = '"+ rombel +"' AND s_r.Prodi = '"+ prodi +"' ORDER BY `semester_ke` ASC";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        if (list != null)
            return list;

        return null;
    }
}
