package com.taotao.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.sso.service.UserService;

/**
 * 用户管理Controller
 * @author chao
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	//http://sso.taotao.com/user/check/{param}/{type}
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param,@PathVariable Integer type,String callback){
		TaotaoResult result = null;
		//参数有效性校验
		if(StringUtils.isBlank(param)){
			result.build(400, "校验内容不能为空");
		}
		if(type == null){
			result.build(400, "校验内容类型不能为空");
		}
		if(type!=1&&type!=2&&type!=3){
			result.build(400, "校验内容类型错误");
		}
		//校验出错
		if(result!=null){
			if(callback!=null){
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setJsonpFunction(callback);
				return mappingJacksonValue;
			}else{
				return result;
			}
		}
		//调用服务
		try {
			result = userService.checkData(param, type);
		} catch (Exception e) {
			result.build(500, ExceptionUtil.getStackTrace(e));
		}
		if (null != callback) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		} else {
			return result; 
		}
	}
}
