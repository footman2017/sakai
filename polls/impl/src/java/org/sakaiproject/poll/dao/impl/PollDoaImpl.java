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
import org.sakaiproject.poll.model.Penjualan;
import org.springframework.dao.DataAccessException;


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
    
    //UAS
    public boolean insertDB(Object o){
            try {
                    getHibernateTemplate().save(o);

                    return true;
            } catch (org.springframework.dao.DataIntegrityViolationException e) {

                    return false;
            } catch (org.hibernate.exception.DataException e) {

                    return false;
            } catch (DataAccessException e) {

                    return false;
            }
    }
    
     public List<Object[]> getDataPenjualan(){
        Query q = null;
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT uas_penjualan.Kd_Penjualan, uas_produk.Nama_Produk, uas_customer.Nama_Customer, uas_penjualan.Jumlah_Barang, uas_penjualan.Total_Biaya\n" +
                           "FROM `uas_penjualan`,`uas_produk`,`uas_customer`\n" +
                           "WHERE uas_penjualan.Kd_Customer = uas_customer.Kd_Customer AND uas_penjualan.Kd_Produk = uas_produk.Kd_Produk";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        
        if (list != null)
            return list;

        return null; 
    }
    
    public List<Object[]> getNamaCustomer(){
        Query q = null;
        
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT Kd_customer, Nama_Customer FROM uas_customer";
        q = session.createSQLQuery(statement);
        List<Object[]> list = (List<Object[]>) q.list();
        
        if (list != null)
            return list;

        return null; 
    }


}
