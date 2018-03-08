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
/*document.write("<script src='"+filesysUrl+"/js/layer-v3.1.1/layer/layer.js'></script>");*/
Date.prototype.Format = function (fmt) { //author: meizz 
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
 *     2.groupGuid: 文件组Guid (如果groupGuid值为空则新创建组并保存文件在该组下，如果groupGuid有值则保存文件在传入的组中)
 *     3.数据库标识（BJ:报建库  ZS:正式库）
 * 返回值：object对象：
 *          status:状态   （true：成功   false:失败）
 *          groupGuid:文件所在组Guid
 */
 /**
function saveAttachFiles(fArrray, groupGuid, dbFlag){
	var retObj = {};
	
	var usrStr = "";
	if(dbFlag == "BJ"){
		usrStr = baseUrl+'/filegroup/addListBJ.do';
	}else if(dbFlag == "ZS"){
		usrStr = baseUrl+'/filegroup/addListZS.do';
	}else{
		retObj["status"] = false;
		return retObj;
	}
	
	//是否保存成功
	var flag = false;
	try{
		//定义对象
		var vo = {};
		var fileArray = new Array();
		$.each(fArrray, function(i, item){  
			var file = {};
			file["groupGuid"] = groupGuid || "";
			file["attachGuid"] = item.fileId;
			file["attachName"] = item.fileName;
			file["attachUploadTime"] = item.uploadTime;
			file["attachMD5"] = item.fileMd5;
			fileArray.push(file);
		});
		vo["groupList"] = fileArray;
		vo["groupGuid"] = groupGuid || "";
		var jstr = JSON.stringify(vo);
		$.ajax({
			async: false,
            cache: false,
			type : 'POST',  
 			contentType : 'application/json', 
 			dataType : 'json', 
 			data: jstr,
 			url: usrStr,
 			success : function(data) {  
 				if(isAjaxSuccess(data)){
 		    		flag = true;
 		    		groupGuid = data.groupGuid;
 		    	}else{
 		    		flag = false;
 		    	}
 		    },  
 		   	error : function(data) {  
 		   		flag = false;
 	        }  
 		});
	}catch(e){
		flag = false;
	}
	
	retObj["status"] = flag;
	retObj["groupGuid"] = groupGuid;
	return retObj;
}
*/
/**
 * 删除单个文件
 * @param fileId 文件id
 */
 /**
function deleteFileById(fileId){
	deleteAttachFile(fileId);
}
*/
/**
 * 批量删除文件组
 * @param groupId 文件组id
 */
 /**
function deleteGroupFile(groupId){
	deleteAttachFile(null,groupId);
}
*/
/**
 * 删除全部文件组
 * @param groupPid 文件组上级id
 */
 /**
function deletePgroupFile(groupPid){
	deleteAttachFile(null,null,groupPid);
}
*/
/**
 * 删除文件
 * @param fileId 单个文件
 * @param groupId 批量组文件
 * @param groupPid 批量组上级文件
 */
 /**
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
			layer.confirm('您是如何看待前端开发？', {
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
*/
//文件删除
/**
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
        			$(".file_"+fileId).remove();
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
*/
/**
function updateRowNumber(){
	$(".file_index").each(function(index,Obj){
		$(this).text(index+1);
	})
}
*/
//删除成功后的业务,需要自己的业务区具体实现,这里不再实现
/**
function delFileCalback(delId){
	console.log("请重写删除后的回调函数："+delId);
}
*/
/**
 * 根据文件组id获取文件列表
 * @param groupGuid 文件组id
 * @param showType 显示类别(table/other)
 * @param idObj 赋值的id对象$("#id")
 * @param isShowCreateTime 是否显示上传时间
 * @param isShowSize 是否显示大小
 * @param isShowDel 是否显示删除按钮
 * @param isShowLs 是否显示历史文件
 * @param isPDF 是否是pdf文件
 * @returns retObj对象
 * retObj.content拼接html内容
 * retObj.count文件个数
 */
 /**
function queryAttachFiles(groupGuid,showType,idObj,isShowCreateTime,isShowSize,isShowDel,isShowLs,isPDF){
	if(groupGuid == undefined || groupGuid == "undefined" || !fileDownLoadUrl || !queryFilesUrl){
		return;
	}
	isShowCreateTime = isShowCreateTime == null ? true : isShowCreateTime;
	isShowLs = isShowLs == true ? true : false;
	var retObj = {};
	retObj["content"] = "";
	retObj["count"] = 0;
	
	try{
		$.ajax({
	        async: false,
	        cache: false,
	        type : "get",  
	        dataType:'jsonp',
	        data: {
	        	"groupId" : groupGuid,
	        	"showLs" : isShowLs
	        },
	        jsonp: "jsonpCallback",  
	        jsonpCallback:"jsonpCallback",
	        url: queryFilesUrl ,
	        success: function(data) {
	        	if (data.code == 200) {
	        		var retObjTmp = createViewHtmlForLocal(data.data,showType,isShowCreateTime,isShowSize,isShowDel,isShowLs,isPDF);
	        		retObj["content"] = retObjTmp.content;
	        		if(idObj){
	        			idObj.html(retObjTmp.content);
	        		}
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
*/
/**
 * 
 * @param rows 文件列表数据
 * @param showType 显示的类型，支持table和非table
 * @param isShowCreateTime 是否显示创建时间
 * @param isSize 是否显示文件大小
 * @param isShowDel 是否可以删除
 * @param isShowLs 是否显示历史文件
 * @param isPDF 是否pdf
 * @returns retObj对象
 * retObj.content拼接html内容
 * retObj.count文件个数
 */
 /**
 function createViewHtmlForLocal(rows,showType,isShowCreateTime,isShowSize,isShowDel,isShowLs,isPDF){
	var str = "";
	var count = 0;
	if(showType == 'table'){  //table形式
		str = str + '<table>';
		str = str + '  <thead><tr>';
		str = str + '    <th width="10%" class="file_rownumber">序号</th>';
		str = str + '    <th width="40%">文件名</th>';
		if(isShowSize == true || isShowSize == 'true') {
			str = str + '    <th width="20%">文件大小</th>';
		}
		if(isShowCreateTime == true || isShowCreateTime == 'true') {
			str = str + '    <th width="20%">上传时间</th>';
		}
		if(isShowDel == true || isShowDel == 'true'){
			str = str + '    <th width="10%">操作</th>';
		}
		str = str + '</tr></thead>';
		$.each(rows, function(i, item){  
			str = str + '<tr class="file_'+item.fileId+'">';
			str = str + '<td class="file_rownumber file_index">'+ eval(i+1) +'</td>';
			if(!isPDF){
				str = str + '<td class="left"><a href="'+ fileDownLoadUrl + item.fileId +'" title="点击下载"><span style="color: blue;">'+ item.title +'</span></a></td>';
			}else{
				str = str + '<td><a href="javascript:void(0);" onclick="yuLanPDF(\''+ item.fileId +'\')" title="点击预览"><span style="color: blue;">'+ dealNull(item.attachName) +'</span></a></td>';
			}
			if(isShowSize == true || isShowSize == 'true') {
				str = str + '<td>'+ (item.length/1000.0).toFixed(2) +'K</td>';
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
*/

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
