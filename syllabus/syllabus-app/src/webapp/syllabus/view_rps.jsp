<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
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
		    <td><p><c:out value="Analisa dan Perancangan Sistem Informasi" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Kode / Beban SKS" escapeXml="false"/></h5></td>
		    <td><p><c:out value="16TKO4013 / 4 SKS" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Semester" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Genap / IV" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Status Matakuliah" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Mata Kuliah Program Studi D3" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Bentuk Pembelajaran" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Kelas / Seminar / Praktikum" escapeXml="false"/></p></td>
		</tr>
		<tr>
		    <td><h5><c:out value="Dosen" escapeXml="false"/></h5></td>
		    <td><p><c:out value="Suprihanto, BSEE, M.S.c." escapeXml="false"/></p></td>
		</tr>
		<tr>
	</table>
	<h5 style="margin-top: 10px;"><c:out value="Deskripsi Mata Kuliah" escapeXml="false"/></h5>
	<p>
		&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspMata kuliah Analisa dan Perancangan Sistem Informasi pada Program Studi D-III Teknik Informatika Politeknik Negeri Bandung adalah mata kuliah Program Studi. Mata kuliah ini ditujukan untuk memberikan pembelajaran kepada mahasiswa mengenai analisa dan perancangan terstruktur dan berorientasi objek dari suatu sistem informasi, penggunaan alat-alat bantu pemodelan, penerapan metodologi dan teknik-teknik yang umum digunakan, serta pendokumentasiannya secara baku.
	</p>
</body>
</html>