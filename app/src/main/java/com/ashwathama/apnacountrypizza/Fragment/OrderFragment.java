package com.ashwathama.apnacountrypizza.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.Blogs.OrderBlog;
import com.ashwathama.apnacountrypizza.Blogs.cartblog;
import com.ashwathama.apnacountrypizza.FinalSelectFrag;
import com.ashwathama.apnacountrypizza.R;
import com.ashwathama.apnacountrypizza.SessionManager;
import com.ashwathama.apnacountrypizza.ViewDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class OrderFragment extends Fragment {


    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        final String phone = userDetails.get(SessionManager.KEY_PHONE);

        final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Orders").child("temperoryorders");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.orderrv);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));


    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrderBlog, OfragBlogViewHolder> cartfirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<OrderBlog, OfragBlogViewHolder>
                (OrderBlog.class, R.layout.orderscroll, OfragBlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(OfragBlogViewHolder m1viewHolder, OrderBlog m1model, int i) {

                m1viewHolder.setDateandTime(m1model.getDateandTime());

                m1viewHolder.setDeliveryAddress(m1model.getDeliveryAddress());
                m1viewHolder.setModeOfPayment(m1model.getModeOfPayment());

                m1viewHolder.setOid(m1model.getOid());

                m1viewHolder.setAmounttopaid(m1model.getAmounttopaid());



            }


        };
        mBlogList.setAdapter(cartfirebaseRecyclerAdapter);

    }




    public static  class OfragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        String z;

        public OfragBlogViewHolder(final View itemView){
            super(itemView);
            mView=itemView;



            SessionManager sessionManager = new SessionManager(mView.getContext());
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            final String phone = userDetails.get(SessionManager.KEY_PHONE);

            final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


            Button b=(Button)mView.findViewById(R.id.viewDetails);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                     final String MY="My";
                    SharedPreferences.Editor editor=mView.getContext().getSharedPreferences(MY,Context.MODE_PRIVATE).edit();

                    editor.putString("Oid",z);
                    editor.apply();




                    FragmentActivity activity=(FragmentActivity)(mView.getContext());
                    FragmentManager fm = activity.getSupportFragmentManager();
                    ViewDetails alertDialog=new ViewDetails();
                    alertDialog.show(fm,"fragment_alert");










                }
            });



        }


        public void setOid(String oid){
            z=oid;
        }
        public void setDateandTime(String dateandTime)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.dat);
            post_title.setText(dateandTime);

        }


        public void setDeliveryAddress(String deliveryAddress)
        {

            TextView post_title=(TextView)mView.findViewById(R.id.Da);
            post_title.setText(deliveryAddress);

        }




        public void setModeOfPayment(String modeOfPayment)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.ts);
            post_priceSmall.setText((modeOfPayment));
        }



        public void setAmounttopaid(int amounttopaid)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.Tao);
            post_priceSmall.setText((String.valueOf(amounttopaid)));
        }

        public void setOrderStatus(String orderStatus)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.Os);
            post_priceSmall.setText((orderStatus));
        }
        public void setAmountStatus(String amountStatus)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.AS);
            post_priceSmall.setText((amountStatus));
        }
    }
}
