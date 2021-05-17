package com.ashwathama.apnacountrypizza.MenuClicked;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.Menu.MenuBlog.menu1Blog;
import com.ashwathama.apnacountrypizza.R;
import com.ashwathama.apnacountrypizza.SessionManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Menu2Frag extends Fragment {






    private RecyclerView mBlogList2;
    private DatabaseReference mDatabase2;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase2= FirebaseDatabase.getInstance().getReference().child("VegTreat");
        mDatabase2.keepSynced(true);
        mBlogList2=(RecyclerView)getView().findViewById(R.id.recycler_pizzalist2);
        mBlogList2.setHasFixedSize(true);
        mBlogList2.setLayoutManager(new LinearLayoutManager(getActivity()));









    }




    @Override
    public void onStart() {
        super.onStart();







        FirebaseRecyclerAdapter<menu1Blog, menu2fragBlogViewHolder> menu2firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<menu1Blog, menu2fragBlogViewHolder>
                (menu1Blog.class, R.layout.menu2_scroll, menu2fragBlogViewHolder.class,mDatabase2) {
            @Override
            public void populateViewHolder(menu2fragBlogViewHolder m1viewHolder, menu1Blog m1model, int i) {

                m1viewHolder.setName(m1model.getName());


                m1viewHolder.setImage(getActivity().getApplicationContext(),m1model.getImage());
                m1viewHolder.setPriceSmall(m1model.getPriceSmall());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());













            }



        };
        mBlogList2.setAdapter(menu2firebaseRecyclerAdapter);








    }



    public static  class menu2fragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        Button atcbtn2;
        public ImageView cart2;
        ImageView fav2;

        final boolean[] state = {false};




        String sname,simage;
        int sPriceSmall,sPriceMedium;






        SessionManager sessionManager = new SessionManager(itemView.getContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);



        private  DatabaseReference dbfavs=FirebaseDatabase.getInstance().getReference("Users")
                .child(phone).child("favorites");
        private DatabaseReference checkfavs=FirebaseDatabase.getInstance().getReference("Users")
                .child(phone);

        private DatabaseReference addcart=checkfavs.child("cart");






        public menu2fragBlogViewHolder(View itemView){
            super(itemView);

            mView=itemView;

            atcbtn2=(Button)itemView.findViewById(R.id.buttonaddtocart2);
            fav2=(ImageView)itemView.findViewById(R.id.img_fav2);





           atcbtn2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   checkfavs.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {


                           if(!snapshot.child("cart").hasChild(("VegTreat "+String.valueOf(getAdapterPosition())))){

                               int position = getAdapterPosition();


                               addcart.child("VegTreat " + String.valueOf(position)).child("name").setValue(sname);
                               addcart.child("VegTreat " + String.valueOf(position)).child("image").setValue(simage);
                               addcart.child("VegTreat " + String.valueOf(position)).child("PriceSmall").setValue(sPriceSmall);
                               addcart.child("VegTreat " + String.valueOf(position)).child("PriceMedium").setValue(sPriceMedium);

                               addcart.child("VegTreat " + String.valueOf(position)).child("id").setValue("VegTreat " + String.valueOf(position));
                               addcart.child("VegTreat " + String.valueOf(position)).child("SQuantity").setValue(0);
                               addcart.child("VegTreat " + String.valueOf(position)).child("MQuantity").setValue(0);
                               addcart.child("VegTreat " + String.valueOf(position)).child("samount").setValue(0);
                               addcart.child("VegTreat " + String.valueOf(position)).child("mamount").setValue(0);
                               addcart.child("VegTreat " + String.valueOf(position)).child("tamount").setValue(0);
                               Toast.makeText(mView.getContext(), "Added To Cart Successfully", Toast.LENGTH_SHORT).show();


                               return;
                           }

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });




               }
           });



            checkfavs.keepSynced(true);


            checkfavs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("favorites")&&snapshot.child("favorites").hasChild("VegTreat "+String.valueOf(getAdapterPosition()))){
                        fav2.setImageResource(R.drawable.like_ic);
                        state[0]=true;
                    }
                    else{
                        fav2.setImageResource(R.drawable.dislike_ic);
                        state[0]=false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });



            fav2.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {







                           if(phone==null) {
                               Toast.makeText(mView.getContext(), "PLEASE LOGIN FIRST", Toast.LENGTH_SHORT).show();
                               return;
                           }

                           if(state[0] ==false){

                               fav2.setImageResource(R.drawable.like_ic);

                               int position=getAdapterPosition();






                               dbfavs.child("VegTreat "+String.valueOf(position)).child("name").setValue(sname);
                               dbfavs.child("VegTreat "+String.valueOf(position)).child("image").setValue(simage);
                               dbfavs.child("VegTreat "+String.valueOf(position)).child("PriceSmall").setValue(sPriceSmall);
                               dbfavs.child("VegTreat "+String.valueOf(position)).child("PriceMedium").setValue(sPriceMedium);




                           }

                           else if(state[0]==true){

                               fav2.setImageResource(R.drawable.dislike_ic);
                               dbfavs.child("VegTreat "+String.valueOf(getAdapterPosition())).removeValue();




                           }









               }
           });




        }






        private ClickListener mClickListener;



        public interface ClickListener{
            public void onItemClick(View view, int position);

            public void onClickedcart(View view, int position);



        }
        public void setOnClickListener(ClickListener clickListener){
            mClickListener = clickListener;
        }






        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.text_pizzamenu2);


            sname=name;
            post_title.setText(name);


        }




        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView)mView.findViewById(R.id.image_pizza2list);
            simage=image;
            Picasso.get().load(image).into(post_image);

        }




        public void setPriceSmall(int priceSmall)
        {
            TextView post_priceSmall=(TextView)mView.findViewById(R.id.smallmedium_pizzamenu2);
            sPriceSmall=priceSmall;
            post_priceSmall.setText(Integer.toString(priceSmall));

        }


        public void setPriceMedium(int priceMedium)

        {
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.pricemedium_pizzamenu2);

            sPriceMedium=priceMedium;
            post_priceMedium.setText(Integer.toString(priceMedium));
        }



    }






}