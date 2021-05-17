package com.ashwathama.apnacountrypizza.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwathama.apnacountrypizza.Blogs.Blog;
import com.ashwathama.apnacountrypizza.Blogs.Blog2;
import com.ashwathama.apnacountrypizza.Blogs.Blog3;
import com.ashwathama.apnacountrypizza.Blogs.Blog4;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu1Frag;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu2Frag;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu3Frag;
import com.ashwathama.apnacountrypizza.MenuClicked.Menu4Frag;
import com.ashwathama.apnacountrypizza.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    ImageSlider mainSlider;
    private RecyclerView mBlogList,mBlogList2,mBlogList3,mBlogList4;
    private DatabaseReference mDatabase,mDatabase2,mDatabase3,mDatabase4;
    LinearLayout toMenu1,toMenu2,toMenu3,toMenu4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);

         }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {



        mainSlider=(ImageSlider)getView().findViewById(R.id.image_slider2);
        final List<SlideModel> remoteImages=new ArrayList<>();



        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            remoteImages.add(new SlideModel(data.child("url").getValue().toString(),data.child("title").getValue().toString(), ScaleTypes.CENTER_CROP ));
                        }

                        mainSlider.setImageList(remoteImages,ScaleTypes.CENTER_CROP);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {



                    }
                });


//This is the starting of Blog1
        mDatabase= FirebaseDatabase.getInstance().getReference().child("SimpleVegPizzas");
        mDatabase.keepSynced(true);
        mBlogList=(RecyclerView)getView().findViewById(R.id.myrecycleview);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(getActivity()));
//This is the ending of Blog 1




        //This is the Starting of Blog2



        mDatabase2= FirebaseDatabase.getInstance().getReference().child("VegTreat");
        mDatabase2.keepSynced(true);
        mBlogList2=(RecyclerView)getView().findViewById(R.id.myrecycleview2);
        mBlogList2.setHasFixedSize(true);
        mBlogList2.setLayoutManager(new LinearLayoutManager(getActivity()));

        //This is the ending of blog2


        //This is the starting of blog3

        mDatabase3= FirebaseDatabase.getInstance().getReference().child("VegSpecial");
        mDatabase3.keepSynced(true);
        mBlogList3=(RecyclerView)getView().findViewById(R.id.myrecycleview3);
        mBlogList3.setHasFixedSize(true);
        mBlogList3.setLayoutManager(new LinearLayoutManager(getActivity()));



        //This is the ending of blog3

        //This is starting of Blog4

        mDatabase4= FirebaseDatabase.getInstance().getReference().child("VegFeast");
        mDatabase4.keepSynced(true);
        mBlogList4=(RecyclerView)getView().findViewById(R.id.myrecycleview4);
        mBlogList4.setHasFixedSize(true);
        mBlogList4.setLayoutManager(new LinearLayoutManager(getActivity()));

        //This is starting of Blog 4




        toMenu1=(LinearLayout)getView().findViewById(R.id.slider_1);
        toMenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment someFragment = new Menu1Frag();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
                transaction.addToBackStack("DetailedPizza");
                transaction.commit();

            }
        });














        toMenu2=(LinearLayout)getView().findViewById(R.id.slider_2);
        toMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment someFragment = new Menu2Frag();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
                transaction.addToBackStack("DetailedPizza");
                transaction.commit();

            }
        });




        toMenu3=(LinearLayout)getView().findViewById(R.id.slider_3);
        toMenu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment someFragment = new Menu3Frag();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, someFragment, "DetailedPizza");
                transaction.addToBackStack("DetailedPizza");
                transaction.commit();

            }
        });





        toMenu4=(LinearLayout)getView().findViewById(R.id.slider_4);
        toMenu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment someFragment = new Menu4Frag();
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



        //OnStart BLog1 start
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>
                (Blog.class, R.layout.blog_row,BlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int i) {

                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getActivity().getApplicationContext(),model.getImage());

            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);

        //On start Blog1 end


        //Onstart blog2 start

        FirebaseRecyclerAdapter<Blog2,BlogViewHolder2> firebaseRecyclerAdapter2=new FirebaseRecyclerAdapter<Blog2, BlogViewHolder2>(Blog2.class, R.layout.blog_row_2,BlogViewHolder2.class,mDatabase2) {
            @Override
            protected void populateViewHolder(BlogViewHolder2 viewHolder2, Blog2 model2, int i) {

                viewHolder2.setName(model2.getName());
                viewHolder2.setDesc(model2.getDesc());
                viewHolder2.setImage(getActivity().getApplicationContext(),model2.getImage());

            }
        };
        mBlogList2.setAdapter(firebaseRecyclerAdapter2);


        //Onstart blog2 end

        //Onstart blog3 start

        FirebaseRecyclerAdapter<Blog3,BlogViewHolder3> firebaseRecyclerAdapter3=new FirebaseRecyclerAdapter<Blog3, BlogViewHolder3>(Blog3.class, R.layout.blog_row_3,BlogViewHolder3.class,mDatabase3) {
            @Override
            protected void populateViewHolder(BlogViewHolder3 viewHolder3, Blog3 model3, int i) {
                viewHolder3.setName(model3.getName());
                viewHolder3.setDesc(model3.getDesc());
                viewHolder3.setImage(getActivity().getApplicationContext(),model3.getImage());


            }
        };

        mBlogList3.setAdapter(firebaseRecyclerAdapter3);


        //On start blog 3 end


        //On start blog 4 start

        FirebaseRecyclerAdapter<Blog4,BlogViewHolder4> firebaseRecyclerAdapter4=new FirebaseRecyclerAdapter<Blog4, BlogViewHolder4>(Blog4.class, R.layout.blog_row_4,BlogViewHolder4.class,mDatabase4) {
            @Override
            protected void populateViewHolder(BlogViewHolder4 viewHolder4, Blog4 model4, int i) {
                viewHolder4.setName(model4.getName());
                viewHolder4.setDesc(model4.getDesc());
                viewHolder4.setImage(getActivity().getApplicationContext(),model4.getImage());
            }
        };
        mBlogList4.setAdapter(firebaseRecyclerAdapter4);

        //On start blog 4 end



    }

    //Class BlogView Holder 1 start
    public static  class BlogViewHolder extends RecyclerView.ViewHolder
    {

        View mView;


        public BlogViewHolder(View itemView){
            super(itemView);
            mView=itemView;
        }

        public void setName(String name)
        {
            TextView post_title=(TextView)mView.findViewById(R.id.post_title);
            post_title.setText(name);
        }

        public void setDesc(String desc){

            TextView post_desc=(TextView)mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);



        }


        public void setImage(Context ctx, String image)
        {
            ImageView post_image=(ImageView)mView.findViewById(R.id.post_image);
            Picasso.get().load(image).into(post_image);
        }







    }

    //Class Blog View Holder 1 end



    //Class BLogVIewHolder  2 start

    public static  class BlogViewHolder2 extends RecyclerView.ViewHolder
    {

        View mView2;


        public BlogViewHolder2(View itemView){
            super(itemView);
            mView2=itemView;
        }

        public void setName(String name)
        {
            TextView post_title_2=(TextView)mView2.findViewById(R.id.post_title_2);
            post_title_2.setText(name);
        }

        public void setDesc(String desc){

            TextView post_desc_2=(TextView)mView2.findViewById(R.id.post_desc_2);
            post_desc_2.setText(desc);



        }


        public void setImage(Context ctx,String image)
        {
            ImageView post_image_2=(ImageView)mView2.findViewById(R.id.post_image_2);
            Picasso.get().load(image).into(post_image_2);
        }







    }

    //Blog viewholder 2 end

    //BlogViewholder 3 start


    public static  class BlogViewHolder3 extends RecyclerView.ViewHolder
    {

        View mView3;


        public BlogViewHolder3(View itemView){
            super(itemView);
            mView3=itemView;
        }

        public void setName(String name)
        {
            TextView post_title_3=(TextView)mView3.findViewById(R.id.post_title_3);
            post_title_3.setText(name);
        }

        public void setDesc(String desc){

            TextView post_desc_3=(TextView)mView3.findViewById(R.id.post_desc_3);
            post_desc_3.setText(desc);



        }


        public void setImage(Context ctx,String image)
        {
            ImageView post_image_3=(ImageView)mView3.findViewById(R.id.post_image_3);
            Picasso.get().load(image).into(post_image_3);
        }







    }


    //Blogviewholder 3 ends

    //Blog ViewHolder 4 start

    public static  class BlogViewHolder4 extends RecyclerView.ViewHolder
    {

        View mView4;


        public BlogViewHolder4(View itemView){
            super(itemView);
            mView4=itemView;
        }

        public void setName(String name)
        {
            TextView post_title_4=(TextView)mView4.findViewById(R.id.post_title_4);
            post_title_4.setText(name);
        }

        public void setDesc(String desc){

            TextView post_desc_4=(TextView)mView4.findViewById(R.id.post_desc_4);
            post_desc_4.setText(desc);



        }


        public void setImage(Context ctx,String image)
        {
            ImageView post_image_4=(ImageView)mView4.findViewById(R.id.post_image_4);
            Picasso.get().load(image).into(post_image_4);
        }







    }

    //Blog ViewHolder 4 ends



}
 /*   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ImageSlider mainSlider;


        mainSlider=(ImageSlider)findViewById(R.id.image_slider2);




        final List<SlideModel> remoteImages=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            remoteImages.add(new SlideModel(data.child("url").getValue().toString(),data.child("title").getValue().toString(), ScaleTypes.CENTER_CROP ));
                        }

                        mainSlider.setImageList(remoteImages,ScaleTypes.CENTER_CROP);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



           }
}*/


  