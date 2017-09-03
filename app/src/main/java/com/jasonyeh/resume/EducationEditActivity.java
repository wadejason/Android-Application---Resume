package com.jasonyeh.resume;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jasonyeh.resume.model.Education;
import com.jasonyeh.resume.util.DateUtils;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class EducationEditActivity extends EditBaseActivity<Education> {

    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EDUCATION_ID = "education_id";

//    private Education education;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_education_edit;
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.education_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Education data) {
        ((EditText) findViewById(R.id.education_edit_school))
                .setText(data.school);
        ((EditText) findViewById(R.id.education_edit_major))
                .setText(data.major);
        ((EditText) findViewById(R.id.education_edit_courses))
                .setText(TextUtils.join("\n", data.courses));

        ((EditText) findViewById(R.id.education_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.education_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));

        findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_EDUCATION_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    @Override
    protected void saveAndExit(@Nullable Education data) {
        if (data == null) {
            data = new Education();
        }

        data.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
        data.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_end_date)).getText().toString());
        data.courses = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


    @Override
    protected Education initializeData() {
        return getIntent().getParcelableExtra(KEY_EDUCATION);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_education_edit);
//
//        // Adding this code will show the back button in the ActionBar of EducationEditActivity
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // check if we have gotten an Education object from outside
//        education = getIntent().getParcelableExtra(KEY_EDUCATION);
//        if (education != null) {
//            setupUIForEdit();
//        }
//        setTitle(education == null ? "New education" : "Edit education");
//    }





//
//    private void setupUIForEdit() {
//        ((EditText) findViewById(R.id.education_edit_school)).setText(education.school);
//        ((EditText) findViewById(R.id.education_edit_major)).setText(education.major);
//        ((EditText) findViewById(R.id.education_edit_start_date)).setText(DateUtils.dateToString(education.startDate));
//        ((EditText) findViewById(R.id.education_edit_end_date)).setText(DateUtils.dateToString(education.endDate));
//        ((EditText) findViewById(R.id.education_edit_courses)).setText(TextUtils.join("\n", education.courses));
//    }

//    // 顯示menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_edit, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) { // this function will get called when you click back button
//        switch (item.getItemId()) {
//            // android.R.id.home -> 返回按鈕
//            case android.R.id.home:
//                finish(); // finish the current Activity
//                return true;
//            // R.id.ic_save -> 打勾保存
//            case R.id.action_save:
//                saveAndExit();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }








//    private void saveAndExit() {
//
//        Education education = new Education();
//        education.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
//        education.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
//        education.startDate = DateUtils.stringToDate(
//                ((EditText) findViewById(R.id.education_edit_start_date)).getText().toString());
//        education.endDate = DateUtils.stringToDate(
//                ((EditText) findViewById(R.id.education_edit_end_date)).getText().toString());
//        // split by \n
//        education.courses = Arrays.asList(TextUtils.split(
//                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));
//
//
//        // intent 用來放數據
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra(KEY_EDUCATION, education);
//        setResult(RESULT_OK, resultIntent);
//        finish();
//
//
////        Education data = new Education();
////        data.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
////        data.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
////        data.startDate = DateUtils.stringToDate(
////                ((EditText) findViewById(R.id.education_edit_start_date)).getText().toString());
////        data.endDate = DateUtils.stringToDate(
////                ((EditText) findViewById(R.id.education_edit_end_date)).getText().toString());
////        // split by \n
////        data.courses = Arrays.asList(TextUtils.split(
////                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));
////
////
////        // intent 用來放數據
////        Intent resultIntent = new Intent();
////        // 這裏data是reference，不是純數據，要放純數據的話 -> serialize & deserialize
////        // putExtra key->value Store
////        resultIntent.putExtra("education", data);
////        setResult(RESULT_OK, resultIntent);
////        finish();
//
//
//    }
}


