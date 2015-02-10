package pl.maliniak.poker.heuristics.buckets.preflop.connectors;

import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.CardinalRange;
import pl.maliniak.poker.heuristics.buckets.preflop.HoleCardinals;
import pl.maliniak.poker.heuristics.buckets.preflop.PreflopBucket;

/**
 * @author meliniak
 */
public class ConnectorBucket extends PreflopBucket {
    /**
     * Zakres dolnej karty.
     */
    private CardinalRange range;
    /**
     * Odleglosc miedzy kartami, 0 dla 67 np.
     */
    private int gap;

    private boolean chujek;

    /**
     * @param range Zakres dolnej karty
     * @param gap 
     */
    public ConnectorBucket(CardinalRange range, int gap, boolean suited) {
        super(suited);
        this.range = range;
        this.gap = gap;

        if(gap >= 3) {
            throw new IllegalArgumentException("Za duzy gap w connectorach?");
        }

        if(gap < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Konektor bez przerwy (gap = 0).
     * @param range Zakres nizszej karty.
     */
    public ConnectorBucket(CardinalRange range, boolean suited) {
        this(range, 0, suited);
    }

    @Override
    public String toString() {
        Cardinal lowBound = range.getLowBound();
        Cardinal highBound = range.getHighBound();

        String suitedStr = suited ? "s":"o";

        // wyzsza karta z konektorow z zakresu
        Cardinal higherCardinalLoBound = Cardinal.getCardinalByOrdinal(lowBound.ordinal() + 2 + gap + 1);
        Cardinal higherCardinalHiBound = Cardinal.getCardinalByOrdinal(highBound.ordinal() + 2 + gap + 1);;

        // zakres z 1 konektorem
        if(lowBound == highBound) {
            return lowBound.toString() + higherCardinalLoBound + suitedStr;
        }

        return lowBound.toString() + higherCardinalLoBound + suitedStr + "-" + highBound.toString() + higherCardinalHiBound + suitedStr;
    }

    @Override
    public boolean belongsTo(HoleCards hc) {
        Cardinal c1 = hc.get(0).getCardinal();
        Cardinal c2 = hc.get(1).getCardinal();

        if(c1 == c2) {
            return false;
        }

        if(hc.isSuited() != suited) {
            return false;
        }
        
        // zamieniany, zeby c1 byl mniejszy od c2
        if(c1.ordinal() > c2.ordinal()) {
            Cardinal tmpC = c2;
            c2 = c1;
            c1 = tmpC;
        }

        // czy nizsza karta nalezy do zakresu
        boolean belongsToRange = range.belongsToRange(c1);
        if(belongsToRange == false) {
            return false;
        }

        // czy gap jest odpowiedni
        if(c2.ordinal() - c1.ordinal() == gap + 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final ConnectorBucket other = (ConnectorBucket) obj;
        if(this.range != other.range && (this.range == null || !this.range.equals(other.range))) {
            return false;
        }
        if(this.gap != other.gap) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.range != null ? this.range.hashCode() : 0);
        hash = 19 * hash + this.gap;
        hash = 19 * hash + (this.suited ? 1 : 0);
        return hash;
    }

    public static void main(String[] args) {
        boolean belongs;
        ConnectorBucket cb;
        String hcStr;

        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), false);
        System.out.println(cb);
        hcStr = "2c4c";
        belongs = cb.belongsTo(new HoleCards(hcStr));
        System.out.println("hcStr = " + hcStr);
        System.out.println("belongs = " + belongs);

        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), false);
        System.out.println(cb);
        hcStr = "3c4c";
        belongs = cb.belongsTo(new HoleCards(hcStr));
        System.out.println("hcStr = " + hcStr);
        System.out.println("belongs = " + belongs);

        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), false);
        System.out.println(cb);
        hcStr = "5c6s";
        belongs = cb.belongsTo(new HoleCards(hcStr));
        System.out.println("hcStr = " + hcStr);
        System.out.println("belongs = " + belongs);
        
        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), false);
        System.out.println(cb);
        hcStr = "4s5c";
        belongs = cb.belongsTo(new HoleCards(hcStr));
        System.out.println("hcStr = " + hcStr);
        System.out.println("belongs = " + belongs);
        
        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), false);
        System.out.println(cb);
        hcStr = "4s5s";
        belongs = cb.belongsTo(new HoleCards(hcStr));
        System.out.println("hcStr = " + hcStr);
        System.out.println("belongs = " + belongs);

        cb = new ConnectorBucket(new CardinalRange(Cardinal.FOUR, Cardinal.TWO), 1, false);
        System.out.println("cb = " + cb);

        cb = new ConnectorBucket(new CardinalRange(Cardinal.JACK, Cardinal.TWO), 2, true);
        System.out.println("cb = " + cb);
    }
}
