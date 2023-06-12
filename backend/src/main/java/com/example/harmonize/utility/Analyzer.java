package com.example.harmonize.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.UserVoiceRepository;
import org.apache.commons.math3.analysis.function.Max;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.mapping.MappingHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class Analyzer {

    int number=0;

    int MaxRow=0;

    public Double GetPossibility(Music list, UserVoice userVoice) {
        Double max_pos = (list.getMax() > userVoice.getMax()) ? userVoice.getMax() : list.getMax();
        Double min_pos = (list.getMin() > userVoice.getMin()) ? list.getMin() : userVoice.getMin();
        return (max_pos - min_pos) / (list.getMax() - list.getMin()) * 100;
    }

    public Map<String, double []> GetGraphData(String Excel, String mid) throws Exception {
        MaxRow =0;
        double [] music = ListDoubleToInt(ExcelToList(mid));
        double [] user = ListDoubleToInt(ExcelToList(Excel));

        double [] diffRate = new double[MaxRow];
        double rate=0.0, sub, m, u;

        for(int i=0; i<MaxRow; i++){

            if(i>=music.length){
                m=0.0;
            }else{
                m = music[i];
            }

            if(i>=user.length){
                u=0.0;
            }else{
                u= user[i];
            }

            sub = Math.abs((m-u));
            if(m ==0.0){
                if(u==0.0) {
                    diffRate[i]=0.0;
                }
            }else{
                diffRate[i] = (sub*100.0)/m;
            }
            rate += diffRate[i];
        }

        System.out.println(100.0 - rate/MaxRow + " "+ Math.round((100.0 - rate/MaxRow)*10.0)/10.0);
        double [] doubles = new double[1];
        doubles[0] = (100.0 - rate/MaxRow)*100.0/100.0;

        Map<String, double []> map = new HashMap<String, double[]>();
        map.put("rate", doubles);
        map.put("user", user);
        map.put("music", music);

        return map;
    }

    public double[] ListDoubleToInt(double[][] doubles){
        double[] cnvt = new double[MaxRow];
        int index=0;
        for(int i=0; i<MaxRow; i++){
            if(index <doubles[0].length && i==doubles[0][index]){
                cnvt[i] = doubles[1][index++];
            }else{
                cnvt[i] =0;
            }
        }
        return cnvt;
    }



    //user 녹음파일 저장 UC{userid}
    public double[][] ExcelToList(String fileName) throws Exception {
        String excelFilePath = System.getProperty("user.dir") + "/src/main/resources/excel/"+fileName+".xlsx";

        if(new File(excelFilePath).exists() == false){
            throw new Exception("No file");
        }

        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        int cnt =0, sheetIndex=0;
        Row row;
        Cell cell;
        Double value;

        int rows = workbook.getSheetAt(sheetIndex).getLastRowNum();

        double[][] data = new double[2][rows];

        for (int i = 1; i <= rows; i++) {
            row = workbook.getSheetAt(sheetIndex).getRow(i);
            cell = row.getCell(1);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        if(MaxRow <value){
                            MaxRow = value.intValue();
                        }
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
                        cnt++;
                        break;
                    default:
                        break;
                }
            }
        }

        System.out.println(MaxRow);
        return data;
    }

    synchronized public Boolean JudgmentRate(String fileName, String uid) throws IOException {
        String excelFilePath = System.getProperty("user.dir") + "/src/main/resources/excel/U"+fileName+".xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        System.out.println(workbook);

        int cnt = 0;

        int sheetIndex = 0;
        Row row;
        Cell cell;
        Double value;
        int rows = workbook.getSheetAt(sheetIndex).getLastRowNum();

        double data = 0.0;
        String scale = fileName.replaceAll("U", "");
        scale = scale.replaceAll(uid, "");
        System.out.println(scale);

        double basic = GetRefer(scale);

        for (int i = 1; i <= rows; i++) {
            row = workbook.getSheetAt(sheetIndex).getRow(i);
            cell = row.getCell(2);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = Double.parseDouble(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        data += value;
                        cnt++;
                        break;
                    default:
                        break;
                }
            }
        }

        System.out.println(data + " " + cnt +" " + data/(double)cnt);

        System.out.println(basic+ " "+ data/(double)cnt + " "+ data/(double)cnt*100/basic);

        double rate = (data/(double)cnt)*100/basic;

        rate = Math.abs(100.0 -rate);

        System.out.println(rate);

        if(rate <= 5.0){
            return true;
        }else{
            return false;
        }
    }

    public List<Double> FindMusicRange(String fileName, String gender) throws IOException {
        List<Double> list = new ArrayList<>();

        String excelFilePath = System.getProperty("user.dir")+"/src/main/resources/excel/" + fileName+".xlsx";
        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));

        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        int cnt = 0;
        double max = Double.MIN_VALUE, min = Double.MAX_VALUE;

        int sheetIndex = 0;
        Row row;
        Cell cell;
        Double value;

        int rows = workbook.getSheetAt(sheetIndex).getLastRowNum();

        double[][] data = new double[2][rows];

        System.out.println(data);

        for (int i = 1; i <= rows; i++) {
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

        double [] level = MeasureLevel(data, rows, gender);

        System.out.println("max" + max);
        System.out.println("min" + min);
        double Maxline=0.0, Minline=0.0;
        if(gender.equals("m")){
            Maxline = ManMapping(Math.round(max*100)/100.0);
            Minline = ManMapping(Math.round(min*100)/100.0);
        }else if(gender.equals("g")){
            Maxline = GirlMapping(Math.round(max*100)/100.0);
            Minline = GirlMapping(Math.round(min*100)/100.0);
        }

        System.out.println(level[0]+" "+level[1]+" " + level[2]*100/Maxline + " " + level[3]*100/Minline);
        System.out.println(ClacRate(level[0]) + " "+ ClacRate(level[1]) + " "+ ClacRate(level[2]*100/Maxline)+ " " + ClacRate(level[3]*100/Minline) + " " + MaxRate(max, gender) + " "+ MinRate(min, gender));
        int score = 0;

        score += ClacRate(level[0]);
        score += ClacRate(level[1]);
        score += ClacRate(level[2]*100/Maxline);
        score += ClacRate(level[3]*100/Minline);
        score += MaxRate(max, gender);
        score += MinRate(min, gender);

        //System.out.println(score);
        workbook.close();
        inputStream.close();

        list.add(max);
        list.add(min);
        list.add(level[0]);
        list.add(level[1]);
        if(score==15){
            list.add(2.0);
        }else{
            //System.out.println(score/5.0);
            list.add(score/5.0);
        }
        return list;
    }

    public int ClacRate(double rate){
        switch ((int) (rate/10)){
            case 0:
            case 1:
            case 2:
                return 1;
            case 3:
            case 4:
            case 5:
                return 2;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                return 3;
        }
        return 0;
    }

    public int MaxRate(double rate, String gender){
        int x=0;
        if(gender.equals("m")){
            if(rate >=0.6){
                x=3;
            }else if(rate >= 0.47){
                x=2;
            }else{
                x=1;
            }
        }else{
            if(rate >=0.74){
                x=3;
            }else if(rate >= 0.66){
                x=2;
            }else{
                x=1;
            }
        }
        return x;
    }

    public int MinRate(double rate, String gender){
        int x=0;
        if(gender.equals("m")){
            if(rate <0.19){
                x=3;
            }else if(rate < 0.25){
                x=2;
            }else{
                x=1;
            }
        }else{
            if(rate <0.38){
                x=3;
            }else if(rate < 0.44){
                x=2;
            }else{
                x=1;
            }
        }
        return x;
    }


    public double[] MeasureLevel(double [][] data, int max, String gender ){

        int tt = (int) data[0][max-1];
        Double [] doubles = new Double[tt+1];

        int [] test = new int[100];
        double [] duration = new double[2];
        double [] result = new double[4];
        // High, Low, change_rate score, average
        int High=0, Low= 0;
        int highlow  =0;
        // 음의 갑작스런 변동 확인

        double standard = 0.0;
        int start = 0;
        double maxx = 0.0;
        double pitchSum = 0.0;

        Arrays.fill(test, 0);
        Arrays.fill(duration, 0.0);

        int k=0;
        doubles[0] = 0.0;
        double diff=0.0;
        for(int i=1; i<doubles.length; i++ ) {
            if (i == data[0][k]) {
                doubles[i] = data[1][k++];
                if(doubles[i-1] != 0){
                    //System.out.println("test: "+ (Math.abs( Math.round(doubles[i]*100)/100.0 - Math.round(doubles[i-1]*100)/100.0)));
                    if(gender.equals("m")){
                        //System.out.println( doubles[i]+" : "+(Math.round(doubles[i]*100)/100.0)+" "+doubles[i]+" : " + Math.round(doubles[i-1]*100)/100.0);
                        diff = ManMapping(Math.round(doubles[i]*100)/100.0) - ManMapping(Math.round(doubles[i-1]*100)/100.0);
                    }else if(gender.equals("g")){
                        diff = GirlMapping(Math.round(doubles[i]*100)/100.0) - GirlMapping(Math.round(doubles[i-1]*100)/100.0);
                    }

                    if(Math.abs(diff) >= 0.6){
                        highlow++;
                    }
                }
            } else {
                doubles[i] = 0.0;
            }
        }

        for(int i=1; i<doubles.length; i++){
            double pitch = Math.round(doubles[i]*100)/100.0;
            if (standard ==0.0 && pitch != 0.0){
                standard=pitch;
                maxx=pitch;
                start = i;
                pitchSum+= pitch;

                if(i+1== doubles.length){
                    duration[1] += MultiDiff(standard, pitchSum, i+1, start);
                }
                continue;
            }
            if((standard != 0.0 && pitch == 0.0)){
                duration[1] += MultiDiff(standard, pitchSum, i, start);
                standard=0.0;
                maxx=0.0;
                start = i;
                pitchSum= 0.0;
                continue;
            }
            if(standard != pitch){
                if(standard > pitch){
                    duration[1] += MultiDiff(standard, pitchSum, i, start);
                    standard = pitch;
                    start = i;
                    maxx = pitch;
                    pitchSum = 0.0;
                    pitchSum += pitch;
                }else{
                    pitchSum += pitch;
                    if(maxx <= pitch){
                        maxx = pitch;
                    }
                }
            }else{
                pitchSum += pitch;
                if(maxx <= pitch){
                    maxx = pitch;
                }
            }
        }

        for(int i=1; i<doubles.length; i++){
            int idx = (int)(Math.round(doubles[i]*100)/100.0 * 100);
            if(idx > 15) {
                test[idx]++;

                if(gender.equals("m")){
                    if(idx > 47){
                        High++;
                    }
                    if(idx<=30){
                        Low++;
                    }
                }else if(gender.equals("g")){
                    if(idx > 66){
                        High++;
                    }
                    if(idx<=49){
                        Low++;
                    }
                }
            }
        }

        int Hole=0;
        int [] total = new int[2];
        Arrays.fill(total, 0);
        double [] total2= new double[2];
        Arrays.fill(total2, 0.0);

        for(int i=0; i<100; i++){
            if(test[i]!=0){
                //System.out.println(i+" : "+test[i] + " : "+ (test[i]*100/(double)total) + "  " + (i/100.0)+ " " + test[i]*ManMapping((i/100.0)));
                Hole += test[i];
                if(gender.equals("m")){
                    if(i > 47){
                        total[0]+= test[i];
                        total2[0] +=test[i]*ManMapping((i/100.0));
                    }
                    if(i<=30){
                        total[1]+= test[i];
                        total2[1] +=test[i]*ManMapping((i/100.0));
                    }
                }else if(gender.equals("g")){
                    if(test[i] > 66){
                        total[0]+=test[i];
                        total2[0] +=test[i]*GirlMapping((i/100.0));
                    }
                    if(test[i]<=49){
                        total[1]+=test[i];
                        total2[1] +=test[i]*GirlMapping((i/100.0));
                    }
                }
            }
        }

        double HigtPitch = (total[0]==0 ? 0 : total2[0]/total[0]);
        double LowPitch = (total[1]==0 ? 0 : total2[1]/total[1]);

        //System.out.println("Average : " + HigtPitch + " "+ total[0]+ " AND " + LowPitch +" " + total[1] + " : " + Math.round((HigtPitch + LowPitch)*10)/10.0);
        //System.out.println("Change high and low  : " + highlow);
        //System.out.println("High : " + Math.round(High*100.0/(double)Hole * 10)/10.0 + " ,  Low : " + Math.round(Low*100.0/(double)Hole*10)/10.0);
        result[0] = Math.round(High*100.0/(double)Hole * 10)/10.0;
        result[1] = Math.round(Low*100.0/(double)Hole*10)/10.0;
        result[2] = Math.round((HigtPitch)*10)/10.0;
        result[3] = Math.round((LowPitch)*10)/10.0;

        return result;
    }

    public double MultiDiff(double standard, double pitchSum, int i, int start){
        double avg= (Math.round((pitchSum/((i)-start))*1000)/1000.0);
        double err= (avg-standard);

        // System.out.println(start + " -> "+(i)+" : "+standard + ", rate : " + ((i)-start) +  " : "+pitchSum);
        //System.out.println(standard + "/"+ avg + "/"+ err);
        //duration += (Math.round(err)*1000/1000.0)*((i)-start);
        //System.out.println(err);

        //System.out.println(start+"~"+ i+ "번 가중치" + standard + " 기준 값  : " + avg + " 값은 :" + ManMapping( Math.round(avg*100)/100.0));
        //System.out.println("2번 차이 * 시간 값  : " + Math.round(err*1000)/1000.0 * (i-start));
        return Math.round(err*1000)/1000.0 * (i-start);
    }


    public double ManMapping(Double avg){
        Map<Double, Double> basicMap = new HashMap<>();
        basicMap.put(0.11, 8.0);
        basicMap.put(0.12, 7.7);
        basicMap.put(0.13, 7.3);
        basicMap.put(0.14, 7.0);
        basicMap.put(0.15, 6.7);
        basicMap.put(0.16, 6.3);
        basicMap.put(0.17, 6.0);
        basicMap.put(0.18, 5.5);
        basicMap.put(0.19, 5.0);
        basicMap.put(0.20, 4.7);
        basicMap.put(0.21, 4.3);
        basicMap.put(0.22, 4.0);
        basicMap.put(0.23, 3.7);
        basicMap.put(0.24, 3.3);
        basicMap.put(0.25, 3.0);
        basicMap.put(0.26, 2.7);
        basicMap.put(0.27, 2.3);
        basicMap.put(0.28, 2.0);
        basicMap.put(0.29, 1.5);
        basicMap.put(0.30, 1.0); //30
        basicMap.put(0.31, 1.0); //31
        basicMap.put(0.32, 1.0); //32
        basicMap.put(0.33, 1.0); //33
        basicMap.put(0.34, 1.0); //34
        basicMap.put(0.35, 1.0); //35
        basicMap.put(0.36, 1.0); //36
        basicMap.put(0.37, 1.0); //37
        basicMap.put(0.38, 1.0); //38
        basicMap.put(0.39, 1.0); //39
        basicMap.put(0.40, 1.0); //40
        basicMap.put(0.41, 1.0); //41
        basicMap.put(0.42, 1.3);
        basicMap.put(0.43, 1.7);
        basicMap.put(0.44, 2.0); //0.44
        basicMap.put(0.45, 2.0); //0.45
        basicMap.put(0.46, 2.0); //0.46
        basicMap.put(0.47, 2.0); //0.47
        basicMap.put(0.48, 2.5); //0.48
        basicMap.put(0.49, 3.0); //0.49
        basicMap.put(0.50, 3.3); //0.50
        basicMap.put(0.51, 3.7); //0.51
        basicMap.put(0.52, 4.0); //0.52
        basicMap.put(0.53, 4.3); //0.53
        basicMap.put(0.54, 4.7); //0.54
        basicMap.put(0.55, 5.0); //0.55
        basicMap.put(0.56, 5.5);
        basicMap.put(0.57, 6.0); //0.57
        basicMap.put(0.58, 6.3); //0.58
        basicMap.put(0.59, 6.7); //0.59
        basicMap.put(0.60, 7.0); //0.60
        basicMap.put(0.61, 7.3); //0.61
        basicMap.put(0.62, 7.7); //0.62
        basicMap.put(0.63, 8.0); //0.63
        basicMap.put(0.64, 8.3); //0.64
        basicMap.put(0.65, 8.7); //0.65
        basicMap.put(0.66, 9.0); //0.66
        basicMap.put(0.67, 9.5); //0.67
        basicMap.put(0.68, 10.0); //0.68
        basicMap.put(0.69, 10.3); //0.69
        basicMap.put(0.70, 10.7); //0.70
        basicMap.put(0.71, 11.0); //0.71
        basicMap.put(0.72, 11.3); //0.72
        basicMap.put(0.73, 11.7); //0.73
        basicMap.put(0.74, 12.0); //0.74
        basicMap.put(0.75, 12.5); //0.75
        basicMap.put(0.76, 13.0); //0.76
        basicMap.put(0.77, 13.3); //0.77
        basicMap.put(0.78, 13.7); //0.78
        basicMap.put(0.79, 14.0); //0.79
        basicMap.put(0.80, 14.3); //0.80
        basicMap.put(0.81, 14.7); //0.81
        basicMap.put(0.82, 15.0); //0.82
        basicMap.put(0.83, 15.3); //0.83
        basicMap.put(0.84, 15.7); //0.84
        basicMap.put(0.85, 16.0); //0.85
        basicMap.put(0.86, 16.5); //0.86
        basicMap.put(0.87, 17.0); //0.87
        basicMap.put(0.88, 17.3); //0.88
        basicMap.put(0.89, 17.7); //0.89
        basicMap.put(0.90, 18.0); //0.90

        //System.out.println("Test print " + basicMap.get(avg) + " Taken avg : " + avg +"\n");
        if(basicMap.get(avg) == null){
            if(avg < 0.11){
                return 9.0;
            }
            return 19.0;
        }else{
            return basicMap.get(avg);
        }
    }


    public double GirlMapping(Double avg){
        Map<Double, Double> basicMap = new HashMap<>();
        basicMap.put(0.11, 15.0);
        basicMap.put(0.12, 14.7);
        basicMap.put(0.13, 14.3);
        basicMap.put(0.14, 14.0);
        basicMap.put(0.15, 13.7);
        basicMap.put(0.16, 13.3);
        basicMap.put(0.17, 13.0);
        basicMap.put(0.18, 12.5);
        basicMap.put(0.19, 12.0);
        basicMap.put(0.20, 11.7);
        basicMap.put(0.21, 11.3);
        basicMap.put(0.22, 11.0);
        basicMap.put(0.23, 10.7);
        basicMap.put(0.24, 10.3);
        basicMap.put(0.25, 10.0);
        basicMap.put(0.26, 9.7);
        basicMap.put(0.27, 9.3);
        basicMap.put(0.28, 9.0);
        basicMap.put(0.29, 8.5);
        basicMap.put(0.30, 8.0); //30
        basicMap.put(0.31, 7.7); //31
        basicMap.put(0.32, 7.3); //32
        basicMap.put(0.33, 7.0); //33
        basicMap.put(0.34, 6.7); //34
        basicMap.put(0.35, 6.3); //35
        basicMap.put(0.36, 6.0); //36
        basicMap.put(0.37, 5.5); //37
        basicMap.put(0.38, 5.0); //38
        basicMap.put(0.39, 4.7); //39
        basicMap.put(0.40, 4.3); //40
        basicMap.put(0.41, 4.0); //41
        basicMap.put(0.42, 3.7);
        basicMap.put(0.43, 3.3);
        basicMap.put(0.44, 3.0); //0.44
        basicMap.put(0.45, 2.7); //0.45
        basicMap.put(0.46, 2.3); //0.46
        basicMap.put(0.47, 2.0); //0.47
        basicMap.put(0.48, 1.5); //0.48
        basicMap.put(0.49, 1.0); //0.49
        basicMap.put(0.50, 1.0); //0.50
        basicMap.put(0.51, 1.0); //0.51
        basicMap.put(0.52, 1.0); //0.52
        basicMap.put(0.53, 1.0); //0.53
        basicMap.put(0.54, 1.0); //0.54
        basicMap.put(0.55, 1.0); //0.55
        basicMap.put(0.56, 1.0);
        basicMap.put(0.57, 1.0); //0.57
        basicMap.put(0.58, 1.0); //0.58
        basicMap.put(0.59, 1.0); //0.59
        basicMap.put(0.60, 1.0); //0.60
        basicMap.put(0.61, 1.3); //0.61
        basicMap.put(0.62, 1.7); //0.62
        basicMap.put(0.63, 2.0); //0.63
        basicMap.put(0.64, 2.3); //0.64
        basicMap.put(0.65, 2.7); //0.65
        basicMap.put(0.66, 3.0); //0.66
        basicMap.put(0.67, 4.5); //0.67
        basicMap.put(0.68, 5.0); //0.68
        basicMap.put(0.69, 5.3); //0.69
        basicMap.put(0.70, 5.7); //0.70
        basicMap.put(0.71, 6.0); //0.71
        basicMap.put(0.72, 6.3); //0.72
        basicMap.put(0.73, 6.7); //0.73
        basicMap.put(0.74, 7.0); //0.74
        basicMap.put(0.75, 7.5); //0.75
        basicMap.put(0.76, 8.0); //0.76
        basicMap.put(0.77, 8.3); //0.77
        basicMap.put(0.78, 8.7); //0.78
        basicMap.put(0.79, 9.0); //0.79
        basicMap.put(0.80, 9.3); //0.80
        basicMap.put(0.81, 9.7); //0.81
        basicMap.put(0.82, 10.0); //0.82
        basicMap.put(0.83, 10.3); //0.83
        basicMap.put(0.84, 10.7); //0.84
        basicMap.put(0.85, 11.0); //0.85
        basicMap.put(0.86, 11.5); //0.86
        basicMap.put(0.87, 12.0); //0.87
        basicMap.put(0.88, 12.3); //0.88
        basicMap.put(0.89, 12.7); //0.89
        basicMap.put(0.90, 13.0); //0.90

        //System.out.println("Test print " + basicMap.get(avg) + " Taken avg : " + avg +"\n");
        if(basicMap.get(avg) == null){
            if(avg < 0.11){
                return 9.0;
            }
            return 19.0;
        }else{
            return basicMap.get(avg);
        }
    }

    public double GetRefer(String scale){
        Map<String, Double> basicMap = new HashMap<>();

        basicMap.put("C2", 0.11);
        basicMap.put("D2", 0.14);
        basicMap.put("E2", 0.17);
        basicMap.put("F2", 0.19);
        basicMap.put("G2", 0.22);
        basicMap.put("A2", 0.25);
        basicMap.put("B2", 0.28);
        basicMap.put("C3", 0.30);
        basicMap.put("D3", 0.33);
        basicMap.put("E3", 0.36);
        basicMap.put("F3", 0.38);
        basicMap.put("G3", 0.41);
        basicMap.put("A3", 0.44);
        basicMap.put("B3", 0.47);
        basicMap.put("C4", 0.49);
        basicMap.put("D4", 0.52);
        basicMap.put("E4", 0.55);
        basicMap.put("F4", 0.57);
        basicMap.put("G4", 0.60);
        basicMap.put("A4", 0.63);
        basicMap.put("B4", 0.66);
        basicMap.put("C5", 0.68);
        basicMap.put("D5", 0.71);
        basicMap.put("E5", 0.74);
        basicMap.put("F5", 0.76);
        basicMap.put("G5", 0.79);
        basicMap.put("A5", 0.82);
        basicMap.put("B5", 0.85);
        basicMap.put("C6", 0.87);
        basicMap.put("D6x", 0.90);

        return basicMap.get(scale);
    }



}