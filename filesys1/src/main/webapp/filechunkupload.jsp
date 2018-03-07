<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>使用webuploader分片上传</title>
<!-- 1.引入文件 -->
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/bootstrap-3.3.7/css/bootstrap.min.css"> --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/js/webuploader/webuploader.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/main.css?v="${Math.random() }>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/jquery.plupload.queue.css?v="${Math.random() }>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript">
	var filesysUrl = '${pageContext.request.contextPath }';
</script>
<script src="js/fileUtils.js?v=2"></script>
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
					</div>
					<span class="plupload_upload_status"></span>
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
	<!-- 3.添加js代码 -->
	<script type="text/javascript">
		$(function(){
			$("#uploader_filelist").delegate("li.plupload_delete a","click",function(){
				removeFile($(this).parent().parent());
			  });
		})
		// 监听分块上传的时间点，断点续传
		var fileMd5;
		var fileName;
		var files = [];
		var groupId = getUrlParam() || guid();
		var groupPid = getUrlParam() || guid();
		var curUserId = getUrlParam("curUserId");
		var filesys = getUrlParam("filesys");
        var filePrivateKey = getUrlParam("filePrivateKey");
     	 //文件格式
		var filter = getUrlParam("filter");
		var mimeTypes;
		if(filter) mimeTypes = "."+filter.replace(new RegExp(',','g'),',.');
		var filesys = getUrlParam("filesys");
        var callBackUrl = getUrlParam("url");
		var $upBtn = $('#uploadBtn');//开始上传按钮 
		var $queue = $('#uploader_filelist');
		state = 'pending';//初始按钮状态 
		var count = 0;//当前正在上传的文件在数组中的下标，一次上传多个文件时使用  
		WebUploader.Uploader.register({//添加组件，API 名称与函数实现的映射。注意时间点
			"before-send-file":"beforeSendFile",
			"before-send":"beforeSend",
			"after-send-file":"afterSendFile"
			},{
				//时间点1：所有分块进行上传之前调用此函数
				beforeSendFile:function(file) {
					// 创建一个deffered,用于通知是否完成操作
					var deferred = new $.Deferred();
					
					// 计算文件的唯一标识，用于断点续传和秒传，对文件进行的md5开始和结束位置
					//要想和后台服务器对文件md5加密生成的一样,需要传入完整的file,如果仅仅需要作为文件分片标识,又想速度快的,那么传入chunkSize的值即可(file, 0, 10*1024*1024)
					(new WebUploader.Uploader()).md5File(file)
						.progress(function(percentage){
							$(".plupload_upload_status").text("正在获取文件信息...");
						})
						.then(function(val) {
							file["md5"] = val;
							$(".plupload_upload_status").text("成功获取文件信息");
							$.ajax({
						      type:"POST",
						      url:"${pageContext.request.contextPath}/checkFileExistServlet?action=checkAndSave", 
						      dataType: 'json',
						       data: {
						    	  fileMd5:file.md5,
						    	  fileName:file.name,
						    	  fileSize:file.size,
						    	  userId:curUserId,
						    	  groupId:groupId,
						    	  groupPid:groupPid
						      },
						      success: function(response) {
							  	  // 如果验证已经上传过
								  if (response.ifExist) {
									  uploader.skipFile(file);
									  $("#" + file.id).find("span.state").text("秒传");
									  $("#" + file.id).find("span.percentage").text(Math.round(1 * 100) + "%");
									  var upFile = {"fileName":file.name,"fileId":response.fileId,
							    				"fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
							    	  files.push(upFile);
								  }
							  	  deferred.resolve();
							      // 放行,进入下一步
						      }
							});
						});
					return deferred.promise();
				},
				//时间点2：如果有分块上传，则每个分块上传之前调用此函数
				beforeSend:function(block) {
					var deferred = new $.Deferred();
					if(Math.ceil(block.file.size/uploader.options.chunkSize) > 1){
						// 支持断点续传，发送到后台判断是否已经上传过
						$.ajax({
							type:"POST",
							url:"${pageContext.request.contextPath}/UploadActionServlet?action=checkChunk",
							data:{
								//文件名称
								fileName : block.file.name,
								// 文件唯一表示								
								fileMd5:block.file.md5,
								// 当前分块下标
								chunk:block.chunk,
								// 当前分块大小
								chunkSize:block.end-block.start
							},
							dataType:"json",
							timeout: 3000, //超时的话，只能认为该分片未上传过 
							success:function(response) {
								if(response.ifExist) {
									// 分块存在，跳过该分块
									deferred.reject();
								} else {
									// 分块不存在或不完整，重新发送
									deferred.resolve();
								}
							}
						});
					} else {
						deferred.resolve();
					}
					// 将文件md5添加到formData中传到后台
					this.owner.options.formData.fileMd5 = block.file.md5;
					return deferred.promise();
				},
				//时间点3：所有分块上传成功后调用此函数
				afterSendFile:function(file) {
					//判断是否使用了分片
					if(Math.ceil(file.size/uploader.options.chunkSize) > 1){
						var deferred = new $.Deferred();
						// 通知合并分块
						$.ajax({
							type:"POST",
							url:"${pageContext.request.contextPath}/UploadActionServlet?action=mergeChunks",
							data:{
								fileName : file.name,
								fileMd5:file.md5,
								userId:curUserId,
								groupId:groupId,
								groupPid:groupPid
							},
							success:function(response){//成功后继续下一个文件的上传
								if(response && response.status == 0){
									uploader.stop(true); 
									alert(file.name+"分片信息不完整,请重新上传!");
								} else {
									if (response && response.fileMd5) {
										
										var upFile = {"fileName":file.name,"fileId":response.fileId,
							    				"fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
							    		files.push(upFile);
									}
									deferred.resolve(); 
								}
							}
						}); 
					}
					
				}
			}
		);
	
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
				accept: {
		            extensions: filter || '',
		            mimeTypes: mimeTypes || ''
		        },
				formData:{"groupId":groupId,"groupPid":groupPid,"userId":curUserId,},
				resize: false,  //不压缩image, 默认如果是jpeg，文件上传前会先压缩再上传！
				// 分块上传设置
				// 是否分块
				chunked:true,
				// 每块文件大小（默认5M）
				chunkSize:10*1024*1024,
				// 开启几个并非线程（默认3个）
				threads:3,
				duplicate : false,//是否重复上传（同时选择多个一样的文件），true可以重复上传 
				// 在上传当前文件时，准备好下一个文件
				prepareNextFile:true,
				fileNumLimit:10,//验证文件总数量, 超出则不允许加入队列
				fileSizeLimit:6*1024*1024*1024,//6G 验证文件总大小是否超出限制, 超出则不允许加入队列  
		        fileSingleSizeLimit:3*1024*1024*1024  //3G 验证单个文件大小是否超出限制, 超出则不允许加入队列
			}		
		);
		
		// 生成缩略图和上传进度
		uploader.on("fileQueued", function(file) {
	        addFileHtml(file);
		  });
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
	        var done_size = parseInt($("#" + file.id).find("input.plupload_file_status").val() || 0);
	        var total_size = parseInt($(".total_size").val() || 0);
	        $(".plupload_total_status").text((done_size/total_size).toFixed(2)+"%");
	        $('#'+file.id).find('.progress').fadeOut();  
	        $('#'+file.id).find('.state').text('上传成功');
	        console.log(file.name+"上传成功");
	        if(response && response.fileMd5){
	        	console.log(response);
		        var upFile = {"fileName":file.name,"fileId":response.fileId,
	    				"fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
	    		files.push(upFile);
	        }
	        //console.log(file.name+"上传成功!");
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
	    	//利用frame可以跨域的特性来传输数据
	    	var total = uploader.getFiles().length; 
	    	var stats = uploader.getStats();
	    	console.log("total:"+total);
	    	console.log(stats);
		  	if( stats.successNum == (total-stats.cancelNum) ){
            	document.getElementById('myframe').src=callBackUrl+'?groupId='+groupId+'&groupPid='+groupPid+'&files='+encodeURIComponent(JSON.stringify(files))+'&v='+Math.random();
	        }
	    });
	   //error :// 当validate不通过时，会以派送错误事件的形式通知调用者。通过upload.on('error', handler)可以捕获到此类错误  
		uploader.on('error',function(handler){
			if (handler == 'F_EXCEED_SIZE') {
				alert("已超出单文件上传大小限制("+uploader.options.fileSingleSizeLimit/(1024*1024)+"M)");
			} else if (handler == 'F_DUPLICATE') {
				alert("请不要上传重复文件");
			} else if (handler == 'Q_TYPE_DENIED') {
				alert("不支持此格式文件,请选择\""+uploader.options.accept[0].mimeTypes+"\"格式类型文件");
			} else if (handler == 'Q_EXCEED_NUM_LIMIT') {
				alert("已超出文件上传数量限制("+uploader.options.fileNumLimit/(1024*1024)+"M)");
			} else if (handler == 'Q_EXCEED_SIZE_LIMIT') {
				alert("已超出文件上传总大小限制("+uploader.options.fileSizeLimit/(1024*1024)+"M)");
			}
		})
		//删除队列中的文件
		function removeFile(del_li){
			var curFileId = del_li.attr("id");
			//删除文件
            uploader.removeFile(curFileId, true);  
           	del_li.remove(); 
           	totalSizeInfo();
		}
		 //添加附件到webuploader中
	    function addFileHtml(file){
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
		function fileSizeInfo(fileSize) {
			if (!fileSize || isNaN(fileSize))
				fileSize = 0;
			var k = fileSize / 1024;
			if (k > 1024 * 5) {
				var m = k / 1024;
				return m.toFixed(2) + " MB";
			}
			return k.toFixed(2) + " KB";
		}
		 //总文件大小信息
		function totalSizeInfo(){
			 var total_size = 0;
			$(".file_size").each(function(){
				total_size += parseInt($(this).val());
			})
			$(".plupload_total_file_size").text(fileSizeInfo(total_size));//总大小
			$(".total_size").val(total_size);
		 }
		/**
		 * 获取iframe传递过来的参数
		 */
		function getUrlParam(name) {
			var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
			if (reg.test(location.href))
				return unescape(RegExp.$2.replace(/\+/g, " "));
			return "";
		};
	</script>
</body>
</html>