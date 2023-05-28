package kr.ac.chungbuk.harmonize.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class User {
    @Expose
    @SerializedName("username")
    public String username;
    @Expose
    @SerializedName("categories")
    public ArrayList<String> categories = new ArrayList<>();
}