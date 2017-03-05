package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.portal.pojo.TbUser;
import com.taotao.portal.service.UserService;
/**
 * 用户管理service
 * @author chao
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;
	@Value("${SSO_USER_TOKEN}")
	private String SSO_USER_TOKEN;
	@Value("${SSO_PAGE_LOGIN}")
	public String SSO_PAGE_LOGIN;
	
	@Override
	public TbUser getUserByToken(String token) {
		//调用sso系统的服务，根据token获取用户信息
		String json = HttpClientUtil.doGet(SSO_BASE_URL + SSO_USER_TOKEN + token);
		//把json转换成TaoTaoResult
		TaotaoResult result = TaotaoResult.formatToPojo(json, TbUser.class);
		if(result.getStatus() == 200){
			TbUser user = (TbUser)result.getData();
			return user;
		}
		return null;
	}

}
