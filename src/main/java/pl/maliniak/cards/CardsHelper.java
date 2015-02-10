package pl.maliniak.cards;

import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * for bucket computations and more.
 * @author meliniak
 */
public class CardsHelper {

    private static Random rnd = new Random();

    /**
     * @param cardinal
     * @param tableCards
     * @return number of cards on the table, higher than the provided card.
     */
    public static int cardsHigher(Cardinal cardinal, TableCards tableCards) {
        int higherCards = 0;

        if(tableCards.size() == 0) {
            throw new IllegalArgumentException();
        }

        for(Card c : tableCards) {
            if(cardinal.compareTo(c.getCardinal()) < 0) {
                higherCards++;
            }
        }

        return higherCards;
    }

    public static int cardsLower(Cardinal cardinal, TableCards tableCards) {
        int lowerCards = 0;

        if(tableCards.size() == 0) {
            throw new IllegalArgumentException();
        }

        for(Card c : tableCards) {
            if(cardinal.compareTo(c.getCardinal()) > 0) {
                lowerCards++;
            }
        }

        return lowerCards;
    }

    public static HoleCardsSet generatePossibleHoleCards(TableCards tc) {
        HoleCardsSet hcs = new HoleCardsSet();

        List<Card> cards = Deck.getInstance().getCards();
        List<Card> cardList = new ArrayList<Card>(cards);

        for(int i = 0; i < cardList.size(); i++) {
            Card c1 = cardList.get(i);

            if(tc.contains(c1)) {
                continue;
            }

            for(int j = i; j < cardList.size(); j++) {
                Card c2 = cardList.get(j);

                if(c1.equals(c2)) {
                    continue;
                }

                if(tc.contains(c2)) {
                    continue;
                }

                hcs.add(new HoleCards(c1, c2));
            }
        }

        return hcs;
    }

    public static HoleCardsSet generateSuitedHoleCardsSet(Suit suit, TableCards tc) {
        HoleCardsSet hcs = new HoleCardsSet();

        for(Cardinal c1 : Cardinal.valuesNoX()) {
            Card card1 = new Card(c1, suit);

            if(tc.contains(card1)) {
                continue;
            }

            for(Cardinal c2 : Cardinal.valuesNoX()) {
                if(c1.equals(c2)) {
                    continue;
                }

                Card card2 = new Card(c2, suit);
                if(tc.contains(card2)) {
                    continue;
                }

                hcs.add(new HoleCards(card1, card2));
            }
        }

        return hcs;
    }

    public static HoleCardsSet generateXSuitHoleCardsSet(Suit suit, TableCards tc) {
        HoleCardsSet hcs = new HoleCardsSet();

        for(Cardinal c1 : Cardinal.valuesNoX()) {
            Card card1 = new Card(c1, suit);

            if(tc.contains(card1)) {
                continue;
            }

            for(Cardinal c2 : Cardinal.valuesNoX()) {
                for(Suit suit2 : Suit.valuesNoX()) {
                    if(suit.equals(suit2)) {
                        continue;
                    }

                    Card card2 = new Card(c2, suit2);
                    if(tc.contains(card2)) {
                        continue;
                    }

                    hcs.add(new HoleCards(card1, card2));
                }
            }
        }

        return hcs;
    }

    public static HoleCardsSet generate1CardHoleCardsSet(Card card, TableCards tc) {
        HoleCardsSet hcs = new HoleCardsSet();
        List<Card> cardSet = Deck.getInstance().getCards();

        if(tc.contains(card)) {
            throw new IllegalArgumentException("already belongs to table cards?");
        }
        
        for(Card c : cardSet) {
            if(tc.contains(c)) {
                continue;
            }

            if(c.equals(card)) {
                continue;
            }

            HoleCards hc = new HoleCards(card, c);
            hcs.add(hc);
        }

        return hcs;
    }
}
