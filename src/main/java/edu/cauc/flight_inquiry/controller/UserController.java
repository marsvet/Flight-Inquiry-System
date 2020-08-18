package edu.cauc.flight_inquiry.controller;


import edu.cauc.flight_inquiry.dao.UserDao;
import edu.cauc.flight_inquiry.po.User;
import edu.cauc.flight_inquiry.util.ImgCodeUtil;
import edu.cauc.flight_inquiry.util.MailUtil;
import edu.cauc.flight_inquiry.util.PwdUtil;
import edu.cauc.flight_inquiry.util.SixCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
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
	  res.put("state", userOp.get().getState());
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

  @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
  public HashMap<String, Object> modifyPwd(@RequestBody HashMap<String, Object> params) {
	String email = (String) params.get("email");
	String password = (String) params.get("password");

	String hashedPwd = PwdUtil.getHashedPwd(password);

	Optional<User> userOp = userDao.findByEmail(email);
	if (!userOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "邮箱不存在");
	  return res;
	}

	User user = userOp.get();
	user.setPassword(hashedPwd);
	userDao.save(user);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/user/modifyAvatar", method = RequestMethod.POST)
  public HashMap<String, Object> modifyAvatar(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
	String fileName = file.getOriginalFilename();
	String prefix = null;

	if (fileName.toLowerCase().endsWith("png"))
	  prefix = "data:image/png;base64,";
	else if (fileName.toLowerCase().endsWith("jpg") || fileName.toLowerCase().endsWith("jpeg")) {
	  prefix = "data:image/jpeg;base64,";
	} else {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "不支持此图片格式");
	  return res;
	}

	String imgBase64 = null;
	try {
	  imgBase64 = prefix + new BASE64Encoder().encode(file.getBytes()).replace("\n", "");
	} catch (IOException e) {
	  e.printStackTrace();
	}

	Optional<User> userOp = userDao.findByEmail(email);
	if (!userOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "邮箱不存在");
	  return res;
	}

	User user = userOp.get();
	user.setAvatar(imgBase64);
	userDao.save(user);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("avatar", imgBase64);
	return res;
  }

  /**
   * 授权
   */
  @RequestMapping(value = "/auth", method = RequestMethod.POST)
  public HashMap<String, Object> auth(@RequestBody HashMap<String, String> params) {
	String email = params.get("email");

	Optional<User> userOp = userDao.findByEmail(email);
	if (!userOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "邮箱不存在");
	  return res;
	}

	User user = userOp.get();
	user.setState(2);
	userDao.save(user);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/user/update", method = RequestMethod.POST)
  public HashMap<String, Object> updateUser(@RequestBody User user) {
	Optional<User> userInDbOp = userDao.findByEmail(user.getEmail());
	User userInDb = userInDbOp.get();
	userInDb.setBirthday(user.getBirthday());
	userInDb.setName(user.getName());
	userInDb.setPhone(user.getPhone());
	userInDb.setSex(user.getSex());
	userInDb.setUsername(user.getUsername());
	userDao.save(userInDb);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/user/del", method = RequestMethod.POST)
  public HashMap<String, Object> delUser(@RequestBody HashMap<String, String> params) {
	String id = params.get("id");
	Optional<User> userOp = userDao.findById(id);
	if (!userOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "删除失败，找不到信息");
	  return res;
	}

	User user = userOp.get();
	user.setState(0);
	userDao.save(user);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/user/allUsers", method = RequestMethod.GET)
  public HashMap<String, Object> getUsers() {
	List<HashMap<String, Object>> list = new ArrayList<>();
	for (User user : userDao.findAll()) {
	  if (user.getState() > 0) {
		HashMap map = new HashMap();
		map.put("id", user.getId());
		map.put("email", user.getEmail());
		map.put("birthday", user.getBirthday());
		map.put("name", user.getName());
		map.put("username", user.getUsername());
		map.put("phone", user.getPhone());
		map.put("sex", user.getSex());
		map.put("avatar", user.getAvatar());
		map.put("state", user.getState());
		list.add(map);
	  }
	}

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("rows", list);

	return res;
  }

  @RequestMapping(value = "/user/findByEmail", method = RequestMethod.GET)
  public HashMap<String, Object> findByEmail(@RequestParam("email") String email) {
	Optional<User> userOp = userDao.findByEmail(email);

	if (!userOp.isPresent() || userOp.get().getState() == 0) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "用户不存在");
	  return res;
	}

	User user = userOp.get();

	HashMap userInfo = new HashMap();
	userInfo.put("id", user.getId());
	userInfo.put("email", user.getEmail());
	userInfo.put("birthday", user.getBirthday());
	userInfo.put("name", user.getName());
	userInfo.put("username", user.getUsername());
	userInfo.put("phone", user.getPhone());
	userInfo.put("sex", user.getSex());
	userInfo.put("avatar", user.getAvatar());

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("userInfo", userInfo);
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
