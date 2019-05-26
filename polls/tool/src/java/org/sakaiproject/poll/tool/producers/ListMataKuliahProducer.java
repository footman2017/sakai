/**********************************************************************************
 * $URL: $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008, 2009 The Sakai Foundation
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

package org.sakaiproject.poll.tool.producers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Option;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Vote;
import org.sakaiproject.poll.tool.params.OptionViewParameters;
import org.sakaiproject.poll.tool.params.PollViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;
import org.sakaiproject.poll.tool.params.PollToolBean;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.util.FormattedText;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UIOutputMany;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UISelectLabel;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.evolvers.FormatAwareDateInputEvolver;
import uk.org.ponder.rsf.evolvers.TextInputEvolver;
import uk.org.ponder.rsf.flow.ARIResult;
import uk.org.ponder.rsf.flow.ActionResultInterceptor;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

@Slf4j
public class ListMataKuliahProducer implements ViewComponentProducer,NavigationCaseReporter, ViewParamsReporter{
	public static final String VIEW_ID = "listMataKuliah";

	private PollListManager pollListManager;
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private PollToolBean pollToolBean;

	public String getViewID() {
		return VIEW_ID;
	}
        
        public void setPollToolBean(PollToolBean pollToolBean) {
        this.pollToolBean = pollToolBean;
        }
        
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setLocaleGetter(LocaleGetter localeGetter) {
		this.localeGetter = localeGetter;
	}

	public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}



	private VoteBean voteBean;
	public void setVoteBean(VoteBean vb){
		this.voteBean = vb;
	}

	private TextInputEvolver richTextEvolver;
	public void setRichTextEvolver(TextInputEvolver richTextEvolver) {
		this.richTextEvolver = richTextEvolver;
	}

	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}


	private ExternalLogic externalLogic;
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}

	private PollVoteManager pollVoteManager;



	public void setPollVoteManager(PollVoteManager pvm){
		this.pollVoteManager = pvm;
	}




	/*
	 * You can change the date input to accept time as well by uncommenting the lines like this:
	 * dateevolver.setStyle(FormatAwareDateInputEvolver.DATE_TIME_INPUT);
	 * and commenting out lines like this:
	 * dateevolver.setStyle(FormatAwareDateInputEvolver.DATE_INPUT);
	 * -AZ
	 */
	private FormatAwareDateInputEvolver dateevolver;
	public void setDateEvolver(FormatAwareDateInputEvolver dateevolver) {
		this.dateevolver = dateevolver;
	}



	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {



//		String currentuserid = externalLogic.getCurrentUserId();
//
//		PollViewParameters ecvp = (PollViewParameters) viewparams;
		Poll poll = null;
//		boolean isNew = true;

		String locale = localeGetter.get().toString();
        Map<String, String> langMap = new HashMap<String, String>();
        langMap.put("lang", locale);
        langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));


		

		
		UIForm newPoll = UIForm.make(tofill, "add-poll-form");

                
		//only display for exisiting polls
		

		UIMessage.make(tofill, "new-poll-descr", "new_poll_title");
		UIMessage.make(tofill, "new-poll-question-label", "new_poll_question_label");
		UIMessage pollDescr = UIMessage.make(tofill, "new-poll-descr-label", "new_poll_descr_label");
		UIMessage.make(tofill, "new-poll-descr-label2", "new_poll_descr_label2");

		//UIMessage.make(tofill, "new-poll-open-label", "new_poll_open_label");
		//UIMessage.make(tofill, "new-poll-close-label", "new_poll_close_label");

		UIMessage.make(tofill, "new-poll-limits", "new_poll_limits");
		//UIMessage pollMin = UIMessage.make(tofill, "new-poll-min-limits", "new_poll_min_limits");
		//UIMessage pollMax =  UIMessage.make(tofill, "new-poll-max-limits", "new_poll_max_limits");


		//the form fields
//		UIInput.make(newPoll, "new-poll-text", "#{poll.text}",poll.getText());
		
		
//		if (!externalLogic.isMobileBrowser())
//		{
//			// show WYSIWYG editor
//		UIInput itemDescr = UIInput.make(newPoll, "newpolldescr:", "#{poll.details}", poll.getDetails()); //$NON-NLS-1$ //$NON-NLS-2$
//		richTextEvolver.evolveTextInput(itemDescr);
//		UILabelTargetDecorator.targetLabel(pollDescr, itemDescr);
//		}
//		else
//		{
//			// do not show WYSIWYG editor in the mobile view
//			UIInput itemDescr = UIInput.make(newPoll, "newpolldescr_mobile", "#{poll.details}", poll.getDetails()); //$NON-NLS-1$ //$NON-NLS-2$
//			UILabelTargetDecorator.targetLabel(pollDescr, itemDescr);
//		}
//
//		UIInput voteOpen = UIInput.make(newPoll, "openDate-iso8601", "poll.voteOpenStr", poll.getVoteOpenStr());
//		UIInput voteClose = UIInput.make(newPoll, "closeDate-iso8601", "poll.voteCloseStr", poll.getVoteCloseStr());
//		//UILabelTargetDecorator.targetLabel(pollOpen, voteOpen);
		//UILabelTargetDecorator.targetLabel(pollClose, voteClose);

		/*
		 * access options
		 */
//		UIMessage pollAccessLabel = UIMessage.make(newPoll,"poll_access_label","new_poll_access_label");
//		UIBoundBoolean accessPublic = UIBoundBoolean.make(newPoll, "access-public", "poll.isPublic", poll.getIsPublic());
//		UIMessage newPollAccessPublicLabel = UIMessage.make(newPoll, "new_poll_access_public_label", "new_poll_access_public");
		
		// SAK-25399: Do not display the public access by default
//		if(!externalLogic.isShowPublicAccess()) {
//			newPoll.remove(pollAccessLabel);		
//			newPoll.remove(accessPublic);
//			newPoll.remove(newPollAccessPublicLabel);
//		}
		
	//	String[] minVotes = new String[]{"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
	//	String[] maxVotes = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
		
                String param_rombel = null; //A2017
                String param_program_studi = null ;
                
                String[] listRombel = new String[]{"B2017","B2014","B2015","B2016"};
		String[] listProgramstudi = new String[]{"D3","D4"};
                UISelect rombel = UISelect.make(newPoll,"rombel-list",listRombel,"#{pollToolBean.rombel}",Integer.toString(0));
		param_rombel=pollToolBean.getRombel();
                UISelect programstudi = UISelect.make(newPoll,"programstudi-list",listProgramstudi,"#{pollToolBean.program_studi}",Integer.toString(0));
                param_program_studi=pollToolBean.getProgram_studi();
                
                UICommand.make(newPoll, "search-matakuliah", UIMessage.make("search"), "#{pollToolBean.searchListMataKuliah}");
                
                UILink no = UILink.make(tofill,"matakuliah-no-title",messageLocator.getMessage("matakuliah_no_title"), "#");
                no.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("matakuliah_no_title_tooltip")));
                UILink kodemk = UILink.make(tofill,"matakuliah-kodemk-title",messageLocator.getMessage("matakuliah_kodemk_title"), "#");
                kodemk.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("matakuliah_kodemk_title_tooltip")));
                UILink namamk = UILink.make(tofill,"matakuliah-namamk-title",messageLocator.getMessage("matakuliah_namamk_title"), "#");
                namamk.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("matakuliah_namamk_title_tooltip")));
                UILink sks = UILink.make(tofill,"matakuliah-sks-title",messageLocator.getMessage("matakuliah_sks_title"), "#");
                sks.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("matakuliah_sks_title_tooltip")));
                   
                
                 
                List<Object[]> listMataKuliah;
//        	listMataKuliah = pollVoteManager.getListMataKuliah(listRombel[0], listProgramstudi[0]);
                if(param_rombel != null || param_program_studi != null ) {
                listMataKuliah = pollVoteManager.getListMataKuliah(param_rombel,param_program_studi);
                } else{ //ini query default
                    listMataKuliah = pollVoteManager.getListMataKuliah("B2017","D3");
                }
                for (Iterator <Object[]> iterator = listMataKuliah.iterator(); iterator.hasNext();){
                    
                 Object[] e = iterator.next();
                 
                 String kode= (String) e[0];
                 String nama = (String) e[1]; 
                 int jumsks = (int) e[2];
                 String jumsksstring = String.valueOf(jumsks); 
                 
                 UIBranchContainer matakuliahrow = UIBranchContainer.make(tofill, "matakuliah-row:");
                 UIOutput.make(matakuliahrow,"matakuliah-no","");
                 UIOutput.make(matakuliahrow,"matakuliah-kodemk", kode);
                 UIOutput.make(matakuliahrow,"matakuliah-namamk", nama);
                 UIOutput.make(matakuliahrow,"matakuliah-sks", jumsksstring);
                }
		/*
		 * 	open - can be viewd at any time
		 * 	never - not diplayed
		 * 	afterVoting - after user has voted
		 * 	afterClosing
		 *
		 */

		UICommand cancel = UICommand.make(newPoll, "cancel",UIMessage.make("new_poll_cancel"),"#{pollToolBean.cancel}");
		
	}


	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("added", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		togo.add(new NavigationCase("option", new OptionViewParameters(PollOptionProducer.VIEW_ID, null, null)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;
	}

	public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}

//	public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
//		// OptionViewParameters outgoing = (OptionViewParameters) result.resultingView;
//		// SAK-14726 : Start BugFix
//		if (log.isDebugEnabled() && actionReturn != null) {
//			log.debug("actionReturn is of type " + actionReturn.getClass());
//		}
//
//		if (actionReturn == null) {
//			return;
//		}
//		
//		Poll poll = null;
//		
//		if(actionReturn instanceof org.sakaiproject.poll.model.Poll) {
//			poll = (Poll) actionReturn;
//		}
//		else {
//
//			PollViewParameters ecvp = (PollViewParameters) incoming;
//
//			if(null == ecvp || null == ecvp.id || "New 0".equals(ecvp.id)) {
//				return;
//
//			} else {
//
//				poll = pollListManager.getPollById(Long.valueOf(ecvp.id));
//			}
//		}
//		// SAK-14726 : End BugFix
//
//		if (poll == null) {
//			return;
//		}
//
//		log.debug("Action result got poll: " + poll.getPollId());
//		log.debug("resulting view is: " + result.resultingView);
//
//		if (poll.getPollOptions() == null || poll.getPollOptions().size() == 0) {
//			result.resultingView = new OptionViewParameters(PollOptionProducer.VIEW_ID, null, poll.getPollId().toString());
//		} else {
//			result.resultingView = new SimpleViewParameters(PollToolProducer.VIEW_ID);
//		}
//
//		//if (poll != null && outgoing.id == null) {
//		//  outgoing.id = poll.getId().toString();
//		//}
//	}


}


