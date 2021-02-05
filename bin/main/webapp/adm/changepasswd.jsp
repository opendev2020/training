<!-- https://bootsnipp.com/snippets/d0A1k -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>修改密码</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="../y/css/bootstrap.min.css">
<link rel="stylesheet" href="../y/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="../y/css/common.css">
<script type="text/javascript" src="../y/js/jquery.min.js"></script>
<script type="text/javascript" src="../y/js/jquery.form.min.js"></script>
<script type="text/JavaScript" src="../y/js/jquery.validate.min.js"></script>
<script type="text/JavaScript" src="../y/js/jquery.validate.ext.js"></script>
<script type="text/javascript" src="../y/js/jquery.placeholder.min.js"></script>
<script type="text/javascript" src="../y/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../y/js/common.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#passwd").validate({
			rules:{
				piNewPass : {
					required : true
				},
				piNewPassRepeat : {
					required : true,
					equalTo : '#piNewPass'
				}
            },
            highlight: function ( element, errorClass, validClass ) {
                $( element ).parent().addClass( "has-error" ).removeClass( "has-success" );
            },
            unhighlight: function (element, errorClass, validClass) {
                $( element ).parent().addClass( "has-success" ).removeClass( "has-error" );
            },
            submitHandler: function (form) {
                $(form).ajaxSubmit({
    				url : 'changepasswdAjax',
    				type : 'post',
    				success : function(data) {
    					alert(data.retMsg);
    					/* window.location.reload(); */
    				},
    				error : function() {
    					alert('error');
    				}
    			});
                //alert( "submitted!" );
            }
		});
	});
</script>
<style>
body {
	margin-top: 20px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<form class="form-horizontal" id="passwd">
			<fieldset>
				<legend>修改密码</legend>
				<div class="form-group">
					<label class="col-md-4 control-label" for="piNewPass">新密码</label>
					<div class="col-md-4">
						<input id="piNewPass" name="piNewPass" type="password" class="form-control input-md">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-4 control-label" for="piNewPassRepeat">确认密码</label>
					<div class="col-md-4">
						<input id="piNewPassRepeat" name="piNewPassRepeat" type="password" class="form-control input-md">
					</div>
				</div>
				<div class="form-group">
					<label class="col-md-4 control-label" for="bCancel"></label>
					<div class="col-md-8">
						<button type="reset" class="btn btn-danger">取消</button>
						<button type="submit" class="btn btn-success">提交</button>
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</body>
</html>