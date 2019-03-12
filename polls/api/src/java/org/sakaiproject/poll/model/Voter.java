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

public class Voter {

    private String userId;
    private Long pollId;
    private String optionText;
    private String userName;
    private String userFName;
    private String userLName;

    public Voter() {
        // needed by hibernate
    }

    public Voter(Poll poll) {
        this.pollId = poll.getPollId();
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

    public void setPollId(Long value) {
        this.pollId = value;
    }

    public Long getPollId() {
        return pollId;
    }

    public String toString() {
        return this.pollId + ":" + this.userId + ":" + ":" + this.optionText;
    }

}
