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
import org.sakaiproject.poll.tool.params.PollToolBean;

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
	
        private PollToolBean pollToolBean;
	private PollListManager pollListManager;
	private PollVoteManager pollVoteManager;
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;

	public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}
        
        public void setPollToolBean(PollToolBean pollToolBean) {
            this.pollToolBean = pollToolBean;	
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
                Poll poll = null;
                
                String param_rombel = null; //A2017
                String param_program_studi = null ;
                String param_semester = null; //1,2,3,4
                String param_tahun_akademik = null; //0217/2018
                
                UIForm newForm = UIForm.make(tofill, "schedule-form");
                
                String[] arrRomble = new String[]{"A2017","B2017"};
		UISelect rombel = UISelect.make(newForm,"rombel",arrRomble,"#{pollToolBean.rombel}",Integer.toString(0));
                param_rombel=pollToolBean.getRombel();
                        
                String[] arrProgramStudi = new String[]{"D3","D4"};
		UISelect programStudi = UISelect.make(newForm,"program-studi",arrProgramStudi,"#{pollToolBean.program_studi}",Integer.toString(0));
                param_program_studi = pollToolBean.getProgram_studi();
                
                String[] arrSemester = new String[]{"Ganjil","Genap"};
		UISelect semester = UISelect.make(newForm,"semester",arrSemester,"#{pollToolBean.semester}",Integer.toString(0));
                param_semester = pollToolBean.getSemester();
                
                String[] arrTahunAkademik = new String[]{"2017/2018","2018/2019","2019/2020"};
		UISelect tahunAkademik = UISelect.make(newForm,"tahun-akademik",arrTahunAkademik,"#{pollToolBean.tahun_akademik}",Integer.toString(0));
                param_tahun_akademik = pollToolBean.getTahun_akademik();
                              
                UICommand.make(newForm, "search-schedule", UIMessage.make("search"), "#{pollToolBean.seacrhJadwalKuliah}");
                
                
                UILink schDay = UILink.make(tofill,"sch-day-title",messageLocator.getMessage("sch_day_title"), "#");
//                schDay.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_day_title_tooltip")));
                
                UILink schTime = UILink.make(tofill,"sch-time-title",messageLocator.getMessage("sch_time_title"), "#");
//                schTime.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_time_title_tooltip")));
                UILink schKdMK = UILink.make(tofill,"sch-kd-mk-title",messageLocator.getMessage("sch_kd_mk_title"), "#");
//                schKdMK.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_kd_mk_title_tooltip")));
                
                UILink schMK = UILink.make(tofill,"sch-mk-title",messageLocator.getMessage("sch_mk_title"), "#");
//                schMK.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_mk_title_tooltip")));
                UILink schType = UILink.make(tofill,"sch-type-title",messageLocator.getMessage("sch_type_title"), "#");
//                schType.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_type_title_tooltip")));
                UILink schKdDosen = UILink.make(tofill,"sch-kd-dosen-title",messageLocator.getMessage("sch_kd_dosen_title"), "#");
//                schKdDosen.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_kd_dosen_title_tooltip")));                
                UILink schDosen = UILink.make(tofill,"sch-dosen-title",messageLocator.getMessage("sch_dosen_title"), "#");
//                schDosen.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_dosen_title_tooltip")));
                UILink schKode = UILink.make(tofill,"sch-kode-ruangan-title",messageLocator.getMessage("sch_kode_title"), "#");
//                schKode.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("sch_kode_title_tooltip")));

		List<Object[]>jadwalKuliah;
                if(param_rombel != null || param_program_studi != null || param_semester != null || param_tahun_akademik != null) {
                    jadwalKuliah = pollVoteManager.getJadwalKuliah(param_rombel,param_program_studi,param_tahun_akademik,param_semester);
                    System.out.println("#herefromform");
                    System.out.println(param_rombel+"-"+param_program_studi+"-"+param_semester+"-"+param_tahun_akademik);
                } else{ //ini query default
                    System.out.println("#herequerydefault");
                    System.out.println(param_rombel+"-"+param_program_studi+"-"+param_semester+"-"+param_tahun_akademik);
                    jadwalKuliah = pollVoteManager.getJadwalKuliah("B2017","D3","2018/2019","Ganjil");
                }
                
//		System.out.println("#A"+jadwalKuliah.isEmpty());
//                System.out.println("#A"+jadwalKuliah.toString());

		for (Iterator <Object[]> iterator=jadwalKuliah.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    String hari = (String)list[0];
                    String jam_mulai = (String)list[1];	
                    String jam_berakhir = (String)list[2];
                    String waktu = jam_mulai + " - " + jam_berakhir;
                    String kd_matkul = (String)list[3];
                    String nama_matkul = (String)list[4];
                    boolean isTeori_ = (boolean) list[5];
                    String isTeori = "";    
                        if(isTeori_)
                            {isTeori = "TE";}
                        else
                            {isTeori = "PR";}
                    String kd_dosen = (String)list[6];
                    String nama_dosen = (String)list[7];	
                    String kd_ruangan = (String)list[8];
	
                    // System.out.println("#ROSE >"+hari);
                    // System.out.println("#ROSE >"+waktu);
                    // System.out.println("#ROSE >"+kd_matkul);
//                    System.out.println("#ROSE >"+nama_matkul);
                    // System.out.println("#ROSE >"+isTeori);
                    // System.out.println("#ROSE >"+kd_dosen);
                    // System.out.println("#ROSE >"+nama_dosen);
                    // System.out.println("#ROSE >"+kd_ruangan);
                   
                    UIBranchContainer schedulerow = UIBranchContainer.make(tofill, "schedule-row:"); 
                   //Create a new <td> element 
                    UIOutput.make(schedulerow,"sch-day", hari); 
                    UIOutput.make(schedulerow,"sch-time", waktu); 
                    UIOutput.make(schedulerow,"sch-kd-mk", kd_matkul); 
                    UIOutput.make(schedulerow,"sch-mk", nama_matkul); 
                    UIOutput.make(schedulerow,"sch-type", isTeori);
                    UIOutput.make(schedulerow,"sch-kd-dosen", kd_dosen);
                    UIOutput.make(schedulerow,"sch-dosen", nama_dosen); 
                    UIOutput.make(schedulerow,"sch-kd-ruangan", kd_ruangan); 

                } 
            } 

	

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("cancel", new PollViewParameters(ResultsProducer.VIEW_ID)));
                togo.add(new NavigationCase("success", new SimpleViewParameters(ScheduleProducer.VIEW_ID)));
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
