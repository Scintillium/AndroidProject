package stacko;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import Adapters.QuestionAdapter;
import classes.Question;
import database.favoriteDatabase;

public class FavoriteActivity extends AppCompatActivity {
    private ArrayList<Question> questions ;
    private RecyclerView favoriteList ;
    private QuestionAdapter questionAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_favoriteActivity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("FAVORITE");
        questions = new ArrayList<>();
        Log.i("FA","0");
        favoriteDatabase favoriteDatabase = new favoriteDatabase(this);
        try {
            questions = favoriteDatabase.query();
        }catch (Exception e){
            Log.i("FA",e.toString());
        }
        questionAdapter = new QuestionAdapter(this,questions);
        Log.i("FA","1");
        favoriteList = (RecyclerView)findViewById(R.id.favoriteList_recyclerView_content_favorite);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Log.i("FA","2");
        favoriteList.setLayoutManager(layoutManager);
        favoriteList.setAdapter(questionAdapter);
        Log.i("FA","3");
    }
}
