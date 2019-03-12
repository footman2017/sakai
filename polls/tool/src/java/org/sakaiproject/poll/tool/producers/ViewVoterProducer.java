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
		
		
		
		//get the number of votes
		int voters = pollVoteManager.getDisctinctVotersForPoll(poll);
		//Object[] args = new Object[] { Integer.valueOf(voters).toString()};
		if (poll.getMaxOptions()>1)
			UIOutput.make(tofill,"poll-size",messageLocator.getMessage("results_poll_size",Integer.valueOf(voters).toString()));

		log.debug(voters + " have voted on this poll");

//		UIOutput.make(tofill,"question",poll.getText());
//		log.debug("got poll " + poll.getText());
		List<Option> pollOptions = poll.getPollOptions();

		log.debug("got a list of " + pollOptions.size() + " options");
		//Append an option for no votes
		if (poll.getMinOptions()==0) {
			Option noVote = new Option(Long.valueOf(0));
			noVote.setOptionText(messageLocator.getMessage("voters_novote"));
			noVote.setPollId(poll.getPollId());
			pollOptions.add(noVote);
		}

		List<Vote> votes = pollVoteManager.getAllVotesForPoll(poll);
		int totalVotes= votes.size();
		log.debug("got " + totalVotes + " votes");
		List<CollatedVote> collation = new ArrayList<CollatedVote>();

		for (int i=0; i <pollOptions.size(); i++ ) {
			CollatedVote collatedVote = new CollatedVote();
			Option option = (Option) pollOptions.get(i);
			log.debug("collating option " + option.getOptionId());
			collatedVote.setoptionId(option.getOptionId());
			collatedVote.setOptionText(option.getOptionText());
			collatedVote.setDeleted(option.getDeleted());
			for (int q=0; q <votes.size(); q++ ) {
				Vote vote = (Vote)votes.get(q);
				if (vote.getPollOption().equals(option.getOptionId())){
					log.debug("got a vote for option " + option.getOptionId());
					collatedVote.incrementVotes();

				}

			}
			collation.add(collatedVote);

		}
                
                UILink count = UILink.make(tofill,"voters-name",messageLocator.getMessage("voters_name_title"), "#");
		count.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("voters_name_title_tooltip")));
                
		UILink title = UILink.make(tofill,"voters-answer",messageLocator.getMessage("voters_answer_title"), "#");
		title.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("voters_answer_title_tooltip")));

//		UILink avotes = UILink.make(tofill,"answers-votes",messageLocator.getMessage("results_answers_votes"), "#");
//		avotes.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("results_answers_votes_tooltip")));
//		UILink apercent = UILink.make(tofill,"answers-percent","%", "#");
//		apercent.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("results_answers_percent_tooltip")));
		
                UIBranchContainer adefault = UIBranchContainer.make(tofill,"answers-default:");
		adefault.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("results_answers_default_tooltip")));
		
		
		//output the votes
		Map<Long,String> chartTextData = new LinkedHashMap<Long,String>();
		Map<Long,String> chartValueData = new LinkedHashMap<Long,String>();
		NumberFormat nf = NumberFormat.getPercentInstance(localegetter.get());
		for (int i=0; i <collation.size(); i++ ) {
			CollatedVote cv = (CollatedVote)collation.get(i);
			UIBranchContainer resultRow = UIBranchContainer.make(tofill,"answer-row:",cv.getoptionId().toString());
			
			String optionText = cv.getOptionText();
			String userName = cv.getOptionText();
			if (cv.getDeleted()) {
				optionText += messageLocator.getMessage("deleted_option_tag_html");
			}

			UIVerbatim.make(resultRow,"voter-answer",optionText);
			UIOutput.make(resultRow,"voter-name",optionText);
			
			
//			log.debug("about to do the calc: (" + cv.getVotes()+"/"+ totalVotes +")*100");
//			double percent = (double)0;
//			if (totalVotes>0  && poll.getMaxOptions() == 1)
//				percent = ((double)cv.getVotes()/(double)totalVotes); //*(double)100;
//			else if (totalVotes>0  && poll.getMaxOptions() > 1)
//				percent = ((double)cv.getVotes()/(double)voters); //*(double)100;
//			else
//				percent = (double) 0;
			
			//setup chartdata, use percentages for the values
			//also, remove the &nbsp; from the beginning of the label, POLL-139
			//we use the same number formatter which adds a % to the end of the data, remove that as well.
			chartTextData.put(cv.getoptionId(), StringUtils.removeStart(optionText, "&nbsp;"));
//			chartValueData.put(cv.getoptionId(), StringUtils.removeEnd(nf.format(percent), "%"));

//			log.debug("result is "+ percent);
//			UIOutput.make(resultRow,"answer-percVotes", nf.format(percent));

		}
//		UIOutput.make(tofill,"votes-total",Integer.valueOf(totalVotes).toString());
//		if (totalVotes > 0 && poll.getMaxOptions() == 1)
//			UIOutput.make(tofill,"total-percent","100%");

		//the cancel button
		UIForm form = UIForm.make(tofill,"actform");
		UICommand cancel = UICommand.make(form,"back",messageLocator.getMessage("voters_cancel"),"#{pollToolBean.cancel}");
		cancel.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("voters_cancel_tooltip"))); 
		
		externalLogic.postEvent("poll.viewResult", "poll/site/" + externalLogic.getCurrentLocationId() +"/poll/" +  poll.getPollId(), false);


	}

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;
	}	
	public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}



	private static class CollatedVote {
		private Long optionId ;
		private String optionText;
		private int votes;
		private Boolean deleted;
		
		public CollatedVote() {
			this.votes=0;
		}
		public void setoptionId(Long val){
			this.optionId = val;
		}

		public Long getoptionId(){
			return this.optionId;
		}

		public void setOptionText(String t){
			this.optionText = t;
		}
		public String getOptionText(){
			return this.optionText;
		}

		public void setVotes(int i){
			this.votes = i;
		}
		public int getVotes(){
			return this.votes;
		}
		public void incrementVotes(){
			this.votes++;
		}
		public void setDeleted(Boolean deleted) {
			this.deleted = deleted;
		}
		public Boolean getDeleted() {
			return deleted;
		}

	}
}
