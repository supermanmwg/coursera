package com.coursera.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursera.R;
import com.coursera.activities.CategoryDetailActivity;
import com.utils.rjos.CategoryRjo;

import java.util.List;

/**
 * Created by supermanmwg on 15-11-1.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryRjo.Element> elements;
    private Context context;
    private int[] colors = {
            R.color.category_color_1,
            R.color.category_color_2,
            R.color.category_color_3,
            R.color.category_color_4

    };

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new ContentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindContent((ContentViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return (elements == null ? 0 : elements.size());
    }

    private void onBindContent(ContentViewHolder holder, int position) {
        CategoryRjo.Element element = elements.get(position);
        holder.nameTv.setText(element.getName());
        holder.setId(element.getId());
        holder.setName(element.getName());
        holder.dividerIv.setBackgroundResource(colors[position % 4]);
    }

    public void setElements(List<CategoryRjo.Element> elements) {
        this.elements = elements;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;
        public ImageView dividerIv;
        public View clickView;
        public int id;
        public String name;
        private View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(CategoryDetailActivity.createIntent(context, id, name));
            }
        };

        public ContentViewHolder(View v) {
            super(v);
            clickView = v;
            nameTv = (TextView) v.findViewById(R.id.category_name);
            dividerIv = (ImageView) v.findViewById(R.id.category_divider);

            clickView.setOnClickListener(clickListener);
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
