package com.ashwathama.apnacountrypizza.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.AddressFrag;
import com.ashwathama.apnacountrypizza.Blogs.AddressBlog;
import com.ashwathama.apnacountrypizza.Blogs.cartblog;
import com.ashwathama.apnacountrypizza.R;
import com.ashwathama.apnacountrypizza.SessionManager;
import com.ashwathama.apnacountrypizza.modeselect;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CartFragment extends Fragment {





    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    LayoutAnimationController layoutAnimationController;
    private DatabaseReference checkfavorite;
    Button proceed;


    FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);



        proceed=(Button)getView().findViewById(R.id.proceedbtn);





        frameLayout=(FrameLayout)getView().findViewById(R.id.fragment_container);

        checkfavorite=FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("cart");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.recycler_cart);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));




        final TextView finalamount=(TextView)getActivity().findViewById(R.id.totalamount);


        checkfavorite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.hasChild("finalam")) {
                    checkfavorite.child("finalam").setValue(0);
                } else {

                    int j = snapshot.child("finalam").getValue(Integer.class);
                    finalamount.setText(String.valueOf(j));

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//
//
//                        Fragment someFragment = new modeselect();
//                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
//                        transaction.addToBackStack("DetailedPizza");
//                        transaction.commit();


                final AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                View msview=getLayoutInflater().inflate(R.layout.modeofpay,null);


                alert.setView(msview);

                final AlertDialog alertDialog=alert.create();
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.show();


                final RadioButton rbo=(RadioButton)msview.findViewById(R.id.radioo);
                final RadioButton rbcod=(RadioButton)msview.findViewById(R.id.radiocod);
                 Button msproceed=(Button) msview.findViewById(R.id.msproceed);
                Button mscancel=(Button) msview.findViewById(R.id.mscancel);
                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
                HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
                String phone = userDetails.get(SessionManager.KEY_PHONE);




                final DatabaseReference cartmDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);






                rbo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cartmDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int m= snapshot.child("finalam").getValue(Integer.class);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        //------------------
                        cartmDatabase.child("temporder").child("paymentmode").setValue("Online Payment");

                        rbcod.setChecked(false);

                    }
                });

                rbcod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        cartmDatabase.child("temporder").child("paymentmode").setValue("Cash On Delivery");
                        rbo.setChecked(false);

                    }
                });

//_______________________________________________________________________________________________________________________________________________________________________


                msproceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //---------------------------

                        DialogFragment dialogFragment=new AddressFrag();
                        dialogFragment.show(getActivity().getSupportFragmentManager(),"My Fragment");

//
//                        final AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
//                        View asview=getLayoutInflater().inflate(R.layout.addressselectdialog,null);
//
//
//
//                        alert.setView(asview);
//
//                        final AlertDialog alertDialog=alert.create();
//                        alertDialog.setCanceledOnTouchOutside(false);
//
//                        alertDialog.show();
//
//                       ImageView addadress=(ImageView) asview.findViewById(R.id.adddadressbtn);
//
//
//                        addadress.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
//                                View view=getLayoutInflater().inflate(R.layout.addressdialog,null);
//
//
//                                alert.setView(view);
//
//                                final AlertDialog alertDialog=alert.create();
//                                alertDialog.setCanceledOnTouchOutside(false);
//
//                                alertDialog.show();
//
//                                final TextInputLayout city, area, house_no, pincode, state, landmark;
//
//
//
//                                //Hooks
//                                city = (TextInputLayout)view.findViewById(R.id.city);
//                                area = (TextInputLayout) view.findViewById(R.id.area);
//                                house_no = (TextInputLayout)view.findViewById(R.id.house_no);
//                                pincode = (TextInputLayout) view.findViewById(R.id.pincode);
//                                state = (TextInputLayout) view.findViewById(R.id.state);
//                                landmark = (TextInputLayout) view.findViewById(R.id.landmark);
////
////              final  boolean b=(city.getEditText().getText().toString()).isEmpty() || (house_no.getEditText().getText().toString()).isEmpty() ||(city.getEditText().getText().toString()).isEmpty() ||
////                        (pincode.getEditText().getText().toString()).isEmpty() ||(landmark.getEditText().getText().toString()).isEmpty() ||(city.getEditText().getText().toString()).isEmpty() ;
//
//
//                                final boolean b;
//
//                                String s=city.getEditText().getText().toString();
//
//                                Button saveaddress=(Button) view.findViewById(R.id.save_address);
//
//                                ImageView backaddress=(ImageView)view.findViewById(R.id.backIconad);
//
//
//
//
//                                SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
//                                HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
//                                String phone = userDetails.get(SessionManager.KEY_PHONE);
//
//
//                                final DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Users")
//                                        .child(phone);
//
//
//
//
//                                saveaddress.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//
//
//
//
//                                        dr.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if (!snapshot.hasChild("Addresses")) {
//                                                    dr.child("TotalAddress").setValue(1);
//                                                    dr.child("Addresses").child("1").child("addressv").setValue(house_no.getEditText().getText() + ", " + area.getEditText().getText() + ", " + city.getEditText().getText() + ", " + landmark.getEditText().getText() + ", " + state.getEditText().getText() + "-" + " " + pincode.getEditText().getText());
//                                                    dr.child("Addresses").child("1").child("idv").setValue("A" + "1");
//                                                    alertDialog.dismiss();
//                                                } else {
//                                                    int t = snapshot.child("TotalAddress").getValue(Integer.class);
//                                                    t++;
//                                                    dr.child("Addresses").child(String.valueOf(t)).child("addressv").setValue(house_no.getEditText().getText() + ", " + area.getEditText().getText() + ", " + city.getEditText().getText() + ", " + landmark.getEditText().getText() + ", " + state.getEditText().getText() + "-" + " " + pincode.getEditText().getText());
//                                                    dr.child("Addresses").child(String.valueOf(t)).child("idv").setValue("A" + String.valueOf(t));
//                                                    dr.child("TotalAddress").setValue(t);
//                                                    alertDialog.dismiss();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });
//
//
//                                    }
//
//
//                                });
//
//
//
//                                backaddress.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        alertDialog.dismiss();
//                                    }
//                                });
//
//
//
//

//                            }
//                        });







                        //---------------------------
                        alertDialog.dismiss();
                    }
                });

                mscancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       cartmDatabase.child("temporder").removeValue();
                        alertDialog.dismiss();
                    }
                });

            }
        });







//_________________________________________________________________________________________________________________________________________________________________


    }




    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<cartblog, cartfragBlogViewHolder> cartfirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<cartblog, cartfragBlogViewHolder>
                (cartblog.class, R.layout.cart_scroll, cartfragBlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(cartfragBlogViewHolder m1viewHolder, cartblog m1model, int i) {

                m1viewHolder.setName(m1model.getName());

                m1viewHolder.setImage(getActivity().getApplicationContext(),m1model.getImage());
                m1viewHolder.setPriceSmall(m1model.getPriceSmall());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());
                m1viewHolder.setId(m1model.getId());
                m1viewHolder.setSamount(m1model.getSamount());
                m1viewHolder.setMamount(m1model.getMamount());
                m1viewHolder.setTamount(m1model.getTamount());
                m1viewHolder.setSQuantity(m1model.getSQuantity());
                m1viewHolder.setMQuantity(m1model.getMQuantity());


            }


        };
        mBlogList.setAdapter(cartfirebaseRecyclerAdapter);

    }







    public static  class cartfragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        int ssprice;
        int smprice;
        String cartid;
        ImageView cross;
        int ssamount,mmamount,ttamount;


        TextView ps,ms,ts;
        TextView pm,mm,tm;

        ImageView c;

        int sQ,mQ;


        public cartfragBlogViewHolder(final View itemView){
            super(itemView);
            mView=itemView;

            SessionManager sessionManager = new SessionManager(mView.getContext());
            HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
            String phone = userDetails.get(SessionManager.KEY_PHONE);

            int k;
            final int[] j = new int[1];

            pm=(TextView)mView.findViewById(R.id.plusm);
            mm=(TextView)mView.findViewById(R.id.minusm);
            tm=(TextView)mView.findViewById(R.id.textm);


            c=(ImageView)mView.findViewById(R.id.cross);
            ps=(TextView)mView.findViewById(R.id.plusS);
            ms=(TextView)mView.findViewById(R.id.minuss);
            ts=(TextView)mView.findViewById(R.id.Texts);


            final DatabaseReference cartmDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

            final int[] m = new int[1];
            final int[] n=new int[1];

            final int[] price = new int[1];



            cartmDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {




                   if(snapshot.child("cart").child(cartid).child("MQuantity").getValue(Integer.class)!=null&&snapshot.child("cart").child(cartid).child("SQuantity").getValue(Integer.class)!=null){

                      m[0] = snapshot.child("cart").child(cartid).child("SQuantity").getValue(Integer.class);
                       n[0] = snapshot.child("cart").child(cartid).child("MQuantity").getValue(Integer.class);

                      ts.setText( String.valueOf(snapshot.child("cart").child(cartid).child("SQuantity").getValue()));

                       tm.setText(String.valueOf(snapshot.child("cart").child(cartid).child("MQuantity").getValue(Integer.class)));



                   }

                   else{


                       Fragment prev = ((AppCompatActivity) mView.getContext()).getSupportFragmentManager().findFragmentByTag("My Fragment");
                       if (prev != null) {
                           DialogFragment df = (DialogFragment) prev;
                           df.dismiss();
                       }


                   }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

            cartmDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.hasChild("finalam")) {

                        price[0] = snapshot.child("finalam").getValue(Integer.class);

                    }
                    else{
                        cartmDatabase.child("finalam").setValue(0);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            ps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(m[0]!=-1){
                        m[0]=m[0]+1;
                        cartmDatabase.child("cart").child(cartid).child("SQuantity").setValue(m[0]);
                        ts.setText(String.valueOf(m[0]));


                        price[0]=price[0]+(ssprice);


                        cartmDatabase.child("finalam").setValue(price[0]);

                    }

                }
            });


            ms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(m[0]!=0&&  m[0]!=-1){

                        m[0]=m[0]-1;
                        cartmDatabase.child("cart").child(cartid).child("SQuantity").setValue(m[0]);
                        ts.setText(String.valueOf(m[0]));


                        price[0]=price[0]-(ssprice);


                        cartmDatabase.child("finalam").setValue(price[0]);


                    }


                   }
            });

            pm.setOnClickListener(new View.OnClickListener() {
                @Override


                public void onClick(View v) {
                    if(n[0]!=-1) {

                        n[0] = n[0] + 1;
                        cartmDatabase.child("cart").child(cartid).child("MQuantity").setValue(n[0]);
                        tm.setText(String.valueOf(n[0]));

                        price[0]=price[0]+(smprice);


                        cartmDatabase.child("finalam").setValue(price[0]);


                    }

                }
            });


            mm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(n[0]!=0   &&n[0]!=-1){

                        n[0]=n[0]-1;
                        cartmDatabase.child("cart").child(cartid).child("MQuantity").setValue(n[0]);
                        tm.setText(String.valueOf(n[0]));


                        price[0]=price[0]-(smprice);


                        cartmDatabase.child("finalam").setValue(price[0]);



                    }


                }
            });


            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    price[0]=price[0]-((ssprice*m[0])+(smprice*n[0]));


                    cartmDatabase.child("finalam").setValue(price[0]);


                    cartmDatabase.child("cart").child(cartid).removeValue();


                }
            });
        }


        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.cartpizzaname);
            post_title.setText(name);

        }


        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView)mView.findViewById(R.id.cartimage);
            Picasso.get().load(image).into(post_image);
        }




        public void setPriceSmall(int priceSmall)
        {

            TextView post_priceSmall=(TextView)mView.findViewById(R.id.cartpricesmall);
            post_priceSmall.setText(Integer.toString(priceSmall));
            ssprice=priceSmall;
        }


        public void setPriceMedium(int priceMedium)
        {
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.cartpricemedium);
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

        public void setSQuantity(int sQuantity){sQ=sQuantity;}


        public void setMQuantity(int mQuantity){mQ=mQuantity;}

    }





}