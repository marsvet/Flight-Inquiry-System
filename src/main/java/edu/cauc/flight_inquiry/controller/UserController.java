package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.UserDao;
import edu.cauc.flight_inquiry.po.User;
import edu.cauc.flight_inquiry.util.ImgCodeUtil;
import edu.cauc.flight_inquiry.util.MailUtil;
import edu.cauc.flight_inquiry.util.PwdUtil;
import edu.cauc.flight_inquiry.util.SixCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public HashMap<String, Object> register(@RequestBody User user) {
	if (userDao.existsByEmail(user.getEmail())) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "该用户已注册");
	  return res;
	}

	user.setPassword(PwdUtil.getHashedPwd(user.getPassword()));
    user.setId(UUID.randomUUID().toString());
    user.setEnterTime(new Date());
    user.setState(1);

    userDao.save(user);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/verifyEmail", method = RequestMethod.POST)
  public HashMap<String, Object> verifyEmail(@RequestBody HashMap<String, String> params) {
	String email = params.get("email");

	String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	Pattern regex = Pattern.compile(emailRegex);
	Matcher matcher = regex.matcher(email);

	if (!matcher.matches()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "邮箱格式不正确");
	  return res;
	}

	String vCode = SixCodeUtil.randomCode();
	MailUtil.sendMail(email, "验证码", "您好！感谢您使用我们的产品，您的邮箱验证码为：" + vCode);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("vCode", vCode);
	return res;
  }

  @RequestMapping(value = "/imgVCode", method = RequestMethod.GET)
  public HashMap<String, Object> genImgCode() {
	Object[] objs = ImgCodeUtil.createImage();
	BufferedImage image = (BufferedImage) objs[1];
	ByteArrayOutputStream stream = new ByteArrayOutputStream();

	String vCode = (String) objs[0];
	String imgVCode = null;

	try {
	  ImageIO.write(image, "png", stream);
	  imgVCode = "data:image/png;base64," + new BASE64Encoder().encode(stream.toByteArray()).replace("\n", "");
	  stream.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}

	HashMap<String, Object> map = new HashMap<>();
	map.put("code", 0);
	map.put("msg", "success");
	map.put("vCode", vCode);
	map.put("imgVCode", imgVCode);
	return map;
  }

}
