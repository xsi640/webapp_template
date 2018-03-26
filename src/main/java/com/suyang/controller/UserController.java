package com.suyang.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suyang.domain.ResponseMessage;
import com.suyang.domain.User;
import com.suyang.exceptions.APIException;
import com.suyang.exceptions.APIExceptionType;
import com.suyang.repository.UserRepository;
import com.suyang.utils.CryptoUtils;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.GET)
	public ResponseMessage findOne(@PathVariable("id") final int id) {
		return ResponseMessage.success(userRepository.findOne(id));
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.GET)
	public ResponseMessage findAll(@RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex,
							  @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) {
		Pageable pageable = new PageRequest(pageIndex - 1, pageSize);
		return ResponseMessage.success(userRepository.findAll(pageable));
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.POST)
	public ResponseMessage create(@RequestParam(required = true) String loginName, 
						@RequestParam(required = true) String loginPwd,
						@RequestParam(required = true) String realName, 
						int sex, Date birthday, String address) {
		User user = new User();
		user.setLoginName(loginName);
		user.setRealName(realName);
		user.setLastLoginIP("");
		user.setLastLoginTime(new Date());
		user.setLoginCount(0);
		user.setSex(sex);
		user.setBirthday(birthday);
		user.setAddress(address);
		String salt = CryptoUtils.getSalt();
		user.setLoginSalt(salt);
		user.setLoginPwd(CryptoUtils.getHash(loginPwd, salt));
		return ResponseMessage.success(userRepository.save(user));
	}

	@RequestMapping(value = "/api/user", method = RequestMethod.PUT)
	public ResponseMessage modify(@RequestParam(required = true) int id, 
						String loginPwd,
						@RequestParam(required = true) String realName, 
						int sex, Date birthday, String address) {
		User user = userRepository.findOne(id);
		if (user == null)
			return null;

		user.setRealName(realName);
		user.setSex(sex);
		user.setBirthday(birthday);
		user.setAddress(address);
		if (!StringUtils.isEmpty(loginPwd)) {
			String salt = CryptoUtils.getSalt();
			user.setLoginSalt(salt);
			user.setLoginPwd(CryptoUtils.getHash(loginPwd, salt));
		}
		return ResponseMessage.success(userRepository.save(user));
	}

	@RequestMapping(value = "/api/user/{id}", method = RequestMethod.DELETE)
	public ResponseMessage delete(@PathVariable("id") int id) throws APIException {
		int result = 0;
		User user = userRepository.findOne(id);
		if (user != null) {
			if (user.getLoginName().equals("admin")) {
				throw new APIException(APIExceptionType.NoLimit);
			}
			userRepository.delete(user);
			result = 1;
		}
		return ResponseMessage.success(result);
	}

	@RequestMapping(value = "/api/user/checkname")
	public ResponseMessage existsName(@RequestParam(required = true) String loginName) {
		return ResponseMessage.success(userRepository.countByLoginName(loginName) > 0);
	}
}
