package com.example.blackjack_markotterson;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {
    private int amtBet;
    private ChipConfiguration chipConfiguration = new ChipConfiguration();
    private LayoutParams params = new LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
    );

    public void setAmtBet(int argBet){
        this.amtBet = argBet;
        chipConfiguration.setChipValueConfiguration(argBet);
    }

    public ChipAdapter(int argBet){
        this.amtBet = argBet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(position==0){
            params.bottomMargin=0;
        }
        else{
            params.bottomMargin=-56;
        }
        holder.linearLayout.setLayoutParams(params);
        if(chipConfiguration.getChipValueArrayList().get(position)==ChipConfiguration.ChipValue.ten){
            holder.chipImage.setImageResource(R.drawable.chip10);
        }
        else if(chipConfiguration.getChipValueArrayList().get(position)==ChipConfiguration.ChipValue.five){
            holder.chipImage.setImageResource(R.drawable.chip5);
        }
        else{
            holder.chipImage.setImageResource(R.drawable.chip);
        }
    }

    @Override
    public int getItemCount(){
        return chipConfiguration.getChipValueArrayList().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView chipImage;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView){
            super(itemView);
            chipImage = itemView.findViewById(R.id.imageChip);
            linearLayout = itemView.findViewById(R.id.linearLayoutChipItem);
        }
    }
}
