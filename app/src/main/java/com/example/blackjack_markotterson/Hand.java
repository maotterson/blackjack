package com.example.blackjack_markotterson;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand = new ArrayList<Card>();
    private int value;
    private HandState handState;

    public enum HandState{
        Bust,Stay,BlackJack,Normal;
    }

    public Hand(){

    }

    public void setHandState(HandState h){
        handState = h;
    }

    public HandState getHandState(){
        return handState;
    }
    public int getHandSize(){
        return hand.size();
    }

    public Card getCardinHandAt(int position){
        return hand.get(position);
    }

    public void removeCardinHandAt(int position){
        hand.remove(position);
    }

    public void addCard(Card argCard){
        hand.add(argCard);
    }

    public void clear(){
        hand.clear();
    }

    public boolean isSplittable(){
        if(getCardinHandAt(0).getRank() == getCardinHandAt(1).getRank()){
            return true;
        }
        else{
            return false;
        }
    }
}
