<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/crossJsp/fileconfig.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="${pageContext.request.contextPath }/js/layui-v2.2.5/layui/css/layui.css"  media="all">
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath }/css/main.css?v=22"> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/showfile.css?v=22">
<title>用户编辑</title>
<script src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<!-- <script src="http://localhost:8080/filesys/js/fileUtils.js?v=15"></script> -->
<script src="${pageContext.request.contextPath }/js/fileUtils.js?<%=Math.random() %>"></script>
<script src="${pageContext.request.contextPath }/js/layui-v2.2.5/layui/layui.js" charset="utf-8"></script>
<script src="${pageContext.request.contextPath }/js/layerUtils.js?<%=Math.random() %>" charset="utf-8"></script>
<script src="${pageContext.request.contextPath }/js/upload.js?<%=Math.random() %>"></script>
<script type="text/javascript">
	$(function(){
		layui.use('layer');
		var groupPid = $("#groupPid").val();
		var groupId = $("#groupPid").val();
		var picFileId = '${sysUser.picFileId }';
		var detFileId = '${sysUser.detailsFileId }';
		if(picFileId){
			var picContent = queryAttachFiles(picFileId,'table',true,true,true,false,false).content;
			$("#picFile_List").html(picContent);
		}
		if(detFileId){
			var fileContent = queryAttachFiles(detFileId,'table',true,true,true,false,false).content;
			$("#detailsFile_List").html(fileContent);
		}
		if(!'${sysUser.id }'){
			document.title = "添加用户";
		}
		$("#upPic").click(function(){
			isPic = true;
			groupId = $("#picFileId").val();
			var parementObj={};
			parementObj["groupId"] = groupId;
			parementObj["groupPid"] = groupPid;
			parementObj["title"] = '图片上传';
			parementObj["area"] = ['893px', '450px'];
			parementObj["type"] = "img";
			parementObj["url"] = crossDomainUrl;
			upLoadFile(parementObj);
		})
		$("#upDetailsFile").click(function(){
			isPic = false;
			groupId = $("#detailsFileId").val();
			var parementObj={};
			parementObj["groupId"] = groupId;
			parementObj["groupPid"] = groupPid;
			parementObj["title"] = '文件上传';
			parementObj["area"] = ['893px', '450px'];
			parementObj["type"] = "file";
			parementObj["url"] = crossDomainUrl;
			parementObj["filter"] = "docx,doc,xls";
			upLoadFile(parementObj);
		})
		$("#save").click(function(){
			var formObj = $("#userForm").serializeObject();
			$.ajax({
				type:"POST",
				contentType:"application/json",
				data:JSON.stringify(formObj),
				url:'${pageContext.request.contextPath }/sysUser/save.do',
				success:function(data){
					if (data.code == 200) {
						layer.alert('编辑成功！',{title:'提示'},function(index){
							parentEditCloseWin();
						});
					} else {
						layer.alert("保存失败");
					}
				},
				error:function(data){
					layer.alert("程序错误!");
				}
			})
		});
	})
	//文件上传回调函数
	var isPic;
	function uploadBack(groupId, groupPid){
		$("#groupPid").val(groupPid);
		if (isPic) {
			$("#picFileId").val(groupId);
			var picContent = queryAttachFiles(groupId,'table',true,true,true,false,false).content;
			$("#picFile_List").html(picContent);
		} else {
			$("#detailsFileId").val(groupId);
			var fileContent = queryAttachFiles(groupId,'table',true,true,true,false,false).content;
			$("#detailsFile_List").html(fileContent);
		}
	}
</script>
</head>
<body>
	<div class="layui-container table_details" style="top: 30px;">
		<form class="layui-form" action="" id="userForm">
		  <input name="groupPid" id="groupPid" value="${sysUser.id }" type="hidden"/>
		  <input name="id" id="id" value="${sysUser.id }" type="hidden"/>
		  <div class="layui-form-item">
		    <div class="layui-inline">
		      <label class="layui-form-label">账号名:</label>
		      <div class="layui-input-inline">
		        <input type="text" name="account" lay-verify="required" autocomplete="off" placeholder="请输入账号名" class="layui-input" value="${sysUser.account }">
		      </div>
		    </div>
		    <div class="layui-inline">
		      <label class="layui-form-label">用户名:</label>
		      <div class="layui-input-inline">
		        <input type="text" name="username" lay-verify="required" autocomplete="off" placeholder="请输入用户名" class="layui-input" value="${sysUser.username }">
		      </div>
		    </div>
		  </div>
		  <div class="layui-form-item">
		    <label class="layui-form-label">密码:</label>
		    <div class="layui-input-block">
		      <input type="password" name="password" lay-verify="required" autocomplete="off" placeholder="请输入密码" class="layui-input" value="${sysUser.password }">
		    </div>
		  </div>
		  
		  <div class="layui-form-item">
		  	<div class="layui-inline">
			    <label class="layui-form-label">图片文件:</label>
			    <div class="layui-input-block">
			      <input type="hidden" name="picFileId" id="picFileId" value="${sysUser.picFileId }">
			      <button type="button" class="layui-btn" id="upPic"><i class="layui-icon"></i>上传图片</button>
			    </div>
			</div>
		    <div class="layui-inline" id="picFile_List">
				
			</div>
		  </div>
		  <div class="layui-form-item">
		  	<div class="layui-inline">
			    <label class="layui-form-label">详细资料:</label>
			    <div class="layui-input-block">
			      <input type="hidden" name="detailsFileId" id="detailsFileId" value="${sysUser.detailsFileId }">
			      <button type="button" class="layui-btn" id="upDetailsFile"><i class="layui-icon"></i>上传资料</button>
			    </div>
		    </div>
		    <div  class="layui-inline" id="detailsFile_List">
				
			</div>
		  </div>
	    </form>
	  <button id="save" class="layui-btn" style="display: none;">保存</button>
	</div>
</body>
</html>
