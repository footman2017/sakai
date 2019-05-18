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
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
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
public class ScheduleProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "schedule";

	
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
                
                String locale = localegetter.get().toString();
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "schedule-html", null).decorate(new UIFreeAttributeDecorator(langMap));
                
                UIMessage.make(tofill,"schedule-title","schedule_title");
                
                UILink schTime = UILink.make(tofill,"sch-time-title",messageLocator.getMessage("sch_time_title"), "#");
                schTime.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_time_title_tooltip")));
                UILink schMK = UILink.make(tofill,"sch-mk-title",messageLocator.getMessage("sch_mk_title"), "#");
                schMK.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_mk_title_tooltip")));
                UILink schType = UILink.make(tofill,"sch-type-title",messageLocator.getMessage("sch_type_title"), "#");
                schType.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_type_title_tooltip")));
                UILink schDosen = UILink.make(tofill,"sch-dosen-title",messageLocator.getMessage("sch_dosen_title"), "#");
                schDosen.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_dosen_title_tooltip")));
                UILink schKode = UILink.make(tofill,"sch-kode-title",messageLocator.getMessage("sch_kode_title"), "#");
                schKode.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_kode_title_tooltip")));
		
		List<Object[]>jadwalKuliah;
		jadwalKuliah = pollVoteManager.getJadwalKuliah();

		System.out.println("#A"+jadwalKuliah.isEmpty());
                System.out.println("#A"+jadwalKuliah.toString());

		for (Iterator <Object[]> iterator=jadwalKuliah.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    String jam_mulai = (String)list[0];	
                    String kd_matkul = (String)list[1];
                    boolean isTeori_ = (boolean) list[2];
                    String isTeori = String.valueOf(isTeori_);
                    String kd_dosen = (String)list[3];	
                    String kd_ruangan = (String)list[4];	

                    System.out.println("#ROSE"+list[0]);
                    System.out.println("#ROSE"+list[1]);
                    System.out.println("#ROSE"+isTeori);
                    System.out.println("#ROSE"+list[3]);
                    System.out.println("#ROSE"+list[4]);
                   UIBranchContainer schedulerow = UIBranchContainer.make(tofill, "schedule-row:"); 
                   //Create a new <td> element 
                   UIOutput.make(schedulerow,"sch-time", jam_mulai); 
                   UIOutput.make(schedulerow,"sch-mk", kd_matkul); 
                   UIOutput.make(schedulerow,"sch-type", isTeori);
                   UIOutput.make(schedulerow,"sch-dosen", kd_dosen); 
                   UIOutput.make(schedulerow,"sch-kode", kd_ruangan); 
                } 
            } 

	

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("cancel", new PollViewParameters(ResultsProducer.VIEW_ID)));
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
