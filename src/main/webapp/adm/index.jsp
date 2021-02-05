<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
<meta name="description" content="">
<meta name="author" content="">
<title>主界面</title>
<script type="text/javascript" src="../y/js/jquery.min.js"></script>
<link href="../y/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="../y/css/dashboard.css" rel="stylesheet" type="text/css" />
<style>
.nav-sidebar>li>ul>li>a {
	color: #428bca;
}

.nav-sidebar>li>ul>li>a:focus {
	color: #428bca;
	font-weight: bold;
}

.secondmenu {
	padding-left: 25px;
}
</style>
<script>
	
</script>
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">管理系统</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#a" data-src="changepasswd">修改密码</a></li>
					<li><a href="logout">退出</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div id="navbar" class="col-sm-3 col-md-2 col-lg-2 sidebar">
				<ul class="nav nav-sidebar" id="myTabs">
					<li class="active"><a href="#a" data-toggle="tab"
						data-src="welcome.jsp"><span class="glyphicon glyphicon-home"></span>
							首页</a></li>
					<shiro:hasPermission name="bmgl">
					<li><a href="#a" data-toggle="tab"
						data-src="orgs/listOrgs.jsp"><span
							class="glyphicon glyphicon-tree-conifer"></span> 部门管理</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="yhgl">
					<li><a href="#a" data-toggle="tab"
						data-src="users/listUsers.jsp"><span
							class="glyphicon glyphicon-user"></span> 用户管理</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="jsgl">
					<li><a href="#a" data-toggle="tab"
						data-src="roles/listRoles.jsp"><span
							class="glyphicon glyphicon-th-large"></span> 角色管理</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="qxgl">
					<li><a href="#a" data-toggle="tab"
						data-src="perms/listPerms.jsp"><span
							class="glyphicon glyphicon-th"></span> 权限管理</a></li>
					</shiro:hasPermission>

					<li><a href="#a" data-src="changepasswd" data-toggle="tab">修改密码</a></li>
					<li><a href="logout">退出</a></li>
				</ul>
			</div>
			<div
				class="col-md-12 col-sm-12 col-xs-12 col-lg-10 col-lg-offset-2 col-sm-offset-3 main">
				<div class="tab-pane active" id="a">
					<iframe src="welcome.jsp" frameborder="0" scrolling="auto"
						style="width: 100%; height: 80vh"></iframe>
				</div>
			</div>
		</div>
	</div>

	<script src="../y/js/jquery.min.js" type="text/javascript"></script>
	<script src="../y/js/jquery.form.min.js" type="text/javascript"></script>
	<script src="../y/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="../y/js/holder.min.js" type="text/javascript"></script>

	<script>
		$(function() {
			$('a')
					.click(
							function() {
								$("li").removeClass("active");
								$(this).parents("li").addClass("active");
								var src = $(this).attr('data-src');

								if (src === undefined) {//没有data-src的就是一级菜单
									var span = $(this).children("span:last");
									var cls = span.attr("class");
									console.log(cls);
									if (cls == "pull-right glyphicon glyphicon-chevron-down") {
										span
												.attr("class",
														"pull-right glyphicon glyphicon-chevron-right");
									} else if (cls == "pull-right glyphicon glyphicon-chevron-right") {
										span
												.attr("class",
														"pull-right glyphicon glyphicon-chevron-down");
									}
									return;
								}

								var paneId = $(this).attr('href');
								$(paneId + " iframe").attr("src", src);
								if ($(this).attr('data-toggle') === undefined) {
									return false;
								}
							});
		});
	</script>
</body>
</html>
