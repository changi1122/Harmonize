package com.example.harmonize.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.UserVoiceRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

public class Analyzer {

    @Autowired
    private UserVoiceRepository userVoiceRepository;

    public Double GetPossibility(Music list, UserVoice userVoice) {
        Double max_pos = (list.getMax() > userVoice.getMax()) ? userVoice.getMax() : list.getMax();
        Double min_pos = (list.getMin() > userVoice.getMin()) ? list.getMin() : userVoice.getMin();
        return (max_pos - min_pos) / (list.getMax() - list.getMin()) * 100;
    }

    public void FindMusicRange() throws IOException {
        List<Double> list = new ArrayList<>();

        String excelFilePath = System.getProperty("user.dir")+"/src/main/resources/excel/test.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        int cnt = 0;
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;

        int sheetIndex = 0;
        Row row;
        Cell cell;
        Double value;
        double[][] data = new double[2][workbook.getSheetAt(sheetIndex).getLastRowNum()];

        for (int i = 1; i <= workbook.getSheetAt(sheetIndex).getLastRowNum(); i++) {
            row = workbook.getSheetAt(sheetIndex).getRow(i);
            cell = row.getCell(1);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        data[0][cnt] = value;
                        break;
                    default:
                        break;
                }
            }
            cell = row.getCell(2);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        data[1][cnt] = value;
                        if (value >= max) {
                            max = value;
                        }
                        if (value <= min) {
                            min = value;
                        }
                        cnt++;
                        break;
                    default:
                        break;
                }
            }
        }

        workbook.close();
        inputStream.close();

        list.add(max);
        list.add(min);
    }
}