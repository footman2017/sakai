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
import uk.org.ponder.rsf.components.UIInput;
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
public class jenisProdukProducer implements ViewComponentProducer,NavigationCaseReporter,ViewParamsReporter {

	public static final String VIEW_ID = "Jenis-Produk";

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

            UIOutput.make(tofill, "jenis-produk-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            UIMessage.make(tofill,"input-jenis-produk-title","input_jenis_produk_title");
            UIForm newJenisProduk = UIForm.make(tofill, "input-jenis-produk-form");
            
            //ISI FORM MULAI DARI SINI
            UIMessage.make(tofill,"add-jenis-produk-label","add_jenis_produk_label");
            
            //Text Field
            UIInput.make(newJenisProduk, "new-jenis-produk-text", "#{pollToolBean.Nama_Jenis_Produk}");
            
            UICommand.make(newJenisProduk, "input-jenis-produk", UIMessage.make("input_jenis_produk"), "#{pollToolBean.processActionAddJenisProduk}");
            
            UIMessage.make(tofill,"data-jenis-produk-title","data_jenis_produk_title");

            UILink kodeJenisProduk = UILink.make(tofill,"kd-jenis-produk-title",messageLocator.getMessage("kd_jenis_produk_title"), "#");
            UILink namaJenisProduk = UILink.make(tofill,"nama-jenis-produk-title",messageLocator.getMessage("nama_jenis_produk_title"), "#");

            List<Object[]> dataJenisProduk;
            dataJenisProduk = pollVoteManager.getDataJenisProduk();
            
            for (Iterator <Object[]> iterator = dataJenisProduk.iterator(); iterator.hasNext();) {
                Object[] list = iterator.next();

                System.out.println("#ROSE"+list[1]);
                
                Integer kd_jenis = (Integer) list[0];
                String nama_jenis_produk = (String) list[1];

                UIBranchContainer row_dataJenisProduk = UIBranchContainer.make(tofill, "jenis-produk-row:");
                UIOutput.make(row_dataJenisProduk, "jenis-kode", kd_jenis.toString());
                UIOutput.make(row_dataJenisProduk, "jenis-nama", nama_jenis_produk);
                }
                
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
