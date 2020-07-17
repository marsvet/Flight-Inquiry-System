package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.UserDao;
import edu.cauc.flight_inquiry.po.User;
import edu.cauc.flight_inquiry.util.PwdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;


@RestController
@CrossOrigin
public class UserController {

  @Autowired
  private UserDao userDao;


  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public HashMap<String, Object> login(@RequestBody HashMap<String, String> params) {

	String email = params.get("email");
	String password = params.get("password");

	Optional<User> userOp = userDao.findByEmail(email);
	if (!userOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "邮箱不存在");
	  return res;
	}

	String hashedPwd = userOp.get().getPassword();
	if (PwdUtil.checkPwd(password, hashedPwd)) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 0);
	  res.put("msg", "success");
	  return res;
	} else {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "密码错误");
	  return res;
	}

  }

}
