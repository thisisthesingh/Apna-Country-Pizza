package com.ashwathama.apnacountrypizza;

import android.app.AlertDialog;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashwathama.apnacountrypizza.Blogs.AddressBlog;
import com.ashwathama.apnacountrypizza.Fragment.CartFragment;
import com.ashwathama.apnacountrypizza.Fragment.OrderFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddressFrag extends DialogFragment {


    private RecyclerView aBlogList;
    private DatabaseReference aDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);


        aDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("Addresses");
        aDatabase.keepSynced(true);
        aBlogList=(RecyclerView)getView().findViewById(R.id.asssrecycleraddress);
        aBlogList.setHasFixedSize(true);
        aBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));


        ImageView addadress=(ImageView) getView().findViewById(R.id.addadddadressbtn);



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

               Button saveaddress=(Button) view.findViewById(R.id.save_address);

               ImageView backaddress=(ImageView)view.findViewById(R.id.backIconad);




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


        Button ap=(Button)getView().findViewById(R.id.msssproceed);
        Button ac=(Button)getView().findViewById(R.id.mssscancel);


        ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDialog().dismiss();




                DialogFragment dialogFragment=new FinalSelectFrag();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL,
                        android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialogFragment.show(getActivity().getSupportFragmentManager(),"My Fragment");

                Fragment someFragment = new OrderFragment();
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
                        transaction.addToBackStack("DetailedPizza");
                        transaction.commit();



            }
        });

        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDialog().dismiss();
                Fragment someFragment = new CartFragment();
                     FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
                        transaction.addToBackStack("DetailedPizza");
                        transaction.commit();

            }
        });









    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressBlog, aAddressViewHolder> afirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AddressBlog, aAddressViewHolder>
                (AddressBlog.class, R.layout.address_scroll, aAddressViewHolder.class,aDatabase) {


            @Override
            protected void populateViewHolder(aAddressViewHolder addressViewHolder, AddressBlog addressBlog, int i) {
                addressViewHolder.setAddressv(addressBlog.getAddressv());
                addressViewHolder.setIdv(addressBlog.getIdv());
            }



        };
        aBlogList.setAdapter(afirebaseRecyclerAdapter);


    }



    public static  class aAddressViewHolder extends RecyclerView.ViewHolder
    {

        View mView;

        String addres;


        String tid;

        public aAddressViewHolder(final View itemView) {
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

                        tdr.child("tad").setValue(addres);
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
            addres=addressv;
        }

        public void setIdv(String idv){

            tid=idv;

        }
    }
}