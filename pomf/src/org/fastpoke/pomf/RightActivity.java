package org.fastpoke.pomf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static org.fastpoke.pomf.R.layout.right;

public class RightActivity extends Activity {
    TextView tvView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.right);
        this.setContentView(right);
        org.fastpoke.pomf.Random.randomRight(this);
    }

    public void onClickMain(View SlideToMainR) {
        Intent intent = new Intent(RightActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        //finish();
    }
}
