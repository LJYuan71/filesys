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
<script src="${pageContext.request.contextPath }/js/fileUtils.js?v=6"></script>
<script type="text/javascript">
	$(function(){
		var conent = queryAttachFiles('1cd7295a-9174-45b1-9a96-d8a967fa4a5f','table',true,true,true,true,false).content;
		$("#WenJian_List").html(conent);
	})
</script>
</head>
<body>
    <h3>这里是应用的首页对应web.xml中对应的welcome-file-list配置</h3>
    <h4>尝试发送/web/spring.do 测试SpringMVC是否搭建成功!</h4>
    
    <h4>图片显示</h4>
    <img alt="无法显示" width="30%;" height="200px;" src="${pageContext.request.contextPath }/downLoadFile?fileId=2f940b78-2b1f-47a1-83f1-07e8f66a0f39">
    <h4>单文件下载</h4>
    <!-- 2c3fd212-f6a4-4bb5-ba3b-8d379bd64aef -->
    <a href="${pageContext.request.contextPath }/downLoadFile?fileId=2c3fd212-f6a4-4bb5-ba3b-8d379bd64aef">下载</a>
    <h4>文件组打包下载</h4>
    <a href="${pageContext.request.contextPath }/downLoadFile?groupId=a21b47f3-6e12-444a-90af-89785cd7512b">下载</a>
    <h4>文件组父文件打包下载</h4>
    <a href="${pageContext.request.contextPath }/downLoadFile?groupPid=fba14db2-793b-4c11-8549-7d02352051ab">下载</a>
    <h4>js生成文件列表及操作</h4>
    <!-- <table>
		<tr style="width: 100%">
			<td id="WenJian_List">
			</td>
		</tr>
	</table> -->
	<div id="WenJian_List">
	
	</div>
	<h4>iFrame调用上传页面</h4>
	<iframe frameborder="1" height="300px" width="50%" align="middle" src="${pageContext.request.contextPath }/imgupload.jsp"></iframe>
</body>
</html>
