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

package org.sakaiproject.poll.dao;

import java.util.List;
import org.sakaiproject.genericdao.api.GeneralGenericDao;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Voter;


public interface PollDao extends GeneralGenericDao {
	
	/**
	 * Get the number of distinct voters on a poll
	 * @param poll
	 * @return
	 */
	 public int getDisctinctVotersForPoll(Poll poll);
         public int getVotersForPoll(Poll poll);
         public List<String> getVoterName(Poll poll);
         public List<String> getVoterLName(Poll poll);
         public List<String> getVoterOption(Poll poll);
         public List<String> getVoterUserId(Poll poll);
//         public List<Object[]> getListDosen(String kd_dosen, String nama_dosen, String MK);
         public List<Object[]> getListDosen();

}
