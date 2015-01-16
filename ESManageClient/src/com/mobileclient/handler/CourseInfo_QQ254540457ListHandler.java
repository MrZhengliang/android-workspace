package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.CourseInfo_QQ254540457;
public class CourseInfo_QQ254540457ListHandler extends DefaultHandler {
	private List<CourseInfo_QQ254540457> courseInfo_QQ254540457List = null;
	private CourseInfo_QQ254540457 courseInfo_QQ254540457;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (courseInfo_QQ254540457 != null) { 
            String valueString = new String(ch, start, length); 
            if ("courseNumber".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseNumber(valueString); 
            else if ("courseName".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseName(valueString); 
            else if ("courseTeacher".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseTeacher(valueString); 
            else if ("courseTime".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseTime(valueString); 
            else if ("coursePlace".equals(tempString)) 
            	courseInfo_QQ254540457.setCoursePlace(valueString); 
            else if ("courseScore".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseScore(new Float(valueString).floatValue());
            else if ("courseMemo".equals(tempString)) 
            	courseInfo_QQ254540457.setCourseMemo(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("CourseInfo_QQ254540457".equals(localName)&&courseInfo_QQ254540457!=null){
			courseInfo_QQ254540457List.add(courseInfo_QQ254540457);
			courseInfo_QQ254540457 = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		courseInfo_QQ254540457List = new ArrayList<CourseInfo_QQ254540457>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("CourseInfo_QQ254540457".equals(localName)) {
            courseInfo_QQ254540457 = new CourseInfo_QQ254540457(); 
        }
        tempString = localName; 
	}

	public List<CourseInfo_QQ254540457> getCourseInfo_QQ254540457List() {
		return this.courseInfo_QQ254540457List;
	}
}
