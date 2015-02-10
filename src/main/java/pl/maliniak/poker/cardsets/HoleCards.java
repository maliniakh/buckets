package pl.maliniak.poker.cardsets;

import pl.maliniak.cards.Card;
import pl.maliniak.cards.Suit;

/**
 * player's 2 cards.
 * @author meliniak
 */
public class HoleCards extends AbstractCardSet {
    public static final long serialVersionUID = 132L;
    
    public HoleCards() {
        super();
    }
    
    public HoleCards(Card card1, Card card2) {
        super();
        
        this.add(card1);
        this.add(card2);
    }

    public HoleCards(String str) {
        super(str);
    }
    
    @Override
    public int capacity() {
        return 2;
    }

    public boolean isSuited() {
        Suit s1 = this.get(0).getSuit();
        Suit s2 = this.get(1).getSuit();

        return s1 == s2;
    }

    /**
     * @return True, gdy karty stanowia parke na rece, false w przeciwnym wypadku.
     */
    public boolean isPocketPair() {
        Card card1 = this.get(0);
        Card card2 = this.get(1);

        if(card1.getCardinal() == card2.getCardinal()) {
            return true;
        } else {
            return false;
        }
    }
}
