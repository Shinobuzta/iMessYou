package com.example.imessyou;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
 
@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity {
 private static final String TAG = MainActivity.class.getName();
    ImageView viewImage;
    TextView cameraButton;
    TextView addPhotoButton;
    TextView sendMessage;
    Bitmap thumbnail;
    EditText messageText;
    TelephonyManager tManager;
    String uid;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraButton=(TextView)findViewById(R.id.btnCamera);
        addPhotoButton=(TextView)findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)findViewById(R.id.viewImage);
        messageText = (EditText)findViewById(R.id.messageText);
        sendMessage = (TextView)findViewById(R.id.btnSendMessage);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectImage();
            	
            	 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                 File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                 intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                 startActivityForResult(intent, 1);
            }
        });
        
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectImage();
            	
            	 Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 startActivityForResult(intent, 2);
            }
        });
        
        sendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					executeMultipartPost();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        messageText.setText(format);
        
        tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();
        
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
 
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions); 
                   
                    viewImage.setImageBitmap(bitmap);
 
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
 
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.e("path of image from gallery......******************.........", picturePath+"");
                viewImage.setImageBitmap(thumbnail);
            }
        }
    }   
    
    
    public void executeMultipartPost() {
        try {
        	Log.e(TAG, "-----------------------");
        	SimpleDateFormat sampleFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
            String format = sampleFormat.format(new Date());
            
            Log.e(TAG, "-----------------------" + format);
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");          
            System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Log.e(TAG, "-----------------------x");
            thumbnail.compress(CompressFormat.JPEG, 75, bos);
            Log.e(TAG, "-----------------------y");
            byte[] data = bos.toByteArray();
            
            Log.e(TAG, "----------------------- data" + data.toString());
//            HttpParams httpParameters = new BasicHttpParams();
//        	HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
//        	HttpConnectionParams.setSoTimeout(httpParameters, 5000);
//            
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://imessyou.cloudapp.net/index.php");
            ByteArrayBody bab = new ByteArrayBody(data,format +".jpg" );
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            Log.e(TAG, "-----------------------1");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            Log.e(TAG, "-----------------------uid" + uid);
            reqEntity.addPart("user",new StringBody(uid));
            reqEntity.addPart("message", new StringBody("yourstring"));
            reqEntity.addPart("key", new StringBody("12345"));
//            reqEntity.addPart("file", bab);
            Log.e(TAG, "-----------------------a");
            postRequest.setEntity(reqEntity);
            Log.e(TAG, "-----------------------b");
            HttpResponse response = httpClient.execute(postRequest);
            Log.e(TAG, "-----------------------c ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            Log.e(TAG, "-----------------------d");
            String sResponse;
            StringBuilder s = new StringBuilder();
 
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            Log.e(TAG,"Response: " + s);
        } catch (Exception e) {
            // handle exception here
            Log.e(TAG, e.getMessage());
        }
    }
    
}
 