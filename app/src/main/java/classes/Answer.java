package classes;

/**
 * Created by Ewan on 2016/12/13.
 */

import org.json.JSONException;
import org.json.JSONObject;


public class Answer {
    private Integer answer_id;
    private String body;
    private String excerpt;


    public Answer(JSONObject ans) throws JSONException {
        this.answer_id = ans.getInt("answer_id");
        this.body = ans.getString("body");
        this.excerpt = body.substring(0,50) + "...";
    }

    public String getExcerpt() {
        return this.excerpt;
    }

    public String getBody() {
        return this.body;
    }
}
