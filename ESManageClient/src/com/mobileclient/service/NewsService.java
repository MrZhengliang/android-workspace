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

import com.mobileclient.domain.News;
import com.mobileclient.handler.NewsListHandler;
import com.mobileclient.util.HttpUtil;

/*������Ϣ����ҵ���߼���*/
public class NewsService {
	/* ���������Ϣ */
	public String AddNews(News news) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("newsId", news.getNewsId() + "");
		params.put("newsTitle", news.getNewsTitle());
		params.put("newsContent", news.getNewsContent());
		params.put("newsDate", news.getNewsDate().toString());
		params.put("newsPhoto", news.getNewsPhoto());
		params.put("action", "add");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* ��ѯ������Ϣ */
	public List<News> QueryNews(News queryConditionNews) throws Exception {
		String urlString = HttpUtil.BASE_URL + "NewsServlet?action=query";
		if(queryConditionNews != null) {
			urlString += "&newsTitle=" + URLEncoder.encode(queryConditionNews.getNewsTitle(), "UTF-8") + "";
			if(queryConditionNews.getNewsDate() != null) {
				urlString += "&newsDate=" + URLEncoder.encode(queryConditionNews.getNewsDate().toString(), "UTF-8");
			}
		}
		URL url = new URL(urlString);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();

		NewsListHandler newsListHander = new NewsListHandler();
		xr.setContentHandler(newsListHander);
		InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
		InputSource is = new InputSource(isr);
		xr.parse(is);
		List<News> newsList = newsListHander.getNewsList();
		return newsList;
	}
	/* ����������Ϣ */
	public String UpdateNews(News news) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("newsId", news.getNewsId() + "");
		params.put("newsTitle", news.getNewsTitle());
		params.put("newsContent", news.getNewsContent());
		params.put("newsDate", news.getNewsDate().toString());
		params.put("newsPhoto", news.getNewsPhoto());
		params.put("action", "update");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/* ɾ��������Ϣ */
	public String DeleteNews(int newsId) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("newsId", newsId + "");
		params.put("action", "delete");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "������Ϣ��Ϣɾ��ʧ��!";
		}
	}
	/* ���ݼ�¼��Ż�ȡ������Ϣ���� */
	public News GetNews(int newsId)  {
		List<News> newsList = new ArrayList<News>();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("newsId", newsId + "");
		params.put("action", "updateQuery");
		byte[] resultByte;
		try {
			resultByte = HttpUtil.SendPostRequest(HttpUtil.BASE_URL + "NewsServlet?", params, "UTF-8");
			String result = new String(resultByte, "UTF-8");
			JSONArray array = new JSONArray(result);
			int length = array.length();
			for (int i = 0; i < length; i++) {
				JSONObject object = array.getJSONObject(i);
				News news = new News();
				news.setNewsId(object.getInt("newsId"));
				news.setNewsTitle(object.getString("newsTitle"));
				news.setNewsContent(object.getString("newsContent"));
				news.setNewsDate(Timestamp.valueOf(object.getString("newsDate")));
				news.setNewsPhoto(object.getString("newsPhoto"));
				newsList.add(news);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = newsList.size();
		if(size>0) return newsList.get(0); 
		else return null; 
	}
}
