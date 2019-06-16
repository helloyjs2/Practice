<%@page import="com.test.sub.BoardDTO"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath();
%>
<%
	BoardDTO dto = new BoardDTO();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List.jsp</title>
<link rel="stylesheet" href="<%=cp%>/css/style.css">
<link rel="stylesheet" href="<%=cp%>/css/list.css">

</head>
<body>
	<div id="bbsList">
		<h1></h1>
		<div id="bbsList_title">게 시 판</div>
		<div id="bbsList_header">
			<div id="leftHeader">
				<form action="" name="searchForm" method="post">
					<select name="searchKey" class="selectFiled">
						<option value="subject">제목</option>
						<option value="name">작성자</option>
						<option value="content">내용</option>
					</select> <input type="text" name="searchValue" class="textFiled"> <input type="button" value="검색" class="btn2" onclick="sendIt()">
				</form>
			</div>

			<div id="rightHeader">
				<input type="button" value="글올리기" class="btn2" onclick="javascript:location.href='<%=cp%>/Created.jsp'">
			</div>

		</div>
		<!--close bbsList_Header  -->

		<div id="bbsList_list">
			<div id="title">
				<dl>
					<dt class="num">번호</dt>
					<dt class="subject">제목</dt>
					<dt class="name">작성자</dt>
					<dt class="created">작성일</dt>
					<dt class="hitCount">조회수</dt>
				</dl>
			</div>
			<!--  close title-->

			<div id="lists">
				

				<dl>
					<dd class="num"><%=dto.getNum()%></dd>
					<dd class="subject">
						<A href="${articleUrl}"&num=<%=dto.getNum()%>"><%=dto.getSubject()%></A>
					</dd>
					<dd class="name"><%=dto.getName()%></dd>
					<dd class="created"><%=dto.getCreated()%></dd>
					<dd class="hitCount"><%=dto.getHitCount()%></dd>
				</dl>



			</div>
			<!--  close lists-->

			<div id="footer">
				<!-- <p>1 prev 21 22 23 24 next 90</p> -->
				<p>

				</p>
			</div>
			<!--  close footer-->
		</div>
	</div>
	
	<script type="text/javascript">
	function sendIt(){
		
		var f = document.searchForm;
		// 유효성 검사
		
		f.action="<%=cp %>/List.jsp";
		f.submit();
	}
	
	</script>
</body>
</html>