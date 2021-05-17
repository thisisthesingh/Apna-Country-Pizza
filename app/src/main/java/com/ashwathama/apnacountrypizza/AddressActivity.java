package com.ashwathama.apnacountrypizza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashwathama.apnacountrypizza.Blogs.AddressBlog;
import com.ashwathama.apnacountrypizza.Fragment.ProfileFragment;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu1Frag;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    public RecyclerView myAddresses;


    private RecyclerView aBlogList;
    private DatabaseReference aDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        myAddresses = findViewById(R.id.addresses_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddresses.setLayoutManager(linearLayoutManager);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);




        aDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Addresses");
        aDatabase.keepSynced(true);
        aBlogList=(RecyclerView)this.findViewById(R.id.addresses_recyclerview);
        aBlogList.setHasFixedSize(true);
        aBlogList.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressBlog, zAddressViewHolder> afirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AddressBlog, zAddressViewHolder>
                (AddressBlog.class, R.layout.addressascroll,zAddressViewHolder.class,aDatabase) {


            @Override
            protected void populateViewHolder(zAddressViewHolder addressViewHolder, AddressBlog addressBlog, int i) {
                addressViewHolder.setAddressv(addressBlog.getAddressv());
                addressViewHolder.setIdv(addressBlog.getIdv());
            }



        };
        aBlogList.setAdapter(afirebaseRecyclerAdapter);


    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddressActivity.this, UserDashboard.class));
        finish();
    }

    public void callDashboardAgain(View view){
        startActivity(new Intent(AddressActivity.this, UserDashboard.class));
        finish();
    }



    public static  class zAddressViewHolder extends RecyclerView.ViewHolder
    {

        View mView;


        String tid;

        public zAddressViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;



        }



        public void setAddressv(String addressv){
            TextView addressview=(TextView)mView.findViewById(R.id.zaddressholdertv);
            addressview.setText(addressv);
        }

        public void setIdv(String idv){

            tid=idv;

        }
    }


}