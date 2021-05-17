package com.ashwathama.apnacountrypizza.MenuClicked;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
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

public class Menu1Frag extends Fragment {


    private RecyclerView mBlogList;
    private DatabaseReference mDatabase;
    LayoutAnimationController layoutAnimationController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mDatabase= FirebaseDatabase.getInstance().getReference().child("SimpleVegPizzas");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.recycler_pizzalist1);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));








    }






    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<menu1Blog, menu1fragBlogViewHolder> menu1firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<menu1Blog, menu1fragBlogViewHolder>
                (menu1Blog.class, R.layout.menu1_scroll, menu1fragBlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(menu1fragBlogViewHolder m1viewHolder, menu1Blog m1model, int i) {

                m1viewHolder.setName(m1model.getName());

                m1viewHolder.setImage(getActivity().getApplicationContext(),m1model.getImage());
                m1viewHolder.setPriceSmall(m1model.getPriceSmall());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());

            }


        };
        mBlogList.setAdapter(menu1firebaseRecyclerAdapter);

    }

    public static  class menu1fragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;
        ImageView fav;
        public String simage,sname;
        public int sPriceSmall,sPriceMedium;

        Button atc;

        final boolean[] state = {false};






        SessionManager sessionManager = new SessionManager(itemView.getContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
       public String phone = userDetails.get(SessionManager.KEY_PHONE);



       private  DatabaseReference dbfavs=FirebaseDatabase.getInstance().getReference("Users")
                .child(phone).child("favorites");
        private DatabaseReference checkfavs=FirebaseDatabase.getInstance().getReference("Users")
                .child(phone);
        private DatabaseReference addcart=checkfavs.child("cart");











        public menu1fragBlogViewHolder(final View itemView){
            super(itemView);
            mView=itemView;
            fav=(ImageView)itemView.findViewById(R.id.img_fav);
            final boolean[] exists = new boolean[1];




            atc=(Button)mView.findViewById(R.id.buttonaddtocart);

            checkfavs.keepSynced(true);


            atc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    checkfavs.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if(!snapshot.child("cart").hasChild(("SimpleVegPizza "+String.valueOf(getAdapterPosition())))){

                                int position = getAdapterPosition();


                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("name").setValue(sname);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("image").setValue(simage);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("PriceSmall").setValue(sPriceSmall);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("PriceMedium").setValue(sPriceMedium);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("SQuantity").setValue(0);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("MQuantity").setValue(0);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("id").setValue("SimpleVegPizza " + String.valueOf(position));
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("samount").setValue(0);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("mamount").setValue(0);
                                addcart.child("SimpleVegPizza " + String.valueOf(position)).child("tamount").setValue(0);

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


            checkfavs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if(snapshot.hasChild("favorites")&&snapshot.child("favorites").hasChild("SimpleVegPizza "+String.valueOf(getAdapterPosition()))){
                       fav.setImageResource(R.drawable.like_ic);
                       state[0]=true;
                   }
                   else{
                       fav.setImageResource(R.drawable.dislike_ic);
                       state[0]=false;
                   }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

            fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {







                    if(phone==null) {
                        Toast.makeText(mView.getContext(), "PLEASE LOGIN FIRST", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(state[0] ==false){

                        fav.setImageResource(R.drawable.like_ic);

                        int position=getAdapterPosition();






                        dbfavs.child("SimpleVegPizza "+String.valueOf(position)).child("name").setValue(sname);
                        dbfavs.child("SimpleVegPizza "+String.valueOf(position)).child("image").setValue(simage);
                        dbfavs.child("SimpleVegPizza "+String.valueOf(position)).child("PriceSmall").setValue(sPriceSmall);
                        dbfavs.child("SimpleVegPizza "+String.valueOf(position)).child("PriceMedium").setValue(sPriceMedium);




                    }

                   else if(state[0]==true){

                        fav.setImageResource(R.drawable.dislike_ic);
                        dbfavs.child("SimpleVegPizza "+String.valueOf(getAdapterPosition())).removeValue();




                    }








                }
            });
        }



        private ClickListener mClickListener;


        public interface ClickListener{
            public void onItemClick(View view, int position);
        }

        public void setOnClickListener(ClickListener clickListener){
            mClickListener =clickListener;
        }


        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.text_pizzamenu1);
            post_title.setText(name);
            sname=name;
        }




        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView)mView.findViewById(R.id.image_pizza1list);
            Picasso.get().load(image).into(post_image);
            simage=image;
        }




        public void setPriceSmall(int priceSmall)
        {
            TextView post_priceSmall=(TextView)mView.findViewById(R.id.smallmedium_pizzamenu1);
            post_priceSmall.setText(Integer.toString(priceSmall));
            sPriceSmall=priceSmall;
        }


        public void setPriceMedium(int priceMedium)
        {
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.pricemedium_pizzamenu1);
            post_priceMedium.setText(Integer.toString(priceMedium));
            sPriceMedium=priceMedium;
        }



    }

}