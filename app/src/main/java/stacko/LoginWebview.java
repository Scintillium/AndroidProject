package stacko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import UserModels.UserShortInfo;
import Utils.Consts;
import Utils.PreferencesHelper;
import retrofit.Callback;
import retrofit.RetrofitClient;
import retrofit.RetrofitError;
import retrofit.ServiceGenerator;
import retrofit.client.Response;

import static com.squareup.okhttp.internal.Internal.logger;

public class LoginWebview extends AppCompatActivity {
    /**
      * aouth 2.0 information
     */
    private final String baseURL = "http://api.stackexchange.com/2.2/";
    /*
    private final String clientID = "8654" ;
    private final String apiKey = "HiBX0zNFDf)d7ZY7kUGv8g((" ;
    */
    private final String clientId = "6136";
    private final String apiKey = "gQJsL7krOvbXkJ0NEI*mWA((";
    private  final String clientSecret = "wGvA23FV1FQJiVHe4lV5)A((" ;
    private String redirectUri = "https://stackexchange.com/oauth/login_success";
    public static String EXTRA_ACTION_TOKEN_URL = "TokenUrl" ;
    /*********************************************************/

    private WebView browser ;
    private ProgressBar progressBar;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_webview);

        progressBar = (ProgressBar) findViewById(R.id.progress_spinner);
        clearCookies();
        browser = (WebView)findViewById(R.id.webView_login);
        Log.i("1","1");
        browser.getSettings().setJavaScriptEnabled(true);
        Log.i("1","2");
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        final String url= ServiceGenerator.API_BASE_URL +"?client_id=" + clientId +"&scope=write_access"+"&redirect_uri=" + redirectUri+"";

        browser.setWebChromeClient(new WebChromeClient() {
            // Show loading progress in activity's title bar.
            @Override
            public void onProgressChanged(WebView view, int progress) {
                setProgress(progress * 100);

                if (progress >= 100) {
                    //setProgressBarIndeterminateVisibility(false);
                    progressBar.setVisibility(View.GONE);
                } else {
                    //setProgressBarIndeterminateVisibility(true);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        browser.setWebViewClient(new WebViewClient() {
            // When start to load page, show url in activity's title bar

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                logger.info("Auth URL: " + url);
                if (url.contains("#access_token")) {
                    return true;
                }
                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                logger.info("Loading URL: " + url);
                progressBar.setVisibility(View.VISIBLE);
                //setProgressBarIndeterminateVisibility(true);
                //setTitle(url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                logger.info("Loaded URL: " + url);
                if (url.contains("#access_token")) {

                    mProgressDialog = new ProgressDialog(LoginWebview.this);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setMessage("Loading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    String token_str = url;
                    String[] str = token_str.split("access_token=");
                    String token = str[1].substring(0, str[1].length() - 14);

                    //Calling method to get UserId
                    RetrofitClient.userIdServices().getUserId("stackoverflow", apiKey, token, new Callback<UserShortInfo>() {
                        @Override
                        public void success(UserShortInfo info, Response response) {
                            if (mProgressDialog.isShowing() && mProgressDialog!=null)
                                mProgressDialog.dismiss();
                            Log.i("success","ok");
                            Log.i("image",info.getItems().get(0).getProfile());
                            finishAct(info.getItems().get(0).getUserId(),info.getItems().get(0).getDisplayName(),info.getItems().get(0).getProfile());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (mProgressDialog.isShowing() && mProgressDialog!=null)
                                mProgressDialog.dismiss();
                            Log.i("failure","ok");
                            String merror = error.getMessage();
                            logger.info("merror :" + merror);
                            Snackbar.make(browser, merror, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                        }
                    });
                    //Intent intent = new Intent();
                    //intent.putExtra(EXTRA_ACTION_TOKEN_URL, url);
                    //setResult(Activity.RESULT_OK, intent);
                    //WebViewActivity.this.finish();
                    //overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_left);
                }
            }

        });

        browser.loadUrl(url);

        /*Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///String scope  = " ?scope \"\" ";
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(ServiceGenerator.API_BASE_URL +"?client_id=" + clientId +"?redirect_uri=" + redirectUri+""));
                startActivity(intent);
            }
        });*/
    }
    private void finishAct(String UserId,String displayName , String ProfileLink){
        Log.i("finishAct",UserId);
        Consts.UserId = UserId;
        PreferencesHelper.setLoginCheck(true);
        PreferencesHelper.setUserID(UserId);
        PreferencesHelper.setDisplayName(displayName);
        PreferencesHelper.setProfileLink(ProfileLink);

        Intent i = new Intent(LoginWebview.this, MainActivity.class);
        startActivity(i);
        LoginWebview.this.finish();
    }

    public void clearCookies(){
        CookieSyncManager.createInstance(LoginWebview.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
