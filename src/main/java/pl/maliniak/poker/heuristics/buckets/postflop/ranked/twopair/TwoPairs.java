package pl.maliniak.poker.heuristics.buckets.postflop.ranked.twopair;

import java.util.List;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

/**
 * Trzy takie same karty.
 * @author meliniak
 */
public class TwoPairs extends RankedPostFlopBucket {

    public TwoPairs() {
        super(2);
    }   

    @Override
    public RankEnum getRank() {
        return RankEnum.TWO_PAIRS;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        RankEnum expectedRank;
        List<Cardinal> expectedRankValues = null;

        if(tc.isHighCardOnBoard()) {
            expectedRank = RankEnum.TWO_PAIRS;
        } else if(tc.isPairOnBoard()) {
            expectedRank = RankEnum.TWO_PAIRS;
            expectedRankValues = tc.getSignificantCardinals();
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
        List<Cardinal> rvTrimmedValues = rv.getTrimmedValues();

        if(pokerRank == expectedRank) {
            if(expectedRankValues == null) {
                return true;
            } else {
                // przypadek gdy mamy parke na boardzie
                Cardinal pairCardinal = expectedRankValues.get(0);
                
                if(hc.get(0).getCardinal().ordinal() > pairCardinal.ordinal() &&
                        hc.get(1).getCardinal().ordinal() > pairCardinal.ordinal()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    @Override
    protected List<RankedPostFlopBucket> bucketRankingList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
