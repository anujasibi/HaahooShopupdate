package com.haahoo.haahooshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.textclassifier.TextLinks;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.haahoo.haahooshop.utils.Global;
import com.haahoo.haahooshop.utils.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DeliveryPlans extends AppCompatActivity {

    TextInputEditText defaultv;
    EditText distance;
    EditText disamount;
    EditText ordamount;
    EditText outamount;
    SessionManager sessionManager;
    public String url= Global.BASE_URL+"api_shop_app/shop_delivery_det/";
    Context context=this;
    TextView save;
    Activity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // will hide the title
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_plans);

        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));


        defaultv=findViewById(R.id.resellprice);
        distance=findViewById(R.id.distance);
        disamount=findViewById(R.id.disamount);
        ordamount=findViewById(R.id.orderamo);
        outamount=findViewById(R.id.outamount);
        save=findViewById(R.id.save);
        sessionManager=new SessionManager(this);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });



    }

    private void submit(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String code=jsonObject.optString("code");
                    if(code.equals("200")){
                        Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DeliveryPlans.this,"Internal Server Error",Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            public Map<String,String>getHeaders(){
                Map<String,String>params=new HashMap<>();
                params.put("Authorization","Token "+sessionManager.getTokens());
                return params;
            }

            @Override
            protected Map<String,String>getParams(){
                Map<String,String>params=new HashMap<>();
                params.put("default_charge",defaultv.getText().toString());
                params.put("free_distance",distance.getText().toString());
                params.put("cart_value",ordamount.getText().toString());
                params.put("charge",disamount.getText().toString());
                params.put("other_charge",outamount.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
