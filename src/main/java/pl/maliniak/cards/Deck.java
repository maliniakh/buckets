package pl.maliniak.cards;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maliniak
 */
public class Deck {
    private static final Logger logger = LoggerFactory.getLogger(Deck.class);

    private List<Card> cardSet;

    private static Deck instance = null;

    private Deck() {
        cardSet = new ArrayList<>();

        for(Cardinal c : Cardinal.valuesNoX()) {
            for(Suit suit : Suit.valuesNoX()) {
                Card card = new Card(c, suit);
                cardSet.add(card);
            }
        }

        cardSet = ImmutableList.copyOf(cardSet);
    }

    public static Deck getInstance() {
        if(instance == null) {
            instance = new Deck();
        }

        return instance;
    }

    public List<Card> getCards() {
        return cardSet;
    }
}
