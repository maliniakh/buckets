package pl.maliniak.poker.cardsets;

import game.eval.stevebrecher.HandEval;
import game.eval.stevebrecher.SBCard;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;

import java.io.Serializable;
import java.util.*;

/**
 * card set base class.
 * hash() and equals() work the way that they use sets instead of lists. that way ACS is order indifferent.
 * @author maliniak
 */
public abstract class AbstractCardSet implements Serializable, Iterable<Card> {

    protected List<Card> cards = null;

    /**
     * @return max number of cards.
     */
    public abstract int capacity();

    public AbstractCardSet() {
        this.cards = new ArrayList<>();
    }

    /**
     * @param str string form like Tc2cAcKh
     */
    public AbstractCardSet(String str) {
        this();

        if (str.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }

        // max length for river cards
        if (str.length() > 5 * 2) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < str.length(); i += 2) {
            String cardStr = str.substring(i, i + 2);

            Card c = new Card(cardStr);
            this.add(c);
        }
    }

    /**
     * @return card with the highest cardinal.
     */
    public Card getHighestCard() {
        Card highestCard = null;

        for (Card c : this.cards) {
            if (highestCard == null) {
                highestCard = c;
            }

            if (c.compareTo(highestCard) > 0) {
                highestCard = c;
            }
        }

        return highestCard;
    }

    /**
     * @return card with lowest cardinal.
     */
    public Card getLowestCard() {
        Card lowestCard = null;

        for (Card c : this.cards) {
            if (lowestCard == null) {
                lowestCard = c;
            }

            if (c.compareTo(lowestCard) < 0) {
                lowestCard = c;
            }
        }

        return lowestCard;
    }

    /**
     * @param index
     * @return card with given index in terms of its value (A A 4 K, index 0 -> A, 1 -> K, 2 -> 4). null if index is
     * out of range.
     */
    public Cardinal getCardinalIndex(int index) {
        List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);

        if (index > cardinalsSorted.size() - 1) {
            return null;
        }

        return cardinalsSorted.get(index);
    }

    /**
     * @param unique whether cardinals need to be unique or might be repeated.
     * @return
     */
    public List<Cardinal> getCardinalsSorted(boolean unique) {
        Collection<Cardinal> cardinals;

        if (unique) {
            cardinals = new HashSet<>();
        } else {
            cardinals = new ArrayList<>();
        }

        for (Card c : this.cards) {
            cardinals.add(c.getCardinal());
        }

        List<Cardinal> cardinalsSorted;
        cardinalsSorted = new ArrayList<>(cardinals);

        Collections.sort(cardinalsSorted);

        return cardinalsSorted;
    }

    /**
     * @return colors to number of occurences in set.
     */
    public Suit2NumberMap getSuit2NumberMap() {
        Suit2NumberMap map = new Suit2NumberMap();

        for (Suit suit : Suit.valuesNoX()) {
            map.put(suit, 0);
        }

        Integer number;
        for (Card card : this) {
            number = map.get(card.getSuit());

            map.put(card.getSuit(), number + 1);
        }

        return map;
    }

    public synchronized boolean add(Card c) {
        if (this.cards.size() > this.capacity()) {
            throw new IllegalStateException("max number of cards exceeded");
        }

        // same card cannot be added to the set, except for the situation, when it's x
        if (this.cards.contains(c) && c.getCardinal() != Cardinal.X && c.getSuit() != Suit.X) {
            throw new IllegalArgumentException("card already added to the set");
        }

        return cards.add(c);
    }

    public synchronized boolean add(AbstractCardSet cardSet) {
        boolean returnValue = true;

        for (Card c : cardSet) {
            returnValue &= this.add(c);
        }

        return returnValue;
    }

    /**
     * @return
     */
    public Rank getRank() {
        SBCard[] sbCardArray = this.toSbCardArray();
        long encodedCards = HandEval.encode(sbCardArray);
        int rankValue;
        switch (this.cards.size()) {
            case 5:
                rankValue = HandEval.hand5Eval(encodedCards);
                break;
            case 6:
                rankValue = HandEval.hand6Eval(encodedCards);
                break;
            case 7:
                rankValue = HandEval.hand7Eval(encodedCards);
                break;
            default:
                throw new IllegalStateException("not a legit number of cards to evaluate it");
        }

        return new Rank(rankValue);
    }

    /**
     * @return flush color. throws an exception if there is no flush.
     */
    public Suit getFlushSuit() {
        Rank rank = this.getRank();
        RankEnum pokerRank = rank.getPokerRank();

        if (pokerRank != RankEnum.FLUSH) {
            throw new IllegalStateException();
        }

        Suit2NumberMap suit2NumberMap = this.getSuit2NumberMap();
        Set<Suit> maxSuitSet = suit2NumberMap.getMaxSuitSet();

        assert maxSuitSet.size() == 1;

        Suit flushSuit = maxSuitSet.iterator().next();
        return flushSuit;
    }

    /**
     * @return Tablice kart
     */
    private SBCard[] toSbCardArray() {
        List<SBCard> cardVector = new ArrayList<>();

        for (Card c : this.cards) {
            cardVector.add(c.toSBCard());
        }

        final SBCard[] cardArray = cardVector.toArray(new SBCard[0]);
        return cardArray;
    }

    /**
     * @param c
     * @return number of occurences.
     */
    public int getCardinalCount(Cardinal c) {
        int count = 0;

        for (Card card : this) {
            if (c == card.getCardinal()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Uooooo. Bez przesady...
     * @param <T>
     * @return new instances of this class.
     */
    public <T extends AbstractCardSet> T newInstance() {
        try {
            // czyli dostajemy taka klase, dla ktorej metoda jest wolana.
            @SuppressWarnings({"unchecked"})
            T t = (T) this.getClass().newInstance();

            t.cards = new ArrayList<>(this.cards);

            return t;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public synchronized Card get(int index) {
        return cards.get(index);
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public synchronized int size() {
        return cards.size();
    }

    /**
     * @param cardinal
     * @return
     */
    public boolean contains(Cardinal cardinal) {
        for (Card card : this) {
            if (card.getCardinal() == cardinal) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param card
     * @return
     */
    public boolean contains(Card card) {
        for (Card c : this) {
            if (c.equals(card)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractCardSet other = (AbstractCardSet) obj;
        if (this.cards != other.cards && (this.cards == null || !new HashSet<Card>(this.cards).equals(new HashSet<Card>(other.cards)))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.cards != null ? new HashSet<>(this.cards).hashCode() : 0);
        return hash;
    }

    @Override
    public synchronized String toString() {
        String string = new String();

        for (Card card : this.cards) {
            string += card + "";
        }

        string = string.trim();
        return string;
    }
}
