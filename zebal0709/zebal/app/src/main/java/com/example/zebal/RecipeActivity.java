package com.example.zebal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    String ingredient;
    RecyclerView recyclerView;
    ArrayList<RecipeList> arrayList, result;
    JSONArray obj, obj2;
    RecipeAdapter recipeAdapter;
    int meat, egg, potato, fishcake, kimchi, onion, tofu;
    List<String> meatlist, egglist, potatolist, fishcakelist, kimchilist, onionlist, tofulist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity);

        Intent intent = getIntent();
        ingredient = intent.getStringExtra("ingredient");
        Log.d("ingreingre", ingredient);

        recyclerView = findViewById(R.id.RecyclerviewIng);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();

        recipeAdapter = new RecipeAdapter(arrayList, this);
        result = foodChooser();
        for (int i = 0; i < result.size(); i++) {
            arrayList.add(result.get(i));
        }

        recipeAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String give = arrayList.get(position).getFood();
                Log.d("giverecipe", give);
                Intent intent = new Intent(RecipeActivity.this, RecipeShowActivity.class);
                intent.putExtra("food", give);
                startActivity(intent);

            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        recyclerView.setAdapter(recipeAdapter);




    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    private ArrayList<RecipeList> foodChooser() {
        ArrayList<RecipeList> foodlist = new ArrayList<>();

        try {
            obj = new JSONArray(ingredient);
            getdb();
            Log.d("recipe", obj2.getJSONObject(0).getString("meat"));
            meatlist = Arrays.asList(obj2.getJSONObject(0).getString("meat").split(", "));
            egglist = Arrays.asList(obj2.getJSONObject(0).getString("egg").split(", "));
            potatolist = Arrays.asList(obj2.getJSONObject(0).getString("potato").split(", "));
            fishcakelist = Arrays.asList(obj2.getJSONObject(0).getString("fishcake").split(", "));
            kimchilist = Arrays.asList(obj2.getJSONObject(0).getString("kimchi").split(", "));
            onionlist = Arrays.asList(obj2.getJSONObject(0).getString("onion").split(", "));
            tofulist = Arrays.asList(obj2.getJSONObject(0).getString("tofu").split(", "));
            for (String aa :meatlist) {
                Log.e("meatlist", aa);
            }
            for (int i = 0; i < obj.length(); i++) {
                JSONObject objitem = obj.getJSONObject(i);
                meat = objitem.getInt("ing1");
                egg = objitem.getInt("ing2");
                potato = objitem.getInt("ing3");
                fishcake = objitem.getInt("ing4");
                kimchi = objitem.getInt("ing5");
                onion = objitem.getInt("ing6");
                tofu = objitem.getInt("ing7");
            }

            if (meat > 0) {

                for (int i = 0; i < meatlist.size(); i++) {
                    RecipeList rl = new RecipeList(meatlist.get(i));
                    foodlist.add(rl);
                }
            }
            if (egg > 0) {
                for (int i = 0; i < egglist.size(); i++) {
                    RecipeList rl = new RecipeList(egglist.get(i));
                    foodlist.add(rl);
                }
            }
            if (potato > 0) {
                for (int i = 0; i < potatolist.size(); i++) {
                    RecipeList rl = new RecipeList(potatolist.get(i));
                    foodlist.add(rl);
                }
            }
            if (fishcake > 0) {
                for (int i = 0; i < fishcakelist.size(); i++) {
                    RecipeList rl = new RecipeList(fishcakelist.get(i));
                    foodlist.add(rl);
                }
            }
            if (kimchi > 0) {
                for (int i = 0; i < kimchilist.size(); i++) {
                    RecipeList rl = new RecipeList(kimchilist.get(i));
                    foodlist.add(rl);
                }
            }
            if (onion > 0) {
                for (int i = 0; i < onionlist.size(); i++) {
                    RecipeList rl = new RecipeList(onionlist.get(i));
                    foodlist.add(rl);
                }
            }
            if (tofu > 0) {
                for (int i = 0; i < tofulist.size(); i++) {
                    RecipeList rl = new RecipeList(tofulist.get(i));
                    foodlist.add(rl);
                }
            }
            return foodlist;

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void getdb() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/getRecipe");
                        //URL url = new URL("http://10.0.2.2:3000/getRecipe");
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
                        obj2 = new JSONArray(sbstring);
                        Log.d("sbtojson", obj2.toString());
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
        synchronized (thread) {
            try {
                thread.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
