<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>使用webuploader普通上传</title>
<!-- 1.引入文件 -->
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/bootstrap-3.3.7/css/bootstrap.min.css"> --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/webuploader/webuploader.css?v=<%=Math.random() %>">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css?v=<%=Math.random() %>">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery.plupload.queue.css?v=<%=Math.random() %>">
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	var filesysUrl = '${pageContext.request.contextPath }';
</script>
<script src="${pageContext.request.contextPath }/js/fileUtils.js?v=<%=Math.random() %>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/bootstrap-3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/webuploader/webuploader.js"></script>
</head>
<body>
	<iframe id="myframe" src="#" style="display:none;"></iframe>
	<div>
		<div class="plupload">
		<div class="plupload_header">
			<div class="plupload_header_content">
				<div class="plupload_header_title">选择文件</div>
				<div class="plupload_header_text">将文件添加到上传队列，然后点击”开始上传“按钮。</div>
			</div>
		</div>
		<div class="plupload_content">
			<div class="plupload_filelist_header">
				<div class="plupload_file_name">文件名</div>
				<div class="plupload_file_action">&nbsp;</div>
				<div class="plupload_file_status">
					<span>状态</span>
				</div>
				<div class="plupload_file_size">大小</div>
				<div class="plupload_clearer">&nbsp;</div>
			</div>
			<div id="uploader" style="">
			<div id="dndArea">
					<!-- 用于显示文件列表 -->
					<div id="fileList">
						<ul id="uploader_filelist" class="plupload_filelist"
							style="position: relative;">
						</ul>
					</div>
					
			</div>
			</div>
			<div class="plupload_filelist_footer">
				<div class="plupload_file_name">
					<div class="plupload_buttons">
						<a href="#" class="plupload_button plupload_add"
							id="filePicker" style="position: relative; z-index: 1;">增加文件</a>
						<a href="#" class="plupload_button plupload_start plupload_disabled" id="uploadBtn">开始上传</a>
						<span class="plupload_total_status"></span>
					</div>
				</div>
				<div class="plupload_file_action"></div>
				<div class="plupload_file_status">
					<span class="plupload_total_status">0%</span>
				</div>
				<div class="plupload_file_size">
					<span class="plupload_total_file_size">0 b</span>
					<input class="total_size" style="display: none;"/>
				</div>
				<div class="plupload_progress">
					<div class="plupload_progress_container">
						<div class="plupload_progress_bar"></div>
					</div>
				</div>
				<div class="plupload_clearer">&nbsp;</div>
			</div>
		</div>
	</div>
	</div>
	<div class="">
		<input id="filesys" value="200" style="display: none;"/>
	    <input id="filePrivateKey" value="1bb3e07be89ccdc64ba94826e12fd2e2" style="display: none;"/>
		<!-- 2.创建页面元素 -->
		<div id="uploader" style="">
			<!-- 创建用于拖拽的区域 -->
			<!-- <div id="dndArea"> -->
				<!-- 用于显示文件列表 -->
				<!-- <div id="fileList"></div> -->
			<!-- </div> -->
			<!-- 用于选择文件 -->
			<!-- <div id="filePicker">选择文件</div> -->
		 	<!-- <button class="btn m-b-xs btn-sm btn-info btn-addon" id="uploadBtn" style="padding:7px 50px;margin-top:20px;">开始上传 -->
		</div>
	</div>
	<!-- 3.添加js代码 -->
	<script type="text/javascript">
		$(function(){
			$("#uploader_filelist").delegate("li.plupload_delete a","click",function(){
				removeFile($(this).parent().parent());
			  });
		})
		var fileMd5;//guid()
		var fileName;
		var files = [];
		var groupId = getUrlParam("groupId") || guid();
		var curUserId = getUrlParam("curUserId");
		//文件格式
		var filter = getUrlParam("filter");
		var mimeTypes;
		if(filter) mimeTypes = "."+filter.replace(new RegExp(',','g'),',.');
		var filesys = getUrlParam("filesys");
        var filePrivateKey = getUrlParam("filePrivateKey");
        var callBackUrl = getUrlParam("url");
		var $upBtn = $('#uploadBtn');//开始上传按钮 
		var $queue = $('#uploader_filelist');
		state = 'pending';//初始按钮状态 
		var count = 0;//当前正在上传的文件在数组中的下标，一次上传多个文件时使用  
	
		// 上传基本配置
		var uploader = WebUploader.create(
			{
				swf:"${pageContext.request.contextPath }/js/Uploader.swf",
				server:"${pageContext.request.contextPath }/FileUploadServlet?filesys="+filesys+"&filePrivateKey="+filePrivateKey,
				pick:{
					id: '#filePicker', //这个id是你要点击上传文件按钮的外层div的id  
	                multiple : true //是否可以批量上传，true可以同时选择多个文件
				},
				auto:false,//选择文件后是否自动上传
				dnd:"#dndArea",
				disableGlobalDnd:true,
				paste:"#uploader",
				//<input type="file" name="file" class="webuploader-element-invisible" multiple="multiple" accept=".ZGYS">
			 	accept: {
		            /* title: 'application', */
		            extensions: filter || '',
		            mimeTypes: mimeTypes || ''
		        },
				formData:{"userId":curUserId,"groupId":groupId,"sysId":filesys},
				resize: false,  //不压缩image, 默认如果是jpeg，文件上传前会先压缩再上传！
				//开启几个并非线程（默认3个）
				threads:3,
				//是否分块
				chunked:false,
				duplicate : false,//是否重复上传（同时选择多个一样的文件），true可以重复上传 
				// 在上传当前文件时，准备好下一个文件
				prepareNextFile:true,
				fileNumLimit:10,//验证文件总数量, 超出则不允许加入队列
				fileSizeLimit:1*1024*1024*1024,//不适用分片上传时，总文件大小限制1G
		        fileSingleSizeLimit:50*1024*1024  //不适用分片上传时，单个文件限制50M
			}		
		);
		
		// 生成缩略图和上传进度
		uploader.on("fileQueued", function(file) {
	        addFile(file);
		  });
		//error :// 当validate不通过时，会以派送错误事件的形式通知调用者。通过upload.on('error', handler)可以捕获到此类错误  
		uploader.on('error',function(handler){
			if (handler == 'F_EXCEED_SIZE') {
				isChangeChunk("已超出单文件上传大小限制("+uploader.options.fileSingleSizeLimit/(1024*1024)+"M),是否启用断点续传");
			} else if (handler == 'F_DUPLICATE') {
				alert("请不要上传重复文件");
			} else if (handler == 'Q_TYPE_DENIED') {
				alert("不支持此格式文件,请选择:\""+uploader.options.accept[0].mimeTypes+"\"格式文件");
			} else if (handler == 'Q_EXCEED_NUM_LIMIT') {
				alert("已超出文件上传数量限制("+uploader.options.fileNumLimit+"个)");
			} else if (handler == 'Q_EXCEED_SIZE_LIMIT') {
				isChangeChunk("已超出文件上传总大小限制("+uploader.options.fileSizeLimit/(1024*1024)+"M),是否启用断点续传");
			}
		})
		function isChangeChunk(msg){//是否变更为分块上传
			if (confirm(msg)==true){ 
				var urlParam = location.href.substring(location.href.indexOf("?"));
				window.location.href = "${pageContext.request.contextPath }/filechunkupload.jsp"+urlParam;
			 }
		}
		
		// 监控上传进度
		// percentage:代表上传文件的百分比
		uploader.on("uploadProgress", function(file, percentage) {
			$("#" + file.id).find("span.state").text("");
			$("#" + file.id).find("div.plupload_file_status").text(Math.round(percentage * 100) + "%");
		});
		//监控所有事件
		uploader.on('all', function(type){ //type会有很多类型,我们只对指定的类型做处理
	        if (type === 'startUpload'){//正在上传时，暂停按钮
	            state = 'uploading';  
	            $upBtn.text('暂停上传');
	        }else if(type === 'stopUpload'){ //正在暂停时，继续上传按钮
	            state = 'paused';
	            $upBtn.text('继续上传');
	        }else if(type === 'uploadFinished'){  
	            state = 'done';  
	            $upBtn.text('上传完成');
	        }
	    }); 
		//开始上传与暂停上传按钮添加上传事件
		$upBtn.on('click',function(){
			if (state === 'uploading'){//上传状态可以暂停
	            uploader.stop(true);  //暂停上传
	        } else if (state === 'paused' || state === "pending") {//暂停状态或者初始化状态可以上传
	        	//执行上传  
	            uploader.upload();
                $(".removeFileBtn").hide();
	        } else if ( state === 'done') {//结束状态关闭页面
	        	alert("上传完成,即将关闭此页面");
	        }
		})
		//上传成功后执行的方法  
	    uploader.on('uploadSuccess', function(file,response) {    
	        $('#'+file.id).toggleClass("plupload_done");
	        $("#" + file.id).find("div.plupload_file_status").text("100%");
	        var done_size = doneSize();
	        var total_size = parseInt($(".total_size").val() || 0);
	        $(".plupload_total_status").text((done_size/total_size).toFixed(2)+"%");
	        $('#'+file.id).find('.progress').fadeOut();  
	        $('#'+file.id).find('.state').text('上传成功');
	        if(response && response.fileMd5){//通过上传请求回来的
	    		var upFile = {"fileName":file.name,"fileId":response.fileId,
	    				"fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
	    		files.push(upFile);
	    	}
	    });
	      
	    //上传出错后执行的方法  
	    uploader.on('uploadError', function(file) {  
	        $('#'+file.id).toggleClass("plupload_failed");
	        $('#'+file.id).find('.progress').fadeOut();
	        $('#'+file.id).find('.state').text('上传失败');
	        console.warn(file.name+"上传失败!");
	    }); 
	 	// 当所有文件上传结束时触发
	    uploader.on('uploadFinished', function(file) {  
	    	$upBtn.text("上传成功");
	    	$(".plupload_total_status").text("100%");
	    	state = "done";
	    	var stats = uploader.getStats();
		  	if( stats.successNum && stats.queueNum == 0 && stats.uploadFailNum == 0 ){
            	document.getElementById('myframe').src=callBackUrl+'?groupId='+groupId+'&files='+encodeURIComponent(JSON.stringify(files))+'&v='+Math.random();
	        }
		  	//$(".plupload_total_status").text(stats.successNum+"个文件上传成功! "+stats.uploadFailNum+"个文件上传失败!");
    	});
	    
		//删除队列中的文件
		function removeFile(del_li){
			var currentFileId = del_li.attr("id");
			//删除文件
            uploader.removeFile(currentFileId, true);  
           	del_li.remove(); 
           	totalSizeInfo();
		}
		 //添加附件到webuploader中
	    function addFile(file){
	    	//位置调整
	        var $li = '<li id="'+file.id+'" class="plupload_delete">'
	        	+'<div class="plupload_file_name"><span>'+file.name+'</span></div>'
	        	+'<div class="plupload_file_action"><a href="#" style="display: block;"></a></div>'
	        	+'<div class="plupload_file_status">0%</div><div  class="plupload_file_size">'+fileSizeInfo(file.size)+'<input class="file_size" value="'+file.size+'" style="display: none;"/></div>'
	        	+'<div class="plupload_clearer">&nbsp;</div></li>';
			$queue.append($li);
			totalSizeInfo();
	    }
	 	 //文件大小信息
		function fileSizeInfo(fileSize){
			if(!fileSize || isNaN(fileSize)) fileSize = 0;
			var k = fileSize/1024;
			if(k > 1024*5){
				var m = k / 1024;
				return m.toFixed(2)+" MB";
			}
			return k.toFixed(2)+" KB";
		}
	 	//总的文件大小
	 	function totalSizeInfo(){
			 var total_size = 0;
			$(".file_size").each(function(){
				total_size += parseInt($(this).val());
			})
			$(".plupload_total_file_size").text(fileSizeInfo(total_size));//总大小
			$(".total_size").val(total_size);
	 	}
	 	//完成的文件大小
	 	function doneSize(){
			 var total_size = 0;
			$("li.plupload_done").each(function(){
				total_size += parseInt($(this).find(".file_size").val()||0);
			})
			return total_size;
	 	}
		/**
		 * 获取iframe传递过来的参数
		 */
		function getUrlParam(name){  
		    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");  
		    if (reg.test(location.href)) return unescape(RegExp.$2.replace(/\+/g, " ")); return "";  
		}; 
	</script>
</body>
</html>