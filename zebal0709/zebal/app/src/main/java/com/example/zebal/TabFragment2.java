package com.example.zebal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TabFragment2 extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<ImageList> arrayList;
    private ImageAdapter imageAdapter;
    int numOfColumns = 4;
    Button gallerybutton, loadbutton, camerabutton;
    private static final int PICK_IMAGE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    Uri imageUri;
    private JSONArray dbList = new JSONArray();
    JSONArray obj;
    private String cameraFilePath;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        Context context = view.getContext();

        obj = new JSONArray();

        loadbutton = view.findViewById(R.id.gallery_load_button);
        camerabutton = view.findViewById(R.id.camera_button);

        gallerybutton = view.findViewById(R.id.gallery_button);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        arrayList = new ArrayList<ImageList>();

        loadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadGallery();
                getdb(dbList);
                Log.d("objcomeout", obj.toString());
                for (int i = 0; i < obj.length(); i++) {
                    try {
                        JSONObject objitem = obj.getJSONObject(i);
                        String objid = objitem.getString("id");
                        String objimage = objitem.getString("image");
                        ImageList objdtl = new ImageList(objid, objimage);
                        arrayList.add(objdtl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                imageAdapter = new ImageAdapter(arrayList, getContext());
                imageAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(imageAdapter);
            }
        });

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadGallery();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        gallerybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openGallery();
            }
        });
        imageAdapter = new ImageAdapter(arrayList, getContext());
        recyclerView.setAdapter(imageAdapter);
        recyclerView.addOnItemTouchListener(new ImageTouchListener(getContext(), new ImageTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                intent.putExtra("image", arrayList.get(position).getImage());
                startActivity(intent);
            }
        }));

        return view;
    }

    private void getdb(final JSONArray dbl) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/getGallery");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        StringBuilder sb = new StringBuilder();
                        System.out.println("get"+conn.getResponseMessage());
                        InputStreamReader in = new InputStreamReader(conn.getInputStream(), Charset.defaultCharset());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        if (bufferedReader != null) {
                            int cp;

                            while ((cp = bufferedReader.read()) != -1) {
                                sb.append((char) cp);
                            }
                            bufferedReader.close();
                        }
                        String sbstring = sb.toString();
                        Log.d("sb", sb.toString());
                        obj = new JSONArray(sbstring);
                        Log.d("sbtojson", obj.toString());
                        in.close();
                        conn.disconnect();
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notify();
                }
            }

        };
        thread.start();
        synchronized (thread){
            try{
                thread.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    private void loadGallery() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/getGallery");
                        //URL url = new URL("http//10.0.2.2:3000/addGallery");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        System.out.println("getResponseCode():" + conn.getResponseMessage());
                        System.out.println("getContent" + conn.getResponseCode());
                        conn.disconnect();

                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notify();
                }
            }
        };
        thread.start();
        synchronized (thread){
            try{
                thread.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if (resultCode == RESULT_OK) {
                Log.d("cameracamera", "hihihhih");
                switch (requestCode) {
                    case CAMERA_REQUEST_CODE:
                        imageUri = Uri.parse(cameraFilePath);
                        Log.d("$$$$$$uri", imageUri.toString());
                        //  imageView.setImageURI(Uri.parse(cameraFilePath));
                        break;
                }
            }
            if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
                imageUri = data.getData();


                String id = Long.toString(ContentUris.parseId(imageUri));
                int len = arrayList.size();
                int exist = 0;
                for (int i = 0; i < len; i++) {
                    if (arrayList.get(i).getId().equals(id)){
                        exist = 1;
                    }
                }
                if (exist == 0) {
                    arrayList.add(new ImageList(id, imageUri.toString()));
                }
                Log.d("num ***************", Integer.toString(imageAdapter.getItemCount()));

                Log.d("Gallerydiuni uri", imageUri.toString());
                Log.d("Gallerydiuni id", id);
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        synchronized (this){
                            try {
                                URL url = new URL("http://143.248.36.32:3000/addGallery");
                                //URL url = new URL("http//10.0.2.2:3000/addGallery");
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("POST");
                                conn.setDoOutput(true);
                                OutputStream out = conn.getOutputStream();
                                String upload_id = "id=" + ContentUris.parseId(imageUri);
                                String upload_image = "image=" + imageUri.toString();
                                out.write(upload_id.getBytes());
                                out.write("&".getBytes());
                                out.write(upload_image.getBytes());
                                InputStream in = conn.getInputStream();
                                ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                                byte[] buf = new byte[1024 * 8];
                                int length = 0;
                                while ((length = in.read(buf)) != -1) {
                                    out2.write(buf, 0, length);
                                }
                                System.out.println(new String(out2.toByteArray(), "UTF-8"));
                                conn.disconnect();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            notify();
                        }
                    }
                };
                thread.start();
                synchronized (thread){
                    try{
                        thread.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                imageAdapter = new ImageAdapter(arrayList, getContext());
                recyclerView.setAdapter(imageAdapter);
            }
        }

    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

}