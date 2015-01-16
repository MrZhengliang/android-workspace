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

import com.mobileclient.domain.Student_QQ287307421;
import com.mobileclient.handler.Student_QQ287307421ListHandler;
import com.mobileclient.util.HttpUtil;

/*学生信息管理业务逻辑层*/
public class Student_QQ287307421Service {
	/* 添加学生信息 */
	public String AddStudent_QQ287307421(Student_QQ287307421 student_QQ287307421) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("studentNumber", student_QQ287307421.getStudentNumber());
		params.put("studentName", student_QQ287307421.getStudentName());
		params.put("studentPassword", student_QQ287307421.getStudentPassword());
		params.put("studentSex", student_QQ287307421.getStudentSex());
		params.put("studentClassNumber", student_QQ287307421.getStudentClassNumber());
		params.put("studentBirthday", student_QQ287307421.getStudentBirthday().toString());
		params.put("studentState", student_QQ287307421.getStudentState());
		params.put("studentPhoto", student_QQ287307421.getStudentPhoto());
		params.put("studentTelephone", student_QQ287307421.getStudentTelephone());
		params.put("studentEmail", student_QQ287307421.getStudentEmail());
		params.put("studentQQ", student_QQ287307421.getStudentQQ());
		params.put("studentAddress", student_QQ287307421.getStudentAddress());
		params.put("studentMemo", student_QQ287307421.getStudentMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Student_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 查询学生信息 */
	public List<Student_QQ287307421> QueryStudent_QQ287307421(Student_QQ287307421 queryConditionStudent_QQ287307421) throws Exception {
		String urlString = HttpUtil.BASE_URL + "Student_QQ287307421Servlet?action=query";
		if(queryConditionStudent_QQ287307421 != null) {
			urlString += "&studentNumber=" + URLEncoder.encode(queryConditionStudent_QQ287307421.getStudentNumber(), "UTF-8") + "";
			urlString += "&studentName=" + URLEncoder.encode(queryConditionStudent_QQ287307421.getStudentName(), "UTF-8") + "";
			urlString += "&studentClassNumber=" + URLEncoder.encode(queryConditionStudent_QQ287307421.getStudentClassNumber(), "UTF-8") + "";
			if(queryConditionStudent_QQ287307421.getStudentBirthday() != null) {
				urlString += "&studentBirthday=" + URLEncoder.encode(queryConditionStudent_QQ287307421.getStudentBirthday().toString(), "UTF-8");
			}
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		Student_QQ287307421ListHandler student_QQ287307421ListHander = new Student_QQ287307421ListHandler();
		xr.setContentHandler(student_QQ287307421ListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<Student_QQ287307421> student_QQ287307421List = student_QQ287307421ListHander.getStudent_QQ287307421List();
		return student_QQ287307421List;
	}
	/* 更新学生信息 */
	public String UpdateStudent_QQ287307421(Student_QQ287307421 student_QQ287307421) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("studentNumber", student_QQ287307421.getStudentNumber());
		params.put("studentName", student_QQ287307421.getStudentName());
		params.put("studentPassword", student_QQ287307421.getStudentPassword());
		params.put("studentSex", student_QQ287307421.getStudentSex());
		params.put("studentClassNumber", student_QQ287307421.getStudentClassNumber());
		params.put("studentBirthday", student_QQ287307421.getStudentBirthday().toString());
		params.put("studentState", student_QQ287307421.getStudentState());
		params.put("studentPhoto", student_QQ287307421.getStudentPhoto());
		params.put("studentTelephone", student_QQ287307421.getStudentTelephone());
		params.put("studentEmail", student_QQ287307421.getStudentEmail());
		params.put("studentQQ", student_QQ287307421.getStudentQQ());
		params.put("studentAddress", student_QQ287307421.getStudentAddress());
		params.put("studentMemo", student_QQ287307421.getStudentMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Student_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 删除学生信息 */
	public String DeleteStudent_QQ287307421(String studentNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("studentNumber", studentNumber);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Student_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "学生信息信息删除失败!";
		}
	}
	/* 根据学号获取学生信息对象 */
	public Student_QQ287307421 GetStudent_QQ287307421(String studentNumber)  {
		List<Student_QQ287307421> student_QQ287307421List = new ArrayList<Student_QQ287307421>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("studentNumber", studentNumber);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "Student_QQ287307421Servlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				Student_QQ287307421 student_QQ287307421 = new Student_QQ287307421();
				student_QQ287307421.setStudentNumber(object.getString("studentNumber"));
				student_QQ287307421.setStudentName(object.getString("studentName"));
				student_QQ287307421.setStudentPassword(object.getString("studentPassword"));
				student_QQ287307421.setStudentSex(object.getString("studentSex"));
				student_QQ287307421.setStudentClassNumber(object.getString("studentClassNumber"));
				student_QQ287307421.setStudentBirthday(Timestamp.valueOf(object.getString("studentBirthday")));
				student_QQ287307421.setStudentState(object.getString("studentState"));
				student_QQ287307421.setStudentPhoto(object.getString("studentPhoto"));
				student_QQ287307421.setStudentTelephone(object.getString("studentTelephone"));
				student_QQ287307421.setStudentEmail(object.getString("studentEmail"));
				student_QQ287307421.setStudentQQ(object.getString("studentQQ"));
				student_QQ287307421.setStudentAddress(object.getString("studentAddress"));
				student_QQ287307421.setStudentMemo(object.getString("studentMemo"));
				student_QQ287307421List.add(student_QQ287307421);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = student_QQ287307421List.size();
		if(size>0) return student_QQ287307421List.get(0); 
		else return null; 
	}
}
