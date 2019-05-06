/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asus
 */

package org.sakaiproject.poll.tool.producers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Option;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Vote;
import org.sakaiproject.poll.model.Voter;
import org.sakaiproject.poll.tool.params.PollViewParameters;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessage;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UILink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;

@Slf4j
public class ViewVoterProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "voteViewVoters";

	
	private PollListManager pollListManager;
	private PollVoteManager pollVoteManager;
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;

	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}

	public void setMessageLocator(MessageLocator messageLocator) {

		this.messageLocator = messageLocator;
	}

	
	public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}

	
	public void setLocaleGetter(LocaleGetter localegetter) {
		this.localegetter = localegetter;
	}
	public void setPollVoteManager(PollVoteManager pvm){
		this.pollVoteManager = pvm;
	}


        private ExternalLogic externalLogic;    
        public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
    
	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}

	
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {

		PollViewParameters ecvp = (PollViewParameters) viewparams;

		String strId = ecvp.id;
		log.debug("got id of " + strId);
		Poll poll = pollListManager.getPollById(Long.valueOf(strId));

		if (!pollListManager.isAllowedViewResults(poll, externalLogic.getCurrentUserId())) {
			tml.addMessage(new TargettedMessage("poll.noviewresult", new Object[]{}, TargettedMessage.SEVERITY_ERROR));
			return;
			
		}

		String locale = localegetter.get().toString();
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
		
                int totalVoter = pollVoteManager.getVotersForPoll(poll);
                List<String> vfname = pollVoteManager.getVoterName(poll);
                List<String> vlname = pollVoteManager.getVoterLName(poll);
                List<String> vuserid = pollVoteManager.getVoterUserId(poll);
                List<String> voption = pollVoteManager.getVoterOption(poll);
                List<CollatedVoter> collationVoter = new ArrayList<CollatedVoter>();
                
                System.out.println("Ini adalah jumlah dari Voter : " + totalVoter);
                System.out.println("User Voter : " + vuserid);
                System.out.println("Nama Voter : " + vfname + " " + vlname);
                System.out.println("Option Voter : " + voption);
                
                //Untuk ke model
//                List<Voter> voters = new ArrayList<Voter>();
                
                List<CollatedVoter> collation = new ArrayList<CollatedVoter>();
                
                // Untuk ke model
//                for (int i=0; i <totalVoter; i++ ) {                         
//			voters.get(i).setUserFName(vfname.get(i));
//                        voters.get(i).setUserLName(vlname.get(i));
//                        voters.get(i).setUserName();
//                        voters.get(i).setUserId(vuserid.get(i));
//                        voters.get(i).setOptionText(voption.get(i));
//		}
                
                for (int i=0; i <totalVoter; i++ ) {    
                        CollatedVoter collatedVoter = new CollatedVoter();
                        
			collatedVoter.setVoterName(vfname.get(i).concat(" ").concat(vlname.get(i)));
                        collatedVoter.setUserId(vuserid.get(i));
                        collatedVoter.setOptionText(voption.get(i));
                        
                        collation.add(collatedVoter);
		}
                
                UILink vname = UILink.make(tofill,"answers-voter",messageLocator.getMessage("results_answers_voter"), "#");
		vname.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("results_answers_voter_tooltip")));

                // Untuk ke model
//                for (int i=0; i <totalVoter; i++ ) {
//			UIBranchContainer resultVoterRow = UIBranchContainer.make(tofill,"answer-row-voter:",voters.get(i).getUserId().toString());
//
//			UIVerbatim.make(resultVoterRow,"answer-option-voter",voters.get(i).getUserName());
//			UIOutput.make(resultVoterRow,"answer-count-voter", Integer.valueOf(i+1).toString());
//			UIOutput.make(resultVoterRow,"answer-optionVote",voters.get(i).getOptionText());
//		}
                
                for (int i=0; i <totalVoter; i++ ) {
			UIBranchContainer resultVoterRow = UIBranchContainer.make(tofill,"answer-row-voter:",collation.get(i).getUserId().toString());

			UIVerbatim.make(resultVoterRow,"answer-option-voter",collation.get(i).getVoterName());
			UIOutput.make(resultVoterRow,"answer-count-voter", Integer.valueOf(i+1).toString());
			UIOutput.make(resultVoterRow,"answer-optionVote",collation.get(i).getOptionText());
		}
                
		//the cancel button
		UIForm form = UIForm.make(tofill,"actform");
		UICommand cancel = UICommand.make(form,"back",messageLocator.getMessage("voters_cancel"),"#{pollToolBean.cancel}");
		cancel.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("voters_cancel_tooltip"))); 
		
		externalLogic.postEvent("poll.viewResult", "poll/site/" + externalLogic.getCurrentLocationId() +"/poll/" +  poll.getPollId(), false);

	}

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(ResultsProducer.VIEW_ID)));
		return togo;
	}	
	public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}

	private static class CollatedVoter {
		private String userId;
                private String voterName;
                private String optionText;
		
		public CollatedVoter() {
			this.voterName = "";
		}

		public void setOptionText(String t){
			this.optionText = t;
		}
		public String getOptionText(){
			return this.optionText;
		}

                public void setUserId(String t){
			this.userId = t;
		}
		public String getUserId(){
			return this.userId;
		}
                
		public void setVoterName(String name){
			this.voterName = name;
		}
		public String getVoterName(){
			return this.voterName;
		}

	}
}
