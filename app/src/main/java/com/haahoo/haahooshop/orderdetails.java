package com.haahoo.haahooshop;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.haahoo.haahooshop.utils.Global;
import com.haahoo.haahooshop.utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class orderdetails extends AppCompatActivity {

    ImageView imageView,back;
    TextView pdtname,price,cusname,phn,hno,landmark,pincode,city,state;
    public String pdtnames,prices,cusnames,phns,hnos,landmarks,pincodes,citys,states,image,ids;
    Context context=this;
    TextView dates,time,sub,submit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String dat="null";
    private String tim="null";
    SessionManager sessionManager;
    private String URLline = Global.BASE_URL+"virtual_order_management/shop_order_dispatched/";
    private ProgressDialog dialogs ;
    Activity activity = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);

        imageView=findViewById(R.id.imageVie);
        back=findViewById(R.id.imageView3);
        pdtname=findViewById(R.id.te);
        price=findViewById(R.id.tee);
        cusname=findViewById(R.id.location);
        phn=findViewById(R.id.owner);
        hno=findViewById(R.id.gstno);
        landmark=findViewById(R.id.category);
        pincode=findViewById(R.id.categoryy);
        city=findViewById(R.id.cityy);
        state=findViewById(R.id.statees);
        dates=findViewById(R.id.date);
        time=findViewById(R.id.time);
        sub=findViewById(R.id.sub);
        submit=findViewById(R.id.submit);
        sessionManager=new SessionManager(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,UpcomingOrder.class));
            }
        });

        Bundle bundle = getIntent().getExtras();

        image=bundle.getString("pdtimage");
        pdtnames=bundle.getString("pdtname");
        prices=bundle.getString("price");
        cusnames=bundle.getString("cusname");
        phns=bundle.getString("phone");
        hnos=bundle.getString("house");
        landmarks=bundle.getString("landmark");
        pincodes=bundle.getString("pincode");
        citys=bundle.getString("city");
        states=bundle.getString("state");
        ids=bundle.getString("id");


      //  Picasso.with(context).load(image).into(imageView);
        Picasso.get().load(image).into(imageView);
        pdtname.setText(pdtnames);
        price.setText("₹ "+prices);
        cusname.setText(cusnames);
        phn.setText(phns);
        hno.setText(hnos);
        landmark.setText(landmarks);
        pincode.setText(pincodes);
        city.setText(citys);
        state.setText(states);
        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));
        dialogs=new ProgressDialog(orderdetails.this,R.style.MyAlertDialogStyle);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub.setVisibility(View.GONE);
                dates.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
        });


        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);




                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                     @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dates.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                dat=dates.getText().toString();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                                tim=time.getText().toString();
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dat.equals("null")||tim.equals("null")){
                    Toast.makeText(context,"Please choose dispatch date and time",Toast.LENGTH_SHORT).show();
                }
                if(!(dat.equals("null")||tim.equals("null"))){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Do you want to dispatch this product??");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dialogs.setMessage("Loading");
                                    dialogs.show();
                                    subuser();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            }
        });
    }

    private void subuser(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogs.dismiss();
                        //  Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        //parseData(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String ot = jsonObject.optString("message");
                            String status=jsonObject.optString("code");
                            String token=jsonObject.optString("Token");
                            //    sessionManager.setTokens(token);




                            Log.d("otp","mm"+token);
                            Log.d("code","mm"+status);
                            if(status.equals("200")&&(!(ot.equals("verify")))){
                                Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(context,Uporder.class));



                            }


                            if(!(status.equals("200"))){
                                Toast.makeText(context, "Failed."+ot, Toast.LENGTH_LONG).show();


                            }

                        } catch (JSONException e) {
                            dialogs.dismiss();
                            e.printStackTrace();
                        }
                        Log.d("response","hhh"+response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialogs.dismiss();
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("order_id",ids);
                params.put("dispatched","1");
                params.put("date",dat);
                params.put("time",tim);


                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sessionManager.getTokens());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,Uporder.class));
    }
}
