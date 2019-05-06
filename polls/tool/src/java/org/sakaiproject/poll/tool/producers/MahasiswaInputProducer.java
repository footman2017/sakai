/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.producers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.Mahasiswa;
import org.sakaiproject.poll.model.Poll;
import org.sakaiproject.poll.tool.params.OptionViewParameters;
import org.sakaiproject.poll.tool.params.VoteBean;
import static org.sakaiproject.poll.tool.producers.AddPollProducer.VIEW_ID;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIBoundBoolean;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
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
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;

import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

/**
 *
 * @author Asus
 */

@Slf4j
public class MahasiswaInputProducer implements ViewComponentProducer,NavigationCaseReporter, ViewParamsReporter {

	public static final String VIEW_ID = "mahasiswaInput";
        
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private Mahasiswa mahasiswa;
        
        public String getViewID() {
		// TODO Auto-generated method stub
		return VIEW_ID;
	}
        
	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setLocaleGetter(LocaleGetter localeGetter) {
		this.localeGetter = localeGetter;
	}

        private VoteBean voteBean;
	public void setVoteBean(VoteBean vb){
		this.voteBean = vb;
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
        
        private PollListManager pollListManager;
        public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}
        
        private FormatAwareDateInputEvolver dateevolver;
	public void setDateEvolver(FormatAwareDateInputEvolver dateevolver) {
		this.dateevolver = dateevolver;
	}
        
        @Override
        public void fillComponents(UIContainer tofill, ViewParameters vp,
                ComponentChecker cc) {
            
                String currentuserid = externalLogic.getCurrentUserId();

//              PollViewParameters ecvp = (PollViewParameters) viewparams;
//		Poll poll = null;
		boolean isNew = true;

		String locale = localeGetter.get().toString();
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);
        
                UIOutput.make(tofill, "mahasiswa-html", null).decorate(new UIFreeAttributeDecorator(langMap));

                UIForm newMahasiswa = UIForm.make(tofill, "add-mahasiswa-form");
//		log.debug("Poll of id: " + ecvp.id);

                //edit
                
                
                //add new
                UIMessage.make(tofill,"mahasiswa_new_title","mahasiswa_new_title");
                //build an empty mahasiswa
                log.debug("this is a new mahasiswa");
                mahasiswa = new Mahasiswa();
                
		UIMessage.make(tofill, "nim-mahasiswa-label", "nim_mahasiswa_label");
		UIMessage.make(tofill, "nama-mahasiswa-label", "nama_mahasiswa_label");
                
                //the form fields
		UIInput.make(newMahasiswa, "nim-mahasiswa-text", "#{mahasiswa.nim}");
		UIInput.make(newMahasiswa, "nama-mahasiswa-text", "#{mahasiswa.nama}");
		
                UICommand.make(newMahasiswa, "submit-new-mahasiswa", UIMessage.make("new_mahasiswa_save"),
			"#{pollToolBean.processActionAddMahasiswa}");
                
                UICommand cancel = UICommand.make(newMahasiswa, "cancel", UIMessage.make("new_mahasiswa_cancel"),"#{pollToolBean.cancel}");
		log.debug("Finished generating view");
        }

        @Override
        public List reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		togo.add(new NavigationCase("added", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		togo.add(new NavigationCase("cancel", new SimpleViewParameters(PollToolProducer.VIEW_ID)));
		return togo;
        }

        @Override
        public ViewParameters getViewParameters() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}
