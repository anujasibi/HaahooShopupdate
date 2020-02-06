package com.haahoo.haahooshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Addvar extends AppCompatActivity {
    Activity activity=this;
    EditText varname,fprice,rprice,counts;
    ImageView imgp,imageView3;
    Context context=this;
    Spinner spinner;
    ArrayList<String> areas = new ArrayList<String>();
    SessionManager sessionManager;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    String url = Global.BASE_URL+"api_shop_app/variant_value_show/";
    private int GALLERY = 1, CAMERA = 2,CGALLERY=3,CCAMERA=4;;
    String files;
    private Uri uri;
    public String country;
    TextView submit;
    public String[]seperated;
    public String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // will hide the title
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvar);

        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.black));

        submit();
        imageView3=findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Addvar.this,AddVariant.class));
            }
        });

        spinner = findViewById(R.id.spinner);
        sessionManager=new SessionManager(this);


        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submituser();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 country = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                // idsp = areasid.get(spinner.getSelectedItemPosition());
               // Toast.makeText(getApplicationContext(), spinner.getSelectedItemPosition()+country, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });




        varname=findViewById(R.id.shopname);
        fprice=findViewById(R.id.gst);
        rprice=findViewById(R.id.email);
        imgp=findViewById(R.id.imgp);
        counts=findViewById(R.id.count);
        imageView3=findViewById(R.id.imageView3);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,AddVariant.class));
            }
        });

        imgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialo();
            }
        });
    }

    private void showPictureDialo() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallar();
                                break;
                            case 1:
                                takePhotoFromCamer();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallar() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, CGALLERY);
    }

    private void takePhotoFromCamer() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CCAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CCAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgp.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            //   Toast.makeText(addshopim.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            Uri tempUri = getImageUri(getApplicationContext(), thumbnail);
            files = (getRealPathFromURI(tempUri));
            Log.d("filepath", "mm" + files);
        } else if (requestCode == CGALLERY) {
            Uri contentURI = data.getData();
            uri = data.getData();
            files = getRealPathFromURIPath(uri, Addvar.this);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                bitmap = getResizedBitmap(bitmap, 400);
                String path = saveImage(bitmap);
                //   Toast.makeText(addshopim.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                imgp.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
                //    Toast.makeText(addshopim.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
        public Uri getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            return Uri.parse(path);
        }

        public String getRealPathFromURI(Uri uri) {
            String path = "";
            if (getContentResolver() != null) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(idx);
                    cursor.close();
                }
            }
            return path;
        }


        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }


        public String saveImage(Bitmap myBitmap) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File wallpaperDirectory = new File(
                    Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
            // have the object build the directory structure, if needed.
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs();
            }

            try {
                File f = new File(wallpaperDirectory, Calendar.getInstance()
                        .getTimeInMillis() + ".jpg");
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(this,
                        new String[]{f.getPath()},
                        new String[]{"image/jpeg"}, null);
                fo.close();
                Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

                return f.getAbsolutePath();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return "";
        }

        private void requestMultiplePermissions() {
            Dexter.withActivity(this)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                             //   Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                //openSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }


                    }).
                    withErrorListener(new PermissionRequestErrorListener() {
                        @Override
                        public void onError(DexterError error) {
                            Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .onSameThread()
                    .check();
        }

        private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.getContentResolver().query(contentURI, proj, null, null, null);
            if (cursor == null) {
                return contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(idx);
            }
        }

        private void submit(){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.d("ressssssssss","mm"+response);
                        JSONObject jsonObject = new JSONObject(response);
                         data=jsonObject.optString("data");
                        Log.d("mmm","data"+data);
                        seperated=data.split(",");
                        Log.d("seperated","data"+seperated);
                        areas.add("Please Choose the Variation Value");
                        // String[]seperated=data.split(",");

                        for (int i=0;i<seperated.length;i++){
                            String name=seperated[i].replace("[","").replace("'","").replace("]","");
                            areas.add(name);
                        }
                        spinner.setAdapter(new ArrayAdapter<String>(Addvar.this, android.R.layout.simple_spinner_dropdown_item, areas));
                        Log.d("valuess","mm"+seperated[0].replace("[","").replace("'",""));

                       /* areas.add("Please Choose Variant Theme");
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("name");
                            String id = jsonObject1.getString("id");
                            sessionManager.setvar(id);
                            String value = jsonObject1.getString("values");
                            String[]seperated=value.split(",");
                            //  sessionManager.setvar(value);
                            Log.d("valuess","mm"+seperated[0].replace("[","").replace("'",""));
                            areas.add(country);
                            //     areasid.add(id);

                        }
*/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("id",sessionManager.getvar());
                    Log.d("dsdfghjkl","mm"+sessionManager.getvar());

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Token "+sessionManager.getTokens());
                    Log.d("token","mm"+sessionManager.getTokens());
                    return params;
                }


            };


            int socketTimeout = 30000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            requestQueue.add(stringRequest);

        }


    private void submituser() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        UploadAPI uploadAPIs = retrofit.create(UploadAPI.class);

        if (files == null) {
            Toast.makeText(Addvar.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
        }
        if (files != null) {
            File immm = new File(files);
            File im=new File(files);
            Log.d("mmmmmmm", "mmm" + immm.length());
            Log.d("mmmmmmm", "mmm" + im.length());
            // Create a request body with file and image media type


            RequestBody photob = RequestBody.create(MediaType.parse("image/*"), immm);
            // Create MultipartBody.Part using file request-body,file name and part name
            MultipartBody.Part part1 = MultipartBody.Part.createFormData("variant_img", immm.getName(), photob);
            Log.d("image", "mm" + part1);
            Log.d("image", "mm" + immm.getName());




            //Create request body with text description and text media type
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
            RequestBody pdt_id = RequestBody.create(MediaType.parse("text/plain"), sessionManager.getPdtaddvar());
            Log.d("nameee","mm"+sessionManager.getPdtaddvar());
            RequestBody variant_name = RequestBody.create(MediaType.parse("text/plain"), varname.getText().toString());
            Log.d("nameee","mm"+varname.getText().toString());
            RequestBody variant_value = RequestBody.create(MediaType.parse("text/plain"), country);
            Log.d("nameee","mm"+country);
            RequestBody retail_value = RequestBody.create(MediaType.parse("text/plain"), rprice.getText().toString());
            Log.d("nameee","mm"+rprice.getText().toString());
            RequestBody fixed_price = RequestBody.create(MediaType.parse("text/plain"), fprice.getText().toString());
            Log.d("nameee","mm"+fprice.getText().toString());
            RequestBody count = RequestBody.create(MediaType.parse("text/plain"), counts.getText().toString());
            Log.d("nameee","mm"+counts.getText().toString());


            //
            Call call = uploadAPIs.uploadoI("Token " + sessionManager.getTokens(),part1,pdt_id,variant_name,variant_value,retail_value,fixed_price,count);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Toast.makeText(context, "Successfull" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,viewvariant.class));
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });


        }


    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(context,AddVariant.class));
    }
}
