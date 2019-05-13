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
import org.hibernate.criterion.DetachedCriteria;

import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.sakaiproject.poll.dao.PollDao;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Glossary;

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
    
    public void setGlossaryToDatabase(Glossary glossary){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "INSERT INTO `SAKAI_GLOSSARY`(`TERM`, `DESCRIPTION`, `CATEGORY`) "
                + "VALUES (["+ glossary.getTerm() +"],["+ glossary.getDescription() +"],["+ glossary.getCategory() +"]])";
        q = session.createSQLQuery(statement);       
    }
    
    public List<Glossary> getAllGlossary(){
	DetachedCriteria d = DetachedCriteria.forClass(Glossary.class);
	
	List<Glossary> l = (List<Glossary>) getHibernateTemplate().findByCriteria(d);
	
	if(l != null && l.size() > 0){
		return l;
	} else{
		return null;
	}
    }
}
