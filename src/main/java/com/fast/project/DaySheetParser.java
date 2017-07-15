package com.fast.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fast.project.pojo.CourseBean;

public class DaySheetParser {
	private static final String FILE_NAME = "BSCS-Spring-2017-Timetable-V5.xlsx";
	private static final String CLEAN_DATA_FILE = "C:\\Users\\Haider\\Desktop\\CleanData.xlsx";
	private static final int DAY = 0;
	private static final int TIMESLOT = 1;
	private static final int ROOM = 2;
	private static final int COURSE = 3;
	private static final int SECTION = 4;
	private static final int TEACHER = 5;

	public static List<String> COURSES = new ArrayList<String>();
	public static List<String> LAB_COURSES = new ArrayList<String>();
	public static List<String> CALCULUS = new ArrayList<String>(Arrays.asList("Cal-II", "Cal-I"));

	public static Map<Integer, String> ROOMS = new HashMap<>();
	public static List<String> TIME_SLOTS = new ArrayList<>();

	public static void perform() {

		try {

			extractCourseShortNames();

			DaySheetParser daySheetParser = new DaySheetParser();
			FileInputStream timeTable = new FileInputStream(daySheetParser.getFileFromResource(FILE_NAME));
			Workbook readWorkbook = new XSSFWorkbook(timeTable);
			//
			XSSFWorkbook writeWorkBook = new XSSFWorkbook();
			XSSFSheet writeSheet = writeWorkBook.createSheet("TimeTable_Sheet");
			//
			int writeRowCount = 0;
			Row heading = writeSheet.createRow(writeRowCount);
			heading.createCell(DAY).setCellValue("Day");
			heading.createCell(TIMESLOT).setCellValue("Time Slot");
			heading.createCell(ROOM).setCellValue("Room");
			heading.createCell(COURSE).setCellValue("Course");
			heading.createCell(SECTION).setCellValue("Section");
			heading.createCell(TEACHER).setCellValue("Teacher");
			writeRowCount++;

			for (int sheet = 6; sheet <= 10; sheet++) {
				Sheet currentSheet = readWorkbook.getSheetAt(sheet);
				System.out.println("Sheet: " + currentSheet.getSheetName());
				extractOrVerifyRooms(currentSheet);
				extractTimeSlots(currentSheet);

				// if (currentSheet.getSheetName().contains("Friday") ||
				// currentSheet.getSheetName().contains("FRIDAY")) {
				// // need to skip 6 column
				// } else {

				for (int row = 4; row < 29; row++) {
					if (row != 22) {
						Row currentRow = currentSheet.getRow(row);
						for (int col = 1; col <= 8; col++) {
							if (sheet == 10 && col == 6) {

							} else {
								Cell cell = currentRow.getCell(col);
								if (cell != null && cell.getCellTypeEnum().equals(CellType.STRING)) {
									writeRowCount++;
									if (row < 22) {
										Row writeRow = writeSheet.createRow(writeRowCount);
										writeRow.createCell(DAY).setCellValue(currentSheet.getSheetName());
										writeRow.createCell(TIMESLOT).setCellValue(TIME_SLOTS.get(col - 1));
										writeRow.createCell(ROOM).setCellValue(ROOMS.get(row));
										String dirty = cell.getStringCellValue();
										System.out.println(dirty);
										String[] courseSecTeacher = dirty.split("\\s{8,}");

										if (courseSecTeacher[0].contains(CALCULUS.get(0))
												|| courseSecTeacher[0].contains(CALCULUS.get(1))) {
											for (String name : CALCULUS) {
												if (courseSecTeacher[0].contains(name)) {
													System.out.println(name);
													writeRow.createCell(COURSE).setCellValue(name);
													String[] courseSec = courseSecTeacher[0].split(name);
													String section = "";
													if (courseSec[1].trim().charAt(0) == '-') {
														if (courseSec[1].trim().contains("("))
															section = courseSec[1].trim().substring(1,
																	courseSec[1].trim().indexOf('('));
														else
															section = courseSec[1].trim().substring(1);
													} else {
														if (courseSec[1].trim().contains("("))
															section = courseSec[1].trim().substring(0,
																	courseSec[1].trim().indexOf('('));
														else
															section = courseSec[1].trim();
													}
													section.replace("Tutorial", "");
													writeRow.createCell(SECTION).setCellValue(section.trim().toUpperCase());
													System.out.println(section.trim());
													break;
													// System.out.println(courseSec[1]);
												}
											}
										} else {
											for (String name : COURSES) {
												if (courseSecTeacher[0].contains(name)) {
													System.out.println(name);
													writeRow.createCell(COURSE).setCellValue(name);
													String[] courseSec = courseSecTeacher[0].split(name);
													String section = "";
													if (courseSec[1].trim().charAt(0) == '-') {
														if (courseSec[1].trim().contains("("))
															section = courseSec[1].trim().substring(1,
																	courseSec[1].trim().indexOf('('));
														else
															section = courseSec[1].trim().substring(1);
													} else {
														if (courseSec[1].trim().contains("("))
															section = courseSec[1].trim().substring(0,
																	courseSec[1].trim().indexOf('('));
														else
															section = courseSec[1].trim();
													}
													section = section.replace("Tutorial", "");
													writeRow.createCell(SECTION).setCellValue(section.trim().toUpperCase());
													System.out.println(section.trim());
													break;
													// System.out.println(courseSec[1]);
												}
											}
										}
										writeRow.createCell(TEACHER).setCellValue(courseSecTeacher[1].trim());
										System.out.println(courseSecTeacher[1]);
									} else {
										if (row > 22) {
											// Row writeRow =
											// writeSheet.createRow(writeRowCount);
											// writeRow.createCell(0).setCellValue(TIME_SLOTS.get(col
											// - 1));
											// writeRow.createCell(1).setCellValue(ROOMS.get(row));
											String dirty = cell.getStringCellValue();
											System.out.println(dirty);
											String[] courseSecTeacher = dirty.split("\\s{8,}");
											boolean notInLab = true;
											for (String name : LAB_COURSES) {
												if (courseSecTeacher[0].contains(name)) {
													notInLab = false;
													System.out.println(name);
													// writeRow.createCell(2).setCellValue(name);
													String[] courseSec = courseSecTeacher[0].split(name);
													if (courseSec[1].trim().contains("+")) {
														// Lab special case when
														// two sections are
														// combined
														String section[] = courseSec[1].trim().split("\\+");
														for (int s = 0; s < section.length; s++) {
															if (section[s].trim().charAt(0) == '-') {
																if (section[s].trim().contains("("))
																	section[s] = section[s].trim().substring(1,
																			section[s].trim().indexOf('('));
																else
																	section[s] = section[s].trim().substring(1);
															} else {
																if (section[s].trim().contains("("))
																	section[s] = section[s].trim().substring(0,
																			section[s].trim().indexOf('('));
																else
																	section[s] = section[s].trim();
															}
														}
														String teacher[] = null;
														if (courseSecTeacher[1].trim().contains("+")) {
															teacher = courseSecTeacher[1].trim().split("\\+");
														}
														for (int i = 0; i < section.length; i++) {
															Row writeRow = writeSheet.createRow(writeRowCount);
															writeRow.createCell(DAY)
																	.setCellValue(currentSheet.getSheetName());
															writeRow.createCell(TIMESLOT)
																	.setCellValue(TIME_SLOTS.get(col - 1));
															writeRow.createCell(ROOM).setCellValue(ROOMS.get(row));
															writeRow.createCell(COURSE).setCellValue(name);
															section[i] = section[i].replace("Tutorial", "");
															writeRow.createCell(SECTION)
																	.setCellValue(section[i].trim().toUpperCase());
															System.out.println(section[i].trim());
															writeRow.createCell(TEACHER)
																	.setCellValue(teacher[i].trim());
															System.out.println(teacher[i]);
															writeRowCount++;
														}
													} else {
														String section = "";
														if (courseSec[1].trim().charAt(0) == '-') {
															if (courseSec[1].trim().contains("("))
																section = courseSec[1].trim().substring(1,
																		courseSec[1].trim().indexOf('('));
															else
																section = courseSec[1].trim().substring(1);
														} else {
															if (courseSec[1].trim().contains("("))
																section = courseSec[1].trim().substring(0,
																		courseSec[1].trim().indexOf('('));
															else
																section = courseSec[1].trim();
														}
														Row writeRow = writeSheet.createRow(writeRowCount);
														writeRow.createCell(DAY)
																.setCellValue(currentSheet.getSheetName());
														writeRow.createCell(TIMESLOT)
																.setCellValue(TIME_SLOTS.get(col - 1));
														writeRow.createCell(ROOM).setCellValue(ROOMS.get(row));
														writeRow.createCell(COURSE).setCellValue(name);
														section = section.replace("Tutorial", "");
														writeRow.createCell(SECTION).setCellValue(section.trim().toUpperCase());
														System.out.println(section.trim());
														writeRow.createCell(TEACHER)
																.setCellValue(courseSecTeacher[1].trim());
														System.out.println(courseSecTeacher[1]);
													}
													break;
												}
											}
											if (notInLab) {
												for (String name : COURSES) {
													if (courseSecTeacher[0].contains(name)) {
														System.out.println(name);
														// writeRow.createCell(2).setCellValue(name);
														String[] courseSec = courseSecTeacher[0].split(name);
														if (courseSec[1].trim().contains("+")) {
															// Lab special case
															// when
															// two sections are
															// combined
															String section[] = courseSec[1].trim().split("\\+");
															for (int s = 0; s < section.length; s++) {
																if (section[s].trim().charAt(0) == '-') {
																	if (section[s].trim().contains("("))
																		section[s] = section[s].trim().substring(1,
																				section[s].trim().indexOf('('));
																	else
																		section[s] = section[s].trim().substring(1);
																} else {
																	if (section[s].trim().contains("("))
																		section[s] = section[s].trim().substring(0,
																				section[s].trim().indexOf('('));
																	else
																		section[s] = section[s].trim();
																}
															}
															String teacher[] = null;
															if (courseSecTeacher[1].trim().contains("+")) {
																teacher = courseSecTeacher[1].trim().split("\\+");
															}
															for (int i = 0; i < section.length; i++) {
																Row writeRow = writeSheet.createRow(writeRowCount);
																writeRow.createCell(DAY)
																		.setCellValue(currentSheet.getSheetName());
																writeRow.createCell(TIMESLOT)
																		.setCellValue(TIME_SLOTS.get(col - 1));
																writeRow.createCell(ROOM).setCellValue(ROOMS.get(row));
																writeRow.createCell(COURSE).setCellValue(name);
																section[i] = section[i].replace("Tutorial", "");
																writeRow.createCell(SECTION)
																		.setCellValue(section[i].trim().toUpperCase());
																System.out.println(section[i].trim());
																writeRow.createCell(TEACHER)
																		.setCellValue(teacher[i].trim());
																System.out.println(teacher[i]);
																writeRowCount++;
															}
														} else {
															String section = "";
															if (courseSec[1].trim().charAt(0) == '-') {
																if (courseSec[1].trim().contains("("))
																	section = courseSec[1].trim().substring(1,
																			courseSec[1].trim().indexOf('('));
																else
																	section = courseSec[1].trim().substring(1);
															} else {
																if (courseSec[1].trim().contains("("))
																	section = courseSec[1].trim().substring(0,
																			courseSec[1].trim().indexOf('('));
																else
																	section = courseSec[1].trim();
															}
															Row writeRow = writeSheet.createRow(writeRowCount);
															writeRow.createCell(DAY)
																	.setCellValue(currentSheet.getSheetName());
															writeRow.createCell(TIMESLOT)
																	.setCellValue(TIME_SLOTS.get(col - 1));
															writeRow.createCell(ROOM).setCellValue(ROOMS.get(row));
															writeRow.createCell(COURSE).setCellValue(name);
															section = section.replace("Tutorial", "");
															writeRow.createCell(SECTION).setCellValue(section.trim().toUpperCase());
															System.out.println(section.trim());
															writeRow.createCell(TEACHER)
																	.setCellValue(courseSecTeacher[1].trim());
															System.out.println(courseSecTeacher[1]);
														}
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			writeRowCount+=2;
			Row writeRow = writeSheet.createRow(writeRowCount);
			for(int i = 0 ; i < 6 ; i++){
				writeRow.createCell(i).setCellValue("END");
			}
			readWorkbook.close();
			FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Haider\\Desktop\\TimeTable.xlsx");
			writeWorkBook.write(outputStream);
			writeWorkBook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void extractCourseShortNames() throws Exception {
		FileInputStream cleanData = new FileInputStream(CLEAN_DATA_FILE);
		Workbook cleanDataWorkbook = new XSSFWorkbook(cleanData);
		Sheet datatypeSheet = cleanDataWorkbook.getSheetAt(0);
		int Course_Short = 10;
		int x = 2;
		do {
			Row currentRow = datatypeSheet.getRow(x);
			Cell cell = currentRow.getCell(Course_Short);
			if (cell != null && cell.getCellTypeEnum().equals(CellType.STRING)
					&& !cell.getStringCellValue().isEmpty()) {
				String value = cell.getStringCellValue();
				if (value.matches("END"))
					break;
				if (!value.contains("Cal")) {
					if (value.equals("Eng"))
						COURSES.add("Eng lang");
					else if (value.matches("([\\w]+-Lab[\\w]*)"))
						LAB_COURSES.add(value.trim());
					else
						COURSES.add(value.trim());
				}
			}
			x++;
		} while (true);

	}


	public static boolean isLabHeadingRow(Row row) {
		if (row.getCell(0).getCellTypeEnum() == CellType.STRING && row.getCell(0).getStringCellValue().equals("LABS")
				&& row.getCell(2).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(3).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(4).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(5).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(6).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(7).getCellTypeEnum() == CellType.BLANK
				&& row.getCell(8).getCellTypeEnum() == CellType.BLANK)
			return true;
		return false;
	}

	public static void extractOrVerifyRooms(Sheet sheet) {
		System.out.println("======== ROOMS ==========");
		if (ROOMS.isEmpty()) {
			for (int r = 4; r < 29; r++) {
				Row row = sheet.getRow(r);
				Cell cell = row.getCell(0);
				if (cell != null && cell.getCellTypeEnum().equals(CellType.STRING)
						&& !cell.getStringCellValue().isEmpty()) {
					System.out.println(cell.getStringCellValue().trim());
					ROOMS.put(r, cell.getStringCellValue().trim());
				}
			}
		} else {
			// for(int r = 4 ; r < 29 ; r++){
			// Row row = sheet.getRow(r);
			// Cell cell = row.getCell(0);
			// if(cell != null && rooms.)){
			//
			// }
			// }
		}
	}

	public static void extractTimeSlots(Sheet sheet) {
		System.out.println("======== TIMESLOTS ==========");
		if (TIME_SLOTS.isEmpty()) {
			Row row = sheet.getRow(2);
			for (int c = 1; c <= 8; c++) {
				Cell cell = row.getCell(c);
				if (cell != null) {
					System.out.println(cell.getStringCellValue().trim());
					TIME_SLOTS.add(cell.getStringCellValue().trim());
				}
			}
		}
	}

	public File getFileFromResource(String name) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(name).getFile());
			return file;
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			return null;
		}
	}
}
