package edu.cauc.flight_inquiry.util;

import edu.cauc.flight_inquiry.po.Airline;
import edu.cauc.flight_inquiry.po.Airport;
import edu.cauc.flight_inquiry.po.FlightInfo;
import edu.cauc.flight_inquiry.po.LuggageInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

  public static List<Airline> readAirlinesExcel(String fileName, InputStream is) {
	List<Airline> list = new ArrayList<>();

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook(is);
	  } else {
		workbook = new XSSFWorkbook(is);
	  }

	  Sheet sheet = workbook.getSheetAt(0);
	  Iterator<Row> rows = sheet.rowIterator();

	  rows.next();    // 忽略第一行
	  while (rows.hasNext()) {
		Row row = rows.next();
		Airline airline = new Airline();
		airline.setAirlineType(row.getCell(0).getStringCellValue());
		airline.setTwoCode(row.getCell(1).getStringCellValue());
		airline.setThreeCode(row.getCell(2).getStringCellValue());
		airline.setZhSimpleName(row.getCell(3).getStringCellValue());
		airline.setEnSimpleName(row.getCell(4).getStringCellValue());
		airline.setZhFullName(row.getCell(5).getStringCellValue());
		airline.setEnFullName(row.getCell(6).getStringCellValue());
		list.add(airline);
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}

	return list;
  }

  public static List<Airport> readAirportsExcel(String fileName, InputStream is) {
	List<Airport> list = new ArrayList<>();

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook(is);
	  } else {
		workbook = new XSSFWorkbook(is);
	  }

	  Sheet sheet = workbook.getSheetAt(0);
	  Iterator<Row> rows = sheet.rowIterator();

	  rows.next();    // 忽略第一行（表头）
	  while (rows.hasNext()) {
		Row row = rows.next();
		Airport airport = new Airport();
		airport.setAirportType(row.getCell(0).getStringCellValue());
		airport.setThreeCode(row.getCell(1).getStringCellValue());
		airport.setFourCode(row.getCell(2).getStringCellValue());
		airport.setZhSimpleName(row.getCell(3).getStringCellValue());
		airport.setEnSimpleName(row.getCell(4).getStringCellValue());
		airport.setZhFullName(row.getCell(5).getStringCellValue());
		airport.setEnFullName(row.getCell(6).getStringCellValue());
		list.add(airport);
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}

	return list;
  }

  public static void writeAirlinesExcel(String fileName, List<Airline> list, OutputStream os) {
	String titleRow[] = {"属性", "二字码", "三字码", "中文简称", "英文简称", "中文全称", "英文全称"};

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook();
	  } else {
		workbook = new XSSFWorkbook();
	  }

	  Sheet sheet = (Sheet) workbook.createSheet();

	  // 创建第一行（表格头）
	  Row row = sheet.createRow(0);
	  for (int i = 0; i < titleRow.length; i++) {
		Cell cell = row.createCell(i);
		cell.setCellValue(titleRow[i]);
	  }

	  // 创建其他行
	  for (int i = 0; i < list.size(); i++) {
		Airline airline = list.get(i);
		row = sheet.createRow(i + 1);
		row.createCell(0).setCellValue(airline.getAirlineType());
		row.createCell(1).setCellValue(airline.getTwoCode());
		row.createCell(2).setCellValue(airline.getThreeCode());
		row.createCell(3).setCellValue(airline.getZhSimpleName());
		row.createCell(4).setCellValue(airline.getEnSimpleName());
		row.createCell(5).setCellValue(airline.getZhFullName());
		row.createCell(6).setCellValue(airline.getEnFullName());
	  }

	  workbook.write(os);

	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  public static void writeAirportsExcel(String fileName, List<Airport> list, OutputStream os) {
	String titleRow[] = {"属性", "三字码", "四字码", "中文简称", "英文简称", "中文全称", "英文全称"};

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook();
	  } else {
		workbook = new XSSFWorkbook();
	  }

	  Sheet sheet = (Sheet) workbook.createSheet();

	  // 创建第一行（表格头）
	  Row row = sheet.createRow(0);
	  for (int i = 0; i < titleRow.length; i++) {
		Cell cell = row.createCell(i);
		cell.setCellValue(titleRow[i]);
	  }

	  // 创建其他行
	  for (int i = 0; i < list.size(); i++) {
		Airport airport = list.get(i);
		row = sheet.createRow(i + 1);
		row.createCell(0).setCellValue(airport.getAirportType());
		row.createCell(1).setCellValue(airport.getThreeCode());
		row.createCell(2).setCellValue(airport.getFourCode());
		row.createCell(3).setCellValue(airport.getZhSimpleName());
		row.createCell(4).setCellValue(airport.getEnSimpleName());
		row.createCell(5).setCellValue(airport.getZhFullName());
		row.createCell(6).setCellValue(airport.getEnFullName());
	  }

	  workbook.write(os);

	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  public static void writeFlightInfosExcel(String fileName, List<FlightInfo> list, OutputStream os) {
	String titleRow[] = {"航班号", "航空公司", "机型", "始发站", "起飞航站楼", "目的站", "目的航站楼", "起飞时间", "到达时间"};

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook();
	  } else {
		workbook = new XSSFWorkbook();
	  }

	  Sheet sheet = (Sheet) workbook.createSheet();

	  // 创建第一行（表格头）
	  Row row = sheet.createRow(0);
	  for (int i = 0; i < titleRow.length; i++) {
		Cell cell = row.createCell(i);
		cell.setCellValue(titleRow[i]);
	  }

	  // 创建其他行
	  for (int i = 0; i < list.size(); i++) {
		FlightInfo flightInfo = list.get(i);
		row = sheet.createRow(i + 1);
		row.createCell(0).setCellValue(flightInfo.getFlightNum());
		row.createCell(1).setCellValue(flightInfo.getAirline());
		row.createCell(2).setCellValue(flightInfo.getPlaneType());
		row.createCell(3).setCellValue(flightInfo.getStartStation());
		row.createCell(4).setCellValue(flightInfo.getStartTerminal());
		row.createCell(5).setCellValue(flightInfo.getDestStation());
		row.createCell(6).setCellValue(flightInfo.getDestTerminal());
		row.createCell(7).setCellValue(flightInfo.getStartTime() == null ? null : flightInfo.getStartTime().toString());
		row.createCell(8).setCellValue(flightInfo.getArriveTime() == null ? null : flightInfo.getArriveTime().toString());
	  }

	  workbook.write(os);

	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  public static void writeLuggageInfosExcel(String fileName, List<LuggageInfo> list, OutputStream os) {

	String titleRow[] = {"航班号", "实际开始使用时间", "实际结束使用时间", "行李号", "转盘号", "航站楼", "航班属性"};

	try {
	  Workbook workbook = null;

	  if (fileName.endsWith("xls")) {
		workbook = new HSSFWorkbook();
	  } else {
		workbook = new XSSFWorkbook();
	  }

	  Sheet sheet = workbook.createSheet();

	  // 创建第一行（表格头）
	  Row row = sheet.createRow(0);
	  for (int i = 0; i < titleRow.length; i++) {
		Cell cell = row.createCell(i);
		cell.setCellValue(titleRow[i]);
	  }

	  // 创建其他行
	  for (int i = 0; i < list.size(); i++) {
		LuggageInfo luggageInfo = list.get(i);
		row = sheet.createRow(i + 1);
		row.createCell(0).setCellValue(luggageInfo.getFlightNum());
		row.createCell(1).setCellValue(luggageInfo.getRealStartTime() == null ? null : luggageInfo.getRealStartTime().toString());
		row.createCell(2).setCellValue(luggageInfo.getRealEndTime() == null ? null :luggageInfo.getRealEndTime().toString());
		row.createCell(3).setCellValue(luggageInfo.getLuggageNum());
		row.createCell(4).setCellValue(luggageInfo.getTurnNum());
		row.createCell(5).setCellValue(luggageInfo.getTerminal());
		row.createCell(6).setCellValue(luggageInfo.getFlightType());
	  }

	  workbook.write(os);

	} catch (Exception e) {
	  e.printStackTrace();
	}

  }
}
