/**
 * 文件上传弹窗接口
 * @param parementObj文件上传所需参数
 * @param parementObj.type 文件上传的类型：img图片；chunk断点续传；其他：文件上传
 * @param parementObj.curUserId 用户id
 * @param parementObj.filesys 系统id
 * @param parementObj.filePrivateKey 系统私钥
 * @param parementObj.url文件回调页面
 */
function upLoadFile(parementObj){
	var upUrl ;
	if (parementObj.type == 'img') {
		upUrl = imgUploadUrl+'?curUserId='+curUserId+'&filesys='+filesys
		  +'&filePrivateKey='+filePrivateKey+'&groupId='+parementObj.groupId+'&groupPid='+parementObj.groupPid
		  +'&url='+parementObj.url;
	} else if (parementObj.type == 'chunk') {
		upUrl = chunkUploadUrl+'?curUserId='+curUserId+'&filesys='+filesys
		+'&filePrivateKey='+filePrivateKey+'&groupId='+parementObj.groupId+'&groupPid='+parementObj.groupPid
		+'&filter='+parementObj.filter+'&url='+parementObj.url;
	} else {
		upUrl = fileUploadUrl+'?curUserId='+curUserId+'&filesys='+filesys
		+'&filePrivateKey='+filePrivateKey+'&groupId='+parementObj.groupId+'&groupPid='+parementObj.groupPid
		+'&filter='+parementObj.filter+'&url='+parementObj.url;
	}
	layer.open({
	      type: 2,
	      title: parementObj.title||'文件上传',
	      shadeClose: true,
	      shade: false,
	      maxmin: true, //开启最大化最小化按钮
	      anim: 5,
	      area: parementObj.area || ['893px', '600px'],
	      moveOut: true,
	      btn: ['关闭'],
	      content: upUrl,
	      success: function(layero, index){
	    	  //利用sessionStorage存储上传文件弹窗的index
	    	  sessionStorage.setItem('upload_index', index);
    	  },
	      yes: function(index, layero){
	    	  layer.close(index);
	      }
	    });
}