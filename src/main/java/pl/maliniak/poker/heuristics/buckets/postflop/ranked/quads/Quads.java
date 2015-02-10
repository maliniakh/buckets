package pl.maliniak.poker.heuristics.buckets.postflop.ranked.quads;

import java.util.List;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

/**
 * Kareta.
 * @author meliniak
 */
public class Quads extends RankedPostFlopBucket {
    
    public Quads() {
        super(2);
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.QUADS;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected List<RankedPostFlopBucket> bucketRankingList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
