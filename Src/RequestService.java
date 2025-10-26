package com.offlineexam.service;

import com.offlineexam.model.EnrollmentRequest;
import com.offlineexam.util.DataStore;

import java.util.ArrayList;
import java.util.List;

public class RequestService {
    private static final String REQUEST_FILE = "requests.dat";
    private static List<EnrollmentRequest> requests = DataStore.load(REQUEST_FILE);

    public static void sendRequest(EnrollmentRequest req) {
        requests.add(req);
        DataStore.save(REQUEST_FILE, requests);
    }

    public static List<EnrollmentRequest> listPendingForTeacher(String teacher) {
        List<EnrollmentRequest> list = new ArrayList<>();
        for (EnrollmentRequest r : requests) {
            if (r.getTeacher().equals(teacher) && "Pending".equals(r.getStatus()))
                list.add(r);
        }
        return list;
    }

    public static void approveRequest(EnrollmentRequest req) {
        for (EnrollmentRequest r : requests) {
            if (r.equals(req)) {
                r.setStatus("Approved");
                break;
            }
        }
        DataStore.save(REQUEST_FILE, requests);
    }

    public static List<EnrollmentRequest> getAllRequests() {
        return new ArrayList<>(requests);
    }
}