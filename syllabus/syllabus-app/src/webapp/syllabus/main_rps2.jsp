<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://sakaiproject.org/jsf/syllabus" prefix="syllabus" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% response.setContentType("text/html; charset=UTF-8"); %>


<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>

<f:view>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="session">
   <jsp:setProperty name="msgs" property="baseName" value="org.sakaiproject.tool.syllabus.bundle.Messages"/>
</jsp:useBean>

	<sakai:view_container title="#{msgs.title_list}">
	<sakai:view_content>
	
	<%
	  	String thisId = request.getParameter("panel");
  		if (thisId == null) 
  		{
    		thisId = "Main" + org.sakaiproject.tool.cover.ToolManager.getCurrentPlacement().getId();
  		}
	%>

<script>includeLatestJQuery('main.jsp');</script>
<script type="text/javascript" src="js/syllabus.js"></script>
<script type="text/javascript" src="/library/js/lang-datepicker/lang-datepicker.js"></script>
<link rel="stylesheet" href="/library/webjars/jquery-ui/1.12.1/jquery-ui.min.css" type="text/css" />

<script type="text/javascript">
	var msgs;
	var mainframeId = '<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>';
	// if redirected, just open in another window else
	// open with size approx what actual print out will look like
	function printFriendly(url) {
		if (url.indexOf("printFriendly") === -1) {
			window.open(url,"mywindow");
		}
		else {
			window.open(url,"mywindow","width=960,height=1100,scrollbars=yes"); 
		}
	}
	
	$(function() {
		msgs = {
				syllabus_title: $("#messages #syllabus_title").html(),
				syllabus_content: $("#messages #syllabus_content").html(),
				clickToAddTitle: $("#messages #clickToAddTitle").html(),
				startdatetitle: $("#messages #startdatetitle").html(),
				enddatetitle: $("#messages #enddatetitle").html(),
				clickToAddStartDate: $("#messages #clickToAddStartDate").html(),
				clickToAddEndDate: $("#messages #clickToAddEndDate").html(),
				clickToAddBody: $("#messages #clickToAddBody").html(),
				saved: $("#messages #saved").html(),
				error: $("#messages #error").html(),
				required: $("#messages #required").html(),
				startBeforeEndDate: $("#messages #startBeforeEndDate").html(),
				calendarDatesNeeded: $("#messages #calendarDatesNeeded").html(),
				clickToExpandAndCollapse: $("#messages #clickToExpandAndCollapse").html(),
				bar_delete: $("#messages #bar_delete").html(),
				bar_cancel: $("#messages #bar_cancel").html(),
				confirmDelete: $("#messages #confirmDelete").html(),
				deleteItemTitle: $("#messages #deleteItemTitle").html(),
				deleteAttachmentTitle: $("#messages #deleteAttachmentTitle").html(),
				bar_new: $("#messages #bar_new").html(),
				bar_publish: $("#messages #bar_publish").html(),
				addItemTitle: $("#messages #addItemTitle").html(),
				draftTitlePrefix: $("#messages #draftTitlePrefix").html(),
				noUndoWarning: $("#messages #noUndoWarning").html(),
				//tambahan
				method: $("#messages #method").html(),
				buatRps: $("#messages #buatRps").html()
			};
		setupAccordion('<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>',<h:outputText value="#{SyllabusTool.editAble == 'true' ? true : false}"/>, msgs, 
							'<h:outputText value="#{SyllabusTool.openDataId}"/>');
					if(<h:outputText value="#{SyllabusTool.editAble == 'true'}"/>){
						//draft/publish toggle:
						setupToggleImages("publish", "publish", "publishOn", "publishOff", msgs);
						//Calendar Toggle
						setupToggleImages("linkCalendar", "linkCal", "linkCalOn", "linkCalOff", msgs);
						//Public/Private to the world toggle
						setupToggleImages("view", "linkWorld", "linkWorldOn", "linkWorldOff", msgs);
						}else{
						//remove CSS classes (otherwise you get those hover over "pencil edit" images
						$(".editItem").removeClass("editItem");
						}
	});
	
	function showConfirmDeleteHelper(deleteButton, event){
		showConfirmDelete(deleteButton, msgs, event);
	}
	
	function showConfirmDeleteAttachmentHelper(deleteButton, event){
		showConfirmDeleteAttachment(deleteButton, msgs, event);
	}
	
	function showConfirmAddHelper(){
		showConfirmAdd(msgs,'<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>');
	}
	
	// in case user includes the URL of a site that replaces top,
	// give them a way out. Handler is set up in the html file.
	// Unload it once the page is fully loaded.
	$(window).load(function () {
        window.onbeforeunload = null;
	});

	// in case an iframe tries to replace the top, we have to give the author a way
	// to get to the page to remove it. This will be cancelled in the javascript
	// after all content has loaded.
	window.onbeforeunload = function()
	{ return ""; };// default message is OK
	
</script>

<%-- gsilver: global things about syllabus tool:
1 ) what happens to empty lists - still generate a table?
2 ) Ids generated by jsf start with _  not optimal keeps us from validating fully.
 --%>
	<h:form id="syllabus">
		<%--gsilver: would be best if used all sakai tags, or none, 2 blocks
		following just gets tries to get around the mix --%>		
		<f:verbatim><ul class="navIntraTool actionToolbar"></f:verbatim>
				
				
				<c:if test="${SyllabusTool.bulkAddItem}">
				<f:verbatim>
				<li>
					<span>
					</f:verbatim>
						<h:commandLink action="#{SyllabusTool.processListNewBulkMain}">
							<h:outputText value="#{msgs.title_edit}"/>
						</h:commandLink>
					<f:verbatim>
					</span>
				</li>
				</f:verbatim>
				</c:if>

				<c:if test="${SyllabusTool.bulkAddItem}">
				<f:verbatim>
				<li>
					<span>
					</f:verbatim>
						<h:commandLink action="#{SyllabusTool.processListNewBulkMain}">
							<h:outputText value="#{msgs.printView}"/>
						</h:commandLink>
					<f:verbatim>
					</span>
				</li>
				</f:verbatim>
				</c:if>
	   
			<sakai:tool_bar_message value="#{msgs.title_Rps}" /> 
			<sakai:doc_section>
				<h:outputText value="#{msgs.notYet_created_Rps}"/>
			</sakai:doc_section>



<body>
	<br>
	<button type="button" class="btn btn-info" style="margin-left: 900px;">Edit RPS</button>
	<button type="button" class="btn btn-info" style="margin-left: 2px;">Delete RPS</button>
	<br>
	<h1 align="center" style="margin-top: 30px;">Rencana Pembelajaran Semester</h1>
	<p align="center">Program Studi DIII Teknik Informatika</p>
	<p align="center">Jurusan Teknik Komputer dan Informatika</p>
	<table border="2" width="100%" align="center" style="margin-top: 40px;">
		<tr>
		    <td><h5><c:out value="Nama Mata Kuliah" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Analisa dan Perancangan Sistem Informasi" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Kode / Beban SKS" escapeXml="false"/></h5></td>
		    <td><p><c:out value="16TKO4013 / 4 SKS" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Semester" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Genap / IV" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Status Matakuliah" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Mata Kuliah Program Studi D3" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Bentuk Pembelajaran" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Kelas / Seminar / Praktikum" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Dosen" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Suprihanto, BSEE, M.S.c." escapeXml="false"/></p></td>
		</tr>
		<tr>
	</table>
	<h5 style="margin-top: 10px;"><c:out value="Deskripsi  Mata Kuliah" escapeXml="false"/></h5>
	<p>
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspMata kuliah Analisa dan Perancangan Sistem Informasi pada Program Studi D-III Teknik Informatika Politeknik Negeri Bandung adalah mata kuliah Program Studi. Mata kuliah ini ditujukan untuk memberikan pembelajaran kepada mahasiswa mengenai analisa dan perancangan terstruktur dan berorientasi objek dari suatu sistem informasi, penggunaan alat-alat bantu pemodelan, penerapan metodologi dan teknik-teknik yang umum digunakan, serta pendokumentasiannya secara baku.
	</p>


			</body>	
					</h:form>
	</sakai:view_content>
	</sakai:view_container>
</f:view>

</html>
