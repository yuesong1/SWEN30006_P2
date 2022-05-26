package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.player.Player;

public class SmartStrategy implements IPlayStrategy{

    @Override
    public Card nextPlay(Player player, CurrentRound currentRound) {
        player.getHand().sort(Hand.SortType.RANKPRIORITY, false);
        return player.getHand().getFirst();


    }
}
