package stacko;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import APIS.StackExchangeAPIS;
import Adapters.AnswerAdapter;
import classes.Answer;
import classes.Filter;
import classes.FilterType;
import classes.Question;
import database.favoriteDatabase;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

public class QuestionActivity extends AppCompatActivity {
    private Question question;
    private Filter filter;
    private TextView title;
    private RecyclerView tags;
    private CodeView body;
    private RecyclerView answerList;
    private ArrayList<Answer> answers;
    private AnswerAdapter answerAdapter;
    private LinearLayoutManager layoutManager;
    private favoriteDatabase fDatabase ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_questionActivity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish(); //返回
             }
         });
        //初始话数据
        answers = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            Gson gson = new Gson();
            question = gson.fromJson(extras.getString("question"),Question.class);
        }
        else{
            question = new Question();
        }
        setTitle("No: "+ question.getId().toString());
        filter = new Filter(FilterType.QUESTION,String.valueOf(question.getId()));
        //初始化组件
        title = (TextView)findViewById(R.id.title_content_question);
        tags = (RecyclerView)findViewById(R.id.tags_recyclerView_content_question);
        answerList = (RecyclerView)findViewById(R.id.answerList_recyclerView_content_question);
        body = (CodeView)findViewById(R.id.questionBody_codeView_content_question);

        title.setText(question.getTitle());
        body.showCodeHtmlByClass(question.getBody(),"pre");
        body.setTheme(CodeViewTheme.GITHUB);

        //answer列表
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setSmoothScrollbarEnabled(false);
        answerList.setLayoutManager(layoutManager);
        answerList.stopScroll();

        //收藏按钮
        fDatabase = new favoriteDatabase(this);

        new getAnswers().execute(filter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        MenuItem favoriteBtn = menu.findItem(R.id.favoriteBtn_question_toolbar);
        if(fDatabase.queryExistancce(question.getId())){
            favoriteBtn.setIcon(R.drawable.ic_bookmark_white_24dp);
        }else {
            favoriteBtn.setIcon(R.drawable.ic_bookmark_border_white_24dp);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favoriteBtn_question_toolbar) {
            if(fDatabase.queryExistancce(question.getId())){
                Log.i("QA","delete begin");
                fDatabase.delete(question.getId());
                item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            }else
            {
                Log.i("QA","insert begin");
                fDatabase.insert(question.getId(),question.getTitle(),question.getBody());
                item.setIcon(R.drawable.ic_bookmark_white_24dp);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //异步任务，获得answer列表
    private final class getAnswers extends AsyncTask<Filter,Void,ArrayList<Answer>>
    {
        private StackExchangeAPIS stackExchangeAPIS = StackExchangeAPIS.getInstance();
        protected ArrayList<Answer> doInBackground(Filter... params)
        {
            Filter filter = params[0];
            switch(filter.type) {
                case QUESTION:
                    answers = stackExchangeAPIS.fetchAnswersByQuestion(filter.value);
                    break;
                case USER:
                    answers = stackExchangeAPIS.fetchAnswersByUser(filter.value);
            }
            return answers;
        }

        protected void onPostExecute(final ArrayList<Answer> answers)
        {
            Toast.makeText(QuestionActivity.this, "loading answers end" , Toast.LENGTH_SHORT).show();
            Toast.makeText(QuestionActivity.this, "loading answers" , Toast.LENGTH_SHORT).show();
            Toast.makeText(QuestionActivity.this,String.valueOf(answers.size()),Toast.LENGTH_SHORT).show();
            answerAdapter = new AnswerAdapter(QuestionActivity.this,answers);
            answerList.setAdapter(answerAdapter);
        }

        protected void onPreExecute()
        {
        }
    }
}
