<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title></title>
        <link rel="stylesheet" type="text/css" href="js/bootstrap-3.3.7/css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css" href="js/bootstrap-3.3.7/css/bootstrap-theme.min.css">
        <link rel="stylesheet" type="text/css" href="css/main.css">
        <link rel="stylesheet" type="text/css" href="js/webuploader/webuploader.css">
    </head>
    <body>
       <div class="page-container">
            <div id="uploader" class="wu-example">
                <div class="queueList">
                    <div id="dndArea" class="placeholder">
                        <div id="filePicker" class="webuploader-container"><div class="webuploader-pick">点击选择图片</div><div id="rt_rt_1c1bqae9prg3gqs1a1q1nl7rch1" style="position: absolute; top: 0px; left: 718px; width: 168px; height: 44px; overflow: hidden; bottom: auto; right: auto;"><input type="file" name="file" class="webuploader-element-invisible" multiple="multiple" accept="images/*"><label style="opacity: 0; width: 100%; height: 100%; display: block; cursor: pointer; background: rgb(255, 255, 255);"></label></div></div>
                        <p>或将照片拖到这里</p>
                    </div>
                <ul class="filelist"></ul></div>
                <div class="statusBar" style="display:none;">
                    <div class="progress" style="display: none;">
                        <span class="text">0%</span>
                        <span class="percentage" style="width: 0%;"></span>
                    </div>    
                    <div class="info">共0张(0B)，已上传0张</div>
                    <div class="btns">
                        <div id="filePicker2" class="webuploader-container"><div class="webuploader-pick">继续添加</div><div id="rt_rt_1c1bqae9rj8j3461ba1css1qq6" style="position: absolute; top: 0px; left: 0px; width: 1px; height: 1px; overflow: hidden;"><input type="file" name="file" class="webuploader-element-invisible" multiple="multiple" accept="images/*"><label style="opacity: 0; width: 100%; height: 100%; display: block; cursor: pointer; background: rgb(255, 255, 255);"></label></div></div>
                        <div class="uploadBtn state-pedding">开始上传</div>
                    </div>
                </div>
            </div>
        </div>
	<iframe id="myframe" src="#" style="display:none;"></iframe>
		<script type="text/javascript">
			var filesysUrl = '${pageContext.request.contextPath }';
		</script>
        <script src="js/jquery-2.1.4.min.js"></script>
		<script src="js/fileUtils.js?v=1"></script>
        <script src="js/bootstrap-3.3.7/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/webuploader/webuploader.min.js"></script>
        <script>
	        var filesys = getUrlParam("filesys");
	        var filePrivateKey = getUrlParam("filePrivateKey");
	        var curUserId = getUrlParam("curUserId");
	        var groupId = getUrlParam("groupId") || guid();
			var callBackUrl = getUrlParam("url");
	        if (!filesys || !filePrivateKey) {
	        	window.close();
	        }
	        window.webuploader = {
	            config:{
	                thumbWidth: 110, //缩略图宽度，可省略，默认为110
	                thumbHeight: 110, //缩略图高度，可省略，默认为110
	                wrapId: 'uploader', //必填
	                thumbContainer:document.body.clientWidth*0.9//缩略图容器大小
	            },
	            //处理客户端新文件上传时，需要调用后台处理的地址, 必填
	            uploadUrl: '${pageContext.request.contextPath }/FileUploadServlet?filesys='+filesys+'&filePrivateKey='+filePrivateKey,
	            //用于秒传的验证
	            checkUrl:'${pageContext.request.contextPath}/checkFileExistServlet?action=checkAndSave'
	            //处理客户端原有文件更新时的后台处理地址，必填
	            /* updateUrl: './fileupdate.php', */
	            //当客户端原有文件删除时的后台处理地址，必填
	            /* removeUrl: './filedel.php' */
	            //初始化客户端上传文件，从后台获取文件的地址, 可选，当此参数为空时，默认已上传的文件为空
	           /*  initUrl: './fileinit.php', */
	        }
	        /**
			 * 获取iframe传递过来的参数
			 */
			function getUrlParam(name){  
			    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");  
			    if (reg.test(location.href)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";  
			}; 
        </script>
        <script src="js/extend-webuploader.js?<%=Math.random() %>" type="text/javascript"></script>
</body>
</html>