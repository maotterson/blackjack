package com.example.blackjack_markotterson;

import java.util.ArrayList;

public class ChipConfiguration
{
    private ArrayList<ChipValue> chipValueArrayList = new ArrayList<ChipValue>();

    public enum ChipValue{
        ten(10),five(5),one(1);

        private int value;

        public int getValue(){
            return value;
        }

        private ChipValue(int argValue){
            value = argValue;
        }
    }

    public void setChipValueConfiguration(int argBetAmt){
        int remaining=argBetAmt;
        chipValueArrayList.clear();
        for (ChipValue value : ChipValue.values()){
            int numChipsOfType = remaining/value.getValue();
            for(int i=0;i<numChipsOfType;i++){
                chipValueArrayList.add(value);
            }
            remaining=remaining%value.getValue();
        }
    }

    public ArrayList<ChipValue> getChipValueArrayList(){
        return chipValueArrayList;
    }

    public int getChipSum(){
        int sum = 0;
        for(ChipValue value : chipValueArrayList){
            sum+=value.getValue();
        }
        return sum;
    }
}
