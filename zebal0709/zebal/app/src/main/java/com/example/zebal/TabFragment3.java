package com.example.zebal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


public class TabFragment3 extends Fragment  {


    Button button;
    Button recipebutton;
    private static final long number = 0;
    private long num = number;
    JSONArray obj;
    JSONArray dbList;
    int ing1, ing2, ing3, ing4, ing5, ing6, ing7;
    ImageView meat;
    ImageView egg1, egg2, egg3, egg4;
    ImageView potato1, potato2, potato3;
    ImageView fishcake;
    ImageView kimchi;
    ImageView onion1, onion2;
    ImageView tofu;
    int edt_ing1, edt_ing2,edt_ing3,edt_ing4,edt_ing5,edt_ing6,edt_ing7;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

//        MainActivity activity = (MainActivity) getActivity();
//        final String myName = activity.getMyname();
//        Toast.makeText(getContext(), "your name" + myName, Toast.LENGTH_SHORT).show();

        getdb(dbList);


        for (int i = 0; i < obj.length(); i++) {
            try {
                JSONObject objitem = obj.getJSONObject(i);
                ing1 = objitem.getInt("ing1");
                ing2 = objitem.getInt("ing2");
                ing3 = objitem.getInt("ing3");
                ing4 = objitem.getInt("ing4");
                ing5 = objitem.getInt("ing5");
                ing6 = objitem.getInt("ing6");
                ing7 = objitem.getInt("ing7");

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("ing1", Integer.toString(ing1));
        meat = (ImageView) view.findViewById(R.id.meat);
        egg1 = (ImageView) view.findViewById(R.id.egg1);
        egg2 = (ImageView) view.findViewById(R.id.egg2);
        egg3 = (ImageView) view.findViewById(R.id.egg3);
        egg4 = (ImageView) view.findViewById(R.id.egg4);
        potato1 = (ImageView) view.findViewById(R.id.potato1);
        potato2 = (ImageView) view.findViewById(R.id.potato2);
        potato3 = (ImageView) view.findViewById(R.id.potato3);
        fishcake = (ImageView) view.findViewById(R.id.fishcake);
        kimchi = (ImageView) view.findViewById(R.id.kimchi);
        onion1 = (ImageView) view.findViewById(R.id.onion1);
        onion2 = (ImageView) view.findViewById(R.id.onion2);
        tofu = (ImageView) view.findViewById(R.id.tofu);


        showIngredient(ing1, ing2, ing3, ing4, ing5, ing6, ing7);

        //if (roll == 1){
        //   Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        //    iv.startAnimation(animation);
        //}
        Log.d("num ", String.valueOf(num));

        button = (Button)view.findViewById(R.id.resetbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final View addingre = LayoutInflater.from(getContext()).inflate(R.layout.add_ingredient, null, false);

                for (int i = 0; i < obj.length(); i++) {
                    try {
                        JSONObject objitem = obj.getJSONObject(i);
                        ing1 = objitem.getInt("ing1");
                        ing2 = objitem.getInt("ing2");
                        ing3 = objitem.getInt("ing3");
                        ing4 = objitem.getInt("ing4");
                        ing5 = objitem.getInt("ing5");
                        ing6 = objitem.getInt("ing6");
                        ing7 = objitem.getInt("ing7");

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                NumberPicker np1 = addingre.findViewById(R.id.numberPicker1);
                np1.setMinValue(0);
                np1.setMaxValue(5);
                np1.setValue(ing1);
                np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing1 = newVal;
                    }
                });

                NumberPicker np2 = addingre.findViewById(R.id.numberPicker2);
                np2.setMinValue(0);
                np2.setMaxValue(5);
                np2.setValue(ing2);
                np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing2 = newVal;
                    }
                });

                NumberPicker np3 = addingre.findViewById(R.id.numberPicker3);
                np3.setMinValue(0);
                np3.setMaxValue(5);
                np3.setValue(ing3);
                np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing3 = newVal;
                    }
                });

                NumberPicker np4 = addingre.findViewById(R.id.numberPicker4);
                np4.setMinValue(0);
                np4.setMaxValue(5);
                np4.setValue(ing4);
                np4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing4 = newVal;
                    }
                });

                NumberPicker np5 = addingre.findViewById(R.id.numberPicker5);
                np5.setMinValue(0);
                np5.setMaxValue(5);
                np5.setValue(ing5);
                np5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing5 = newVal;
                    }
                });

                NumberPicker np6 = addingre.findViewById(R.id.numberPicker6);
                np6.setMinValue(0);
                np6.setMaxValue(5);
                np6.setValue(ing6);
                np6.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing6 = newVal;
                    }
                });

                NumberPicker np7 = addingre.findViewById(R.id.numberPicker7);
                np7.setMinValue(0);
                np7.setMaxValue(5);
                np7.setValue(ing7);
                np7.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                        edt_ing7 = newVal;
                    }
                });


                builder.setPositiveButton("OK", null);
                builder.setNegativeButton("Cancel", null);
                builder.setView(addingre);
                builder.setTitle("Refreigerator Update!");

                final AlertDialog bd = builder.create();

                bd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button b = bd.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final int editing1 = edt_ing1;
                                final int editing2 = edt_ing2;
                                final int editing3 = edt_ing3;
                                final int editing4 = edt_ing4;
                                final int editing5 = edt_ing5;
                                final int editing6 = edt_ing6;
                                final int editing7 = edt_ing7;
                                givedb(editing1, editing2, editing3, editing4, editing5, editing6, editing7);
                                bd.dismiss();
                                getdb(dbList);
                                showIngredient(editing1, editing2, editing3, editing4, editing5, editing6, editing7);
                            }
                        });
                        Button c = bd.getButton(AlertDialog.BUTTON_NEGATIVE);
                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bd.cancel();
                            }
                        });
                    }
                });

                bd.show();
            }
        });


        recipebutton = (Button) view.findViewById(R.id.recipebutton);
        recipebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("ingredient", obj.toString());
                startActivity(intent);
            }
        });


        return view;
    }



    private void givedb(final int ing1, final int ing2, final int ing3, final int ing4, final int ing5, final int ing6, final int ing7) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/giveIngredient");
                        //URL url = new URL("http://10.0.2.2:3000/giveIngredient");
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        OutputStream out = conn.getOutputStream();
                        String upload_ing1 = "ing1=" + Integer.toString(ing1);
                        String upload_ing2 = "ing2=" + Integer.toString(ing2);
                        String upload_ing3 = "ing3=" + Integer.toString(ing3);
                        String upload_ing4 = "ing4=" + Integer.toString(ing4);
                        String upload_ing5 = "ing5=" + Integer.toString(ing5);
                        String upload_ing6 = "ing6=" + Integer.toString(ing6);
                        String upload_ing7 = "ing7=" + Integer.toString(ing7);

                        out.write(upload_ing1.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing2.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing3.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing4.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing5.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing6.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_ing7.getBytes());
                        Log.d("wait1", "wait");


                        InputStream in = conn.getInputStream();
                        Log.d("wait2", "wait");
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        Log.d("wait3", "wait");
                        byte[] buf = new byte[1080 * 1920];
                        int length = 0;
                        while ((length = in.read(buf)) != -1) {
                            out2.write(buf, 0, length);
                        }
                        System.out.println(new String(out2.toByteArray(), "UTF-8"));
                        Log.d("wait44", "wait");
                        out.close();
                        in.close();
                        Log.d("wait55", "wait");
                        conn.disconnect();
                        Log.d("wait66", "wait");
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
        synchronized (thread) {
            try {
                thread.wait();
                Log.d("wait", "wait");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void getdb(final JSONArray dbl) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/getIngredient");
                        //URL url = new URL("http://10.0.2.2:3000/getIngredient");
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
        synchronized (thread) {
            try {
                thread.wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void showIngredient(int meatnumber, int eggnumber, int potatonumber, int fishcakenumber, int kimchinumber, int onionnumber, int tofunumber) {
        if (meatnumber == 0) {
            meat.setVisibility(View.INVISIBLE);
        }
        else {
            meat.setVisibility(View.VISIBLE);
        }

        if (eggnumber == 0) {
            egg1.setVisibility(View.INVISIBLE);
            egg2.setVisibility(View.INVISIBLE);
            egg3.setVisibility(View.INVISIBLE);
            egg4.setVisibility(View.INVISIBLE);
        }
        else if (eggnumber == 1) {
            egg1.setVisibility(View.VISIBLE);
            egg2.setVisibility(View.INVISIBLE);
            egg3.setVisibility(View.INVISIBLE);
            egg4.setVisibility(View.INVISIBLE);
        }
        else if (eggnumber == 2) {
            egg1.setVisibility(View.VISIBLE);
            egg2.setVisibility(View.VISIBLE);
            egg3.setVisibility(View.INVISIBLE);
            egg4.setVisibility(View.INVISIBLE);
        }
        else if (eggnumber == 3) {
            egg1.setVisibility(View.VISIBLE);
            egg2.setVisibility(View.VISIBLE);
            egg3.setVisibility(View.VISIBLE);
            egg4.setVisibility(View.INVISIBLE);
        }
        else {
            egg1.setVisibility(View.VISIBLE);
            egg2.setVisibility(View.VISIBLE);
            egg3.setVisibility(View.VISIBLE);
            egg4.setVisibility(View.VISIBLE);
        }

        if (potatonumber == 0) {
            potato1.setVisibility(View.INVISIBLE);
            potato2.setVisibility(View.INVISIBLE);
            potato3.setVisibility(View.INVISIBLE);
        }
        else if (potatonumber == 1) {
            potato1.setVisibility(View.VISIBLE);
            potato2.setVisibility(View.INVISIBLE);
            potato3.setVisibility(View.INVISIBLE);
        }
        else if (potatonumber == 2) {
            potato1.setVisibility(View.VISIBLE);
            potato2.setVisibility(View.VISIBLE);
            potato3.setVisibility(View.INVISIBLE);
        }
        else {
            potato1.setVisibility(View.VISIBLE);
            potato2.setVisibility(View.VISIBLE);
            potato3.setVisibility(View.VISIBLE);
        }

        if (fishcakenumber == 0) {
            fishcake.setVisibility(View.INVISIBLE);
        }
        else {
            fishcake.setVisibility(View.VISIBLE);
        }

        if (kimchinumber == 0) {
            kimchi.setVisibility(View.INVISIBLE);
        }
        else {
            kimchi.setVisibility(View.VISIBLE);
        }

        if (onionnumber == 0) {
            onion1.setVisibility(View.INVISIBLE);
            onion2.setVisibility(View.INVISIBLE);
        }
        else if (onionnumber == 1) {
            onion1.setVisibility(View.VISIBLE);
            onion2.setVisibility(View.INVISIBLE);
        }
        else {
            onion1.setVisibility(View.VISIBLE);
            onion2.setVisibility(View.VISIBLE);
        }

        if (tofunumber == 0) {
            tofu.setVisibility(View.INVISIBLE);
        }
        else {
            tofu.setVisibility(View.VISIBLE);
        }
    }

}