package com.example.login_register_me1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HomeFragment extends Fragment {
   // private OnFragmentInteractionListener mListener;
    private SessionHandler session;
    private ImageView top_logo;

    InetAddress inet_address;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        // Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        //getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);

        if(Build.VERSION.SDK_INT >9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }

        session = new SessionHandler(getActivity().getApplicationContext());
        User user = session.getUserDetails();

        top_logo = view.findViewById(R.id.top_logo);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        Button logoutBtn = view.findViewById(R.id.buttonLogout);
        CardView To_do = view.findViewById(R.id.card_todo);

        //check_server();

//        logoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                session.logoutUser();
//                Intent i = new Intent(getActivity(), LoginActivity.class);
//                startActivity(i);
//                getActivity().finish();
//
//            }
//        });

        //To-do card onclick event
        To_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
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
        Animation pop_top_logo = AnimationUtils.loadAnimation(getActivity(),R.anim.splashanimation);
        top_logo.startAnimation(pop_top_logo);



        return view;
    }

    //Method to check if the server is available
    public void check_server(){
        try {
            inet_address = InetAddress.getLocalHost();

            String server_ip = inet_address.getHostAddress();
            String hostname = inet_address.getHostName();
            String server_down = getString(R.string.server_down);
            String server_up = getString(R.string.server_up);

            InetAddress inet = InetAddress.getByName(server_ip);

            TextView server_ping_text = getView().findViewById(R.id.serverPing);

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
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
