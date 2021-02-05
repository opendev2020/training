<%--
  Created by IntelliJ IDEA.
  User: qh
  Date: 2018/6/2
  Time: 15:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.cust.login.*"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>管理员登录</title>

    <!-- Bootstrap core CSS -->
    <link href="../y/css/bootstrap.min.css" rel="stylesheet">

    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <link href="../y/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="../y/css/signin.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../js/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="../y/js/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="../y/js/html5shiv.min.js"></script>
    <script src="../y/js/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="container">

		<form action="" method="post" class="form-signin">
			<h2 class="form-signin-heading">管理员登录</h2>
			<%
				Exception ex = (Exception) request.getAttribute("shiroLoginFailure");
				if (ex != null) {
			%>
			<div class="alert alert-danger">
				<%=ex instanceof MyAuthenticationException ? ex.getMessage() : "登录验证失败！"%>
			</div>
			<%
				}
			%>
			<label for="username" class="sr-only">用户名</label>
			<input name="username" type="text" id="username" class="form-control" placeholder="用户名" required autofocus>
			<label for="password" class="sr-only">密码</label>
			<input name="password" type="password" id="password" class="form-control" placeholder="密码" required>
			<input name="rememberMe" type="checkbox" id="rememberMe" value="true">
			<label for="rememberMe">记住我</label>
			<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
		</form>

	</div> <!-- /container -->


<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
<script src="../y/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
