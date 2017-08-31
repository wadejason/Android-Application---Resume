package com.jasonyeh.resume;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jasonyeh.resume.model.BasicInfo;
import com.jasonyeh.resume.model.Education;
import com.jasonyeh.resume.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BasicInfo basicInfo;
    private Education education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        setupBasicInfoUI();
        setupEducationUI();
    }

    private void setupBasicInfoUI() {
        // Display the basic information onto the UI
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducationUI() {
        // Display the education data onto the UI
        // Follow the example in setupBasicInfoUI
        // You will probably find formatItems method useful when displaying the courses

        String dateRangeStr = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);

        ((TextView) findViewById(R.id.education_school)).setText(education.school + " (" + dateRangeStr + ")");
        ((TextView) findViewById(R.id.education_courses)).setText(formatItems(education.courses));
    }

    private void fakeData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "Jason";
        basicInfo.email = "jasonyeh@xxx.com";

        education = new Education();
        education.school = "SJSU";
        education.major = "Computer Engineering";
        education.startDate = DateUtils.stringToDate("08/2015");
        education.endDate = DateUtils.stringToDate("08/2017");

        education.courses = new ArrayList<>();
        education.courses.add("Database");
        education.courses.add("Algorithm");
        education.courses.add("Network Security");

    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
