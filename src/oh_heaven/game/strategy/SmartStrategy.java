package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.Oh_Heaven;
import oh_heaven.game.player.Player;

import java.util.ArrayList;

public class SmartStrategy implements IPlayStrategy{

    @Override
    public Card nextPlay(Player player, CurrentRound currentRound) {
        player.getHand().sort(Hand.SortType.RANKPRIORITY, false);
        // if smart player is leading, pick one smallest card in hand
        if(currentRound.getLead()==null){
            return Oh_Heaven.smallestCard(player.getHand().getCardList());
        }

        ArrayList<Card> sameSuitAsLead = player.getHand().getCardsWithSuit(currentRound.getLead());
        // if a smart player do not have any card that have a same suit as lead,
        // pick one smallest of card in hand,
        // if they have the same suit, check the winner, if the winner is smaller,
        // pick the biggest card in hand, if the winner is bigger, pick the smallest
        if (sameSuitAsLead.size() > 0) {
            Card biggest=Oh_Heaven.biggestCard(sameSuitAsLead);
            if( Oh_Heaven.rankGreater(biggest,currentRound.getWinningCard()) ){
                return Oh_Heaven.smallestCard(sameSuitAsLead);
            }else{
                return biggest;
            }
        } else {
            return Oh_Heaven.smallestCard(player.getHand().getCardList());
        }
    }
}
