package com.example.zebal;

import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class TabFragment1 extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<DataList> arrayList;
    private DataAdapter dataAdapter;
    Button newnumber;
    Button getfromdb;
    private JSONArray dbList;
    JSONArray obj;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.Recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<DataList>();
        obj = new JSONArray();

        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String profileImage = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {
                    Cursor cursor2 = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (cursor2.moveToNext()) {
                        String number = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (profileImage == null){
                            profileImage = "basic";
                        }
                        arrayList.add(new DataList(id, name, number, profileImage));
                    }
                    cursor2.close();
                }
            }
        }
        cursor.close();
        dataAdapter = new DataAdapter(arrayList, getContext());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String number = arrayList.get(position).number;
                makePhoneCall(number);
            }
            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Contact?").setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getContext().getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID+" =?", new String[]{String.valueOf(arrayList.get(position).id)});
                                //   String call_number = arrayList.get(deletePosition).number;
                                arrayList.remove(position);
                                dataAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        }));
        recyclerView.setAdapter(dataAdapter);


        newnumber = (Button) view.findViewById(R.id.button_main_insert);
        newnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View insertview = LayoutInflater.from(getContext()).inflate(R.layout.add_number, null, false);
                builder.setView(insertview);
                builder.setTitle("Insert Contacts").setMessage("Fill in the Blanks to Add Contacts.");

                final Button ButtonSubmit = (Button) insertview.findViewById(R.id.button_dialog_submit);
                final EditText editTextname = (EditText) insertview.findViewById(R.id.edittext_dialog_name);
                final EditText editTextmemo = (EditText) insertview.findViewById(R.id.edittext_dialog_memo);
                final EditText editTextnumber = (EditText) insertview.findViewById(R.id.edittext_dialog_number);

                ButtonSubmit.setText("Insert");
                final AlertDialog dialog = builder.create();


                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editTextnumber.length() != 11) {
                            Toast.makeText(getContext(), "invalid number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String Strname = editTextname.getText().toString();
                        String Strmemo = editTextmemo.getText().toString();
                        String Strnumber = editTextnumber.getText().toString();
                        Strnumber = Strnumber.substring(0, 3) + "-" + Strnumber.substring(3, 7) + "-" + Strnumber.substring(7, 11);

                        DataList dtl = new DataList(Strmemo, Strname, Strnumber, "basic"); // 1st parameter is id
                        arrayList.add(dtl);
                        dataAdapter.notifyDataSetChanged();

                        addtodb(Strname, Strmemo, Strnumber);



                        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

                        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());
                        if(Strname != null && !Strname.trim().equals(""))
                        {
                            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, Strname)
                                    .build());
                        }
                        if(Strnumber != null && !Strnumber.trim().equals(""))
                        {
                            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, Strnumber)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build());
                        }
                        if (ops.size() > 0) {
                            try
                            {
                                getContext().getApplicationContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                            } catch (RemoteException e) {
                                Log.e(TAG, String.format("%s: %s", e.toString(), e.getMessage()));
                            } catch (OperationApplicationException e) {
                                Log.e(TAG, String.format("%s: %s", e.toString(), e.getMessage()));
                            }
                        }





                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        getfromdb = (Button) view.findViewById(R.id.get_from_db);
        dbList = new JSONArray();
        getfromdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdb(dbList);
                Log.d("objcomeout", obj.toString());
                for (int i = 0; i < obj.length(); i++) {
                    try {
                        JSONObject objitem = obj.getJSONObject(i);
                        String objname = objitem.getString("name");
                        String objmemo = objitem.getString("memo");
                        String objnumber = objitem.getString("number");
                        String objphoto = "basic";
                        DataList objdtl = new DataList(objmemo, objname, objnumber, objphoto);
                        arrayList.add(objdtl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                dataAdapter.notifyDataSetChanged();
            }
        });




        return view;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            return;
        }

    };


    private void getdb(final JSONArray dbl) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/getnum");
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


    private void addtodb(final String name, final String memo, final String number) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        URL url = new URL("http://143.248.36.32:3000/givenum");
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        OutputStream out = conn.getOutputStream();
                        String upload_name = "name=" + name;
                        String upload_memo = "memo=" + memo;
                        String upload_number = "number=" + number;
                        out.write(upload_name.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_memo.getBytes());
                        out.write("&".getBytes());
                        out.write(upload_number.getBytes());

                        InputStream in = conn.getInputStream();
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        byte[] buf = new byte[1080 * 1920];
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
                    notify();
                }
            }
        };
        thread.start();
        synchronized (thread) {
            try {
                thread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void makePhoneCall(String number){
        if ((number.trim().length()) > 0) {
            number = number.replace("-","");
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
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

}