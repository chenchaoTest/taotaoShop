package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.sso.dao.JedisClient;
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
	@Autowired
	private JedisClient jedis;
	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	
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

	@Override
	public TaotaoResult createUser(TbUser user) {
		user.setUpdated(new Date());
		user.setCreated(new Date());
		//md5加密
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		userMapper.insert(user);
		return TaotaoResult.ok();
	}
	/**
	 * 用户登陆
	 */
	@Override
	public TaotaoResult userLogin(String username, String password,HttpServletRequest request,HttpServletResponse response) {
		//查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		//如果没有次用户名
		if(null == list || list.size() == 0 ){
			return TaotaoResult.build(400, "用户名和密码错误");
		}
		TbUser user = list.get(0);
		//对比密码
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return TaotaoResult.build(400, "用户名和密码错误");
		}
		//生成token
		String token = UUID.randomUUID().toString();
		//保存用户之前,把用户对象的密码清空
		user.setPassword(null);
		//把用户信息写入redis
		jedis.set(REDIS_USER_SESSION_KEY+":"+token, JsonUtils.objectToJson(user));
		//设置过期时间
		jedis.exprie(REDIS_USER_SESSION_KEY+":"+token,SSO_SESSION_EXPIRE);
		
		//添加cookies的逻辑 有效期 是关闭浏览器就失效 ，也可以设置cookie有效期
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		//返回token
		return TaotaoResult.ok(token);
	}

	/**
	 * 通过token查询用户信息
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		//根据token从redis中查询用户信息
		String json = jedis.get(REDIS_USER_SESSION_KEY + ":" + token);
		//判断是否为空
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "此session已经过期，请重新登录");
		}
		//更新过期时间
		jedis.exprie(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		//返回用户信息
		return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
	}

	/**
	 * 安全退出
	 */
	@Override
	public TaotaoResult logout(String token) {
		String json = jedis.get(REDIS_USER_SESSION_KEY + ":" + token);
		if (StringUtils.isBlank(json)) {
			return TaotaoResult.build(400, "此session已经过期，请重新登录");
		}
		//删除token
		jedis.del(REDIS_USER_SESSION_KEY + ":" + token);
		return TaotaoResult.ok();
	}

}
