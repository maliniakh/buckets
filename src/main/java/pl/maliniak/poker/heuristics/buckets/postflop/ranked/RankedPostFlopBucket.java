package pl.maliniak.poker.heuristics.buckets.postflop.ranked;

import java.util.List;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.LowPair;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.Pair1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.Pair2Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips;

/**
 * Wiaderko, ktore okresla przynaleznosc do konkretnego ukladu reki gracza.
 *
 * @author meliniak
 */
public abstract class RankedPostFlopBucket extends PostflopBucket implements Comparable<RankedPostFlopBucket> {

    /**
     * Uklad kart.
     */
    private RankEnum rank;

    public RankedPostFlopBucket() {
        this.rank = this.getRank();
    }

    public RankedPostFlopBucket(Integer holeCardsUsed) {
        super(holeCardsUsed);
        this.rank = this.getRank();
    }

    public Rank getActuallRank() {
        throw new UnsupportedOperationException();
    }

    /**
     * Zwraca ranking wiaderka.<br/>
     *
     * @return
     */
    public abstract RankEnum getRank();

    @Override
    public int compareTo(RankedPostFlopBucket o) {
        return this.rank.ordinal() - o.rank.ordinal();
    }

    /**
     * lista bucketow w kolejnosci sily (rosnaco) buckety w liscie maja wspolna nadklase (czyli konkretny bucket 1 levelu), np wszystkie
     * buckety od parki (highpair, bottompair itd) motyw jest potrzebny do liczcenia equity na sd przeciwko rozkladom bucketow
     * (bucketdistribution) normalnie buckety beda porownywane po ranku, ale w ramach takiego samego ranku trzeba juz recznie to definiowac
     * (np wlasnie za pomoca takiej uporzodkowanej listy )
     */
    protected abstract List<? extends RankedPostFlopBucket> bucketRankingList();

    /**
     * @param rpfb
     * @return uzywa bucketRankingList() do porownywania bucketow o tym samym ranku.
     */
    public int compareBuckets(RankedPostFlopBucket rpfb) {
        if (rpfb.getRank() != this.rank) {
            throw new IllegalArgumentException("porownywanie bucketow o roznych rankach");
        }
        
        // przypadek dla lowpair, pair2hcu i pair2hcu, traktujemy tak samo, bo ich zakres sie pokrywa
        // jezeli oba buckety stanowie ktores z tych trzech powyzszych to zakladamy ze sa rowne
        // TODO_2: ogarnac
        boolean equalThis = (this.getClass() == LowPair.class || this.getClass() == Pair2Hcu.class || this.getClass() == Pair1Hcu.class);
        boolean equalOther = (rpfb.getClass() == LowPair.class || rpfb.getClass() == Pair2Hcu.class || rpfb.getClass() == Pair1Hcu.class);
        if(equalThis && equalOther) {
            return 0;
        }

        // TODO_1: ogarnac, jak to powinno byc z tripsami, czy powinien byc podzial na tripsxhcu czy nie
        // w modelu sie pojawia trips1hcu chyba, nie powinien sam tylko trips po prostu?
        if(rpfb instanceof Trips) {
            return 0;
        }
        
        int indexOfThis = this.bucketRankingList().indexOf(this);
        int indexOfOther = this.bucketRankingList().indexOf(rpfb);

        if (indexOfThis == -1) {
            throw new IllegalStateException("bucket " + this + " nie jest wpisany do listy?");
        }
        if (indexOfOther == -1) {
            throw new IllegalStateException("bucket " + rpfb + " nie jest wpisany do listy?");
        }
        
        int diff = indexOfThis - indexOfOther;
//        assert diff != 0;
        return diff;
    }
}
