package com.taotao.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.sso.mapper.TbUserMapper;
import com.taotao.sso.pojo.TbUser;
import com.taotao.sso.pojo.TbUserExample;
import com.taotao.sso.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;
/**
 * 用户管理service
 * @author chao
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		//创建查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//对数据进行校验：1、2、3分别代表username、phone、email
		//用户名校验
		if(1==type){
			//用户名校验
			criteria.andUsernameEqualTo(content);
		}else if(2==type){
			//手机号检验
			criteria.andPhoneEqualTo(content);
		}else{
			//emai校验
			criteria.andEmailEqualTo(content);
		}
		List<TbUser> userList = userMapper.selectByExample(example);
		if(userList == null || userList.size()==0){
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}

}
