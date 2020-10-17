package com.example.login_register_me1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.login_register_me1.Model.Data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TodoActivity extends AppCompatActivity {

    private static final String KEY_EMPTY = "";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TYPE = "item";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_AMOUNT = "cost";
    private static final String KEY_NOTE = "note";
    private static final String KEY_USERNAME = "username";
    private ProgressDialog pDialog;
    private FloatingActionButton fab_btn;
    private String add_item_url = "http://192.168.1.22/HSalpha_Andro/db/add_item_to_db.php";
    private String retrieve_items_url = "http://192.168.1.22/HSalpha_Andro/db/retrieve_from_db.php";
    private String remove_item_url = "";
    private String update_item_url = "";
    private String mType;
    private String mAmount;
    private String mNote;
    private String username;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    //private RecyclerView.LayoutManager recyclerViewLayoutManager;

    List<items> itemName;
    ArrayList<Data> arrayList;
    private TextView toastText;
    LayoutInflater inflater;
    private CardView toast_container;
    private int duration = Toast.LENGTH_SHORT;
    ArrayList<String> allItems;

    BottomNavigationView bottomNavigationView;

    private SessionHandler session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        setContentView(R.layout.layout_todolist);

        //Allow all threading policies (for some reason needed to ping the server)
        if(Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Window g = getWindow();
            g.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        fab_btn = findViewById(R.id.fab);
        username = user.getUsername();
        toast_container = findViewById(R.id.no_connection_toast_container);
        bottomNavigationView = findViewById(R.id.bottomBar_todo);
        recyclerView = findViewById(R.id.recycler_home);
        arrayList = new ArrayList<Data>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        JsonFetch jsonFetch = new JsonFetch();
        jsonFetch.execute();

        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //customDialog();
                AlertDialog.Builder mydialog = new AlertDialog.Builder(TodoActivity.this);

                LayoutInflater inflater = LayoutInflater.from(TodoActivity.this);
                View myview = inflater.inflate(R.layout.add_item, null);
                final AlertDialog dialog = mydialog.create();
                dialog.setView(myview);

                final EditText type = myview.findViewById(R.id.editTextType);
                final EditText amount = myview.findViewById(R.id.editTextAmount);
                final EditText note = myview.findViewById(R.id.editTextNote);
                Button btnsave = myview.findViewById(R.id.button_save);

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mType = type.getText().toString().trim();
                        mAmount = amount.getText().toString().trim();
                        mNote = note.getText().toString().trim();

                        if (TextUtils.isEmpty(mType)){
                            type.setError("Required Field...");
                            return;
                        }
                        if (TextUtils.isEmpty(mAmount)){
                            amount.setError("Required Field...");
                            return;
                        }
                        if (TextUtils.isEmpty(mNote)){
                            note.setError("Required Field...");
                            return;
                        }
                        addItemtoDatabase();
                        display_item_added_confirmation();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        //Bottom navigation select listener
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    //Method to check which navigation item is clicked and show its contents
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        Fragment fragment = null;
                        Bundle bundle = new Bundle();
                        switch (id)
                        {
                            case R.id.back_to_main:
                                System.out.println("Home navigation item is clicked");
                                //Toast.makeText(getApplicationContext(),"I, Home, am clicked",
                                        //Toast.LENGTH_SHORT).show();
                                //todo Start Home fragment(sub-activity)
                                Intent i = new Intent(TodoActivity.this, LoggedActivity.class);
                                startActivity(i);
                                finish();
                                item.setChecked(true);
                                break;
                        }

                        return true;
                    }
                });
    }





    public class JsonFetch extends AsyncTask<String,String,String>{
        HttpURLConnection httpURLConnection = null;
        String mainfile;
        BufferedReader bufferedReader = null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(retrieve_items_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }
                mainfile = stringBuffer.toString();
                System.out.println(mainfile);

                //todo HOW TO CHANGE OBJECT TO ARRAY
                JSONArray parent = new JSONArray(mainfile);
                int i = 0;

                while(i <= parent.length()){
                    JSONObject child = parent.getJSONObject(i);

                    String user_id = child.getString("0");
                    String item_id = child.getString("1");
                    String item_name = child.getString("2");
                    String item_description = child.getString("3");
                    String added_date = child.getString("4");
                    String item_cost = child.getString("5");

                    arrayList.add(new Data(item_name,item_description,added_date,item_id,item_cost));
                    i++;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            RecyclerViewCardViewAdapter recyclerViewCardViewAdapter = new RecyclerViewCardViewAdapter(arrayList,getApplicationContext());
            recyclerView.setAdapter(recyclerViewCardViewAdapter);
        }
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(TodoActivity.this);
        pDialog.setMessage("Adding item to the list...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void display_item_added_confirmation(){
        inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.no_connection_toast,toast_container);

        toastText = layout.findViewById(R.id.text_no_connection);

        toastText.setText(R.string.item_added);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    private void displayLoader_remove() {
        pDialog = new ProgressDialog(TodoActivity.this);
        pDialog.setMessage("Removing item from the list...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /*
    public void customDialog(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(TodoActivity.this);

        LayoutInflater inflater = LayoutInflater.from(TodoActivity.this);
        View myview = inflater.inflate(R.layout.add_item, null);
        final AlertDialog dialog = mydialog.create();
        dialog.setView(myview);

        final EditText type = myview.findViewById(R.id.editTextType);
        final EditText amount = myview.findViewById(R.id.editTextAmount);
        final EditText note = myview.findViewById(R.id.editTextNote);
        Button btnsave = myview.findViewById(R.id.button_save);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                if (TextUtils.isEmpty(mType)){
                    type.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mAmount)){
                    amount.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("Required Field...");
                    return;
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    */
    private void addItemtoDatabase(){
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //populate request parameters
            request.put(KEY_TYPE, mType);
            request.put(KEY_AMOUNT, mAmount);
            request.put(KEY_NOTE, mNote);
            request.put(KEY_USERNAME, username);
        } catch (JSONException e){
            e.printStackTrace();
            Log.d("Track_process1", "Unable to populate parameters");
        }
        Log.d("Track_process2", "Parameters populated");
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, add_item_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Track_process3", "Checkpoint 3");
                pDialog.dismiss();
                try {
                    //Check if the item was added successfully
                    if (response.getInt(KEY_STATUS) == 0) {
                        //Return  to the page with the item added
                    } else if (response.getInt(KEY_STATUS) == 1) {
                        pDialog.dismiss();
                        //What should be done here?
                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Track_process4", "Got you!!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                error.printStackTrace();
                //Display error message whenever an error occurs
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Track_process5", "An error boiyy");
            }
        });
        //Access the requestQueue through my singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    /*
    private void removeItemfromDatabase(){
        displayLoader_remove();
        try {
            URL remove_item = new URL(remove_item_url);
            URLConnection rc = remove_item.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(rc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();
        } catch (Exception e){
            e.printStackTrace();
            Log.d("Track_process6", "An error boiyy");
        }


    }
    */
}
