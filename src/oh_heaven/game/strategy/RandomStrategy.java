package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.CardUtility;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.Oh_Heaven;
import oh_heaven.game.player.Player;

public class RandomStrategy implements IPlayStrategy {

    @Override
    public Card nextPlay(Player player, CurrentRound currentRound) {
        // randomly selects a card from the hand
        Hand hand = player.getHand();
        return CardUtility.randomCard(hand);
    }
}
