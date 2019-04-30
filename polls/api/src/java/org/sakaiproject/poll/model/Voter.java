/**********************************************************************************
 * $URL: $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.poll.model;

import java.util.Date;
import org.sakaiproject.event.api.UsageSession;
import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.tool.cover.SessionManager;

public class Voter {

    private String userId;
    private Long pollId;
    private String ip;
    private String optionText;
    private String userName;
    private String userFName;
    private String userLName;

    public Voter() {
        // needed by hibernate
    }
    
    public Voter(Poll poll) {
        this.pollId = poll.getPollId();


        // TODO move this stuff to the service
        // user is current user
        userId = SessionManager.getCurrentSessionUserId();
        // set the Ip to the current sessions IP
        UsageSession usageSession = UsageSessionService.getSession();
        if (usageSession != null) {
            ip = usageSession.getIpAddress();
        }
    }

    public void setUserId(String uid) {
        userId = uid;
    }

    public String getUserId() {
        return userId;
    }
    
    public void setUserName() {
        userName = this.userFName + " " + this.userLName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserFName(String fname) {
        userFName = fname;
    }

    public String getUserFName() {
        return userFName;
    }     
    
    public void setUserLName(String lname) {
        userLName = lname;
    }

    public String getUserLName() {
        return userLName;
    }
    
    public void setIp(String value) {
        ip = value;
    }

    public String getIp() {
        return ip;
    }

    public void setPollId(Long value) {
        this.pollId = value;
    }

    public Long getPollId() {
        return pollId;
    }

    public String toString() {
        return this.pollId + ":" + this.userId + ":" + ":" + this.optionText;
    }
    
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
    
    public String getOptionText() {
        return this.optionText;
    }

}
