package com.example.blackjack_markotterson;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    //stack of cards
    private Stack<Card> deckList = new Stack<Card>();

    //deck constructor; fill the deck with cards, shuffle deck
    public Deck(){
        fillDeck();
        shuffleDeck();
    }

    public void push(Card argCard){
        deckList.push(argCard);
    }

    //fill the deck with ace->king of each suit
    private void fillDeck(){
        addAllCardsofSuit(Card.Suit.clubs);
        addAllCardsofSuit(Card.Suit.diamonds);
        addAllCardsofSuit(Card.Suit.spades);
        addAllCardsofSuit(Card.Suit.hearts);
    }

    //add all 13 card ranks of the provided suit
    private void addAllCardsofSuit(Card.Suit argSuit){
        for(int i=0; i<=12; i++) {
            deckList.push(new Card(Card.Rank.values()[i], argSuit));
        }
    }

    //shuffle the order of elements in the array list
    public void shuffleDeck(){
        Collections.shuffle(deckList);
    }

    //draw a card
    public Card drawCard(){
        return deckList.pop();
    }

    //reset the contents of the deck
    private void resetDeck(){
        deckList.clear();
    }


}
