package oh_heaven.game.player;

import ch.aplu.jcardgame.Card;
import oh_heaven.game.CurrentRound;
import oh_heaven.game.strategy.IPlayStrategy;
import oh_heaven.game.strategy.StrategyFactory;

public class NPC extends Player {

    private IPlayStrategy playStrategy;

    private StrategyFactory strategyFactory = StrategyFactory.getInstance();
    // act as the context object in an oh_heaven.game.strategy pattern structure

    public NPC(int index, String playStrategyType) {
        super(index);
        // if a factory pattern is used, it increases the cohesion of NPC and
        // factory classes
        this.playStrategy = strategyFactory.createStrategy(playStrategyType);


        // initialise play strategy based on the type
        //if (playStrategyType.equals("random")) {
            //playStrategy = new RandomStrategy();
        //} else if() {

        }

    @Override
    public Card playCard(CurrentRound currentRound) {
        return playStrategy.nextPlay(this, currentRound);
    }
}
