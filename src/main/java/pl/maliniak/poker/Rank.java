package pl.maliniak.poker;

import game.eval.stevebrecher.HandEval;
import pl.maliniak.poker.cardsets.AbstractCardSet;

/**
 * Sila reki.
 * @author maliniak
 */
public class Rank implements Comparable<Rank> {
    // wartosci reki

    private int value = 0;
    /**
     * Wartosci kart po kolei, ktore skladaja sie na sile reki.
     * Dla AT @ 3TTA2 to beda np T, A, 3, 2.
     */
    private RankValues rankValues;

    public Rank(int value) {
        this.value = value;

        this.rankValues = new RankValues(this);
    }

    /**
     * Oblicza sile reki.
     * @param cardSet Wejsciowy zbior kart.
     * @return Obliczona sila reki.
     */
    private RankEnum computePokerHand(AbstractCardSet cardSet) {
        throw new UnsupportedOperationException();
    }
    

    public RankEnum getPokerRank() {
        // wartosc reki, np 2 pary, strit itd, z biblioteki steva brechtera
        int rankGroup;

        rankGroup = this.value & 0x0F000000;

        if(rankGroup == 0) {
            return RankEnum.HIGH_CARD;
        } else if(rankGroup == HandEval.PAIR) {
            return RankEnum.PAIR;
        } else if(rankGroup == HandEval.TWO_PAIR) {
            return RankEnum.TWO_PAIRS;
        } else if(rankGroup == HandEval.THREE_OF_A_KIND) {
            return RankEnum.TRIPS;
        } else if(rankGroup == HandEval.STRAIGHT) {
            return RankEnum.STRAIGHT;
        } else if(rankGroup == HandEval.FLUSH) {
            return RankEnum.FLUSH;
        } else if(rankGroup == HandEval.FULL_HOUSE) {
            return RankEnum.FULL_HOUSE;
        } else if(rankGroup == HandEval.FOUR_OF_A_KIND) {
            return RankEnum.QUADS;
        } else if(rankGroup == HandEval.STRAIGHT_FLUSH) {
            return RankEnum.STRAIGHT_FLUSH;
        } else {
            throw new IllegalStateException("Nie mozna rozpoznac kategorii rankingu reki");
        }
    }

    public int getValue() {
        return value;
    }

    public RankValues getRankValues() {
        return rankValues;
    }

    @Override
    public int compareTo(Rank o) {
        return this.value - o.value;
    }

//    public static void main(String[] args) {
//        TableCards tc = new TableCards();
//        Rank rank;
//
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.THREE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.FOUR, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.FIVE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.SIX, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.SEVEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");;
//
//        rank = tc.getRank();tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.NINE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.NINE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.NINE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FOUR, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.NINE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FIVE, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.KING, Suit.CLUBS));
//        tc.add(new Card(Cardinal.KING, Suit.HEARTS));
//        tc.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.NINE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SIX, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SIX, Suit.SPADES));
//        tc.add(new Card(Cardinal.FIVE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.ACE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.ACE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.THREE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.HEARTS));
//        tc.add(new Card(Cardinal.JACK, Suit.SPADES));
//        tc.add(new Card(Cardinal.SIX, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SIX, Suit.SPADES));
//        tc.add(new Card(Cardinal.SIX, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SIX, Suit.SPADES));
//        tc.add(new Card(Cardinal.SIX, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FOUR, Suit.SPADES));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.ACE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FIVE, Suit.HEARTS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FIVE, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");;
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FIVE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FIVE, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.FIVE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TEN, Suit.DIAMONDS));
//        tc.add(new Card(Cardinal.TEN, Suit.HEARTS));
//        tc.add(new Card(Cardinal.TEN, Suit.SPADES));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.SIX, Suit.DIAMONDS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//
//        tc = new TableCards();
//        tc.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        tc.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tc.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        tc.add(new Card(Cardinal.FIVE, Suit.CLUBS));
//        System.out.print(tc);
//        rank = tc.getRank();
//        System.out.println(rank.getPokerRank());
//        System.out.println(rank.getRankValues().toString() + "\n");
//    }
}
