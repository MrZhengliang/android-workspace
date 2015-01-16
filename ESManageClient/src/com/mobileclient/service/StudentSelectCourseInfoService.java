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

import com.mobileclient.domain.StudentSelectCourseInfo;
import com.mobileclient.handler.StudentSelectCourseInfoListHandler;
import com.mobileclient.util.HttpUtil;

/*ѡ����Ϣ����ҵ���߼���*/
public class StudentSelectCourseInfoService {
	/* ���ѡ����Ϣ */
	public String AddStudentSelectCourseInfo(StudentSelectCourseInfo studentSelectCourseInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("selectId", studentSelectCourseInfo.getSelectId() + "");
		params.put("studentNumber", studentSelectCourseInfo.getStudentNumber());
		params.put("courseNumber", studentSelectCourseInfo.getCourseNumber());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "StudentSelectCourseInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* ��ѯѡ����Ϣ */
	public List<StudentSelectCourseInfo> QueryStudentSelectCourseInfo(StudentSelectCourseInfo queryConditionStudentSelectCourseInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "StudentSelectCourseInfoServlet?action=query";
		if(queryConditionStudentSelectCourseInfo != null) {
			urlString += "&studentNumber=" + URLEncoder.encode(queryConditionStudentSelectCourseInfo.getStudentNumber(), "UTF-8") + "";
			urlString += "&courseNumber=" + URLEncoder.encode(queryConditionStudentSelectCourseInfo.getCourseNumber(), "UTF-8") + "";
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		StudentSelectCourseInfoListHandler studentSelectCourseInfoListHander = new StudentSelectCourseInfoListHandler();
		xr.setContentHandler(studentSelectCourseInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<StudentSelectCourseInfo> studentSelectCourseInfoList = studentSelectCourseInfoListHander.getStudentSelectCourseInfoList();
		return studentSelectCourseInfoList;
	}
	/* ����ѡ����Ϣ */
	public String UpdateStudentSelectCourseInfo(StudentSelectCourseInfo studentSelectCourseInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("selectId", studentSelectCourseInfo.getSelectId() + "");
		params.put("studentNumber", studentSelectCourseInfo.getStudentNumber());
		params.put("courseNumber", studentSelectCourseInfo.getCourseNumber());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "StudentSelectCourseInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* ɾ��ѡ����Ϣ */
	public String DeleteStudentSelectCourseInfo(int selectId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("selectId", selectId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "StudentSelectCourseInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "ѡ����Ϣ��Ϣɾ��ʧ��!";
		}
	}
	/* ���ݼ�¼��Ż�ȡѡ����Ϣ���� */
	public StudentSelectCourseInfo GetStudentSelectCourseInfo(int selectId)  {
		List<StudentSelectCourseInfo> studentSelectCourseInfoList = new ArrayList<StudentSelectCourseInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("selectId", selectId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "StudentSelectCourseInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				StudentSelectCourseInfo studentSelectCourseInfo = new StudentSelectCourseInfo();
				studentSelectCourseInfo.setSelectId(object.getInt("selectId"));
				studentSelectCourseInfo.setStudentNumber(object.getString("studentNumber"));
				studentSelectCourseInfo.setCourseNumber(object.getString("courseNumber"));
				studentSelectCourseInfoList.add(studentSelectCourseInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = studentSelectCourseInfoList.size();
		if(size>0) return studentSelectCourseInfoList.get(0); 
		else return null; 
	}
}
