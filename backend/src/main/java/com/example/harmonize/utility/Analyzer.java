package com.example.harmonize.utility;

import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;

public class Analyzer {

    public Double GetPossibility(Music list, UserVoice userVoice){
        Double max_pos = (list.getMax() > userVoice.getMax())? userVoice.getMax() : list.getMax();
        Double min_pos = (list.getMin() > userVoice.getMin())? list.getMin() : userVoice.getMin();
        return (max_pos-min_pos)/(list.getMax()-list.getMin())*100;
    }

}
