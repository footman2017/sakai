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
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Glossary;
import org.sakaiproject.poll.model.Option;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.model.Vote;
import org.sakaiproject.poll.tool.params.OptionViewParameters;
import org.sakaiproject.poll.tool.params.PollViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;
import org.sakaiproject.poll.tool.params.GlossaryBean;
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
public class glossaryProducer implements ViewComponentProducer,
        NavigationCaseReporter, ViewParamsReporter, 
        ActionResultInterceptor {
	public static final String VIEW_ID = "glossaryAddView";

	private PollListManager pollListManager;
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private Glossary glossary;

	public String getViewID() {
		return VIEW_ID;
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
        
        private GlossaryBean glossaryBean;
	public void setGlossaryBean(GlossaryBean gb){
		this.glossaryBean = gb;
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



		String currentuserid = externalLogic.getCurrentUserId();

		PollViewParameters ecvp = (PollViewParameters) viewparams;
		Poll poll = null;
		boolean isNew = true;

		String locale = localeGetter.get().toString();
        Map<String, String> langMap = new HashMap<String, String>();
        langMap.put("lang", locale);
        langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "glossary-html", null).decorate(new UIFreeAttributeDecorator(langMap));


		

		UIForm newMahasiswa = UIForm.make(tofill, "add-glossary-form");

                UIMessage.make(tofill,"mahasiswa_new_title","mahasiswa_new_title");
                //build an empty mahasiswa
                log.debug("this is a new mahasiswa");
                glossary = new Glossary();
                
		UIMessage.make(tofill, "id-glossary-label", "id_glossary_label");
		UIMessage.make(tofill, "term-glossary-label", "term_glossary_label");
                UIMessage.make(tofill, "description-glossary-label", "description_glossary_label");
                UIMessage.make(tofill, "category-glossary-label", "category_glossary_label");
                
                //the form fields
		UIInput.make(newMahasiswa, "term-glossary-text", "#{glossary.term}");
		UIInput.make(newMahasiswa, "description-glossary-text", "#{glossary.description}");
                UIInput.make(newMahasiswa, "category-glossary-text", "#{glossary.category}");
                String[] category = new String[]{"Komputer","Pemrograman","Perangkat Lunak","Hacking","Design"};
                UISelect cat = UISelect.make(newMahasiswa,"category",category,"#{glossary.category}",glossary.getCategory());

        	List<Glossary> listGlossary;
        	listGlossary = pollVoteManager.getAllGlossary();

        	for (Glossary item : listGlossary){
            //Create a new <li> element
	            UIBranchContainer row = UIBranchContainer.make(tofill, "list-glossary:");
	            UIOutput.make(row, "term_glossary", item.getTerm());
	            UIOutput.make(row, "description_glossary", item.getDescription());
                    UIOutput.make(row, "category_glossary", item.getCategory());
        	}
                
                List<CollatedGlossary> collation = new ArrayList<CollatedGlossary>();
                
                for (int i=0; i <listGlossary.size(); i++ ) {    
                        CollatedGlossary collatedGlossary = new CollatedGlossary();
                        
			collatedGlossary.setId(Long.toString(listGlossary.get(i).getId()));
                        collatedGlossary.setTerm(listGlossary.get(i).getTerm());
                        collatedGlossary.setDescription(listGlossary.get(i).getDescription());
                        collatedGlossary.setCategory(listGlossary.get(i).getCategory());
                        
                        collation.add(collatedGlossary);
		}
                
                UILink idt = UILink.make(tofill,"answers-count",messageLocator.getMessage("results_glossary_id"), "#");
                UILink termt = UILink.make(tofill,"answers-term",messageLocator.getMessage("results_glossary_term"), "#");
                UILink desct = UILink.make(tofill,"answers-voter",messageLocator.getMessage("results_glossary_desc"), "#");
                UILink catt = UILink.make(tofill,"answers-title",messageLocator.getMessage("results_glossary_cat"), "#");
                
                for (int i=0; i <listGlossary.size(); i++ ) {
			UIBranchContainer resultVoterRow = UIBranchContainer.make(tofill,"answer-row-voter:",collation.get(i).getId().toString());

			UIVerbatim.make(resultVoterRow,"answer-option-voter",collation.get(i).getTerm());
			UIOutput.make(resultVoterRow,"answer-count-voter", Integer.valueOf(i+1).toString());
			UIOutput.make(resultVoterRow,"answer-optionVote",collation.get(i).getDescription());
                        UIOutput.make(resultVoterRow,"answer-optionCategory",collation.get(i).getCategory());
		}
                


			UICommand.make(newMahasiswa, "submit-new-glossary", UIMessage.make("new_poll_submit"),
			"#{pollToolBean.processActionAddGlossary}");

//		UICommand cancel = UICommand.make(newMahasiswa, "cancel",UIMessage.make("new_poll_cancel"),"#{pollToolBean.cancel}");
//		cancel.parameters.add(new UIELBinding("#{voteCollection.submissionStatus}", "cancel"));
//		log.debug("Finished generating view");
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

	public void interceptActionResult(ARIResult result, ViewParameters incoming, Object actionReturn) {
		// OptionViewParameters outgoing = (OptionViewParameters) result.resultingView;
		// SAK-14726 : Start BugFix
		if (log.isDebugEnabled() && actionReturn != null) {
			log.debug("actionReturn is of type " + actionReturn.getClass());
		}

		if (actionReturn == null) {
			return;
		}
		
		Poll poll = null;
		
		if(actionReturn instanceof org.sakaiproject.poll.model.Poll) {
			poll = (Poll) actionReturn;
		}
		else {

			PollViewParameters ecvp = (PollViewParameters) incoming;

			if(null == ecvp || null == ecvp.id || "New 0".equals(ecvp.id)) {
				return;

			} else {

				poll = pollListManager.getPollById(Long.valueOf(ecvp.id));
			}
		}
		// SAK-14726 : End BugFix

		if (poll == null) {
			return;
		}

		log.debug("Action result got poll: " + poll.getPollId());
		log.debug("resulting view is: " + result.resultingView);

		if (poll.getPollOptions() == null || poll.getPollOptions().size() == 0) {
			result.resultingView = new OptionViewParameters(PollOptionProducer.VIEW_ID, null, poll.getPollId().toString());
		} else {
			result.resultingView = new SimpleViewParameters(PollToolProducer.VIEW_ID);
		}

		//if (poll != null && outgoing.id == null) {
		//  outgoing.id = poll.getId().toString();
		//}
	}
        
        private static class CollatedGlossary {
		private String Id;
                private String Term;
                private String Description;
                private String Category;
		
		public CollatedGlossary() {
			this.Term = "";
		}

                public String getId() {
                    return Id;
                }

                public void setId(String id) {
                    this.Id = id;
                }

                public String getTerm() {
                    return Term;
                }

                public void setTerm(String Term) {
                    this.Term = Term;
                }

                public String getDescription() {
                    return Description;
                }

                public void setDescription(String Description) {
                    this.Description = Description;
                }

                public String getCategory() {
                    return Category;
                }

                public void setCategory(String Category) {
                    this.Category = Category;
                }

	}

}


