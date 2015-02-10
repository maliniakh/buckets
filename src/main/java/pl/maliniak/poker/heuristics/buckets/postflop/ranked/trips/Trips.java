package pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips;

import java.util.List;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

/**
 *
 * @author meliniak
 */
public class Trips extends RankedPostFlopBucket{

    public Trips(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    public Trips() {
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.TRIPS;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        RankEnum expectedRank;
        List<Cardinal> expectedRankValues = null;

        if(tc.isHighCardOnBoard()) {
            expectedRank = RankEnum.TRIPS;
        } else if(tc.isPairOnBoard()) {
            expectedRank = RankEnum.TRIPS;
        } else if(tc.isTwoPairOnBoard()) {
            return false;
        } else if(tc.isTripsOnBoard()) {
            return false;
        } else if(tc.isStraightOnBoard()) {
            return false;
        } else if(tc.isFlushOnBoard()) {
            return false;
        } else if(tc.isFullHouseOnBoard()) {
            return false;
        } else if(tc.isQuadsOnBoard()) {
            return false;
        } else if(tc.isStraightFlushOnBoard()) {
            return false;
        } else {
            throw new IllegalStateException();
        }

        HoleWTableCards hctc = new HoleWTableCards(hc, tc);

        Rank rank = hctc.getRank();
        RankEnum pokerRank = rank.getPokerRank();
        RankValues rv = rank.getRankValues();

        if(pokerRank == expectedRank) {
            if(expectedRankValues == null) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected List<RankedPostFlopBucket> bucketRankingList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
