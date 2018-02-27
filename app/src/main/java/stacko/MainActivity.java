package stacko;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import APIS.StackExchangeAPIS;
import Adapters.QuestionAdapter;
import Listener.OnRecyclerItemClickListener;
import Utils.PreferencesHelper;
import classes.Filter;
import classes.Question;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
 {
    private Integer questionPage = 1;
    private ArrayList<Question> questions;
     private RecyclerView recyclerView_mainActivity ;
     private SwipeRefreshLayout swipeRefreshLayout_mainActivity ;
     private ImageView profile;
     private TextView displayName;
     private  Filter filter ;
     private QuestionAdapter questionAdapter ;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_mainActivity);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search_toolbar){
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        //初始化组件

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        View headerView = navigationView.getHeaderView(0);
        recyclerView_mainActivity = (RecyclerView) findViewById(R.id.recyclerView_mainActivity);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_mainActivity.setLayoutManager(layoutManager);
        displayName = (TextView)headerView.findViewById(R.id.display_name);
        profile = (ImageView)headerView.findViewById(R.id.profile_img);
        //数据初始化
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            filter = new Filter();
            Log.i("intent","loss");
        }else{
            Log.i("intent","got");
            Gson gson = new Gson();
            filter = gson.fromJson(extras.getString("filter"),Filter.class);
        }
        questions = new ArrayList<>();
         questionAdapter = new QuestionAdapter(MainActivity.this, questions) ;
        recyclerView_mainActivity.setAdapter(questionAdapter);
        new getQuestions().execute(filter);
        // SwipeRefresh 下拉刷新，下拉加载更多
        swipeRefreshLayout_mainActivity = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh_mainActivity);
        swipeRefreshLayout_mainActivity.setColorSchemeColors(ContextCompat.getColor(this,R.color.pink), ContextCompat.getColor(this,R.color.heavy_pink));
        swipeRefreshLayout_mainActivity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                questionPage = 1;
                questions = new ArrayList<>();
                new getQuestions().execute(filter);
                Log.i("page",questionPage.toString());
            }
        });
        Log.i("main thread","1");
        recyclerView_mainActivity.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int currentPosition = layoutManager.findLastVisibleItemPosition() ;
                if(newState == RecyclerView.SCROLL_STATE_IDLE && currentPosition +1 == questionAdapter.getItemCount())
                {
                    new getQuestions().execute(filter);
                    layoutManager.scrollToPositionWithOffset(0,0);
                    Log.i("page",questionPage.toString());
                }
            }
        });
        //点击跳转

        recyclerView_mainActivity.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView_mainActivity) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                Log.i("adapter","click");
                Gson gson = new Gson();
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                String queJson = gson.toJson(questions.get(viewHolder.getLayoutPosition()));
                intent.putExtra("question",queJson);
                Log.i("adapter","clicked");
                startActivity(intent);
            }

            @Override
            public void onItemLOngClick(RecyclerView.ViewHolder viewHolder) {

            }
        });
        Button loginBtn = (Button)headerView.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        //下载图片
        displayName.setText(PreferencesHelper.getDisplayName());

        Picasso.with(MainActivity.this).load(PreferencesHelper.getProfileLink()).into(profile, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError() {
            }
        });

        /*
        RetrofitClient.userInfoServices().getUserInfo(PreferencesHelper.getUserID(), new Callback<UserShortInfo>() {
            @Override
            public void success(UserShortInfo info, Response response) {
                logger.info("Profile :" + info.getItems().get(0).getProfile());
                displayName.setText(info.getItems().get(0).getDisplayName());


                String imageUrl = info.getItems().get(0).getProfile();
                 Log.i("imageUrl", imageUrl);
                Picasso.with(MainActivity.this).load(imageUrl).into(profile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                String merror = error.getMessage();
                Log.i("usserID",PreferencesHelper.getUserID());
                Log.i("merror :" , merror);
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //设置toolbar搜索按钮
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.nav_favorite:
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                break ;
            case R.id.nav_question:
                new  getQuestions().execute(new Filter());
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private final class getQuestions extends AsyncTask<Filter, Void, ArrayList<Question>> {
        StackExchangeAPIS stackExchangeAPIS = StackExchangeAPIS.getInstance();

        protected ArrayList<Question> doInBackground(Filter... params) {

            Filter filter = params[0];
            ArrayList<Question> newQuestions = new ArrayList<>() ;
            Log.i("filtertype",filter.type.toString());
            switch (filter.type) { //根据filter来访问问题列表
                case NONE:
                    Log.i("filtertype","here");
                    newQuestions = stackExchangeAPIS.fetchQuestionsByPage(questionPage++);
                    break;
                case TAG:
                    newQuestions = stackExchangeAPIS.fetchQuestionsByTag(filter.value, questionPage++);
                    break;
                case USER:
                    newQuestions = stackExchangeAPIS.fetchQuestionsByUser(filter.value, questionPage++);
                    break;
                case QUERY:
                    newQuestions = stackExchangeAPIS.fetchQuestionsByIntitle(filter.value, questionPage++);
                    break;
            }
            if (newQuestions != null) {
                for (Question que : newQuestions) {
                    questions.add(que);
                }
            }
            return questions;
        }

        protected void onPostExecute(final ArrayList<Question> questions) {
            Toast.makeText(MainActivity.this, "Loading finished", Toast.LENGTH_SHORT).show();

            if (questions == null) {
                Toast.makeText(MainActivity.this, "There was an error. Please try again", Toast.LENGTH_SHORT).show();
            } else if (questions.size() == 0) {
                Toast.makeText(MainActivity.this, "No Question found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    questionAdapter = new QuestionAdapter(MainActivity.this,questions);
                    try{
                        recyclerView_mainActivity.setAdapter(questionAdapter);
                    }catch (Exception e){
                        Log.i("Ex",e.toString());

                    }
                } catch (Exception e) {
                    Log.i("ex", e.toString());
                    Log.i("QuestionFrag", "Set Adapter Failed");
                }
            }
            if(swipeRefreshLayout_mainActivity.isRefreshing()){
                swipeRefreshLayout_mainActivity.setRefreshing(false);
            }
        }
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
        }
    }


}
