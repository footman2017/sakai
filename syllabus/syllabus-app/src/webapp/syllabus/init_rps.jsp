
			<h:outputText value="#{SyllabusTool.alertMessage}" styleClass="alertMessage" rendered="#{SyllabusTool.alertMessage != null}" />
				
			<sakai:tool_bar_message value="#{msgs.add_sylla_bulk}" /> 
			<sakai:doc_section>
				<h:outputText value="#{msgs.newSyllabusBulkForm}"/>
			</sakai:doc_section>
			<h:form id="syllabusEdit">
				<sakai:button_bar>
					<h:commandButton
						action="#{SyllabusTool.processEditBulkPost}"
						styleClass="active"
						value="#{msgs.bar_publish}" 
						accesskey="s"
						title="#{msgs.button_publish}" />
				</sakai:button_bar>
			</h:form>