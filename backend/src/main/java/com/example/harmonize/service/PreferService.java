package com.example.harmonize.service;

import com.example.harmonize.entity.Category;
import com.example.harmonize.entity.Prefer;
import com.example.harmonize.repository.CategoryRepository;
import com.example.harmonize.repository.PreferRepository;
import com.example.harmonize.utility.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class PreferService {

    @Autowired
    private PreferRepository preferRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<String> GetPreferCategory(Long uid){
        return preferRepository.findCategoryNameByUser_id(uid);
    }

    public void SaveCategoryByID(Long uid, ArrayList<String> list){

        preferRepository.deleteAllByUser_id(uid);

        for (String c : list) {
            Prefer prefer = new Prefer();
            Long cid = categoryRepository.findByCategory_name(c);
            prefer.setCategory_id(cid);
            prefer.setUser_id(uid);
            preferRepository.save(prefer);
        }
    }

    public void DeleteCategoryID(Long uid, ArrayList<String> list){
        for (int i=0; i<list.size(); i++){
            Long cid = categoryRepository.findByCategory_name(list.get(i));
            Long pid = preferRepository.findByCategory_idAndUser_id(uid, cid);

            preferRepository.deleteById(pid);
        }
    }


}
