package org.launchcode.techjobs.console;

//import jdk.internal.icu.text.UnicodeSet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by LaunchCode
 */



public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;
    private static ArrayList<HashMap<String, String>> allJobsLC = new ArrayList<HashMap<String, String>>();

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        makeLC();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (int i = 0; i < allJobsLC.size(); i++) {
            String aValue = allJobsLC.get(i).get(column);
            String valueLC = value.toLowerCase();
            if (aValue.contains(valueLC)) {
                jobs.add(allJobs.get(i));
            }
        }

//        for (HashMap<String, String> row : allJobsLC) {
//
//            String aValue = row.get(column);
//
//            if (aValue.contains(value)) {
//                jobs.add(row);
//            }
//        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String value){
        loadData();

        makeLC();

        ArrayList<HashMap<String, String>> newArray = new ArrayList<>();

//        for (HashMap<String, String> map : allJobs) {
        for (int i = 0; i < allJobsLC.size(); i++) {
            for (String str : allJobsLC.get(i).keySet()) {
                String search = allJobsLC.get(i).get(str);
                String valueLC = value.toLowerCase();
                if (search.contains(valueLC)) {
                    newArray.add(allJobs.get(i));
                }
            }
        }

        return newArray;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    // make new ArrayList with LC values
    private static void makeLC() {

        if (allJobsLC.size() == 0) {
            for (HashMap<String, String> map : allJobs) {
                HashMap<String, String> newMap = new HashMap<>();
                for (String str : map.keySet()) {
                    String newValue = map.get(str);
                    String newValueLC = newValue.toLowerCase();
                    newMap.put(str, newValueLC);
                }
                allJobsLC.add(newMap);
                }
        } else {
            return;
        }

            }
        }



