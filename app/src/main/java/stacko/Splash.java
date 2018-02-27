package stacko;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Utils.PreferencesHelper;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(PreferencesHelper.getLoginCheck()){//判断是否已登录

            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            Splash.this.finish();
        }else{
            Intent intent = new Intent(Splash.this , LoginWebview.class);
            startActivity(intent);
            Splash.this.finish();
        }
    }

}
