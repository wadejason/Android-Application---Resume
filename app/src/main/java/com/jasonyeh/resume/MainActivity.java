package com.jasonyeh.resume;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jasonyeh.resume.model.BasicInfo;
import com.jasonyeh.resume.model.Education;
import com.jasonyeh.resume.model.Experience;
import com.jasonyeh.resume.model.Project;
import com.jasonyeh.resume.util.DateUtils;
import com.jasonyeh.resume.util.ImageUtils;
import com.jasonyeh.resume.util.ModelUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;
    private static final int REQ_CODE_EDIT_PROJECT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";

    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDIT_BASIC_INFO:
                    BasicInfo basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
                    updateBasicInfo(basicInfo);
                    break;
                case REQ_CODE_EDIT_EDUCATION:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationId != null) {
                        deleteEducation(educationId);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
                case REQ_CODE_EDIT_EXPERIENCE:
                    String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if (experienceId != null) {
                        deleteExperience(experienceId);
                    } else {
                        Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    break;
                case REQ_CODE_EDIT_PROJECT:
                    String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if (projectId != null) {
                        deleteProject(projectId);
                    } else {
                        Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                        updateProject(project);
                    }
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });

        setupBasicInfo();
        setupEducations();
        setupExperiences();
        setupProjects();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Your email"
                : basicInfo.email);

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }

    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            setupEducation(educationView, education);
            educationsLayout.addView(educationView);
        }
    }

    private void setupEducation(View educationView, final Education education) {
        String dateString = DateUtils.dateToString(education.startDate)
                + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) educationView.findViewById(R.id.education_school))
                .setText(education.school + " (" + dateString + ")");
        ((TextView) educationView.findViewById(R.id.education_major)).setText(
                education.major);
        ((TextView) educationView.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));

        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
    }

    private void setupExperiences() {
        LinearLayout experiencesLayout = (LinearLayout) findViewById(R.id.experience_list);
        experiencesLayout.removeAllViews();
        for (Experience experience : experiences) {
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item, null);
            setupExperience(experienceView, experience);
            experiencesLayout.addView(experienceView);
        }
    }

    private void setupExperience(View experienceView, final Experience experience) {
        String dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        ((TextView) experienceView.findViewById(R.id.experience_company))
                .setText(experience.company + " (" + dateString + ")");
        ((TextView) experienceView.findViewById(R.id.experience_details))
                .setText(formatItems(experience.details));

        ImageButton editExperienceBtn = (ImageButton) experienceView.findViewById(R.id.edit_experience_btn);
        editExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
    }

    private void setupProjects() {
        LinearLayout projectListLayout = (LinearLayout) findViewById(R.id.project_list);
        projectListLayout.removeAllViews();
        for (Project project : projects) {
            View projectView = getLayoutInflater().inflate(R.layout.project_item, null);
            setupProject(projectView, project);
            projectListLayout.addView(projectView);
        }
    }

    private void setupProject(@NonNull View projectView, final Project project) {
        String dateString = DateUtils.dateToString(project.startDate)
                + " ~ " + DateUtils.dateToString(project.endDate);
        ((TextView) projectView.findViewById(R.id.project_name))
                .setText(project.name + " (" + dateString + ")");
        ((TextView) projectView.findViewById(R.id.project_details))
                .setText(formatItems(project.details));
        projectView.findViewById(R.id.edit_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
    }


    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this, MODEL_BASIC_INFO, new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this, MODEL_EDUCATIONS, new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this, MODEL_EXPERIENCES, new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProjects = ModelUtils.read(this, MODEL_PROJECTS, new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
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

    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);

        this.basicInfo = basicInfo;
        setupBasicInfo();
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }

        if (!found) {
            educations.add(education);
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (e.id.equals(experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }

        if (!found) {
            experiences.add(experience);
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, project.id)) {
                found = true;
                projects.set(i, project);
                break;
            }
        }

        if (!found) {
            projects.add(project);
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteExperience(@NonNull String experienceId) {
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void deleteProject(@NonNull String projectId) {
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, projectId)) {
                projects.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }
}




//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQ_CODE_EDIT_EDUCATION = 100; //request code for education_edit
//    private static final String MODEL_EDUCATIONS = "educations";
//
//    private BasicInfo basicInfo;
////    private Education education;
//    private List<Education> educations;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        fakeData();
//        loadData();
//        setupUI();
//    }
//
//    //  接收取得結果，並添加到介面
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case REQ_CODE_EDIT_EDUCATION:
//                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
//                    if (educationId != null) {
//                        deleteEducation(educationId);
//                    } else {
//                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
//                        updateEducation(education);
//                    }
//                    break;
//            }
//        }
////        // Check
////        if (resultCode == RESULT_OK && requestCode == REQ_CODE_EDIT_EDUCATION) {
////            Education newEducation = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
////            educations.add(newEducation); //更新數據
////            //setupEducationsUI(); // 畫介面
////            setupEducations();
////            updateEducation(newEducation);
////
////        }
//    }
//
//    private void setupUI() {
//        setContentView(R.layout.activity_main);
//
//        // findViewById return 的是 view
////        ((ImageButton) findViewById(R.id.add_education_btm)).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //點擊後進入新的activity, intent參數 (context, class)
////                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
////                startActivity(intent);
////            }
////        });
//
//        // findViewById return 的是 view，所有的view 對象都有 onClickListener 方法，所以可以改成
////        findViewById(R.id.add_education_btm).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                //點擊後進入新的activity, intent參數 (context, class)
////                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
////                //startActivity(intent);
////                // 想要結果 要改成 startActivityForResult 需要request code 區分重哪邊來
////                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
////            }
////        });
//
//        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btm);
//        addEducationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
//                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
//            }
//        });
//        setupBasicInfoUI();
////        setupEducationUI();
////        setupEducationsUI();
//
////        setupEducationsUI();
//        setupEducations();
//
//    }
//
//    private void setupBasicInfoUI() {
//        // Display the basic information onto the UI
//        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
//        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
//    }
//    // single education
////    private void setupEducationUI() {
////        // Display the education data onto the UI
////        // Follow the example in setupBasicInfoUI
////        // You will probably find formatItems method useful when displaying the courses
////
////        String dateRangeStr = DateUtils.dateToString(education.startDate)
////                + " ~ " + DateUtils.dateToString(education.endDate);
////
////        ((TextView) findViewById(R.id.education_school)).setText(education.school + " (" + dateRangeStr + ")");
////        ((TextView) findViewById(R.id.education_courses)).setText(formatItems(education.courses));
////    }
//
//    // multi educations
////    private void setupEducationsUI() {
////        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
////        for (Education education : educations) {
////            //把佈局文件轉成樹
////            View view = getLayoutInflater().inflate(R.layout.education_item, null);
////            // convert education object into view
////            String dateRangeStr = DateUtils.dateToString(education.startDate)
////                + " ~ " + DateUtils.dateToString(education.endDate);
////
////            ((TextView) view.findViewById(R.id.education_school)).setText(
////                    education.school + " (" + dateRangeStr + ")");
////            ((TextView) view.findViewById(R.id.education_courses)).setText(
////                    formatItems(education.courses));
////            // view ->介面上
////            educationsLayout.addView(view);
////
////        }
////    }
//
//
////    // multi education 代碼重構 Code refactoring
////    private void setupEducationsUI() {
////        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
////        educationsLayout.removeAllViews();   //把之前在界面上的remove，之後放新的
////        for (Education education : educations) {
////            educationsLayout.addView(getEducationView(education));
////        }
////    }
////    // 轉換器
////    // Why final here? So that inner class object can make a copy of this variable and store the copy in heap
////    private View getEducationView(final Education education) {
////        View view = getLayoutInflater().inflate(R.layout.education_item, null);
////        // convert education object into view
////        String dateRangeStr = DateUtils.dateToString(education.startDate)
////                + " ~ " + DateUtils.dateToString(education.endDate);
////
////        ((TextView) view.findViewById(R.id.education_school)).setText(
////                education.school + " (" + dateRangeStr + ")");
////        ((TextView) view.findViewById(R.id.education_major)).setText(
////                education.major);
////        ((TextView) view.findViewById(R.id.education_courses)).setText(
////                formatItems(education.courses));
////
////        view.findViewById(R.id.edit_education_btn).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
////                // This Education object is the one that’s correspondent to that’s being clicked on
////                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
////                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
////            }
////        });
////        // view ->介面上
////        return view;
////    }
//    private void setupEducations() {
//        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
//        educationsLayout.removeAllViews();
//        for (Education education : educations) {
//            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
//            setupEducation(educationView, education);
//            educationsLayout.addView(educationView);
//        }
//    }
//
//    private void setupEducation(View educationView, final Education education) {
//        String dateString = DateUtils.dateToString(education.startDate)
//                + " ~ " + DateUtils.dateToString(education.endDate);
//        ((TextView) educationView.findViewById(R.id.education_school))
//                .setText(education.school + " (" + dateString + ")");
//        ((TextView) educationView.findViewById(R.id.education_major)).setText(education.major);
//        ((TextView) educationView.findViewById(R.id.education_courses))
//                .setText(formatItems(education.courses));
//
//        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
//        editEducationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
//                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
//                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
//            }
//        });
//    }
//
//
//    private void loadData() {
//
//        List<Education> savedEducation = ModelUtils.read(this,
//                MODEL_EDUCATIONS,
//                new TypeToken<List<Education>>(){});
//        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;
//
//    }
////    private void fakeData() {
////        basicInfo = new BasicInfo();
////        basicInfo.name = "Jason";
////        basicInfo.email = "jasonyeh@xxx.com";
////
////        educations = new ArrayList<>();
////
////        // education1
////        Education education1 = new Education();
////        education1 = new Education();
////        education1.school = "SJSU";
////        education1.major = "Computer Engineering";
////        education1.startDate = DateUtils.stringToDate("08/2015");
////        education1.endDate = DateUtils.stringToDate("08/2017");
////
////        education1.courses = new ArrayList<>();
////        education1.courses.add("Database");
////        education1.courses.add("Algorithm");
////        education1.courses.add("Network Security");
////
////        educations.add(education1);
////
////        // education2
////        Education education2 = new Education();
////        education2 = new Education();
////        education2.school = "NTNU";
////        education2.major = "Mathematics";
////        education2.startDate = DateUtils.stringToDate("09/2009");
////        education2.endDate = DateUtils.stringToDate("06/2013");
////
////        education2.courses = new ArrayList<>();
////        education2.courses.add("Linear Algebra");
////        education2.courses.add("Calculus");
////        education2.courses.add("Discrete Mathematics");
////
////        educations.add(education2);
////
////    }
//
//    public static String formatItems(List<String> items) {
//        StringBuilder sb = new StringBuilder();
//        for (String item: items) {
//            sb.append(' ').append('-').append(' ').append(item).append('\n');
//        }
//        if (sb.length() > 0) {
//            sb.deleteCharAt(sb.length() - 1);
//        }
//        return sb.toString();
//    }
//
//
//    private void updateEducation(Education education) {
//        boolean found = false;
//        for (int i = 0; i < educations.size(); ++i) {
//            Education e = educations.get(i);
//            if (TextUtils.equals(e.id, education.id)) {
//                found = true;
//                educations.set(i, education);
//                break;
//            }
//        }
//
//        if (!found) {
//            educations.add(education);
//        }
//
//        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
//        setupEducations();
//    }
//
//
//    private void deleteEducation(@NonNull String educationId) {
//        for (int i = 0; i < educations.size(); ++i) {
//            Education e = educations.get(i);
//            if (TextUtils.equals(e.id, educationId)) {
//                educations.remove(i);
//                break;
//            }
//        }
//
//        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
//        setupEducations();
//    }
//
//}
