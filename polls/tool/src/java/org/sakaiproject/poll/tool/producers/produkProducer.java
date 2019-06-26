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
//import org.sakaiproject.poll.model.Voter;
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
public class produkProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "produk";

	private PollToolBean pollToolBean;
	private PollListManager pollListManager;
	private PollVoteManager pollVoteManager;
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;

    public void setPollToolBean(PollToolBean pollToolBean) {
        this.pollToolBean = pollToolBean;
    }

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
            
            Poll poll = null;
           
            String locale = localegetter.get().toString();
            Map<String, String> langMap = new HashMap<String, String>();
            langMap.put("lang", locale);
            langMap.put("xml:lang", locale);

            UIOutput.make(tofill, "produk-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            UIMessage.make(tofill,"dataProduk-title","dataProduk_title");

//            UILink no = UILink.make(tofill,"no-title",messageLocator.getMessage("no_title"), "#");
            UILink kodeProduk = UILink.make(tofill,"kodeProduk-title",messageLocator.getMessage("kodeProduk_title"), "#");
            UILink jenisProduk = UILink.make(tofill,"jenisProduk-title",messageLocator.getMessage("jenisProduk_title"), "#");
            UILink namaProduk = UILink.make(tofill,"namaProduk-title",messageLocator.getMessage("namaProduk_title"), "#");
            UILink hargaProduk = UILink.make(tofill,"hargaProduk-title",messageLocator.getMessage("hargaProduk_title"), "#");
            UILink jumlahStok = UILink.make(tofill,"jumlahStok-title",messageLocator.getMessage("jumlahStok_title"), "#");

//            List<Object[]> listDosen;
//            listDosen = pollVoteManager.getListDosen();

//            for (Iterator <Object[]> iterator = listDosen.iterator(); iterator.hasNext();) {
//                Object[] e = iterator.next();
//
//                String kode= (String) e[0];
//                String nama = (String) e[1]; 
//                String matakul = (String) e[2]; 
//
//                UIBranchContainer row_listDosen = UIBranchContainer.make(tofill, "dosen-row:");
//                UIOutput.make(row_listDosen, "dosenList-kd", kode);
//                UIOutput.make(row_listDosen, "dosenList-nama", nama);
//                UIOutput.make(row_listDosen, "dosenList-matkul", matakul);
//
//            }
                
	}

	public List<NavigationCase> reportNavigationCases() {
		
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
//		togo.add(new NavigationCase("cancel", new PollViewParameters(ResultsProducer.VIEW_ID)));
//                togo.add(new NavigationCase("success", new SimpleViewParameters(ListDosenProducer.VIEW_ID)));
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
