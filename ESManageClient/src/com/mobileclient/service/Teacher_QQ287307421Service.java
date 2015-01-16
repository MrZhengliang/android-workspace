package com.mobileclient.service;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.mobileclient.domain.Teacher_QQ287307421;
import com.mobileclient.handler.Teacher_QQ287307421ListHandler;
import com.mobileclient.util.HttpUtil;

/*教师信息管理业务逻辑层*/
public class Teacher_QQ287307421Service {
	/* 添加教师信息 */
	public String AddTeacher_QQ287307421(Teacher_QQ287307421 teacher_QQ287307421) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNumber", teacher_QQ287307421.getTeacherNumber());
		params.put("teacherName", teacher_QQ287307421.getTeacherName());
		params.put("teacherPassword", teacher_QQ287307421.getTeacherPassword());
		params.put("teacherSex", teacher_QQ287307421.getTeacherSex());
		params.put("teacherBirthday", teacher_QQ287307421.getTeacherBirthday().toString());
		params.put("teacherArriveDate", teacher_QQ287307421.getTeacherArriveDate().toString());
		params.put("teacherCardNumber", teacher_QQ287307421.getTeacherCardNumber());
		params.put("teacherPhone", teacher_QQ287307421.getTeacherPhone());
		params.put("teacherPhoto", teacher_QQ287307421.getTeacherPhoto());
		params.put("teacherAddress", teacher_QQ287307421.getTeacherAddress());
		params.put("teacherMemo", teacher_QQ287307421.getTeacherMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Teacher_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 查询教师信息 */
	public List<Teacher_QQ287307421> QueryTeacher_QQ287307421(Teacher_QQ287307421 queryConditionTeacher_QQ287307421) throws Exception {
		String urlString = HttpUtil.BASE_URL + "Teacher_QQ287307421Servlet?action=query";
		if(queryConditionTeacher_QQ287307421 != null) {
			urlString += "&teacherNumber=" + URLEncoder.encode(queryConditionTeacher_QQ287307421.getTeacherNumber(), "UTF-8") + "";
			urlString += "&teacherName=" + URLEncoder.encode(queryConditionTeacher_QQ287307421.getTeacherName(), "UTF-8") + "";
			if(queryConditionTeacher_QQ287307421.getTeacherBirthday() != null) {
				urlString += "&teacherBirthday=" + URLEncoder.encode(queryConditionTeacher_QQ287307421.getTeacherBirthday().toString(), "UTF-8");
			}
			if(queryConditionTeacher_QQ287307421.getTeacherArriveDate() != null) {
				urlString += "&teacherArriveDate=" + URLEncoder.encode(queryConditionTeacher_QQ287307421.getTeacherArriveDate().toString(), "UTF-8");
			}
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		Teacher_QQ287307421ListHandler teacher_QQ287307421ListHander = new Teacher_QQ287307421ListHandler();
		xr.setContentHandler(teacher_QQ287307421ListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Teacher_QQ287307421> teacher_QQ287307421List = teacher_QQ287307421ListHander.getTeacher_QQ287307421List();
		return teacher_QQ287307421List;
	}
	/* 更新教师信息 */
	public String UpdateTeacher_QQ287307421(Teacher_QQ287307421 teacher_QQ287307421) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNumber", teacher_QQ287307421.getTeacherNumber());
		params.put("teacherName", teacher_QQ287307421.getTeacherName());
		params.put("teacherPassword", teacher_QQ287307421.getTeacherPassword());
		params.put("teacherSex", teacher_QQ287307421.getTeacherSex());
		params.put("teacherBirthday", teacher_QQ287307421.getTeacherBirthday().toString());
		params.put("teacherArriveDate", teacher_QQ287307421.getTeacherArriveDate().toString());
		params.put("teacherCardNumber", teacher_QQ287307421.getTeacherCardNumber());
		params.put("teacherPhone", teacher_QQ287307421.getTeacherPhone());
		params.put("teacherPhoto", teacher_QQ287307421.getTeacherPhoto());
		params.put("teacherAddress", teacher_QQ287307421.getTeacherAddress());
		params.put("teacherMemo", teacher_QQ287307421.getTeacherMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Teacher_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 删除教师信息 */
	public String DeleteTeacher_QQ287307421(String teacherNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNumber", teacherNumber);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Teacher_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "教师信息信息删除失败!";
		}
	}
	/* 根据教师编号获取教师信息对象 */
	public Teacher_QQ287307421 GetTeacher_QQ287307421(String teacherNumber)  {
		List<Teacher_QQ287307421> teacher_QQ287307421List = new ArrayList<Teacher_QQ287307421>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("teacherNumber", teacherNumber);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Teacher_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Teacher_QQ287307421 teacher_QQ287307421 = new Teacher_QQ287307421();
				teacher_QQ287307421.setTeacherNumber(object.getString("teacherNumber"));
				teacher_QQ287307421.setTeacherName(object.getString("teacherName"));
				teacher_QQ287307421.setTeacherPassword(object.getString("teacherPassword"));
				teacher_QQ287307421.setTeacherSex(object.getString("teacherSex"));
				teacher_QQ287307421.setTeacherBirthday(Timestamp.valueOf(object.getString("teacherBirthday")));
				teacher_QQ287307421.setTeacherArriveDate(Timestamp.valueOf(object.getString("teacherArriveDate")));
				teacher_QQ287307421.setTeacherCardNumber(object.getString("teacherCardNumber"));
				teacher_QQ287307421.setTeacherPhone(object.getString("teacherPhone"));
				teacher_QQ287307421.setTeacherPhoto(object.getString("teacherPhoto"));
				teacher_QQ287307421.setTeacherAddress(object.getString("teacherAddress"));
				teacher_QQ287307421.setTeacherMemo(object.getString("teacherMemo"));
				teacher_QQ287307421List.add(teacher_QQ287307421);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = teacher_QQ287307421List.size();
		if(size>0) return teacher_QQ287307421List.get(0); 
		else return null; 
	}
}
