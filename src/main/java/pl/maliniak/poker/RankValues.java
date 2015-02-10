package pl.maliniak.poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Wartosci kart po kolei, ktore skladaja sie na sile reki.
 * Dla AT @ 3TTA2 to beda np T, A, 3, 2.
 * @author meliniak
 */
public class RankValues implements Comparable<List<Cardinal>> {
    /* Each evaluation method's return value is an int; 32 bits = 0x0VTBKKKK
     * where each letter refers to a 4-bit nybble:<pre>
     * V nybble = category code ranging from {@link HandCategory#NO_PAIR}<code>.ordinal()</code>
     *                                    to {@link HandCategory#STRAIGHT_FLUSH}<code>.ordinal()</code>
     * T nybble = rank (2..14) of top pair for two pair, 0 otherwise
     * B nybble = rank (1..14) of quads or trips (including full house trips),
     *              or rank of high card (5..14) in straight or straight flush,
     *              or rank of bottom pair for two pair (hence the symbol "B"),
     *              or rank of pair for one pair,
     *              or 0 otherwise
     * KKKK mask = 16-bit mask with...
     *              5 bits set for no pair or (non-straight-)flush
     *              3 bits set for kickers with pair,
     *              2 bits set for kickers with trips,
     *              1 bit set for pair within full house or kicker with quads
     *                            or kicker with two pair
     *              or 0 otherwise</pre>
     */

    /**
     * Ranking ukladu kart.
     */
    private Rank rank;
    private List<Cardinal> cardinalList;

    /**
     * Mapuje uklad pokerowy (RankEnum) do ilosci hmm istotnych cardinali.
     * Np dla parki AT@T28 value takiego układu to TA82, ale do parki liczy się
     * tylko pierwsza wartości, więc będzie w zmapowane do 1.
     * Dla 2 parek 2 itd.
     */
    private static final Map<RankEnum, Integer> rank2importantCardinalsMap = new HashMap<RankEnum, Integer>();

    static {
        rank2importantCardinalsMap.put(RankEnum.HIGH_CARD, 5);
        rank2importantCardinalsMap.put(RankEnum.PAIR, 1);
        rank2importantCardinalsMap.put(RankEnum.TWO_PAIRS, 2);
        rank2importantCardinalsMap.put(RankEnum.TRIPS, 1);
        rank2importantCardinalsMap.put(RankEnum.STRAIGHT, 1);
        rank2importantCardinalsMap.put(RankEnum.FLUSH, 5);
        rank2importantCardinalsMap.put(RankEnum.FULL_HOUSE, 2);
        rank2importantCardinalsMap.put(RankEnum.QUADS, 1);
        rank2importantCardinalsMap.put(RankEnum.STRAIGHT_FLUSH, 1);
    }
    
    
    public RankValues(Rank rank) {
        this.rank = rank;

        if(rank.getValue() > 0) {
            this.updateCardinals();
        }
    }

    /**
     * Zwraca liste cardinali ograniczona do istotych wartosci, np dla parki bedzie
     * to tylko jedna wartosc o wysokosci pary, dla dwoch parek dwie wartosci,
     * dla fulla 2, strita 1 itd.
     * @return
     */
    public List<Cardinal> getTrimmedValues() {
        Integer cardinalsCount = rank2importantCardinalsMap.get(rank.getPokerRank());

        List<Cardinal> listCopy = new ArrayList<Cardinal>(cardinalList);
        listCopy = listCopy.subList(0, cardinalsCount);

        return listCopy;
    }

    /**
     * Aktualizuje liste.
     */
    private void updateCardinals() {
        /**
         * maski dla 0x0VTBKKKK i 
         */
        int tNybbleMask = 0x00F00000;
        int bNybbleMask = 0x000F0000;
        int kkkkMask = 0x0000FFFF;

        final int rankValue = this.rank.getValue();
        // warto wyliczone z maskami
        int t = (rankValue & tNybbleMask) >> 20;
        int b = (rankValue & bNybbleMask) >> 16;
        int kkkk = (rankValue & kkkkMask);

        //String str = String.format("%s\t%s", Integer.toHexString(rankValue), rankValue);
        //System.out.println(str);

        int kkkkVal1 = RankValues.getKKKKValue(kkkk, 4);
        int kkkkVal2 = RankValues.getKKKKValue(kkkk, 3);
        int kkkkVal3 = RankValues.getKKKKValue(kkkk, 2);
        int kkkkVal4 = RankValues.getKKKKValue(kkkk, 1);
        int kkkkVal5 = RankValues.getKKKKValue(kkkk, 0);

        int kkkkCardinal5 = 0;
        int kkkkCardinal4 = 0;
        int kkkkCardinal3 = 0;
        int kkkkCardinal2 = 0;
        int kkkkCardinal1 = 0;

        if(kkkkVal1 != 0) {
            kkkkCardinal1 = getKKKKCardinalVal(kkkkVal1);
        }
        if(kkkkVal2 != 0) {
            kkkkCardinal2 = getKKKKCardinalVal(kkkkVal2);
        }
        if(kkkkVal3 != 0) {
            kkkkCardinal3 = getKKKKCardinalVal(kkkkVal3);
        }
        if(kkkkVal4 != 0) {
            kkkkCardinal4 = getKKKKCardinalVal(kkkkVal4);
        }
        if(kkkkVal5 != 0) {
            kkkkCardinal5 = getKKKKCardinalVal(kkkkVal5);
        }

        RankEnum pokerRank = this.rank.getPokerRank();

        //System.out.println(kkkkCardinal1 + " " + kkkkCardinal2 + " " + kkkkCardinal3 + " " + kkkkCardinal4 + " " + kkkkCardinal5 + " " + pokerRank);

        Cardinal c1, c2, c3, c4, c5;
        c1 = c2 = c3 = c4 = c5 = null;
        double tmp;
        switch(pokerRank) {
            case HIGH_CARD:
                c1 = Cardinal.getCardinal(kkkkCardinal1);
                c2 = Cardinal.getCardinal(kkkkCardinal2);
                c3 = Cardinal.getCardinal(kkkkCardinal3);
                c4 = Cardinal.getCardinal(kkkkCardinal4);
                c5 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case PAIR:
                c1 = Cardinal.getCardinal(b);
                c2 = Cardinal.getCardinal(kkkkCardinal3);
                c3 = Cardinal.getCardinal(kkkkCardinal4);
                c4 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case TWO_PAIRS:
                c1 = Cardinal.getCardinal(t);
                c2 = Cardinal.getCardinal(b);
                c3 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case TRIPS:
                c1 = Cardinal.getCardinal(b);
                c2 = Cardinal.getCardinal(kkkkCardinal4);
                c3 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case STRAIGHT:
                c1 = Cardinal.getCardinal(b);
                break;
            case FLUSH:
                c1 = Cardinal.getCardinal(kkkkCardinal1);
                c2 = Cardinal.getCardinal(kkkkCardinal2);
                c3 = Cardinal.getCardinal(kkkkCardinal3);
                c4 = Cardinal.getCardinal(kkkkCardinal4);
                c5 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case FULL_HOUSE:
                tmp = Math.log(kkkk) / Math.log(2) + 2;

                c1 = Cardinal.getCardinal(b);
                c2 = Cardinal.getCardinal((int) tmp);
                break;
            case QUADS:
                c1 = Cardinal.getCardinal(b);
                c2 = Cardinal.getCardinal(kkkkCardinal5);
                break;
            case STRAIGHT_FLUSH:
                c1 = Cardinal.getCardinal(b);
                break;
        }

        // fiu, wypelniamy liste
        this.cardinalList = new ArrayList<Cardinal>();
        if(c1 != null) {
            this.cardinalList.add(c1);
            if(c2 != null) {
                this.cardinalList.add(c2);
                if(c3 != null) {
                    this.cardinalList.add(c3);
                    if(c4 != null) {
                        this.cardinalList.add(c4);
                        if(c5 != null) {
                            this.cardinalList.add(c5);
                        }
                    }
                }
            }
        }
    }

    /**
     * Zwraca wartosc dla odpowiedniej maski. Chodzi o wyciagniecie, na ktorych
     * pozycjach sa jedynki w zapisie binarnym. <br/>
     * Np dla 100101 jako argumentu kkkk, funkcja zwroci dla order (ktora jedynka
     * liczac od prawej strony i indeksujac od 0) 0, 1 i 2 otrzymamy odpowiednio
     * nastepujace wartosci w zapisie binarnym: 000001, 000100, 100000.
     * @param kkkk Wartosc wymaskowana z HandEval.
     * @param order Ktora 1, liczac od prawej strony.
     * @return Wymaskowana wartosc.
     */
    private static int getKKKKValue(int kkkk, int order) {
        int number = 0;

        int maskedVal;
        int maskedShiftedVal;
        int counter = 0;
        int mask;
        while(counter < 0x10 && order >= 0) {
            mask = 1 << counter;
            maskedVal = kkkk & mask;
            maskedShiftedVal = maskedVal >> counter;

            if(maskedShiftedVal == 1) {
                if(order == 0) {
                    return mask;
                }
                order--;
            }
            counter++;
        }

        return 0;
    }

    /**
     * Przeksztalca wartosc z getKKKKValue()
     * @param kkkkVal
     * @return
     */
    private static int getKKKKCardinalVal(int kkkkVal) {
        double cardinalVal = Math.log(kkkkVal) / Math.log(2) + 2;

        assert cardinalVal == (int) cardinalVal : "Wynikiem nie jest liczba calkowita.";

        return (int) cardinalVal;
    }

    public <T> T[] toArray(T[] a) {
        return cardinalList.toArray(a);
    }

    public int size() {
        return cardinalList.size();
    }

    public ListIterator<Cardinal> listIterator() {
        return cardinalList.listIterator();
    }

    public boolean isEmpty() {
        return cardinalList.isEmpty();
    }

    public int indexOf(Cardinal c) {
        return cardinalList.indexOf(c);
    }

    public Cardinal get(int index) {
        return cardinalList.get(index);
    }

    public boolean contains(Cardinal c) {
        return cardinalList.contains(c);
    }    
    
    @Override
    public String toString() {
        return this.cardinalList.toString();
    }

    /**
     * Porownuje RV i List<Cardinal>
     * @param otherRv
     * @return > 0 gdy this jest wieksze na pierwszym roznym elemencie.
     * A, 2, T > A, 2, 8
     */
    @Override
    public int compareTo(List<Cardinal> otherRv) {
        for(int i = 0; i < otherRv.size(); i++) {
            Cardinal otherCardinal = otherRv.get(i);

            // this lista i porownywana maja takie same elementy
            // ale this lista jest krotsza, i wobec tego ma mniejsza "wartosc"
            if(this.cardinalList.size() == i) {
                return -otherCardinal.ordinal();
            }

            Cardinal thisCardinal = cardinalList.get(i);

            // jezeli sa rowne to przechodzimy do nastepnego elementu
            if(otherCardinal == thisCardinal) {
                continue;
            } else {
                // jezeli rozne to zwracamy roznice
                return thisCardinal.ordinal() - otherCardinal.ordinal();
            }
        }

        // mozliwe ze this lista jest dluzsza
        // wtedy ma wieksza wartosc
        if(this.cardinalList.size() > otherRv.size()) {
            return this.cardinalList.get(otherRv.size()).ordinal();
        } else {
            return 0;
        }
    }

    /**
     * Porownuje 2 listy z cardinalami.
     * @param l1
     * @param l2
     * @return >0 wtw. l1 > l2. np A, 2, T > A, 2, 8.
     */
    public static int compare(List<Cardinal> l1, List<Cardinal> l2) {
        for(int i = 0; i < l2.size(); i++) {
            Cardinal l2Cardinal = l2.get(i);

            // this lista i porownywana maja takie same elementy
            // ale this lista jest krotsza, i wobec tego ma mniejsza "wartosc"
            if(l1.size() == i) {
                return -l2Cardinal.ordinal();
            }

            Cardinal l1Cardinal = l1.get(i);

            // jezeli sa rowne to przechodzimy do nastepnego elementu
            if(l2Cardinal == l1Cardinal) {
                continue;
            } else {
                // jezeli rozne to zwracamy roznice
                return l1Cardinal.ordinal() - l2Cardinal.ordinal();
            }
        }

        // mozliwe ze this lista jest dluzsza
        // wtedy ma wieksza wartosc
        if(l1.size() > l2.size()) {
            return l1.get(l2.size()).ordinal();
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        TableCards tc = new TableCards("TcJsQsKcAd");

        List<Cardinal> l1 = new ArrayList<Cardinal>();
        List<Cardinal> l2 = new ArrayList<Cardinal>();

        l1.add(Cardinal.TEN);
        l1.add(Cardinal.ACE);
        l1.add(Cardinal.TWO);

        l2.add(Cardinal.TEN);
        l2.add(Cardinal.ACE);
        l2.add(Cardinal.FOUR);

        int compare = compare(l1, l2);
        System.out.println(compare);

    }
}
