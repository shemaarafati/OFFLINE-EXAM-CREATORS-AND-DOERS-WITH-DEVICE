package com.offlineexam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String text;
    private String type; // MCQ, Essay, TrueFalse
    private List<String> options = new ArrayList<>();
    private int correctOptionIndex = -1; // for MCQ/TrueFalse

    public Question() {}
    public Question(int id, String text, String type){ this.id=id; this.text=text; this.type=type; }
    public int getId(){return id;} public String getText(){return text;} public String getType(){return type;}
    public List<String> getOptions(){return options;} public void addOption(String o){ options.add(o); }
    public int getCorrectOptionIndex(){return correctOptionIndex;} public void setCorrectOptionIndex(int i){ correctOptionIndex=i; }
    @Override public String toString(){ return id + " - " + type + ": " + (text.length()>40? text.substring(0,40)+"...": text); }
}
