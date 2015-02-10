package pl.maliniak.cards;

import java.io.Serializable;

/**
 * @author maliniak
 */
public class Card implements Comparable<Card>, Serializable {
    public static final long serialVersionUID = 391L;
    
    private Cardinal cardinal;
    private Suit suit;

    public Card(Cardinal cardinal, Suit color) {
        this.cardinal = cardinal;
        this.suit = color;
    }

    /**
     * @param str string form (2-9,T,J,Q,A i c,h,s,d)
     */
    public Card(String str) {
        if(str.length() != 2) {
            throw new IllegalArgumentException();
        }

        String cardinalStr = str.substring(0, 1);
        String suitStr = str.substring(1, 2);

        cardinal = Cardinal.getCardinal(cardinalStr);
        suit = Suit.getSuit(suitStr);
    }

    public Cardinal getCardinal() {
        return cardinal;
    }

    public void setCardinal(Cardinal cardinal) {
        this.cardinal = cardinal;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public int compareTo(Card o) {
        return (this.cardinal.ordinal() - o.cardinal.ordinal());    
    }    
    
    /**
     * @return card from evaluator library.
     */
    public game.eval.stevebrecher.SBCard toSBCard() {
        game.eval.stevebrecher.SBCard.Rank sbRank;
        game.eval.stevebrecher.SBCard.Suit sbSuit = null;
        
        sbRank = game.eval.stevebrecher.SBCard.Rank.values()[this.cardinal.ordinal()];
        
        // kolor w ten sposob
        switch (this.suit) {
            case CLUBS:
                sbSuit = game.eval.stevebrecher.SBCard.Suit.CLUB;
                break;
            case DIAMONDS:
                sbSuit = game.eval.stevebrecher.SBCard.Suit.DIAMOND;
                break;
            case HEARTS:
                sbSuit = game.eval.stevebrecher.SBCard.Suit.HEART;
                break;
            case SPADES:
                sbSuit = game.eval.stevebrecher.SBCard.Suit.SPADE;
                break;
            default:
                throw new IllegalArgumentException("Cos nie tak z kolorem karty");
        }
        
        return new game.eval.stevebrecher.SBCard(sbRank, sbSuit);
    }

    @Override
    public String toString() {
        return this.cardinal.toString() + this.suit.toString();
    }

    /**
     * @return same as toString, but letters instead of color symbols.
     */
    public String toStringLetterSuits() {
        return this.cardinal.toString() + this.suit.toStringLetter();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if(this.cardinal != other.cardinal) {
            return false;
        }
        if(this.suit != other.suit) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + this.cardinal.hashCode();
        hash = 59 * hash + this.suit.hashCode();
        return hash;
    }    
}
