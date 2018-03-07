jQuery(function(){
    var $ = jQuery;
    if( !window.webuploader ){
        console.log("请配置好window.webuploader");
        $('#dndArea p').html('请配置好window.webuploader');
        return false;
    }
    if( !window.webuploader.config || !window.webuploader.config.wrapId){
        console.log("请配置好window.webuploader.config.wrapId");
        $('#dndArea p').html('请配置好window.webuploader.config.wrapId');
        return false;
    }
    if( !window.webuploader.uploadUrl ){
        console.log("请配置好window.webuploader.uploadUrl");
        $('#dndArea p').html('请配置好window.webuploader.uploadUrl');
        return false;
    }
    var fileMd5;
	var fileName;
	var count = 0;
    var disX = 0;
    var disY = 0;
    var minZindex = 1;
    var origin;
    var is_moveing = false;
    var $wrap = $('#' + window.webuploader.config.wrapId);
    var $queue = $('<ul class="filelist"></ul>').appendTo( $wrap.find('.queueList'));
    var $statusBar = $wrap.find('.statusBar');
    var $info = $statusBar.find('.info');
    var $upload = $wrap.find('.uploadBtn');
    var $placeHolder = $wrap.find('.placeholder');
    var $progress = $statusBar.find('.progress').hide();
    var fileCount = 0;
    var fileSize = 0;
    var state = 'pedding';
    var percentages = {};
    //回调文件列表
    var files = [];
    var supportTransition = (function(){
        var s = document.createElement('p').style,
            r = 'transition' in s ||
                'WebkitTransition' in s ||
                'MozTransition' in s ||
                'msTransition' in s ||
                'OTransition' in s;
        s = null;
        return r;
    })();
    
    //添加组件
    WebUploader.Uploader.register({
		"before-send-file":"beforeSendFile",
		"before-send":"beforeSend",
		"after-send-file":"afterSendFile"
		},{
			//时间
			beforeSendFile:function(file) {
				// 创建一个deffered,用于通知是否完成操作
				var deferred = WebUploader.Deferred();
				
				// 计算文件的唯一标识，用于断点续传和秒传，对文件进行的md5开始和结束位置
				//要想和后台服务器对文件md5加密生成的一样,需要传入完整的file,如果仅仅需要作为文件分片标识,又想速度快的,那么传入chunkSize的值即可(file, 0, 10*1024*1024)
				(new WebUploader.Uploader()).md5File(file)
					.progress(function(percentage){
						$("#"+file.id).find(".progess span").text("正在获取文件信息...");
					})
					.then(function(val) {
						fileMd5 = val;
						$("#" + file.id).find(".progess span").text("成功获取文件信息");
						$.ajax({
					      type:"POST",
					      url: window.webuploader.checkUrl, 
					      dataType: 'json',
					       data: {
					    	  fileMd5:fileMd5,
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
								  /*$("#" + file.id).find(".progess span").text("秒传");*/
								  $("#" + file.id).find(".progess span").text(Math.round(1 * 100) + "%");
								  var upFile = {"fileName":file.name,"fileId":response.fileId,
										  "fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
								  files.push(upFile);
							  }
						  	  deferred.resolve();
						      // 放行,进入下一步
					      }
						});
					});
				// 通知完成操作
				 fileName = file.name; //为自定义参数文件名赋值
				return deferred.promise();
			},
			beforeSend:function() {
	            var deferred = WebUploader.Deferred();
	         	//将文件md5添加到formData中传到后台
	            this.owner.options.formData.fileMd5 = fileMd5;
	            deferred.resolve();
	            return deferred.promise();
	        },
	        afterSendFile:function() {
	        	
	        }
		}
	);
    
    
    // 优化retina, 在retina下这个值是2
    var ratio = window.devicePixelRatio || 1;
    // 缩略图大小
    var thumbnailWidth = window.webuploader.config.thumbWidth || 110;
    thumbnailWidth *= ratio;
    var thumbnailHeight = window.webuploader.config.thumbHeight || 110;
    var thumbContainer = window.webuploader.config.thumbContainer || document.body.clientWidth*0.9;
    thumbnailHeight *= ratio;
    var uploader = WebUploader.create({
        swf: "webuploader-0.1.5/Uploader.swf",
        server: window.webuploader.uploadUrl,
        pick: {
            id:'#filePicker',
            label: '点击选择图片'
        },
        dnd: '.queueList',
        paste: document.body,
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        },
        compress: false,//不启用压缩
        resize: false,//尺寸不改变
        disableGlobalDnd: true,
        formData:{"userId":curUserId,
        		groupId:groupId,
        		groupPid:groupPid},
        threads:3,
        chunked: false,
        duplicate : false,//是否重复上传（同时选择多个一样的文件），true可以重复上传 
		// 在上传当前文件时，准备好下一个文件
		prepareNextFile:true,
        fileNumLimit: 30,
        fileSingleSizeLimit:50*1024*1024,
        fileSizeLimit:1*1024*1024*1024
    });

    function setDragEvent(){
        $(this).on('drop', function(e){
            var $from = $(origin).parents('li');
            var $to = $(e.target).parents('li');
            var origin_pos = $from.position();
            var target_pos = $to.position();
            var from_sort = $from.attr('data-sort');
            var to_sort = $to.attr('data-sort');

            $from.addClass('move').animate(target_pos,"fast", function(){
                $(this).removeClass('move');
            }).attr('data-sort', to_sort);

            $to.addClass('move').animate(origin_pos,'fast', function(){
                $(this).removeClass('move');
            }).attr('data-sort', from_sort);
        }).on('dragstart', function(e){
            if(is_moveing){
                return false;
            }
            is_moveing = true;
            e.originalEvent.dataTransfer.effectAllowd = 'move';
            origin = this;
        }).on('dragover', function(e){
            if( e.preventDefault)
                e.preventDefault();
            is_moveing = false;
            e.originalEvent.dataTransfer.dropEffect = 'move';
        });
    }

    //添加附件到webuploader中
    function addFile( file ){
    	//位置调整
        var index = $queue.find('li').length;//点添加文件前有几张图片
        var imgLeft = index * (thumbnailWidth+10);//第n张图的左边距离,根据其来绝对定位图片
        var imgTop = 0;//上边距
        var wrapHeight = thumbnailHeight+20;
        var thumbColnum = Math.floor(thumbContainer/(thumbnailWidth+10));//每行可以最多容下几张图
        var topIndex = Math.floor(index/thumbColnum);//当前图片应该位于第几行
        if( topIndex >= 1){//第二行以后
            imgTop = topIndex * (thumbnailHeight+10);//当前图片的top
            wrapHeight = imgTop + wrapHeight;//为容器高度设置值
            imgLeft = (index % (parseInt(thumbContainer/(thumbnailWidth+10)) ) ) * (thumbnailWidth+10);
        }
        $queue.height(wrapHeight);
        var $li = $('<li data-key="'+file.key+'" title="'+file.name+'" data-src="'+file.src+'" data-sort="'+index+'" draggable="true" id="' + file.id + '" style="position:absolute;margin:0;cursor:move;width:'+thumbnailWidth+'px;height:'+thumbnailHeight+'px;left:'+imgLeft+'px;top:'+imgTop+'px;">' +
                '<p class="title">' + file.name + '</p>' +
                '<p class="imgWrap"></p>' + 
                '<p class="progress"><span></span></p>' + '</li>'
            ),
            $btns = $('<div class="file-panel">' +
                '<span class="cancel" title="移除"></span>').appendTo( $li ),

            $progess = $li.find('p.progress span'),
            $wrap = $li.find('p.imgWrap'),
            $info = $('<p class="error"></p>'),

            showError = function( code ){
                switch( code ){
                    case 'exceed_size':
                        text = '文本大小超出';
                        break;
                    case 'interrupt':
                        text = '上传暂停';
                        break;
                    default:
                        text = '上传失败';
                        break;
                }
                $info.text( text ).appendTo( $li );
            };

        if( file.src == "client"){
            if( file.getStatus() == 'invalid'){
                showError( file.statusText );
            } else {
                $wrap.text('预览中');
                uploader.makeThumb( file, function(error, src){
                    if( error ){
                        $wrap.text('不能预览');
                        return ;
                    }
                    var img = $('<img draggable="true" src="'+src+'">');
                    img.bind('load', setDragEvent);
                    $wrap.empty().append( img );
                }, thumbnailWidth, thumbnailHeight);

                percentages[ file.id ] = [ fileSize, 0];
                file.rotation = 0;
            };

            file.on('statuschange', function(cur, prev){
                if( prev == 'progress'){
                    $progress.hide().width(0);
                } else if( prev == 'queued'){
                    $li.off('mouseenter mouseleave');
                    $btns.remove();
                }

                if( cur == 'error' || cur == 'invalid'){
                    showError( file.statusText );
                    percentages[ file.id][ 1 ] = 1;
                } else if( cur == 'interrupt'){
                    showError('interrupt');
                } else if( cur == 'queued'){
                    percentages[ file.id ][1] = 0;
                } else if( cur == 'progress'){
                    $info.remove();
                    $progress.css('display', 'block');
                } else if( cur == 'complete' ){
                    $li.append('<span class="success"></span>');
                }

                $li.removeClass('state-'+prev).addClass('state-'+cur);
            });
        }
        else{
            var img = $('<img draggable="true" src="'+file.path+'">');
            img.bind('load',setDragEvent);
            $wrap.empty().append( img );
        }

        $li.on('mouseenter', function(){
            $btns.stop().animate({height:30});
        });
        $li.on('mouseleave', function(){
            $btns.stop().animate({height:0})
        });

        $btns.on('click', 'span', function(){
            var index = $(this).index(), deg;
            switch( index ){
                case 0:
                    //修改删除后面所有图片的位置
                    var allImgs = {};
                    var del_sort = parseInt($('#'+file.id).attr('data-sort'));
                    $queue.find('li').each(function(index, obj){
                        if( $(obj).attr('data-sort') > del_sort){
                            var sort = parseInt($(obj).attr('data-sort'));
                            var $prevObj = $("li[data-sort="+(sort-1)+"]");
                            if( $prevObj ){
                                allImgs[$(obj).attr('id')] = $prevObj.position();
                            }
                        }
                    });
                    var oldTop ;//图片移动前的top
                    var newTop ;//图片移动后的top
                    for(var k in allImgs){//对后续的图片排序
                        var sort = parseInt($('#'+k).attr('data-sort'));
                        oldTop = $('#'+k).css('top');
                        $('#'+k).attr('data-sort',sort-1).css({left:allImgs[k].left+'px', top:allImgs[k].top+'px'});
                        newTop = allImgs[k].top+"px";
                    }
                    if (oldTop != newTop) {//容器高度缩小
                    	$queue.height($queue.height()-(thumbnailHeight+10));
                    }                   
                    allImgs = null;
                    if( file.src == "client")
                        uploader.removeFile( file );
                    else{
                        $('#'+file.id).remove();
                    }
                    return;
                case 1:
                    file.rotation += 90;
                    break;
                case 2:
                    file.rotation -= 90;
                    break;
            }

            if( supportTransition ){
                deb = 'rotate('+ file.rotation +'deg)';
                $wrap.css({
                    '-webkit-transform': deb,
                    '-mos-transform': deg,
                    '-o-transform': deg,
                    'transform': deg
                });
            } else {
            }
        });
        $li.appendTo( $queue );
    }

    //删除webupload中的图片
    function removeFile( file ){
        var $li = $('#'+file.id);
        delete percentages[ file.id ];
        updateTotalProgress();
        $li.off().find('.file-panel').off().end().remove();
    }

    //更新webuploader中图片上传的进度
    function updateTotalProgress(){
        var loaded = 0,
            total = 0,
            spans = $progress.children(),
            percent;

        $.each( percentages, function(k,v){
            total += v[0];
            loaded += v[0] * v[1];
        });

        percent = total? loaded /total : 0;
        spans.eq(0).text(Math.round(percent*100)+'%');
        spans.eq(1).css('width', Math.round(percent*100)+'%');
        updateStatus();
    }

    //更新webuploader中的状态
    function updateStatus(){
        var text = '', stats;
        if( state == 'ready'){
            text = '选中'+fileCount + '张图片，共'+ WebUploader.formatSize( fileSize ) +'.';
        } else if( state == 'confirm'){
            stats = uploader.getStats();
            if( stats.uploadFailNum ){
                text = '已成功上传'+stats.successNum+'张照片'+stats.uploadFailNum +'张照片上传失败,<a class="retry" href="#">重新上传</a>失败图片或<a class="ignore" href="#">忽略</a>';
            }
        } else {
            stats = uploader.getStats();
            text = '共' + fileCount +'张('+WebUploader.formatSize(fileSize)+')，已上传'+stats.successNum+'张';
            if( stats.uploadFailNum){
                text += ',失败'+ stats.uploadFailNum +'张';
            }
        }
        $info.html(text);
    }

    //设置webuploader的状态
    function setState(val){
        var file,stats;
        if( val == state){
            return ;
        }
        $upload.removeClass('state-'+state);
        $upload.addClass('state-'+val);
        state = val;

        switch( state ){
            case 'pedding':
                $placeHolder.removeClass('element-invisible');
                $queue.parent().removeClass('filled');
                $queue.hide();
                $statusBar.addClass('element-invisible');
                uploader.refresh();
                break;
            case 'ready':
                $placeHolder.addClass('element-invisible');
                $('#filePicker2').removeClass('element-invisible');
                $queue.parent().addClass('filled');
                $queue.show();
                $statusBar.removeClass('element-invisible');
                uploader.refresh();
                break;
            case 'uploading':
                $('filePicker2').addClass('element-invisible');
                $progress.show();
                $upload.text('暂停上传');
                break;
            case 'paused':
                $progress.show();
                $upload.text('继续上传');
                break;
            case 'confirm':
                $progress.hide();
                $upload.text('开始上传').addClass('disabled');
                stats = uploader.getStats();
                if( stats.successNum && !stats.uploadFailNum ){
                    setState( 'finish' );
                    return ;
                }
                break;
            case 'finish':
                stats = uploader.getStats();
                if( stats.successNum ){
                	//利用frame可以跨域的特性来传输数据
                    document.getElementById('myframe').src=callBackUrl+'?groupId='+groupId+'&groupPid='+groupPid+'&files='+encodeURIComponent(JSON.stringify(files))+'&v='+Math.random();
                } else {
                    state = 'done';
                    location.reload();
                }
                break;
        }
        updateStatus();
    }

    //文件加入到webuploader中的队列
    function fileQueue(file){
        file.src = file.src || "client";
        fileCount++;
        fileSize += file.size;

        if( fileCount == 1){
            $placeHolder.addClass('element-invisible');
            $statusBar.show();
        }

        addFile(file);
        setState( 'ready' );
        updateTotalProgress();
    }


    if( !WebUploader.Uploader.support() ) {
        console.log('WebUploader 不支持');
        throw new Error('WebUploader does not support');
    }

    uploader.addButton({
        id: '#filePicker2',
        label: '继续添加',
    });


    uploader.on('uploadProgress', function( file, percentage){
        var $li = $('#' + file.id),
        $percent = $li.find('.progess span');
        
        $percent.css( "width", percentage * 100 + '%');
        updateTotalProgress();
    });


    uploader.on('fileQueued', fileQueue);

    uploader.on('fileDequeued', function(file){
        fileCount --;
        fileSize -= file.size;
        if( !fileCount){
            setState('pedding');
        }
        removeFile( file );
        updateTotalProgress();
    });

    uploader.on('uploadSuccess', function(file,response){
    	if(response){//通过上传请求回来的
    		var upFile = {"fileName":file.name,"fileId":response.fileId,
    				"fileMd5":response.fileMd5,"uploadTime":response.uploadTime,"length":file.size};
    		files.push(upFile);
    	}
        $('#' + file.id ).find('p.state').text('已上传');
    });

    uploader.on('uploadError', function(file){
        console.log(file.id + '上传出错');
    });

    uploader.on('uploadComplete', function(file){
        $('#' + file.id ).find('p.state').fadeOut();
    });

    uploader.on('all', function( type ){
        if( type == 'uploadFinished') {
            setState('confirm');
        } else if( type == 'startUpload' ){
            setState('uploading');
        } else if( type == 'stopUpload' ){
            setState('paused');
        }
    });

    uploader.on('uploadBeforeSend', function(block, data){
        data.sort = $('#'+data.id).attr('data-sort');
    });

    $upload.on('click', function(){
        uploader.sort(function(obj1, obj2){
            return $('#'+obj1.id).attr('data-sort') > $('#'+obj2.id) ? -1: 1;
        });
        if( $(this).hasClass('disabled')){
            return false;
        }
        if( state == 'ready'){
            if(uploader.getFiles().length < 1){
            	
            } else {
            	uploader.upload();
            }
        } else if(state == 'paused'){
            uploader.upload();
        } else if( state == 'uploading'){
            uploader.stop();
        }
    });

    $info.on('click', '.retry', function(){//重新上传按钮
        uploader.retry();
    });

    $info.on('click', '.ignore', function(){//忽略按钮
        alert('todo');
    });

    $upload.addClass('state-'+state);
    updateTotalProgress();

    //initServerFile();

});

