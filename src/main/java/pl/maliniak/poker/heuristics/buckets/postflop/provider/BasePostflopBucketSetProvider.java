package pl.maliniak.poker.heuristics.buckets.postflop.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.BackdoorFlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.FlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw.GutshotStraightDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw.StraightDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.flush.Flush;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.fullhouse.FullHouse;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard.HighCardBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard.HighestCard;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.*;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.quads.Quads;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight0Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight2Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straightflush.StraightFlush;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips2Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.twopair.TwoPairs;

import java.util.*;

/**
 * @author maliniak
 */
public abstract class BasePostflopBucketSetProvider implements PostflopBucketSetProvider {

    private static final Logger logger = LoggerFactory.getLogger(BasePostflopBucketSetProvider.class);

    /**
     * Wszystkie buckety rankingowane, ktore zwracaja siebie, badz deleguja swoje podklasy.
     */
    private static Map<RankEnum, RankedPostFlopBucket> rank2DelegatorMap = new HashMap<RankEnum, RankedPostFlopBucket>();
    /**
     * Rankingowane buckety, ale tylko pierwszego levelu (np PairBucket ale
     * MiddlePairBucket juz nie).
     */
    private static Set<RankedPostFlopBucket> rankedFirstLevelBuckets = new HashSet<RankedPostFlopBucket>();
    /**
     * Rankingowane buckety, oprocz tych pierwszego levelu.
     */
    private static Set<RankedPostFlopBucket> rankedNonFirstLevelBuckets = new HashSet<RankedPostFlopBucket>();
    /**
     * Wszystkie rankingowane buckety.
     */
    private static Set<RankedPostFlopBucket> rankedBuckets = new HashSet<RankedPostFlopBucket>();
    /**
     * Wszystkie draw buckety.
     */
    private static Set<DrawBucket> drawBucketSet = new HashSet<DrawBucket>();
    /**
     * Wszystkie buckety postflop.
     */
    private static  Set<PostflopBucket> postflopBuckets = new HashSet<PostflopBucket>();
    /**
     * Wszystkie buckety, ale posortowane w liscie.
     */
    private static  List<PostflopBucket> postflopBucketsSorted = null;
    /**
     * Mapuje instancje bucketow do zbioru klas bucketow, ktore moga byc przez nie delegowane.
     */
    private static Map<PostflopBucket, Set<? extends PostflopBucket>> bucket2Delegates = new HashMap<PostflopBucket, Set<? extends PostflopBucket>>();
    /**
     * mapa reprezentacji stringowych bucketow (to co zwraca ichniejszy toString()) do ich instancji.
     */
    private static Map<String, PostflopBucket> name2bucketMap = new HashMap<String, PostflopBucket>();
    
    static {
            // rejestrujemy buckety
            registerRankedFirstLevelBuckets();
            registerRankedNonFirstLevelBuckets();
            registerDrawBuckets();

            // na koncu tworzymy set wszystkich wiaderek
            initBucketSet();

            initBucketNamesMap();

    }

    /**
     * Rejestruje i mapuje do skojarzonego rankingu w klasie wszystkie
     * wiaderka rankingowane, w sensie PairBucket itd.
     */
    private static void registerRankedFirstLevelBuckets() {
        // wiaderka pierwszego levelu (czyli pairbucket tak ale highpairbucket juz nie)
        // najpierw wrzucamy do setu
        rankedFirstLevelBuckets.add(new HighCardBucket());
        rankedFirstLevelBuckets.add(new Pair());
        rankedFirstLevelBuckets.add(new TwoPairs());
        rankedFirstLevelBuckets.add(new Trips());
        rankedFirstLevelBuckets.add(new Straight());
        rankedFirstLevelBuckets.add(new Flush());
        rankedFirstLevelBuckets.add(new FullHouse());
        rankedFirstLevelBuckets.add(new Quads());
        rankedFirstLevelBuckets.add(new StraightFlush());

        // pozniej iterujemy ten set i mapujemy kazdy wpis do skojarzonego rankingu
        // oraz dodajemy do setu wszystkich wiaderek
        for (RankedPostFlopBucket rankedBucket : rankedFirstLevelBuckets) {
            rank2DelegatorMap.put(rankedBucket.getRank(), rankedBucket);
            rankedBuckets.add(rankedBucket);
        }
    }

    /**
     * Rejestruje buckety rankingowane, ktore nie sa wiaderkami pierwszego poziomu.
     * Np MiddlePair ale nie PairBucket.
     */
    private static void registerRankedNonFirstLevelBuckets() {
        // high card
        rankedNonFirstLevelBuckets.add(new HighestCard());
        // parki
        rankedNonFirstLevelBuckets.add(new Pair1Hcu());
        rankedNonFirstLevelBuckets.add(new Pair2Hcu());
        rankedNonFirstLevelBuckets.add(new Trips1Hcu());
        rankedNonFirstLevelBuckets.add(new Trips2Hcu());
        rankedNonFirstLevelBuckets.add(new HighPair());
        rankedNonFirstLevelBuckets.add(new SecondHighPair());
        rankedNonFirstLevelBuckets.add(new OverPair());
        rankedNonFirstLevelBuckets.add(new MiddlePair());
        rankedNonFirstLevelBuckets.add(new LowPair());
        rankedNonFirstLevelBuckets.add(new SecondMiddlePair());
        // flush
        rankedNonFirstLevelBuckets.add(new Flush());
        // strity
        rankedNonFirstLevelBuckets.add(new Straight0Hcu());
        rankedNonFirstLevelBuckets.add(new Straight1Hcu());
        rankedNonFirstLevelBuckets.add(new Straight2Hcu());

        rankedBuckets.addAll(rankedNonFirstLevelBuckets);
    }

    private static void registerDrawBuckets() {
        drawBucketSet.add(new StraightDraw());

        drawBucketSet.add(new FlushDraw());
//        drawBucketSet.add(new FlushDrawBucket1Hcu());
//        drawBucketSet.add(new FlushDrawBucket2Hcu());

        drawBucketSet.add(new GutshotStraightDraw());
//        drawBucketSet.add(new Gutshot2StraightDraw());

        drawBucketSet.add(new BackdoorFlushDraw());
    }

    /**
     * Tworzy zbior wszystkich wiaderek postflopowych na podstawie wiaderek
     * wczesniej juz zarejestrowanych.<br/>
     * Wywoluje rowniez inicjalizator tworzacy mape delegatorow.
     */
    private static void initBucketSet() {
        postflopBuckets.addAll(rankedBuckets);
        postflopBuckets.addAll(drawBucketSet);

        initBucket2Delegates();
    }

    /**
     * Inicjalizuje mape. Musza byc wczesniej wypelnione wszystkie kolekcje.
     */
    private static void initBucket2Delegates() {
        Class superClass = null;
        Set<PostflopBucket> delegateSet;                        // zbior klas delegujacych

        // najpierw dla bucketow rankingowanych
        for (RankedPostFlopBucket delegator : rankedBuckets) {
            delegateSet = new HashSet<PostflopBucket>();

            for (RankedPostFlopBucket delegate : rankedBuckets) {
                superClass = delegate.getClass().getSuperclass();

                // jezeli klasa jest ta sama, to znaczy ze mamy relacje delegator - bucket delegujacy
                if (superClass == delegator.getClass()) {
                    delegateSet.add(delegate);
                }
            }

            // dodajemy entry z wyszukanym zbiorem klas, ktore dziedzicza
            bucket2Delegates.put(delegator, delegateSet);
        }

        // pozniej dla bucketow drawowych
        for (DrawBucket delegator : drawBucketSet) {
            delegateSet = new HashSet<PostflopBucket>();

            for (DrawBucket delegate : drawBucketSet) {
                superClass = delegate.getClass().getSuperclass();

                // jezeli klasa jest ta sama, to znaczy ze mamy relacje delegator - bucket delegujacy
                if (superClass == delegator.getClass()) {
                    delegateSet.add(delegate);
                }
            }

            // dodajemy entry z wyszukanym zbiorem klas, ktore dziedzicza
            bucket2Delegates.put(delegator, delegateSet);
        }

        // sprawdzamy czy buckety sa poprawnie pododawane
        // czy kazdy bucket dodany w mapie bucket2Delegates ma "rodzica"
        // tzn. dla kazdego bucketu B istnieje taki bucket P ze
        // P.contains(B) == true
        // to i tak nie gwarantuje tego ze wszystkie buckety zostaly dodane
        for (PostflopBucket pfb : bucket2Delegates.keySet()) {
            // "toplevelowe" wiaderka nie maja delegatow
            superClass = pfb.getClass().getSuperclass();
            if (superClass == RankedPostFlopBucket.class || superClass == DrawBucket.class) {
                continue;
            }

            boolean parentFound = false;
            for (PostflopBucket parentBucket : bucket2Delegates.keySet()) {
                Set<? extends PostflopBucket> childSet = bucket2Delegates.get(parentBucket);

                if (childSet.contains(pfb)) {
                    parentFound = true;
                    // mamy to co chemy, break
                    break;
                }
            }

            if (parentFound == false) {
                throw new IllegalStateException("Nie mozna znalezc wiaderka, ktore moze wydelegowac wiadrko: '" + pfb
                        + "', nie zostaly dodane wszystkie wiaderka do odpowiednich kolekcji w klasie.");
            }
        }
    }

    /**
     * inicjalizuje name2bucketMap.
     */
    private static void initBucketNamesMap() {
        if(postflopBuckets == null || postflopBuckets.size() == 0) {
            throw new IllegalStateException("postflopBuckets nie zainicjalizowane");
        }

        for (PostflopBucket pfb : postflopBuckets) {
            name2bucketMap.put(pfb.toString(), pfb);
        }
    }

    /**
     * Zwraca zbior delegatow wiaderka.
     * @param bucket Wiaderko, ktore ma ewentualnie delegowac.
     * @return Zbior delegatow, czyli podklas klasy wiaderka podanej jako argument.
     */
    public static Set<? extends PostflopBucket> getDelegates(PostflopBucket bucket) {
        Set<? extends PostflopBucket> delegates = bucket2Delegates.get(bucket);

        if (delegates == null) {
            throw new IllegalStateException("Wiaderko nie zostalo dodane do listy wiaderek. Nalezy dodac odpowiedni wpis "
                    + "w klasie BasePostflopBucketSetProvider");
        }

        return delegates;
    }

    public static Set<DrawBucket> getDrawBucketSet() {
        return drawBucketSet;
    }
}
