package oh_heaven.game.strategy;

public class StrategyFactory {

    // TODO: singleton
    private IPlayStrategy strategyToBeCreated;
    public IPlayStrategy createStrategy(String strategyType) {

        public static SimplePlayStrategyFactory getInstance() {
            if (instance == null) {
                instance = new SimplePlayStrategyFactory();
            }
            return instance;
        }

        switch (strategyType) {
            case "random":
                strategyToBeCreated = new RandomStrategy();
            break;
            case "legal":
                strategyToBeCreated = new LegalStrategy();
                break;
            case "smart":
                strategyToBeCreated = new SmartStrategy();
        }
        return strategyToBeCreated;
    }
}
