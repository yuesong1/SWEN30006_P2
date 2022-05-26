package oh_heaven.game;
import ch.aplu.jcardgame.*;

import java.util.*;

public class CardUtility {
    static public final int seed = 30006;
    static final Random random = new Random(seed);
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

    public static boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
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
