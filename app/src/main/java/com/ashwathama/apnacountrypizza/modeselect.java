package com.ashwathama.apnacountrypizza;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.Blogs.AddressBlog;
import com.ashwathama.apnacountrypizza.Menu.MenuBlog.menu1Blog;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu1Frag;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class modeselect extends Fragment implements PaymentResultListener{


    RadioButton rbo,rbcod;
    ImageView iv,backaddress;
    Button finalpro,cancel;
    ImageView addadress;
    TextView fam;

    Button saveaddress;

    private RecyclerView aBlogList;
    private DatabaseReference aDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modeselect, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkout.preload(getActivity().getApplicationContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);




        aDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Addresses");
        aDatabase.keepSynced(true);
        aBlogList=(RecyclerView)getView().findViewById(R.id.recycleraddress);
        aBlogList.setHasFixedSize(true);
        aBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

        final DatabaseReference cartmDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


        iv=(ImageView)getView().findViewById(R.id.iv) ;
        addadress=(ImageView) getView().findViewById(R.id.addadressbtn);
        fam=(TextView)getView().findViewById(R.id.fan);




        rbo=(RadioButton)getView().findViewById(R.id.radioo);
        rbcod=(RadioButton)getView().findViewById(R.id.radiocod);
        finalpro=(Button)getView().findViewById(R.id.finalpro);
        cancel=(Button)getView().findViewById(R.id.cancel);







        cartmDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int t=snapshot.child("finalam").getValue(Integer.class);
                fam.setText(String.valueOf(t));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        rbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalpro.setVisibility(View.VISIBLE);

                cartmDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       int m= snapshot.child("finalam").getValue(Integer.class);
                       finalpro.setText("Pay Rs "+String.valueOf(m));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                //------------------
                cartmDatabase.child("temporder").child("paymentmode").setValue("Online Payment");
                rbcod.setChecked(false);
                iv.setImageResource(R.drawable.cash);
            }
       });

        rbcod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalpro.setVisibility(View.VISIBLE);
                finalpro.setText("Place Order");
                cartmDatabase.child("temporder").child("paymentmode").setValue("Cash On Delivery");
                rbo.setChecked(false);
                iv.setImageResource(R.drawable.cash);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartmDatabase.child("temporder").removeValue();
                startActivity(new Intent(getActivity().getApplicationContext(), UserDashboard.class));
                getActivity().finish();

            }
        });




        //--------------------------------------------------------------------------------------------------------------------------
        addadress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                View view=getLayoutInflater().inflate(R.layout.addressdialog,null);


                alert.setView(view);

                final AlertDialog alertDialog=alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.show();

                final TextInputLayout city, area, house_no, pincode, state, landmark;



                //Hooks
                city = (TextInputLayout)view.findViewById(R.id.city);
                area = (TextInputLayout) view.findViewById(R.id.area);
                house_no = (TextInputLayout)view.findViewById(R.id.house_no);
                pincode = (TextInputLayout) view.findViewById(R.id.pincode);
                state = (TextInputLayout) view.findViewById(R.id.state);
                landmark = (TextInputLayout) view.findViewById(R.id.landmark);
//
//              final  boolean b=(city.getEditText().getText().toString()).isEmpty() || (house_no.getEditText().getText().toString()).isEmpty() ||(city.getEditText().getText().toString()).isEmpty() ||
//                        (pincode.getEditText().getText().toString()).isEmpty() ||(landmark.getEditText().getText().toString()).isEmpty() ||(city.getEditText().getText().toString()).isEmpty() ;


                final boolean b;

               String s=city.getEditText().getText().toString();

               saveaddress=(Button) view.findViewById(R.id.save_address);

               backaddress=(ImageView)view.findViewById(R.id.backIconad);




                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
                String phone = userDetails.get(SessionManager.KEY_PHONE);


               final DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Users")
                        .child(phone);




                saveaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {




                            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.hasChild("Addresses")) {
                                        dr.child("TotalAddress").setValue(1);
                                        dr.child("Addresses").child("1").child("addressv").setValue(house_no.getEditText().getText() + ", " + area.getEditText().getText() + ", " + city.getEditText().getText() + ", " + landmark.getEditText().getText() + ", " + state.getEditText().getText() + "-" + " " + pincode.getEditText().getText());
                                        dr.child("Addresses").child("1").child("idv").setValue("A" + "1");
                                        alertDialog.dismiss();
                                    } else {
                                        int t = snapshot.child("TotalAddress").getValue(Integer.class);
                                        t++;
                                        dr.child("Addresses").child(String.valueOf(t)).child("addressv").setValue(house_no.getEditText().getText() + ", " + area.getEditText().getText() + ", " + city.getEditText().getText() + ", " + landmark.getEditText().getText() + ", " + state.getEditText().getText() + "-" + " " + pincode.getEditText().getText());
                                        dr.child("Addresses").child(String.valueOf(t)).child("idv").setValue("A" + String.valueOf(t));
                                        dr.child("TotalAddress").setValue(t);
                                        alertDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }

                    
                });



                backaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });





            }
        });




        //--------------------------------------------------------------------------------------------------------------------------------



        cartmDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s=snapshot.child("temporder").child("paymentmode").getValue(String.class);

                if(s=="Cash On Delivery"){

                    finalpro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Cash On Delivery Processed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else if(s=="Online Payment"){


                    finalpro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Online payment is to be processed-----------------------------



                            final String[] orderamount = new String[1];

                            SessionManager sessionManager = new SessionManager(getContext());
                            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
                            String phone = userDetails.get(SessionManager.KEY_PHONE);


                            aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("finalam");
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



                            //Online Payment is to be processed-----------------------------
                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });










    }
//
//    private void startPayment() {
//
//        final String[] orderamount = new String[1];
//
//        final Checkout co = new Checkout();
//
//        SessionManager sessionManager = new SessionManager(getContext());
//        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
//        String phone = userDetails.get(SessionManager.KEY_PHONE);
//
//
//        aDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("finalam");
//        aDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                orderamount[0] = String.valueOf(snapshot.getValue(Integer.class));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", "Apna Country Pizza");
//            options.put("description", "App Payment");
//            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
//            options.put("currency", "INR");
//            String payment = (orderamount[0]);
//            // amount is in paise so please multiple it by 100
//            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
//            double total = Double.parseDouble(payment);
//            total = total * 100;
//            options.put("amount", total);
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "ashwathamatechlabs@gmail.com");
//            preFill.put("contact", "917906913683");
//            options.put("prefill", preFill);
//            co.open(getActivity(), options);
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//
//
//
//
//
//    }
//    @Override
//    public void onPaymentSuccess(String s) {
//
//        // payment successfull pay_DGU19rDsInjcF2
//        Log.e(TAG, " payment successfull "+ s.toString());
//        Toast.makeText(getContext(), "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//
//        Log.e(TAG,  "error code "+String.valueOf(i)+" -- Payment failed "+s.toString()  );
//        try {
//            Toast.makeText(getContext(), "Payment error please try again", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            Log.e("OnPaymentError", "Exception in onPaymentError", e);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressBlog, AddressViewHolder> afirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AddressBlog, AddressViewHolder>
                (AddressBlog.class, R.layout.address_scroll, AddressViewHolder.class,aDatabase) {


            @Override
            protected void populateViewHolder(AddressViewHolder addressViewHolder, AddressBlog addressBlog, int i) {
                addressViewHolder.setAddressv(addressBlog.getAddressv());
                addressViewHolder.setIdv(addressBlog.getIdv());
            }



        };
        aBlogList.setAdapter(afirebaseRecyclerAdapter);


    }

    private void setUpPayment(String orderamount) {



        Checkout checkout = new Checkout();

        // checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity =getActivity();

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Apna Country Pizza");
            options.put("description", "powered by ATLABS");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", "ashwathamatechlabs@gmail.com");
            options.put("prefill.contact","917906913683");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }



    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(getActivity(), "Payment successfull" +s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(getActivity(), "Payment Failed" +s, Toast.LENGTH_SHORT).show();
    }


    public static  class AddressViewHolder extends RecyclerView.ViewHolder
    {

        View mView;


        String tid;

        public AddressViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            final ImageView tick = (ImageView)itemView.findViewById(R.id.tickmark);
            LinearLayout linearLayout=(LinearLayout)itemView.findViewById(R.id.ll);

            SessionManager sessionManager = new SessionManager(mView.getContext());
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            String phone = userDetails.get(SessionManager.KEY_PHONE);

            final DatabaseReference tdr= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);


            tdr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   String s= snapshot.child("selectedaddress").getValue(String.class);
                    if (s == tid) {

                        tick.setVisibility(View.VISIBLE);
                        tick.setImageResource(R.drawable.greentick);

                    }
                    else{
                        tick.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   tdr.child("selectedaddress").setValue(tid);

                }
            });



        }



        public void setAddressv(String addressv){
            TextView addressview=(TextView)mView.findViewById(R.id.addressholdertv);
            addressview.setText(addressv);
        }

        public void setIdv(String idv){

            tid=idv;

        }
 }


}