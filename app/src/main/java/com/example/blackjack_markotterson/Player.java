package com.example.blackjack_markotterson;

public class Player {
    private double balance=100;
    private Hand currentHand;
    private Hand splitHand;
    private int wins;
    private int losses;
    private int draws;

    public Player(){
        currentHand = new Hand();
        splitHand = new Hand();
    }

    //get information regarding a player's chip total
    public double getBalance() {
        return balance;
    }

    public Card getCardAt(int position){
        return currentHand.getCardinHandAt(position);
    }

    public Card getSplitHandCardAt(int position){
        return splitHand.getCardinHandAt(position);
    }

    public Hand getHand(){
        return currentHand;
    }

    public Hand getSplitHand(){
        return splitHand;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public void giveWin(){
        wins++;
    }
    public void giveLoss(){
        losses++;
    }
    public void giveDraw(){
        draws++;
    }

    public void addCardToHand(Card argCard){
        currentHand.addCard(argCard);
    }

    public void addCardToSplitHand(Card argCard){
        splitHand.addCard(argCard);
    }

    public void addBalance(double value){
        balance=balance+value;
    }

    public void subtractBalance(double value){
        balance=balance-value;
    }
}
