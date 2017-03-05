package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.utils.CookieUtils;
import com.taotao.portal.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;

public class LoginIntercepter implements HandlerInterceptor {

	@Autowired
	private UserServiceImpl userService;
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// 返回ModelAndView之后
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2, ModelAndView arg3) throws Exception {
		//在Handle执行之后，返回ModelAndView之前
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		//在Handle执行之前处理
		//1、判断用户是否登陆  2、从cookie中取token 3、根据token换取用户信息 
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		TbUser user = userService.getUserByToken(token);
		if(null == user){
			response.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect="+request.getRequestURL());
			//返回false
			return false;
		}
		//4、取不到用户信息 跳转到登录页面，把用户请求的url作为参数传递给登录页面
		//5、取到用户信息 直接过 放行
		//返回值决定Handle是否执行 true执行 false不执行
		return true;
	}

	

}
