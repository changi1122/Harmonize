package com.example.harmonize.service;

import com.example.harmonize.repository.BookMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookMarkService {

    @Autowired
    private BookMarkRepository bookMarkRepository;

}
