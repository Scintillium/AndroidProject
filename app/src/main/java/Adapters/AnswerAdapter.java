package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import classes.Answer;
import stacko.R;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

/**
 * Created by Ewan on 2016/12/18.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    private ArrayList<Answer> list ;
    private  Context context ;
    private LayoutInflater inflater ;
    public AnswerAdapter(Context context, ArrayList<Answer> list){
        super();
        this.context = context ;
        this.list = list ;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_answer_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.body = (CodeView)view.findViewById(R.id.answerBody_codeView_item_answerList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.body.showCodeHtmlByClass(list.get(position).getBody(),"pre");
        holder.body.setTheme(CodeViewTheme.GITHUB);
        Log.i("shit",String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
        CodeView body ;
    }
}
