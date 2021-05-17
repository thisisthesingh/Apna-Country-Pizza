package com.ashwathama.apnacountrypizza;

import android.net.Ikev2VpnProfile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.ashwathama.apnacountrypizza.Fragment.ProfileFragment.MANAGE_ADDRESS;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressModel> addressModelList;
    private int MODE;

    public AddressesAdapter(List<AddressModel> addressModelList, int MODE) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
    }

    @NonNull
    @Override
    public AddressesAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addresses_item_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressesAdapter.Viewholder holder, int position) {
        String name = addressModelList.get(position).getFullName();
        String address = addressModelList.get(position).getAddress();
        String pincode = addressModelList.get(position).getPincode();
        holder.setData(name, address, pincode);
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView fullName;
        private TextView address;
        private TextView pinCode;
        private ImageView icon;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.address_name);
            address = itemView.findViewById(R.id.address_address);
            pinCode = itemView.findViewById(R.id.address_pincode);
            icon = itemView.findViewById(R.id.check);

        }

        private void setData(String username, String useraddress, String userpincode){

            fullName.setText(username);
            address.setText(useraddress);
            pinCode.setText(userpincode);

//            if(MODE == MANAGE_ADDRESS){
//
//           }
//            else if (MODE == SELECT_ADDRESS){
//                  icon.setImageResource(R.drawable.tick);
//            }

        }

    }
}
