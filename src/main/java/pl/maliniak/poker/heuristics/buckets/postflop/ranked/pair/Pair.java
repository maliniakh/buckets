package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author meliniak
 */
public class Pair extends RankedPostFlopBucket {

    private static final ArrayList<Pair> bucketRankingList = 
            Lists.newArrayList(
            new LowPair(), new Pair2Hcu(), new Pair1Hcu(),          // RFCTR:, IMPRV:, te sie pokrywaja i sa sobie rowne, 
            new SecondMiddlePair(), new SecondHighPair(), new MiddlePair(), new HighPair(), new OverPair());
    
    public Pair(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    public Pair() {
        super();
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.PAIR;
    }

    // <editor-fold defaultstate="collapsed" desc="generateHoleCardsSet(...) stare">
//    @Override
//    protected HoleCardsSet generateHoleCardsSet(TableCards tc) {
//        HoleCardsSet resultHcs = new HoleCardsSet();
//        RankEnum expectedRank;
//        List<Cardinal> expectedRankValues = null;
//
//        if(tc.isHighCardOnBoard()) {
//            expectedRank = RankEnum.PAIR;
//        } else if(tc.isPairOnBoard()) {
//            expectedRank = RankEnum.TWO_PAIRS;
//        } else if(tc.isTwoPairOnBoard()) {
//            expectedRank = RankEnum.TWO_PAIRS;
//            expectedRankValues = tc.getSignificantCardinals();
//        } else if(tc.isTripsOnBoard()) {
//            expectedRank = RankEnum.FULL_HOUSE;
//        } else if(tc.isStraightOnBoard()) {
//            return resultHcs;
//        } else if(tc.isFlushOnBoard()) {
//            return resultHcs;
//        } else if(tc.isFullHouseOnBoard()) {
//            expectedRank = RankEnum.FULL_HOUSE;
//            expectedRankValues = tc.getSignificantCardinals();
//        } else if(tc.isQuadsOnBoard()) {
//            return resultHcs;
//        } else if(tc.isStraightFlushOnBoard()) {
//            return resultHcs;
//        } else {
//            throw new IllegalStateException();
//        }
//
//        HoleCardsSet allPossibleHcs = CardsHelper.generatePossibleHoleCards(tc);
////        resultHcs.addAll(allPossibleHcs);
//
//        // iterujemy po zbiorze i sprawdzamy ktore rece mamy usunac
//        for(HoleCards hc : allPossibleHcs) {
//            HoleWTableCards hctc = new HoleWTableCards(hc, tc);
//
//            Rank rank = hctc.getRank();
//            RankEnum pokerRank = rank.getPokerRank();
//            RankValues rv = rank.getRankValues();
//            List<Cardinal> rvTrimmedValues = rv.getTrimmedValues();
//
//            if(pokerRank == expectedRank) {
//                if(expectedRankValues == null) {
//                    resultHcs.add(hc);
//                } else {
//                    // trzeba jeszcze sprawdzic zgodnosc RV z expecetedRv jak expectedRV != null
//                    int diff = RankValues.compare(expectedRankValues, rvTrimmedValues);
//
//                    if(diff >= 0) {
//                        resultHcs.add(hc);
//                    }
//                }
//            }
//        }
//
//        return resultHcs;
//    } //</editor-fold>

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        RankEnum expectedRank;
        List<Cardinal> expectedRankValues = null;

        if(tc.isHighCardOnBoard()) {
            expectedRank = RankEnum.PAIR;
        } else if(tc.isPairOnBoard()) {
            expectedRank = RankEnum.TWO_PAIRS;
        } else if(tc.isTwoPairOnBoard()) {
            expectedRank = RankEnum.TWO_PAIRS;
            expectedRankValues = tc.getSignificantCardinals();
        } else if(tc.isTripsOnBoard()) {
            expectedRank = RankEnum.FULL_HOUSE;
        } else if(tc.isStraightOnBoard()) {
            return false;
        } else if(tc.isFlushOnBoard()) {
            return false;
        } else if(tc.isFullHouseOnBoard()) {
            expectedRank = RankEnum.FULL_HOUSE;
            expectedRankValues = tc.getSignificantCardinals();
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
                // trzeba jeszcze sprawdzic zgodnosc RV z expecetedRv jak expectedRV != null
                int diff = RankValues.compare(expectedRankValues, rvTrimmedValues);

                if(diff >= 0) {
                    return true;
                } 
            }
        }

        return false;
    }

    public static void main(String[] args) {
        TableCards tc = new TableCards("Ac2h4c");
        Pair p = new Pair();

        HoleCardsSet hcs = p.getHoleCardSet(tc);

        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
        System.out.println(hcs.size());
    }

    @Override
    protected List<Pair> bucketRankingList() {
        return bucketRankingList;
    }
}
