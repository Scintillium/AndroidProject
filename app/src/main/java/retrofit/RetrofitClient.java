package retrofit;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import APIS.RestInterface;
import Utils.Consts;
import retrofit.client.OkClient;

import static application.MainApplication.getContext;

/**
 * Created by Ewan on 2017/1/13.
 */

public class RetrofitClient {

    /**
     * Customization
     */

    private static final Context context;
    private static final OkHttpClient httpClient = new OkHttpClient();

    static {        context = getContext();
    }

    /**
     * RestAdapters
     */
    private static final RestAdapter.Builder commonBuilder = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(new ConnectivityAwareRetrofitClient(new OkClient(httpClient), context))
            .setErrorHandler(new RetrofitErrorHandler(context));

    private static final RestAdapter userInfoAdapter = commonBuilder.setEndpoint(Consts.mainURL).build();

    private static final RestAdapter userIdAdapter = commonBuilder.setEndpoint(Consts.mainURL2).build();
    /**
     * Web service definitions
     */

    private static final RestInterface USER_INFO_SERVICES = userInfoAdapter.create(RestInterface.class);

    public static RestInterface userInfoServices() {
        return USER_INFO_SERVICES;
    }

    private static final RestInterface USER_ID_SERVICES = userIdAdapter.create(RestInterface.class);

    public static RestInterface userIdServices() {
        Log.i("RetrofitClient" , " userIdServices");
        if(context == null){
            Log.i("null"," userIdServices is null");
        }
        else{
            Log.i("null"," userIdServices is not null");
        }
        return USER_ID_SERVICES;
    }
}
