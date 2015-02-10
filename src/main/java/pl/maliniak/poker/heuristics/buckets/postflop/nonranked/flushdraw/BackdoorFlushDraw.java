package pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw;

import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.FlopBucketOnly;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;
import pl.maliniak.poker.street.StreetName;

/**
 *
 * @author meliniak
 */
public class BackdoorFlushDraw extends DrawBucket implements FlopBucketOnly {

    public BackdoorFlushDraw() {
    }

    public BackdoorFlushDraw(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(tc.getStreet() != StreetName.FLOP) {
            throw new IllegalArgumentException();
        }
        
        HoleWTableCards hctc = new HoleWTableCards(hc, tc);
        return hctc.isBackdoorFlushDraw();
    }

    public static void main(String[] args) {
        
    }
}
