/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.producers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.sakaiproject.poll.tool.producers.PermissionsProducer.VIEW_ID;
import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
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

        @Override
        public void fillComponents(UIContainer uic, ViewParameters vp, ComponentChecker cc) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List reportNavigationCases() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public ViewParameters getViewParameters() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
}
