package oh_heaven.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import oh_heaven.game.player.HumanPlayer;
import oh_heaven.game.player.NPC;
import oh_heaven.game.player.Player;

import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {

    static public final int seed = 30006;
    static final Random random = new Random(seed);
    public final int nbPlayers = 4;
    public int nbStartCards = 13;
    public int nbRounds = 3;
    public int madeBidBonus = 10;
    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};
    private final String version = "1.0";
    private final int handWidth = 400;
    private final int trickWidth = 40;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };
    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(575, 25),
            // new Location(650, 575)
            new Location(575, 575)
    };
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private int thinkingTime = 2000;
    Font bigFont = new Font("Serif", Font.BOLD, 36);
    private Actor[] scoreActors = {null, null, null, null};
    private Hand[] hands;
    private Location hideLocation = new Location(-500, -500);
    private Location trumpsActorLocation = new Location(50, 50);
    private boolean enforceRules = false;
    //所有游戏相关数据都被保存在里面
    private int[] scores = new int[nbPlayers];
    private int[] tricks = new int[nbPlayers];
    private int[] bids = new int[nbPlayers];
    private Card selected;

    // use an arrayList to store scores, tricks and bids
    private List<Player> players = new ArrayList<>();

    private void setupPlayers(Properties properties) {


        for (int i = 0; i < nbPlayers; i++) {
            // task 3 load player properties
            String type =properties.getProperty(String.format("players.%d",i)) ;
            if(type.equals("human")){
                players.add(new HumanPlayer(i));
            }
            if(!type.equals("human")){
                players.add(new NPC(i, type));
            }

            // originally initScore()
            // scores[i] = 0;
            String text = "[" + String.valueOf(scores[i]) + "]" + String.valueOf(tricks[i]) +
                    "/" + String.valueOf(bids[i]);
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);

        }
    }

    public Oh_Heaven(Properties properties)
    // full gameplay sequence
    {
        super(700, 700, 30);
        setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        parseProperties(properties);
        setupPlayers(properties); // newly added for task 1

        initScores();
        for (int i = 0; i < nbRounds; i++) {
            initTricks();
            initRound();
            playRound();
            updateScores();
        }
        ;
        for (int i = 0; i < nbPlayers; i++) updateScoreGraphics(i);
        int maxScore = 0;
        for (int i = 0; i < nbPlayers; i++) if (scores[i] > maxScore) maxScore = scores[i];
        Set<Integer> winners = new HashSet<Integer>();
        for (int i = 0; i < nbPlayers; i++) if (scores[i] == maxScore) winners.add(i);
        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText(winText);
        refresh();
    }

    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return smallest Card from hand, for task 3
    public static Card smallestCard(ArrayList<Card> cards) {
        if(cards.isEmpty()) return null;
        Card smallest=cards.get(0);
        for (Card c:cards) {
            if(rankGreater(c,smallest)){
                smallest=c;
            }
        }
        return smallest;
    }

    // return biggest Card from hand, for task 3
    public static Card biggestCard(ArrayList<Card> cards) {
        if(cards.isEmpty()) return null;
        Card biggest=cards.get(0);
        for (Card c:cards) {
            if(!rankGreater(c,biggest)){
                biggest=c;
            }
        }
        return biggest;
    }
    // return random Card from Hand
    public static Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    //initialise property elements
    private void parseProperties(Properties properties) {
        this.nbStartCards = properties.getProperty("nbStartCards") == null ? nbStartCards
                : Integer.parseInt(properties.getProperty("nbStartCards"));
        this.nbRounds = properties.getProperty("rounds") == null ? nbRounds :
                Integer.parseInt(properties.getProperty("rounds"));
        this.enforceRules = properties.getProperty("enforceRules") == null ? enforceRules :
                Boolean.parseBoolean(properties.getProperty("enforceRules"));
        this.thinkingTime = properties.getProperty("thinkingTime") == null ? thinkingTime :
                Integer.parseInt(properties.getProperty("thinkingTime"));
        this.madeBidBonus = properties.getProperty("bonus") == null ? madeBidBonus :
                Integer.parseInt(properties.getProperty("bonus"));
    }
    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        final Properties properties;
        if (args == null || args.length == 0) {
            properties = PropertiesLoader.loadPropertiesFile(null);
        } else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }
        new Oh_Heaven(properties);
    }

    private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer) {
        Hand pack = deck.toHand(false);
        // pack.setView(Oh_Heaven.this, new RowLayout(hideLocation, 0));
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j = 0; j < nbPlayers; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack);
                // System.out.println("Cards = " + dealt);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
                // dealt.transfer(hands[j], true);
            }
        }
    }

    public static boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    public void setStatus(String string) {
        setStatusText(string);
    }



    private void updateScoreGraphics(int player) {
        removeActor(scoreActors[player]);
        String text = "[" + String.valueOf(scores[player]) + "]" + String.valueOf(tricks[player]) + "/" + String.valueOf(bids[player]);
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void initScores() {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] = 0;
        }
    }

    private void updateScores() {
        for (int i = 0; i < nbPlayers; i++) {
            scores[i] += tricks[i];
            if (tricks[i] == bids[i]) scores[i] += madeBidBonus;
        }
    }

    private void initTricks() {
        for (int i = 0; i < nbPlayers; i++) {
            tricks[i] = 0;
        }
    }

    private void initBids(Suit trumps, int nextPlayer) {
        int total = 0;
        for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
            int iP = i % nbPlayers;
            bids[iP] = nbStartCards / 4 + random.nextInt(2);
            total += bids[iP];
        }
        if (total == nbStartCards) {  // Force last bid so not every bid possible
            int iP = (nextPlayer + nbPlayers) % nbPlayers;
            if (bids[iP] == 0) {
                bids[iP] = 1;
            } else {
                bids[iP] += random.nextBoolean() ? -1 : 1;
            }
        }
        // for (int i = 0; i < nbPlayers; i++) {
        // 	 bids[i] = nbStartCards / 4 + 1;
        //  }
    }

    private void initRound() {
        hands = new Hand[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            hands[i] = new Hand(deck);
            // create hands for each player
            players.get(i).setHand(hands[i]);
        }
        dealingOut(hands, nbPlayers, nbStartCards);
        for (int i = 0; i < nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
        }

        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                //set up double click to play for human player
                ((HumanPlayer) player).setupCardListener();
            }
        }

        // graphics
        RowLayout[] layouts = new RowLayout[nbPlayers];
        for (int i = 0; i < nbPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].setTargetArea(new TargetArea(trickLocation));
            hands[i].draw();
        }
//	    for (int i = 1; i < nbPlayers; i++) // This code can be used to visually hide the cards in a hand (make them face down)
//	      hands[i].setVerso(true);			// You do not need to use or change this code.
        // End graphics
    }

    private void playRound() {
        // Select and display trump suit
        final Suit trumps = randomEnum(Suit.class);
        final Actor trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
        addActor(trumpsActor, trumpsActorLocation);
        // End trump suit

        CurrentRound currentRound = new CurrentRound(trumps);

        Hand trick; // 这一轮出过的牌
        int winner; // 这一轮的赢家
        Card winningCard; // 决胜卡
        Suit lead; // 这轮第一张牌的花色

        int nextPlayer = random.nextInt(nbPlayers); // randomly select player to lead for this round
        initBids(trumps, nextPlayer);
        // initScore();
        for (int i = 0; i < nbPlayers; i++) updateScoreGraphics(i);
        for (int i = 0; i < nbStartCards; i++) {
            trick = new Hand(deck);
            selected = null;
            // if (false) {
            //if (0 == nextPlayer) {  // Select lead depending on player type
                //hands[0].setTouchEnabled(true);
                //setStatus("Player 0 double-click on card to lead.");
                //while (null == selected) delay(100);
            //} else {
                //setStatusText("Player " + nextPlayer + " thinking...");
                //delay(thinkingTime);
                //selected = randomCard(hands[nextPlayer]);
            //}
            // lead
            selected = players.get(nextPlayer).playCard(currentRound);

            // Lead with selected card
            trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
            trick.draw();
            selected.setVerso(false);
            // No restrictions on the card being lead
            lead = (Suit) selected.getSuit();
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            winner = nextPlayer;
            winningCard = selected;
            // End Lead

            // update round information
            currentRound.cardPlayed(nextPlayer, selected);
            currentRound.setLead(lead);
            currentRound.setWinner(winner);
            currentRound.setWinningCard(winningCard);
            // TODO: update round player's score


            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = null;
                // if (false) {
                //if (0 == nextPlayer) {
                    //hands[0].setTouchEnabled(true);
                    //setStatus("Player 0 double-click on card to follow.");
                    //while (null == selected) delay(100);
                //} else {
                    //setStatusText("Player " + nextPlayer + " thinking...");
                    //delay(thinkingTime);
                    //selected = randomCard(hands[nextPlayer]);
                //}
                // replace above with selected
                selected = players.get(nextPlayer).playCard(currentRound);

                // Follow with selected card
                trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards() + 2) * trickWidth));
                trick.draw();
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                if (selected.getSuit() != lead && hands[nextPlayer].getNumberOfCardsWithSuit(lead) > 0) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    System.out.println(violation);
                    if (enforceRules)
                        try {
                            throw (new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }
                // End Check
                selected.transfer(trick, true); // transfer to trick (includes graphic effect)
                System.out.println("winning: " + winningCard);
                System.out.println(" played: " + selected);
                // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
                // System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }

                // update round information
                currentRound.cardPlayed(nextPlayer, selected);
                currentRound.setLead(lead);
                currentRound.setWinner(winner);
                currentRound.setWinningCard(winningCard);
                // TODO: update round player's score

                // End Follow
            }
            delay(600);
            trick.setView(this, new RowLayout(hideLocation, 0));
            trick.draw();
            nextPlayer = winner;
            setStatusText("Player " + nextPlayer + " wins trick.");
            tricks[nextPlayer]++;
            updateScoreGraphics(nextPlayer);
        }
        removeActor(trumpsActor);
    }


    public enum Suit {
        SPADES, HEARTS, DIAMONDS, CLUBS
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO
    }

}
