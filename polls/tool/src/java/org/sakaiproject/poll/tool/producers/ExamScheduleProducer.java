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
public class ExamScheduleProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "examSchedule";

	
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

		UIOutput.make(tofill, "exam-html", null).decorate(new UIFreeAttributeDecorator(langMap));
                
                UIMessage.make(tofill,"exam-schedule-title","exam_schedule_title");
		
                UIForm newForm = UIForm.make(tofill, "exam-form");
                
                String[] arrProgramStudi = new String[]{"D3","D4"};
		UISelect programStudi = UISelect.make(newForm,"examProdiOptions",arrProgramStudi,"#{poll.minOptions}",Integer.toString(0));
                
                String[] arrSemester = new String[]{"1","2","3","4","5","6","7","8"};
		UISelect semester = UISelect.make(newForm,"examSemesterOptions",arrSemester,"#{poll.minOptions}",Integer.toString(0));
                
                String[] arrTahunAkademik = new String[]{"2017/2018","2018/2019","2019/2020"};
		UISelect tahunAkademik = UISelect.make(newForm,"examTahunOptions",arrTahunAkademik,"#{poll.minOptions}",Integer.toString(0));
                
//                UICommand.make(newForm, "search-schedule", UIMessage.make("search"), " ");
                
                
                UILink schDay = UILink.make(tofill,"exam-day-title",messageLocator.getMessage("sch_day_title"), "#");
//                schDay.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_day_title_tooltip")));
                
                UILink schTime = UILink.make(tofill,"exam-time-title",messageLocator.getMessage("sch_time_title"), "#");
//                schTime.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_time_title_tooltip")));
                
                UILink schMK = UILink.make(tofill,"exam-mk-title",messageLocator.getMessage("sch_mk_title"), "#");
//                schMK.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_mk_title_tooltip")));
                UILink schType = UILink.make(tofill,"exam-type-title",messageLocator.getMessage("sch_type_title"), "#");
//                schType.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_type_title_tooltip")));

                UILink schDosen = UILink.make(tofill,"exam-dosen-title",messageLocator.getMessage("sch_dosen_title"), "#");
//                schDosen.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_dosen_title_tooltip")));
                UILink schKode = UILink.make(tofill,"exam-kode-ruangan-title",messageLocator.getMessage("sch_kode_title"), "#");
//                schKode.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_kode_title_tooltip")));
                
                UILink schRombel = UILink.make(tofill,"exam-rombel-title",messageLocator.getMessage("sch_rombel_title"), "#");
//                schRombel.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_rombel_title_tooltip")));
                
                UILink schPengawas = UILink.make(tofill,"exam-pengawas-title",messageLocator.getMessage("sch_pengawas_title"), "#");
//                schPengawas.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_pengawas_title_tooltip")));
		
		List<Object[]>jadwalUjian;
		jadwalUjian = pollVoteManager.getJadwalUjian("D3","3","EAS");

		System.out.println("#A"+jadwalUjian.isEmpty());
                System.out.println("#A"+jadwalUjian.toString());

		for (Iterator <Object[]> iterator=jadwalUjian.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    String hari = (String)list[0];
                    String jam_mulai = (String)list[1];	
                    String jam_berakhir = (String)list[2];
                    String waktu = jam_mulai + " - " + jam_berakhir;
                    String nama_matkul = (String)list[3];
                    boolean isTeori_ = (boolean) list[4];
                    String isTeori = "";    
                        if(isTeori_)
                            {isTeori = "TE";}
                        else
                            {isTeori = "PR";}
                    String nama_dosen = (String)list[5];	
                    String kd_ruangan = (String)list[6];
                    String kd_rombel = (String)list[7];
	
                    // System.out.println("#ROSE >"+hari);
                    // System.out.println("#ROSE >"+waktu);
                    // System.out.println("#ROSE >"+kd_matkul);
                    System.out.println("#ROSE >"+nama_matkul);
                    System.out.println("#ROSE >"+kd_rombel);
                    // System.out.println("#ROSE >"+isTeori);
                    // System.out.println("#ROSE >"+kd_dosen);
                    // System.out.println("#ROSE >"+nama_dosen);
                    // System.out.println("#ROSE >"+kd_ruangan);
                   
                    UIBranchContainer schedulerow = UIBranchContainer.make(tofill, "exam-row:"); 
                   //Create a new <td> element 
                    UIOutput.make(schedulerow,"exam-day", hari); 
                    UIOutput.make(schedulerow,"exam-time", waktu); 
                    UIOutput.make(schedulerow,"exam-mk", nama_matkul); 
                    UIOutput.make(schedulerow,"exam-type", isTeori);
                    UIOutput.make(schedulerow,"exam-dosen", nama_dosen); 
                    UIOutput.make(schedulerow,"exam-pengawas", nama_dosen); 
                    UIOutput.make(schedulerow,"exam-kd-ruangan", kd_ruangan);
                    UIOutput.make(schedulerow,"exam-rombel", kd_rombel);

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
