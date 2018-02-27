package retrofit;

import android.content.Context;

import java.io.IOException;

import retrofit.client.Client;
import retrofit.client.Request;
import retrofit.client.Response;

/**
 * Created by Ewan on 2017/1/13.
 */

public class ConnectivityAwareRetrofitClient implements Client {
    private Client client;
    private Context context;

    public ConnectivityAwareRetrofitClient(Client client, Context context) {
        this.client = client;
        this.context = context;
    }

    @Override
    public Response execute(Request request) throws IOException {
        if (!NetworkHelper.connectedToNetwork(context)) {
            throw new NoConnectivityException("No connectivity");
        }

        return client.execute(request);
    }
}
