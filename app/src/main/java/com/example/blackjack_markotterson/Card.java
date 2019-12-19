package com.example.blackjack_markotterson;

public class Card {
    final private Rank rank;
    final private Suit suit;
    private boolean isRevealed = false;

    public enum Rank {
        ace, deuce, three, four, five, six, seven, eight, nine, ten, jack, queen, king;
    }

    public enum Suit {
        hearts, diamonds, spades, clubs;
    }

    public Card(Rank argRank, Suit argSuit){
        this.rank = argRank;
        this.suit = argSuit;
    }

    public Suit getSuit(){
        return this.suit;
    }

    public Rank getRank(){
        return this.rank;
    }


    public void showCard(){
        this.isRevealed = true;
    }

    public void hideCard() {
        this.isRevealed = false;
    }

    public String getCardImage(){
        if(this.isRevealed){
            return this.rank + "_of_" + this.suit;
        }
        else{
            return "card_back";
        }
    }

    public String toString(){
        return rank + " " + suit;
    }
}
