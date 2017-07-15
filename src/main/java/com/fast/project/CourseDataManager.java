package com.fast.project;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fast.project.pojo.CourseBean;

public class CourseDataManager {
	
	private static final String CLEAN_DATA_FILE = "C:\\Users\\Haider\\Desktop\\CleanData.xlsx";
	
	
	public  List<CourseBean> getCourseBeans() throws Exception {
		FileInputStream cleanData = new FileInputStream(CLEAN_DATA_FILE);
		Workbook cleanDataWorkbook = new XSSFWorkbook(cleanData);
		Sheet datatypeSheet = cleanDataWorkbook.getSheetAt(0);
		int x = 2;
		List<CourseBean> list = new ArrayList<>();
		do {
			Row currentRow = datatypeSheet.getRow(x);
			Cell cell = currentRow.getCell(0); //taking decision on only first cell for now
			if (cell != null && cell.getCellTypeEnum().equals(CellType.STRING)
					&& !cell.getStringCellValue().isEmpty()) {
				String value = cell.getStringCellValue();
				if (value.matches("END"))
					break;
				CourseBean courseBean = new CourseBean();
				courseBean
						.setCategory(currentRow.getCell(0) != null ? currentRow.getCell(0).getStringCellValue() : null);
				courseBean.setCode(currentRow.getCell(1) != null ? currentRow.getCell(1).getStringCellValue() : null);
				courseBean
						.setFullName(currentRow.getCell(2) != null ? currentRow.getCell(2).getStringCellValue() : null);
				courseBean.setShortName(
						currentRow.getCell(10) != null ? currentRow.getCell(10).getStringCellValue() : null);
				courseBean.setNumberOfSections(
						currentRow.getCell(11) != null ? (int) currentRow.getCell(11).getNumericCellValue() : 0);
				courseBean.setCreditHours(
						currentRow.getCell(12) != null ? (int) currentRow.getCell(12).getNumericCellValue() : 0);
				courseBean.setBatch(
						currentRow.getCell(13) != null ? (int) currentRow.getCell(13).getNumericCellValue() : 0);
				courseBean.setPlanning(currentRow.getCell(14) != null ? true : false);
				list.add(courseBean);
				System.out.println(x+"--- : "+courseBean.getFullName());
			}
				x++;		
		} while (true);
		cleanDataWorkbook.close();
		return list;

	}
}
