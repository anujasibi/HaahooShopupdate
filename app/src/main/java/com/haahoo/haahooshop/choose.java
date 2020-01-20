package com.haahoo.haahooshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.haahoo.haahooshop.utils.SessionManager;

public class choose extends AppCompatActivity {

    Context context=this;
    String device_id = null;
    private ProgressDialog dialog ;
    Activity activity = this;
    CardView card,cardn;
    SessionManager sessionManager;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        sessionManager=new SessionManager(this);
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));

        card=findViewById(R.id.card);
        cardn=findViewById(R.id.cardn);
        textView=findViewById(R.id.text);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,stepper.class));
                sessionManager.setrole(textView.getText().toString());
                Log.d("roleeree","mmm"+sessionManager.getrole());

            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,MainActivity.class));
    }
}
