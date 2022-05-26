package oh_heaven.game.strategy;

import ch.aplu.jcardgame.Card;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.player.Player;

public interface Strategy {

    public Card nextPlay(Player player, CurrentRound currentRound);

}
