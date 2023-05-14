package com.example.harmonize.utility;

import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {

    public Double GetPossibility(Music list, UserVoice userVoice){
        Double max_pos = (list.getMax() > userVoice.getMax())? userVoice.getMax() : list.getMax();
        Double min_pos = (list.getMin() > userVoice.getMin())? list.getMin() : userVoice.getMin();
        return (max_pos-min_pos)/(list.getMax()-list.getMin())*100;
    }

    // 배재 만들자 max, min 찾는거
    public List<Double> FindMusicRange(){
        List<Double> list = new ArrayList<>();

        list.add(0.82312);
        list.add(0.32121);

        return list;
    }

}
