/**
 * $URL$
 * $Id$
 *
 * Copyright (c) 2006-2012 The Sakai Foundation, 2013- The Apereo Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sakaiproject.basiclti.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Properties;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;

import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import org.tsugi.jackson.JacksonUtil;

import org.tsugi.basiclti.BasicLTIUtil;
import org.tsugi.basiclti.BasicLTIConstants;

import org.tsugi.lti2.LTI2Constants;
import org.tsugi.lti2.LTI2Vars;
import org.tsugi.lti2.LTI2Caps;
import org.tsugi.lti2.LTI2Util;
import org.tsugi.lti2.LTI2Messages;
import org.tsugi.lti2.ToolProxy;
import org.tsugi.lti2.ToolProxyBinding;
import org.tsugi.lti2.ContentItem;
import org.tsugi.lti2.LTI2Config;

import org.sakaiproject.lti.api.LTIService;
import org.sakaiproject.lti2.SakaiLTI2Config;

import org.tsugi.lti13.LTI13Util;

import org.tsugi.lti13.objects.LaunchJWT;
import org.tsugi.lti13.objects.ResourceLink;
import org.tsugi.lti13.objects.Context;
import org.tsugi.lti13.objects.ToolPlatform;
import org.tsugi.lti13.objects.LaunchLIS;
import org.tsugi.lti13.objects.BasicOutcome;

import io.jsonwebtoken.Jwts;
import java.net.MalformedURLException;

import java.security.Key;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.tool.cover.ToolManager;
import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.cover.UserDirectoryService;
import org.sakaiproject.site.api.ToolConfiguration;
import org.sakaiproject.tool.api.Placement;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.api.privacy.PrivacyManager;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.AuthzGroup;
import org.sakaiproject.authz.api.GroupProvider;
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.authz.api.Member;
import org.sakaiproject.authz.api.GroupNotDefinedException;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.util.ResourceLoader;
import org.sakaiproject.util.Web;
import org.sakaiproject.portal.util.CSSUtils;
import org.sakaiproject.portal.util.ToolUtils;
import org.sakaiproject.linktool.LinkToolUtil;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.cover.SecurityService;

import org.sakaiproject.service.gradebook.shared.GradebookService;
import org.sakaiproject.service.gradebook.shared.ConflictingAssignmentNameException;
import org.sakaiproject.service.gradebook.shared.Assignment;
import org.sakaiproject.service.gradebook.shared.CommentDefinition;

import net.oauth.OAuth;
import org.apache.commons.lang.StringUtils;

import org.apache.commons.math3.util.Precision;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.service.gradebook.shared.AssessmentNotFoundException;
import org.sakaiproject.service.gradebook.shared.GradebookNotFoundException;
import org.tsugi.lti13.DeepLinkResponse;
import org.tsugi.lti13.LTI13KeySetUtil;
import org.tsugi.lti13.objects.DeepLink;
import org.tsugi.lti13.objects.Endpoint;
import org.tsugi.lti13.objects.NamesAndRoles;

/**
 * Some Sakai Utility code for IMS Basic LTI This is mostly code to support the
 * Sakai conventions for making and launching BLTI resources within Sakai.
 */
@SuppressWarnings("deprecation")
@Slf4j
public class SakaiBLTIUtil {

	public static final boolean verbosePrint = false;

	// Turns off extensions
	public static final String LTI_STRICT = "basiclti.strict";
	// Compensate for incoherent choices in the certification tests
	// Search for the "getStrictVersion()" string to find code that works around
	// bad choices in certifications tests that are un-negotiable
	public static final String LTI_STRICT_VERSION = "basiclti.strict.version";
	// Change this as we release versions of Sakai that need to certify
	// with versions of LTI.
	public static final String LTI_STRICT_VERSION_DEFAULT = "02.00.00"; // 2.0

	public static final String BASICLTI_OUTCOMES_ENABLED = "basiclti.outcomes.enabled";
	public static final String BASICLTI_OUTCOMES_ENABLED_DEFAULT = "true";
	public static final String BASICLTI_SETTINGS_ENABLED = "basiclti.settings.enabled";
	public static final String BASICLTI_SETTINGS_ENABLED_DEFAULT = "true";
	public static final String BASICLTI_ROSTER_ENABLED = "basiclti.roster.enabled";
	public static final String BASICLTI_ROSTER_ENABLED_DEFAULT = "true";
	public static final String BASICLTI_CONTENTLINK_ENABLED = "basiclti.contentlink.enabled";
	public static final String BASICLTI_CONTENTLINK_ENABLED_DEFAULT = null; // i.e. false
	public static final String BASICLTI_LINEITEMS_ENABLED = "basiclti.lineitems.enabled";
	public static final String BASICLTI_LINEITEMS_ENABLED_DEFAULT = "true";
	public static final String BASICLTI_CONSUMER_USERIMAGE_ENABLED = "basiclti.consumer.userimage.enabled";
	public static final String INCOMING_ROSTER_ENABLED = "basiclti.incoming.roster.enabled";
	public static final String BASICLTI_ENCRYPTION_KEY = "basiclti.encryption.key";
	public static final String BASICLTI_LAUNCH_SESSION_TIMEOUT = "basiclti.launch.session.timeout";
	public static final String LTI13_DEPLOYMENT_ID = "lti13.deployment_id";
	public static final String LTI13_DEPLOYMENT_ID_DEFAULT = "1"; // To match Moodle

	public static final String SVC_tc_profile = "tc_profile";
	public static final String SVC_tc_registration = "tc_registration";
	public static final String SVC_Settings = "Settings";
	public static final String SVC_Result = "Result";

	public static final String LTI1_PATH = "/imsblis/service/";
	public static final String LTI2_PATH = "/imsblis/lti2/";
	public static final String LTI13_PATH = "/imsblis/lti13/";

	// Enable the Sakai "ext_" parameters in LTI 2.x Launches
	public static final String SAKAI_EXTENSIONS_ALL = "Sakai.extensions.all";

	public static final String SAKAI_CONTENTITEM_SELECTANY = "Sakai.contentitem.selectAny";
	public static final String SAKAI_CONTENTITEM_SELECTFILE = "Sakai.contentitem.selectFile";
	public static final String SAKAI_CONTENTITEM_SELECTIMPORT = "Sakai.contentitem.selectImport";
	public static final String SAKAI_CONTENTITEM_SELECTLINK = "Sakai.contentitem.selectLink";

	public static final String CANVAS_PLACEMENTS_COURSENAVIGATION = "Canvas.placements.courseNavigation";
	public static final String CANVAS_PLACEMENTS_ACCOUNTNAVIGATION = "Canvas.placements.accountNavigation";
	public static final String CANVAS_PLACEMENTS_ASSIGNMENTSELECTION = "Canvas.placements.assignmentSelection";
	public static final String CANVAS_PLACEMENTS_LINKSELECTION = "Canvas.placements.linkSelection";
	public static final String CANVAS_PLACEMENTS_CONTENTIMPORT = "Canvas.placements.contentImport";

	public static boolean rosterEnabled() {
		String allowRoster = ServerConfigurationService.getString(BASICLTI_ROSTER_ENABLED, BASICLTI_ROSTER_ENABLED_DEFAULT);
		return "true".equals(allowRoster);
	}

	public static boolean outcomesEnabled() {
		String allowOutcomes = ServerConfigurationService.getString(BASICLTI_OUTCOMES_ENABLED, BASICLTI_OUTCOMES_ENABLED_DEFAULT);
		return "true".equals(allowOutcomes);
	}

	public static boolean settingsEnabled() {
		String allowSettings = ServerConfigurationService.getString(BASICLTI_SETTINGS_ENABLED, BASICLTI_SETTINGS_ENABLED_DEFAULT);
		return "true".equals(allowSettings);
	}

	public static boolean lineItemsEnabled() {
		String allowLineItems = ServerConfigurationService.getString(BASICLTI_LINEITEMS_ENABLED, BASICLTI_LINEITEMS_ENABLED_DEFAULT);
		return "true".equals(allowLineItems);
	}

	public static boolean contentLinkEnabled() {
		String allowContentLink = ServerConfigurationService.getString(BASICLTI_CONTENTLINK_ENABLED, BASICLTI_CONTENTLINK_ENABLED_DEFAULT);
		return "true".equals(allowContentLink);
	}

	// Retrieve the property from the configuration unless it
	// is overridden by the server configurtation (i.e. sakai.properties)
	public static String getCorrectProperty(Properties config,
			String propName, Placement placement) {
		// Check for global overrides in properties
		if (LTIService.LTI_ALLOWSETTINGS.equals(propName) && !settingsEnabled()) {
			return "false";
		}

		if (LTIService.LTI_ALLOWROSTER.equals(propName) && !rosterEnabled()) {
			return "false";
		}

		if ("contentlink".equals(propName) && !contentLinkEnabled()) {
			return null;
		}

		// Check for explicit setting in properties
		String propertyName = placement.getToolId() + "." + propName;
		String propValue = ServerConfigurationService.getString(propertyName, null);
		if (propValue != null && propValue.trim().length() > 0) {
			log.debug("Sakai.home {}={}", propName, propValue);
			return propValue;
		}

		// Take it from the placement
		return config.getProperty("imsti." + propName, null);
	}

	// Look at a Placement and come up with the launch urls, and
	// other launch parameters to drive the launch.
	public static boolean loadFromPlacement(Properties info, Properties launch, Placement placement) {
		Properties config = placement.getConfig();
		log.debug("Sakai properties={}", config);
		String launch_url = toNull(getCorrectProperty(config, LTIService.LTI_LAUNCH, placement));
		setProperty(info, "launch_url", launch_url);
		if (launch_url == null) {
			String xml = toNull(getCorrectProperty(config, "xml", placement));
			if (xml == null) {
				return false;
			}
			BasicLTIUtil.parseDescriptor(info, launch, xml);
		}

		String secret = getCorrectProperty(config, LTIService.LTI_SECRET, placement);

		// BLTI-195 - Compatibility mode for old-style encrypted secrets
		if (secret == null || secret.trim().length() < 1) {
			String eSecret = getCorrectProperty(config, "encryptedsecret", placement);
			if (eSecret != null && eSecret.trim().length() > 0) {
				secret = eSecret.trim() + ":" + SimpleEncryption.CIPHER;
			}
		}

		setProperty(info, LTIService.LTI_SECRET, secret);

		// This is not "consumerkey" on purpose - we are mimicking the old placement model
		setProperty(info, "key", getCorrectProperty(config, "key", placement));
		setProperty(info, LTIService.LTI_DEBUG, getCorrectProperty(config, LTIService.LTI_DEBUG, placement));
		setProperty(info, LTIService.LTI_FRAMEHEIGHT, getCorrectProperty(config, LTIService.LTI_FRAMEHEIGHT, placement));
		setProperty(info, LTIService.LTI_NEWPAGE, getCorrectProperty(config, LTIService.LTI_NEWPAGE, placement));
		setProperty(info, LTIService.LTI_TITLE, getCorrectProperty(config, "tooltitle", placement));

		// Pull in and parse the custom parameters
		String customstr = toNull(getCorrectProperty(config, LTIService.LTI_CUSTOM, placement));
		parseCustom(info, customstr);

		if (info.getProperty("launch_url", null) != null
				|| info.getProperty("secure_launch_url", null) != null) {
			return true;
		}
		return false;
	}

	public static void parseCustom(Properties info, String customstr) {
		if (customstr != null) {
			String splitChar = "\n";
			if (customstr.trim().indexOf("\n") == -1) {
				splitChar = ";";
			}
			String[] params = customstr.split(splitChar);
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				if (param == null) {
					continue;
				}
				if (param.length() < 1) {
					continue;
				}
				int pos = param.indexOf("=");
				if (pos < 1) {
					continue;
				}
				if (pos + 1 > param.length()) {
					continue;
				}
				String key = BasicLTIUtil.mapKeyName(param.substring(0, pos));
				if (key == null) {
					continue;
				}
				String value = param.substring(pos + 1);
				if (value == null) {
					continue;
				}
				value = value.trim();
				if (value.length() < 1) {
					continue;
				}
				setProperty(info, "custom_" + key, value);
			}
		}
	}

	/**
	 * adjustCustom - Deal with various custom parameter strings
	 *
	 * The correct way: x=1\ny=2\nz=3 The old Sakai way: x=1;y=2;z=3 A format
	 * string that confuses things: x=1;\ny=2\nz=3 In this string, the first
	 * parameter should be "1;"
	 *
	 * So here are the rules:
	 *
	 * If it is null, blank, or has no equal signs return unchanged If there is
	 * a newline anywhere in the trimmed string return unchanged If there is one
	 * equal sign return unchanged If there is a new line anywhere in the string
	 * after trim, return unchanged If we see ..=..;..=..;..=..[;] - we replace
	 * ; with \n
	 */
	public static String adjustCustom(String customstr) {
		if (customstr == null) {
			return customstr;
		}
		String trim = customstr.trim();
		if (trim.length() == 0) {
			return customstr;
		}

		if (trim.indexOf('\n') >= 0) {
			return customstr;
		}

		String[] pieces = trim.split("=");

		// Two pieces means one equal sign (a=42)
		if (pieces.length <= 2) {
			return customstr;
		}

		// Insist that all but the first and last piece have a semicolon
		// x | 1;y | 2;z | 3
		for (int i = 1; i < pieces.length - 1; i++) {
			String piece = pieces[i];
			String[] chunks = piece.split(";");
			if (chunks.length != 2) {
				return customstr;
			}
		}

		// Now we can assume that ';' maps to '\n'
		return customstr.replace(';', '\n');
	}

	public static String encryptSecret(String orig) {
		String encryptionKey = ServerConfigurationService.getString(BASICLTI_ENCRYPTION_KEY, null);
		return encryptSecret(orig, encryptionKey);
	}

	// For unit tests mostly
	public static String encryptSecret(String orig, String encryptionKey) {
		if (StringUtils.isEmpty(orig) || StringUtils.isEmpty(encryptionKey) ) {
			return orig;
		}

		// Never double encrypt
		String check = decryptSecret(orig, encryptionKey);
		if ( ! orig.equals(check) ) {
			return orig;
		}

		// May throw runtime exception - just let it log as this is abnormal...
		String newsecret = SimpleEncryption.encrypt(encryptionKey, orig);
		return newsecret;
	}

	public static String decryptSecret(String orig) {
		String encryptionKey = ServerConfigurationService.getString(BASICLTI_ENCRYPTION_KEY, null);
		return decryptSecret(orig, encryptionKey);
	}

	public static String decryptSecret(String orig, String encryptionKey) {
		if (StringUtils.isEmpty(orig) || StringUtils.isEmpty(encryptionKey) ) {
			return orig;
		}

		try {
			String newsecret = SimpleEncryption.decrypt(encryptionKey, orig);
			return newsecret;
		} catch (RuntimeException re) {
			log.debug("Exception when decrypting secret - this is normal if the secret is unencrypted");
			return orig;
		}
	}

	public static boolean sakaiInfo(Properties props, Placement placement, ResourceLoader rb) {
		log.debug("placement={}", placement.getId());
		log.debug("placement title={}", placement.getTitle());
		String context = placement.getContext();
		log.debug("ContextID={}", context);

		return sakaiInfo(props, context, placement.getId(), rb);
	}

	public static void addSiteInfo(Properties props, Properties lti2subst, Site site) {
		if (site != null) {
			String context_type = site.getType();
			if (context_type != null && context_type.toLowerCase().contains("course")) {
				setProperty(props, BasicLTIConstants.CONTEXT_TYPE, BasicLTIConstants.CONTEXT_TYPE_COURSE_SECTION);
				setProperty(lti2subst, LTI2Vars.CONTEXT_TYPE, LTI2Vars.CONTEXT_TYPE_DEFAULT);
			} else {
				setProperty(props, BasicLTIConstants.CONTEXT_TYPE, BasicLTIConstants.CONTEXT_TYPE_GROUP);
				setProperty(lti2subst, LTI2Vars.CONTEXT_TYPE, BasicLTIConstants.CONTEXT_TYPE_GROUP);
			}
			setProperty(props, BasicLTIConstants.CONTEXT_ID, site.getId());
			setProperty(lti2subst, LTI2Vars.COURSESECTION_SOURCEDID, site.getId());
			setProperty(lti2subst, LTI2Vars.CONTEXT_ID, site.getId());

			setProperty(props, BasicLTIConstants.CONTEXT_LABEL, site.getTitle());
			setProperty(lti2subst, LTI2Vars.COURSESECTION_LABEL, site.getTitle());
			setProperty(lti2subst, LTI2Vars.CONTEXT_LABEL, site.getTitle());

			setProperty(props, BasicLTIConstants.CONTEXT_TITLE, site.getTitle());
			setProperty(lti2subst, LTI2Vars.COURSESECTION_LONGDESCRIPTION, site.getTitle());
			setProperty(lti2subst, LTI2Vars.CONTEXT_TITLE, site.getTitle());

			String courseRoster = getExternalRealmId(site.getId());
			if (courseRoster != null) {
				setProperty(props, BasicLTIConstants.LIS_COURSE_OFFERING_SOURCEDID, courseRoster);
				setProperty(props, BasicLTIConstants.LIS_COURSE_SECTION_SOURCEDID, courseRoster);
				setProperty(lti2subst, LTI2Vars.COURSESECTION_SOURCEDID, courseRoster);
				setProperty(lti2subst, LTI2Vars.COURSEOFFERING_SOURCEDID, courseRoster);
			} else {
				setProperty(props, BasicLTIConstants.LIS_COURSE_OFFERING_SOURCEDID, site.getId());
				setProperty(props, BasicLTIConstants.LIS_COURSE_SECTION_SOURCEDID, site.getId());
				setProperty(lti2subst, LTI2Vars.COURSESECTION_SOURCEDID, site.getId());
				setProperty(lti2subst, LTI2Vars.COURSEOFFERING_SOURCEDID, site.getId());
			}

			// SAK-31282 - Add the Academic Session (ext_sakai_academic_session) to LTI launches
			String termPropertyName = ServerConfigurationService.getString("irubric.termPropertyName", "");
			if (termPropertyName.length() > 0) {
				String academicSessionId = site.getProperties().getProperty(termPropertyName);
				if ((academicSessionId == "") || (academicSessionId == null)) {
					academicSessionId = "OTHER";
				}
				setProperty(props, "ext_sakai_academic_session", academicSessionId);
			}
		}

		// Fix up the return Url
		String returnUrl = ServerConfigurationService.getString("basiclti.consumer_return_url", null);
		if (returnUrl == null) {
			returnUrl = getOurServerUrl() + LTI1_PATH + "return-url";
			Session s = SessionManager.getCurrentSession();
			if (s != null) {
				String controllingPortal = (String) s.getAttribute("sakai-controlling-portal");
				if (controllingPortal == null) {
					returnUrl = returnUrl + "/site";
				} else {
					returnUrl = returnUrl + "/" + controllingPortal;
				}
			}
			returnUrl = returnUrl + "/" + site.getId();
		}

		setProperty(props, BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL, returnUrl);

	}

	public static void addUserInfo(Properties ltiProps, Properties lti2subst, Map<String, Object> tool) {
		int releasename = getInt(tool.get(LTIService.LTI_SENDNAME));
		int releaseemail = getInt(tool.get(LTIService.LTI_SENDEMAILADDR));
		User user = UserDirectoryService.getCurrentUser();
		if (user != null) {
			setProperty(ltiProps, BasicLTIConstants.USER_ID, user.getId());
			setProperty(lti2subst, LTI2Vars.USER_ID, user.getId());
			setProperty(ltiProps, BasicLTIConstants.LIS_PERSON_SOURCEDID, user.getEid());
			setProperty(lti2subst, LTI2Vars.USER_USERNAME, user.getEid());
			setProperty(lti2subst, LTI2Vars.PERSON_SOURCEDID, user.getEid());

			if (releasename == 1) {
				setProperty(ltiProps, BasicLTIConstants.LIS_PERSON_NAME_GIVEN, user.getFirstName());
				setProperty(ltiProps, BasicLTIConstants.LIS_PERSON_NAME_FAMILY, user.getLastName());
				setProperty(ltiProps, BasicLTIConstants.LIS_PERSON_NAME_FULL, user.getDisplayName());
				setProperty(lti2subst, LTI2Vars.PERSON_NAME_GIVEN, user.getFirstName());
				setProperty(lti2subst, LTI2Vars.PERSON_NAME_FAMILY, user.getLastName());
				setProperty(lti2subst, LTI2Vars.PERSON_NAME_FULL, user.getDisplayName());
			}
			if (releaseemail == 1) {
				setProperty(ltiProps, BasicLTIConstants.LIS_PERSON_CONTACT_EMAIL_PRIMARY, user.getEmail());
				setProperty(lti2subst, LTI2Vars.PERSON_EMAIL_PRIMARY, user.getEmail());
				// Only send the display ID if it's different to the EID.
				// the anonymous user has a null EID.
				if (user.getEid() != null && !user.getEid().equals(user.getDisplayId())) {
					setProperty(ltiProps, BasicLTIConstants.EXT_SAKAI_PROVIDER_DISPLAYID, user.getDisplayId());
				}
			}
		}
	}

	public static String getRoleString(String context) {
		String theRole = LTI2Vars.MEMBERSHIP_ROLE_LEARNER;
		if (SecurityService.isSuperUser()) {
			theRole = LTI2Vars.MEMBERSHIP_ROLE_INSTRUCTOR + ",Administrator,urn:lti:instrole:ims/lis/Administrator,urn:lti:sysrole:ims/lis/Administrator";
		} else if (SiteService.allowUpdateSite(context)) {
			theRole = LTI2Vars.MEMBERSHIP_ROLE_INSTRUCTOR;
		}
		return theRole;
	}

	public static void addRoleInfo(Properties props, Properties lti2subst, String context, String roleMapProp) {
		String theRole = getRoleString(context);

		setProperty(props, BasicLTIConstants.ROLES, theRole);
		setProperty(lti2subst, LTI2Vars.MEMBERSHIP_ROLE, theRole);

		String realmId = SiteService.siteReference(context);
		User user = null;
		Map<String, String> roleMap = convertRoleMapPropToMap(roleMapProp);
		try {
			user = UserDirectoryService.getCurrentUser();
			if (user != null) {
				Role role = null;
				String roleId = null;
				AuthzGroup realm = ComponentManager.get(AuthzGroupService.class).getAuthzGroup(realmId);
				if (realm != null) {
					role = realm.getUserRole(user.getId());
				}
				if (role != null) {
					roleId = role.getId();
				}
				if (roleId != null && roleId.length() > 0) {
					setProperty(props, "ext_sakai_role", roleId);
				}
				if (roleMap.containsKey(roleId)) {
					setProperty(props, BasicLTIConstants.ROLES, roleMap.get(roleId));
					setProperty(lti2subst, LTI2Vars.MEMBERSHIP_ROLE, roleMap.get(roleId));
				}
			}
		} catch (GroupNotDefinedException e) {
			log.error("SiteParticipantHelper.getExternalRealmId: site realm not found {}", e.getMessage());
		}

		// Check if there are sections the user is part of (may be more than one)
		String courseRoster = getExternalRealmId(context);
		if (user != null && courseRoster != null) {
			GroupProvider groupProvider = (GroupProvider) ComponentManager.get(
					org.sakaiproject.authz.api.GroupProvider.class);
			String[] courseRosters = groupProvider.unpackId(courseRoster);
			List<String> rosterList = new ArrayList<>();
			String userEid = user.getEid();
			for (int i = 0; i < courseRosters.length; i++) {
				String providerId = courseRosters[i];
				Map userRole = groupProvider.getUserRolesForGroup(providerId);
				if (userRole.containsKey(userEid)) {
					rosterList.add(providerId);
				}
			}
			if (rosterList.size() > 0) {
				String[] sArray = new String[rosterList.size()];
				sArray = (String[]) rosterList.toArray(sArray);
				String providedGroups = groupProvider.packId(sArray);
				setProperty(props, "ext_sakai_section", providedGroups);
			}
		}
	}

	// Retrieve the Sakai information about users, etc.
	public static boolean sakaiInfo(Properties props, String context, String placementId, ResourceLoader rb) {

		Site site;
		try {
			site = SiteService.getSite(context);
		} catch (IdUnusedException e) {
			log.error("No site/page associated with Launch context={}", context);
			return false;
		}

		// Add the generic information
		addGlobalData(site, props, null, rb);
		ToolConfiguration placement = SiteService.findTool(placementId);
		Properties config = placement.getConfig();
		String roleMapProp = toNull(getCorrectProperty(config, "rolemap", placement));
		addRoleInfo(props, null, context, roleMapProp);
		addSiteInfo(props, null, site);

		// Add Placement Information
		addPlacementInfo(props, placementId);
		return true;
	}

	public static void addPlacementInfo(Properties props, String placementId) {

		// Get the placement to see if we are to release information
		ToolConfiguration placement = SiteService.findTool(placementId);
		Properties config = placement.getConfig();

		// Start setting the Basici LTI parameters
		setProperty(props, BasicLTIConstants.RESOURCE_LINK_ID, placementId);
		String pagetitle = toNull(getCorrectProperty(config, LTIService.LTI_PAGETITLE, placement));
		String tooltitle = toNull(getCorrectProperty(config, "tooltitle", placement));

		// Cross-copy these if they are blank
		if (pagetitle == null) {
			pagetitle = tooltitle;
		}
		if (tooltitle == null) {
			tooltitle = pagetitle;
		}

		if (pagetitle != null) {
			setProperty(props, BasicLTIConstants.RESOURCE_LINK_TITLE, pagetitle);
		} else {
			setProperty(props, BasicLTIConstants.RESOURCE_LINK_TITLE, placement.getTitle());
		}
		if (tooltitle != null) {
			setProperty(props, BasicLTIConstants.RESOURCE_LINK_DESCRIPTION, tooltitle);
		} else {
			setProperty(props, BasicLTIConstants.RESOURCE_LINK_DESCRIPTION, placement.getTitle());
		}

		String releasename = toNull(getCorrectProperty(config, "releasename", placement));
		String releaseemail = toNull(getCorrectProperty(config, "releaseemail", placement));

		// Use SHA256 if requested (SAK-33898)
		String sha256 = toNull(getCorrectProperty(config, "sha256", placement));
		if ("on".equals(sha256)) {
			setProperty(props, OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA256");
		}

		User user = UserDirectoryService.getCurrentUser();

		PrivacyManager pm = (PrivacyManager) ComponentManager.get("org.sakaiproject.api.privacy.PrivacyManager");

		// TODO: Think about anonymous
		if (user != null) {
			String context = placement.getContext();
			boolean isViewable = pm.isViewable("/site/" + context, user.getId());
			setProperty(props, "ext_sakai_privacy", isViewable ? "visible" : "hidden");

			setProperty(props, BasicLTIConstants.USER_ID, user.getId());

			if (ServerConfigurationService.getBoolean(BASICLTI_CONSUMER_USERIMAGE_ENABLED, true)) {
				String imageUrl = getOurServerUrl() + "/direct/profile/" + user.getId() + "/image";
				setProperty(props, BasicLTIConstants.USER_IMAGE, imageUrl);
			}

			if ("on".equals(releasename)) {
				setProperty(props, BasicLTIConstants.LIS_PERSON_NAME_GIVEN, user.getFirstName());
				setProperty(props, BasicLTIConstants.LIS_PERSON_NAME_FAMILY, user.getLastName());
				setProperty(props, BasicLTIConstants.LIS_PERSON_NAME_FULL, user.getDisplayName());
			}
			if ("on".equals(releaseemail)) {
				setProperty(props, BasicLTIConstants.LIS_PERSON_CONTACT_EMAIL_PRIMARY, user.getEmail());
				setProperty(props, BasicLTIConstants.LIS_PERSON_SOURCEDID, user.getEid());
				setProperty(props, "ext_sakai_eid", user.getEid());
			}

			String assignment = null;
			// It is a little tricky - the tool configuration on/off decides whether
			// We check the serverCongigurationService true/false
			// We use the tool configuration to force outcomes off regardless of
			// server settings (i.e. an external tool never wants the outcomes
			// UI shown because it simply does not handle outcomes).
			String allowOutcomes = toNull(getCorrectProperty(config, LTIService.LTI_ALLOWOUTCOMES, placement));
			if (!"off".equals(allowOutcomes)) {
				assignment = toNull(getCorrectProperty(config, "assignment", placement));
				if (!outcomesEnabled()) {
					allowOutcomes = null;
				}
			}

			String allowSettings = toNull(getCorrectProperty(config, LTIService.LTI_ALLOWSETTINGS, placement));
			if (!"on".equals(allowSettings)) {
				allowSettings = null;
			}

			String allowRoster = toNull(getCorrectProperty(config, LTIService.LTI_ALLOWROSTER, placement));
			if (!"on".equals(allowRoster)) {
				allowRoster = null;
			}

			String result_sourcedid = getSourceDID(user, placement, config);

			String theRole = getRoleString(context);

			// if ( result_sourcedid != null && theRole.indexOf(LTI2Vars.MEMBERSHIP_ROLE_LEARNER) >= 0 ) {
			if (result_sourcedid != null) {

				if ("true".equals(allowOutcomes) && assignment != null) {
					if (theRole.contains(LTI2Vars.MEMBERSHIP_ROLE_LEARNER)) {
						setProperty(props, BasicLTIConstants.LIS_RESULT_SOURCEDID, result_sourcedid);
					}
					setProperty(props, "ext_outcome_data_values_accepted", "text");  // SAK-25696

					// New Basic Outcomes URL
					String outcome_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lis_basic_outcome_url", null);
					if (outcome_url == null) {
						outcome_url = getOurServerUrl() + LTI1_PATH;
					}
					setProperty(props, "ext_ims_lis_basic_outcome_url", outcome_url);
					outcome_url = ServerConfigurationService.getString("basiclti.consumer." + BasicLTIConstants.LIS_OUTCOME_SERVICE_URL, null);
					if (outcome_url == null) {
						outcome_url = getOurServerUrl() + LTI1_PATH;
					}
					setProperty(props, BasicLTIConstants.LIS_OUTCOME_SERVICE_URL, outcome_url);
				}

				if ("on".equals(allowSettings)) {
					setProperty(props, "ext_ims_lti_tool_setting_id", result_sourcedid);

					String service_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lti_tool_setting_url", null);
					if (service_url == null) {
						service_url = getOurServerUrl() + LTI1_PATH;
					}
					setProperty(props, "ext_ims_lti_tool_setting_url", service_url);
				}

				if ("on".equals(allowRoster)) {
					setProperty(props, "ext_ims_lis_memberships_id", result_sourcedid);

					String roster_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lis_memberships_url", null);
					if (roster_url == null) {
						roster_url = getOurServerUrl() + LTI1_PATH;
					}
					setProperty(props, "ext_ims_lis_memberships_url", roster_url);
				}

			}

			// Send along the deprecated LinkTool encrypted session if requested
			String sendsession = toNull(getCorrectProperty(config, "ext_sakai_session", placement));
			if ("true".equals(sendsession)) {
				Session s = SessionManager.getCurrentSession();
				if (s != null) {
					String sessionid = s.getId();
					if (sessionid != null) {
						sessionid = LinkToolUtil.encrypt(sessionid);
						setProperty(props, "ext_sakai_session", sessionid);
					}
				}
			}

			// Send along the SAK-28125 encrypted session if requested
			String encryptsession = toNull(getCorrectProperty(config, "ext_sakai_encrypted_session", placement));
			String secret = toNull(getCorrectProperty(config, LTIService.LTI_SECRET, placement));
			String key = toNull(getCorrectProperty(config, "key", placement));
			if (secret != null && key != null && "true".equals(encryptsession)
					&& !SecurityService.isSuperUser()) {

				secret = decryptSecret(secret);
				// sha1secret is 160-bits hex the sha1 for "secret" is
				// e5e9fa1ba31ecd1ae84f75caaa474f3a663f05f4
				String sha1Secret = PortableShaUtil.sha1Hash(secret);
				Session s = SessionManager.getCurrentSession();
				if (s != null) {
					String sessionid = s.getId();
					if (sessionid != null) {
						sessionid = BlowFish.encrypt(sha1Secret, sessionid);
						setProperty(props, "ext_sakai_encrypted_session", sessionid);
						// Don't just change this as it will break existing connections
						// Especially to LTI tools written in Java with the default JCE
						setProperty(props, "ext_sakai_blowfish_length", "128");
					}
				}
			}
		}

		// Send along the content link
		String contentlink = toNull(getCorrectProperty(config, "contentlink", placement));
		if (contentlink != null) {
			setProperty(props, "ext_resource_link_content", contentlink);
		}
	}

	public static void addGlobalData(Site site, Properties props, Properties custom, ResourceLoader rb) {
		if (rb != null) {
			String locale = rb.getLocale().toString();
			setProperty(props, BasicLTIConstants.LAUNCH_PRESENTATION_LOCALE, locale);
			setProperty(custom, LTI2Vars.MESSAGE_LOCALE, locale);
		}

		// Add information about the Tool Consumer for LTI 1.x
		LTI2Config cnf = new SakaiLTI2Config();
		setProperty(props, "tool_consumer_info_product_family_code", cnf.getProduct_family_product_code());  // Test 2.4
		setProperty(props, "tool_consumer_info_version", cnf.getProduct_info_product_version());  // Test 2.5

		// Get the organizational information
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_GUID,
				ServerConfigurationService.getString("basiclti.consumer_instance_guid",
						ServerConfigurationService.getString("serverName", null)));
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_NAME,
				ServerConfigurationService.getString("basiclti.consumer_instance_name", null));
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_DESCRIPTION,
				ServerConfigurationService.getString("basiclti.consumer_instance_description", null));
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_CONTACT_EMAIL,
				ServerConfigurationService.getString("basiclti.consumer_instance_contact_email", null));
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INSTANCE_URL,
				ServerConfigurationService.getString("basiclti.consumer_instance_url",
						ServerConfigurationService.getString("serverUrl", null)));

		// Send along the CSS URL
		String tool_css = ServerConfigurationService.getString("basiclti.consumer.launch_presentation_css_url", null);
		if (tool_css == null) {
			tool_css = getOurServerUrl() + CSSUtils.getCssToolBase();
		}
		setProperty(props, BasicLTIConstants.LAUNCH_PRESENTATION_CSS_URL, tool_css);

		// Send along the CSS URL list
		String tool_css_all = ServerConfigurationService.getString("basiclti.consumer.ext_sakai_launch_presentation_css_url_all", null);
		if (site != null && tool_css_all == null) {
			tool_css_all = getOurServerUrl() + CSSUtils.getCssToolBase() + ',' + getOurServerUrl() + CSSUtils.getCssToolSkinCDN(CSSUtils.getSkinFromSite(site));
		}
		setProperty(props, "ext_sakai_" + BasicLTIConstants.LAUNCH_PRESENTATION_CSS_URL + "_list", tool_css_all);

		// Let tools know we are coming from Sakai
		String sakaiVersion = ServerConfigurationService.getString("version.sakai", "2");
		setProperty(props, "ext_lms", "sakai-" + sakaiVersion);
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INFO_PRODUCT_FAMILY_CODE, "sakai");
		setProperty(props, BasicLTIConstants.TOOL_CONSUMER_INFO_VERSION, sakaiVersion);
		setProperty(custom, LTI2Vars.TOOLCONSUMERINFO_PRODUCTFAMILYCODE, "sakai");
		setProperty(custom, LTI2Vars.TOOLCONSUMERINFO_VERSION, sakaiVersion);

		// We pass this along in the Sakai world - it might
		// might be useful to the external tool
		String serverId = ServerConfigurationService.getServerId();
		setProperty(props, "ext_sakai_serverid", serverId);
		setProperty(props, "ext_sakai_server", getOurServerUrl());
	}

	// getProperty(String name);
	// Gnerate HTML from a descriptor and properties from
	public static String[] postLaunchHTML(String descriptor, String contextId, String resourceId, ResourceProperties props, ResourceLoader rb) {
		if (descriptor == null || contextId == null || resourceId == null) {
			return postError("<p>" + getRB(rb, "error.descriptor", "Error, missing contextId, resourceid or descriptor") + "</p>");
		}

		// Add user, course, etc to the launch parameters
		Properties launch = new Properties();
		if (!sakaiInfo(launch, contextId, resourceId, rb)) {
			return postError("<p>" + getRB(rb, "error.info.resource",
					"Error, cannot load Sakai information for resource=") + resourceId + ".</p>");
		}

		Properties info = new Properties();
		if (!BasicLTIUtil.parseDescriptor(info, launch, descriptor)) {
			return postError("<p>" + getRB(rb, "error.badxml.resource",
					"Error, cannot parse descriptor for resource=") + resourceId + ".</p>");
		}

		return postLaunchHTML(info, launch, rb);
	}

	// This must return an HTML message as the [0] in the array
	// If things are successful - the launch URL is in [1]
	public static String[] postLaunchHTML(Map<String, Object> content, Map<String, Object> tool,
			String state, String nonce, LTIService ltiService, ResourceLoader rb) {
		if (content == null) {
			return postError("<p>" + getRB(rb, "error.content.missing", "Content item is missing or improperly configured.") + "</p>");
		}
		if (tool == null) {
			return postError("<p>" + getRB(rb, "error.tool.missing", "Tool item is missing or improperly configured.") + "</p>");
		}

		int status = getInt(tool.get(LTIService.LTI_STATUS));
		if (status == 1) {
			return postError("<p>" + getRB(rb, "tool.disabled", "Tool is currently disabled") + "</p>");
		}

		// Go with the content url first
		String launch_url = (String) content.get(LTIService.LTI_LAUNCH);
		if (launch_url == null) {
			launch_url = (String) tool.get(LTIService.LTI_LAUNCH);
		}
		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.nolaunch", "This tool is not yet configured.") + "</p>");
		}

		String context = (String) content.get(LTIService.LTI_SITE_ID);
		Site site;
		try {
			site = SiteService.getSite(context);
		} catch (IdUnusedException e) {
			log.error("No site/page associated with Launch context={}", context);
			return postError("<p>" + getRB(rb, "error.site.missing", "Cannot load site.") + context + "</p>");
		}

		// Percolate up to get the other objects...
		Map<String, Object> proxyBinding;
		Map<String, Object> deploy = null;

		Long deployKey = getLongKey(tool.get(LTIService.LTI_DEPLOYMENT_ID));
		if (deployKey >= 0) {
			deploy = ltiService.getDeployDao(deployKey);
		}

		Long toolKey = getLongKey(tool.get(LTIService.LTI_ID));
		proxyBinding = ltiService.getProxyBindingDao(toolKey, context);

		// See if there are the necessary items
		String secret = (String) content.get(LTIService.LTI_SECRET);
		if (secret == null) {
			secret = (String) tool.get(LTIService.LTI_SECRET);
		}
		String key = (String) content.get(LTIService.LTI_CONSUMERKEY);
		if (key == null) {
			key = (String) tool.get(LTIService.LTI_CONSUMERKEY);
		}

		if (LTIService.LTI_SECRET_INCOMPLETE.equals(key) && LTIService.LTI_SECRET_INCOMPLETE.equals(secret)) {
			return postError("<p>" + getRB(rb, "error.tool.partial", "Tool item is incomplete, missing a key and secret.") + "</p>");
		}

		// Picking the launch is a little tricky...
		Long toolVersion = getLongNull(tool.get(LTIService.LTI_VERSION));  // LTI 1.x OR 2.x
		// 0=inherit from tool, 1=LTI 1.1, 2=LTI 1.3
		Long contentLTI13 = getLongNull(content.get(LTIService.LTI13));
		Long toolLTI13 = getLongNull(tool.get(LTIService.LTI13));

		boolean isLTI1 = toolVersion == null || (!toolVersion.equals(LTIService.LTI_VERSION_2));
		boolean isLTI2 = !isLTI1;  // In case there is an LTI 3
		boolean isLTI13 = toolLTI13.equals(1L) && !contentLTI13.equals(1L);
		if (secret == null || key == null && toolLTI13.equals(1L)) {
			isLTI13 = true;  // No way to launch LTI 1.1
		}

		log.debug("toolVersion={} isLTI1={} isLTI13={}", toolVersion, isLTI1, isLTI13);

		// If we are doing LTI2, We will need a ToolProxyBinding
		ToolProxyBinding toolProxyBinding = null;
		JSONArray enabledCapabilities = null;
		if (isLTI2) {
			String tool_proxy_binding = (String) tool.get(LTI2Constants.TOOL_PROXY_BINDING);
			if (tool_proxy_binding != null && tool_proxy_binding.trim().length() > 0) {
				toolProxyBinding = new ToolProxyBinding(tool_proxy_binding);
				enabledCapabilities = toolProxyBinding.enabledCapabilities(LTI2Messages.BASIC_LTI_LAUNCH_REQUEST);
			}
		}

		/*              As of 2017-03-25, The LTI 2.0 test has morphed into the LTI 2.1 test in many ways
                even though LTI 2.1 is not yet approved.  Bummer that things morph under your feet
                but the 2017 version is probably better than the 2016 version - but tool folks will
                feel a "stinging sensation" as fields vanish when LMS's re-certify the same spec
                a while later.

		// As of 2016-02, LTI 2.0 certification fails these:
		//    5.8: Send a lis_person_sourcedid
		//    6.9: Send a lis_course_offering_sourcedid
		//    6.10: Send a lis_course_section_sourcedid
		// Unless we include the fields below (generally needed by consumers)

		// LTI 2.1 fails "12.5: Launch parameters are consistent with tool proxy"
		// If we do include them or any of the "launch_presentation_*" fields

		// The normal case is to include these fields to help out the provider
		// The special certification case is for LTI 2.1 (unless we can get the
		// certification fixed by the time it is final).

		if ( enabledCapabilities != null && ! checkStrictVersion("02.01.00","02.01.00") ) {
			addEnabledCapability(enabledCapabilities, LTI2Vars.PERSON_SOURCEDID);
			addEnabledCapability(enabledCapabilities, LTI2Vars.RESULT_SOURCEDID);
			// It looks like as of 2017-03-25, LTI 2.0 finds this distasteful
			// addEnabledCapability(enabledCapabilities, LTI2Vars.BASICOUTCOME_SOURCEDID);
			addEnabledCapability(enabledCapabilities, LTI2Vars.COURSEOFFERING_SOURCEDID);
			addEnabledCapability(enabledCapabilities, LTI2Vars.COURSESECTION_SOURCEDID);

			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_DOCUMENT_TARGET);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_HEIGHT);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_LOCALE);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_WIDTH);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LAUNCH_PRESENTATION_CSS_URL);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LIS_COURSE_OFFERING_SOURCEDID);
			addEnabledCapability(enabledCapabilities, BasicLTIConstants.LIS_COURSE_SECTION_SOURCEDID);
		}
		 */
		// Start building up the properties
		Properties ltiProps = new Properties();
		Properties toolProps = new Properties();
		Properties lti2subst = new Properties();
		if (isLTI1) {
			setProperty(ltiProps, BasicLTIConstants.LTI_VERSION, BasicLTIConstants.LTI_VERSION_1);
		} else {
			setProperty(ltiProps, BasicLTIConstants.LTI_VERSION, BasicLTIConstants.LTI_VERSION_2);
		}
		addGlobalData(site, ltiProps, lti2subst, rb);
		addSiteInfo(ltiProps, lti2subst, site);
		addRoleInfo(ltiProps, lti2subst, context, (String) tool.get("rolemap"));
		addUserInfo(ltiProps, lti2subst, tool);

		// This is for 1.2 - Not likely to be used
		// http://www.imsglobal.org/lti/ltiv1p2/ltiIMGv1p2.html
		if (deploy != null) {
			setProperty(lti2subst, LTI2Vars.TOOLCONSUMERPROFILE_URL, getOurServerUrl()
					+ LTI2_PATH + SVC_tc_profile + "/"
					+ (String) deploy.get(LTIService.LTI_CONSUMERKEY));
		}

		String resource_link_id = "content:" + content.get(LTIService.LTI_ID);
		setProperty(ltiProps, BasicLTIConstants.RESOURCE_LINK_ID, resource_link_id);
		setProperty(lti2subst, "ResourceLink.id", resource_link_id);

		setProperty(toolProps, "launch_url", launch_url);
		setProperty(toolProps, "state", state);  // So far LTI 1.3 only
		setProperty(toolProps, "nonce", nonce);  // So far LTI 1.3 only

		setProperty(toolProps, LTIService.LTI_SECRET, secret);
		setProperty(toolProps, "key", key);

		int debug = getInt(tool.get(LTIService.LTI_DEBUG));
		if (debug == 2) {
			debug = getInt(content.get(LTIService.LTI_DEBUG));
		}
		setProperty(toolProps, LTIService.LTI_DEBUG, debug + "");

		int frameheight = getInt(tool.get(LTIService.LTI_FRAMEHEIGHT));
		if (frameheight == 2) {
			frameheight = getInt(content.get(LTIService.LTI_FRAMEHEIGHT));
		}
		setProperty(toolProps, LTIService.LTI_FRAMEHEIGHT, frameheight + "");

		int newpage = getInt(tool.get(LTIService.LTI_NEWPAGE));
		if (newpage == 2) {
			newpage = getInt(content.get(LTIService.LTI_NEWPAGE));
		}
		setProperty(toolProps, LTIService.LTI_NEWPAGE, newpage + "");

		String title = (String) content.get(LTIService.LTI_TITLE);
		if (title == null) {
			title = (String) tool.get(LTIService.LTI_TITLE);
		}
		if (title != null) {
			setProperty(ltiProps, BasicLTIConstants.RESOURCE_LINK_TITLE, title);
			setProperty(ltiProps, BasicLTIConstants.RESOURCE_LINK_DESCRIPTION, title);
			setProperty(lti2subst, LTI2Vars.RESOURCELINK_TITLE, title);
			setProperty(lti2subst, LTI2Vars.RESOURCELINK_DESCRIPTION, title);
		}

		User user = UserDirectoryService.getCurrentUser();

		int allowoutcomes = getInt(tool.get(LTIService.LTI_ALLOWOUTCOMES));
		int allowroster = getInt(tool.get(LTIService.LTI_ALLOWROSTER));
		int allowsettings = getInt(tool.get(LTIService.LTI_ALLOWSETTINGS));
		String placement_secret = (String) content.get(LTIService.LTI_PLACEMENTSECRET);
		// int tool_id = getInt(tool.get(LTIService.LTI_ID));

		String result_sourcedid = getSourceDID(user, resource_link_id, placement_secret);
		if (result_sourcedid != null) {

			String theRole = getRoleString(context);
			if (allowoutcomes == 1) {
				// New Basic Outcomes URL
				String outcome_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lis_basic_outcome_url", null);
				if (outcome_url == null) {
					outcome_url = getOurServerUrl() + LTI1_PATH;
				}
				setProperty(ltiProps, "ext_ims_lis_basic_outcome_url", outcome_url);
				outcome_url = ServerConfigurationService.getString("basiclti.consumer." + BasicLTIConstants.LIS_OUTCOME_SERVICE_URL, null);
				if (outcome_url == null) {
					outcome_url = getOurServerUrl() + LTI1_PATH;
				}
				setProperty(ltiProps, BasicLTIConstants.LIS_OUTCOME_SERVICE_URL, outcome_url);
				setProperty(lti2subst, LTI2Vars.BASICOUTCOME_URL, outcome_url);

				if (theRole.contains(LTI2Vars.MEMBERSHIP_ROLE_LEARNER)) {
					setProperty(ltiProps, BasicLTIConstants.LIS_RESULT_SOURCEDID, result_sourcedid);
					setProperty(lti2subst, LTI2Vars.RESULT_SOURCEDID, result_sourcedid);
					setProperty(lti2subst, LTI2Vars.BASICOUTCOME_SOURCEDID, result_sourcedid);
					String result_url = getOurServerUrl() + LTI2_PATH + SVC_Result + "/" + result_sourcedid;
					setProperty(lti2subst, LTI2Vars.RESULT_URL, result_url);
				}
			}

			// We continue to support the old settings for LTI 2 see SAK-25621
			if (allowsettings == 1) {
				setProperty(ltiProps, "ext_ims_lti_tool_setting_id", result_sourcedid);

				String service_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lti_tool_setting_url", null);
				if (service_url == null) {
					service_url = getOurServerUrl() + LTI1_PATH;
				}
				setProperty(ltiProps, "ext_ims_lti_tool_setting_url", service_url);
				if (!isLTI1) {
					String settings_url = getOurServerUrl() + LTI2_PATH + SVC_Settings + "/";
					setProperty(lti2subst, LTI2Vars.LTILINK_CUSTOM_URL, settings_url + LTI2Util.SCOPE_LtiLink + "/" + resource_link_id);
					setProperty(lti2subst, LTI2Vars.TOOLPROXYBINDING_CUSTOM_URL, settings_url + LTI2Util.SCOPE_ToolProxyBinding + "/" + resource_link_id);
					setProperty(lti2subst, LTI2Vars.TOOLPROXY_CUSTOM_URL, settings_url + LTI2Util.SCOPE_ToolProxy + "/" + key);
				}
			}

			if (allowroster == 1) {
				setProperty(ltiProps, "ext_ims_lis_memberships_id", result_sourcedid);

				String roster_url = ServerConfigurationService.getString("basiclti.consumer.ext_ims_lis_memberships_url", null);
				if (roster_url == null) {
					roster_url = getOurServerUrl() + LTI1_PATH;
				}
				setProperty(ltiProps, "ext_ims_lis_memberships_url", roster_url);
			}

		}

		// Merge all the sources of properties according to the arcane precedence for launch
		Properties custom = new Properties();

		LTI2Util.mergeLTI2Custom(custom, (String) content.get(LTIService.LTI_SETTINGS));
		LTI2Util.mergeLTI2Custom(custom, (String) tool.get(LTIService.LTI_SETTINGS));
		LTI2Util.mergeLTI2Parameters(custom, (String) tool.get(LTIService.LTI_PARAMETER));
		if (proxyBinding != null) {
			LTI2Util.mergeLTI2Custom(custom, (String) proxyBinding.get(LTIService.LTI_SETTINGS));
		}
		if (deploy != null) {
			LTI2Util.mergeLTI2Custom(custom, (String) deploy.get(LTIService.LTI_SETTINGS));
		}

		int allowCustom = getInt(tool.get(LTIService.LTI_ALLOWCUSTOM));
		if (allowCustom == 1) {
			String contentCustom = (String) content.get(LTIService.LTI_CUSTOM);
			contentCustom = adjustCustom(contentCustom);
			LTI2Util.mergeLTI1Custom(custom, contentCustom);
		}

		String toolCustom = (String) tool.get(LTIService.LTI_CUSTOM);
		toolCustom = adjustCustom(toolCustom);
		LTI2Util.mergeLTI1Custom(custom, toolCustom);

		if (isLTI2) {
			log.debug("before ltiProps={}", ltiProps);
			log.debug("enabledCapabilities={}", enabledCapabilities);
			boolean allowExt = enabledCapabilities.contains(SAKAI_EXTENSIONS_ALL);
			LTI2Util.filterLTI1LaunchProperties(ltiProps, enabledCapabilities, allowExt);
		}

		// See if there are any locally deployed substitutions
		ltiService.filterCustomSubstitutions(lti2subst, tool, site);

		log.debug("ltiProps={}", ltiProps);
		log.debug("lti2subst={}", lti2subst);
		log.debug("before custom={}", custom);
		LTI2Util.substituteCustom(custom, lti2subst);
		log.debug("after custom={}", custom);

		// Place the custom values into the launch
		LTI2Util.addCustomToLaunch(ltiProps, custom, isLTI1);

		// Check which kind of signing we are supposed to do
		Long toolSHA256 = getLong(tool.get(LTIService.LTI_SHA256));
		Long contentSHA256 = getLong(content.get(LTIService.LTI_SHA256));
		if (toolSHA256.equals(1L) || (toolSHA256.equals(2L) && contentSHA256.equals(1L))) {
			ltiProps.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA256");
			log.debug("Launching with SHA256 Signing");
		} else if (toolProxyBinding != null) {
			if (toolProxyBinding.enabledCapability(LTI2Messages.BASIC_LTI_LAUNCH_REQUEST,
					LTI2Caps.OAUTH_HMAC256)) {

				ltiProps.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA256");
				log.debug("Launching with SHA256 Signing");
			}
		}

		log.debug("LAUNCH TYPE {}", (isLTI1 ? "LTI 1" : "LTI 2"));
		if (isLTI13) {
			return postLaunchJWT(toolProps, ltiProps, tool, content, rb);
		}
		return postLaunchHTML(toolProps, ltiProps, rb);
	}

	/**
	 * An LTI 2.0 Registration launch
	 *
	 * This must return an HTML message as the [0] in the array If things are
	 * successful - the launch URL is in [1]
	 */
	public static String[] postRegisterHTML(Long deployKey, Map<String, Object> tool, ResourceLoader rb, String placementId) {
		if (tool == null) {
			return postError("<p>" + getRB(rb, "error.tool.missing", "Tool item is missing or improperly configured.") + "</p>");
		}

		int status = getInt(tool.get(LTIService.LTI_REG_STATE));
		if (status != 0) {
			return postError("<p>" + getRB(rb, "error.lti2.badstate", "Tool is in the wrong state to register") + "</p>");
		}

		String launch_url = (String) tool.get(LTIService.LTI_REG_LAUNCH);
		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.lti2.noreg", "This tool is has no registration url.") + "</p>");
		}

		String password = (String) tool.get(LTIService.LTI_REG_PASSWORD);
		String key = (String) tool.get(LTIService.LTI_REG_KEY);
		String consumerkey = (String) tool.get(LTIService.LTI_CONSUMERKEY);

		if (password == null || key == null || consumerkey == null) {
			return postError("<p>" + getRB(rb, "error.lti2.partial", "Tool item is incomplete, missing a key and password.") + "</p>");
		}

		// Start building up the properties
		Properties ltiProps = new Properties();

		setProperty(ltiProps, BasicLTIConstants.LTI_VERSION, LTI2Constants.LTI2_VERSION_STRING);
		setProperty(ltiProps, LTI2Constants.REG_KEY, key);
		// Also duplicate reg_key as the proposed Tool Proxy GUID
		setProperty(ltiProps, LTI2Constants.TOOL_PROXY_GUID, key);
		// TODO: Lets show off and encrypt this secret too...
		setProperty(ltiProps, LTI2Constants.REG_PASSWORD, password);
		setProperty(ltiProps, BasicLTIConstants.LTI_MESSAGE_TYPE, BasicLTIConstants.LTI_MESSAGE_TYPE_TOOLPROXYREGISTRATIONREQUEST);

		String serverUrl = getOurServerUrl();
		setProperty(ltiProps, LTI2Constants.TC_PROFILE_URL, serverUrl + LTI2_PATH + SVC_tc_profile + "/" + consumerkey);
		setProperty(ltiProps, BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL, serverUrl + "/portal/tool/" + placementId + "?panel=PostRegister&id=" + deployKey);

		int debug = getInt(tool.get(LTIService.LTI_DEBUG));

		log.debug("ltiProps={}", ltiProps);

		boolean dodebug = debug == 1;
		if (log.isDebugEnabled()) {
			dodebug = true;
		}
		String launchtext = getRB(rb, "launch.button", "Press to Launch External Tool");
		String postData = BasicLTIUtil.postLaunchHTML(ltiProps, launch_url, launchtext, dodebug, null);

		String[] retval = {postData, launch_url};
		return retval;
	}

	/**
	 * An LTI 2.0 Reregistration launch
	 *
	 * This must return an HTML message as the [0] in the array If things are
	 * successful - the launch URL is in [1]
	 */
	public static String[] postReregisterHTML(Long deployKey, Map<String, Object> deploy, ResourceLoader rb, String placementId) {
		if (deploy == null) {
			return postError("<p>" + getRB(rb, "error.deploy.missing", "Deployment is missing or improperly configured.") + "</p>");
		}

		int status = getInt(deploy.get("reg_state"));
		if (status == 0) {
			return postError("<p>" + getRB(rb, "error.deploy.badstate", "Deployment is in the wrong state to register") + "</p>");
		}

		// Figure out the launch URL to use unless we have been told otherwise
		String launch_url = (String) deploy.get("reg_launch");

		// Find the global message for Reregistration
		String reg_profile = (String) deploy.get("reg_profile");

		ToolProxy toolProxy = null;
		try {
			toolProxy = new ToolProxy(reg_profile);
		} catch (Throwable t) {
			return postError("<p>" + getRB(rb, "error.deploy.badproxy", "This deployment has a broken reg_profile.") + "</p>");
		}

		JSONObject proxy_message = toolProxy.getMessageOfType("ToolProxyReregistrationRequest");
		String re_path = toolProxy.getPathFromMessage(proxy_message);
		if (re_path != null) {
			launch_url = re_path;
		}

		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.deploy.noreg", "This deployment is has no registration url.") + "</p>");
		}

		String consumerkey = (String) deploy.get(LTIService.LTI_CONSUMERKEY);
		String secret = (String) deploy.get(LTIService.LTI_SECRET);

		// If secret is encrypted, decrypt it
		secret = decryptSecret(secret);

		if (secret == null || consumerkey == null) {
			return postError("<p>" + getRB(rb, "error.deploy.partial", "Deployment is incomplete, missing a key and secret.") + "</p>");
		}

		// Start building up the properties
		Properties ltiProps = new Properties();

		setProperty(ltiProps, BasicLTIConstants.LTI_VERSION, LTI2Constants.LTI2_VERSION_STRING);
		setProperty(ltiProps, BasicLTIConstants.LTI_MESSAGE_TYPE, BasicLTIConstants.LTI_MESSAGE_TYPE_TOOLPROXY_RE_REGISTRATIONREQUEST);

		String serverUrl = getOurServerUrl();
		setProperty(ltiProps, LTI2Constants.TC_PROFILE_URL, serverUrl + LTI2_PATH + SVC_tc_profile + "/" + consumerkey);
		setProperty(ltiProps, BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL, serverUrl + "/portal/tool/" + placementId + "?panel=PostRegister&id=" + deployKey);

		int debug = getInt(deploy.get(LTIService.LTI_DEBUG));

		// Handle any substution variables from the message
		Properties lti2subst = new Properties();
		addGlobalData(null, ltiProps, lti2subst, rb);
		setProperty(lti2subst, LTI2Vars.TOOLCONSUMERPROFILE_URL, getOurServerUrl()
				+ LTI2_PATH + SVC_tc_profile + "/"
				+ (String) deploy.get(LTIService.LTI_CONSUMERKEY));

		Properties custom = new Properties();
		JSONArray parameter = toolProxy.getParameterFromMessage(proxy_message);
		if (parameter != null) {
			LTI2Util.mergeLTI2Parameters(custom, parameter.toString());
			log.debug("lti2subst={}", lti2subst);
			log.debug("before custom={}", custom);
			LTI2Util.substituteCustom(custom, lti2subst);
			log.debug("after custom={}", custom);
			// Merge the custom values into the launch
			LTI2Util.addCustomToLaunch(ltiProps, custom, false);
		}

		Map<String, String> extra = new HashMap<String, String>();
		ltiProps = BasicLTIUtil.signProperties(ltiProps, launch_url, "POST",
				consumerkey, secret, null, null, null, extra);

		log.debug("signed ltiProps={}", ltiProps);

		boolean dodebug = debug == 1;
		if (log.isDebugEnabled()) {
			dodebug = true;
		}
		String launchtext = getRB(rb, "launch.button", "Press to Launch External Tool");
		String postData = BasicLTIUtil.postLaunchHTML(ltiProps, launch_url, launchtext, dodebug, extra);

		String[] retval = {postData, launch_url};
		return retval;
	}

	/**
	 * Build a URL, Adding Sakai's CSRF token
	 */
	public static String addCSRFToken(String url) {
		Session session = SessionManager.getCurrentSession();
		Object csrfToken = session.getAttribute(UsageSessionService.SAKAI_CSRF_SESSION_ATTRIBUTE);
		if (!url.contains("?")) {
			url = url + "?";
		} else {
			url = url + "&";
		}
		url = url + "sakai_csrf_token=" + URLEncoder.encode(csrfToken.toString());
		return url;
	}

	/**
	 * Create a ContentItem from the current request (may throw runtime)
	 */
	public static ContentItem getContentItemFromRequest(Map<String, Object> tool) {

		Placement placement = ToolManager.getCurrentPlacement();
		String siteId = placement.getContext();

		String toolSiteId = (String) tool.get(LTIService.LTI_SITE_ID);
		if (toolSiteId != null && !toolSiteId.equals(siteId)) {
			throw new RuntimeException("Incorrect site id");
		}

		HttpServletRequest req = ToolUtils.getRequestFromThreadLocal();

		String lti_log = req.getParameter("lti_log");
		String lti_errorlog = req.getParameter("lti_errorlog");
		if (lti_log != null) {
			log.debug(lti_log);
		}
		if (lti_errorlog != null) {
			log.warn(lti_errorlog);
		}

		ContentItem contentItem = new ContentItem(req);

		String oauth_consumer_key = req.getParameter("oauth_consumer_key");
		String oauth_secret = (String) tool.get(LTIService.LTI_SECRET);
		oauth_secret = decryptSecret(oauth_secret);

		String URL = getOurServletPath(req);
		if (!contentItem.validate(oauth_consumer_key, oauth_secret, URL)) {
			log.warn("Provider failed to validate message: {}", contentItem.getErrorMessage());
			String base_string = contentItem.getBaseString();
			if (base_string != null) {
				log.warn("base_string={}", base_string);
			}
			throw new RuntimeException("Failed OAuth validation");
		}
		return contentItem;
	}

		/**
	 * Create a ContentItem from the current request (may throw runtime)
	 */
	public static DeepLinkResponse getDeepLinkFromToken(Map<String, Object> tool, String id_token) {

		Placement placement = ToolManager.getCurrentPlacement();
		String siteId = placement.getContext();

		String toolSiteId = (String) tool.get(LTIService.LTI_SITE_ID);
		if (toolSiteId != null && !toolSiteId.equals(siteId)) {
			throw new RuntimeException("Incorrect site id");
		}

		HttpServletRequest req = ToolUtils.getRequestFromThreadLocal();

		String lti_log = req.getParameter("lti_log");
		String lti_errorlog = req.getParameter("lti_errorlog");
		if (lti_log != null) {
			log.debug(lti_log);
		}
		if (lti_errorlog != null) {
			log.warn(lti_errorlog);
		}

		String publicKeyStr = (String) tool.get(LTIService.LTI13_TOOL_PUBLIC);
		if (publicKeyStr == null) {
			throw new RuntimeException("Could not find tool public key");
		}

		Key publicKey = LTI13Util.string2PublicKey(publicKeyStr);
		if (publicKey == null) {
			throw new RuntimeException("Could not deserialize tool public key");
		}

		// Fill up the object, validate and return
		DeepLinkResponse dlr = new DeepLinkResponse(id_token);
		if ( ! dlr.validate(publicKey) ) {
			throw new RuntimeException("Could not verify signature");
		}

		return dlr;
	}

	/**
	 * An LTI 2.0 ContentItemSelectionRequest launch
	 *
	 * This must return an HTML message as the [0] in the array If things are
	 * successful - the launch URL is in [1]
	 */
	public static String[] postContentItemSelectionRequest(Long toolKey, Map<String, Object> tool,
			String state, String nonce, ResourceLoader rb, String contentReturn, Properties dataProps) {
		if (tool == null) {
			return postError("<p>" + getRB(rb, "error.tool.missing", "Tool is missing or improperly configured.") + "</p>");
		}

		String launch_url = (String) tool.get("launch");
		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.tool.noreg", "This tool is has no launch url.") + "</p>");
		}

		String consumerkey = (String) tool.get(LTIService.LTI_CONSUMERKEY);
		String secret = (String) tool.get(LTIService.LTI_SECRET);

		// If secret is encrypted, decrypt it
		secret = decryptSecret(secret);

		Long toolVersion = getLongNull(tool.get(LTIService.LTI_VERSION));
		boolean isLTI1 = toolVersion == null || (!toolVersion.equals(LTIService.LTI_VERSION_2));
		boolean isLTI2 = !isLTI1;  // In case there is an LTI 3

		// LTI 1.3 is a variation on  LTI 1.1
		Long toolLTI13 = getLongNull(tool.get(LTIService.LTI13));
		boolean isLTI13 = toolLTI13.equals(1L);
		if (secret == null || consumerkey == null && toolLTI13.equals(1L)) {
			isLTI13 = true;  // No way to launch LTI 1.1
		}
		log.debug("toolVersion={} isLTI1={} isLTI13={}", toolVersion, isLTI1, isLTI13);

		if (!isLTI13 && (secret == null || consumerkey == null)) {
			return postError("<p>" + getRB(rb, "error.tool.partial", "Tool is incomplete, missing a key and secret.") + "</p>");
		}

		// Start building up the properties
		Properties ltiProps = new Properties();

		setProperty(ltiProps, BasicLTIConstants.LTI_VERSION, BasicLTIConstants.LTI_VERSION_1);
		setProperty(ltiProps, BasicLTIConstants.LTI_MESSAGE_TYPE, LTI2Messages.CONTENT_ITEM_SELECTION_REQUEST);

		setProperty(ltiProps, ContentItem.ACCEPT_MEDIA_TYPES, ContentItem.MEDIA_LTILINKITEM);
		setProperty(ltiProps, BasicLTIConstants.ACCEPT_PRESENTATION_DOCUMENT_TARGETS, "iframe,window"); // Nice to add overlay
		setProperty(ltiProps, BasicLTIConstants.ACCEPT_UNSIGNED, "true");
		setProperty(ltiProps, BasicLTIConstants.ACCEPT_MULTIPLE, "false");
		setProperty(ltiProps, BasicLTIConstants.ACCEPT_COPY_ADVICE, "false"); // ???
		setProperty(ltiProps, BasicLTIConstants.AUTO_CREATE, "true");
		setProperty(ltiProps, BasicLTIConstants.CAN_CONFIRM, "false");
		// setProperty(ltiProps, BasicLTIConstants.TITLE, "");
		// setProperty(ltiProps, BasicLTIConstants.TEXT, "");

		// Pull in additonal data
		JSONObject dataJSON = new JSONObject();
		Enumeration en = dataProps.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = dataProps.getProperty(key);
			if (value == null) {
				continue;
			}

			// Allow overrides
			if (BasicLTIConstants.ACCEPT_MEDIA_TYPES.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.ACCEPT_MEDIA_TYPES, value);
				continue;
			} else if (BasicLTIConstants.ACCEPT_PRESENTATION_DOCUMENT_TARGETS.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.ACCEPT_PRESENTATION_DOCUMENT_TARGETS, value);
				continue;
			} else if (BasicLTIConstants.ACCEPT_UNSIGNED.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.ACCEPT_UNSIGNED, value);
				continue;
			} else if (BasicLTIConstants.AUTO_CREATE.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.AUTO_CREATE, value);
				continue;
			} else if (BasicLTIConstants.CAN_CONFIRM.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.CAN_CONFIRM, value);
				continue;
			} else if (BasicLTIConstants.TITLE.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.TITLE, value);
				continue;
			} else if (BasicLTIConstants.TEXT.equals(key)) {
				setProperty(ltiProps, BasicLTIConstants.TEXT, value);
				continue;
			}

			// Pass in data for use to get back.
			dataJSON.put(key, value);
		}
		setProperty(ltiProps, BasicLTIConstants.DATA, dataJSON.toString());

		setProperty(ltiProps, BasicLTIConstants.CONTENT_ITEM_RETURN_URL, contentReturn);

		// This must always be there
		String context = (String) tool.get(LTIService.LTI_SITE_ID);
		Site site;
		try {
			site = SiteService.getSite(context);
		} catch (IdUnusedException e) {
			log.error("No site/page associated with Launch context={}", context);
			return postError("<p>" + getRB(rb, "error.site.missing", "Cannot load site.") + context + "</p>");
		}

		Properties lti2subst = new Properties();

		addGlobalData(site, ltiProps, lti2subst, rb);
		addSiteInfo(ltiProps, lti2subst, site);
		addRoleInfo(ltiProps, lti2subst, context, (String) tool.get("rolemap"));

		int releasename = getInt(tool.get(LTIService.LTI_SENDNAME));
		int releaseemail = getInt(tool.get(LTIService.LTI_SENDEMAILADDR));
		addUserInfo(ltiProps, lti2subst, tool);

		// Don't sent the normal return URL when we are doing ContentItem launch
		// Certification Issue
		if (LTI2Messages.CONTENT_ITEM_SELECTION_REQUEST.equals(ltiProps.getProperty(BasicLTIConstants.LTI_MESSAGE_TYPE))) {

			ltiProps.remove(BasicLTIConstants.LAUNCH_PRESENTATION_RETURN_URL);
		}

		String customstr = toNull((String) tool.get(LTIService.LTI_CUSTOM));
		parseCustom(ltiProps, customstr);

		boolean dodebug = getInt(tool.get(LTIService.LTI_DEBUG)) == 1;
		if (log.isDebugEnabled()) {
			dodebug = true;
		}

		if ( isLTI13 ) {
			Properties toolProps = new Properties();
			toolProps.put("launch_url", launch_url);
			setProperty(toolProps, "state", state);  // So far LTI 1.3 only
			setProperty(toolProps, "nonce", nonce);  // So far LTI 1.3 only
			toolProps.put(LTIService.LTI_DEBUG, dodebug ? "1" : "0");

			Map<String, Object> content = null;
			return postLaunchJWT(toolProps, ltiProps, tool, content, rb);
		}

		// If we are doing LTI2, We will need a ToolProxyBinding
		ToolProxyBinding toolProxyBinding = null;
		JSONArray enabledCapabilities = null;
		if (isLTI2) {
			String tool_proxy_binding = (String) tool.get(LTI2Constants.TOOL_PROXY_BINDING);
			if (tool_proxy_binding != null && tool_proxy_binding.trim().length() > 0) {
				toolProxyBinding = new ToolProxyBinding(tool_proxy_binding);
				enabledCapabilities = toolProxyBinding.enabledCapabilities(LTI2Messages.CONTENT_ITEM_SELECTION_REQUEST);
			}
		}

		// Check which kind of signing we are supposed to do
		Long toolSHA256 = getLong(tool.get(LTIService.LTI_SHA256));
		if (toolSHA256.equals(1L)) {
			ltiProps.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA256");
			log.debug("Launching with SHA256 Signing");
		} else if (toolProxyBinding != null) {
			if (toolProxyBinding.enabledCapability(LTI2Messages.BASIC_LTI_LAUNCH_REQUEST,
					LTI2Caps.OAUTH_HMAC256)) {

				ltiProps.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA256");
				log.debug("Launching with SHA256 Signing");
			}
		}

		Map<String, String> extra = new HashMap<>();
		ltiProps = BasicLTIUtil.signProperties(ltiProps, launch_url, "POST",
				consumerkey, secret, null, null, null, extra);

		log.debug("signed ltiProps={}", ltiProps);

		String launchtext = getRB(rb, "launch.button", "Press to Launch External Tool");
		String postData = BasicLTIUtil.postLaunchHTML(ltiProps, launch_url, launchtext, dodebug, extra);

		String[] retval = {postData, launch_url};
		return retval;
	}

	// This must return an HTML message as the [0] in the array
	// If things are successful - the launch URL is in [1]
	public static String[] postLaunchHTML(String placementId, ResourceLoader rb) {
		if (placementId == null) {
			return postError("<p>" + getRB(rb, "error.missing", "Error, missing placementId") + "</p>");
		}
		ToolConfiguration placement = SiteService.findTool(placementId);
		if (placement == null) {
			return postError("<p>" + getRB(rb, "error.load", "Error, cannot load placement=") + placementId + ".</p>");
		}

		// Add user, course, etc to the launch parameters
		Properties ltiProps = new Properties();
		if (!sakaiInfo(ltiProps, placement, rb)) {
			return postError("<p>" + getRB(rb, "error.missing",
					"Error, cannot load Sakai information for placement=") + placementId + ".</p>");
		}

		// Retrieve the launch detail
		Properties toolProps = new Properties();
		if (!loadFromPlacement(toolProps, ltiProps, placement)) {
			return postError("<p>" + getRB(rb, "error.nolaunch", "Not Configured.") + "</p>");
		}
		return postLaunchHTML(toolProps, ltiProps, rb);
	}

	public static String[] postLaunchHTML(Properties toolProps, Properties ltiProps, ResourceLoader rb) {

		String launch_url = toolProps.getProperty("secure_launch_url");
		if (launch_url == null) {
			launch_url = toolProps.getProperty("launch_url");
		}
		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.missing", "Not configured") + "</p>");
		}

		String org_guid = ServerConfigurationService.getString("basiclti.consumer_instance_guid",
				ServerConfigurationService.getString("serverName", null));
		String org_desc = ServerConfigurationService.getString("basiclti.consumer_instance_description", null);
		String org_url = ServerConfigurationService.getString("basiclti.consumer_instance_url",
				ServerConfigurationService.getString("serverUrl", null));

		// Look up the LMS-wide secret and key - default key is guid
		String key = getToolConsumerInfo(launch_url, "key");
		if (key == null) {
			key = org_guid;
		}
		String secret = getToolConsumerInfo(launch_url, LTIService.LTI_SECRET);

		// Demand key/secret in a pair
		if (key == null || secret == null) {
			key = null;
			secret = null;
		}

		// If we do not have LMS-wide info, use the local key/secret
		if (secret == null) {
			secret = toNull(toolProps.getProperty(LTIService.LTI_SECRET));
			key = toNull(toolProps.getProperty("key"));
		}

		// If secret is encrypted, decrypt it
		secret = decryptSecret(secret);

		// Pull in all of the custom parameters
		for (Object okey : toolProps.keySet()) {
			String skey = (String) okey;
			if (!skey.startsWith(BasicLTIConstants.CUSTOM_PREFIX)) {
				continue;
			}
			String value = toolProps.getProperty(skey);
			if (value == null) {
				continue;
			}
			setProperty(ltiProps, skey, value);
		}

		String oauth_callback = ServerConfigurationService.getString("basiclti.oauth_callback", null);
		// Too bad there is not a better default callback url for OAuth
		// Actually since we are using signing-only, there is really not much point
		// In OAuth 6.2.3, this is after the user is authorized
		if (oauth_callback == null) {
			oauth_callback = "about:blank";
		}
		setProperty(ltiProps, "oauth_callback", oauth_callback);

		// Sanity checks
		if (secret == null) {
			return postError("<p>" + getRB(rb, "error.nosecret", "Error - must have a secret.") + "</p>");
		}
		if (secret != null && key == null) {
			return postError("<p>" + getRB(rb, "error.nokey", "Error - must have a secret and a key.") + "</p>");
		}

		Map<String, String> extra = new HashMap<>();
		ltiProps = BasicLTIUtil.signProperties(ltiProps, launch_url, "POST",
				key, secret, org_guid, org_desc, org_url, extra);

		if (ltiProps == null) {
			return postError("<p>" + getRB(rb, "error.sign", "Error signing message.") + "</p>");
		}
		log.debug("LAUNCH III={}", ltiProps);

		String debugProperty = toolProps.getProperty(LTIService.LTI_DEBUG);
		boolean dodebug = "on".equals(debugProperty) || "1".equals(debugProperty);
		if (log.isDebugEnabled()) {
			dodebug = true;
		}

		String launchtext = getRB(rb, "launch.button", "Press to Launch External Tool");
		String postData = BasicLTIUtil.postLaunchHTML(ltiProps, launch_url, launchtext, dodebug, extra);

		String[] retval = {postData, launch_url};
		return retval;
	}

	// In case this gets more complex later
	public static String getDeploymentId(String site_id) {
		String deployment_id = ServerConfigurationService.getString(LTI13_DEPLOYMENT_ID, LTI13_DEPLOYMENT_ID_DEFAULT);
		return deployment_id;
	}

	public static String getIssuer(String site_id) {
		String retval = getOurServerUrl();
		String deployment_id = getDeploymentId(site_id);
		if ( ! LTI13_DEPLOYMENT_ID_DEFAULT.equals(deployment_id) ) {
				retval += "/deployment/" + deployment_id;
		}
		if ( StringUtils.isNotEmpty(site_id) ) {
			retval = retval + "/site/" + site_id;
		}
		return retval;
	}

	public static String getSubject(String user_id, String site_id) {
		String retval = getOurServerUrl();
		String deployment_id = getDeploymentId(site_id);
		if ( ! LTI13_DEPLOYMENT_ID_DEFAULT.equals(deployment_id) ) {
				retval += "/deployment/" + deployment_id;
		}
		retval = retval + "/user/" + user_id;
		return retval;
	}

	// Return the Sakai user_id from an LTI 1.3 Subject
	// https://dev1.sakaicloud.com/user/c71bb6b6-3f3c-4922-a1f9-73855570a0eb
	public static String parseSubject(String subject) {
		if ( subject == null ) return subject;
		String retval = getOurServerUrl();
		if ( ! subject.startsWith(getOurServerUrl()) ) return subject;

		String [] pieces = subject.split("/");
		int where = pieces.length-1;
		if ( where < 0 ) return subject;
		return pieces[where];
	}

	public static String[] postLaunchJWT(Properties toolProps, Properties ltiProps,
			Map<String, Object> tool, Map<String, Object> content, ResourceLoader rb) {
		String launch_url = toolProps.getProperty("secure_launch_url");
		if (launch_url == null) {
			launch_url = toolProps.getProperty("launch_url");
		}
		if (launch_url == null) {
			return postError("<p>" + getRB(rb, "error.missing", "Not configured") + "</p>");
		}

		String org_guid = ServerConfigurationService.getString("basiclti.consumer_instance_guid",
				ServerConfigurationService.getString("serverName", null));
		String org_desc = ServerConfigurationService.getString("basiclti.consumer_instance_description", null);
		String org_url = ServerConfigurationService.getString("basiclti.consumer_instance_url",
				ServerConfigurationService.getString("serverUrl", null));

		String orig_site_id_null = (String) tool.get("orig_site_id_null");
		String site_id = null;
		if ( ! "true".equals(orig_site_id_null) ) {
			site_id = (String) tool.get(LTIService.LTI_SITE_ID);
		}

		String client_id = (String) tool.get(LTIService.LTI13_CLIENT_ID);
		String platform_public = (String) tool.get(LTIService.LTI13_PLATFORM_PUBLIC);
		String platform_private = decryptSecret((String) tool.get(LTIService.LTI13_PLATFORM_PRIVATE));
		String placement_secret = null;
		if (content != null) {
			placement_secret = (String) content.get(LTIService.LTI_PLACEMENTSECRET);
		}

		if (platform_private == null) {
			return postError("<p>" + getRB(rb, "error.no.platform.private.key", "Missing Platform Private Key.") + "</p>");
		}

	/*

context_id: mercury
context_label: mercury site
context_title: mercury site
context_type: Group
custom_x=42
custom_y=043040450
ext_ims_lis_basic_outcome_url: http://localhost:8080/imsblis/service/
ext_ims_lis_memberships_id: c1007fb6345a87cd651785422a2925114d0707fad32c66edb6bfefbf2165819a:::admin:::content:3
ext_ims_lis_memberships_url: http://localhost:8080/imsblis/service/
ext_ims_lti_tool_setting_id: c1007fb6345a87cd651785422a2925114d0707fad32c66edb6bfefbf2165819a:::admin:::content:3
ext_ims_lti_tool_setting_url: http://localhost:8080/imsblis/service/
ext_lms: sakai-19-SNAPSHOT
ext_sakai_academic_session: OTHER
ext_sakai_launch_presentation_css_url_list: http://localhost:8080/library/skin/tool_base.css,http://localhost:8080/library/skin/morpheus-default/tool.css?version=49b21ca5
ext_sakai_role: maintain
ext_sakai_server: http://localhost:8080
ext_sakai_serverid: MacBook-Pro-92.local
launch_presentation_css_url: http://localhost:8080/library/skin/tool_base.css
launch_presentation_locale: en_US
launch_presentation_return_url: http://localhost:8080/imsblis/service/return-url/site/mercury
lis_course_offering_sourcedid: mercury
lis_course_section_sourcedid: mercury
lis_outcome_service_url: http://localhost:8080/imsblis/service/
lis_person_name_family: Administrator
lis_person_name_full: Sakai Administrator
lis_person_name_given: Sakai
lis_person_sourcedid: admin
lti_message_type: basic-lti-launch-request
lti_version: LTI-1p0
resource_link_description: Tsugi Breakout
resource_link_id: content:3
resource_link_title: Tsugi Breakout
roles: Instructor,Administrator,urn:lti:instrole:ims/lis/Administrator,urn:lti:sysrole:ims/lis/Administrator
user_id: admin
		 */

		String context_id = ltiProps.getProperty("context_id");

		// Lets make a JWT from the LTI 1.x data
		boolean deepLink = false;
		LaunchJWT lj = new LaunchJWT();
		if ( BasicLTIConstants.LTI_MESSAGE_TYPE_CONTENTITEMSELECTIONREQUEST.equals(ltiProps.getProperty(BasicLTIConstants.LTI_MESSAGE_TYPE)) ) {
			lj.message_type = LaunchJWT.MESSAGE_TYPE_DEEP_LINK;
			deepLink = true;
		}
		lj.target_link_uri = launch_url;  // The actual launch URL
		lj.launch_presentation.css_url = ltiProps.getProperty("launch_presentation_css_url");
		lj.locale = ltiProps.getProperty("launch_presentation_locale");
		lj.launch_presentation.return_url = ltiProps.getProperty("launch_presentation_return_url");
		lj.audience = client_id;
		lj.issuer = getIssuer(site_id);
		lj.lti11_legacy_user_id = (String) ltiProps.getProperty("user_id");
		lj.subject = getSubject(lj.lti11_legacy_user_id, context_id);
		lj.name = ltiProps.getProperty("lis_person_name_full");
		lj.nonce = toolProps.getProperty("nonce");
		lj.email = ltiProps.getProperty("lis_person_contact_email_primary");
		lj.issued = new Long(System.currentTimeMillis() / 1000L);
		lj.expires = lj.issued + 3600L;
		lj.deployment_id = getDeploymentId(context_id);
		// TODO: Check through the rolemap logic
		String lti1_roles = ltiProps.getProperty("roles");
		if (lti1_roles != null && lti1_roles.contains("Instructor")) {
			lj.roles.add(LaunchJWT.ROLE_INSTRUCTOR);
		} else {
			lj.roles.add(LaunchJWT.ROLE_LEARNER);
		}

		String resource_link_id = ltiProps.getProperty("resource_link_id");
		if ( resource_link_id != null ) {
			lj.resource_link = new ResourceLink();
			lj.resource_link.id = resource_link_id;
			lj.resource_link.title = ltiProps.getProperty("resource_link_title");
			lj.resource_link.description = ltiProps.getProperty("resource_link_description");
		}

		lj.context = new Context();
		lj.context.id = context_id;
		lj.context.label = ltiProps.getProperty("context_label");
		lj.context.title = ltiProps.getProperty("context_title");
		lj.context.type.add(Context.COURSE_OFFERING);

		lj.tool_platform = new ToolPlatform();
		lj.tool_platform.name = "Sakai";
		lj.tool_platform.version = ltiProps.getProperty("tool_consumer_info_version");
		lj.tool_platform.product_family_code = ltiProps.getProperty("tool_consumer_info_product_family_code");
		lj.tool_platform.url = org_url;
		lj.tool_platform.description = org_desc;

		LaunchLIS lis = new LaunchLIS();
		lis.person_sourcedid = ltiProps.getProperty("lis_person_sourcedid");
		lis.course_offering_sourcedid = ltiProps.getProperty("lis_course_offering_sourcedid");
		lis.course_section_sourcedid = ltiProps.getProperty("lis_course_section_sourcedid");
		lis.version = new ArrayList<>();
		lis.version.add("1.0.0");
		lis.version.add("1.1.0");
		lj.lis = lis;

		lj.custom = new TreeMap<>();
		for (Map.Entry<Object, Object> entry : ltiProps.entrySet()) {
			String custom_key = (String) entry.getKey();
			String custom_val = (String) entry.getValue();
			if (!custom_key.startsWith("custom_")) {
				continue;
			}
			custom_key = custom_key.substring(7);
			lj.custom.put(custom_key, custom_val);
		}

		int allowOutcomes = getInt(tool.get(LTIService.LTI_ALLOWOUTCOMES));
		int allowRoster = getInt(tool.get(LTIService.LTI_ALLOWROSTER));
		int allowSettings = getInt(tool.get(LTIService.LTI_ALLOWSETTINGS));
		int allowLineItems = getInt(tool.get(LTIService.LTI_ALLOWLINEITEMS));

		String sourcedid = ltiProps.getProperty("lis_result_sourcedid");

		if (sourcedid != null) {
			BasicOutcome outcome = new BasicOutcome();
			outcome.lis_result_sourcedid = ltiProps.getProperty("lis_result_sourcedid");
			outcome.lis_outcome_service_url = ltiProps.getProperty("lis_outcome_service_url");
			lj.basicoutcome = outcome;
		}

		String signed_placement = null;
		if (placement_secret != null && resource_link_id != null && context_id != null) {
			signed_placement = getSignedPlacement(context_id, resource_link_id, placement_secret);
		}

		if (signed_placement != null && (
			  ( (allowOutcomes != 0 && outcomesEnabled()) ||
				(allowLineItems != 0 && lineItemsEnabled()) )
			  )
			) {
			Endpoint endpoint = new Endpoint();
			endpoint.scope = new ArrayList<>();
			endpoint.scope.add(Endpoint.SCOPE_LINEITEM);

			if ( allowOutcomes != 0 && outcomesEnabled() ) {
				endpoint.lineitem = getOurServerUrl() + LTI13_PATH + "lineitem/" + signed_placement;
			}
			if ( allowOutcomes != 0 && outcomesEnabled() ) {
				endpoint.lineitems = getOurServerUrl() + LTI13_PATH + "lineitems/" + signed_placement;
			}
			lj.endpoint = endpoint;
		}

		if (allowRoster != 0 && rosterEnabled() && signed_placement != null) {
			NamesAndRoles nar = new NamesAndRoles();
			nar.context_memberships_url = getOurServerUrl() + LTI13_PATH + "namesandroles/" + signed_placement;
			lj.names_and_roles = nar;
		}

		/*
			Extra fields for DeepLink
			lti_message_type=ContentItemSelectionRequest
			accept_copy_advice=false
			accept_media_types=application/vnd.ims.lti.v1.ltiResourceLink
			accept_multiple=false
			accept_presentation_document_targets=iframe,window
			accept_unsigned=true
			auto_create=true
			can_confirm=false
			content_item_return_url=http://localhost:8080/portal/tool/6bdb721d-07f9-445b-a973-2190b50654cc/sakai.basiclti.admin.helper.helper?eventSubmit_doContentItemPut=Save&sakai.session=22702e53-60f3-45fd-b8db-a9d803eed3d4.MacBook-Pro-92.local&returnUrl=http%3A%2F%2Flocalhost%3A8080%2Fportal%2Fsite%2F92e7ddf2-1c60-486c-97ae-bc2ffbde8e67%2Ftool%2F4099b420-119a-4c39-9e05-0a933b2e5858%2FBltiPicker%3F3%26itemId%3D-1%26addBefore%3D&panel=PostContentItem&tool_id=13&sakai_csrf_token=458f712764cd597e96be99d2bab6d9da17d63c3834bc3770851a3d93ea8cdb83
			data={"remember":"always bring a towel"}

		    "deep_link_return_url": "https://platform.example/deep_links",
			"accept_types": ["link", "file", "html", "ltiResourceLink", "image"],
			"accept_media_types": "image/:::asterisk:::,text/html",
			"accept_presentation_document_targets": ["iframe", "window", "embed"],
			"accept_multiple": true,
			"auto_create": true,
			"title": "This is the default title",
			"text": "This is the default text",
			"data": "csrftoken:c7fbba78-7b75-46e3-9201-11e6d5f36f53"
		*/

		if ( deepLink ) {
			DeepLink ci = new DeepLink();
			// accept_copy_advice is not in deep linking - files are to be copied - images maybe
			String accept_media_types = ltiProps.getProperty("accept_media_types");
			if ( ContentItem.MEDIA_LTILINKITEM.equals(accept_media_types) ) {
				ci.accept_types.add(DeepLink.ACCEPT_TYPE_LTILINK);
			} else {
				ci.accept_media_types = ltiProps.getProperty("accept_media_types");
			}
			ci.accept_multiple = "true".equals(ltiProps.getProperty("accept_multiple"));
			String target = ltiProps.getProperty("accept_presentation_document_targets");
			if ( target != null ) {
				String [] pieces = target.split(",");
				for (String piece : pieces) {
					ci.accept_presentation_document_targets.add(piece);
				}
			}

			// Accept_unsigned is not in DeepLinking - they are signed JWTs
			ci.auto_create = "true".equals(ltiProps.getProperty("auto_create"));
			// can_confirm is not there
			ci.deep_link_return_url = ltiProps.getProperty(BasicLTIConstants.CONTENT_ITEM_RETURN_URL);
			ci.data = ltiProps.getProperty("data");
			lj.deep_link = ci;
		}

		String ljs = JacksonUtil.toString(lj);
		log.debug("ljs = {}", ljs);

		Key privateKey = LTI13Util.string2PrivateKey(platform_private);
		Key publicKey = LTI13Util.string2PublicKey(platform_public);

		if ( privateKey == null | publicKey == null ) {
			return postError("<p>" + getRB(rb, "error.no.pki", "Public and/or Private Key(s) not configured.") + "</p>");
		}

		String kid = LTI13KeySetUtil.getPublicKID(publicKey);

		String jws = Jwts.builder().setHeaderParam("kid", kid).
				setPayload(ljs).signWith(privateKey).compact();

		log.debug("jws = {}", jws);

		// Internal test for round trip
		// String subject = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jws).getBody().getSubject();
		// System.out.println("subject="+subject);
		String debugProperty = toolProps.getProperty(LTIService.LTI_DEBUG);
		boolean dodebug = "on".equals(debugProperty) || "1".equals(debugProperty);
		if (log.isDebugEnabled()) {
			dodebug = true;
		}

		String state = toolProps.getProperty("state");
		state = StringUtils.trimToNull(state);

		// This is a comma separated list of valid redirect URLs - lame as heck
		String lti13_oidc_redirect = toNull((String) tool.get(LTIService.LTI13_OIDC_REDIRECT));

		// If we have been told to send this to a redirect_uri instead of a launch...
		HttpServletRequest req = ToolUtils.getRequestFromThreadLocal();
		String redirect_uri = req.getParameter("redirect_uri");
		if ( redirect_uri != null && lti13_oidc_redirect != null ) {
			if ( lti13_oidc_redirect.indexOf(redirect_uri) >= 0 ) {
				launch_url = redirect_uri;
			}
		}

		String form_id = java.util.UUID.randomUUID().toString();
		String html = "<form action=\"" + launch_url + "\" id=\""+ form_id + "\" method=\"POST\">\n"
				+ "    <input type=\"hidden\" name=\"id_token\" value=\"" + BasicLTIUtil.htmlspecialchars(jws) + "\" />\n";

		if ( state != null ) {
			html += "    <input type=\"hidden\" name=\"state\" value=\"" + BasicLTIUtil.htmlspecialchars(state) + "\" />\n";
		}

		if ( dodebug ) {
			html += "    <input type=\"submit\" value=\"Proceed with LTI 1.3 Launch\" />\n</form>\n";
		}
		html += "    </form>\n";

		if ( ! dodebug ) {
			html += "<script>\n document.getElementById(\"" + form_id + "\").submit();\n</script>\n";
		} else {
			html += "<p>\n--- Unencoded JWT:<br/>"
					+ BasicLTIUtil.htmlspecialchars(ljs)
					+ "</p>\n<p>\n--- State:<br/>"
					+ BasicLTIUtil.htmlspecialchars(state)
					+ "</p>\n<p>\n--- Encoded JWT:<br/>"
					+ BasicLTIUtil.htmlspecialchars(jws)
					+ "</p>\n";
		}
		String[] retval = {html, launch_url};
		return retval;
	}

	public static String getSourceDID(User user, Placement placement, Properties config) {
		String placementSecret = toNull(getCorrectProperty(config, "placementsecret", placement));
		if (placementSecret == null) {
			return null;
		}
		return getSourceDID(user, placement.getId(), placementSecret);
	}

	public static String getSourceDID(User user, String placeStr, String placementSecret) {
		if (placementSecret == null) {
			return null;
		}
		String suffix = ":::" + user.getId() + ":::" + placeStr;
		String base_string = placementSecret + suffix;
		String signature = LegacyShaUtil.sha256Hash(base_string);
		return signature + suffix;
	}

	public static String getSignedPlacement(String context_id, String resource_link_id, String placementSecret) {
		if (placementSecret == null) {
			return null;
		}
		String suffix = ":::" + context_id + ":::" + resource_link_id;
		String base_string = placementSecret + suffix;
		String signature = LegacyShaUtil.sha256Hash(base_string);
		return signature + suffix;
	}

	public static String[] postError(String str) {
		String[] retval = {str};
		return retval;
	}

	public static String getRB(ResourceLoader rb, String key, String def) {
		if (rb == null) {
			return def;
		}
		return rb.getString(key, def);
	}

	// To make absolutely sure we never send an XSS, we clean these values
	public static void setProperty(Properties props, String key, String value) {
		if (value == null) {
			return;
		}
		if (props == null) {
			return;
		}
		value = Web.cleanHtml(value);
		if (value.trim().length() < 1) {
			return;
		}
		props.setProperty(key, value);
	}

	private static String getExternalRealmId(String siteId) {
		String realmId = SiteService.siteReference(siteId);
		String rv = null;
		try {
			AuthzGroup realm = ComponentManager.get(AuthzGroupService.class).getAuthzGroup(realmId);
			rv = realm.getProviderGroupId();
		} catch (GroupNotDefinedException e) {
			log.error("SiteParticipantHelper.getExternalRealmId: site realm not found {}", e.getMessage());
		}
		return rv;
	} // getExternalRealmId

	// Look through a series of secrets from the properties based on the launchUrl
	private static String getToolConsumerInfo(String launchUrl, String data) {
		String default_secret = ServerConfigurationService.getString("basiclti.consumer_instance_" + data, null);
		log.debug("launchUrl = {}", launchUrl);
		URL url;
		try {
			url = new URL(launchUrl);
		} catch (MalformedURLException e) {
			url = null;
		}

		if (url == null) {
			return default_secret;
		}
		String hostName = url.getHost();
		log.debug("host = {}", hostName);
		if (hostName == null || hostName.length() < 1) {
			return default_secret;
		}
		// Look for the property starting with the full name
		String org_info = ServerConfigurationService.getString("basiclti.consumer_instance_" + data + "." + hostName, null);
		if (org_info != null) {
			return org_info;
		}
		for (int i = 0; i < hostName.length(); i++) {
			if (hostName.charAt(i) != '.') {
				continue;
			}
			if (i > hostName.length() - 2) {
				continue;
			}
			String hostPart = hostName.substring(i + 1);
			String propName = "basiclti.consumer_instance_" + data + "." + hostPart;
			org_info = ServerConfigurationService.getString(propName, null);
			if (org_info != null) {
				return org_info;
			}
		}
		return default_secret;
	}

	// expected_oauth_key can be null - if it is non-null it must match the key in the request
	public static Object validateMessage(HttpServletRequest request, String URL,
			String oauth_secret, String expected_oauth_key) {
		oauth_secret = decryptSecret(oauth_secret);
		return BasicLTIUtil.validateMessage(request, URL, oauth_secret, expected_oauth_key);
	}

	// Returns:
	// String implies error
	// Boolean.TRUE - Sourcedid checks out
	// Boolean.FALSE - Sourcedid or secret fail
	public static Object checkSourceDid(String sourcedid, HttpServletRequest request,
			LTIService ltiService) {
		return handleGradebook(sourcedid, request, ltiService, false, false, null, null);
	}

	// Grade retrieval Map<String, Object> with "grade" => Double  and "comment" => String
	public static Object getGrade(String sourcedid, HttpServletRequest request,
			LTIService ltiService) {
		return handleGradebook(sourcedid, request, ltiService, true, false, null, null);
	}

	// Boolean.TRUE - Grade updated
	public static Object setGrade(String sourcedid, HttpServletRequest request,
			LTIService ltiService, Double grade, String comment) {
		return handleGradebook(sourcedid, request, ltiService, false, false, grade, comment);
	}

	// Boolean.TRUE - Grade deleted
	public static Object deleteGrade(String sourcedid, HttpServletRequest request,
			LTIService ltiService) {
		return handleGradebook(sourcedid, request, ltiService, false, true, null, null);
	}

	// Quite a long bit of code
	private static Object handleGradebook(String sourcedid, HttpServletRequest request,
			LTIService ltiService, boolean isRead, boolean isDelete,
			Double theGrade, String comment) {
		// Truncate this to the maximum length to insure no cruft at the end
		if (sourcedid.length() > 2048) {
			sourcedid = sourcedid.substring(0, 2048);
		}

		// Attempt to parse the sourcedid, any failure is fatal
		String placement_id = null;
		String signature = null;
		String user_id = null;
		try {
			int pos = sourcedid.indexOf(":::");
			if (pos > 0) {
				signature = sourcedid.substring(0, pos);
				String dec2 = sourcedid.substring(pos + 3);
				pos = dec2.indexOf(":::");
				user_id = dec2.substring(0, pos);
				placement_id = dec2.substring(pos + 3);
			}
		} catch (Exception e) {
			return "Unable to decrypt result_sourcedid=" + sourcedid;
		}

		log.debug("signature={}", signature);
		log.debug("user_id={}", user_id);
		log.debug("placement_id={}", placement_id);

		Properties pitch = getPropertiesFromPlacement(placement_id, ltiService);
		if (pitch == null) {
			return "Error retrieving result_sourcedid information";
		}

		String siteId = pitch.getProperty(LTIService.LTI_SITE_ID);
		Site site;
		try {
			site = SiteService.getSite(siteId);
		} catch (IdUnusedException e) {
			return "Error retrieving result_sourcedid site: " + e.getLocalizedMessage();
		}

		// Check the message signature using OAuth
		String oauth_secret = pitch.getProperty(LTIService.LTI_SECRET);
		log.debug("oauth_secret: {}", oauth_secret);
		oauth_secret = decryptSecret(oauth_secret);
		log.debug("oauth_secret (decrypted): {}", oauth_secret);

		String oauth_consumer_key = pitch.getProperty(LTIService.LTI_CONSUMERKEY);
		log.debug("oauth_consumer_key: {}", oauth_consumer_key);

		String URL = getOurServletPath(request);

		// Validate the incoming message
		Object retval = validateMessage(request, URL, oauth_secret, oauth_consumer_key);
		if (retval instanceof String) {
			return retval;
		}

		// Check the signature of the sourcedid to make sure it was not altered
		String placement_secret = pitch.getProperty(LTIService.LTI_PLACEMENTSECRET);
		if (placement_secret == null) {
			return "Could not find placement secret";
		}

		String pre_hash = placement_secret + ":::" + user_id + ":::" + placement_id;
		String received_signature = LegacyShaUtil.sha256Hash(pre_hash);
		log.debug("Received signature={} received={}", signature, received_signature);
		boolean matched = signature.equals(received_signature);

		String old_placement_secret = pitch.getProperty(LTIService.LTI_OLDPLACEMENTSECRET);
		if (old_placement_secret != null && !matched) {
			pre_hash = placement_secret + ":::" + user_id + ":::" + placement_id;
			received_signature = LegacyShaUtil.sha256Hash(pre_hash);
			log.debug("Received signature II={} received={}", signature, received_signature);
			matched = signature.equals(received_signature);
		}

		if (!matched) {
			return "Sourcedid signature did not match";
		}

		// If we are not supposed to lookup or set the grade, we are done
		if (isRead == false && isDelete == false && theGrade == null) {
			return new Boolean(matched);
		}

		// Look up the assignment so we can find the max points
		GradebookService g = (GradebookService) ComponentManager
				.get("org.sakaiproject.service.gradebook.GradebookService");

		// Make sure the user exists in the site
		boolean userExistsInSite = false;
		try {
			Member member = site.getMember(user_id);
			if (member != null) {
				userExistsInSite = true;
			}
		} catch (Exception e) {
			log.warn("{} siteId={}, {}", e.getLocalizedMessage(), siteId, e);
			return "User not found in site";
		}

		// Make sure the placement is configured to receive grades
		String assignment = pitch.getProperty("assignment");
		log.debug("ASSN={}", assignment);
		if (assignment == null) {
			return "Assignment not set in placement";
		}

		Assignment assignmentObject = getAssignment(site, user_id, assignment, 100L);
		if (assignmentObject == null) {
			log.warn("assignmentObject or Id is null, cannot proceed with grading in site {} for assignment {}", siteId, assignment);
			return "Grade failure siteId=" + siteId;
		}

		// Now read, set, or delete the grade...
		Session sess = SessionManager.getCurrentSession();
		String message = null;

		try {
			// Indicate "who" is setting this grade - needs to be a real user account
			String gb_user_id = ServerConfigurationService.getString(
					"basiclti.outcomes.userid", "admin");
			String gb_user_eid = ServerConfigurationService.getString(
					"basiclti.outcomes.usereid", gb_user_id);
			sess.setUserId(gb_user_id);
			sess.setUserEid(gb_user_eid);
			if (isRead) {
				String actualGrade = g.getAssignmentScoreString(siteId, assignmentObject.getId(), user_id);
				Double dGrade = null;
				if (actualGrade != null && actualGrade.length() > 0) {
					dGrade = new Double(actualGrade);
					dGrade = dGrade / assignmentObject.getPoints();
				}
				CommentDefinition commentDef = g.getAssignmentScoreComment(siteId, assignmentObject.getId(), user_id);
				message = "Result read";
				Map<String, Object> retMap = new TreeMap<>();
				retMap.put("grade", dGrade);
				if (commentDef != null) {
					retMap.put("comment", commentDef.getCommentText());
				}
				retval = retMap;
			} else if (isDelete) {
				g.setAssignmentScoreString(siteId, assignmentObject.getId(), user_id, null, "External Outcome");
				log.info("Delete Score site={} assignment={} user_id={}", siteId, assignment, user_id);
				message = "Result deleted";
				retval = Boolean.TRUE;
			} else {
				g.setAssignmentScoreString(siteId, assignmentObject.getId(), user_id, getRoundedGrade(theGrade, assignmentObject.getPoints()), "External Outcome");
				g.setAssignmentScoreComment(siteId, assignmentObject.getId(), user_id, comment);

				log.info("Stored Score={} assignment={} user_id={} score={}", siteId, assignment, user_id, theGrade);
				message = "Result replaced";
				retval = Boolean.TRUE;
			}
		} catch (Exception e) {
			retval = "Grade failure " + e.getMessage() + " siteId=" + siteId;
			log.warn("handleGradebook Grade failure in site: {}, error: {}", siteId, e);
		} finally {
			sess.invalidate(); // Make sure to leave no traces
		}

		return retval;
	}

	public static Object getGradeLTI13(Site site, Long tool_id, Map<String, Object> content, String user_id,
			String assignment) {
		return handleGradebookLTI13(site, tool_id, content, user_id, assignment, null, null, null, true, false);
	}

	// Boolean.TRUE - Grade updated
	public static Object setGradeLTI13(Site site, Long tool_id, Map<String, Object> content, String user_id,
			String assignment, Long scoreGiven, Long maxPoints, String comment) {
		return handleGradebookLTI13(site, tool_id, content, user_id, assignment, scoreGiven, maxPoints, comment, false, false);
	}

	// Boolean.TRUE - Grade deleted
	public static Object deleteGradeLTI13(Site site, Long tool_id, Map<String, Object> content, String user_id,
			String assignment) {
		return handleGradebookLTI13(site, tool_id, content, user_id, assignment, null, null, null, false, true);
	}

	// Quite a long bit of code
	private static Object handleGradebookLTI13(Site site,  Long tool_id, Map<String, Object> content, String user_id,
			String assignment, Long scoreGiven, Long maxPoints, String comment, boolean isRead, boolean isDelete) {

		// If we are not supposed to lookup or set the grade, we are done
		if (isRead == false && isDelete == false && scoreGiven == null) {
			return new Boolean(false);
		}

		String siteId = site.getId();

		// Look up the assignment so we can find the max points
		GradebookService g = (GradebookService) ComponentManager
				.get("org.sakaiproject.service.gradebook.GradebookService");

		Assignment assignmentObject = getAssignment(site, user_id, assignment, maxPoints);
		if (assignmentObject == null) {
			log.warn("assignmentObject or Id is null, cannot proceed with grading for site {}, assignment {}", siteId, assignment);
			return "Grade failure siteId=" + siteId;
		}

		// Now read, set, or delete the grade...
		Session sess = SessionManager.getCurrentSession();
		String message = null;
		Map<String, Object> retMap = new TreeMap<>();
		Object retval;

		try {
			// Indicate "who" is setting this grade - needs to be a real user account
			String gb_user_id = ServerConfigurationService.getString(
					"basiclti.outcomes.userid", "admin");
			String gb_user_eid = ServerConfigurationService.getString(
					"basiclti.outcomes.usereid", gb_user_id);
			sess.setUserId(gb_user_id);
			sess.setUserEid(gb_user_eid);
			if (isRead) {
				String actualGrade = g.getAssignmentScoreString(siteId, assignmentObject.getId(), user_id);
				Double dGrade = null;
				if (actualGrade != null && actualGrade.length() > 0) {
					dGrade = new Double(actualGrade);
					dGrade = dGrade / assignmentObject.getPoints();
				}
				CommentDefinition commentDef = g.getAssignmentScoreComment(siteId, assignmentObject.getId(), user_id);
				message = "Result read";
				retMap.put("grade", dGrade);
				if (commentDef != null) {
					retMap.put("comment", commentDef.getCommentText());
				}
				retval = retMap;
			} else if (isDelete) {
				g.setAssignmentScoreString(siteId, assignmentObject.getId(), user_id, null, "External Outcome");
				log.info("Delete Score site={} assignment={} user_id={}", siteId, assignment, user_id);
				message = "Result deleted";
				retval = Boolean.TRUE;
			} else {
				g.setAssignmentScoreString(siteId, assignmentObject.getId(), user_id, scoreGiven.toString(), "External Outcome");
				g.setAssignmentScoreComment(siteId, assignmentObject.getId(), user_id, comment);

				log.info("Stored Score={} assignment={} user_id={} score={}", siteId, assignment, user_id, scoreGiven);
				message = "Result replaced";
				retval = Boolean.TRUE;
			}
		} catch (NumberFormatException | AssessmentNotFoundException | GradebookNotFoundException e) {
			retval = "Grade failure " + e.getMessage() + " siteId=" + siteId;
			log.warn("handleGradebook Grade failure in site: {}, error: {}", siteId, e);
		} finally {
			sess.invalidate(); // Make sure to leave no traces
		}

		return retval;
	}

	public static Assignment getAssignment(Site site, String userId, String assignment, Long scoreMaximum) {
		// Look up the assignment so we can find the max points
		GradebookService g = (GradebookService) ComponentManager
				.get("org.sakaiproject.service.gradebook.GradebookService");

		String siteId = site.getId();
		if (scoreMaximum == null) {
			scoreMaximum = 100L;
		}

		Assignment assignmentObject = null;

		pushAdvisor();
		try {
			List gradebookAssignments = g.getAssignments(siteId);
			for (Iterator i = gradebookAssignments.iterator(); i.hasNext();) {
				Assignment gAssignment = (Assignment) i.next();
				if (gAssignment.isExternallyMaintained()) {
					continue;
				}
				if (assignment.equals(gAssignment.getName())) {
					assignmentObject = gAssignment;
					break;
				}
			}
		} catch (GradebookNotFoundException e) {
			assignmentObject = null; // Just to make double sure
		} finally {
			popAdvisor();
		}

		// Attempt to add assignment to grade book
		if (assignmentObject == null && g.isGradebookDefined(siteId)) {
			pushAdvisor();
			try {
				assignmentObject = new Assignment();
				assignmentObject.setPoints(Double.valueOf(scoreMaximum));
				assignmentObject.setExternallyMaintained(false);
				assignmentObject.setName(assignment);
				assignmentObject.setReleased(true);
				assignmentObject.setUngraded(false);
				Long assignmentId = g.addAssignment(siteId, assignmentObject);
				assignmentObject.setId(assignmentId);
				log.info("Added assignment: {} with Id: {}", assignment, assignmentId);
			} catch (ConflictingAssignmentNameException e) {
				log.warn("ConflictingAssignmentNameException while adding assignment {}", e.getMessage());
				assignmentObject = null; // Just to make sure
			} catch (Exception e) {
				log.warn("GradebookNotFoundException (may be because GradeBook has not yet been added to the Site) {}", e.getMessage());
				assignmentObject = null; // Just to make double sure
			} finally {
				popAdvisor();
			}
		}

		if (assignmentObject == null || assignmentObject.getId() == null) {
			log.warn("assignmentObject or Id is null.");
			assignmentObject = null;
		}

		return assignmentObject;
	}

	// Returns theGrade * points rounded to 2 digits (as a String)
	// Used for testing and to avoid precision problems
	public static String getRoundedGrade(Double theGrade, Double points) throws Exception {
		if (theGrade < 0.0 || theGrade > 1.0) {
			throw new Exception("Grade out of range");
		}
		theGrade = theGrade * points;
		theGrade = Precision.round(theGrade, 2);
		return String.valueOf(theGrade);
	}

	// Extract the necessary properties from a placement
	public static Properties getPropertiesFromPlacement(String placement_id, LTIService ltiService) {
		// These are the fields from a placement - they are not an exact match
		// for the fields in tool/content
		String[] fieldList = {"key", LTIService.LTI_SECRET, LTIService.LTI_PLACEMENTSECRET,
			LTIService.LTI_OLDPLACEMENTSECRET, LTIService.LTI_ALLOWSETTINGS,
			"assignment", LTIService.LTI_ALLOWROSTER, "releasename", "releaseemail",
			"toolsetting"};

		Properties retval = new Properties();

		String siteId;
		if (isPlacement(placement_id)) {
			ToolConfiguration placement;
			Properties config;
			try {
				placement = SiteService.findTool(placement_id);
				config = placement.getConfig();
				siteId = placement.getSiteId();
			} catch (Exception e) {
				log.debug("Error getPropertiesFromPlacement: {}, error: {}", e.getLocalizedMessage(), e);
				return null;
			}
			retval.setProperty("placementId", placement_id);
			retval.setProperty(LTIService.LTI_SITE_ID, siteId);
			for (String field : fieldList) {
				String value = toNull(getCorrectProperty(config, field, placement));
				if (field.equals("toolsetting")) {
					value = config.getProperty("toolsetting", null);
					field = LTIService.LTI_SETTINGS;
				}
				if (value == null) {
					continue;
				}
				if (field.equals("releasename")) {
					field = LTIService.LTI_SENDNAME;
				}
				if (field.equals("releaseemail")) {
					field = LTIService.LTI_SENDEMAILADDR;
				}
				if (field.equals("key")) {
					field = LTIService.LTI_CONSUMERKEY;
				}
				retval.setProperty(field, value);
			}
		} else { // Get information from content item
			Map<String, Object> content;
			Map<String, Object> tool;

			String contentStr = placement_id.substring(8);
			Long contentKey = getLongKey(contentStr);
			if (contentKey < 0) {
				return null;
			}

			// Leave off the siteId - bypass all checking - because we need to
			// finde the siteId from the content item
			content = ltiService.getContentDao(contentKey);
			if (content == null) {
				return null;
			}
			siteId = (String) content.get(LTIService.LTI_SITE_ID);
			if (siteId == null) {
				return null;
			}

			retval.setProperty("contentKey", contentStr);
			retval.setProperty(LTIService.LTI_SITE_ID, siteId);

			Long toolKey = getLongKey(content.get(LTIService.LTI_TOOL_ID));
			if (toolKey < 0) {
				return null;
			}
			tool = ltiService.getToolDao(toolKey, siteId);
			if (tool == null) {
				return null;
			}

			// Adjust the content items based on the tool items
			ltiService.filterContent(content, tool);

			for (String formInput : LTIService.TOOL_MODEL) {
				Properties info = parseFormString(formInput);
				String field = info.getProperty("field", null);
				String type = info.getProperty("type", null);
				Object o = tool.get(field);
				if (o instanceof String) {
					retval.setProperty(field, (String) o);
					continue;
				}
				if ("checkbox".equals(type)) {
					int check = getInt(o);
					if (check == 1) {
						retval.setProperty(field, "on");
					} else {
						retval.setProperty(field, "off");
					}
				}
			}

			for (String formInput : LTIService.CONTENT_MODEL) {
				Properties info = parseFormString(formInput);
				String field = info.getProperty("field", null);
				String type = info.getProperty("type", null);
				Object o = content.get(field);
				if (o instanceof String) {
					retval.setProperty(field, (String) o);
					continue;
				}
				if ("checkbox".equals(type)) {
					int check = getInt(o);
					if (check == 1) {
						retval.setProperty(field, "on");
					} else {
						retval.setProperty(field, "off");
					}
				}
			}
			retval.setProperty("assignment", (String) content.get("title"));
		}
		return retval;
	}

	public static boolean isPlacement(String placement_id) {
		if (placement_id == null) {
			return false;
		}
		return !(placement_id.startsWith("content:") && placement_id.length() > 8);
	}

	// Since ServerConfigurationService.getServerUrl() is wonky because it sometimes looks
	// at request.getServerName() instead of the serverUrl property we have our own
	// priority to determine our current url.
	// BLTI-273
	public static String getOurServerUrl() {
		String ourUrl = ServerConfigurationService.getString("sakai.lti.serverUrl");
		if (ourUrl == null || ourUrl.equals("")) {
			ourUrl = ServerConfigurationService.getString("serverUrl");
		}
		if (ourUrl == null || ourUrl.equals("")) {
			ourUrl = ServerConfigurationService.getServerUrl();
		}
		if (ourUrl == null || ourUrl.equals("")) {
			ourUrl = "http://127.0.0.1:8080";
		}

		if (ourUrl.endsWith("/") && ourUrl.length() > 2) {
			ourUrl = ourUrl.substring(0, ourUrl.length() - 1);
		}

		return ourUrl;
	}

	public static String getOurServletPath(HttpServletRequest request) {
		String URLstr = request.getRequestURL().toString();
		String retval = URLstr.replaceFirst("^https??://[^/]*", getOurServerUrl());
		return retval;
	}

	public static String toNull(String str) {
		if (str == null) {
			return null;
		}
		if (str.trim().length() < 1) {
			return null;
		}
		return str;
	}

	// Pull in a few things to avoid circular dependency
	public static int getInt(Object o) {
		if (o instanceof String) {
			try {
				return (new Integer((String) o)).intValue();
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		if (o instanceof Number) {
			return ((Number) o).intValue();
		}
		return -1;
	}

	public static String[] positional = {"field", "type"};

	public static Properties parseFormString(String str) {
		Properties op = new Properties();
		String[] pairs = str.split(":");
		int i = 0;
		for (String s : pairs) {
			String[] kv = s.split("=");
			if (kv.length == 2) {
				op.setProperty(kv[0], kv[1]);
			} else if (kv.length == 1 && i < positional.length) {
				op.setProperty(positional[i++], kv[0]);
			} else {
				// TODO : Logger something here
			}
		}
		return op;
	}

	public static Long getLongKey(Object key) {
		return getLong(key);
	}

	public static Long getLong(Object key) {
		Long retval = getLongNull(key);
		if (retval != null) {
			return retval;
		}
		return new Long(-1);
	}

	public static Long getLongNull(Object key) {
		if (key == null) {
			return null;
		}
		if (key instanceof Number) {
			return new Long(((Number) key).longValue());
		}
		if (key instanceof String) {
			if (((String) key).length() < 1) {
				return new Long(-1);
			}
			try {
				return new Long((String) key);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Setup a security advisor.
	 */
	public static void pushAdvisor() {
		// setup a security advisor
		SecurityService.pushAdvisor(new SecurityAdvisor() {
			public SecurityAdvice isAllowed(String userId, String function,
					String reference) {
				return SecurityAdvice.ALLOWED;
			}
		});
	}

	/**
	 * Remove our security advisor.
	 */
	public static void popAdvisor() {
		SecurityService.popAdvisor();
	}

	/**
	 * Converts a string from a comma-separated list of role maps to a
	 * Map<String, String>. Each role mapping in the string should be of the
	 * form sakairole1:ltirole1,sakairole2:ltirole2
	 * or sakairole4:ltirole4,ltirole5;sakairole6:ltirole6
	 * Using semicolon as the delimiter allows you to indicate more than one IMS role.
	 */
	public static Map<String, String> convertRoleMapPropToMap(String roleMapProp) {
		Map<String, String> roleMap = new HashMap<>();
		if (roleMapProp == null) {
			return roleMap;
		}

		String delim = ",";
		if( roleMapProp.contains(";") ) delim = ";";
		String[] roleMapPairs = roleMapProp.split(delim);
		for (String s : roleMapPairs) {
			String[] roleMapPair = s.split(":");
			if (roleMapPair.length != 2) {
				throw new IllegalArgumentException("Malformed rolemap property. Value must be a comma or semicolon-separated list of values of the form maintain:Learner");
			}
			roleMap.put(roleMapPair[0].trim(), roleMapPair[1].trim());
		}
		return roleMap;
	}

	/**
	 * Check the strict version we are complying with because of strange
	 * anomalies of the certification tests as they evolve over time. The
	 * biggest problem is that as the LTI 2.1 certification tests are developed,
	 * some 2.1 features sneak back into later versions of the LTI 2.0
	 * certification tests making it impossible. to comply with multiple
	 * versions at the same time. It is 100% sad that this is necessary.
	 */
	public static boolean checkStrictVersion(String low, String high) {
		String sver = ServerConfigurationService.getString(LTI_STRICT_VERSION, LTI_STRICT_VERSION_DEFAULT);
		if (sver == null) {
			return false;
		}
		return (sver.compareTo(low) >= 0 && sver.compareTo(high) <= 0);
	}

	/**
	 * Add an enabledCapability if it does not exist
	 */
	public static void addEnabledCapability(JSONArray enabledCapabilities, String cap) {
		if (!enabledCapabilities.contains(cap)) {
			enabledCapabilities.add(cap);
		}
	}

}