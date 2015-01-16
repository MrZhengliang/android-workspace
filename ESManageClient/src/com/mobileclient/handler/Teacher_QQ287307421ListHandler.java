package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Teacher_QQ287307421;
public class Teacher_QQ287307421ListHandler extends DefaultHandler {
	private List<Teacher_QQ287307421> teacher_QQ287307421List = null;
	private Teacher_QQ287307421 teacher_QQ287307421;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (teacher_QQ287307421 != null) { 
            String valueString = new String(ch, start, length); 
            if ("teacherNumber".equals(tempString)) 
            	teacher_QQ287307421.setTeacherNumber(valueString); 
            else if ("teacherName".equals(tempString)) 
            	teacher_QQ287307421.setTeacherName(valueString); 
            else if ("teacherPassword".equals(tempString)) 
            	teacher_QQ287307421.setTeacherPassword(valueString); 
            else if ("teacherSex".equals(tempString)) 
            	teacher_QQ287307421.setTeacherSex(valueString); 
            else if ("teacherBirthday".equals(tempString)) 
            	teacher_QQ287307421.setTeacherBirthday(Timestamp.valueOf(valueString));
            else if ("teacherArriveDate".equals(tempString)) 
            	teacher_QQ287307421.setTeacherArriveDate(Timestamp.valueOf(valueString));
            else if ("teacherCardNumber".equals(tempString)) 
            	teacher_QQ287307421.setTeacherCardNumber(valueString); 
            else if ("teacherPhone".equals(tempString)) 
            	teacher_QQ287307421.setTeacherPhone(valueString); 
            else if ("teacherPhoto".equals(tempString)) 
            	teacher_QQ287307421.setTeacherPhoto(valueString); 
            else if ("teacherAddress".equals(tempString)) 
            	teacher_QQ287307421.setTeacherAddress(valueString); 
            else if ("teacherMemo".equals(tempString)) 
            	teacher_QQ287307421.setTeacherMemo(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Teacher_QQ287307421".equals(localName)&&teacher_QQ287307421!=null){
			teacher_QQ287307421List.add(teacher_QQ287307421);
			teacher_QQ287307421 = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		teacher_QQ287307421List = new ArrayList<Teacher_QQ287307421>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Teacher_QQ287307421".equals(localName)) {
            teacher_QQ287307421 = new Teacher_QQ287307421(); 
        }
        tempString = localName; 
	}

	public List<Teacher_QQ287307421> getTeacher_QQ287307421List() {
		return this.teacher_QQ287307421List;
	}
}
