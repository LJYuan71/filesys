/**
 * 以下配置文件缺一不可，对于文件系统可以自定义实现的js
 */
//var curUserId = '';
//var filePrivateKey = '';
//var filesys = '';
//var fileDownLoadUrl = '';
//var fileDelUrl = '';
//var fileGroupDelUrl = '';
//var queryFilesUrl = ''
Date.prototype.Format = function (fmt) {  
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
//将一个表单的数据返回成JSON对象 
$.fn.serializeObject = function() {	
  var o = {};  
  var a = this.serializeArray();  
  $.each(a, function() {  
    if (o[this.name]) {  
      if (!o[this.name].push) {  
        o[this.name] = [ o[this.name] ];  
      }  
      o[this.name].push(this.value || '');  
    } else {  
      o[this.name] = this.value || '';  
    }  
  });  
  return o;  
};
//处理null及undefined
function dealNull(value){
	if(value == undefined || value == null){
		return "";
	}
	return value;
}
/*
 * 文件上传后保存本地数据库
 * 参数：1.fArrray: 文件服务器返回数组对象
 *     2.groupGuid: 文件组id
 *     2.groupGuid: 文件组pid
 * 返回值：object对象：
 *          code:状态 200：成功 其他：失败
 *          groupId:文件所在组id
 *          groupPid:文件所在组pid
 */
function saveAttachFiles(fileArray, groupId, groupPid){
	var retObj = {};
	//baseUrl+
	
	//是否保存成功
	var flag = false;
	try{
		//定义对象
		var vo = {};
		var fileAry = new Array();
		$.each(fileArray, function(i, item){  
			var file = {};
			file["groupId"] = groupId || "";
			file["groupPid"] = groupPid || "";
			file["fileId"] = item.fileId;
			file["title"] = item.fileName;
			file["length"] = item.length;
			file["md5"] = item.fileMd5;
			file["uploadTime"] = item.uploadTime;
			fileAry.push(file);
		});
		vo["fileGroups"] = fileAry;
		var jstr = JSON.stringify(fileAry);
		$.ajax({
			async: false,
            cache: false,
			type : 'POST',  
 			contentType : 'application/json', 
 			dataType : 'json', 
 			data: jstr,
 			url: ctx+'/filegroup/saveList.do',
 			success : function(data) {  
 				if(data.code == 200){
 		    		flag = true;
 		    	}else{
 		    		flag = false;
 		    	}
 		    },  
 		   	error : function(data) {  
 		   		flag = false;
 	        }  
 		});
	}catch(e){
		console.error(e);
		flag = false;
	}
	
	retObj["status"] = flag;
	retObj["groupId"] = groupId;
	retObj["groupPid"] = groupPid;
	return retObj;
}
/**
 * 删除单个文件
 * @param fileId 文件id
 */
function deleteFileById(fileId){
	deleteAttachFile(fileId);
}
/**
 * 批量删除文件组
 * @param groupId 文件组id
 */
function deleteGroupFile(groupId){
	deleteAttachFile(null,groupId);
}
/**
 * 删除全部文件组
 * @param groupPid 文件组上级id
 */
function deletePgroupFile(groupPid){
	deleteAttachFile(null,null,groupPid);
}
/**
 * 删除文件
 * @param fileId 单个文件
 * @param groupId 批量组文件
 * @param groupPid 批量组上级文件
 */
function deleteAttachFile(fileId, groupId, groupPid){
	if (!fileId && !groupId && !groupPid) {
		return;
	}
	if(!fileDelUrl && !fileGroupDelUrl){
		return;
	}
	var url = fileDelUrl;
	if (fileId) {
		url = url + "&fileId="+fileId;
	} else if (groupId){
		url = fileGroupDelUrl + "&fileId="+fileId || ''+"&groupId="+groupId || ''+"&groupPid="+groupPid || '';
	} else if (groupPid){
		url = fileGroupDelUrl + "&fileId="+fileId || ''+"&groupId="+groupId || ''+"&groupPid="+groupPid || '';
	}
	var content = "";
	try{
		//单个文件时提醒，多个文件时不卡业务流程
		if(fileId){
			layer.confirm('是否确定删除？', {
				  btn: ['确定','取消'] //按钮
				}, function(index){
					delFiles(url,fileId);
					layer.close(index);
				});
		}else{
			delFiles(url);
		}
	}catch(e){
		console.error(e);
	}
}
//文件删除
function delFiles(url,fileId){
	$.ajax({  
        type : "GET",  
        async:false,  
        url: url,
        dataType : "jsonp",//数据类型为jsonp  
        jsonp: "jsonpCallback",//服务端用于接收callback调用的function名的参数  
        jsonpCallback:"jsonpCallback",
        timeout: 5000,
        success : function(data){  
        	if(data.code == 200 || data.code == "200"){ //删除成功
        		//删除成功后的业务,需要自己的业务区具体实现
        		delFileCalback(url.substring(url.indexOf("?")+1));
        		//页面删除对应文件
        		if (fileId) {
        			$("tr[fileId="+fileId+"]").remove();
        			updateRowNumber();
        		}
        	}else{
        		console.warn(data);
        		layer.alert("删除失败!", {icon: 2});
        	}
        },  
        error:function(XMLHttpRequest, textStatus, errorThrown){  
        	console.error(XMLHttpRequest);
        	layer.alert("删除失败!", {icon: 2});
        }
    });
}
function updateRowNumber(){
	$(".file_index").each(function(index,Obj){
		$(this).text(index+1);
	})
}
//删除成功后的业务,需要自己的业务区具体实现,这里不再实现
function delFileCalback(url){
	var fileId = getUrlParam("fileId",url);
	var groupId = getUrlParam("groupId",url);
	var groupPid = getUrlParam("groupPid",url);
	//删除本系统数据
	$.ajax({
		async: false,
        cache: false,
		type : 'POST',  
		dataType : 'json', 
		data:{fileId:fileId,groupId:groupId,groupPid:groupPid},
		url: ctx+'/filegroup/del.do',
		success : function(data) {  
			if(data.code == 200){
	    		flag = true;
	    	}else{
	    		flag = false;
	    		layer.alert("删除失败!", {icon: 2});
	    	}
	    },  
	   	error : function(data) { 
	   		flag = false;
	   		layer.alert("删除失败!", {icon: 2});
        }  
	});
	return flag;
}
/**
 * 根据文件组id获取文件列表
 * @param groupGuid 文件组id
 * @param showType 显示类别(table/other)
 * @param isShowCreateTime 是否显示上传时间
 * @param isShowSize 是否显示大小
 * @param isShowDel 是否显示删除按钮
 * @returns retObj对象
 * retObj.content拼接html内容
 * retObj.count文件个数
 */
function queryAttachFiles(groupGuid,showType,isShowCreateTime,isShowSize,isShowDel){
	isShowCreateTime = isShowCreateTime == null ? true : isShowCreateTime;
	var retObj = {};
	retObj["content"] = "";
	retObj["count"] = 0;
	
	if(groupGuid == undefined || groupGuid == "undefined" || !fileDownLoadUrl || !queryFilesUrl){
		return retObj;
	}
	try{
		$.ajax({
	        async: false,
	        cache: false,
	        type : "get",  
	        //dataType:'jsonp',
	        dataType:'json',
	        data: {
	        	"groupId" : groupGuid
	        },
	        //jsonp: "jsonpCallback",  
	        //jsonpCallback:"jsonpCallback",
	        url: ctx+'/filegroup/queryAllJson.do' ,
	        success: function(data) {
	        	if (data.code == 200) {
	        		var retObjTmp = createViewHtmlForLocal(data.data,showType,isShowCreateTime,isShowSize,isShowDel);
	        		retObj["content"] = retObjTmp.content;
	        		retObj["count"] = retObjTmp.count;
				}else{
					console.warn(data);
					retObj["content"] = "";
				}
	        },
	        error : function(data) {  
	        	console.error(data);
	        	retObj["content"] = "";
	        }
		});
	}catch(e){
		console.error(e);
		retObj["content"] = "";
	}
	return retObj;
}

/**
 * 
 * @param rows 文件列表数据
 * @param showType 显示的类型，支持table和非table
 * @param isShowCreateTime 是否显示创建时间
 * @param isSize 是否显示文件大小
 * @param isShowDel 是否可以删除
 * @returns retObj对象
 * retObj.content拼接html内容
 * retObj.count文件个数
 */
function createViewHtmlForLocal(rows,showType,isShowCreateTime,isShowSize,isShowDel){
	var str = "";
	var count = 0;
	if(showType == 'table'){  //table形式
		str = str + '<table>';
		str = str + '  <thead><tr>';
		str = str + '    <th width="10%" class="file_rownumber">序号</th>';
		str = str + '    <th width="40%">文件名</th>';
		if(isShowSize == true || isShowSize == 'true') {
			str = str + '    <th width="15%">文件大小</th>';
		}
		if(isShowCreateTime == true || isShowCreateTime == 'true') {
			str = str + '    <th width="25%">上传时间</th>';
		}
		if(isShowDel == true || isShowDel == 'true'){
			str = str + '    <th width="10%">操作</th>';
		}
		str = str + '</tr></thead>';
		$.each(rows, function(i, item){
			str = str + '<tr fileId="'+item.fileId+'">';
			str = str + '<td class="file_rownumber file_index">'+ eval(i+1) +'</td>';
			str = str + '<td class="left"><a href="'+ fileDownLoadUrl + item.fileId +'" title="点击下载"><span style="color: blue;">'+ item.title +'</span></a></td>';
			if(isShowSize == true || isShowSize == 'true') {
				str = str + '<td>'+ fileSizeInfo(item.length) +'</td>';
			}
			if(isShowCreateTime == true || isShowCreateTime == 'true') {
				str = str + '<td>'+ new Date(item.uploadTime).Format("yyyy-MM-dd hh:mm") +'</td>';
			}
			if(isShowDel == true || isShowDel == 'true'){
				if ((item.canDel == 1 || item.canDel == '1') && fileDelUrl) {
					str = str + '<td><a class="file_'+item.fileId+'" href="javascript:void(0)" onclick="deleteAttachFile(\''+ item.fileId +'\')">删除</a></td>';
				} else {
					str = str + '<td><a class="file_'+item.fileId+'" href="javascript:void(0)" style="color: #ccc;">删除</a></td>';
				}
			}
			str = str + '</tr>';
			count++;
	    });
		str = str + '</table>';
	}else{
		$.each(rows, function(i, item){  
			str = str + '<a href="'+  fileDownLoadUrl + item.fileId +'" class="file_'+item.fileId+'"><span style="color: blue;">'+ dealNull(item.title) +'</span></a>';
			if(isShowDel == true || isShowDel == 'true'){
				if ((item.canDel == 1 || item.canDel == '1') && fileDelUrl) {
					str = str + '<span style="margin-left: 15px;"><a class="file_'+item.fileId+'" href="javascript:void(0)" onclick="deleteAttachFile(\''+ item.fileId +'\')">删除</a></span>';
				} else {
					str = str + '<span style="margin-left: 15px;"><a class="file_'+item.fileId+'" href="javascript:void(0)" style="color: #ccc;">删除</a></span>';
				}
			}
			str = str + '</br>';
			count++;
		});
		
	}
	var retObj = {};
	retObj["content"] = str;
	retObj["count"] = count;
	return retObj;
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

function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
    s[8] = s[13] = s[18] = s[23] = "-";
 
    var uuid = s.join("");
    return uuid;
}

function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

function getUrlParam(name,url){  
    var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"); 
    if(!url){
    	url = location.href;
    }
    if (reg.test(url)) return decodeURIComponent(RegExp.$2.replace(/\+/g, " ")); return "";  
}; 
