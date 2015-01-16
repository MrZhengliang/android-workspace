package com.mobileclient.handler;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobileclient.domain.Student_QQ287307421;
public class Student_QQ287307421ListHandler extends DefaultHandler {
	private List<Student_QQ287307421> student_QQ287307421List = null;
	private Student_QQ287307421 student_QQ287307421;
	private String tempString;
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (student_QQ287307421 != null) { 
            String valueString = new String(ch, start, length); 
            if ("studentNumber".equals(tempString)) 
            	student_QQ287307421.setStudentNumber(valueString); 
            else if ("studentName".equals(tempString)) 
            	student_QQ287307421.setStudentName(valueString); 
            else if ("studentPassword".equals(tempString)) 
            	student_QQ287307421.setStudentPassword(valueString); 
            else if ("studentSex".equals(tempString)) 
            	student_QQ287307421.setStudentSex(valueString); 
            else if ("studentClassNumber".equals(tempString)) 
            	student_QQ287307421.setStudentClassNumber(valueString); 
            else if ("studentBirthday".equals(tempString)) 
            	student_QQ287307421.setStudentBirthday(Timestamp.valueOf(valueString));
            else if ("studentState".equals(tempString)) 
            	student_QQ287307421.setStudentState(valueString); 
            else if ("studentPhoto".equals(tempString)) 
            	student_QQ287307421.setStudentPhoto(valueString); 
            else if ("studentTelephone".equals(tempString)) 
            	student_QQ287307421.setStudentTelephone(valueString); 
            else if ("studentEmail".equals(tempString)) 
            	student_QQ287307421.setStudentEmail(valueString); 
            else if ("studentQQ".equals(tempString)) 
            	student_QQ287307421.setStudentQQ(valueString); 
            else if ("studentAddress".equals(tempString)) 
            	student_QQ287307421.setStudentAddress(valueString); 
            else if ("studentMemo".equals(tempString)) 
            	student_QQ287307421.setStudentMemo(valueString); 
        } 
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if("Student_QQ287307421".equals(localName)&&student_QQ287307421!=null){
			student_QQ287307421List.add(student_QQ287307421);
			student_QQ287307421 = null; 
		}
		tempString = null;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		student_QQ287307421List = new ArrayList<Student_QQ287307421>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
        if ("Student_QQ287307421".equals(localName)) {
            student_QQ287307421 = new Student_QQ287307421(); 
        }
        tempString = localName; 
	}

	public List<Student_QQ287307421> getStudent_QQ287307421List() {
		return this.student_QQ287307421List;
	}
}
