package com.offlineexam.service;
import com.offlineexam.model.Exam;
import com.offlineexam.util.DataStore;
import java.util.*;

public class ExamService {
    private static final String FILE = "exams.dat";
    private static List<Exam> exams = DataStore.load(FILE);
    private static int idSeq = 1;

    static {
        for(Exam e: exams) {
            if(e.getId() >= idSeq) idSeq = e.getId() + 1;
        }
    }

    public static boolean createExam(Exam e){
        e.setId(idSeq++);
        exams.add(e);
        DataStore.save(FILE, exams);
        return true;
    }

    public static List<Exam> listByCourse(int courseId){
        List<Exam> out = new ArrayList<>();
        for(Exam e: exams) {
            if(e.getCourseId() == courseId) out.add(e);
        }
        return out;
    }

    public static Exam findById(int id){
        for(Exam e: exams) {
            if(e.getId() == id) return e;
        }
        return null;
    }

    public static List<Exam> listAll(){
        return new ArrayList<>(exams);
    }

    public static void saveAll(){
        DataStore.save(FILE, exams);
    }
}