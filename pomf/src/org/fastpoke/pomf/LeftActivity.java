package org.fastpoke.pomf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static org.fastpoke.pomf.R.layout.left;

public class LeftActivity extends Activity {
    TextView tvView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.left);
        this.setContentView(left);
        org.fastpoke.pomf.Random.randomLeft(this);
    }

    public void onClickMain(View SlideToMainL) {
        Intent intent = new Intent(LeftActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        //finish();
    }

}
