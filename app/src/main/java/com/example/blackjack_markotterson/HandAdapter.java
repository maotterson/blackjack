package com.example.blackjack_markotterson;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;

public class HandAdapter extends RecyclerView.Adapter<HandAdapter.ViewHolder> {
    private Hand hand;
    private Context context;

    public HandAdapter(Hand hand, Context context){
        this.hand = hand;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //inflate the receipt_item layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
        ViewHolder currentViewHolder = new ViewHolder(v);
        return currentViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String imageString = hand.getCardinHandAt(position).getCardImage();
        String packageName = context.getPackageName();

        int resourceId = context.getResources().getIdentifier(imageString, "drawable", packageName);

        //bind the data to the viewholder item
        holder.cardImage.setImageResource(resourceId);
    }

    @Override
    public int getItemCount(){
        int itemCount = hand.getHandSize();
        return itemCount;
    }
    //ViewHolder subclass
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cardImage;

        public ViewHolder(View itemView){
            super(itemView);
            cardImage = itemView.findViewById(R.id.imageCard);
        }
    }

}
