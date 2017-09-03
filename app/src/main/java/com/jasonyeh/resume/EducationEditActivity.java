package com.jasonyeh.resume;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jasonyeh.resume.model.Education;
import com.jasonyeh.resume.util.DateUtils;

import java.util.Arrays;

public class EducationEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);

        // Adding this code will show the back button in the ActionBar of EducationEditActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // 顯示menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // this function will get called when you click back button
        switch (item.getItemId()) {
            // android.R.id.home -> 返回按鈕
            case android.R.id.home:
                finish(); // finish the current Activity
                return true;
            // R.id.ic_save -> 打勾保存
            case R.id.action_save:
                saveAndExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        Education data = new Education();
        data.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
        data.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
        data.startDate = DateUtils.stringToDate(
                ((EditText) findViewById(R.id.education_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(
                ((EditText) findViewById(R.id.education_edit_end_date)).getText().toString());
        data.courses = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));


        // intent 用來放數據
        Intent resultIntent = new Intent();
        // 這裏data是reference，不是純數據，要放純數據的話 -> serialize & deserialize
        resultIntent.putExtra("education", data);
        setResult(RESULT_OK, resultIntent);
        finish();

//        Education education = new Education();
//        education.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
//        education.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
//        education.startDate = DateUtils.stringToDate(
//                ((EditText) findViewById(R.id.education_edit_start_date)).getText().toString());
//        education.endDate = DateUtils.stringToDate(
//                ((EditText) findViewById(R.id.education_edit_end_date)).getText().toString());
//        education.courses = Arrays.asList(TextUtils.split(
//                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));
    }
}


