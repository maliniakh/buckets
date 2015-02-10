package pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.Pair;

/**
 *
 * @author meliniak
 */
public class Straight extends RankedPostFlopBucket {

    private static final ArrayList<Straight> bucketRankingList = 
            Lists.newArrayList(new Straight1Hcu(), new Straight2Hcu());
    
    public Straight(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    public Straight() {
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.STRAIGHT;
    }

    @Override
    protected List<Straight> bucketRankingList() {
        return bucketRankingList;
    }
}
