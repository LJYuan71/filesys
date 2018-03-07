<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<script type="text/javascript">
var curUserId = '${sysUser.id }';
var filePrivateKey = '${fileUrl.filePrivateKey}';
var filesys = '${fileUrl.filesys}';
var filesysUrl = '${fileUrl.filesysUrl}';
var fileDownLoadUrl = '${fileUrl.fileDownLoadUrl}?userId='+curUserId+'&filesys='+filesys+'&filePrivateKey='+filePrivateKey+'&fileId=';
var fileDelUrl = '${fileUrl.fileDelUrl}?userId='+curUserId+'&filesys='+filesys+'&filePrivateKey='+filePrivateKey;
var fileGroupDelUrl = '${fileUrl.fileGroupDelUrl}?userId='+curUserId+'&filesys='+filesys+'&filePrivateKey='+filePrivateKey;
var queryFilesUrl = '${fileUrl.queryFilesUrl}';
var imgUploadUrl = '${fileUrl.imgUploadUrl}';
var fileUploadUrl = '${fileUrl.fileUploadUrl}';
var chunkUploadUrl = '${fileUrl.chunkUploadUrl}';
var crossDomainUrl = '${fileUrl.crossDomainUrl}';
var ctx = '${pageContext.request.contextPath }';
</script>
</head>
<body>
</body>
</html>