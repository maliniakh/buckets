package pl.maliniak.poker.heuristics.buckets.preflop.ranges;

import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.preflop.HoleCardinals;
import pl.maliniak.poker.heuristics.buckets.preflop.PreflopBucket;

/**
 * Wiaderko z dowolnym zakresem kart.
 * @author meliniak
 */
public class RangeBucket extends PreflopBucket {

    /**
     * Pierwsze ograniczenie kart.
     */
    protected HoleCardinals hcHigh;

    /**
     * Drugie ograniczenie kart.
     */
    protected HoleCardinals hcLow;

    public RangeBucket(HoleCardinals hcHigh, HoleCardinals hcLow) {
        this(hcHigh, hcLow, false);
    }

    public RangeBucket(HoleCardinals hcHigh, HoleCardinals hcLow, boolean suited) {
        super(suited);
        this.hcHigh = hcHigh;
        this.hcLow = hcLow;

        if(hcHigh.getC1().ordinal() < hcLow.getC1().ordinal()
                || hcHigh.getC2().ordinal() < hcLow.getC2().ordinal()) {
            throw new IllegalArgumentException("Niewlasciwe starszenstwo kart wzgledem siebie");
        }
    }

    /**
     * Pojedyczna reka, np AK.
     * @param hc
     */
    public RangeBucket(HoleCardinals hc) {
        this(hc, false);
    }
    
    public RangeBucket(HoleCardinals hc, boolean suited) {
        super(suited);
        this.hcHigh = hc;
        this.hcLow = hc;
    }

    @Override
    public boolean belongsTo(HoleCards hc) {
        if(hc.isSuited() != this.suited) {
            return false;
        }
        
        Cardinal c1 = hc.get(0).getCardinal();
        Cardinal c2 = hc.get(1).getCardinal();

        Cardinal hcHiC1 = hcHigh.getC1();
        Cardinal hcLoC1 = hcLow.getC1();
        Cardinal hcHiC2 = hcHigh.getC2();
        Cardinal hcLoC2 = hcLow.getC2();
        
        if(((c1.ordinal() <= hcHiC1.ordinal() && c1.ordinal() >= hcLoC1.ordinal()) && (c2.ordinal() <= hcHiC2.ordinal() && c2.ordinal() >= hcLoC2.ordinal())) ||
                ((c2.ordinal() <= hcHiC1.ordinal() && c2.ordinal() >= hcLoC1.ordinal()) && (c1.ordinal() <= hcHiC2.ordinal() && c1.ordinal() >= hcLoC2.ordinal()))) {
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
    public String toString() {
        String suitedStr = this.suited ? "s":"o";
        
        if(hcHigh.equals(hcLow)) {
            return hcHigh.toString() + suitedStr;
        } else {
            return hcHigh + suitedStr + "-" + hcLow + suitedStr;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final RangeBucket other = (RangeBucket) obj;
        if(this.hcHigh != other.hcHigh && (this.hcHigh == null || !this.hcHigh.equals(other.hcHigh))) {
            return false;
        }
        if(this.hcLow != other.hcLow && (this.hcLow == null || !this.hcLow.equals(other.hcLow))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.hcHigh != null ? this.hcHigh.hashCode() : 0);
        hash = 29 * hash + (this.hcLow != null ? this.hcLow.hashCode() : 0);
        hash = 29 * hash + (this.suited ? 1 : 0);
        return hash;
    }
}
