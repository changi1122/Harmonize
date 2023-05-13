package com.example.harmonize.service;

import com.example.harmonize.entity.Prefer;
import com.example.harmonize.repository.PreferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;

@Service
public class PreferService {

    @Autowired
    private PreferRepository preferRepository;

    public List<String> GetPreferCategory(Long uid){
        return preferRepository.findCategoryNameByUser_id(uid);
    }
}
