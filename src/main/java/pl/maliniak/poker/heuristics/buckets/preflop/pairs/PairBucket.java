
package pl.maliniak.poker.heuristics.buckets.preflop.pairs;

import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.CardinalRange;
import pl.maliniak.poker.heuristics.buckets.preflop.PreflopBucket;

/**
 *
 * @author meliniak
 */
public class PairBucket extends PreflopBucket {
    /**
     * pair range.
     */
    private CardinalRange range;
    
    public PairBucket(CardinalRange range) {
        super(false);
        this.range = range;
    }

    @Override
    public boolean belongsTo(HoleCards hc) {
        Cardinal c = hc.get(0).getCardinal();

        if(c != hc.get(1).getCardinal()) {
            return false;
        }

        if(range.belongsToRange(c)) {
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
        Cardinal lowBound = range.getLowBound();
        Cardinal hiBound = range.getHighBound();

        if(lowBound != hiBound) {
            return hiBound.toString() + hiBound + "-" + lowBound + lowBound;
        } else {
            return hiBound.toString() + hiBound;            // np AA
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
        final PairBucket other = (PairBucket) obj;
        if(this.range != other.range && (this.range == null || !this.range.equals(other.range))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.range != null ? this.range.hashCode() : 0);
        hash = 23 * hash + (this.suited ? 1 : 0);
        return hash;
    }
}
