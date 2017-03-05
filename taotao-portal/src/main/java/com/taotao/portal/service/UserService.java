package com.taotao.portal.service;

import com.taotao.portal.pojo.TbUser;

public interface UserService {
	TbUser getUserByToken(String token);
}
