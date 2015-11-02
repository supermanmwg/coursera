package com.coursera.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.base.fragments.BaseFragment;
import com.coursera.R;
import com.coursera.activities.CourseDetailActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.utils.apis.ApiService;
import com.utils.apis.ICourseApi;
import com.utils.rjos.CourseDetailRjo;
import com.utils.rjos.CourseRjo;
import com.utils.rjos.CourseraCallback;

import org.w3c.dom.Text;

/**
 * Created by supermanmwg on 15-11-2.
 */
public class CourseFragment extends BaseFragment {
    //data
    private int courseId;
    private String courseName;
    private String courseUniversity;

    //retrofit
    private ICourseApi courseApiService;

    //views
    private SimpleDraweeView coursePic;
    private TextView courseNameTv;
    private TextView universityNameTv;
    private TextView shortDescTv;
    private TextView aboutCourseTv;
    private TextView courseLanguageTv;
    private TextView courseWorkloadTv;
    private TextView courseTeacherTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseApiService = ApiService.getInstance(getActivity()).create(ICourseApi.class);
        courseId = getArguments().getInt(CourseDetailActivity.ID);
        courseName = getArguments().getString(CourseDetailActivity.NAME);
        courseUniversity = getArguments().getString(CourseDetailActivity.UNIVERSITY_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_course, container, false);
        initView(v);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getCourseDetail();
    }

    private void initView(View v) {
        coursePic = (SimpleDraweeView) v.findViewById(R.id.course_photo);
        courseNameTv = (TextView) v.findViewById(R.id.course_name);
        universityNameTv = (TextView) v.findViewById(R.id.university_name);
        shortDescTv = (TextView) v.findViewById(R.id.course_short_desc);
        aboutCourseTv = (TextView) v.findViewById(R.id.about_the_course);
        courseLanguageTv = (TextView) v.findViewById(R.id.course_language);
        courseWorkloadTv = (TextView) v.findViewById(R.id.course_workload);
        courseTeacherTv = (TextView) v.findViewById(R.id.course_teacher);
    }

    public void onEventMainThread(CourseDetailRjo rjo) {
        if(rjo.isSuccess()) {
            setView(rjo);
        } else {
            Toast.makeText(getActivity(), getString(R.string.get_courses_error, rjo.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    private void setView(CourseDetailRjo rjo) {
        CourseDetailRjo.Element element = rjo.getElements().get(0);
        Uri picUri = Uri.parse(element.getPhoto());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(picUri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();

        coursePic.setController(controller);
        courseNameTv.setText(element.getName());
        universityNameTv.setText(courseUniversity);
        shortDescTv.setText(element.getShortDescription());
        aboutCourseTv.setText(Html.fromHtml(element.getAboutTheCourse()));
        courseLanguageTv.setText(getString(R.string.language, element.getLanguage()));
        courseWorkloadTv.setText(getString(R.string.work_load,element.getEstimatedClassWorkload()));
        courseTeacherTv.setText(getString(R.string.instructor,element.getInstructor()));
    }

    private void getCourseDetail() {
        courseApiService.getCourseById(courseId, new CourseraCallback<>(getActivity(), CourseDetailRjo.class));
    }
}
