<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <title>首页</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="${pageContext.request.contextPath }/js/layui-v2.2.5/layui/css/layui.css"  media="all">
 </head>
<body>
	<div class="layui-container">
	    <h2>这个页面通过SpringMVC中配置的jsp视图解析器解析出路径:Controller中返回的ModelAndView中携带路径</h2>
	    <div>
		    <div style="top: 20px;">
			  <div class="layui-inline">
			    <input class="layui-input" id="search" autocomplete="off">
			  </div>
			  <button class="layui-btn search" data-type="reload">搜索</button>
			  <button class="layui-btn" id="add">添加</button>
			</div>
		    <table class="layui-table" lay-data="{ url:'${pageContext.request.contextPath }/sysUser/queryAllJson.do', page:true, id:'userTable',limits:[10,20,50,100],method:'post'}" lay-filter="sysUser">
			  <thead>
			    <tr>
			      <th lay-data="{type:'checkbox',fixed:'left'}"></th>
			      <th lay-data="{field:'id',sort: true, align:'center'}">ID</th>
			      <th lay-data="{field:'account', align:'center',sort: true}">账号名</th>
			      <th lay-data="{field:'username', align:'center',sort: true}">用户名</th>
			      <th lay-data="{field:'password', align:'center'}">密码</th>
			      <th lay-data="{field:'picFileId', align:'center'}">图片文件</th>
			      <th lay-data="{field:'detailsFileId', sort: true, align:'center'}">详细资料文件</th>
			      <th lay-data="{field:'id', align:'center', toolbar: '#cz'}">操作</th>
			    </tr>
			  </thead>
			</table>
		</div>
	</div>
	<script type="text/html" id="cz">
  		<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="get">查看</a>
  		<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
	</script>
	<script src="${pageContext.request.contextPath }/js/jquery-2.1.4.min.js" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath }/js/layui-v2.2.5/layui/layui.js" charset="utf-8"></script>
	<script src="${pageContext.request.contextPath }/js/layerUtils.js?<%=Math.random() %>" charset="utf-8"></script>
	<script>
		$(function(){
			$("#add").click(function(){
				parentOpenEdit('添加用户信息','${pageContext.request.contextPath }/sysUser/edit.do','1300px','700px');
			})
		})
		layui.use('table', function(){
		  var table = layui.table;
		  //监听表格复选框选择
		  table.on('checkbox(sysUser)', function(obj){
		    console.log(obj)
		  });
		  //监听工具条
		  table.on('tool(sysUser)', function(obj){
		    var data = obj.data;
		    if(obj.event === 'get'){
		    	parentOpenGet("查看信息",'${pageContext.request.contextPath }/sysUser/get.do?id='+data.id,'1300px','700px')
		    } else if(obj.event === 'del'){
		      layer.confirm('真的删除行么', function(index){
		    	//继续删除
		  		$.ajax({
					type : "post",
					url : '${pageContext.request.contextPath }/sysUser/del.do?ids='+data.id,
					success : function(res) {
						var json = eval("(" + res + ")");
						if(json=="success"){
							layer.alert('删除成功'); 
						}else{
							layer.alert('删除失败，请联系管理员'); 
						}
					}
				}); 
		  		active['reload'] ? active['reload'].call(this) : '';
		        layer.close(index);
		      });
		    } else if(obj.event === 'edit'){
		    	parentOpenEdit('编辑用户信息','${pageContext.request.contextPath }/sysUser/edit.do?id='+data.id,'1300px','700px');
		    }
		  });
		  table.on('sort(sysUser)', function(obj){ //排序事件
			  
			  var search = $('#search').val();
		        //执行重载
		        table.reload('userTable', {
		          page: {
		            curr: 1 //重新从第 1 页开始
		          }
		          ,where: {
		        	initSort: obj,
	            	search: search,
	            	sort:obj.field,//排序字段
	            	order:obj.type//排序方式
		          }
		        });
		  });
		  var $ = layui.$, active = {
		    getCheckData: function(){ //获取选中数据
		      var checkStatus = table.checkStatus('id')
		      ,data = checkStatus.data;
		      layer.alert(JSON.stringify(data));
		    }
		    ,getCheckLength: function(){ //获取选中数目
		      var checkStatus = table.checkStatus('id')
		      ,data = checkStatus.data;
		      layer.msg('选中了：'+ data.length + ' 个');
		    }
		    ,isAll: function(){ //验证是否全选
		      var checkStatus = table.checkStatus('id');
		      layer.msg(checkStatus.isAll ? '全选': '未全选')
		    }
		    ,reload: function(){
		        var search = $('#search').val();
		        //执行重载
		        table.reload('userTable', {
		          page: {
		            curr: 1 //重新从第 1 页开始
		          }
		          ,where: {
	            	search: search
		          }
		        });
		      }
		  };
		  
		  $('.search').on('click', function(){
		    var type = $(this).data('type');
		    active[type] ? active[type].call(this) : '';
		  });
		  $("#search").on('keyup',function(event){
			 if(event.keyCode == 13){
				 active['reload'] ? active['reload'].call(this) : '';
		    }
		  });
		});
	</script>
</body>
</html>
