package com.jasonyeh.resume;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasonyeh.resume.model.BasicInfo;
import com.jasonyeh.resume.model.Education;
import com.jasonyeh.resume.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDUCATION_EDIT = 100; //request code for education_edit

    private BasicInfo basicInfo;
//    private Education education;
    private List<Education> educations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    //  接收取得結果，並添加到介面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_EDUCATION_EDIT) {
            Education newEducation = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
            educations.add(newEducation); //更新數據
            setupEducationsUI(); // 畫介面
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        // findViewById return 的是 view
//        ((ImageButton) findViewById(R.id.add_education_btm)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //點擊後進入新的activity, intent參數 (context, class)
//                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
//                startActivity(intent);
//            }
//        });

        // findViewById return 的是 view，所有的view 對象都有 onClickListener 方法，所以可以改成
        findViewById(R.id.add_education_btm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //點擊後進入新的activity, intent參數 (context, class)
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                //startActivity(intent);
                // 想要結果 要改成 startActivityForResult 需要request code 區分重哪邊來
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });
        setupBasicInfoUI();
//        setupEducationUI();
//        setupEducationsUI();

        setupEducationsUI();

    }

    private void setupBasicInfoUI() {
        // Display the basic information onto the UI
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }
    // single education
//    private void setupEducationUI() {
//        // Display the education data onto the UI
//        // Follow the example in setupBasicInfoUI
//        // You will probably find formatItems method useful when displaying the courses
//
//        String dateRangeStr = DateUtils.dateToString(education.startDate)
//                + " ~ " + DateUtils.dateToString(education.endDate);
//
//        ((TextView) findViewById(R.id.education_school)).setText(education.school + " (" + dateRangeStr + ")");
//        ((TextView) findViewById(R.id.education_courses)).setText(formatItems(education.courses));
//    }

    // multi educations
//    private void setupEducationsUI() {
//        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
//        for (Education education : educations) {
//            //把佈局文件轉成樹
//            View view = getLayoutInflater().inflate(R.layout.education_item, null);
//            // convert education object into view
//            String dateRangeStr = DateUtils.dateToString(education.startDate)
//                + " ~ " + DateUtils.dateToString(education.endDate);
//
//            ((TextView) view.findViewById(R.id.education_school)).setText(
//                    education.school + " (" + dateRangeStr + ")");
//            ((TextView) view.findViewById(R.id.education_courses)).setText(
//                    formatItems(education.courses));
//            // view ->介面上
//            educationsLayout.addView(view);
//
//        }
//    }

    // multi education 代碼重構 Code refactoring
    private void setupEducationsUI() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();   //把之前在界面上的remove，之後放新的
        for (Education education : educations) {
            educationsLayout.addView(getEducationView(education));
        }
    }
    // 轉換器
    private View getEducationView(Education education) {
        View view = getLayoutInflater().inflate(R.layout.education_item, null);
        // convert education object into view
        String dateRangeStr = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);

        ((TextView) view.findViewById(R.id.education_school)).setText(
                education.school + " (" + dateRangeStr + ")");
        ((TextView) view.findViewById(R.id.education_major)).setText(
                education.major);
        ((TextView) view.findViewById(R.id.education_courses)).setText(
                formatItems(education.courses));
        // view ->介面上
        return view;
    }


    private void fakeData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "Jason";
        basicInfo.email = "jasonyeh@xxx.com";

        educations = new ArrayList<>();

        // education1
        Education education1 = new Education();
        education1 = new Education();
        education1.school = "SJSU";
        education1.major = "Computer Engineering";
        education1.startDate = DateUtils.stringToDate("08/2015");
        education1.endDate = DateUtils.stringToDate("08/2017");

        education1.courses = new ArrayList<>();
        education1.courses.add("Database");
        education1.courses.add("Algorithm");
        education1.courses.add("Network Security");

        educations.add(education1);

        // education2
        Education education2 = new Education();
        education2 = new Education();
        education2.school = "NTNU";
        education2.major = "Mathematics";
        education2.startDate = DateUtils.stringToDate("09/2009");
        education2.endDate = DateUtils.stringToDate("06/2013");

        education2.courses = new ArrayList<>();
        education2.courses.add("Linear Algebra");
        education2.courses.add("Calculus");
        education2.courses.add("Discrete Mathematics");

        educations.add(education2);

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
