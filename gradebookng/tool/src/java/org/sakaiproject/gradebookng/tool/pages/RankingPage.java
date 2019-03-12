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
package org.sakaiproject.gradebookng.tool.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.sakaiproject.gradebookng.business.GbRole;
import org.sakaiproject.gradebookng.business.util.EventHelper;
import org.sakaiproject.gradebookng.tool.panels.StudentGradeSummaryGradesPanel;
import org.sakaiproject.rubrics.logic.RubricsConstants;
import org.sakaiproject.user.api.User;

/**
 *
 * The page that students get. Similar to the student grade summary panel that instructors see.
 * Show Information Of Student Ranking In This Site
 * @author Ali Piqri (@alipiqri2)
 *
 */
public class RankingPage extends BasePage {

	private static final long serialVersionUID = 1L;

	public RankingPage() {
		disableLink(this.rankPageLink);
		if (role == GbRole.NONE) {
			sendToAccessDeniedPage(getString("error.role"));
		}

		final User u = this.businessService.getCurrentUser();

		final Map<String, Object> userData = new HashMap<>();
		userData.put("studentUuid", u.getId());
		userData.put("groupedByCategoryByDefault", true);

		final HiddenField<String> rubricsTokenHiddenField = new HiddenField<String>("rubricsTokenHiddenField", Model.of(rubricsService.generateJsonWebToken(RubricsConstants.RBCS_TOOL_GRADEBOOKNG)));
		add(rubricsTokenHiddenField);

		add(new Label("heading", new StringResourceModel("heading.studentrankingpage", null, new Object[] { u.getDisplayName() })));
		add(new Label("summary", "Ranking 1 : " + this.businessService.getGradebookRankView().get(0).getFullname()));
		add(new Label("summary2", "Ranking 2 : " + this.businessService.getGradebookRankView().get(1).getFullname()));
		add(new Label("summary3", "Ranking 3 : " + this.businessService.getGradebookRankView().get(2).getFullname()));

		EventHelper.postStudentViewEvent(this.businessService.getGradebook(), u.getId());
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);

		final String version = serverConfigService.getString("portal.cdn.version", "");

		// tablesorted used by student grade summary
		response.render(JavaScriptHeaderItem.forScript("includeWebjarLibrary('jquery.tablesorter')", null));
		response.render(JavaScriptHeaderItem.forScript("includeWebjarLibrary('jquery.tablesorter/2.27.7/dist/css/theme.bootstrap.min.css')", null));

		// GradebookNG Grade specific styles and behaviour
		response.render(
				CssHeaderItem.forUrl(String.format("/gradebookng-tool/styles/gradebook-grades.css?version=%s", version)));
		response.render(
				CssHeaderItem.forUrl(
						String.format("/gradebookng-tool/styles/gradebook-print.css?version=%s", version),
						"print"));
		response.render(
				JavaScriptHeaderItem.forUrl(
						String.format("/gradebookng-tool/scripts/gradebook-grade-summary.js?version=%s", version)));
	}
}
