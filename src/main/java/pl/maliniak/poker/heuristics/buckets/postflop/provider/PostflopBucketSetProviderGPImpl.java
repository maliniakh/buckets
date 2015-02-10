package pl.maliniak.poker.heuristics.buckets.postflop.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.FlopBucketOnly;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucketSet;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.BackdoorFlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.FlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw.GutshotStraightDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw.StraightDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.flush.Flush;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.fullhouse.FullHouse;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard.HighCardBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.*;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.quads.Quads;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight.Straight2Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.straightflush.StraightFlush;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips2Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.twopair.TwoPairs;
import pl.maliniak.poker.street.StreetName;

import java.util.*;

import static pl.maliniak.poker.street.StreetName.FLOP;
import static pl.maliniak.poker.street.StreetName.RIVER;

/**
 * poprzerabiac komentarze<b/>
 * klasa kiedys implementowala PostflopBucketSetProvider, ale bylo trzeba zmienic sygnature z getBuckets(MIA) na
 * getBuckets(HoleCards hc, TableCards tc), a dbimpl jest deprecated. 
 * @author meliniak
 */
public class PostflopBucketSetProviderGPImpl extends BasePostflopBucketSetProvider {
    private static Logger logger = LoggerFactory.getLogger(PostflopBucketSetProviderGPImpl.class);

    /**
     * Mapuje nazwe gettera (tzn. jego czesc, np Flush dla getTFlush) do bucketow odpowiednich.
     * Poniewaz klasa <b>GetterBaseNamePK<b/> jest dziedziczona przez <b>GetterBaseNameHcuPK<b/>, w mapie
     * moga znalezc sie instancje obu  klas. Czesc bucketow da sie przyporzadkowac na podstawie nazwy gettera,
     * ale czesc dodatkowo przy uzyciu ilosc kart na rece.
     */
    private static SortedMap<GetterBaseNamePK, PostflopBucket> baseNameHcu2postflopBucket;
    /**
     * Do kazdego klucza (czyli bucketu), mapuje zbior innych bucketow. Dany wpis wymusza brak
     * bucketow ze zbioru; np jak mamy flush to nie ma juz strit drawow.
     */
    private static Class2BucketMap excludingBucketsMap = new Class2BucketMap();

    static {
        initBaseNameHcu2postflopBucket();
        initExcludingBucketsMap();
    }

    @Override
    public PostflopBucketSet getBuckets(TableCards tc, HoleCards hc) {
//        MozgInputArgumentGP miagp = (MozgInputArgumentGP) mia;
        PostflopBucketSet bucketSet = new PostflopBucketSet();


        // w takiej sytuacji nie znamy kart goscia
        if (hc == null) {
            return null;
        }

        StreetName street = tc.getStreet();

        if(logger.isDebugEnabled()) {
            logger.debug("Wyszukiwanie bucketow dla: " + hc + "@" + tc);
        }

        Set<Map.Entry<GetterBaseNamePK, PostflopBucket>> entries = baseNameHcu2postflopBucket.entrySet();
        for(Map.Entry<GetterBaseNamePK, PostflopBucket> entry : entries) {
            GetterBaseNamePK pk = entry.getKey();
            PostflopBucket pfb = entry.getValue();

            // jezeli wiaderko dotyczy tylko flopu to musimy tez miec flop, w przeciwnym
            // razie przechodzimy do nastepnej iteracji
            if (pfb instanceof FlopBucketOnly && street != FLOP) {
                continue;
            }

            // sprawdzamy czy wiaderko z hierachii zostalo juz dodane do wynikowego zbioru
            // poniewaz buckety sa posortowane zawsze bedzie to "szerszy" bucket
            // np Straight1Hcu jest juz dodany a chcemy sprawdzac Straight, w takim wypadku continue
            for (PostflopBucket p : bucketSet) {
                if (p.getClass().isInstance(pfb)) {
                    continue;
                }
            }

            // tak samo z drawbucketami i river
            if (pfb instanceof DrawBucket && street == RIVER) {
                continue;
            }

            boolean matches = pk.matches(hc, tc);
            if (matches) {
                PostflopBucket delegatedBucket = pfb.delegateBucket(tc, hc, street);

                // dodajemy wydelegowane wiaderko do zbioru wiaderek wyjsciowych
                bucketSet.add(delegatedBucket);
            }
        }

        // usuwamy wiaderka, ktore sie wykluczaja
        Iterator<PostflopBucket> iterator = bucketSet.iterator();
        Set<PostflopBucket> removedBuckets = new HashSet<PostflopBucket>();

        while (iterator.hasNext()) {
            PostflopBucket pfb = iterator.next();
            Set<Class<? extends PostflopBucket>> excludingBuckets = excludingBucketsMap.get(pfb.getClass());

            if (excludingBuckets == null) {
                continue;
            }

            for (Class<? extends PostflopBucket> bucketClass : excludingBuckets) {
                if (bucketSet.contains(bucketClass)) {
                    iterator.remove();
                    removedBuckets.add(pfb);

                    break;
                }
            }
        }
        if (removedBuckets.size() > 0) {
            if(logger.isDebugEnabled()) {
                logger.debug("Ze zbioru bucketow (" + bucketSet.toString() + ") usunieto: " + removedBuckets);
            }
        }

        // musimy miec co najmniej 1 wiaderko rankingowane
        if (bucketSet.rankedBucketsCount() == 0) {
            throw new IllegalStateException("Nie znaleziono zadnego wiaderka - zbior jest pusty; MIA: " + hc + "@" +tc);
        }

        if(logger.isTraceEnabled()) {
            logger.trace(String.format("%-25s%s", hc + "@" +tc, bucketSet.toString()));
        }

        return bucketSet;
    }

    private static void initBaseNameHcu2postflopBucket() {
        Map<GetterBaseNamePK, PostflopBucket> baseNameHcu2postflopBucketTmp = new HashMap<GetterBaseNamePK, PostflopBucket>();

        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new FlushDraw()), new FlushDraw());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new StraightDraw()), new StraightDraw());
//        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK("BflushDraw", 1), new BackdoorFlushDraw1Hcu());
//        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK("BflushDraw", 2), new BackdoorFlushDraw2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new BackdoorFlushDraw()), new BackdoorFlushDraw());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new GutshotStraightDraw()), new GutshotStraightDraw());

        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new HighCardBucket()), new HighCardBucket());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Pair(), 2), new Pair2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Pair(), 1), new Pair1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Pair(), 0), new HighCardBucket());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK(new TwoPairs(), 2, true), new Pair2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK(new TwoPairs(), 2, false), new TwoPairs());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new TwoPairs(), 1), new Pair1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new TwoPairs(), 0), new Pair1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK(new Trips(), 2, true), new Trips2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK(new Trips(), 2, false), new Trips1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Trips(), 1), new Trips1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Trips(), 0), new HighCardBucket());
        //baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK("Straight"), new Straight());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Straight(), 2), new Straight2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Straight(), 1), new Straight1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Straight(), 0), new HighCardBucket());
//        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK("Straight", 0), new Straight0Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Flush(), 2), new Flush());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Flush(), 1), new Flush());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new Flush(), 0), new HighCardBucket());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairTripsOnBoardPK(new FullHouse(), 2, true, false), new FullHouse());     // parka na rece z taka sama figura na boardzie i
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairTripsOnBoardPK(new FullHouse(), 2, true, true), new Pair2Hcu());       // trzy takie same karty na boardzie i parka na rece
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairTripsOnBoardPK(new FullHouse(), 1, true, true), new FullHouse());      // np 55@TTT5
//        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK("Fullhouse", 2, true), new Pair2Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK(new FullHouse(), 2, false), new FullHouse());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairTripsOnBoardPK(new FullHouse(), 1, false, false), new FullHouse());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairTripsOnBoardPK(new FullHouse(), 1, false, true), new Pair1Hcu());
//        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPocketPairPK("Fullhouse", 1, false), new Trips1Hcu());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK(new FullHouse(), 0), new HighCardBucket());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new Quads()), new Quads());
        baseNameHcu2postflopBucketTmp.put(new GetterBaseNamePK(new StraightFlush()), new StraightFlush());

        // tworzymy treemap posortowany na podstawie komparatora z podana tymczasowa mapa
        baseNameHcu2postflopBucket = new TreeMap<>(new BucketHierarchyComparator(baseNameHcu2postflopBucketTmp));
        baseNameHcu2postflopBucket.putAll(baseNameHcu2postflopBucketTmp);

        //baseNameHcu2postflopBucket.putAll(baseNameHcu2postflopBucketTmp);
    }

    private static class BucketHierarchyComparator implements Comparator<GetterBaseNamePK> {

        Map<GetterBaseNamePK, PostflopBucket> map;

        public BucketHierarchyComparator(Map<GetterBaseNamePK, PostflopBucket> map) {
            this.map = map;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compare(GetterBaseNamePK o1, GetterBaseNamePK o2) {
            PostflopBucket pfb1 = map.get(o1);
            PostflopBucket pfb2 = map.get(o2);

            if (pfb1.equals(pfb2)) {
                String o1ClassName = o1.toString();
                String o2ClassName = o2.toString();

                int result = o1ClassName.compareTo(o2ClassName);
                return result;
            }

            Class<? extends PostflopBucket> superClass;
            Class<? extends PostflopBucket> otherClass;

            // do pomnozenia przez wyniki tak aby uzyskac znak koncowy
            int sign;

            if (pfb1.getClass().isInstance(pfb2)) {
                superClass = pfb2.getClass();
                otherClass = pfb1.getClass();
                sign = 1;
            } else if (pfb2.getClass().isInstance(pfb1)) {
                superClass = pfb1.getClass();
                otherClass = pfb2.getClass();
                sign = -1;
            } else {
                String pfb1ClassName = pfb1.getClass().getName();
                String pfb2ClassName = pfb2.getClass().getName();
                // klasy nie sa swoimi instancjami, czyli nie leza na wspolnej linii w hierarchii
                // musimy zwrocic wynik rozny niz 0, czyli np porownujemy leksykalnie
                int dist = pfb1ClassName.compareTo(pfb2ClassName);
                return dist;
            }

            // sprawdzamy odleglosc tych klas w hierarchii
            int hierarchyDistance = 1;
            while (superClass.equals(otherClass)) {
                superClass = (Class<? extends PostflopBucket>) superClass.getSuperclass();
                hierarchyDistance++;
            }

            int result = hierarchyDistance * sign * 1000;
            return result;
        }
    }

    private static void initExcludingBucketsMap() {
        Set<Class<? extends PostflopBucket>> bucketSet;

        bucketSet = new HashSet<>();
        bucketSet.add(Flush.class);
        bucketSet.add(Straight.class);
        bucketSet.add(Trips.class);
        bucketSet.add(TwoPairs.class);
        excludingBucketsMap.put(DrawBucket.class, bucketSet);

        bucketSet = new HashSet<>();
        bucketSet.add(FlushDraw.class);
        bucketSet.add(StraightDraw.class);
        bucketSet.add(HighPair.class);
        bucketSet.add(OverPair.class);
        excludingBucketsMap.put(GutshotStraightDraw.class, bucketSet);

        bucketSet = new HashSet<>();
        bucketSet.add(OverPair.class);
        bucketSet.add(HighPair.class);
        bucketSet.add(StraightDraw.class);
        excludingBucketsMap.put(BackdoorFlushDraw.class, bucketSet);
    }

    /**
     * Klasa zawierajaca nazwe gettera ukladu reki i ilosc wykorzystanych kart
     * na rece, ktore ten uklad tworza. Klasa jest uzywana jako key w mapie i
     * sluzy do przyporzadkowania do wiaderek ktore maja delegowac siebie lub inne wiaderka.
     * REFAKTOR NAZW TYCH KLAS
     */
    private static class GetterBaseNamePK {

        /**
         * Nazwa bazowa gettera (np. Flush dla getFlgTFlush czy cos takiego).
         */
        protected PostflopBucket pfb;

        public GetterBaseNamePK(PostflopBucket pfb) {
            this.pfb = pfb;
        }

        public boolean matches(HoleCards hc, TableCards tc) {
//            HoleCards hc = mia.getHoleCards();
//            TableCards tc = mia.getTableCards();

            HoleWTableCards hctc = new HoleWTableCards(hc, tc);

            // drawbucket nie zostanie zwrocony przez rank, kazdy oddzielnie musimy rozpatrywac
            if (pfb instanceof DrawBucket) {
                // po kolei sprawdzamy drawbuckety, czy pasuja do aktualnego pfb
                if (pfb instanceof FlushDraw) {
                    boolean flushDraw = hctc.isFlushDraw();
                    if (flushDraw == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (pfb instanceof StraightDraw) {
                    // nie isStraightDraw tylko openended bo tak to interpretuje pt3
                    boolean oesd = hctc.isOpenEndedStraightDraw();
                    if (oesd == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (pfb instanceof BackdoorFlushDraw) {
                    // backdoor tylko na flopie moze byc
                    if (tc.getStreet() != StreetName.FLOP) {
                        return false;
                    }

                    boolean backdoor = hctc.isBackdoorFlushDraw();
                    if (backdoor == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else if (pfb instanceof GutshotStraightDraw) {
                    boolean gutShotStraightDraw = hctc.isGutShotStraightDraw();
                    if (gutShotStraightDraw == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    throw new UnsupportedOperationException("jakis drawbucket nieobsluzony?");
                }
            } else if (pfb instanceof RankedPostFlopBucket) {
                Rank rank = hctc.getRank();
                RankedPostFlopBucket rankedPfb = (RankedPostFlopBucket) pfb;

                // jak zgadza sie rank to zwracamy true
                if (rankedPfb.getRank() == rank.getPokerRank()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                throw new IllegalStateException("jakis inny typ bucketow?");
            }
        }

        public PostflopBucket getPfb() {
            return pfb;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GetterBaseNamePK other = (GetterBaseNamePK) obj;
            if ((this.pfb == null) ? (other.pfb != null) : !this.pfb.equals(other.pfb)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + (this.pfb != null ? this.pfb.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            String str;
            str = "pfb: " + "'" + pfb + "'";

            return str;
        }
    }

    private static class GetterBaseNameHcuPK extends GetterBaseNamePK {

        /**
         * Ilosc wykorzystanych kart.
         */
        protected Integer handCardsUsed;

        public GetterBaseNameHcuPK(PostflopBucket pfb, int handCardsUsed) {
            super(pfb);
            this.handCardsUsed = handCardsUsed;
        }

        @Override
        public boolean matches(HoleCards hc, TableCards tc) {
            boolean matches = super.matches(hc, tc);
            if (matches == false) {
                return false;
            }

//            HoleCards hc = mia.getHoleCards();
//            TableCards tc = mia.getTableCards();
            Integer hcu;

            HoleWTableCards hctc = new HoleWTableCards(hc, tc);
            hcu = hctc.getHoleCardsUsed();

            if (handCardsUsed.equals(hcu)) {
                return true;
            }

            return false;
        }

        public Integer getHandCardsUsed() {
            return handCardsUsed;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            if (super.equals(obj) == false) {
                return false;
            }
            final GetterBaseNameHcuPK other = (GetterBaseNameHcuPK) obj;

            if (this.handCardsUsed != other.handCardsUsed && (this.handCardsUsed == null || !this.handCardsUsed.equals(other.handCardsUsed))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * super.hashCode();
            hash += 67 * hash + (this.handCardsUsed != null ? this.handCardsUsed.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            String str = super.toString();
            str += ", hcu: " + handCardsUsed;

            return str;
        }
    }

    private static class GetterBaseNameHcuPocketPairPK extends GetterBaseNameHcuPK {

        /**
         * Czy mamy doczynienia z parka na rece.
         */
        private boolean pocketPair;

        public GetterBaseNameHcuPocketPairPK(PostflopBucket pfb, int handCardsUsed, boolean pocketPair) {
            super(pfb, handCardsUsed);
            this.pocketPair = pocketPair;
        }

        @Override
        public boolean matches(HoleCards hc, TableCards tc) {
            boolean matches = super.matches(hc, tc);
            if (matches == false) {
                return false;
            }

//            Card c1 = mia.getHoleCards().get(0);
//            Card c2 = mia.getHoleCards().get(1);
            Card c1 = hc.get(0);
            Card c2 = hc.get(1);

            boolean cardinalEquity = c1.getCardinal().equals(c2.getCardinal());

            if (pocketPair == cardinalEquity) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isPocketPair() {
            return pocketPair;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            if (super.equals(obj) == false) {
                return false;
            }
            final GetterBaseNameHcuPocketPairPK other = (GetterBaseNameHcuPocketPairPK) obj;
            if (this.pocketPair != other.pocketPair) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * super.hashCode();
            hash += 59 * hash + (this.pocketPair ? 1 : 0);
            return hash;
        }

        @Override
        public String toString() {
            String str = super.toString();
            str += ", " + "isPP: " + pocketPair;

            return str;
        }
    }

    private static class GetterBaseNameHcuPocketPairTripsOnBoardPK extends GetterBaseNameHcuPocketPairPK {

        /**
         * Czy mamy tripsa na stole, np 888J2.
         */
        private boolean tripsOnBoard;

        public GetterBaseNameHcuPocketPairTripsOnBoardPK(PostflopBucket pfb, int handCardsUsed, boolean pocketPair, boolean tripsOnBoard) {
            super(pfb, handCardsUsed, pocketPair);
            this.tripsOnBoard = tripsOnBoard;
        }

        @Override
        public boolean matches(HoleCards hc, TableCards tc) {
            boolean matches = super.matches(hc, tc);

            if (matches == false) {
                return false;
            }

            // czy na boardzie mamy tripsa
//            TableCards tc = mia.getTableCards();
            List<Cardinal> cardinalsSorted = tc.getCardinalsSorted(true);
            boolean tripsOnBoardFound = false;
            for (Cardinal cardinal : cardinalsSorted) {
                int cardinalCount = tc.getCardinalCount(cardinal);

                // ktoras z figur wystepuje w ilosci 3
                if (cardinalCount == 3) {
                    tripsOnBoardFound = true;
                    break;
                }
            }

            if (tripsOnBoard == true) {
                if (tripsOnBoardFound == true) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (tripsOnBoardFound == true) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        @Override
        public String toString() {
            String str = super.toString();
            str += ", tripsOnBrd:" + tripsOnBoard;
            return str;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }

            if (super.equals(obj) == false) {
                return false;
            }

            final GetterBaseNameHcuPocketPairTripsOnBoardPK other = (GetterBaseNameHcuPocketPairTripsOnBoardPK) obj;
            if (this.tripsOnBoard != other.tripsOnBoard) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 73 * hash + super.hashCode();
            hash = 73 * hash + (this.tripsOnBoard ? 1 : 0);
            return hash;
        }
    }

    private static class Class2BucketMap extends HashMap<Class<? extends PostflopBucket>, Set<Class<? extends PostflopBucket>>> {

        @Override
        public Set<Class<? extends PostflopBucket>> get(Object key) {
            Set<Class<? extends PostflopBucket>> pfbSet = new HashSet<Class<? extends PostflopBucket>>();
            Class<? extends PostflopBucket> keyClass = (Class<? extends PostflopBucket>) key;

            while (true) {
                Set<Class<? extends PostflopBucket>> tmpPfbSet;
                tmpPfbSet = super.get(keyClass);

                if (tmpPfbSet != null) {
                    pfbSet.addAll(tmpPfbSet);

                }

                // wyzszych klas juz nie przeszukujemy
                // a wlasnie ze przeszukujemy
                if (keyClass.equals(PostflopBucket.class)) {
                    break;
                }

                keyClass = (Class<? extends PostflopBucket>) keyClass.getSuperclass();
            }

            return pfbSet;
        }
    }
}