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

import com.mobileclient.domain.CourseInfo_QQ254540457;
import com.mobileclient.handler.CourseInfo_QQ254540457ListHandler;
import com.mobileclient.util.HttpUtil;

/*课程信息管理业务逻辑层*/
public class CourseInfo_QQ254540457Service {
	/* 添加课程信息 */
	public String AddCourseInfo_QQ254540457(CourseInfo_QQ254540457 courseInfo_QQ254540457) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNumber", courseInfo_QQ254540457.getCourseNumber());
		params.put("courseName", courseInfo_QQ254540457.getCourseName());
		params.put("courseTeacher", courseInfo_QQ254540457.getCourseTeacher());
		params.put("courseTime", courseInfo_QQ254540457.getCourseTime());
		params.put("coursePlace", courseInfo_QQ254540457.getCoursePlace());
		params.put("courseScore", courseInfo_QQ254540457.getCourseScore() + "");
		params.put("courseMemo", courseInfo_QQ254540457.getCourseMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseInfo_QQ254540457Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 查询课程信息 */
	public List<CourseInfo_QQ254540457> QueryCourseInfo_QQ254540457(CourseInfo_QQ254540457 queryConditionCourseInfo_QQ254540457) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CourseInfo_QQ254540457Servlet?action=query";
		if(queryConditionCourseInfo_QQ254540457 != null) {
			urlString += "&courseNumber=" + URLEncoder.encode(queryConditionCourseInfo_QQ254540457.getCourseNumber(), "UTF-8") + "";
			urlString += "&courseName=" + URLEncoder.encode(queryConditionCourseInfo_QQ254540457.getCourseName(), "UTF-8") + "";
			urlString += "&courseTeacher=" + URLEncoder.encode(queryConditionCourseInfo_QQ254540457.getCourseTeacher(), "UTF-8") + "";
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CourseInfo_QQ254540457ListHandler courseInfo_QQ254540457ListHander = new CourseInfo_QQ254540457ListHandler();
		xr.setContentHandler(courseInfo_QQ254540457ListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = courseInfo_QQ254540457ListHander.getCourseInfo_QQ254540457List();
		return courseInfo_QQ254540457List;
	}
	/* 更新课程信息 */
	public String UpdateCourseInfo_QQ254540457(CourseInfo_QQ254540457 courseInfo_QQ254540457) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNumber", courseInfo_QQ254540457.getCourseNumber());
		params.put("courseName", courseInfo_QQ254540457.getCourseName());
		params.put("courseTeacher", courseInfo_QQ254540457.getCourseTeacher());
		params.put("courseTime", courseInfo_QQ254540457.getCourseTime());
		params.put("coursePlace", courseInfo_QQ254540457.getCoursePlace());
		params.put("courseScore", courseInfo_QQ254540457.getCourseScore() + "");
		params.put("courseMemo", courseInfo_QQ254540457.getCourseMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseInfo_QQ254540457Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 删除课程信息 */
	public String DeleteCourseInfo_QQ254540457(String courseNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNumber", courseNumber);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseInfo_QQ254540457Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "课程信息信息删除失败!";
		}
	}
	/* 根据课程编号获取课程信息对象 */
	public CourseInfo_QQ254540457 GetCourseInfo_QQ254540457(String courseNumber)  {
		List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = new ArrayList<CourseInfo_QQ254540457>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("courseNumber", courseNumber);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CourseInfo_QQ254540457Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				CourseInfo_QQ254540457 courseInfo_QQ254540457 = new CourseInfo_QQ254540457();
				courseInfo_QQ254540457.setCourseNumber(object.getString("courseNumber"));
				courseInfo_QQ254540457.setCourseName(object.getString("courseName"));
				courseInfo_QQ254540457.setCourseTeacher(object.getString("courseTeacher"));
				courseInfo_QQ254540457.setCourseTime(object.getString("courseTime"));
				courseInfo_QQ254540457.setCoursePlace(object.getString("coursePlace"));
				courseInfo_QQ254540457.setCourseScore((float) object.getDouble("courseScore"));
				courseInfo_QQ254540457.setCourseMemo(object.getString("courseMemo"));
				courseInfo_QQ254540457List.add(courseInfo_QQ254540457);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = courseInfo_QQ254540457List.size();
		if(size>0) return courseInfo_QQ254540457List.get(0); 
		else return null; 
	}
}
