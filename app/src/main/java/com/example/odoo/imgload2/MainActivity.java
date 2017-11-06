package com.example.odoo.imgload2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Hashtable;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RESULT_LOAD_IMAGE = 1;
    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static int REQUEST_PENDING=0;

//requests for runtime time permissions

//    String CAMERA_PERMISSION = android.Manifest.permission.CAMERA; 

//    String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE; 

//    String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;





    @IntDef({DialogType.DIALOG_DENY, DialogType.DIALOG_NEVER_ASK})
    @Retention(RetentionPolicy.SOURCE)
    @interface DialogType {
        int DIALOG_DENY = 0, DIALOG_NEVER_ASK = 1;
    }










    // for security permissions
    @DialogType
    private int mDialogType;
    private String mRequestPermissions = "We are requesting the camera and Gallery permission as it is absolutely necessary for the app to perform it\'s functionality.\nPlease select \"Grant Permission\" to try again and \"Cancel \" to exit the application.";
    private String mRequsetSettings = "You have rejected the camera and Gallery permission for the application. As it is absolutely necessary for the app to perform you need to enable it in the settings of your device.\nPlease select \"Go to settings\" to go to application settings in your device and \"Cancel \" to exit the application.";
    private String mGrantPermissions = "Grant Permissions";
    private String mCancel = "Cancel";
    private String mGoToSettings = "Go To Settings";
    private String mPermissionRejectWarning = "Cannot Proceed Without Permissions</string><string name=\"explanation_permission_location_request\">We are requesting the location permission as it is necessary for the app to perform search functionality properly.\nPlease select \"Grant Permission\" to try again and \"Cancel \" to deny permission.";
    private static final String SERVER_ADDRESS = "http://192.168.43.113:808/savePicture.php";
    private static  String IP = "192.168.1.33";
    ImageView imageToUpload;
    Button bUploadImage;
    EditText uploadImageName;
    Bitmap p;
    TextView txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        bUploadImage = (Button) findViewById(R.id.bUploadImage);
        uploadImageName = (EditText) findViewById(R.id.etUploadName);

        imageToUpload.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, MainActivity.this);
        } else {
            // Open your camera here.
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.change_proxy:
                showChangeLangDialog();
                return true;

            //similarly write for other actions

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_pop_up, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.etServerAddress);

        dialogBuilder.setTitle("Server Address");
        dialogBuilder.setMessage("Enter the IP Address of the server");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 IP = edt.getText().toString();
                Toast.makeText(MainActivity.this, "IP entered" , Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selected_image = data.getData();
            imageToUpload.setImageURI(selected_image);

        }
    }

//    private class UploadImage extends AsyncTask<Void,Void,Void>{
//
//        Bitmap image;
//        String name;
//        public UploadImage(Bitmap image,String name){
//            this.image = image;
//            this.name = name;
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//
////
////            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
////            dataToSend.add(new BasicNameValuePair("image", encodedImage));
////            dataToSend.add(new BasicNameValuePair("name", name));
////
////            HttpParams httpRequestParams = getHttpRequestParams();
////            HttpClient client = new DefaultHttpClient(httpRequestParams);
////            HttpPost post = new HttpPost();
////
////
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//        }
//    }
//
//    private HttpParams getHttpRequestParams(){
//        HttpParams httpRequestParams = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(httpRequestParams,1000*30);
//        HttpConnectionParams.setSoTimeout(httpRequestParams,1000*30);
//        return httpRequestParams;
//    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    // Call your camera here.
                } else {
                    boolean showRationale1 = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA);
                    boolean showRationale2 = shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    boolean showRationale3 = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (showRationale1 && showRationale2 && showRationale3) {
                        //explain to user why we need the permissions
                        mDialogType = DialogType.DIALOG_DENY;
                        // Show dialog with
                        openAlertDialog(mRequestPermissions, mGrantPermissions, mCancel, new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                switch (mDialogType) {
                                    case DialogType.DIALOG_DENY:
                                        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, MainActivity.this);
                                        break;
                                    case DialogType.DIALOG_NEVER_ASK:
                                        finish();
                                        break;

                                }
                            }

                            @Override
                            public void onNegativeButtonClicked() {

                            }
                            
                        }, MainActivity.this);
                    } else {
                        //explain to user why we need the permissions and ask him to go to settings to enable it
                        mDialogType = DialogType.DIALOG_NEVER_ASK;
                        openAlertDialog(mRequsetSettings, mGoToSettings, mCancel, new OnDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                switch (mDialogType) {
                                    case DialogType.DIALOG_DENY:
                                        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, MainActivity.this);
                                        break;
                                    case DialogType.DIALOG_NEVER_ASK:
                                        finish();
                                        break;

                                }
                            }

                            @Override
                            public void onNegativeButtonClicked() {

                            }



                        }, MainActivity.this);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            // Open your camera here.
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageToUpload:
//                uploadImageName.setText(null);
//                txt.setText(null);
                Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(CameraIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploadImage:
//                Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
                uploadImage();
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // this code will be executed after 2 seconds
//                    }
//                }, 5000);

//                getResult();
                break;
        }
        
    }

    public static void openAlertDialog(String message, String positiveBtnText, String negativeBtnText,
                                       final OnDialogButtonClickListener listener,Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AppTheme);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onPositiveButtonClicked();
            }
        });
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                listener.onNegativeButtonClicked();
            }
        });

        builder.setTitle(mContext.getResources().getString(R.string.app_name));
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.create().show();
    }
    public interface OnDialogButtonClickListener {

        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }


    private void uploadImage(){
        p = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        //Showing the progress dialog
//        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        String str = "http://" + IP + ":808/savePicture.php";
//        String result_php = "http://" + IP + ":808/getResult.php";
        //Toast.makeText(MainActivity.this, SERVER_ADDRESS , Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                getResult();

                            }
                        }, 10000);
//                        while (REQUEST_PENDING != 0){
//                            getResult();
//                        }
                        //Disimissing the progress dialog
//                        loading.dismiss();
//                        txt = (TextView)findViewById(R.id.resultTextView);
//                        txt.setText(s);
                        //Showing toast message of the response
//                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
//                        loading.dismiss();

                        //Showing toast

//                        Toast.makeText(MainActivity.this, ""+volleyError, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(p);

                //Getting Image Name
                String name = uploadImageName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();


//                params.put("empsno", "81");
//                params.put("storesno", "165");
//                params.put("lrSno", "1808");
//                params.put("recQty", "0");
//                params.put("recVol", "0");
//                params.put("recWgt", "0");
//                params.put("damageQty", "0");
//                params.put("looseQty", "0");
//                params.put("deliveryDate", "2016-09-24");
//                params.put("deliveryTime", "10:15");
                params.put("image", image);
                params.put("name", name);
//                params.put("receivedBy", "amankumar");
//                params.put("ipAddress", "12.65.65.32");

                //returning parameters
                return params;
            }
        };

//        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, result_php,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        //Disimissing the progress dialog
////                        loading.dismiss();
//                        txt = (TextView)findViewById(R.id.resultTextView);
//                        txt.setText(s);
//                        //Showing toast message of the response
//                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        //Dismissing the progress dialog
////                        loading.dismiss();
//
//                        //Showing toast
//
//                        Toast.makeText(MainActivity.this, ""+volleyError, Toast.LENGTH_LONG).show();
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                //Converting Bitmap to String
//                String image = getStringImage(p);
//
//                //Getting Image Name
//                String name = uploadImageName.getText().toString().trim();
//
//                //Creating parameters
//                Map<String,String> params = new Hashtable<String, String>();
//
//
////                params.put("empsno", "81");
////                params.put("storesno", "165");
////                params.put("lrSno", "1808");
////                params.put("recQty", "0");
////                params.put("recVol", "0");
////                params.put("recWgt", "0");
////                params.put("damageQty", "0");
////                params.put("looseQty", "0");
////                params.put("deliveryDate", "2016-09-24");
////                params.put("deliveryTime", "10:15");
////                params.put("image", image);
////                params.put("name", name);
////                params.put("receivedBy", "amankumar");
////                params.put("ipAddress", "12.65.65.32");
//
//                //returning parameters
//                return params;
//            }
//        };
//        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        REQUEST_PENDING = 1;
        //Adding request to the queue
        requestQueue.add(stringRequest);
//        requestQueue.add(stringRequest2);
    }



    private void getResult(){
        String result_php = "http://" + IP + ":808/getResult.php";

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, result_php,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        if(s != ""){
//                            REQUEST_PENDING=0;
//                        }

                        txt = (TextView)findViewById(R.id.resultTextView);
                        txt.setText(s);
                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, ""+volleyError, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new Hashtable<String, String>();


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest2);
    }



}
