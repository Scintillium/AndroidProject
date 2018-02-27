package APIS;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import classes.Answer;
import classes.Question;
import classes.Site;
import classes.Tag;
import classes.User;

/**
 * Created by Ewan on 2016/12/13.
 */

public class StackExchangeAPIS {
    private final String baseURL = "http://api.stackexchange.com/2.2/";
    private final String clientID = "8654" ;
    private  final String clientSecret = "wGvA23FV1FQJiVHe4lV5)A((" ;
    private final String key = "&key=HiBX0zNFDf)d7ZY7kUGv8g((" ;

    private String site = "site=stackoverflow" + key;
    private static StackExchangeAPIS instance ;
    public StackExchangeAPIS() {return;}

   public static StackExchangeAPIS getInstance()
   {
       if(instance == null)
       {
           instance = new StackExchangeAPIS();
       }
       return instance ;
   }

    public void setSite(String s) {
        this.site = "site=" + s;
    }
    //输入页面，返回问题
    public ArrayList<Question> fetchQuestionsByPage(int page) {
        String requestURL = this.baseURL + "questions?order=desc&sort=activity&page=" + String.valueOf(page) + "&" + this.site;
        JSONObject response = null;
        ArrayList<Question> questions = new ArrayList<Question>();
        try {
            response = this.sendRequest(requestURL);
            JSONArray jsonQuestionsArray = response.getJSONArray("items");

            for (int i = 0; i < jsonQuestionsArray.length(); i++) {
                JSONObject queJson = jsonQuestionsArray.getJSONObject(i);
                try {
                    Question question = new Question(queJson);
                    question.addDetails(fetchQuestionDetails(question.getId()));
                    questions.add(question);
                } catch (Exception e) {
                    Log.i("APP", e.toString());
                }
            }

        } catch (Exception e) {
            Log.i("APP", e.toString());
            questions = null;
        }
        return questions ;
    }
    public ArrayList<Question> fetchQuestionsByTag(String tag, int page) {
        ArrayList<Question> questions = new ArrayList<Question>();
        try {
            tag = URLEncoder.encode(tag, "utf-8");
            String requestUrl = this.baseURL + "search?order=desc&sort=activity&tagged=" + tag + "&page=" + String.valueOf(page) + "&" + this.site;
            JSONObject questionsJson = this.sendRequest(requestUrl);
            JSONArray questionsArray = questionsJson.getJSONArray("items");
            for(int i = 0; i < questionsArray.length(); i++) {
                try {
                    JSONObject queJson = questionsArray.getJSONObject(i);
                    Question que = new Question(queJson);
                    que.addDetails(fetchQuestionDetails(que.getId()));
                    questions.add(que);
                } catch (Exception e) {
                    Log.i("APP", e.toString());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.toString());
            questions = null;
        }

        return questions;
    }
    public ArrayList<Question> fetchQuestionsByUser(String user, int page) {
        ArrayList<Question> questions = new ArrayList<Question>();
        try {
            user = URLEncoder.encode(user, "utf-8");
            String requestUrl = this.baseURL + "users/" + user + "/questions?order=desc&sort=activity&page=" + String.valueOf(page) + "&" + this.site;
            JSONObject questionsJson = this.sendRequest(requestUrl);
            JSONArray questionsArray = questionsJson.getJSONArray("items");
            for(int i = 0; i < questionsArray.length(); i++) {
                try {
                    JSONObject queJson = questionsArray.getJSONObject(i);
                    Question que = new Question(queJson);
                    que.addDetails(fetchQuestionDetails(que.getId()));
                    questions.add(que);
                } catch (Exception e) {
                    Log.i("APP", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            questions = null;
        }

        return questions;
    }
    public ArrayList<Question> fetchQuestionsByIntitle(String intitle, int page) {
        ArrayList<Question> questions = new ArrayList<Question>();
        try {
            intitle = URLEncoder.encode(intitle, "utf-8");
            String requestUrl = this.baseURL + "search?order=desc&sort=activity&intitle=" + intitle + "&page=" + String.valueOf(page) + "&" + this.site;
            JSONObject questionsJson = this.sendRequest(requestUrl);
            JSONArray questionsArray = questionsJson.getJSONArray("items");
            for(int i = 0; i < questionsArray.length(); i++) {
                try {
                    JSONObject queJson = questionsArray.getJSONObject(i);
                    Question que = new Question(queJson);
                    que.addDetails(fetchQuestionDetails(que.getId()));
                    questions.add(que);
                } catch (Exception e) {
                    Log.i("APP", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            questions = null;
        }

        return questions;
    }
    public JSONObject fetchQuestionDetails(int queid) {
        String requestUrl = this.baseURL + "questions/" + String.valueOf(queid) + "?order=desc&sort=activity&filter=!9hnGssGO4&" + this.site;
        JSONObject queDetailsJson = null;
        try {
            queDetailsJson = this.sendRequest(requestUrl);
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
        }

        return queDetailsJson;
    }
    public ArrayList<Tag> fetchTags(int page) {
        String requestUrl = this.baseURL + "tags?order=desc&sort=popular&page=" + String.valueOf(page) + "&" + this.site;
        ArrayList<Tag> tags = new ArrayList<Tag>();
        try {
            JSONObject tagsJson = this.sendRequest(requestUrl);
            JSONArray tagsArray = tagsJson.getJSONArray("items");

            for(int i = 0; i < tagsArray.length(); i++) {
                try {
                    JSONObject tagJson = tagsArray.getJSONObject(i);
                    Tag tag = new Tag(tagJson);
                    tags.add(tag);
                } catch (Exception e) {
                    Log.i("APP", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            tags = null;
        }

        return tags;
    }
    public JSONObject fetchTagWiki(String tag) {
        JSONObject wiki = null;
        try {
            tag = URLEncoder.encode(tag, "utf-8");
            String requestUrl = this.baseURL + "tags/" + tag + "/wikis?" + this.site;
            JSONObject response = this.sendRequest(requestUrl);
            JSONArray items = response.getJSONArray("items");
            wiki = items.getJSONObject(0);
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
        }

        return wiki;
    }
    public ArrayList<User> fetchUsers(int page) {
        ArrayList<User> users = new ArrayList<User>();

        String requestUrl = this.baseURL + "users?order=desc&sort=reputation&page=" + String.valueOf(page) + "&" + this.site;
        try {
            JSONObject usersJson = this.sendRequest(requestUrl);
            JSONArray usersArray = usersJson.getJSONArray("items");
            for(int i = 0; i < usersArray.length(); i++) {
                JSONObject userJson = usersArray.getJSONObject(i);
                User user = new User(userJson);
                users.add(user);
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            users = null;
        }

        return users;
    }
    public JSONObject fetchUser(Integer id) {
        JSONObject user = null;
        String requestUrl = this.baseURL + "users/" + id + "?order=desc&sort=reputation&" + this.site;
        try {
            JSONObject responseObj = this.sendRequest(requestUrl);
            JSONArray items = responseObj.getJSONArray("items");
            user = items.getJSONObject(0);
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
        }

        return user;
    }
    public ArrayList<Answer> fetchAnswersByUser(String user) {
        ArrayList<Answer> answers = new ArrayList<>();
        try {
            user = URLEncoder.encode(user, "utf-8");
            String requestUrl = this.baseURL + "users/" + user + "/answers?order=desc&sort=activity&filter=!*LVw4pKRvjyBRppf&" + this.site;
            JSONObject answersJson = this.sendRequest(requestUrl);
            JSONArray answersArray = answersJson.getJSONArray("items");
            for(int i = 0; i < answersArray.length(); i++) {
                try {
                    JSONObject ansJson = answersArray.getJSONObject(i);
                    Answer ans = new Answer(ansJson);
                    answers.add(ans);
                } catch (Exception e) {
                    Log.i("APP", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            answers = null;
        }
        return answers;
    }
    public ArrayList<Answer> fetchAnswersByQuestion(String question) {
        ArrayList<Answer> answers = new ArrayList<>();
        Log.i("APP", "start fetch answer");
        try {
            question = URLEncoder.encode(question, "utf-8");
            String requestUrl = this.baseURL + "questions/" + question + "/answers?order=desc&sort=activity&filter=!*LVw4pKRvjyBRppf&" + this.site;
            JSONObject answersJson = this.sendRequest(requestUrl);
            Log.i("APP", "send request");
            JSONArray answersArray = answersJson.getJSONArray("items");
                Log.i("length", String.valueOf(answersArray.length()));
            for(int i = 0; i < answersArray.length(); i++) {
                try {
                    JSONObject ansJson = answersArray.getJSONObject(i);
                    Answer ans = new Answer(ansJson);
                    answers.add(ans);
                    Log.i("length", String.valueOf(answers.size()));
                } catch (Exception e) {
                    Log.i("APP", e.toString());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.toString());
            answers = null;
        }
        return answers;
    }
    public ArrayList<Site> fetchSites() {
        ArrayList<Site> sites = new ArrayList<Site>();

        try {
            String requestUrl = "https://api.stackexchange.com/2.0/sites?pagesize=100";
            JSONObject sitesJson = this.sendRequest(requestUrl);
            JSONArray sitesArray = sitesJson.getJSONArray("items");

            for (int i = 0; i < sitesArray.length(); i++) {
                try {
                    sites.add(new Site(sitesArray.getJSONObject(i)));
                } catch (Exception e) {
                    Log.i("APP", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.i("APP", e.getMessage());
            sites = null;
        }
        return sites;
    }


    public JSONObject sendRequest(String path) throws IOException, JSONException {

        JSONObject response = new JSONObject();
        String jsonString;
        URL url = new URL(path);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        if(conn.getResponseCode()==200) // 如果访问成功则返回“200”
        {
            InputStream is = conn.getInputStream();       //以输入流的形式返回
            //将输入流转换成字符串
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            jsonString =baos.toString();
            baos.close();
            is.close();
            response = new JSONObject(jsonString);
        }
        return response;
    }
}
