package com.coursera.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursera.R;
import com.coursera.activities.CourseDetailActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.utils.rjos.CourseRjo;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private CourseRjo rjo;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);

        return new ContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindContent((ContentViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return (rjo == null ? 0 : rjo.getElements().size());
    }

    public void onBindContent(ContentViewHolder holder, int position) {
        CourseRjo.Element element = rjo.getElements().get(position);
        Uri picUri = Uri.parse(element.getLargeIcon());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(picUri).build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();

        holder.courseIcon.setController(controller);
        holder.workLoadTv.setText(element.getEstimatedClassWorkload());
        holder.courseTitleTv.setText(element.getName());
        String universityName = rjo.getUniversities(rjo.getElements().get(position).getLinks());
        holder.universityNameTv.setText(universityName);
        holder.setUniversityName(universityName);
        holder.setCourseId(element.getId());
        holder.setCourseName(element.getName());
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setRjo(CourseRjo rjo) {
        this.rjo = rjo;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

        public SimpleDraweeView courseIcon;
        public TextView courseTitleTv;
        public TextView universityNameTv;
        public TextView workLoadTv;
        public View clickView;
        public int courseId;
        public String courseName;
        public String universityName;

        private View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                  context.startActivity(CourseDetailActivity.createIntent(context, courseId, courseName, universityName));
            }
        };

        public ContentViewHolder(View v) {
            super(v);
            courseIcon = (SimpleDraweeView) v.findViewById(R.id.course_icon);
            courseTitleTv = (TextView) v.findViewById(R.id.course_title);
            universityNameTv = (TextView) v.findViewById(R.id.university_name);
            workLoadTv = (TextView) v.findViewById(R.id.work_load);
            clickView = v;
            clickView.setOnClickListener(clickListener);
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public void setUniversityName(String universityName) {
            this.universityName = universityName;
        }
    }

}

