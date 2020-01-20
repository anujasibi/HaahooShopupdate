package com.haahoo.haahooshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haahoo.haahooshop.utils.Global;
import com.haahoo.haahooshop.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class finaladd extends AppCompatActivity {
    TextInputEditText distance;
    Spinner spinner;
    public String status="";
    public String status1="";
    ArrayList<String> areas = new ArrayList<String>();
    String delivery_type = "null";
    //String URL="https://testapi.creopedia.com/api_shop_app/list_shop_cat/ ";
    //String URL="https://haahoo.in/api_shop_app/list_shop_cat/ ";
    String URL= Global.BASE_URL+"api_shop_app/list_shop_cat/ ";
    CheckBox checkBox1,checkBox2,checkBox3,check,checkm,checks1,checks2;
    private RadioGroup radioSexGroup;
    private RadioButton one,two,three;
    SessionManager sessionManager;
    ImageView imageView;
    TextInputLayout ress;
    TextInputEditText resell;
    TextView save,res;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaladd);

        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));
        res=findViewById(R.id.res);
        ress=findViewById(R.id.ress);
        resell=findViewById(R.id.resellprice);
        checkBox1 = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox1);
        checkBox3 = findViewById(R.id.checkBox2);
        check=findViewById(R.id.checkBo);
        checkm=findViewById(R.id.checkBo1);
        checks1=findViewById(R.id.checkBot);
        checks2=findViewById(R.id.checkBo1t);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        one=findViewById(R.id.radioMale);
        two=findViewById(R.id.radioFemale);
        three=findViewById(R.id.radioFe);
        save=findViewById(R.id.save);
        distance=findViewById(R.id.name);
        sessionManager=new SessionManager(this);
        imageView=findViewById(R.id.imageView3);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(finaladd.this,addprod.class));
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkm.setChecked(false);
                status="1";
                res.setVisibility(View.VISIBLE);
                ress.setVisibility(View.VISIBLE);
                sessionManager.setcheckn(status);


            }
        });

        checkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check.setChecked(false);
                status="0";
                sessionManager.setcheckn(status);
                res.setVisibility(View.GONE);
                ress.setVisibility(View.GONE);
                resell.setText("0");
            }
        });

        checks1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checks2.setChecked(false);
                status1="1";
                sessionManager.setaddshop(status1);
            }
        });
        checks2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checks1.setChecked(false);
                status1="0";
                sessionManager.setaddshop(status1);
            }
        });


        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checkBox2.setChecked(false);
                    checkBox3.setChecked(false);
                    delivery_type = checkBox1.getText().toString();
                    sessionManager.setcheck(delivery_type);
                 //   Toast.makeText(finaladd.this,"bhnjv"+checkBox1.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checkBox1.setChecked(false);
                    checkBox3.setChecked(false);
                    delivery_type = checkBox2.getText().toString();
                    sessionManager.setcheck(delivery_type);
                 //   Toast.makeText(finaladd.this,"bhnjv"+checkBox2.getText().toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    checkBox1.setChecked(false);
                    checkBox2.setChecked(false);
                    delivery_type = checkBox3.getText().toString();
                    sessionManager.setcheck(delivery_type);
                //    Toast.makeText(finaladd.this,"bhnjv"+checkBox3.getText().toString(),Toast.LENGTH_SHORT).show();

                }
            }
        });

        sessionManager.setcheck(delivery_type);

        Log.d("gvggxxsxsssxss","mm"+delivery_type);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(distance.getText().length()==0||delivery_type.equals("null")||status.equals("")||(resell.getText().toString().length() == 0)) {
                    Toast.makeText(finaladd.this, "All are fields are required", Toast.LENGTH_SHORT).show();
                }
                if(!(distance.getText().length()==0||delivery_type.equals("null")||status.equals(""))) {
                    if (!(resell.getText().toString().length() == 0)) {
                        sessionManager.setreselprice(resell.getText().toString());
                        Log.d("mmmmmmmmmmmm", "mm" + sessionManager.getreselprice());
                        sessionManager.setcatdistance(distance.getText().toString());
                        int selectedId = radioSexGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id
                        one = (RadioButton) findViewById(selectedId);

                        sessionManager.setradio(one.getText().toString());

                     /*   Toast.makeText(finaladd.this,
                                one.getText(), Toast.LENGTH_SHORT).show();*/

                        startActivity(new Intent(finaladd.this, subscription.class));
                    }
                }
            }
        });


//        if (checkBox1.isChecked()){
//            delivery_type = checkBox1.getText().toString();
//            Toast.makeText(finaladd.this,"hjvgjh"+delivery_type,Toast.LENGTH_SHORT).show();
//            checkBox2.setChecked(false);
//            checkBox3.setChecked(false);
//        }
//        if (checkBox2.isChecked()){
//            delivery_type = checkBox2.getText().toString();
//            Toast.makeText(finaladd.this,"hjvgjh"+delivery_type,Toast.LENGTH_SHORT).show();
//            checkBox1.setChecked(false);
//            checkBox3.setChecked(false);
//        }
//        if (checkBox3.isChecked()){
//            delivery_type = checkBox3.getText().toString();
//            Toast.makeText(finaladd.this,"hjvgjh"+delivery_type,Toast.LENGTH_SHORT).show();
//            checkBox1.setChecked(false);
//            checkBox2.setChecked(false);
//        }


       // spinner = findViewById(R.id.spinner);
       // loadSpinnerData(URL);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String country= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
//              //  idsp=areasid.get(spinner.getSelectedItemPosition());
//                Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                // DO Nothing here
//            }
//        });

    }



    private void loadSpinnerData(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String country=jsonObject1.getString("name");
                        String id=jsonObject1.getString("id");
                        areas.add(country);
                      //  areasid.add(id);

                    }

                    spinner.setAdapter(new ArrayAdapter<String>(finaladd.this, android.R.layout.simple_spinner_dropdown_item, areas));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(finaladd.this,addprod.class));
    }
}
