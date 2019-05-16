package org.sakaiproject.poll.tool.producers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
//import org.sakaiproject.poll.model.Mahasiswa;
import org.sakaiproject.poll.model.glossary;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.tool.params.OptionViewParameters;
import org.sakaiproject.poll.tool.params.PollViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;
//import static org.sakaiproject.poll.tool.producers.InputGlossaryProducer.VIEW_ID;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
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
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.UISelectChoice;
import uk.org.ponder.rsf.components.UIVerbatim;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UILabelTargetDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.stringutil.StringList;

@Slf4j
public class glossaryProducer implements ViewComponentProducer,
DefaultView,NavigationCaseReporter {
	public static final String VIEW_ID = "glossary";
	
	private PollListManager pollListManager;
	
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;
	private PollVoteManager pollVoteManager;  

	private static final String NAVIGATE_ADD = "actions-add";
	private static final String NAVIGATE_PERMISSIONS = "actions-permissions";
	private static final String NAVIGATE_MAHASISWA = "actions-mahasiswa"; //modif
	private static final String NAVIGATE_GLOSSARY = "actions-glossary"; //modif
	private static final String NAVIGATE_VOTE = "glossary-vote";

	public String getViewID() {
		return VIEW_ID;
	}

        private ExternalLogic externalLogic;    
        public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
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

	private VoteBean voteBean;
	public void setVoteBean(VoteBean vb){
		this.voteBean = vb;
	}

	private TargettedMessageList tml;
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.tml = tml;
	}

	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
		
		voteBean.setPoll(null);
		voteBean.voteCollection = null;

		String locale = localegetter.get().toString();
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "glossary-html", null).decorate(new UIFreeAttributeDecorator(langMap));
			
		UIOutput.make(tofill, "glossary-list-title", messageLocator.getMessage("glossary_list_title"));	
		
		boolean renderDelete = false;
                
		//populate the action links
                UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));

                UIInternalLink.make(actions,NAVIGATE_ADD,UIMessage.make("action_add_poll"),
                                        new PollViewParameters(AddPollProducer.VIEW_ID, "New 0"));
                UIInternalLink.make(actions, NAVIGATE_PERMISSIONS, UIMessage.make("action_set_permissions"),new SimpleViewParameters(PermissionsProducer.VIEW_ID));

                //modif
//                UIInternalLink.make(actions, NAVIGATE_MAHASISWA, 
//                        UIMessage.make("action_set_mahasiswa"),
//                        new SimpleViewParameters(MahasiswaInputProducer.VIEW_ID));

                UIInternalLink.make(actions, NAVIGATE_GLOSSARY, 
                        UIMessage.make("action_set_glossary"),
                        new SimpleViewParameters(glossaryProducer.VIEW_ID));

                
//		if(glossaries.isEmpty()) UIOutput.make(tofill, "no-glossary", messageLocator.getMessage("glossary_list_empty"));
//		else{
                    // fix for broken en_ZA locale in JRE http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6488119
                    Locale M_locale = null;
                    String langLoc[] = localegetter.get().toString().split("_");
                    if ( langLoc.length >= 2 ) {
                            if ("en".equals(langLoc[0]) && "ZA".equals(langLoc[1]))
                                    M_locale = new Locale("en", "GB");
                            else
                                    M_locale = new Locale(langLoc[0], langLoc[1]);
                    } else{
                            M_locale = new Locale(langLoc[0]);
                    }

                    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                                    DateFormat.SHORT, M_locale);
                    TimeZone tz = externalLogic.getLocalTimeZone();
                    df.setTimeZone(tz);
                    //m_log.debug("got timezone: " + tz.getDisplayName());


                    UILink id = UILink.make(tofill,"glossary-id-title",messageLocator.getMessage("glossary_id_title"), "#");
                    id.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("glossary_id_title_tooltip")));
                    UILink term = UILink.make(tofill,"glossary-term-title",messageLocator.getMessage("glossary_term_title"), "#");
                    term.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("glossary_term_title_tooltip")));
                    UILink description = UILink.make(tofill,"glossary-description-title",messageLocator.getMessage("glossary_description_title"), "#");
                    description.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("glossary_description_title_tooltip")));
                    UILink category = UILink.make(tofill,"glossary-category-title",messageLocator.getMessage("glossary_category_title"), "#");
                    category.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("glossary_category_title_tooltip")));


                List<glossary> glossaries;
        	glossaries = pollVoteManager.getAllGlossary();

                    for (glossary Glossary : glossaries){
                        
                        UIBranchContainer glossaryrow = UIBranchContainer.make(tofill, "glossary-row:");
	//Create a new <td> element
                        UIOutput.make(glossaryrow,"glossary-id", Long.toString(Glossary.getId()));
                        UIOutput.make(glossaryrow,"glossary-term", Glossary.getTerm());
                        UIOutput.make(glossaryrow,"glossary-description", Glossary.getDescription());
                        UIOutput.make(glossaryrow,"glossary-category", Glossary.getCategory());
                    }
		
//                }
                
    }

    @Override
    public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("added", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		togo.add(new NavigationCase("option", new OptionViewParameters(PollOptionProducer.VIEW_ID, null, null)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;        
    }
}