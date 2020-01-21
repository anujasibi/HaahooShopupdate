package com.haahoo.haahooshop;

import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.haahoo.haahooshop.utils.Global;
import com.haahoo.haahooshop.utils.SessionManager;

import java.util.List;

public class editchoosepdtadapter extends
        RecyclerView.Adapter<editchoosepdtadapter.ViewHolder> {

    private List<OffersModel> offersList;
    private Context context;
    public String id;
    SessionManager sessionManager;

    private int lastSelectedPosition = -1;

    public editchoosepdtadapter(List<OffersModel> offersListIn
            , Context ctx) {
        offersList = offersListIn;
        context = ctx;
        sessionManager=new SessionManager(ctx);
    }

    @Override
    public editchoosepdtadapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editcatvieww, parent, false);

        editchoosepdtadapter.ViewHolder viewHolder =
                new editchoosepdtadapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final editchoosepdtadapter.ViewHolder holder,
                                 final int position) {
        final OffersModel offersModel = offersList.get(position);

        holder.offerAmount.setText("" + offersModel.getName());




        id=offersModel.getRadio();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.d("idssd","mm"+offersModel.getRadio());
      //          Log.d("first","mm"+Global.trr.get(position));

                for (int i=0;i<Global.trr.size();i++){
                    if(offersModel.getRadio()==Global.trr.get(i)){
                        Global.trr.remove(i);
                        holder.cardView.setVisibility(View.GONE);
                        if(Global.trr.size()==0){
                            Toast.makeText(context,"Please add atleast one category",Toast.LENGTH_SHORT).show();
                        }
                    }


                    Log.d("ghjklkj","mm"+Global.trr);

                }


            }
        });

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
    }

    @Override
    public int getItemCount() {
        return offersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView offerAmount;
        public ImageView imageView;
        public CardView cardView;

        public ViewHolder(final View view) {
            super(view);
            offerAmount = (TextView) view.findViewById(R.id.cat);
            imageView=view.findViewById(R.id.img);
            cardView=view.findViewById(R.id.card);



        }
    }
}