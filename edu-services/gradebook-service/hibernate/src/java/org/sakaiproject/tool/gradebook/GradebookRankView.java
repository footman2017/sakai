/**
 * Copyright (c) 2003-2017 The Apereo Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://opensource.org/licenses/ecl2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sakaiproject.tool.gradebook;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GradebookRankView implements Serializable, Comparable<Object> {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String gradebookUID;
	private String studentID;
	private String fullname;
	private double pointsEarned;
	
	public GradebookRankView() {
    }
	
	public GradebookRankView(String fullname) {
        this.fullname = fullname;
	}
	

    @Override
	public int compareTo(Object o) {
        return getFullname().compareTo(((GradebookRankView)o).getFullname());
    }
    @Override
	public String toString() {
        return new ToStringBuilder(this).
            append(getFullname()).toString();
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGradebookUID() {
		return gradebookUID;
	}
	public void setGradebookUID(String gradebookUID) {
		this.gradebookUID = gradebookUID;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public double getPointsEarned() {
		return pointsEarned;
	}
	public void setPointsEarned(double pointsEarned) {
		this.pointsEarned = pointsEarned;
	}
}
