<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../../y/css/bootstrap.min.css">
<link rel="stylesheet" href="../../y/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="../../y/css/common.css">
<script type="text/javascript" src="../../y/js/jquery.min.js"></script>
<script type="text/javascript" src="../../y/js/jquery.form.min.js"></script>
<script type="text/javascript" src="../../y/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="../../y/js/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="../../y/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../y/js/vue.min.js"></script>
<script type="text/javascript" src="../../y/js/common.js"></script>
<script type="text/javascript">
	var vum;
	var page = 1;

	$(document).ready(function() {
		vum = new Vue({
			el : "#app",
			data : {
				datas : [],
				pages : []
			},
			methods : {
				showData : function(p) {
					page = p;
					search(1,'listAjax','searchForm', vum);
				}
			}
		});
		vum.showData(page);

		$("#addForm").validate({
			submitHandler : function(form) {
				$(form).ajaxSubmit({
					dataType : "json",
					success : function(data) {
						alert(data.retMsg);
						$("#addModal").modal("hide");
						vum.showData(page);
					},
					error : function(data) {
						alert(data.status);
					}
				});
			}
		});
		
		$("#importForm").validate({
			submitHandler : function(form) {
				var btn = loading($("#importForm input[type='submit']"))
				if(btn.isloading){
					return;
				}
				$(form).ajaxSubmit({
					dataType : "json",
					success : function(data) {
						alert(data.retMsg);
						$("#importModal").modal("hide");
						vum.showData(page);
						unloading(btn);
					},
					error : function(data) {
						alert(data.status);
						unloading(btn);
					}
				});
			}
		});
		
	});

	function add() {
		fillForm({});
		$('#username').attr("readOnly", false);
		$('#addForm').attr("action", "addAjax");
		$('#addModalLabel').text("添加用户信息");
		$('#addModal').modal("toggle");
	}

	function del(id) {
		if (confirm("确定删除该用户吗？")) {
			$.ajax({
				url : "deleteAjax",
				type : "POST",
				data : {
					username : id
				},
				success : function(data) {
					alert(data.retMsg);
					vum.showData(page);
				}
			});
		}
	}
	
	function openImportForm() {
		$('#importForm').attr("action", "importAjax");
		$('#importModalLabel').text("导入用户");
		$('#importModal').modal("toggle");
	}

	function load(data) {
		$('#username').attr("readOnly", true);
		$('#addForm').attr("action", "updateAjax");
		$('#addModalLabel').text("修改用户信息");
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
				<input type="hidden" name="columns" value="c_username">
				<input type="hidden" name="columns" value="c_name">
				<input type="hidden" name="operators" value="like">
				<input type="hidden" name="operators" value="like">
				<input type="hidden" name="orders" value="none">
				<input type="hidden" name="orders" value="none">
				<input type="hidden" name="logicalopts" value="">
				<input type="hidden" name="logicalopts" value="and">
				<span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="用户名">
				</span>
				<span class="col-md-2">
				<input type="text" name="values" class="form-control" placeholder="姓名">
				</span>
			</form>
			<button class="btn btn-primary"
				onClick="search(1,'listAjax','searchForm', vum);">
				<span class="glyphicon glyphicon-search"></span>
			</button>
			<button class="btn btn-primary" onClick="add();">添加用户</button>
			<button class="btn btn-primary" onClick="openImportForm()"><span class="glyphicon glyphicon-import"></span>导入用户</button>
			<button class="btn btn-primary" onClick="location.href='export'"><span class="glyphicon glyphicon-export"></span>导出用户</button>
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
						<h4 class="modal-title" id="addModalLabel">添加用户</h4>
					</div>
					<form id="addForm" method="post" action="" class="form-horizontal">
						<div class="modal-body">
							<div class="form-group">
								<label for="username" class="col-sm-3 control-label">用户名</label>
								<div class="col-sm-9">
									<input type="text" name="username" id="username" class="form-control"
										placeholder="用户名">
								</div>
							</div>
							<div class="form-group">
								<label for="name" class="col-sm-3 control-label">姓名</label>
								<div class="col-sm-9">
									<input type="text" name="name" id="name" class="form-control"
										placeholder="姓名">
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
		
		<div class="modal fade" id="importModal" tabindex="-1" role="dialog"
			aria-labelledby="importModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="importModalLabel">导入用户</h4>
					</div>
					<form id="importForm" method="post" action="" enctype="multipart/form-data" class="form-horizontal">
						<div class="modal-body">
							<div class="form-group">
								<label for="file" class="col-sm-3 control-label">Excel文件</label>
								<div class="col-sm-9">
									<input type="file" name="file" id="file" class="form-control">
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
					<th>用户名</th>
					<th>姓名</th>
					<th>操作</th>
				</tr>
				<tr v-for="data in datas">
					<td>{{data.username}}</td>
					<td>{{data.name}}</td>
					<td><button title="编辑用户信息"
							@click="load(data)">
							<span class="glyphicon glyphicon-edit"></span>
						</button>
						<button title="删除用户" @click="del(data.username);">
							<span class="glyphicon glyphicon-remove"></span>
						</button>
					</td>
			</table>
			<span>总数量：{{pages.rowCount}}</span> <span>总页数：{{pages.lastPage}}</span>
			<button class="btn btn-default btn-sm"
				@click="showData(pages.firstPage)">
				<span class="glyphicon glyphicon-backward"></span>
			</button>
			<button class="btn btn-default btn-sm"
				@click="showData(pages.prePage)">
				<span class="glyphicon glyphicon-chevron-left"></span>
			</button>
			<span>当前页：{{pages.pageNum}}</span>
			<button class="btn btn-default btn-sm"
				@click="showData(pages.nextPage)">
				<span class="glyphicon glyphicon-chevron-right"></span>
			</button>
			<button class="btn btn-default btn-sm"
				@click="showData(pages.lastPage)">
				<span class="glyphicon glyphicon-forward"></span>
			</button>
		</div>
	</div>
</body>
</html>