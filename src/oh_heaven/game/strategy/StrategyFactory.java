package oh_heaven.game.strategy;

public class StrategyFactory {

    private static StrategyFactory instance;

    // private constructor
    private StrategyFactory() {};

    public static StrategyFactory getInstance() {
        if (instance == null) {
            instance = new StrategyFactory();
        }
        return instance;
    }

    private Strategy strategyToBeCreated;
    public Strategy createStrategy(String strategyType) {

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
