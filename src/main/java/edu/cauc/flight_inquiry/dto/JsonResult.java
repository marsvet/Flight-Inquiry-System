package edu.cauc.flight_inquiry.dto;

import lombok.Data;

@Data
public class JsonResult<T> {

  private int code;
  private String msg;
  private T data;

  /**
   * 默认状态码为 0，提示信息为 success
   */
  public JsonResult() {
	this.code = 0;
	this.msg = "success";
  }

  public JsonResult(int code, String msg) {
	this.code = code;
	this.msg = msg;
  }

  public JsonResult(T data) {
	this.data = data;
  }

  public JsonResult(int code, String msg, T data) {
	this.code = code;
	this.msg = msg;
	this.data = data;
  }

}
