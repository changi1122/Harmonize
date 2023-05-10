package com.example.harmonize.service;

import com.example.harmonize.repository.PreferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PreferService {

    @Autowired
    private PreferRepository preferRepository;
}
