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

package org.sakaiproject.service.gradebook.shared;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * JavaBean to hold data associated with a Gradebook assignment. The Course Grade is not considered an assignment.
 */
@NoArgsConstructor
@ToString
public class GradebookRankView implements Serializable, Comparable<GradebookRankView> {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private int id;
	
	@Getter
	@Setter
	private String gradebookUID;
	
	@Getter
	@Setter
	private String studentID;
	
	@Getter
	@Setter
	private String fullname;
	
	@Getter
	@Setter
	private double pointsEarned;
	
	@Override
	public int compareTo(final GradebookRankView o) {
		return new CompareToBuilder()
				.append(this.id, o.id)
				.toComparison();
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof GradebookRankView)) {
			return false;
		}
		final GradebookRankView other = (GradebookRankView) o;
		return new EqualsBuilder()
				.append(this.id, other.id)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.id)
				.toHashCode();
	}
}
