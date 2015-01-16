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

import com.mobileclient.domain.CollegeInfo;
import com.mobileclient.handler.CollegeInfoListHandler;
import com.mobileclient.util.HttpUtil;

/*学院信息管理业务逻辑层*/
public class CollegeInfoService {
	/* 添加学院信息 */
	public String AddCollegeInfo(CollegeInfo collegeInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeInfo.getCollegeNumber());
		params.put("collegeName", collegeInfo.getCollegeName());
		params.put("collegeBirthDate", collegeInfo.getCollegeBirthDate().toString());
		params.put("collegeMan", collegeInfo.getCollegeMan());
		params.put("collegeTelephone", collegeInfo.getCollegeTelephone());
		params.put("collegeMemo", collegeInfo.getCollegeMemo());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 查询学院信息 */
	public List<CollegeInfo> QueryCollegeInfo(CollegeInfo queryConditionCollegeInfo) throws Exception {
		String urlString = HttpUtil.BASE_URL + "CollegeInfoServlet?action=query";
		if(queryConditionCollegeInfo != null) {
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		CollegeInfoListHandler collegeInfoListHander = new CollegeInfoListHandler();
		xr.setContentHandler(collegeInfoListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<CollegeInfo> collegeInfoList = collegeInfoListHander.getCollegeInfoList();
		return collegeInfoList;
	}
	/* 更新学院信息 */
	public String UpdateCollegeInfo(CollegeInfo collegeInfo) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeInfo.getCollegeNumber());
		params.put("collegeName", collegeInfo.getCollegeName());
		params.put("collegeBirthDate", collegeInfo.getCollegeBirthDate().toString());
		params.put("collegeMan", collegeInfo.getCollegeMan());
		params.put("collegeTelephone", collegeInfo.getCollegeTelephone());
		params.put("collegeMemo", collegeInfo.getCollegeMemo());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* 删除学院信息 */
	public String DeleteCollegeInfo(String collegeNumber) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeNumber);
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "学院信息信息删除失败!";
		}
	}
	/* 根据学院编号获取学院信息对象 */
	public CollegeInfo GetCollegeInfo(String collegeNumber)  {
		List<CollegeInfo> collegeInfoList = new ArrayList<CollegeInfo>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("collegeNumber", collegeNumber);
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "CollegeInfoServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				CollegeInfo collegeInfo = new CollegeInfo();
				collegeInfo.setCollegeNumber(object.getString("collegeNumber"));
				collegeInfo.setCollegeName(object.getString("collegeName"));
				collegeInfo.setCollegeBirthDate(Timestamp.valueOf(object.getString("collegeBirthDate")));
				collegeInfo.setCollegeMan(object.getString("collegeMan"));
				collegeInfo.setCollegeTelephone(object.getString("collegeTelephone"));
				collegeInfo.setCollegeMemo(object.getString("collegeMemo"));
				collegeInfoList.add(collegeInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = collegeInfoList.size();
		if(size>0) return collegeInfoList.get(0); 
		else return null; 
	}
}
