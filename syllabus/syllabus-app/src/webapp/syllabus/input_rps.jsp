<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
	<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
		<jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
	</jsp:useBean>
	<sakai:view_container title="#{msgs.bar_create_edit}">
		<sakai:view_content>

<script>includeLatestJQuery('edit_bulk.jsp');</script>
<link rel="stylesheet" href="/library/webjars/jquery-ui/1.12.1/jquery-ui.min.css" type="text/css" />
<script type="text/javascript" src="/library/js/lang-datepicker/lang-datepicker.js"></script>


	<script type="text/javascript">
		jQuery(document).ready(function() {
			localDatePicker({
				input: '#syllabusEdit\\:dataStartDate',
				useTime: 0,
				parseFormat: 'YYYY-MM-DD',
				allowEmptyDate: true,
				val: '<h:outputText value="#{SyllabusTool.bulkEntry.startDate}"><f:convertDateTime pattern="yyyy-MM-dd"/></h:outputText>',
				ashidden: {iso8601: 'dataStartDateISO8601'}
			});
			localDatePicker({
				input: '#syllabusEdit\\:dataEndDate',
				useTime: 0,
				parseFormat: 'YYYY-MM-DD',
				allowEmptyDate: true,
				val: '<h:outputText value="#{SyllabusTool.bulkEntry.endDate}"><f:convertDateTime pattern="yyyy-MM-dd"/></h:outputText>',
				ashidden: {iso8601: 'dataEndDateISO8601'}
			});
		});
		$(function() {
			$('.timeInput').timepicker({
		    	hour: 8,
		    	defaultValue: "08:00 <h:outputText value="#{msgs.am}"/>",
		    	timeOnlyTitle: "<h:outputText value="#{msgs.choose_time}"/>",
				timeFormat: "hh:mm tt",
				currentText: "<h:outputText value="#{msgs.now}"/>",
				closeText: "<h:outputText value="#{msgs.done}"/>",
				amNames: ['<h:outputText value="#{msgs.am}"/>', '<h:outputText value="#{msgs.am2}"/>'],
				pmNames: ['<h:outputText value="#{msgs.pm}"/>', '<h:outputText value="#{msgs.pm2}"/>'],
				timeText: "<h:outputText value="#{msgs.time}"/>",
				hourText: "<h:outputText value="#{msgs.hour}"/>",
				minuteText: "<h:outputText value="#{msgs.minute}"/>",
				beforeShow: function (textbox, instance) {
					            instance.dpDiv.css({
					                    marginLeft: textbox.offsetWidth + 'px'
								});
							}
			});
			
			//radio options:
			$('.radioByDate input:radio').change(
				function(){
					$('.radioByItems input:radio').each(function(i){
						this.checked = false;
					});
					$('.radioOption').each(function(i){
						$(this).removeClass("radioOptionSelected");
					});
					$('.radioByDate').each(function(i){
						$(this).addClass("radioOptionSelected");
					});
					$('.bulkAddByItemsPanel').slideUp();
					$('.bulkAddByDatePanel').slideDown();
					resizeFrame('grow');
				}
			);
			$('.radioByItems input:radio').change(
				function(){
					$('.radioByDate input:radio').each(function(i){
						this.checked = false;
					});
					$('.radioOption').each(function(i){
						$(this).removeClass("radioOptionSelected");
					});
					$('.radioByItems').each(function(i){
						$(this).addClass("radioOptionSelected");
					});
					$('.bulkAddByItemsPanel').slideDown();
					$('.bulkAddByDatePanel').slideUp();
					resizeFrame('shrink');
				}
			);
			if($('.radioByDate input:radio').is(':checked')){
				//date option is selected... we need to setup the UI
				//this can happen if a user gets a warning message when
				//setting up the dates options
				$('.radioByDate input:radio').each(function(){
					$('.radioByItems input:radio').each(function(i){
						this.checked = false;
					});
					$('.radioOption').each(function(i){
						$(this).removeClass("radioOptionSelected");
					});
					$('.radioByDate').each(function(i){
						$(this).addClass("radioOptionSelected");
					});
					$('.bulkAddByItemsPanel').hide();
					$('.bulkAddByDatePanel').show();
					resizeFrame('grow');
				});
			}
		});
		//this function needs jquery 1.1.2 or later - it resizes the parent iframe without bringing the scroll to the top
		function resizeFrame(updown){
		    var clientH;
		    if (top.location != self.location) {
		        var frame = parent.document.getElementById(window.name);
		    }
		    if (frame) {
		        if (updown == 'shrink') {
		            clientH = document.body.clientHeight - 200;
		        }
		        else {
		            clientH = document.body.clientHeight + 200;
		        }
		        $(frame).height(clientH);
		    }
		    else {
		        throw ("resizeFrame did not get the frame (using name=" + window.name + ")");
		    }
		}
	</script>
	<style>
		.radioOption{
			background: none repeat scroll 0 0 #EEEEEE;
			border-radius: 5px 5px 5px 5px;
			padding: .5em;
			width: 35em;
		}
		
		.radioOptionSelected{
			background: none repeat scroll 0 0 #CCCCCC;
		}
	</style>
			<h:outputText value="#{SyllabusTool.alertMessage}" styleClass="alertMessage" rendered="#{SyllabusTool.alertMessage != null}" />
				
			<sakai:tool_bar_message value="#{msgs.inputRpsTitle}" /> 
			<sakai:doc_section>
				<h:outputText value="#{msgs.inputRps_desc}"/>
			</sakai:doc_section>
			<h:form id="syllabusEdit">
			<h:panelGrid columns="1" styleClass="jsfFormTable">
				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="namaMK">
						<h:outputText value="#{msgs.namaMK}"/>
					</h:outputLabel>
					<h:inputText readonly="true" value="Analisis dan Perancangan Perangkat Lunak" id="namaMK"/>
					<c:set var="fghfgh" value="" />
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="kodeMK">
						<h:outputText value="#{msgs.kodeMK}"/>
					</h:outputLabel>
					<h:inputText readonly="true" value="16TKO1063/4SKS" id="kodeMK"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="semester">
						<h:outputText value="#{msgs.semester}"/>
					</h:outputLabel>
					<h:inputText readonly="true" value="Genap/IV" id="semester"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="statusMK">
						<h:outputText value="#{msgs.statusMK}"/>
					</h:outputLabel>
					<h:inputText readonly="true" value="Mata Kuliah Program Studi D3" id="statusMK"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="bentukPmbljr">
						<h:outputText value="#{msgs.bentukPmbljr}"/>
					</h:outputLabel>
					<h:selectOneMenu value="#{SyllabusTool.formData.bentukPmbljr}">
							   	<f:selectItem itemValue="Kelas" itemLabel="Kelas" />
							   	<f:selectItem itemValue="Seminar" itemLabel="Seminar" />
							   	<f:selectItem itemValue="Praktikum" itemLabel="Praktikum" />
					</h:selectOneMenu>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="dosen">
						<h:outputText value="#{msgs.dosen}"/>
					</h:outputLabel>
					<h:inputText readonly="true" value="Suprihanto, BSEE, M.Sc." id="dosen"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="descMK">
						<h:outputText value="#{msgs.descMK}"/>
					</h:outputLabel>
					<h:inputTextarea readonly="true" value="Adalah Matakuliah Analisis Perancangan Perangkat Lunak" id="descMK"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="prasyarat">
						<h:outputText value="#{msgs.prasyarat}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.prasyarat}" id="prasyarat"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="referensi">
						<h:outputText value="#{msgs.referensi}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.referensi}" id="referensi"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="capaian">
						<h:outputText value="#{msgs.capaian}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.capaian}" id="capaian"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="peta">
						<h:outputText value="#{msgs.peta}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.peta}" id="peta"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="hasilBljr">
						<h:outputText value="#{msgs.hasilBljr}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.hasilBljr}" id="hasilBljr"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="topic">
						<h:outputText value="#{msgs.topic}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.topic}" id="topic"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="metodePmbljr">
						<h:outputText value="#{msgs.metodePmbljr}"/>
					</h:outputLabel>
					<h:inputText value="#{SyllabusTool.formData.metodePmbljr}" id="metodePmbljr"/>
				</h:panelGroup>

				<h:panelGroup styleClass="shorttext">
					<h:outputLabel for="jadwal">
						<h:outputText value="#{msgs.jadwal}"/>
					</h:outputLabel>
					<h:outputText value="Tatap Muka "/>
					<h:inputText value="" id="tmPert" size="3" maxlength="3"/>
					<h:outputText value=" X "/>
					<h:inputText value="" id="tnMnt" size="3" maxlength="3"/>
					<h:outputText value=" "/>

					<h:outputText value="Belajar Mandiri "/>
					<h:inputText value="" id="bmPert" size="3" maxlength="3"/>
					<h:outputText value=" X "/>
					<h:inputText value="" id="bmMnt" size="3" maxlength="3"/>

					<h:outputText value="Praktikum"/>
					<h:inputText value="" id="prPert" size="3" maxlength="3"/>
					<h:outputText value=" X "/>
					<h:inputText value="" id="prMnt" size="3" maxlength="3"/>

					<h:outputText value="Penugasan Terstruktur "/>
					<h:inputText value="" id="ptPert" size="3" maxlength="3"/>
					<h:outputText value=" X "/>
					<h:inputText value="" id="ptMnt" size="3" maxlength="3"/>
				</h:panelGroup>
			</h:panelGrid>

				<sakai:button_bar>
					<h:commandButton
						action="#{SyllabusTool.processInputForm}"
						styleClass="active"
						value="Lanjut ke RPS Mingguan" 
						accesskey="s"
						title="#{msgs.button_publish}" />
					<h:commandButton
						action="#{SyllabusTool.processEditBulkDraft}"
						value="Batalkan" 
						accesskey="s"
						title="#{msgs.button_save}" />
				</sakai:button_bar>
			</h:form>
		</sakai:view_content>
	</sakai:view_container>

</f:view>
