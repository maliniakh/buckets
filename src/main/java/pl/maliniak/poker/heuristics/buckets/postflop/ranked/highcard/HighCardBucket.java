package pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.Pair;

/**
 *
 * @author meliniak
 */
public class HighCardBucket extends RankedPostFlopBucket {

    private static final ArrayList<HighCardBucket> bucketRankingList = 
            Lists.newArrayList(new HighCardBucket(), new HighestCard());
    
    public HighCardBucket() {
        super(2);
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.HIGH_CARD;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        RankEnum expectedRank;
        List<Cardinal> expectedRankValues = null;

        if(tc.isHighCardOnBoard()) {
            expectedRank = RankEnum.HIGH_CARD;
        } else if(tc.isPairOnBoard()) {
            expectedRank = RankEnum.PAIR;
        } else if(tc.isTwoPairOnBoard()) {
            expectedRank = RankEnum.TWO_PAIRS;
            expectedRankValues = tc.getSignificantCardinals();
        } else if(tc.isTripsOnBoard()) {
            expectedRank = RankEnum.TRIPS;
        } else if(tc.isStraightOnBoard()) {
            expectedRank = RankEnum.STRAIGHT;
            expectedRankValues = tc.getSignificantCardinals();
        } else if(tc.isFlushOnBoard()) {
            expectedRank = RankEnum.FLUSH;
            expectedRankValues = tc.getSignificantCardinals();
        } else if(tc.isFullHouseOnBoard()) {
            expectedRank = RankEnum.FULL_HOUSE;
            expectedRankValues = tc.getSignificantCardinals();
        } else if(tc.isQuadsOnBoard()) {
            expectedRank = RankEnum.QUADS;
        } else if(tc.isStraightFlushOnBoard()) {
            expectedRank = RankEnum.STRAIGHT_FLUSH;
            expectedRankValues = tc.getSignificantCardinals();
        } else {
            throw new IllegalStateException();
        }

        HoleWTableCards hctc = new HoleWTableCards(hc, tc);
        Rank rank = hctc.getRank();
        RankEnum pokerRank = rank.getPokerRank();
        RankValues rv = rank.getRankValues();
        List<Cardinal> trimmedValues = rv.getTrimmedValues();

        if(pokerRank == expectedRank) {
            if(expectedRankValues != null) {
                // RankEnum ten sam ale nie zgadzaja sie cardinale, tzn mamy
                // doczynienia np z dwie wyzszymi parkami niz sa na boardzie
                if(trimmedValues.equals(expectedRankValues)) {
                    return true;
                }
            } else {
                return true;
            }
        }

        return false;
    }

    /*@Override
    public PostflopBucket delegateBucket(TableCards tableCards, HoleCards holeCards, StreetName street,int holeCardsUsed) {
    return new HighCardBucket(street, tableCards, holeCardsUsed);
    }*/
    public static void main(String[] args) {
        HighCardBucket hcb = new HighCardBucket();

        long nanoTime1 = System.currentTimeMillis();
        HoleCardsSet hcs = hcb.getHoleCardSet(new TableCards("2c3c2h3h4c"));
        long nanoTime2 = System.currentTimeMillis();
        long diff = nanoTime2 - nanoTime1;

        System.out.println(diff);

//        String join = Joiner.on("\n").join(hcs);
//        System.out.println(join);

        System.out.println(hcs.size());
    }

    @Override
    protected List<? extends RankedPostFlopBucket> bucketRankingList() {
        return bucketRankingList;
    }
}
