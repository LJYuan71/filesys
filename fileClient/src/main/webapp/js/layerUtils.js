//顶级页面弹出框-编辑页面,可以设置下方保存按钮的值
function parentOpenEditButtonname(title,url,width,height,buttonname){
	parent.layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content:url,
		btn: [buttonname, '关闭'],
		yes: function(index, layero){
			var body = parent.layer.getChildFrame('body', index);
			body.find('#submit').click();
		}
	});
}
//顶级页面弹出框-编辑页面
function parentOpenEdit(title,url,width,height){
	window.top.layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content:url,
		btn: ['保存', '关闭'],
		yes: function(index, layero){
			var body = parent.layer.getChildFrame('body', index);
			body.find('#save').click();
		}
	});
}

//顶级页面弹出框-明细页面
function parentOpenGet(title,url,width,height){
	window.top.layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content: url,
		btn: ['关闭']
	}); 
}
//顶级页面弹出框-关闭
function parentEditCloseWin(){
	/*if($("#workframe",window.parent.document).contents().find("#find").attr("id")==undefined){
		parent.$('#table').bootstrapTable('refresh', null);
    	var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
  		parent.layer.close(index);
	}else{
		$("#workframe",window.parent.document).contents().find("#find").click();
	    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
	  	parent.layer.close(index);
	}*/
	var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.close(index);
	parent.$('.search').click();
}
//当前页面弹出框-编辑页面,可以设置下方保存按钮的值
function openEditButtonname(title,url,width,height,buttonname){
	layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content:url,
		btn: [buttonname, '关闭'],
		yes: function(index, layero){
			var body =layer.getChildFrame('body', index);
			body.find('#submit').click();
		}
	});
}
//当前页面弹出框-编辑页面
function openEdit(title,url,width,height){
	layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content:url,
		btn: ['保存', '关闭'],
		yes: function(index, layero){
			var body =layer.getChildFrame('body', index);
			body.find('#submit').click();
		}
	});
}
//当前页面弹出框-明细页面
function openGet(title,url,width,height){
	layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content: url,
		btn: ['关闭']
	}); 
}

//顶级页面弹出框-明细页面--页面销毁，返回时刷新table
function openGetBackRefresh(title,url,width,height){
	parent.layer.open({
		type: 2,
		title: title,
		//maxmin: true,
		shadeClose: true,
		shade: 0.8,
		area: [width, height],
		content: url,
		btn: ['关闭'],
		end: function(){//无论是确认还是取消，只要层被销毁了，end都会执行，不携带任何参数。
			$("#table").bootstrapTable("refresh");
		}
	}); 
}