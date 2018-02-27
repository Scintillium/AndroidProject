package classes;

/**
 * Created by Ewan on 2016/12/13.
 */

import org.json.JSONException;
import org.json.JSONObject;

public class Site {
    private String name;
    private String baseURL;


    public Site(JSONObject obj) throws JSONException {
        this.name = obj.getString("name");
        this.baseURL = obj.getString("api_site_parameter");
    }

    public String getName() {
        return this.name;
    }

    public String baseURL() {
        return this.baseURL;
    }
}
