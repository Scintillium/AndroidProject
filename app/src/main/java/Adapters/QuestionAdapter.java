package Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import classes.Question;
import stacko.R;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

/**
 * Created by Ewan on 2016/12/21.
 */

public  class  QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private ArrayList<Question> list = new ArrayList<>() ;
    private Context context;
    private LayoutInflater layoutInflater;
    public  QuestionAdapter(Context context, ArrayList<Question> list)
    {
        super();
        this.list = list ;
        this.context = context ;
        this.layoutInflater = LayoutInflater.from(context);
        Log.i("QA","construct");
        Log.i("QuestionList",String.valueOf(list.size()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("QA","on Create View");
        View view = layoutInflater.inflate(R.layout.item_question_list , parent , false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.title = (CodeView) view.findViewById(R.id.title_question_list_item);
        viewHolder.score = (TextView)view.findViewById(R.id.score_question_list_item);
        viewHolder.tags = (RecyclerView)view.findViewById(R.id.recycler_item_question_list);
        viewHolder.brief = (CodeView)view.findViewById(R.id.brief_question_list_item);
        Log.i("QA","on Create View End");
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("QA",String.valueOf(position)) ;
        holder.score.setText(list.get(position).getScore().toString());
        holder.brief.setTheme(CodeViewTheme.GITHUB);
        holder.brief.showCodeHtmlByClass(list.get(position).getBrief(),"pre");
        holder.title.setTheme(CodeViewTheme.GITHUB);
        holder.title.showCodeHtmlByCssSelect("<div style = \"font-size:1.1em;\"> " + list.get(position).getTitle() + "</div>","pre");
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.tags.setLayoutManager(layoutManager);
        holder.tags.setAdapter(new TagAdapter(this.context,list.get(position).getTags()));
    }

    @Override
    public int getItemCount() {
        return list.size() ;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
        CodeView title ;
        TextView score ;
        CodeView brief ;
        RecyclerView tags ;
    }
}



