package com.example.blackjack_markotterson;

import java.util.ArrayList;

public class RuleBook {

    public int[] getValues(Card card){
        switch(card.getRank()){
            case ace:
                return new int[]{1,11};
            case deuce:
                return new int[]{2};
            case three:
                return new int[]{3};
            case four:
                return new int[]{4};
            case five:
                return new int[]{5};
            case six:
                return new int[]{6};
            case seven:
                return new int[]{7};
            case eight:
                return new int[]{8};
            case nine:
                return new int[]{9};
            default:
                return new int[]{10};
        }
    }

    public int determineValue(Hand hand){
        ArrayList<Integer> handValuePossibilities=new ArrayList<Integer>();
        //initialize the list of possible hand values with a value of 0 as the only current possible hand value
        handValuePossibilities.add(0,0);
        Card currentCard;

        //loop through each card in the hand
        for(int i=0; i<hand.getHandSize(); i++){
            currentCard = hand.getCardinHandAt(i);

            //get all the current values from value array
            for(int j=0; j<handValuePossibilities.size(); j++){
                //set each of the current existing values within the possible values array equal to the values plus the lowest possible value associated with that card (for aces, this will add one)
                handValuePossibilities.set(j,handValuePossibilities.get(j)+getValues(currentCard)[0]);
            }

            //check to see if the card is an ace so we can also account for the possible values with an ace worth 11 points
            if(currentCard.getRank() == Card.Rank.ace){
                int possibilities = handValuePossibilities.size();
                for(int j=0; j<possibilities; j++){
                    //duplicate the existing values within the possibilities array, recalculating the values where an ace is worth 11 points instead of 1
                    handValuePossibilities.add(j,handValuePossibilities.get(j)+getValues(currentCard)[1]-1);
                }
            }
        }

        //loop through the list of value possibilities and find the max without exceeding 21
        //note: if currentMaxValue stays 0, that indicates that the player busted
        int currentMaxValue = 0;
        for (int i=0; i<handValuePossibilities.size(); i++){
            int currentPossibleValue = handValuePossibilities.get(i);
            if(currentPossibleValue>currentMaxValue && currentPossibleValue<22){
                currentMaxValue = currentPossibleValue;
            }
        }

        return currentMaxValue;
    }

    //determine the state of the hand (e.g. busted, blackjack, stay)
    public Hand.HandState determineHandState(Hand hand){
        int handValue = determineValue(hand);
        if(handValue == 21){
            return Hand.HandState.BlackJack;
        }
        else if(handValue == 0){
            return Hand.HandState.Bust;
        }
        else{
            return Hand.HandState.Normal;
        }
    }

    //========================================================
    //computer draw logic

    public void houseDraw(BlackjackGame game){
        Hand houseHand =  game.getHouse().getHand();
        houseHand.getCardinHandAt(0).showCard();
        if(determineValue(houseHand)<17 && determineValue(houseHand)!=0){
            Card receivedCard = game.dealCard();
            receivedCard.showCard();
            houseHand.addCard(receivedCard);
            houseDraw(game);
        }
        else{
            determineWinner(game);
        }
    }


    public void determineWinner(BlackjackGame game){
        Hand hand;
        if(game.getIsHandlingSplitHand()){
            hand=game.getPerson().getSplitHand();
        }
        else{
            hand=game.getPerson().getHand();
        }

        if(determineValue(hand)>determineValue(game.getHouse().getHand())){
            game.setResult(BlackjackGame.Result.Win);
        }
        else if(determineValue(hand)==determineValue(game.getHouse().getHand())){
            game.setResult(BlackjackGame.Result.Push);
        }
        else{
            game.setResult(BlackjackGame.Result.Lose);
        }

        game.rewardWinner();
    }
}
