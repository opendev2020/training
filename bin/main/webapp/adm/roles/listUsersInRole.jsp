<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>用户角色管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${ctx}/y/css/bootstrap.min.css">
<link rel="stylesheet" href="${ctx}/y/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="${ctx}/y/css/common.css">
<script type="text/javascript" src="${ctx}/y/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/jquery.form.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/vue.min.js"></script>
<script type="text/javascript" src="${ctx}/y/js/common.js"></script>
<script type="text/javascript">
	var vum1;
	var vum2;

	$(document).ready(function() {
		vum1 = new Vue({
			el : "#app1",
			data : {
				datas : []
			},
			methods : {
				showData : function() {
					$.ajax({
						url : "listUsersNotInRoleAjax",
						type : "POST",
						data : {rid : '${role.id}'},
						success : function(data) {
							vum1.datas = data.result;
						}
					})
				}
			}
		});
		vum1.showData();
		vum2 = new Vue({
			el : "#app2",
			data : {
				datas : []
			},
			methods : {
				showData : function() {
					$.ajax({
						url : "listUsersInRoleAjax",
						type : "POST",
						data : {rid : '${role.id}'},
						success : function(data) {
							vum2.datas = data.result;
						}
					})
				}
			}
		});
		vum2.showData();
	});
	
	function checkAll(self){
		var flag = $(self).prop("checked");
		$(self).parents("form").find(":checkbox").each(function(){
			$(this).prop("checked", flag);
		});
	}

	function add() {
		$("#form-app1").ajaxSubmit({
			success : function(data) {
				$("#form-app1").find(":checkbox").prop("checked", false);
				alert(data.retMsg);
				vum1.showData();
				vum2.showData();
			},
			error : function(data) {
				alert(data.status)
			}
		});
	}

	function del() {
		$("#form-app2").ajaxSubmit({
			success : function(data) {
				$("#form-app2").find(":checkbox").prop("checked", false);
				alert(data.retMsg);
				vum1.showData();
				vum2.showData();
			},
			error : function(data) {
				alert(data.status)
			}
		});
	}
</script>
</head>
<body>
	<div class="container-fluid" align="center">
		<p align="left">
			<a href="listRoles.jsp">所有角色</a>>>
			<a href="listUsersInRole?rid=${role.id}">${role.name }</a>
			<button class="btn btn-primary" onClick="add();">添加用户</button>
			<button class="btn btn-primary" onClick="del();">删除用户</button>
		</p>
		<div id="app1" class="col-sm-6">
			<p>候选用户</p>
			<form id="form-app1" action="addUsersToRoleAjax" method="post">
			<input type="hidden" name="rid" value="${role.id }">
			<table class="table table-hover">
				<tr>
					<th><input type="checkbox" onclick="checkAll(this)">全选</th>
					<th>用户名</th>
					<th>姓名</th>
				</tr>
				<tr v-for="data in datas">
					<td><input type="checkbox" name="usernames" :value="data.username"></td>
					<td>{{data.username}}</td>
					<td>{{data.name}}</td>
			</table>
			</form>
		</div>
		<div id="app2" class="col-sm-6">
			<p>已选用户</p>
			<form id="form-app2" action="delUsersFromRoleAjax" method="post">
			<input type="hidden" name="rid" value="${role.id }">
			<table class="table table-hover">
				<tr>
					<th><input type="checkbox" onclick="checkAll(this)">全选</th>
					<th>用户名</th>
					<th>姓名</th>
				</tr>
				<tr v-for="data in datas">
					<td><input type="checkbox" name="usernames" :value="data.username"></td>
					<td>{{data.username}}</td>
					<td>{{data.name}}</td>
			</table>
			</form>
		</div>
	</div>
</body>
</html>