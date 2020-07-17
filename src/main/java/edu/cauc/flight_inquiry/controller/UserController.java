package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.UserDao;
import edu.cauc.flight_inquiry.po.User;
import edu.cauc.flight_inquiry.dto.JsonResult;
import edu.cauc.flight_inquiry.util.PwdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin
public class UserController {

  @Autowired
  private UserDao userDao;


  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public JsonResult<User> login(@RequestBody Map<String, String> params) {

	String email = params.get("email");
	String password = params.get("password");

	Optional<User> userOp = userDao.findByEmail(email);
	if (!userOp.isPresent())
	  return new JsonResult<>(1, "邮箱不存在");

	String hashedPwd = userOp.get().getPassword();
	if (PwdUtil.checkPwd(password, hashedPwd))
	  return new JsonResult<>();
	else
	  return new JsonResult<>(1, "密码错误");

  }

}
