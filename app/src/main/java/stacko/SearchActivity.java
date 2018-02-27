package stacko;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.google.gson.Gson;

import classes.Filter;
import classes.FilterType;

public class SearchActivity extends AppCompatActivity {
   private Filter filter ;
    private  int checkedButtonID ;
    private RadioGroup radioGroup ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_searchActivity);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        radioGroup = (RadioGroup)findViewById(R.id.type_radioGroup_content_search);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedButtonID = checkedId ;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView_searchActivity).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       // searchView.setIconifiedByDefault(false);// Do not iconify the widget; expand it by defaul
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.setQueryHint("search...");
        //searchView.setIconified(false);
        searchView.requestFocus();

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // This is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                // **Here you can get the value "query" which is entered in the search box.**
                filter = new Filter();
                   switch (checkedButtonID){
                       case R.id.tag_radioBtn:
                           filter.type = FilterType.TAG ;
                           break;
                       case R.id.user_radioBtn:
                           filter.type = FilterType.USER ;
                           break ;
                       case R.id.title_radioBtn:
                           filter.type = FilterType.QUERY ;
                           break ;
                       default:
                           filter.type = FilterType.NONE ;
                           break ;
                   }

                filter.value = query ;
                Gson gson = new Gson();
                String gsonString = gson.toJson(filter);
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.putExtra("filter",gsonString);
                startActivity(intent);
                finish();
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
