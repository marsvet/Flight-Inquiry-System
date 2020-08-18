package edu.cauc.flight_inquiry.controller;

import edu.cauc.flight_inquiry.dao.AirlineDao;
import edu.cauc.flight_inquiry.po.Airline;
import edu.cauc.flight_inquiry.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin
public class AirlineController {

  @Autowired
  private AirlineDao airlineDao;


  @RequestMapping(value = "/airline/add", method = RequestMethod.POST)
  public HashMap<String, Object> addAirline(@RequestBody Airline airline) {
	airline.setId(UUID.randomUUID().toString());
	airline.setState(1);
	airlineDao.save(airline);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airline/update", method = RequestMethod.POST)
  public HashMap<String, Object> updateAirline(@RequestBody Airline airline) {
	airline.setState(1);
	airlineDao.save(airline);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airline/search", method = RequestMethod.POST)
  public HashMap<String, Object> getAirlines(@RequestBody HashMap<String, Object> params) {
	String zhSimpleName = params.containsKey("zhSimpleName") ? (String) params.get("zhSimpleName") : "";
	String enSimpleName = params.containsKey("enSimpleName") ? (String) params.get("enSimpleName") : "";
	String zhFullName = params.containsKey("zhFullName") ? (String) params.get("zhFullName") : "";
	String enFullName = params.containsKey("enFullName") ? (String) params.get("enFullName") : "";
	String twoCode = params.containsKey("twoCode") ? (String) params.get("twoCode") : "";
	String threeCode = params.containsKey("threeCode") ? (String) params.get("threeCode") : "";
	String airlineType = params.containsKey("airlineType") ? (String) params.get("airlineType") : "";
	int limit = (int) params.get("limit");
	int offset = params.get("offset") == null ? ((int) params.get("page") - 1) * limit : (int) params.get("offset");

	int total = airlineDao.getAirlinesCount(zhSimpleName, enSimpleName, zhFullName, enFullName, twoCode, threeCode, airlineType);
	List<Airline> airlines = airlineDao.getAirlines(zhSimpleName, enSimpleName, zhFullName, enFullName, twoCode, threeCode, airlineType, offset, limit);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("total", total);
	res.put("rows", airlines);

	return res;
  }

  @RequestMapping(value = "/airline/del", method = RequestMethod.POST)
  public HashMap<String, Object> delAirline(@RequestBody HashMap<String, String> params) {
	String id = params.get("id");
	Optional<Airline> airlineOp = airlineDao.findById(id);
	if (!airlineOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "删除失败，找不到信息");
	  return res;
	}

	Airline airline = airlineOp.get();
	airline.setState(0);
	airlineDao.save(airline);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airline/importExcel", method = RequestMethod.POST)
  public HashMap<String, Object> importExcel(@RequestParam("file") MultipartFile file) {
	String fileName = file.getOriginalFilename();

	if (!(fileName.endsWith("xls") || fileName.endsWith("xlsx"))) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "不支持此文件格式");
	  return res;
	}

	InputStream is = null;
	try {
	  is = file.getInputStream();
	} catch (IOException e) {
	  e.printStackTrace();
	}

	List<Airline> airlines = ExcelUtil.readAirlinesExcel(fileName, is);

	for (Airline airline : airlines) {
	  airline.setId(UUID.randomUUID().toString());
	  airline.setState(1);
	}
	airlineDao.saveAll(airlines);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airline/exportExcel", method = RequestMethod.GET)
  public void exportExcel(HttpServletResponse response) {
	String fileName = "航空公司.xlsx";
	List<Airline> list = airlineDao.findAll();

	try {
	  response.setContentType("application/octet-stream");
	  response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	  OutputStream os = new BufferedOutputStream(response.getOutputStream());
	  ExcelUtil.writeAirlinesExcel(fileName, list, os);
	  os.flush();
	  os.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

}
