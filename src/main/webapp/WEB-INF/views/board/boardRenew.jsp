<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정하기</title>
</head>
<link href="${pageContext.request.contextPath}/resources/css/style.css"
	rel="stylesheet" type="text/css" />
<body bgcolor="#FFEFD5">
	<form name="renewForm" action="bUpdate.do" method="post"
		encType="multipart/form-data">
		<!-- encType이 오타없이 잘 적혀야 오류가 뜨지 않음 -->
		<!-- 아래 hidden은 실어주는 코드이다 이걸 가지고 bUpdate.do로 간다 -->
		<input type="hidden" name="board_num" value="${board.board_num}">
		<input type="hidden" name="board_file" value="${board.board_file}">
		<table align="center">
			<tr>
				<td>제목</td>
				<td><input type="text" name="board_title"
					value="${board.board_title}"></td>
			</tr>
			<tr>
				<td>작성자</td>
				<td>${board.board_writer}</td>
			</tr>
			<tr>
				<td>글 비밀번호</td>
				<td><input type="password" name="board_pwd"></td>
			</tr>
			<tr>
				<td>이전 첨부파일</td>
				<td>
					<c:if test="${empty board.board_file}">
					첨부파일 없음
					</c:if>
					<c:if test="${!empty board.board_file}">
						<a href="${pageContext.request.contextPath}/resources/uploadFiles/${board.board_file}"
							download>${board.board_file}</a>
					</c:if>
				</td>
			</tr>
			<tr>
				<td>변경할 첨부파일</td>
				<td><input type="file" name="upfile" multiple></td>
			</tr>
			<tr>
				<td>내용</td>
				<td><textarea cols="50" rows="7" name="board_content">${board.board_content}</textarea></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" id="renew" value="수정하기"> &nbsp; 
					<%-- <c:url var="blist" value="blist.do">
						<c:param name="page" value="1" />
					</c:url>  --%>
					<a href="bList.do">목록으로</a></td>
			</tr>
		</table>
	</form>
</body>
<script
	src="${pageContext.request.contextPath}/resources/js/jquery-3.5.1.js"></script>
<script type="text/javascript">
	$(function() {
		$('form[name=renewForm]').on('submit', function(event) {
			if ($('input[name=board_pwd]').val() != "${board.board_pwd}") {
				alert("비밀번호가 일치하지 않습니다.");
				event.preventDefault(); // 지금까지 쌓여있는 이벤트를 싹 없앤다
			} else {
				return true; // 남아있는 다른 동작들을 하게 된다
			}
		});
	});
</script>
</html>