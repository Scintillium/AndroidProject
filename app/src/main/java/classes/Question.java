package classes;

/**
 * Created by Ewan on 2016/12/13.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Question {
    private Integer id;
    private Integer score;
    private Integer answerCount;
    private String title;
    private ArrayList<String> tags;
    private User owner;
    private Boolean answered;
    private String brief;
    private String body;
    public Question(){
        this.id = 0;
        this.score = 0;
        this.title = "";
        this.body = "";
        this.answerCount = 0 ;
        this.answered = false;
        this.tags = new ArrayList<>();
        this.tags.add("null");
        this.brief = "";
        this.answered = false ;
    }
    public Question(JSONObject question) throws JSONException {
        this.id = question.getInt("question_id");
        this.score = question.getInt("score");
        this.answerCount = question.getInt("answer_count");
        this.title = question.getString("title");
        this.answered = question.getBoolean("is_answered");
        this.tags = new ArrayList<String>();
        // parse array of tags
        JSONArray tagsJson = question.getJSONArray("tags");
        for(int i = 0; i < tagsJson.length(); i++) {
            String tag = tagsJson.getString(i);
            this.tags.add(tag);
        }

        // parse user
        JSONObject userJson = question.getJSONObject("owner");
        User user = new User(userJson);
        this.owner = user;
    }

    public void addDetails(JSONObject detailsJson) throws JSONException {
        JSONArray array = detailsJson.getJSONArray("items");
        JSONObject queDetails = array.getJSONObject(0);
        this.body = queDetails.getString("body");
    }

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public Integer getScore(){return this.score ;}

    public  User getOwner(){return this.owner;}

    public String getBrief(){
        this.brief = body;
        if(brief.length() > 100){
            brief = brief.substring(0,100) + "..." ;
        }
        return brief ;
    }

    public ArrayList<String> getTags(){return  this.tags;}

    public  void setTitle(String title){
        this.title = title ;
    }
    public  void setId(int id){this.id = id;}
    public void setBody(String body){this.body = body ;}
}
