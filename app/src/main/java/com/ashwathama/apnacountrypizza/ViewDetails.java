package com.ashwathama.apnacountrypizza;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwathama.apnacountrypizza.Blogs.OrderBlog;
import com.ashwathama.apnacountrypizza.Blogs.cartblog;
import com.ashwathama.apnacountrypizza.Blogs.viewdetailsblog;
import com.ashwathama.apnacountrypizza.Fragment.CartFragment;
import com.ashwathama.apnacountrypizza.Fragment.OrderFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Random;

public class ViewDetails extends DialogFragment {


    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_details, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_DeviceDefault_Dialog_Alert);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        final String phone = userDetails.get(SessionManager.KEY_PHONE);




        SharedPreferences prefs=getContext().getSharedPreferences("My",Context.MODE_PRIVATE);

        String Oid=prefs.getString("Oid", "null");

        Toast.makeText(getActivity(), Oid, Toast.LENGTH_SHORT).show();



        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Orders").child("temperoryorders").child(Oid).child("Items");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.recyclerviewdetails);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));


    }


    @Override
    public void onStart() {
        super.onStart();





        FirebaseRecyclerAdapter<viewdetailsblog, BlogViewHolder> cartfirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<viewdetailsblog, BlogViewHolder>
                (viewdetailsblog.class, R.layout.viewdetailscroll, BlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder m1viewHolder, viewdetailsblog m1model, int i) {


                m1viewHolder.setName(m1model.getName());
                m1viewHolder.setMQuantity(m1model.getMQuantity());
                m1viewHolder.setSQuantity(m1model.getSQuantity());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());

                m1viewHolder.setPriceSmall(m1model.getPriceSmall());

            }


        };


        mBlogList.setAdapter(cartfirebaseRecyclerAdapter);

    }



    public static  class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        String z;

        public BlogViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

        }


        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.vdname);
            post_title.setText(name);

        }

        public void setPriceSmall(int priceSmall) {

            TextView post_title=(TextView)mView.findViewById(R.id.vds);
            post_title.setText(String.valueOf(priceSmall));
        }
        public void setPriceMedium(int priceMedium) {


            TextView post_title=(TextView)mView.findViewById(R.id.vdm);
            post_title.setText(String.valueOf(priceMedium));

        }

        public void setMQuantity(int MQuantity) {

            TextView post_title=(TextView)mView.findViewById(R.id.qsm);
            post_title.setText(String.valueOf(MQuantity));
        }


        public void setSQuantity(int SQuantity) {

            TextView post_title=(TextView)mView.findViewById(R.id.qss);
            post_title.setText(String.valueOf(SQuantity));
        }






    }





        }