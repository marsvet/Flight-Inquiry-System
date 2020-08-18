package edu.cauc.flight_inquiry.controller;

import edu.cauc.flight_inquiry.dao.AirportDao;
import edu.cauc.flight_inquiry.po.Airport;
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
public class AirportController {

  @Autowired
  private AirportDao airportDao;

  @RequestMapping(value = "/airport/add", method = RequestMethod.POST)
  public HashMap<String, Object> addAirport(@RequestBody Airport airport) {
	airport.setId(UUID.randomUUID().toString());
	airport.setState(1);
	airportDao.save(airport);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airport/update", method = RequestMethod.POST)
  public HashMap<String, Object> updateAirport(@RequestBody Airport airport) {
	airport.setState(1);
	airportDao.save(airport);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airport/search", method = RequestMethod.POST)
  public HashMap<String, Object> getAirports(@RequestBody HashMap<String, Object> params) {
	String zhSimpleName = params.containsKey("zhSimpleName") ? (String) params.get("zhSimpleName") : "";
	String enSimpleName = params.containsKey("enSimpleName") ? (String) params.get("enSimpleName") : "";
	String zhFullName = params.containsKey("zhFullName") ? (String) params.get("zhFullName") : "";
	String enFullName = params.containsKey("enFullName") ? (String) params.get("enFullName") : "";
	String threeCode = params.containsKey("threeCode") ? (String) params.get("threeCode") : "";
	String fourCode = params.containsKey("fourCode") ? (String) params.get("fourCode") : "";
	String airportType = params.containsKey("airportType") ? (String) params.get("airportType") : "";
	int limit = (int) params.get("limit");
	int offset = params.get("offset") == null ? ((int) params.get("page") - 1) * limit : (int) params.get("offset");

	int total = airportDao.getAirportsCount(zhSimpleName, enSimpleName, zhFullName, enFullName, threeCode, fourCode, airportType);
	List<Airport> airports = airportDao.getAirports(zhSimpleName, enSimpleName, zhFullName, enFullName, threeCode, fourCode, airportType, offset, limit);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	res.put("total", total);
	res.put("rows", airports);

	return res;
  }

  @RequestMapping(value = "/airport/del", method = RequestMethod.POST)
  public HashMap<String, Object> delAirport(@RequestBody HashMap<String, String> params) {
	String id = params.get("id");
	Optional<Airport> airportOp = airportDao.findById(id);
	if (!airportOp.isPresent()) {
	  HashMap<String, Object> res = new HashMap<>();
	  res.put("code", 1);
	  res.put("msg", "删除失败，找不到信息");
	  return res;
	}

	Airport airport = airportOp.get();
	airport.setState(0);
	airportDao.save(airport);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airport/importExcel", method = RequestMethod.POST)
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

	List<Airport> airports = ExcelUtil.readAirportsExcel(fileName, is);
	for (Airport airport : airports) {
	  airport.setId(UUID.randomUUID().toString());
	  airport.setState(1);
	}
	airportDao.saveAll(airports);

	HashMap<String, Object> res = new HashMap<>();
	res.put("code", 0);
	res.put("msg", "success");
	return res;
  }

  @RequestMapping(value = "/airport/exportExcel", method = RequestMethod.GET)
  public void exportExcel(HttpServletResponse response) {
	String fileName = "通航机场.xlsx";
	List<Airport> list = airportDao.findAll();

	try {
	  response.setContentType("application/octet-stream");
	  response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
	  OutputStream os = new BufferedOutputStream(response.getOutputStream());
	  ExcelUtil.writeAirportsExcel(fileName, list, os);
	  os.flush();
	  os.close();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

}
