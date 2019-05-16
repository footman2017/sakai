package org.sakaiproject.poll.dao.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.sakaiproject.poll.dao.PollDao;
import org.sakaiproject.poll.model.Form;
import org.sakaiproject.poll.model.glossary;
//import org.sakaiproject.poll.model.Mahasiswa;
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
    
    //modif
//    public List<Mahasiswa> getAllExample(){
//	DetachedCriteria d = DetachedCriteria.forClass(Mahasiswa.class);
//	
//	List<Mahasiswa> l = (List<Mahasiswa>) getHibernateTemplate().findByCriteria(d);
//	
//	if(l != null && l.size() > 0){
//		return l;
//	} else{
//		return null;
//	}
//    }
    
    public List<glossary> getAllGlossary(){
	DetachedCriteria d = DetachedCriteria.forClass(glossary.class);
	
	List<glossary> l = (List<glossary>) getHibernateTemplate().findByCriteria(d);
	
	if(l != null && l.size() > 0){
		return l;
	} else{
		return null;
	}
    }
    
//    public void setFormToDatabase(Mahasiswa mahasiswa){
//        Query q = null;
//
//        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
//        String statement = "INSERT INTO `mahasiswa_user`(`NIM`, `NAMA`, `KELAS`, `JENIS_KELAMIN`, `PEMINATAN`, `ALAMAT`) "
//                + "VALUES (["+ mahasiswa.getNim() +"],["+ mahasiswa.getNama() +"],["+ mahasiswa.getKelas() +"],["+ mahasiswa.getIsMale()+"],["+ mahasiswa.getSDA()+"],["+ mahasiswa.getAlamat() +"])";
//        q = session.createSQLQuery(statement);       
//    }

    @Override
    public void setFormToDatabase(Form form) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}