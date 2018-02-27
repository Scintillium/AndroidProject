package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stacko.R;

/**
 * Created by Ewan on 2016/12/16.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private List<String> tagList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;


    public TagAdapter(Context context, List<String> tagList) {
        super();
        this.context = context;
        this.tagList = tagList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_tag, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tag = (TextView) view.findViewById(R.id.text_item_tag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TagAdapter.ViewHolder holder, final int position) {
        holder.tag.setText(tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
        TextView tag;
    }

    /**
     * Created by Ewan on 2016/12/13.
     */

}