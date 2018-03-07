<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/crossJsp/fileconfig.jsp"%>
<title>文件上传的回调页面</title>
<script src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath }/js/fileUtils.js?<%=Math.random() %>"></script>
</head>

<body>

</body>
</html>
<script type="text/javascript">
	$(function() {
		//文件数组对象
		var files = getUrlParam("files");
		var fArray = eval('('+files+')');
		//取参数对象
		var groupId = getUrlParam("groupId");
		//取参数对象
		var groupPid = getUrlParam("groupPid");
		//回调
		if(groupId){
			var retObj = saveAttachFiles(fArray, groupId, groupPid);
			if(retObj.status == true){
				parent.parent.uploadBack(groupId, groupPid);
				//获取上传文件弹窗的index
				var upload_index = sessionStorage.getItem('upload_index');
				parent.parent.layer.close(upload_index);
			}else{
				alert("保存文件出错");
				return;
			}
		}
	});

	/**
	 * 获取iframe传递过来的参数
	 */
	function getUrlParam(name){  
	    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");  
	    if (reg.test(location.href)) return decodeURIComponent(RegExp.$2.replace(/\+/g, " ")); return "";  
	}; 
</script>