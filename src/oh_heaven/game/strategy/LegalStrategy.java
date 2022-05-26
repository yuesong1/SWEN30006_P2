package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.Oh_Heaven;
import oh_heaven.game.player.Player;

import java.util.ArrayList;

public class LegalStrategy implements IPlayStrategy {
    @Override
    public Card nextPlay(Player player, CurrentRound currentRound) {
        // follows game rule to play the cards with the same suit as the lead

        //check if lead
        if (currentRound.getLead() == null) {
            return Oh_Heaven.randomCard(player.getHand());
        }

        ArrayList<Card> sameSuitAsLead = player.getHand().getCardsWithSuit(currentRound.getLead());

        if (sameSuitAsLead.size() > 0) {
            return Oh_Heaven.randomCard(sameSuitAsLead);
        } else {
            return Oh_Heaven.randomCard(player.getHand());
        }
    }
}
