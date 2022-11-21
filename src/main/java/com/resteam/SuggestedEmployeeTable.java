package com.resteam;

import com.sun.prism.Image;

public class SuggestedEmployeeTable {
    private Image png;
    private String name;
    private String degree;
    private String age;
    private String workExperience;
    private String city;


    public SuggestedEmployeeTable(Image png, String name, String degree, String age, String workExperience, String city){
        setSuggestedEmployeeRecord(png, name, degree, age, workExperience, city);
    }

    public Image getPng(){
        return this.png;
    }

    public void setPng(Image png){
        this.png = png;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDegree(){
        return this.degree;
    }

    public void setDegree(String degree){
        this.degree = degree;
    }

    public String getAge(){
        return this.age;
    }

    public void setAge(String age){
        this.age = age;
    }

    public String getWorkExperience(){
        return this.workExperience;
    }

    public void setWorkExperience(String workExperience){
        this.workExperience = workExperience;
    }

    public String getCity(){
        return this.city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setSuggestedEmployeeRecord(Image png,String name,String degree,String age,String workExperience,String city){
        setPng(png);
        setName(name);
        setDegree(degree);
        setAge(age);
        setWorkExperience(workExperience);
        setCity(city);
    }
}
