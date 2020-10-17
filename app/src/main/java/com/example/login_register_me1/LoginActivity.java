package com.example.login_register_me1;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE  = "message";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;

    private String Username;
    //private String Email;
    private String Password;

    private ProgressDialog pDialog;
    private String Login_url = "http://192.168.1.22/HSalpha_Andro/db/login.php"; //Path to login script

    //CREATE A SESSIONHANDLER CLASS FIRST
    private SessionHandler session;

    LayoutInflater inflater;
    private TextView no_connection_text;
    private CardView toast_container;
    private int duration = Toast.LENGTH_SHORT;

    public void android1(View view) {
        Toast.makeText(this, "Button 1 Clicked", Toast.LENGTH_SHORT).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            //LOAD CURRENT USER ACTIVITIES
        }
        setContentView(R.layout.layout_login);

        etUsername = findViewById(R.id.editTextUname);
        etPassword = findViewById(R.id.editTextPassword);
        toast_container = findViewById(R.id.no_connection_toast_container);


        TextView register = findViewById(R.id.TextViewRegister);
        Button login = findViewById(R.id.buttonLogin);

        //Launch the Registration screen when the text is clicked
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


        //Launch the Login process
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                Username = etUsername.getText().toString().toLowerCase().trim();
                Password = etPassword.getText().toString().trim();

                if (validateInputs()) {
                    // * Check point 1* //
                    Log.d("DashEY", "The user is found");
                    login();
                }
            }
        });
    }

    public void display_no_conn_toast(){
        inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.no_connection_toast,toast_container);

        no_connection_text = layout.findViewById(R.id.text_no_connection);

        no_connection_text.setText(R.string.server_down);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    private void test(){

    }

    /**
     * Launch the User dashboard on Successful Login
     *
     */
    private void UserDashboard() {
        //CREATE ACTIVITIES FOR ALREADY REGISTERED USERS
        //Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
        Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Display progress bar while registering
     *
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging in... Please wait");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    /**
     * The main login process
     *
     */
    private void login() {
        displayLoader();
        Log.d("DashEY2", "The user is being processed");
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME,Username);
            request.put(KEY_PASSWORD,Password);
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.d("DashEY3", "Oops");
            System.err.println("We have an error mate: " + e);
            pDialog.dismiss();
        }
        Log.d("DashEY4", "Tried and not caught");
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, Login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if the user got logged in successfully

                            if (response.getInt(KEY_STATUS) == 0) {
                                session.LoginUser(Username, response.getString(KEY_FIRST_NAME));
                                Log.d("DashEY5", "The user is found");
                                UserDashboard();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //e.printStackTrace();
                            Log.d("DashEY6", "Olala");
                            System.err.println("We have an error mate: " + e);
                            pDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Display and error message whenever an error occurs
//                        Toast.makeText(getApplicationContext(),
//                                error.getMessage(), Toast.LENGTH_SHORT).show();
                        display_no_conn_toast();
                        pDialog.dismiss();
                    }
                });

        //Access the RequestQueue through my singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * This validates inputs and shows errors if any
     * @return
     */
    private boolean validateInputs(){
        if (KEY_EMPTY.equals(Username)) {
            etUsername.setError("Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(Password)) {
            etPassword.setError("The Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
