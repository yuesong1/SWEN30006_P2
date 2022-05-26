package oh_heaven.game.player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.CurrentRound;

public abstract class Player {

    protected int index;
    protected int score;
    protected int trick;
    protected int bid;

    protected Hand hand;

    // index = 第几个玩家
    public Player(int index) {
        this.index =index;
    }

    public abstract Card playCard(CurrentRound currentRound);

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTrick() {
        return trick;
    }

    public void setTrick(int trick) {
        this.trick = trick;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}