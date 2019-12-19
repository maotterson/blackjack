package com.example.blackjack_markotterson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.media.MediaPlayer;

import android.os.Bundle;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    //==========================================
    //fields
    private final NumberFormat balanceFormat = NumberFormat.getCurrencyInstance();

    private int bet=0;
    private int splitBet=0;
    private int nextBet=0;

    //adapters
    private HandAdapter humanAdapter;
    private HandAdapter humanSplitAdapter;
    private HandAdapter computerAdapter;

    //chip stack adapters
    private ChipAdapter mainChipAdapter;
    private ChipAdapter splitChipAdapter;

    //player and blackjack game objects
    private Player human;
    private Player computer;
    private BlackjackGame gameInstance;
    private BlackjackGame.GameState gameState;

    //ui controls
    private Button btnDeal;
    private Button btnHit;
    private Button btnStay;
    private Button btnSplit;
    private Button btnDoubleDown;
    private Button btnSurrender;
    private TextView txtComputer;
    private TextView txtHuman;
    private TextView txtResult;
    private TextView txtBalance;
    private TextView txtBetAmt;
    private SeekBar seekBet;

    private MediaPlayer mpCoins;
    private MediaPlayer mpCards;

    //=======================================================
    //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create new game instance, get game state and point to human and computer
        gameInstance = new BlackjackGame();
        gameState = gameInstance.getGameState();

        human = gameInstance.getPerson();
        computer = gameInstance.getHouse();

        //initialize, configure, and display the recycler views for player cards
        LinearLayoutManager layoutManagerHuman = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerHumanSplit = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerComputer = new LinearLayoutManager(this);

        layoutManagerHuman.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerHumanSplit.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerComputer.setOrientation(RecyclerView.HORIZONTAL);

        humanAdapter = new HandAdapter(human.getHand(), this);
        humanSplitAdapter = new HandAdapter(human.getSplitHand(), this);
        computerAdapter = new HandAdapter(computer.getHand(), this);

        RecyclerView humanRecycler = (RecyclerView) findViewById(R.id.humanRecycler);
        RecyclerView humanSplitRecycler = (RecyclerView) findViewById(R.id.humanRecycler2);
        RecyclerView computerRecycler = (RecyclerView) findViewById(R.id.computerRecycler);

        humanRecycler.setLayoutManager(layoutManagerHuman);
        humanSplitRecycler.setLayoutManager(layoutManagerHumanSplit);
        computerRecycler.setLayoutManager(layoutManagerComputer);

        humanRecycler.setAdapter(humanAdapter);
        humanSplitRecycler.setAdapter(humanSplitAdapter);
        computerRecycler.setAdapter(computerAdapter);

        //initialize, configure, and display the recycler views for chip stacks pertaining to bet amount
        LinearLayoutManager layoutManagerChipMain = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerChipSplit = new LinearLayoutManager(this);

        layoutManagerChipMain.setOrientation(RecyclerView.VERTICAL);
        layoutManagerChipMain.setReverseLayout(true);
        layoutManagerChipMain.setStackFromEnd(true);
        layoutManagerChipSplit.setOrientation(RecyclerView.VERTICAL);
        layoutManagerChipSplit.setReverseLayout(true);
        layoutManagerChipSplit.setStackFromEnd(true);

        mainChipAdapter = new ChipAdapter(bet);
        splitChipAdapter = new ChipAdapter(splitBet);

        RecyclerView chipRecycler = (RecyclerView) findViewById(R.id.chipsMain);
        RecyclerView chipSplitRecycler = (RecyclerView) findViewById(R.id.chipsSplit);

        chipRecycler.setLayoutManager(layoutManagerChipMain);
        chipSplitRecycler.setLayoutManager(layoutManagerChipSplit);

        chipRecycler.setAdapter(mainChipAdapter);
        chipSplitRecycler.setAdapter(splitChipAdapter);


        //grab ui controls
        btnDeal = (Button)findViewById(R.id.btnDeal);
        btnHit = (Button)findViewById(R.id.btnHit);
        btnStay = (Button)findViewById(R.id.btnStay);
        btnDoubleDown = (Button)findViewById(R.id.btnDoubleDown);
        btnSplit = (Button)findViewById(R.id.btnSplit);
        btnSurrender = (Button)findViewById(R.id.btnSurrender);
        txtComputer = (TextView)findViewById(R.id.computerValue);
        txtHuman = (TextView)findViewById(R.id.humanValue);
        txtResult = (TextView)findViewById(R.id.txtResult);
        txtBalance = (TextView)findViewById(R.id.txtBalance);
        txtBalance.setText(balanceFormat.format(gameInstance.getPerson().getBalance()));
        txtBetAmt = (TextView)findViewById(R.id.txtBetAmt);

        seekBet = (SeekBar)findViewById(R.id.barBet);
        seekBet.setOnSeekBarChangeListener(seekListener);
        updateBet(seekBet);

        btnDeal.setTag(BlackjackGame.Choice.Deal);
        btnHit.setTag(BlackjackGame.Choice.Hit);
        btnStay.setTag(BlackjackGame.Choice.Stay);
        btnSurrender.setTag(BlackjackGame.Choice.Surrender);
        btnSplit.setTag(BlackjackGame.Choice.Split);
        btnDoubleDown.setTag(BlackjackGame.Choice.DoubleDown);

        btnDeal.setOnClickListener(decisionListener);
        btnHit.setOnClickListener(decisionListener);
        btnStay.setOnClickListener(decisionListener);
        btnSurrender.setOnClickListener(decisionListener);
        btnSplit.setOnClickListener(decisionListener);
        btnDoubleDown.setOnClickListener(decisionListener);

        //mpCards = MediaPlayer.create(this, R.raw.shuffle);
        mpCoins = MediaPlayer.create(this, R.raw.coins);
    }

    //===================================================
    //Listener objects

    OnClickListener decisionListener = new OnClickListener(){
        @Override
        public void onClick(View view) {
            BlackjackGame.Choice choice = (BlackjackGame.Choice)view.getTag();
            play(choice);
            updateValues();
            updateChips();
            notifyAdapters();
        }
    };

    OnSeekBarChangeListener seekListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            updateBet(seekBar);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    //==================================================
    //bet amount -> bet

    public void updateBet(SeekBar seekBar){
        int amount = (seekBar.getProgress()+1)*2;

        NumberFormat betFormat = NumberFormat.getCurrencyInstance();
        txtBetAmt.setText(betFormat.format(amount));
        nextBet = amount;
        updateChips();
    }

    //==================================================
    //button -> gameInstance -> ui communication methods

    public void play(BlackjackGame.Choice choice){
        switch(choice){
            case Deal:
                bet=nextBet;
                //mpCards.start();
                dealNewHand(bet);
                return;
            case Hit:
                gameInstance.setGameState(BlackjackGame.GameState.Hit);
                gameInstance.play();
                break;
            case Stay:
                gameInstance.setGameState(BlackjackGame.GameState.Hold);
                gameInstance.play();
                break;
            case Split:
                splitBet=bet;
                gameInstance.setGameState(BlackjackGame.GameState.Split);
                gameInstance.play();
                break;
            case DoubleDown:
                bet=bet*2;
                gameInstance.setGameState(BlackjackGame.GameState.Doubled);
                gameInstance.play();
                break;
            case Surrender:
                surrender();
                bet=0;
                break;
        }
        getButtonConfiguration();

    }

    public void dealNewHand(double bet){
        splitBet=0;
        txtResult.setText("");

        //deal out the starting game state
        gameInstance.resetState();

        gameInstance.initializeGame(bet);
        txtBalance.setText(balanceFormat.format(gameInstance.getPerson().getBalance()));
        getButtonConfiguration();

        boolean isSplittable = human.getHand().isSplittable();

        //if the user hit a blackjack the game will already be over, hence no opportunity to double down
        if(gameInstance.getGameState() == BlackjackGame.GameState.Normal){
            btnDoubleDown.setEnabled(true);
        }

        if(isSplittable){
            btnSplit.setEnabled(true);
        }
    }

    public void surrender(){
        gameInstance.surrender();
    }

    //========================================
    //ui buttons/display configuration methods
    public void getButtonConfiguration(){
        gameState = gameInstance.getGameState();
        if((gameState == BlackjackGame.GameState.Normal)||(gameState == BlackjackGame.GameState.Split)||(gameState == BlackjackGame.GameState.Doubled)||(gameState == BlackjackGame.GameState.Hit)){
            txtBalance.setText(balanceFormat.format(gameInstance.getPerson().getBalance()));
            continueGameButtons();
        }
        else{
            txtResult.setText(gameInstance.getResult().toString());
            txtBalance.setText(balanceFormat.format(gameInstance.getPerson().getBalance()));
            resetGameButtons();
        }
    }

    public void continueGameButtons(){
        btnDeal.setEnabled(false);
        btnSurrender.setEnabled(true);
        btnHit.setEnabled(true);
        btnStay.setEnabled(true);
        btnDoubleDown.setEnabled(false);
        btnSplit.setEnabled(false);
    }

    public void resetGameButtons(){
        btnDeal.setEnabled(true);
        btnDoubleDown.setEnabled(false);
        btnSurrender.setEnabled(false);
        btnHit.setEnabled(false);

        btnStay.setEnabled(false);
        btnSplit.setEnabled(false);
        if (gameInstance.getResult()== BlackjackGame.Result.Lose){
            if(gameInstance.getIsHandlingSplitHand()){
                splitBet=0;
            }
            else{
                bet=0;
            }
        }
        else if(gameInstance.getResult() == BlackjackGame.Result.Win){
            if(gameInstance.getIsHandlingSplitHand()) {
                splitBet = 2 * splitBet;
            }
            else{
                bet = 2 * bet;
            }
            mpCoins.start();
        }
        updateChips();
    }

    public void notifyAdapters(){
        humanAdapter.notifyDataSetChanged();
        humanSplitAdapter.notifyDataSetChanged();
        computerAdapter.notifyDataSetChanged();
    }

    public void updateChips(){
        mainChipAdapter.setAmtBet(bet);
        mainChipAdapter.notifyDataSetChanged();
        splitChipAdapter.setAmtBet(splitBet);
        splitChipAdapter.notifyDataSetChanged();
    }

    public void updateValues(){
        if(gameInstance.getIsHandlingSplitHand()){
            //txtHuman.setText(Integer.toString(gameInstance.getRuleBook().determineValue(human.getSplitHand())));
        }
        else{
            //txtHuman.setText(Integer.toString(gameInstance.getRuleBook().determineValue(human.getHand())));
        }
    }

}
