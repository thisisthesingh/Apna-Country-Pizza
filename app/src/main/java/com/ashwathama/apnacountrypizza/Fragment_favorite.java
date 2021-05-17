package com.ashwathama.apnacountrypizza;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.Menu.MenuBlog.menu1Blog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Fragment_favorite extends Fragment {


    private RecyclerView mBlogList;
    TextView favslang;

    private DatabaseReference mDatabase;
    LayoutAnimationController layoutAnimationController;
    private DatabaseReference checkfavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        favslang=(TextView)getActivity().findViewById(R.id.favslang);


        SessionManager sessionManager = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> userDetails = sessionManager.getUsersDetailsFromSession();
        String phone = userDetails.get(SessionManager.KEY_PHONE);






        checkfavorite=FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(phone).child("favorites");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.recycler_pizzalistfav);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }



    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<menu1Blog, favfragBlogViewHolder> favfirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<menu1Blog, favfragBlogViewHolder>
                (menu1Blog.class, R.layout.favorite_scroll, favfragBlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(favfragBlogViewHolder m1viewHolder, menu1Blog m1model, int i) {

                m1viewHolder.setName(m1model.getName());

                m1viewHolder.setImage(getActivity().getApplicationContext(),m1model.getImage());
                m1viewHolder.setPriceSmall(m1model.getPriceSmall());
                m1viewHolder.setPriceMedium(m1model.getPriceMedium());

            }


        };
        mBlogList.setAdapter(favfirebaseRecyclerAdapter);

    }



    public static  class favfragBlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;




        public favfragBlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
        }


        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.text_pizzamenufav);
            post_title.setText(name);
        }




        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView)mView.findViewById(R.id.image_pizzafavlist);
            Picasso.get().load(image).into(post_image);
        }




        public void setPriceSmall(int priceSmall)
        {
            TextView post_priceSmall=(TextView)mView.findViewById(R.id.smallmedium_pizzamenufav);
            post_priceSmall.setText(Integer.toString(priceSmall));
        }


        public void setPriceMedium(int priceMedium)
        {
            TextView post_priceMedium=(TextView)mView.findViewById(R.id.pricemedium_pizzamenufav);
            post_priceMedium.setText(Integer.toString(priceMedium));
        }


    }

}