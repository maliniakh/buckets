package pl.maliniak.poker.heuristics.buckets.postflop.ranked.flush;

import java.util.List;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;
import pl.maliniak.poker.street.StreetName;

/**
 *
 * @author meliniak
 */
public class Flush extends RankedPostFlopBucket {

    public Flush() {
        super(null);
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.FLUSH;
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
