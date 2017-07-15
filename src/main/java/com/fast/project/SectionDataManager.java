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

import com.fast.project.pojo.CourseBean;
import com.fast.project.pojo.SectionBean;

public class SectionDataManager {

	private static final String CLEAN_DATA_FILE = "C:\\Users\\Haider\\Desktop\\CleanData.xlsx";

	public  Map<String, Integer> SECTIONS = new HashMap<>();

	public static void main(String[] args) {
//		try {
//			extractSections();
//			insertSectionsOperation();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public  List<SectionBean> getSectionBeans() {
		List<SectionBean> list = new ArrayList<>();
		for(String s : SECTIONS.keySet()){
			SectionBean sectionBean = new SectionBean();
			sectionBean.setName(s);
			list.add(sectionBean);
		}
		return list;
	}

	public  void extractSections() throws Exception {
		FileInputStream cleanData = new FileInputStream(CLEAN_DATA_FILE);
		Workbook cleanDataWorkbook = new XSSFWorkbook(cleanData);
		Sheet datatypeSheet = cleanDataWorkbook.getSheetAt(0);
		int cellIndex = 14;
		int x = 2;
		do {
			Row currentRow = datatypeSheet.getRow(x);
			Cell sectionCell = currentRow.getCell(cellIndex);
			if (sectionCell != null && sectionCell.getCellTypeEnum().equals(CellType.STRING)
					&& !sectionCell.getStringCellValue().isEmpty()) {
				String value = sectionCell.getStringCellValue();
				if (value.matches("END"))
					break;

				value = value.replaceAll("=\\s*\\(", ",").replaceAll("\\s*\\)", "").replaceAll("\\s*", "");
				for (String s : value.split(",")) {
					if (!s.isEmpty() && !SECTIONS.containsKey(s.toUpperCase()))
						SECTIONS.put(s.toUpperCase(), 0);
				}
			}
			Cell teacherCell = currentRow.getCell(cellIndex + 1);
			if (teacherCell != null && teacherCell.getCellTypeEnum().equals(CellType.STRING)
					&& !teacherCell.getStringCellValue().isEmpty()) {
				String value = teacherCell.getStringCellValue();
				if (value.matches("END"))
					break;
				if (value.split("\\)\\s*,").length > 1) {
					String[] temps = value.split("\\)\\s*,");
					for (int i = 0; i < temps.length; i++) {
						temps[i] = temps[i].substring(temps[i].indexOf("(") + 1, temps[i].length());
						temps[i] = temps[i].replaceAll("\\s*\\(", "").replaceAll("\\s*\\)", "").replaceAll("\\s*", "");
						System.out.println(temps[i]);
						for (String s : temps[i].split(",")) {
							if (!s.isEmpty() && !SECTIONS.containsKey(s.toUpperCase()))
								SECTIONS.put(s.toUpperCase(), 0);
						}
					}
				}
			}
			x++;
		} while (true);
		cleanDataWorkbook.close();
	}
}
