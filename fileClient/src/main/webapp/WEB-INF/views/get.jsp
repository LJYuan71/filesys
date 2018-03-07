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
<link rel="stylesheet" href="${pageContext.request.contextPath }/css/showfile.css?v=22">
<title>查看用户信息</title>
<script src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<!-- <script src="http://localhost:8080/filesys/js/fileUtils.js?v=15"></script> -->
<script src="${pageContext.request.contextPath }/js/fileUtils.js?<%= Math.random()%>"></script>
<script src="${pageContext.request.contextPath }/js/layui-v2.2.5/layui/layui.js" charset="utf-8"></script>
<script src="${pageContext.request.contextPath }/js/upload.js"></script>
<script type="text/javascript">
	$(function(){
		layui.use('layer');
		var picFileId = '${sysUser.picFileId }';
		var detFileId = '${sysUser.detailsFileId }';
		if(picFileId){
			var picContent = queryAttachFiles(picFileId,'table',false,false,false,false,false).content;
			$("#picFile_List").html(picContent);
			$("#downLoadPic").attr("href",fileDownLoadUrl+'&groupId='+picFileId);
		}
		if(detFileId){
			var fileContent = queryAttachFiles(detFileId,'table',false,false,false,false,false).content;
			$("#detailsFile_List").html(fileContent);
			$("#downLoadDet").attr("href",fileDownLoadUrl+'&groupId='+detFileId);
		}
		if(picFileId || detFileId){
			//全部下载
			$("#downLoadAll").attr("href",fileDownLoadUrl+'&groupPid='+$("#userid").val());
			//选择下载
			$("#downLoadChoose").click(function(){
				var fileIds = [];
				$("tr[fileId]").each(function(){
					var fileId = $(this).attr("fileId");
					var fileLen = $("tr[fileId]").length;
					var r = Math.random() * fileLen;
					if (r > (fileLen/2)){//(这里使用随机下载)
						fileIds.push(fileId);
					}
				})
				window.location.href= fileDownLoadUrl+"&fileIds="+fileIds.join(",");
			})
			
		}
	})
</script>
</head>
<body>
	<div class="layui-container table_details">
		<input id="userid" value ="${sysUser.id }" style="display: none;"/>
		<form class="layui-form" action="">
			<div class="layui-row">
				<div class="layui-col-xs2">
					<div>账号名:</div>
				</div>
				<div class="layui-col-xs2">
					<div>${sysUser.account }</div>
				</div>
				<div class="layui-col-xs2">
					<div>用户名:</div>
				</div>
				<div class="layui-col-xs2">
					<div>${sysUser.username }</div>
				</div>
				<div class="layui-col-xs4">
					<a href="javaScript:void(0);" id="downLoadAll" class="layui-btn">下载全部</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javaScript:void(0);" id="downLoadChoose" class="layui-btn">选择下载</a>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-xs2">
					<div>密码:</div>
				</div>
				<div class="layui-col-xs10">
					<div>${sysUser.password }</div>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-xs2">
					<div>图片文件:<a href="javaScript:void(0);" id="downLoadPic" class="layui-btn">下载</a></div>
				</div>
				<div class="layui-col-xs10">
					<div id="picFile_List"></div>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-xs2">
					<div>详细资料:<a href="javaScript:void(0);" id="downLoadDet" class="layui-btn">下载</a></div>
				</div>
				<div class="layui-col-xs10">
					<div id="detailsFile_List"></div>
				</div>
			</div>
    	</form>
	</div>
</body>
</html>
