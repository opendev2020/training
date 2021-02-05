<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>权限管理</title>
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
	var vum;
	//var page = 1;

	$(document).ready(function() {
		vum = new Vue({
			el : "#app",
			data : {
				datas : []
			},
			methods : {
				showData : function() {
					simpleSearch('listPermissionsAjax','searchForm', vum);
				}
			}
		});
		vum.showData();

		$("#addForm").validate({
			submitHandler : function(form) {
				$(form).ajaxSubmit({
					dataType : "json",
					success : function(data) {
						alert(data.retMsg);
						$("#addModal").modal("hide");
						vum.showData();
					},
					error : function(data) {
						alert(data.status);
					}
				});
			}
		});
		
	});
	
	function simpleSearch(url, formid, vum) {
		$("#" + formid).ajaxSubmit({
			url : url,
			type : 'post',
			success : function(data) {
				vum.datas = data.result;
			},
			error : function(data) {
				alert('error');
			}
		});
	}

	function add() {
		fillForm({});
		$('#id').attr("readOnly", false);
		$('#addForm').attr("action", "addPermissionAjax");
		$('#addModalLabel').text("添加权限信息");
		$('#addModal').modal("toggle");
	}

	function del(id) {
		if (confirm("确定删除该权限吗？")) {
			$.ajax({
				url : "deletePermissionAjax",
				type : "POST",
				data : {
					id : id
				},
				success : function(data) {
					alert(data.retMsg);
					vum.showData();
				}
			});
		}
	}

	function load(data) {
		$('#id').attr("readOnly", true);
		$('#addForm').attr("action", "updatePermissionAjax");
		$('#addModalLabel').text("修改权限信息");
		$('#addModal').modal("toggle");
		fillForm(data);
	}

	function fillForm(obj) {
		$('#addForm input:text').val(function(index, value) {
			return obj[this.id];
		});
	}
</script>
</head>
<body>
	<div class="container-fluid" align="center">
		<div class="form-group" style="text-align: left">
			<form id="searchForm">
				<span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="权限代码">
				</span>
				<span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="权限名称">
				</span>
			</form>
			<button class="btn btn-primary"
				onClick="simpleSearch('listPermissionsAjax','searchForm', vum);">
				<span class="glyphicon glyphicon-search"></span>
			</button>
			<button class="btn btn-primary" onClick="add();">添加权限</button>
		</div>
		<div class="modal fade" id="addModal" tabindex="-1" role="dialog"
			aria-labelledby="addModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="addModalLabel">添加权限</h4>
					</div>
					<form id="addForm" method="post" action="" class="form-horizontal">
						<div class="modal-body">
							<div class="form-group">
								<label for="id" class="col-sm-3 control-label">权限代码</label>
								<div class="col-sm-9">
									<input type="text" name="id" id="id" class="form-control"
										placeholder="权限代码">
								</div>
							</div>
							<div class="form-group">
								<label for="name" class="col-sm-3 control-label">权限名称</label>
								<div class="col-sm-9">
									<input type="text" name="name" id="name" class="form-control"
										placeholder="权限名称">
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">关闭</button>
							<input type="submit" class="btn btn-primary" value="提交">
						</div>
					</form>
				</div>
			</div>
		</div>
		<div id="app">
			<table class="table table-hover">
				<tr>
					<th>权限代码</th>
					<th>权限名称</th>
					<th>操作</th>
				</tr>
				<tr v-for="data in datas">
					<td>{{data.id}}</td>
					<td>{{data.name}}</td>
					<td><button title="编辑权限信息"
							@click="load(data)">
							<span class="glyphicon glyphicon-edit"></span>
						</button>
						<button title="删除权限" @click="del(data.id);">
							<span class="glyphicon glyphicon-remove"></span>
						</button>
					</td>
			</table>
		</div>
	</div>
</body>
</html>