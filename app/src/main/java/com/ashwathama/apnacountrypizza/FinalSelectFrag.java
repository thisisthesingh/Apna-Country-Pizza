package com.ashwathama.apnacountrypizza;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashwathama.apnacountrypizza.Blogs.cartblog;
import com.ashwathama.apnacountrypizza.Fragment.CartFragment;
import com.ashwathama.apnacountrypizza.Fragment.OrderFragment;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu2Frag;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class FinalSelectFrag extends DialogFragment implements PaymentResultListener {


    int o;
    String m;
    private int TO;
    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;

    Button b1,b2;
    TextView ffad,fffam,ffpaym;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_final_select, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        final String phone = userDetails.get(SessionManager.KEY_PHONE);

        final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("cart");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.ffrv1);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

    ffad=(TextView)getView().findViewById(R.id.ffaddress);
    ffpaym=(TextView)getView().findViewById(R.id.ffpaymode);
    fffam=(TextView)getView().findViewById(R.id.fffam);

        final String[] n = new String[1];

    utp.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

             m=snapshot.child("tad").getValue(String.class);
            ffad.setText(m);

            n[0] =snapshot.child("temporder").child("paymentmode").getValue(String.class);
            ffpaym.setText(n[0]);

             o=snapshot.child("finalam").getValue(Integer.class);
            fffam.setText(String.valueOf(o));

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });




    b1=(Button)getView().findViewById(R.id.ffproceed);

    b2=(Button)getView().findViewById(R.id.ffcancel);





    b2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    });


    b1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



            if(n[0].equals("Cash On Delivery")){
                OrderPlacement(m,n,o);
            }

            if(n[0].equals("Online Payment")){



                final String[] orderamount = new String[1];

                SessionManager sessionManager = new SessionManager(getContext());
                HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
                String phone = userDetails.get(SessionManager.KEY_PHONE);


               DatabaseReference aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("finalam");
                aDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderamount[0] = String.valueOf((snapshot.getValue(Integer.class))*100);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                setUpPayment(orderamount[0]);




            }

        }
    });







    }

    private void setUpPayment(String s) {


        final Checkout checkout = new Checkout();

        // checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity =getActivity();




        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);


        final int[] k = new int[1];
        DatabaseReference aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("finalam");
        aDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                k[0] =snapshot.getValue(Integer.class);
                Toast.makeText(getContext(), String.valueOf(snapshot.getValue(Integer.class)), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject options = new JSONObject();

                    options.put("name", "Apna Country Pizza");
                    options.put("description", "powered by ATLABS");
                    options.put("image", "https://firebasestorage.googleapis.com/v0/b/apna-country-pizza-46418.appspot.com/o/iconn.png?alt=media&token=151b6ca1-c5e1-4ac2-bc63-b3a74cf0d149");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
                    options.put("theme.color", "#3399cc");
                    options.put("currency", "INR");
                    options.put("amount",(snapshot.getValue(Integer.class)*100));//pass amount in currency subunits
//            options.put("prefill.email", "");
//            options.put("prefill.contact","917906913683");
                    checkout.open(activity, options);
                } catch(Exception e) {
                    Log.e("TAG", "Error in starting Razorpay Checkout", e);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */




    }

    @Override
    public void onPaymentSuccess(String s) {


        Calendar calendar;
        SimpleDateFormat simpledateformat;
        final String Date;


        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());

        Toast.makeText(getActivity().getApplicationContext(), Date, Toast.LENGTH_SHORT).show();

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        final String phone = userDetails.get(SessionManager.KEY_PHONE);

        final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        utp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("AmountStatus").setValue("UnPaid");

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


        Toast.makeText(getContext(), "YOUR ORDER HAS BEEN PLACED", Toast.LENGTH_SHORT).show();

        utp.child("cart").setValue(null);


        utp.child("finalam").setValue(0);



        getDialog().dismiss();


        Fragment someFragment = new OrderFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
        transaction.addToBackStack("DetailedPizza");
        transaction.commit();




    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getContext(), "ERROR IN PAYMENT : Please Check the status of Payment", Toast.LENGTH_SHORT).show();
    }

    private void OrderPlacement(final String m, final String[] n, final int o) {


        Calendar calendar;
        SimpleDateFormat simpledateformat;
        final String Date;


        calendar = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpledateformat.format(calendar.getTime());

        Toast.makeText(getActivity().getApplicationContext(), Date, Toast.LENGTH_SHORT).show();

        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        final String phone = userDetails.get(SessionManager.KEY_PHONE);

        final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        utp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("AmountStatus").setValue("UnPaid");


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

                    utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("AmountStatus").setValue("UnPaid");

                    utp.child("Orders").child("temperoryorders").child(String.valueOf(TO)).child("Items").setValue(  snapshot.child("cart").getValue());








                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Toast.makeText(getContext(), "YOUR ORDER HAS BEEN PLACED", Toast.LENGTH_SHORT).show();

        utp.child("cart").setValue(null);


        utp.child("finalam").setValue(0);

        getDialog().dismiss();


        Fragment someFragment = new OrderFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
        transaction.addToBackStack("DetailedPizza");
        transaction.commit();



    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<cartblog, fcartfragBlogViewHolder> cartfirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<cartblog, fcartfragBlogViewHolder>
                (cartblog.class, R.layout.ffscroll, fcartfragBlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(fcartfragBlogViewHolder m1viewHolder, cartblog m1model, int i) {

                m1viewHolder.setName(m1model.getName());

                m1viewHolder.setImage(getActivity().getApplicationContext(),m1model.getImage());
                m1viewHolder.setPriceSmall(m1model.getPriceSmall());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());
                m1viewHolder.setId(m1model.getId());
                m1viewHolder.setSQuantity(m1model.getSQuantity());
                m1viewHolder.setMQuantity(m1model.getMQuantity());


            }


        };
        mBlogList.setAdapter(cartfirebaseRecyclerAdapter);

    }




    public static  class fcartfragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        int ssprice;
        int smprice;
        String cartid;
        int ssamount,mmamount,ttamount;
        int sQ,mQ;
        String nsame;
        String MQ,SQ;


        public fcartfragBlogViewHolder(final View itemView){
            super(itemView);
            mView=itemView;

            SessionManager sessionManager = new SessionManager(mView.getContext());
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            final String phone = userDetails.get(SessionManager.KEY_PHONE);

            final DatabaseReference utp= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

//
//            utp.child("tempitems").child("name").setValue(nsame);
//            utp.child("tempitems").child("SmallQ").setValue(sQ);
//            utp.child("tempitems").child("MediumQ").setValue(mQ);
        }


        public void setName(String name)
        {
            nsame=name;
            TextView post_title=(TextView)mView.findViewById(R.id.ffsn);
            post_title.setText(name);

        }


        public void setImage(Context ctx, String image)
        {


        }




        public void setPriceSmall(int priceSmall)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.ffsa);
            post_priceSmall.setText(Integer.toString(priceSmall));
            ssprice=priceSmall;
        }


        public void setPriceMedium(int priceMedium)
        {
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.fmm);
            post_priceMedium.setText(Integer.toString(priceMedium));
            smprice=priceMedium;
        }

        public void setId(String Id)
        {
            cartid=Id;

        }

        public void setSamount(int samount){

            ssamount=samount;
        }
        public void setMamount(int mamount){

            mmamount=mamount;
        }


        public void setTamount(int tamount){
            ttamount=tamount;
        }

        public void setSQuantity(int sQuantity){sQ=sQuantity;
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.ffsq);
            post_priceMedium.setText(Integer.toString(sQuantity));

            sQ=sQuantity;
        }


        public void setMQuantity(int mQuantity){mQ=mQuantity;
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.fma);
            post_priceMedium.setText(Integer.toString(mQuantity));

            mQ=mQuantity;
        }

    }





}