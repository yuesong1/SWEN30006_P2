package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import oh_heaven.game.CardUtility;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.Oh_Heaven;
import oh_heaven.game.player.Player;

import java.util.ArrayList;

public class LegalStrategy implements Strategy {
    @Override
    public Card nextPlay(Player player, CurrentRound currentRound) {
        // follows game rule to play the cards with the same suit as the lead

        //check if current player is leading, if leading, chose random card
        if (currentRound.getLead() == null) {
            return CardUtility.randomCard(player.getHand());
        }

        ArrayList<Card> sameSuitAsLead = player.getHand().getCardsWithSuit(currentRound.getLead());

        if (sameSuitAsLead.size() > 0) {
            return CardUtility.randomCard(sameSuitAsLead);
        } else {
            return CardUtility.randomCard(player.getHand());
        }
    }
}
