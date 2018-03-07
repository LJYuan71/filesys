<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>默认首页</title>
<style type="text/css">
a {
	text-decoration: none;
}

td {
	text-align: center;
}
.left{
	text-align:left;
}
</style>
<script src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath }/js/fileUtils.js?v=2"></script>
<script type="text/javascript">
	$(function(){
		var conent = queryAttachFiles('1cd7295a-9174-45b1-9a96-d8a967fa4a5f','table',true,true,true,true,false).content;
		$("#WenJian_List").html(conent);
	})
</script>
</head>
<body>
	内容
</body>
</html>
