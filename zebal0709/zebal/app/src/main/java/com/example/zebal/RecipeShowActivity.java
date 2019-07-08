package com.example.zebal;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class RecipeShowActivity extends AppCompatActivity {
    String foodname;
    TextView textView;
    String recipe;
    TextView textView2;
    JSONObject obj2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recipe);

        Intent intent = getIntent();
        foodname = intent.getStringExtra("food");
        textView = findViewById(R.id.foodname);
        textView.setText(foodname);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(50);
        getRecipe();
        try {
            recipe = obj2.getString("recipe");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView2 = findViewById(R.id.recipe);
        textView2.setText(recipe.replaceAll("asdf", "\n"));
        textView2.setTextSize(25);
        textView2.setGravity(Gravity.LEFT);
        textView2.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getRecipe() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        //URL url = new URL("http://10.0.2.2:3000/getRecipeDetail");
                        URL url = new URL("http://143.248.36.32:3000/getRecipeDetail");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        StringBuilder sb = new StringBuilder();

                        OutputStream out = conn.getOutputStream();
                        String upload_food = "name=" + foodname;
                        out.write(upload_food.getBytes());
                        Log.d("yogi", "yogi1");




                        InputStreamReader in2 = new InputStreamReader(conn.getInputStream(), Charset.defaultCharset());
                        Log.d("yogi", "yogi1");
                        BufferedReader bufferedReader = new BufferedReader(in2);
                        Log.d("yogi", "yogi1");
                        if (bufferedReader != null) {
                            int cp;

                            while ((cp = bufferedReader.read()) != -1) {
                                sb.append((char) cp);
                            }

                            bufferedReader.close();
                        }
                        Log.d("yogi", "yogi2");
                        String sbstring = (String) sb.toString();
                        Log.d("sb", sbstring);
                        obj2 = new JSONObject(sbstring);

                        Log.d("sbtojson", obj2.toString());
                        in2.close();
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
}
