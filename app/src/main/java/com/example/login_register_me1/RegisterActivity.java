package com.example.login_register_me1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * Software created by Bruno Axel KAMERE on December 10th 2019 --Start date--
 *
 * !!!This class handles the registering process!!!
 *
 * In build gradle add Volley dependency "implementation 'com.android.volley:volley:1.1.0'"
 * Visit "https://developer.android.com/training/volley/request" for more info in JSON Volley
 *
 */
public class RegisterActivity extends AppCompatActivity {
    //Variables declaration
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE  = "message";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";

    private EditText etFirst_Name;
    private EditText etLast_Name;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private String FirstName;
    private String LastName;
    private String Username;
    private String Email;
    private String Password;
    private String ConfirmPassword;
    private ProgressDialog pDialog;
    private String Register_url = "http://192.168.1.22/HSalpha_Andro/db/register.php"; //Path to register script
    //CREATE A SESSIONHANDLER CLASS FIRST
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.layout_register);

        etUsername = findViewById(R.id.editTextUnameReg);
        etPassword = findViewById(R.id.editTextPasswordReg);
        etConfirmPassword = findViewById(R.id.editTextPasswordRep);
        etFirst_Name = findViewById(R.id.editTextFname);
        etLast_Name = findViewById(R.id.editTextLname);
        etEmail = findViewById(R.id.editTextEmail);

        final Button register = findViewById(R.id.buttonregister);
        TextView login = findViewById(R.id.TextViewLogin);

        //Launch the login screen when the text is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve data entered in edit texts areas
                FirstName = etFirst_Name.getText().toString().trim();
                LastName = etLast_Name.getText().toString().trim();
                Username = etUsername.getText().toString().toLowerCase().trim();
                Email = etEmail.getText().toString().trim();
                Password = etPassword.getText().toString().trim();
                ConfirmPassword = etConfirmPassword.getText().toString().trim();

                //Create validateInputs and registerUser classes FIRST
                if (validateInputs()) {
                    registerUser();
                }
            }
        });
    }

    /**
     * Display progress bar while registering
     *
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Please wait while we sign you up...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Launch Welcome(New User sign up page) Activity
     *
     */
    private void WelcomeDashboard() {
        //CREATE WELCOME ACTIVITIES
    }

    private void UserDashboard() {
        //CREATE ACTIVITIES FOR ALREADY REGISTERED USERS
        Intent i = new Intent(getApplicationContext(), LoggedActivity.class);
        startActivity(i);
        finish();
    }


    /**
     * Registering process...
     *
     */
    private void registerUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put(KEY_USERNAME, Username);
            request.put(KEY_PASSWORD, Password);
            request.put(KEY_LAST_NAME, LastName);
            request.put(KEY_FIRST_NAME, FirstName);
            request.put(KEY_EMAIL, Email);
            Log.d("REGISTER_TRACK1", "Request parameters populated");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("REGISTER_TRACK2", "Unable to populate parameters");
        }
        Log.d("REGISTER_TRACK3", "Byemeye, check the following code");
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, Register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("REGISTER_TRACK4", "Checkpoint ola");
                        pDialog.dismiss();
                        try {
                            //Check if the user got registered successfully
                            if (response.getInt(KEY_STATUS) == 0) {
                                //Set the user session
                                session.LoginUser(Username, response.getString(KEY_FIRST_NAME));
                                UserDashboard();
                            } else if (response.getInt(KEY_STATUS) == 1) {
                                //Display an error message if the username already exists
                                etUsername.setError("Username already taken");
                                etUsername.requestFocus();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("REGISTER_TRACK5", "Gotcha");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        error.printStackTrace();
                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("REGISTER_TRACK6", "Gotchafdfd");
                    }
                });

        //Access the requestQueue through my singleton class
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    /**
     * This below validates the inputs and show errors if any
     * show errors if any of the inputs is blank
     * @return
     */
    private boolean validateInputs() {
        if (KEY_EMPTY.equals(FirstName)) {
            etFirst_Name.setError("The First Name cannot be empty");
            etFirst_Name.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(LastName)) {
            etLast_Name.setError("The Last Name cannot be empty");
            etLast_Name.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(Email)) {
            etEmail.setError("The Email cannot be empty");
            etEmail.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(Username)) {
            etUsername.setError("The Username cannot be empty");
            etUsername.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(Password)) {
            etPassword.setError("The Password cannot be empty");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(ConfirmPassword)) {
            etConfirmPassword.setError("The Confirm password cannot be empty");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!Password.equals(ConfirmPassword)) {
            etConfirmPassword.setError("Password and Confirm password does not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}
