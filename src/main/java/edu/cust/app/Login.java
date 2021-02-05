package edu.cust.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="用户登录相关接口")
@Controller
public class Login {

	@PostMapping("/app/loginAjax")
	@ApiOperation("登录接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="username", value="用户名", defaultValue="test", required=true)
	})
	public String login(String username, String password) {
		return "json";
	}
}
