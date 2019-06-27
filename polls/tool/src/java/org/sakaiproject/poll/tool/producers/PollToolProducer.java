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

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.tool.params.PollViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;

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
public class PollToolProducer implements ViewComponentProducer,
DefaultView,NavigationCaseReporter {
	public static final String VIEW_ID = "votePolls";
	
	private PollListManager pollListManager;
	
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;
	private PollVoteManager pollVoteManager;  

	private static final String NAVIGATE_ADD = "actions-add";
	private static final String NAVIGATE_PERMISSIONS = "actions-permissions";
	private static final String NAVIGATE_VOTE = "poll-vote";

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

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
			
		UIOutput.make(tofill, "poll-list-title", messageLocator.getMessage("poll_list_title"));	
		
		boolean renderDelete = false;
		//populate the action links
		if (this.isAllowedPollAdd() || this.isSiteOwner() ) {
			UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));
			log.debug("this user has some admin functions");
			if (this.isAllowedPollAdd()) {
				log.debug("User can add polls");
				//UIOutput.make(tofill, "poll-add", messageLocator
				//       .getMessage("action_add_poll"));
//				UIInternalLink.make(actions,NAVIGATE_ADD,UIMessage.make("action_add_poll"),
//						new PollViewParameters(AddPollProducer.VIEW_ID, "New 0"));
//                                UIOutput.make(tofill, "poll-add", messageLocator
//				       .getMessage("action_add_poll"));
			} 
			if (this.isSiteOwner()) {
				UIInternalLink.make(actions, "actions-penjualan", UIMessage.make("action_set_penjualan"),new SimpleViewParameters(PollToolProducer.VIEW_ID));
                                UIInternalLink.make(actions, "actions-produk", UIMessage.make("action_set_produk"),new SimpleViewParameters(produkProducer.VIEW_ID));
                                UIInternalLink.make(actions, "actions-customer", UIMessage.make("action_set_customer"),new SimpleViewParameters(customerProducer.VIEW_ID));
                                UIInternalLink.make(actions, "actions-jenisproduk", UIMessage.make("action_set_jenisproduk"),new SimpleViewParameters(PollToolProducer.VIEW_ID));
			} 
		}
                
                UIOutput.make(tofill, "input-penjualan-title", messageLocator.getMessage("input_penjualan_title"));	

                UIForm newForm = UIForm.make(tofill, "input-penjualan-form");
                

                // Untuk dropdown nama get dari db
                List<Object[]>namaCust;
		namaCust = pollVoteManager.getNamaCustomer();
                int size = 0;
                for (Iterator <Object[]> iterator=namaCust.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    size++;               
                }
                String[] kodeCustomer = new String[size];
                String[] namaCustomer = new String[size];

		System.out.println("#B"+namaCust.isEmpty());
                System.out.println("#B"+namaCust.toString());
                int count=0;
		for (Iterator <Object[]> iterator=namaCust.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    namaCustomer[count] = (String)list[1];	
                    kodeCustomer[count] = (String)list[0].toString();  
                    count++;                    

                    System.out.println("#ROSE"+list[0]);
                    System.out.println("#ROSE"+list[1]);
                }                 
		UISelect namaInput = UISelect.make(newForm,"nama-penjual",kodeCustomer,namaCustomer,Integer.toString(0));
                
                UILink jenisiProdukLabel = UILink.make(tofill,"input-jenisproduk-title",messageLocator.getMessage("input_jenisproduk_title"), "#");
                String[] jenisProduk = new String[]{"Electronic","HomeAppliance"};
		UISelect jenisProdukInput = UISelect.make(newForm,"jenis-produk",jenisProduk,"#{pollToolBean.Kd_Jenis}",Integer.toString(0));

                UILink namaProdukLabel = UILink.make(tofill,"input-namaproduk-title",messageLocator.getMessage("input_namaproduk_title"), "#");
                String[] namaProduk = new String[]{"Asus Zenbook 15","Acer Predator 5","Macbook Pro 15"};
		UISelect namaProdukInput = UISelect.make(newForm,"nama-produk",namaProduk,"#{pollToolBean.Kd_Produk}",Integer.toString(0));
                
                UILink jumlahBarangLabel = UILink.make(tofill,"jumlah-barang-title",messageLocator.getMessage("input_jumlahbarang_title"), "#");
                String[] jumlahBarang = new String[]{"1","2","3"};
		UISelect jumlahBarangInput = UISelect.make(newForm,"jumlah-barang",jumlahBarang,"#{pollToolBean.Jumlah_Barang}",Integer.toString(0));
                
//                UIInput.make(newForm, "jumlah-total", "#");
                
                UICommand.make(newForm, "input-penjualan-barang", UIMessage.make("input_penjualan_barang"), "#{pollToolBean.processActionAddPenjualan}");
                
                UIOutput.make(tofill, "data-penjualan-title", messageLocator.getMessage("data_penjualan_title"));
                List<Object[]>dataPenjualan;
		dataPenjualan = pollVoteManager.getDataPenjualan();

		System.out.println("#A"+dataPenjualan.isEmpty());
                System.out.println("#A"+dataPenjualan.toString());

		for (Iterator <Object[]> iterator=dataPenjualan.iterator(); iterator.hasNext();){ 
                    Object[] list = iterator.next();
                    Integer kd_penjualan = (Integer)list[0];	
                    String nama_produk = (String)list[1];
                    String nama_customer = (String)list[2];
                    Integer jumlah_barang = (Integer)list[3];
                    BigInteger total_harga = (BigInteger)list[4];	

                    System.out.println("#ROSE"+list[0]);
                    System.out.println("#ROSE"+list[1]);
                    System.out.println("#ROSE"+list[2]);
                    System.out.println("#ROSE"+list[3]);
                    System.out.println("#ROSE"+list[4]);
                   UIBranchContainer penjualanrow = UIBranchContainer.make(tofill, "penjualan-row:"); 
                   //Create a new <td> element 
                   UIOutput.make(penjualanrow,"kd-penjualan", kd_penjualan.toString()); 
                   UIOutput.make(penjualanrow,"nama-produk", nama_produk); 
                   UIOutput.make(penjualanrow,"nama-customer", nama_customer);
                   UIOutput.make(penjualanrow,"jml-barang", jumlah_barang.toString()); 
                   UIOutput.make(penjualanrow,"total-harga", total_harga.toString()); 
                } 
				
	}


	private boolean isAllowedPollAdd() {
		if (externalLogic.isUserAdmin())
			return true;

		if (externalLogic.isAllowedInLocation(PollListManager.PERMISSION_ADD, externalLogic.getCurrentLocationReference()))
			return true;

		return false;
	}

	private boolean isSiteOwner(){
		if (externalLogic.isUserAdmin())
			return true;
		else if (externalLogic.isAllowedInLocation("site.upd", externalLogic.getCurrentLocationReference()))
			return true;
		else
			return false;
	}

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
                togo.add(new NavigationCase("success", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;
	}

	private boolean pollCanEdit(Poll poll) {
		if (externalLogic.isUserAdmin())
			return true;

		if (externalLogic.isAllowedInLocation(PollListManager.PERMISSION_EDIT_ANY, externalLogic.getCurrentLocationReference()))
			return true;

		if (externalLogic.isAllowedInLocation(PollListManager.PERMISSION_EDIT_OWN, externalLogic.getCurrentLocationReference())
				&& poll.getOwner().equals(externalLogic.getCurrentUserId()))
			return true;

		return false;
	}

	private boolean pollCanDelete(Poll poll) {
		return pollListManager.userCanDeletePoll(poll);
	}
}
