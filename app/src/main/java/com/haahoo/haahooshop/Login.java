package com.haahoo.haahooshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haahoo.haahooshop.utils.Global;
import com.haahoo.haahooshop.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputLayout pwdlayout;
    TextInputEditText phoneno,password;
    TextView continuetologin,login;
    boolean doubleBackToExitPressedOnce = false;
    Context context=this;
    String device_id = null;
    private ProgressDialog dialog ;
    Activity activity = this;
    SessionManager sessionManager;
    TextView forgot;
    ImageView logo;
    private String URLline = Global.BASE_URL+"api_shop_app/shop_role_type/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));

        sessionManager = new SessionManager(this);
        forgot=findViewById(R.id.forgot);
        phoneno=findViewById(R.id.uname);
        pwdlayout=findViewById(R.id.pwdlayout);
        password=findViewById(R.id.pwd);
        logo=findViewById(R.id.logo);
        Picasso.get().load(Global.BASE_URL+"media/files/events_add/extra_pic/haahoo_logo1.png").into(logo);
        login=findViewById(R.id.login);
        continuetologin=findViewById(R.id.continuetologin);
        dialog=new ProgressDialog(Login.this,R.style.MyAlertDialogStyle);

        continuetologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneno.getText().toString().length()==0||password.getText().toString().length()==0){
                    Toast.makeText(context,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                if(!(phoneno.getText().toString().length()==0||password.getText().toString().length()==0)){

                    dialog.setMessage("Loading");
                    dialog.show();
                    submit();
                }
            }
        });
    }
    private void submit(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        //parseData(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ot = jsonObject.optString("message");
                            String status=jsonObject.optString("role_type");
                            String token=jsonObject.optString("Token");
                            String payment=jsonObject.getString("payment_status");
                            String activation=jsonObject.getString("activation_status");
                            String code=jsonObject.optString("code");
                            //    sessionManager.setTokens(token);
                            Log.d("otp","mm"+token);
                            Log.d("code","mm"+status);
                            if (ot.equals("Invalid Credentials")){
                                Toast.makeText(context,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
//                            if(code.equals("203")){
//                                Toast.makeText(Login.this, ot+" Something went wrong", Toast.LENGTH_LONG).show();
//                            }
//                            if(status.equals("")){
//                                Toast.makeText(Login.this, "Successful", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(Login.this, choose.class);
//                                startActivity(intent);
//                            }
//                            if((!(status.equals("")))&&payment.equals("Payment not done")){
//                                Toast.makeText(Login.this, "Payment not done.Please do payment for further processing", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(Login.this, Payment.class);
//                                startActivity(intent);
//                            }
                           /* if((!(status.equals("")))&&payment.equals("Payment done")&&activation.equals("not activated")){
                                Toast.makeText(Login.this, "Please Activate Your Account", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this, activation.class);
                                startActivity(intent);
                            }*/
//                            if((!(status.equals("")))&&payment.equals("Payment done")) {
//                                Intent intent = new Intent(Login.this, Navigation.class);
//                                startActivity(intent);
//                            }


                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        Log.d("response","hhh"+response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token "+sessionManager.getTokens());
                Log.d("token","mm"+sessionManager.getTokens());
                return params;
            }
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone_no",phoneno.getText().toString());
                params.put("password",password.getText().toString());
                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}

