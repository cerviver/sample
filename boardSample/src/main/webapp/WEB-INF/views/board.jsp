<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 만들기</title>
<link rel="stylesheet" href="<c:url value='/resources/css/bootstrap.min.css'/>">
<script src="${pageContext.request.contextPath }/resources/js/jquery-3.5.1.min.js"></script>
<script src="${pageContext.request.contextPath }/resources/js/bootstrap.min.js"></script>
<script type="text/javascript">
	var page=1;
	
	$(document).ready(function() {
		
		list(page);
	});

 function list(pageNum) {
	 page=pageNum;
	
	 //alert("pageNum = "+pageNum);
	 
	 $.ajax({
		url: "<c:url value='/list.do'/>",
		method: "POST",
		data:{"pageNum":pageNum},
		dataType: "json",
		success: function(data) {
			$("#boardlist").children().remove();
			for(var i=0; i<data.boardList.length; i++) {
				var contents = data.boardList[i].contents;
					contents = contents.replace(/\n/gi, '\\n');
				var txt="<tr onclick=\"detail('"+data.boardList[i].title+"','"
						+contents+"','"+data.boardList[i].no+"','"+data.boardList[i].fname+"');\">";
				txt += "<td>"+data.boardList[i].no+"</td>"+
					"<td>"+data.boardList[i].title+"</td>"+
					"<td>"+data.boardList[i].date+"</td>";
				txt += "</tr>";
				$("#boardlist").append(txt);
			
				if(i == 0) {
					detail(data.boardList[i].title,contents.replace(/\\n/gi,'\n'),data.boardList[i].no,data.boardList[i].fname);
				}
			}
			
			pageList(data.pager);
		},
		error: function(req) {
			alert("응답 에러 코드 = "+req.status);
		}
	 });
 }
 
 function pageList(pager) {
	 var html="";
	 
	 html+="<ul class='pagination justify-content-center' style='margin:20px 0'>";
	 
	 if(pager.startPage>pager.blockSize) {
		 html+="<li class='page-item'><a class='page-link' href='javascript:list(1);'>[처음]</a></li>";
		 html+="<li class='page-item'><a class='page-link' href='javascript:list("+pager.prevPage+");'>[이전]</a></li>";
	 } 
	 else {
		 html+="<li class='page-item'><a class='page-link'> << </a></li>"; 
		 html+="<li class='page-item'><a class='page-link'> < </a></li>"; 
	 }
	 
	 /*
	 else {
		 html+="[처음][이전]";
	 }
	 */
	 
	 for(i=pager.startPage; i<=pager.endPage; i++) {
		 if(pager.pageNum!=i) {
			 html+="<li class='page-item'><a class='page-link' href='javascript:list("+i+");'>"+i+"</a></li>";
		 } else {
			 html+="<li class='page-item'><a class='page-link' >"+i+"</a></li>";
		 }
	 }
	 
	 if(pager.endPage!=pager.totalPage) {
		 html+="<li class='page-item'><a class='page-link' href='javascript:list("+pager.nextPage+");'>[다음]</a></li>"
		 html+="<li class='page-item'><a class='page-link' href='javascript:list("+pager.totalPage+");'>[마지막]</a></li>"
	 } 
	 else {
		 html+="<li class='page-item'><a class='page-link'> > </a></li>"; 
		 html+="<li class='page-item'><a class='page-link'> >> </a></li>"; 
	 }
	 /*
	 else {
		 html+="[다음][마지막]";
	 }
	 */
	 html+="</ul>";
	 
	 $("#pageNumDiv").html(html);
	 
	 /*
	<ul class="pagination pagination-sm " style="margin:20px 0"">
		<li class="page-item"><a class="page-link" href="#">Previous</a></li>
		<li class="page-item"><a class="page-link" href="#">1</a></li>
		<li class="page-item"><a class="page-link" href="#">2</a></li>
		<li class="page-item"><a class="page-link" href="#">3</a></li>
		<li class="page-item"><a class="page-link" href="#">Next</a></li>
	</ul>
	 */
 }
 
 function detail(title,contents,no,fname) {
	 $('#title').val(title);
	 $('#contents').val(contents);
	 $('#no').val(no);
	 $('#file').val('');
	 
	 //alert(fname);
	 
	 setTimeout(getImg(fname),500);
 }
 
 function getImg(fname) {
	   //alert(fname);	
	 
		$.ajax({
			url: "<c:url value='/img.do'/>?fname="+encodeURI(fname),
			processData: false,
			contentType: false,
			method: "GET",
			cache: false,
			data: '',
			success: function(data) {
				if(data == ''){
					$("#img").attr("src","");			
				} else {
					$("#img").attr("src","data:image/jpeg;base64,"+data);
				}
			},
			error: function(req) {
				alert("응답 에러 코드 = "+req.status);
			}
		});
}

 function save() {
	if( !confirm("저장 하시겠습니까?")){
		return;
	};

	var formData = new FormData();
	
	formData.append('title',$('#title').val());
	formData.append('contents',$('#contents').val());
	formData.append('file',$('#file')[0].files[0]);
	
	 $.ajax({
		url: "<c:url value='/add.do'/>",
		processData: false,
		contentType: false,
		method: "POST",
		cache: false,
		data: formData,
		success: function(data) {
			if(data.returnCode=="success") {
				list(page);
			} else {
				alert(data.returnDesc);
			}
		},
		error: function(req) {
			alert("응답 에러 코드 = "+req.status);
		}
	 });
 }
 
 function modi() {
	if( !confirm("수정하시겠습니까?")){
		return;
	};
	
	var formData = new FormData();
	
	formData.append('no',$("#no").val());
	formData.append('title',$("#title").val());
	formData.append('contents',$("#contents").val());
	formData.append('file',$('#file')[0].files[0]);
	
	 $.ajax({
		url: "<c:url value='/modi.do'/>",
		processData: false,
		contentType: false,
		method: "POST",
		cache: false,
		data: formData,
		success: function(data) {
			if(data.returnCode=="success") {
				list(page);
			} else {
				alert(data.returnDesc);
			}
		},
		error: function(req) {
			alert("응답 에러 코드 = "+req.status);
		}
	 });
 }
 
 function cancel() {
	//alert('cancel');
	$("#title").val('');
	$("#contents").val('');
	$("#img").attr("src","");
 }
 function del() {
	if($("#no").val()==''){
		alert("삭제할 데이터가 존재하지 않습니다.")
	};
	
	if( !confirm("삭제하시겠습니까?") ){
		return;
	}
	
	var formData = new FormData();
	
	formData.append('no',$("#no").val());
	formData.append('title',$("#title").val());
	formData.append('contents',$("#contents").val());
	
	
	$.ajax({
		url: "<c:url value='/del.do'/>",
		processData: false,
		contentType: false,
		method: "POST",
		cache: false,
		data: formData,
		success: function(data) {
			if(data.returnCode=="success"){
				$("#title").val('');
				$("#contents").val('');
				$("#file").val('');
				list();
			} else {
				alert(data.returnDesc);
			}
		},
		error: function(req) {
			alert("응답 에러 코드 = "+req.status);
		}
	});
 }
 function delimg() {
	 if($("#no").val()==''){
			alert("삭제할 데이터가 존재하지 않습니다.")
		};
		
		if( !confirm("이미지를 삭제하시겠습니까?") ){
			return;
		}
		
		var formData = new FormData();
		
		formData.append('no',$("#no").val());
		formData.append('title',$("#title").val());
		formData.append('contents',$("#contents").val());
		
		
		$.ajax({
			url: "<c:url value='/delimg.do'/>",
			processData: false,
			contentType: false,
			method: "POST",
			cache: false,
			data: formData,
			success: function(data) {
				if(data.returnCode=="success"){
					$("#title").val('');
					$("#contents").val('');
					$("#file").val('');
					list();
				} else {
					alert(data.returnDesc);
				}
			},
			error: function(req) {
				alert("응답 에러 코드 = "+req.status);
			}
		});
 }
 
</script>
</head>
<body>
<div class="card">
	<div class="card-header">
		<ul class="nav nav-tabs">
		  <li class="nav-item">
		    <a class="nav-link active" href="<c:url value='/board.do'/>">싱글이미지 게시판</a>
		  </li>
		  <li class="nav-item">
		    <a class="nav-link" href="<c:url value='/board2.do'/>">멀티이미지 게시판</a>
		  </li>
		</ul>
	</div>
    <div class="card-body">
    	<div class="row">
			<div class="col-lg-4">
				<div class="card" style="min-height:500px;max-height:1000px">
				<table class="table">
						<thead>
					      <tr>
					        <th>번호</th>
					        <th>게시물 리스트</th>
					      </tr>
						</thead>
						<tbody id="boardlist">
						</tbody>
				</table>
				<div id="pageNumDiv">
				</div>
				</div>
			</div>
			<div class="col-lg-5">
				<div class="card bg-light text-dark" style="min-height:500px;max-height:1000px">
					<form action="" id="form1" enctype="multipart/form-data">
					  <div class="form-group">
					    <label class="control-label" for="title">제목:</label>
					    <div>
					      <input type="text" class="form-control" name="title" id="title" placeholder="제목을 입력하세요">
					    </div>
					  </div>
					  <div class="form-group">
					    <label class="control-label" for="contents">내용:</label>
					    <div> 
					      <textarea class="form-control" rows="10" name="contents" id="contents"></textarea>
					    </div>
					  </div>
					  <div className="form-group">
							<label className="control-label">이미지첨부: jpg,gif,png</label>
							<div>
							    <input type="file" className="form-control" id="file" name="file" style="width:90%" />
							</div>
					  </div>
					  <input type="hidden" id="no" name="no"/>
					</form>
					<div style="text-align:center">
						<div class="btn-group">
						  <button type="button" class="btn btn-primary" onclick="save()">저장</button>
						  <button type="button" class="btn btn-secondary" onclick="cancel()">취소</button>
						  <button type="button" class="btn btn-success" onclick="modi()">변경</button>
						  <button type="button" class="btn btn-danger" onclick="del()">삭제</button>
						  <button type="button" class="btn btn-info" onclick="delimg()">그림삭제</button>
						</div>
					</div>
				</div>
			</div>
			<div class="col-lg-3">
				<div class="card bg-light text-dark" style="min-height:500px;max-height:1000px">
					<img src="" id="img" alt="image" style="max-width: 100%">
				</div>
			</div>
		</div>
    </div>
    <div class="card-footer">게시판 만들기</div>
</div>
</body>
</html>