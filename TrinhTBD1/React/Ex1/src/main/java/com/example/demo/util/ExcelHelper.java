package com.example.demo.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    public static <T> void exportToExcel(OutputStream out, List<T> list, Class<T> clazz) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(clazz.getSimpleName() + "s");
            Field[] fields = clazz.getDeclaredFields();

            Row headerRow = sheet.createRow(0);
            int colIdx = 0;
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase("id")) {
                    continue;
                }
                Cell cell = headerRow.createCell(colIdx++);
                cell.setCellValue(GenericTableHelper.toTitleCase(field.getName()));
            }

            int rowIdx = 1;
            for (T item : list) {
                Row row = sheet.createRow(rowIdx++);
                colIdx = 0;
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object val = field.get(item);
                    Cell cell = row.createCell(colIdx++);
                    if (val == null) {
                        cell.setCellValue("");
                    } else if (val instanceof Number) {
                        cell.setCellValue(((Number) val).doubleValue());
                    } else if (val instanceof Boolean) {
                        cell.setCellValue((Boolean) val);
                    } else {
                        cell.setCellValue(val.toString());
                    }
                }
            }
            workbook.write(out);
        }
    }

    public static <T> List<T> importFromExcel(InputStream is, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Template header row is missing.");
            }

            Field[] fields = clazz.getDeclaredFields();
            List<String> expectedHeaders = new ArrayList<>();
            for (Field field : fields) {
                if (!field.getName().equalsIgnoreCase("id")) {
                    expectedHeaders.add(GenericTableHelper.toTitleCase(field.getName()).toLowerCase().replace(" ", ""));
                }
            }

            int cellCount = headerRow.getLastCellNum();
            if (cellCount < expectedHeaders.size()) {
                throw new IllegalArgumentException("Template header columns count is incorrect.");
            }

            for (int col = 0; col < expectedHeaders.size(); col++) {
                Cell cell = headerRow.getCell(col);
                if (cell == null || cell.getCellType() != CellType.STRING) {
                    throw new IllegalArgumentException("Template header is invalid.");
                }
                String actualHeader = cell.getStringCellValue().trim().toLowerCase().replace(" ", "");
                if (!expectedHeaders.contains(actualHeader)) {
                    throw new IllegalArgumentException("Template headers do not match the expected format.");
                }
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                T entity = clazz.getDeclaredConstructor().newInstance();
                int colIdx = 0;
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase("id")) {
                        continue;
                    }
                    Cell cell = row.getCell(colIdx++);
                    if (cell == null) {
                        continue;
                    }
                    field.setAccessible(true);
                    setFieldValue(entity, field, cell);
                }
                list.add(entity);
            }
        }
        return list;
    }

    private static void setFieldValue(Object entity, Field field, Cell cell) throws Exception {
        Class<?> type = field.getType();
        if (type == String.class) {
            cell.setCellType(CellType.STRING);
            field.set(entity, cell.getStringCellValue().trim());
        } else if (type == Double.class || type == double.class) {
            if (cell.getCellType() == CellType.NUMERIC) {
                field.set(entity, cell.getNumericCellValue());
            } else {
                field.set(entity, Double.parseDouble(cell.getStringCellValue().trim()));
            }
        } else if (type == Integer.class || type == int.class) {
            if (cell.getCellType() == CellType.NUMERIC) {
                field.set(entity, (int) cell.getNumericCellValue());
            } else {
                field.set(entity, Integer.parseInt(cell.getStringCellValue().trim()));
            }
        } else if (type == Long.class || type == long.class) {
            if (cell.getCellType() == CellType.NUMERIC) {
                field.set(entity, (long) cell.getNumericCellValue());
            } else {
                field.set(entity, Long.parseLong(cell.getStringCellValue().trim()));
            }
        } else if (type == Boolean.class || type == boolean.class) {
            if (cell.getCellType() == CellType.BOOLEAN) {
                field.set(entity, cell.getBooleanCellValue());
            } else {
                field.set(entity, Boolean.parseBoolean(cell.getStringCellValue().trim()));
            }
        }
    }
}
