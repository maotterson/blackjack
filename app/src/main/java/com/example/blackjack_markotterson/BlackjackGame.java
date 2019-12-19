package com.example.blackjack_markotterson;

public class BlackjackGame {
    private Deck deck;
    private RuleBook ruleBook;
    private Player person;
    private Player house;
    private Result result;
    private GameState gameState=GameState.Start;
    private boolean isDoubledDown=false;
    private boolean isSplit=false;
    private boolean isHandlingSplitHand=false;
    private double bet;
    private double sideBet;

    public enum GameState {
        Start,Hit,Normal,Split,Doubled,Hold;
    }

    public enum Result {
        Win,Lose,Push;
    }

    public enum Choice {
        Deal,Hit,Stay,Split,DoubleDown,Surrender;
    }

    public BlackjackGame(){
        this.deck = new Deck();
        this.person = new Player();
        this.house = new Player();
        this.ruleBook = new RuleBook();
    }
    public Result getResult() {return result;}

    public Player getPerson(){
        return person;
    }

    public Player getHouse(){
        return house;
    }

    public RuleBook getRuleBook(){
        return ruleBook;
    }
    public boolean getIsHandlingSplitHand() {
        return isHandlingSplitHand;
    }

    public GameState getGameState(){
        return gameState;
    }

    public double getBet(){
        return this.bet;
    }

    public void setGameState(GameState arg){
        this.gameState = arg;
    }

    public void setResult(Result arg){
        this.result = arg;
    }
    //===========================================================

    public void initializeGame(double argBet){
        gameState = GameState.Normal;
        result = null;
        this.bet = argBet;
        person.subtractBalance(bet);

        //deal two cards to each player
        dealHouseCards();

        person.addCardToHand(dealCard());
        person.addCardToHand(dealCard());
        person.getHand().getCardinHandAt(0).showCard();
        person.getHand().getCardinHandAt(1).showCard();

        //21 on draw = immediate win
        if(ruleBook.determineValue(person.getHand())==21){
            result = Result.Win;
            rewardWinner();
        }
    }

    public void dealHouseCards(){
        //deal two cards to each player
        house.addCardToHand(dealCard());
        house.addCardToHand(dealCard());
        house.getHand().getCardinHandAt(1).showCard();
    }
    //==========================================================

    public Card dealCard(){
        Card dealtCard = deck.drawCard();
        return dealtCard;
    }

    public void play(){
        if(gameState == GameState.Doubled){
            isDoubledDown = true;
            doubleDown();
        }
        else if(gameState == GameState.Split){
            isSplit = true;
            split();
        }
        else if(gameState == GameState.Hit){
            hit();
        }
        else if(gameState == GameState.Hold){
            ruleBook.houseDraw(this);
        }

    }

    public void hit(){
        Hand hand;
        Card receivedCard = dealCard();
        receivedCard.showCard();

        //see if we are working with the player's split hand
        if(isHandlingSplitHand){
            person.addCardToSplitHand(receivedCard);
            hand=person.getSplitHand();
        }
        else{
            person.addCardToHand(receivedCard);
            hand=person.getHand();
        }


        Hand.HandState currentHandState = ruleBook.determineHandState(hand);


        if(currentHandState == Hand.HandState.Bust)
        {
            result=Result.Lose;
            rewardWinner();
        }

        else if(currentHandState == Hand.HandState.BlackJack){
            ruleBook.houseDraw(this);
        }

        //if they doubled down, they can't hit more than once
        if(isDoubledDown){
            ruleBook.houseDraw(this);
        }
    }

    public void split(){
        //move the second dealt card from their first hand to their split hand
        person.addCardToSplitHand(person.getHand().getCardinHandAt(1));
        person.getHand().removeCardinHandAt(1);

        //the user also places an additional bet
        person.subtractBalance(bet);
    }

    public void doubleDown(){
        //effectively double the bet
        person.subtractBalance(bet);
        this.bet=bet*2;
    }

    public void surrender(){
        result = Result.Lose;
        rewardWinner();
        resetState();
    }

    public void rewardWinner(){
        if(result == Result.Win){
            //blackjacks pay out 150% bet
            if(person.getHand().getHandState()== Hand.HandState.BlackJack){
                person.addBalance(2.5*bet);
            }
            else{
                person.addBalance(2*bet);
            }
            person.giveWin();
        }
        //in the case of house rules, a push will result in losing money as well
        else if(result == Result.Push){
            person.giveDraw();
        }
        else{
            person.giveLoss();
        }

        //see if there is a split hand to deal with
        if(isSplit){
            gameState = GameState.Normal;
            isHandlingSplitHand=true;
            isSplit = false;
        }
        else{
            gameState = GameState.Start;
            isDoubledDown=false;
            isHandlingSplitHand=false;
        }

    }

    //==============================================================
    public void clearHands(){
        isSplit=false;
        isDoubledDown=false;
        //return copies of each player's cards to deck
        for(int i=0; i<house.getHand().getHandSize(); i++){
            Card currentCard = house.getHand().getCardinHandAt(i);
            currentCard.hideCard();
            deck.push(currentCard);
        }
        for(int i=0; i<house.getSplitHand().getHandSize(); i++){
            Card currentCard = house.getHand().getCardinHandAt(i);
            currentCard.hideCard();
            deck.push(currentCard);
        }
        for(int i=0; i<person.getHand().getHandSize(); i++){
            Card currentCard = person.getHand().getCardinHandAt(i);
            currentCard.hideCard();
            deck.push(currentCard);
        }

        //clear the player hands
        house.getHand().clear();
        person.getSplitHand().clear();
        person.getHand().clear();

    }

    public void resetState(){
        gameState = GameState.Start;
        clearHands();
        deck.shuffleDeck();

    }
}
