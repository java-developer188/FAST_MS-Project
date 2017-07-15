package com.fast.project;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fast.project.pojo.TeacherBean;

public class TeacherDataManager {

	private static final String CLEAN_DATA_FILE = "C:\\Users\\Haider\\Desktop\\CleanData.xlsx";

	private Map<String, Integer> teacherBeans = new HashMap<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TeacherDataManager teacherDataManager = new TeacherDataManager();
		try {
			teacherDataManager.extractTeacher();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<TeacherBean> getTeacherBeans() {
		List<TeacherBean> list = new ArrayList<>();
		for (String s : teacherBeans.keySet()) {
			TeacherBean teacherBean = new TeacherBean();
			teacherBean.setFullName(s);
			list.add(teacherBean);
		}
		return list;
	}

	public void extractTeacher() throws Exception {
		FileInputStream cleanData = new FileInputStream(CLEAN_DATA_FILE);
		Workbook cleanDataWorkbook = new XSSFWorkbook(cleanData);
		Sheet datatypeSheet = cleanDataWorkbook.getSheetAt(0);
		int cellIndex = 15;
		int x = 2;
		do {
			Row currentRow = datatypeSheet.getRow(x);

			Cell teacherCell = currentRow.getCell(cellIndex);
			if (teacherCell != null && teacherCell.getCellTypeEnum().equals(CellType.STRING)
					&& !teacherCell.getStringCellValue().isEmpty()) {
				String value = teacherCell.getStringCellValue().trim();
				if (value.matches("END"))
					break;
				if(value.contains("Sehrish")||value.contains("Dr.hasina")||value.contains("Dr.Fahad riaz")||
						value.contains("Dr. Fahad Sammad")||value.contains("Sumaira")||value.contains("Dr. Zulfiqar")||
						value.contains("Dr. Atif")||value.contains("Dr. Jawad")||value.contains("Dr. Sufian")||
						value.contains("Imran Shah")||value.contains("Dr. Khusro")){
					System.out.println(value);
				}
				if (value.split("\\)\\s*,").length > 1) {
					String[] temps = value.split("\\)\\s*,");
					for (int i = 0; i < temps.length; i++) {
						temps[i] = temps[i].substring(0, temps[i].indexOf("("));
						temps[i] = temps[i].replaceAll("\\s*\\(", "").replaceAll("\\s*\\)", "");
						temps[i] = temps[i].replaceAll("\\s{2,}", " ").trim();
						
						//remove salutation
						String noSalutation ="";
						if(temps[i].contains("Mr. "))
							noSalutation = temps[i].split("Mr. ")[1];
						else
						if(temps[i].contains("Mr "))
							noSalutation = temps[i].split("Mr ")[1];
						else
						if(temps[i].contains("Ms. "))
							noSalutation = temps[i].split("Ms. ")[1];
						else
						if(temps[i].contains("Ms "))
							noSalutation = temps[i].split("Ms ")[1];
						else
							noSalutation = temps[i];
						
						String[] uncapitalize =  noSalutation.split(" ");
						String capitalize = "";
						for(String s:uncapitalize){
							capitalize += (s.length() == 0) ? s : s.substring(0, 1).toUpperCase() + s.substring(1)+" ";
						}
						
						
						if (!capitalize.isEmpty() && !teacherBeans.containsKey(capitalize)) {
							teacherBeans.put(capitalize, 0);
							System.out.println(capitalize);
						}
					}
				} else {
					if (value.contains("(")) {
						value = value.substring(0, value.indexOf("("));
					}

					value = value.replaceAll("\\s*\\(", "").replaceAll("\\s*\\)", "");
					value = value.replaceAll("\\s{2,}", " ").trim();
					
					//remove salutation
					String noSalutation ="";
					if(value.contains("Mr. "))
						noSalutation = value.split("Mr. ")[1];
					else
					if(value.contains("Mr "))
						noSalutation = value.split("Mr ")[1];
					else
					if(value.contains("Ms. "))
						noSalutation = value.split("Ms. ")[1];
					else
					if(value.contains("Ms "))
						noSalutation = value.split("Ms ")[1];
					else
						noSalutation = value;
					
					String[] uncapitalize =  noSalutation.split(" ");
					String capitalize = "";
					for(String s:uncapitalize){
						capitalize += (s.length() == 0) ? s : s.substring(0, 1).toUpperCase() + s.substring(1)+" ";
					}

					if (!capitalize.isEmpty() && !teacherBeans.containsKey(capitalize)) {
						teacherBeans.put(capitalize, 0);
						System.out.println(capitalize);
					}
				}
			}
			x++;
		} while (true);
		cleanDataWorkbook.close();
	}

}
