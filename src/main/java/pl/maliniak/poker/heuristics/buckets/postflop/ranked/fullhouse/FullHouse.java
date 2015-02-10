package pl.maliniak.poker.heuristics.buckets.postflop.ranked.fullhouse;

import java.util.List;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

/**
 * FH.
 * @author meliniak
 */
public class FullHouse extends RankedPostFlopBucket {
    
    public FullHouse() {
        super(2);
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.FULL_HOUSE;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected List<? extends RankedPostFlopBucket> bucketRankingList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
