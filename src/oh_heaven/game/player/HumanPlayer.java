package oh_heaven.game.player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import oh_heaven.game.CurrentRound;

public class HumanPlayer extends Player {

    private Card selected;

    public HumanPlayer(int index) {
        super(index);
    }

    @Override
    public Card playCard(CurrentRound currentRound) {
        selected = null;
        hand.setTouchEnabled(true);

        //delay
        hand.setTouchEnabled(false);
        //TODO: auto-generated method stub
        return null;
    }

    public void setupCardListener() {
        // Set up human player for interaction
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) {
                selected = card;
                hand.setTouchEnabled(false);
            }
        };
        hand.addCardListener(cardListener);
    }

}
