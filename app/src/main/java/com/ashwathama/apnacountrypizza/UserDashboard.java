package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwathama.apnacountrypizza.Fragment.CartFragment;
import com.ashwathama.apnacountrypizza.Fragment.FavoriteFragment;
import com.ashwathama.apnacountrypizza.Fragment.HomeFragment;
import com.ashwathama.apnacountrypizza.Fragment.MenuFragment;
import com.ashwathama.apnacountrypizza.Fragment.OrderFragment;
import com.ashwathama.apnacountrypizza.Fragment.ProfileFragment;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu1Frag;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu2Frag;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.razorpay.PaymentResultListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PaymentResultListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    ImageView likebtn;

    TextView logout;
    private int TO;
    TextView username;

    ImageView menuIcon;

    int backpress = 0;
    Fragment selectedFragment = null;
//    TextView username;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_dash);
        likebtn=findViewById(R.id.like_btn);
        menuIcon = findViewById(R.id.ham_icon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedlistener);

        logout = findViewById(R.id.dj_logout);

        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){

                    case R.id.dj_home:
                        selectedFragment = new HomeFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.dj_fav:
                        selectedFragment = new Fragment_favorite();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.dj_myorders:
                        selectedFragment = new MenuFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.dj_profile:
                        selectedFragment = new ProfileFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.dj_logout:
                        SessionManager sessionManager = new SessionManager(UserDashboard.this);
                        sessionManager.logoutUserFromSessiom();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(UserDashboard.this, LoginActivity.class));
                        finish();
                        break;

                    case R.id.dj_support:
                        startActivity(new Intent(UserDashboard.this, Developers.class));

                }

                if(selectedFragment!=null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                }

                return true;
            }
        });

//        logoutUser();

//        //Show User Name on the Navigation Menu
        View header = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem inout = menu.findItem(R.id.dj_logout);
        username = (TextView) header.findViewById(R.id.username);
//        SessionManager sessionManager = new SessionManager(this);
//        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
//
//
//        String name = userDetails.get(SessionManager.KEY_FULLNAME);

        SessionManager sessionManager = new SessionManager(UserDashboard.this.getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);
        String fullName = userDetails.get(SessionManager.KEY_FULLNAME);

        if(fullName != null){

            final DatabaseReference cartmDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


            cartmDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    username.setText(  snapshot.child("fullName").getValue(String.class));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{

            username.setText("User");
            inout.setTitle("Login");

        }


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new Fragment_favorite();
                FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,new Fragment_favorite(), "DetailedPizza");
                fragmentTransaction.addToBackStack("DetailedPizza");
                fragmentTransaction.commit();

            }
        });

    }


    public void openOrderFrag(){

    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.dj_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {

        Fragment home = getSupportFragmentManager().findFragmentByTag("DetailedPizza");

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        if (home instanceof Menu1Frag || home instanceof Menu2Frag || home instanceof  MenuFragment|| home instanceof  OrderFragment || home instanceof  CartFragment && home.isVisible()) {
            if (getFragmentManager().getBackStackEntryCount() != 0) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        } else {
            //Primary fragment
            moveTaskToBack(true);
        }


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedlistener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                    switch (menuitem.getItemId()) {

                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();

                            break;

                        case R.id.nav_menu:

                            selectedFragment = new CartFragment();
                            break;

                        case R.id.nav_favorite:
                            selectedFragment = new OrderFragment();
                            break;

                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }


                    if(selectedFragment!=null)
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    }

                    return true;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


    @Override
    public void onPaymentSuccess(String s) {


        try {

            Toast.makeText(getApplication(),"sucess",Toast.LENGTH_LONG).show();


            Calendar calendar;
            SimpleDateFormat simpledateformat;
            final String Date;


            calendar = Calendar.getInstance();
            simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date = simpledateformat.format(calendar.getTime());

            Toast.makeText(getApplicationContext(), Date, Toast.LENGTH_SHORT).show();

            SessionManager sessionManager = new SessionManager(getApplicationContext());
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            final String phone = userDetails.get(SessionManager.KEY_PHONE);


            final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


            utp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    String m=snapshot.child("tad").getValue(String.class);


                    int o=snapshot.child("finalam").getValue(Integer.class);

                    if(!snapshot.hasChild("TotalOrders")){
                        utp.child("TotalOrders").setValue(1);



                        TO=1;
                        int c=TO+1;
                        utp.child("TotalOrders").setValue(c);

                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("DateandTime").setValue(String.valueOf(Date));
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("PhoneNumber").setValue(phone);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("DeliveryAddress").setValue(m);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("ModeOfPayment").setValue(snapshot.child("temporder").child("paymentmode").getValue());
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Amounttopaid").setValue(o);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("OrderStatus").setValue("Waiting For Confirmation");
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("AmountStatus").setValue("Paid");
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Oid").setValue(String.valueOf(TO));

                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Items").setValue(  snapshot.child("cart").getValue());




                    }
                    else{
                        TO=snapshot.child("TotalOrders").getValue(Integer.class);

                        int c=TO+1;
                        utp.child("TotalOrders").setValue(c);

                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("DateandTime").setValue(String.valueOf(Date));
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("PhoneNumber").setValue(phone);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("DeliveryAddress").setValue(m);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("ModeOfPayment").setValue(snapshot.child("temporder").child("paymentmode").getValue());
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Amounttopaid").setValue(o);
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("OrderStatus").setValue("Waiting For Confirmation");

                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("AmountStatus").setValue("Paid");
                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Oid").setValue(String.valueOf(TO));

                        utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Items").setValue(  snapshot.child("cart").getValue());








                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });




            Toast.makeText(getApplicationContext(), "YOUR ORDER HAS BEEN PLACED", Toast.LENGTH_SHORT).show();

            utp.child("cart").setValue(null);


            utp.child("finalam").setValue(0);

            utp.child("cart").setValue(null);


            utp.child("finalam").setValue(0);


            Fragment prev = getSupportFragmentManager().findFragmentByTag("My Fragment");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }




//            Fragment someFragment = new OrderFragment();
//            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, OrderFragment, "DetailedPizza");
//            transaction.addToBackStack("DetailedPizza");
//            transaction.commit();





        }catch (Exception e){
            Log.e("TAG", "Exception in onPaymentSuccess", e);

        }

    }


    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getApplicationContext(), "ERROR IN PAYMENT : Please Check the status of Payment", Toast.LENGTH_SHORT).show();

        try {


            Fragment prev = getSupportFragmentManager().findFragmentByTag("My Fragment");
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }

            Toast.makeText(getApplicationContext(), "Payment failed: "+i+""+s,Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }

    }

}