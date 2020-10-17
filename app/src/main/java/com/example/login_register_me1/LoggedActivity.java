package com.example.login_register_me1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.InetAddress;
import java.net.UnknownHostException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LoggedActivity extends AppCompatActivity {
    private FrameLayout fragmentContainer;
    private SessionHandler session;
    private ImageView top_logo;
    BottomNavigationView bottomNavigationView;

    InetAddress inet_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage);
        //Window g = getWindow();
        //g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        //setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        //getWindow().setStatusBarColor(Color.TRANSPARENT);

        //Allow all threading policies (for some reason needed to ping the server)
        if(Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Window g = getWindow();
            g.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();

        fragmentContainer = findViewById(R.id.fragment_container);
        top_logo = findViewById(R.id.top_logo);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        Button logoutBtn = findViewById(R.id.buttonLogout);
        CardView To_do = findViewById(R.id.card_todo);
        bottomNavigationView = findViewById(R.id.bottomBar);
       // TextView welcomeText = findViewById(R.id.welcomeText);

        //welcomeText.setText("Welcome "+ user.getLastname()+",
        // your session will expire on "+user.getSessionExpiryDate());

        //Execute the server availability check method
        check_server();

        //Logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(LoggedActivity.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

        //To-do card onclick event
        To_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //session.logoutUser();
                Intent i = new Intent(LoggedActivity.this, TodoActivity.class);
                startActivity(i);
                finish();
            }
        });





        //pullToRefresh feature
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                check_server();
            }
        });

        //Animation of the logo when the activity loads
        Animation pop_top_logo = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        top_logo.startAnimation(pop_top_logo);

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
                    case R.id.navigationHome:
                        System.out.println("Home navigation item is clicked");
                        Toast.makeText(getApplicationContext(),"I, Home, am clicked",
                                Toast.LENGTH_SHORT).show();
                        //todo Start Home fragment(sub-activity)
                        item.setChecked(true);
                        break;
                    case R.id.navigationSearch:
                        //fragment = new HomeFragment();
                        openFragment();
                        System.out.println("Discover navigation item is clicked");
                        Toast.makeText(getApplicationContext(),"I, Discover, am clicked",
                                Toast.LENGTH_SHORT).show();
                        //todo Start Search fragment(sub-activity)
                        item.setChecked(true);
                        break;
                    case R.id.navigationMyProfile:
                        System.out.println("My Profile navigation item is clicked");
                        Toast.makeText(getApplicationContext(),"I, My profile, am clicked",
                                Toast.LENGTH_SHORT).show();
                        //todo Start Profile fragment(sub-activity)
                        item.setChecked(true);
                        break;
                    case R.id.navigationLogout:
                        //System.out.println("My Profile navigation item is clicked");
//                        Toast.makeText(getApplicationContext(),"I, My profile, am clicked",
//                                Toast.LENGTH_SHORT).show();
                        //todo Start Profile fragment(sub-activity)
                        session.logoutUser();
                        Intent i = new Intent(LoggedActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        item.setChecked(true);
                        break;
                }

                return true;
            }
        });
    }

    //Method to check if the server is available
    public void check_server() {
        try {
            inet_address = InetAddress.getLocalHost();

            String server_ip = inet_address.getHostAddress();
            String hostname = inet_address.getHostName();
            String server_down = getString(R.string.server_down);
            String server_up = getString(R.string.server_up);

            InetAddress inet = InetAddress.getByName(server_ip);

            TextView server_ping_text = findViewById(R.id.serverPing);

            System.out.println("Your current IP address is " + server_ip);
            System.out.println("Your Current Hostname is " + hostname);
            System.out.println("Sending ping request to " + server_ip);
            try {
                if (inet.isReachable(5000))
                {
                    server_ping_text.setText(server_up);
                } else {
                    server_ping_text.setText(server_down);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void openFragment(){
        HomeFragment fragment = HomeFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right,
                R.anim.slide_in_right, R.anim.slide_out_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, fragment, "HOME_FRAGMENT").commit();
    }


}
