package pl.maliniak.poker.cardsets;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.cards.CardsHelper;
import pl.maliniak.cards.Deck;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.street.StreetName;

/**
 * Zbior kart na stole.
 * @author meliniak
 */
public class TableCards extends AbstractCardSet {

    public static final long serialVersionUID = 981L;
    /**
     * Indeksy pod ktorymi przechowywane sa karty.
     */
    public static final int FLOP0_CARD = 0;
    public static final int FLOP1_CARD = 1;
    public static final int FLOP2_CARD = 2;
    public static final int TURN_CARD = 3;
    public static final int RIVER_CARD = 4;

    public TableCards() {
    }

    public TableCards(String str) {
        super(str);
    }

    public TableCards(Card ... cards) {
        if(cards.length < 1 || cards.length > 5) {
            throw new IllegalArgumentException("cards.length == " + cards.length) {
                @Override
                public void printStackTrace() {
                    System.out.println("I CHUJ");
                }
            };
        }

        for (Card c : cards) {
            this.add(c);
        }
    }

    @Override
    public int capacity() {
        return 5;
    }

    /**
     * @param flop Karty do ustawienia jako flop.
     */
    public void setFlopCards(FlopCards flop) {
        // jezeli trzeba, to powiekszamy do rozmiaru 3
        if (super.cards.size() < flop.capacity()) {
            while(super.cards.size() < 3) {
                super.cards.add(null);
            }
        }

        // ustawiamy karty
        super.cards.set(0, flop.get(0));
        super.cards.set(1, flop.get(1));
        super.cards.set(2, flop.get(2));
    }

    /**
     * @param card Karta do ustawienia na turn.
     */
    public void setTurnCard(Card card) {
        if (super.cards.size() < 4) {
            while(super.cards.size() < 4) {
                super.cards.add(null);
            }
        }

        super.cards.set(TURN_CARD, card);
    }

    /**
     * @param card Karta do ustawienia na river.
     */
    public void setRiverCard(Card card) {
        if (super.cards.size() < 5) {
            while(super.cards.size() < 5) {
                super.cards.add(null);
            }
        }

        super.cards.set(RIVER_CARD, card);
    }

    /**
     * Zwraca nowa instancje kart na stole, jakie byly na zadanym streecie.
     * Metoda toleruje nieprawidlowe argumenty. Np. gdy na stole sa tylko karty z flopu,
     * a argumentem jest RIVER, to metoda nie bedzie miala efektu.
     * @param street Dany street.
     * @return Karty na stole w danym streecie.
     */
    public TableCards trimCards(StreetName street) {
        TableCards tc;

        tc = this.newInstance();
        if (street.ordinal() < StreetName.RIVER.ordinal()) {
            tc.trimCardVector(4);
        }

        if (street.ordinal() < StreetName.TURN.ordinal()) {
            tc.trimCardVector(3);
        }

        if (street.ordinal() < StreetName.FLOP.ordinal()) {
            tc.trimCardVector(0);
        }

        return tc;
    }

    /**
     * Sprawdza mozliwosc wystapienia flush draw.
     * TODO: zastanowic sie, czy zwracac wyniki tylko dla 2 kart tego samego
     * TODO: koloru na stole, czy dla >= 2
     * @param holeCardsSet Wynikowy zbior rak, ktore tworza draw. Moze byc null.
     * @return True, jezeli draw jest mozliwy.
     */
    public boolean is2CardFlushDrawPossible(HoleCardsSet handCardsSet) {
        boolean isPossible = false;

        if (handCardsSet == null) {
            handCardsSet = new HoleCardsSet();
        }

        Map<Suit, Integer> suit2NumberMap = this.getSuit2NumberMap();
        Set<Suit> keySet = suit2NumberMap.keySet();
        HoleCards handCards;
        Card card1, card2;
        int number;
        for (Suit suit : keySet) {
            number = suit2NumberMap.get(suit);

            // gdy kolor wystepuje 2 razy, mamy mozliwosc draw
            if (number == 2) {
                isPossible = true;

                card1 = new Card(Cardinal.X, suit);
                card2 = new Card(Cardinal.X, suit);
                handCards = new HoleCards(card1, card2);

                handCardsSet.add(handCards);
            }
        }

        return isPossible;
    }

    /**
     * Sprawdza mozliwosc wystapienia flush draw z 1 karta na rece.
     * @param holeCardsSet Wynikowy zbior rak, ktore tworza draw. Moze byc null.
     * @return True, jezeli draw jest mozliwy.
     */
    public boolean is1CardFlushDrawPossible(HoleCardsSet handCardsSet) {
        boolean isPossible = false;

        if (handCardsSet == null) {
            handCardsSet = new HoleCardsSet();
        }

        Map<Suit, Integer> suit2NumberMap = this.getSuit2NumberMap();
        Set<Suit> keySet = suit2NumberMap.keySet();
        HoleCards handCards;
        Card card1, card2;
        int number;
        for (Suit suit : keySet) {
            number = suit2NumberMap.get(suit);

            // gdy kolor wystepuje 3 razy, mamy mozliwosc draw
            if (number == 3) {
                isPossible = true;

                card1 = new Card(Cardinal.X, suit);
                card2 = new Card(Cardinal.X, Suit.X);
                handCards = new HoleCards(card1, card2);

                handCardsSet.add(handCards);
            }
        }

        return isPossible;
    }

    /**
     * @return True, gdy flushdraw jest mozliwy.
     */
    public boolean isFlushDrawPossible() {
        boolean is1CardFlushDrawPossible = this.is1CardFlushDrawPossible(null);
        boolean is2CardFlushDrawPossible = this.is2CardFlushDrawPossible(null);

        return is1CardFlushDrawPossible || is2CardFlushDrawPossible;
    }

    /**
     * @param handCardsSet
     * @return 
     */
    public boolean isOpenEndedStraightDrawPossible(HoleCardsSet handCardsSet) {
        // OPT: pewnie mozna cala metoda jakos zoptymalizowac
        boolean drawPossible = false;

        // itereujemy po kartach, -6 bo
        Cardinal[] cardinalsStraight = Cardinal.valuesStraight();
        Set<Cardinal> numberOfEnclosedCards;                                          // ile kart jest zawartych w oknie
        boolean containsBorderCards;                                            // czy w oknie sa karty 0 i ostatnie
        // jak sa to odrzucamy
        HoleCards drawHand;                                                     // reka ktora tworzy draw
        int bottomOrdinal, topOrdinal;                                          // ograniczenia indeksow kart, takie tam
        Cardinal bottomCardinal, topCardinal;                                   // to samo tylko wartosci kart
        Cardinal drawCardinal;
        for (int i = 0; i < cardinalsStraight.length - 5; i++) {
            // iterujemy po kartach w secie i sprawdzamy ktora sie zawiera w aktualnym oknie
            // a dokladnie miedzy 1 a 5 pozycja, skrajne musza byc puste, w sensie te karty
            // musza nie wystepowac
            bottomCardinal = cardinalsStraight[i];
            topCardinal = cardinalsStraight[i + 5];
            // taki motywik bo powinismy uzyskiwac -1 dla dolnego asa a dostajemy 14
            bottomOrdinal = (cardinalsStraight[i] == Cardinal.ACE && i == 0) ? -1 : bottomCardinal.ordinal();
            topOrdinal = topCardinal.ordinal();
            numberOfEnclosedCards = new HashSet<Cardinal>();
            for (Card card : this) {
                final Cardinal cardinal = card.getCardinal();
                if (cardinal.ordinal() > bottomOrdinal && cardinal.ordinal() < topOrdinal) {
                    numberOfEnclosedCards.add(cardinal);
                }
            }

            // czy sa karty gracznine w oknie
            containsBorderCards = false;
            containsBorderCards = ((cardinalsStraight[i] == Cardinal.ACE) ? false : this.contains(cardinalsStraight[i])) || this.contains(cardinalsStraight[i + 5]);

            if ((numberOfEnclosedCards.size() == 2 || numberOfEnclosedCards.size() == 3) && containsBorderCards == false) {
                // pozostaje podac karty, ktorych brakuje do draw
                // iterujemy, sprawdzamy i dodajemy
                drawHand = new HoleCards();
                for (int k = i + 1; k < i + 5; k++) {
                    drawCardinal = cardinalsStraight[k];

                    if (this.contains(drawCardinal) == false) {
                        drawHand.add(new Card(drawCardinal, Suit.X));
                    }
                }

                // mamy jedna karte do stritu, druga moze byc dowolna
                if (drawHand.size() == 1) {
                    drawHand.add(new Card(Cardinal.X, Suit.X));
                }

                if (handCardsSet != null) {
                    handCardsSet.add(drawHand);
                }
                drawPossible = true;
            }
        }

        return drawPossible;
    }

    // <editor-fold defaultstate="collapsed" desc="is2HcuStraightPossible(...)">
//    public boolean is2HcuStraightPossible(HoleCardsSet handCardsSet) {
//        boolean straightPossible = false;
//
//        // itereujemy po kartach, -6 bo
//        Cardinal[] cardinalsStraight = Cardinal.valuesStraight();
////        int numberOfEnclosedCards = 0;                                          // ile kart jest zawartych w oknie
////        boolean containsBorderCards;                                            // czy w oknie sa karty 0 i ostatnie
//        // jak sa to odrzucamy
//        HoleCards straightHand;                                                     // reka ktora tworzy strita
//        int bottomOrdinal, topOrdinal;                                          // ograniczenia indeksow kart, takie tam
//        Cardinal bottomCardinal, topCardinal;                                   // to samo tylko wartosci kart
//        Cardinal drawCardinal;
//        for(int i = 0; i < cardinalsStraight.length - 5; i++) {
//            // iterujemy po kartach w secie i sprawdzamy ktora sie zawiera w aktualnym oknie
//            // a dokladnie miedzy 1 a 5 pozycja, skrajne musza byc puste, w sensie te karty
//            // musza nie wystepowac
//            bottomCardinal = cardinalsStraight[i];
//            topCardinal = cardinalsStraight[i + 4];
//            // taki motywik bo powinismy uzyskiwac -1 dla dolnego asa a dostajemy 14
//            bottomOrdinal = (cardinalsStraight[i] == Cardinal.ACE && i == 0) ? -1 : bottomCardinal.ordinal();
//            topOrdinal = topCardinal.ordinal();
//            // zbior cardinals z boardu ktore ukladaja sie do seta
//            Set<Cardinal> enclosedCardinals = new HashSet<Cardinal>();
////            numberOfEnclosedCards = 0;
//            for(Card card : this) {
//                // as w pierwszej iteracji, czyli dla strita A-5
//                if(card.getCardinal() == Cardinal.ACE && i == 0) {
//                    enclosedCardinals.add(card.getCardinal());
//                    continue;
//                }
//                int cardOrdinal = card.getCardinal().ordinal();
//
//                if(cardOrdinal >= bottomOrdinal && cardOrdinal <= topOrdinal) {
//                    enclosedCardinals.add(card.getCardinal());
//                }
//            }
//
//            // czy sa karty gracznine w oknie
////            containsBorderCards = false;
////            containsBorderCards = ((cardinalsStraight[i] == Cardinal.ACE) ? false : this.contains(cardinalsStraight[i])) || this.contains(cardinalsStraight[i + 5]);
//
////            if((numberOfEnclosedCards == 2 || numberOfEnclosedCards == 3)&& containsBorderCards == false) {
//            if(enclosedCardinals.size() == 3) {
//                // pozostaje podac karty, ktorych brakuje do draw
//                // iterujemy, sprawdzamy i dodajemy
//                straightHand = new HoleCards();
//
//                for(int k = i + 1; k < i + 5; k++) {
//                    drawCardinal = cardinalsStraight[k];
//
//                    if(this.contains(drawCardinal) == false) {
//                        straightHand.add(new Card(drawCardinal, Suit.X));
//                    }
//                }
//
//                // mamy jedna karte do stritu, druga moze byc dowolna
//                if(straightHand.size() == 1) {
//                    straightHand.add(new Card(Cardinal.X, Suit.X));
//                }
//
//                if(handCardsSet != null) {
//                    handCardsSet.add(straightHand);
//                }
//                straightPossible = true;
//            }
//        }
//
//        return straightPossible;
//    }// </editor-fold>
    /**
     * wolno dziala niemilosiernie.
     * <b>dlatego korzystac z {@link LUTCardinals}, stad tez widocznosc package</b>.
     * Czy 2 kartowy strit jest mozliwy.
     * @param holeCardsSet Kolekcja ktora zostanie wypelniona zbiorami kart na rece,
     * ktore ukladaja sie w strita.
     * @return
     */
    boolean is2HcuStraightPossible(HoleCardsSet handCardsSet) {
        HoleCardsSet possibleHcs = CardsHelper.generatePossibleHoleCards(this);

        // przed flopem naturalnie nie
        if (this.size() == 0) {
            return false;
        }

        if (this.size() == 5) {
            final Rank rank = this.getRank();
            final RankEnum pokerRank = rank.getPokerRank();
            if (pokerRank == RankEnum.FLUSH) {
                // jak jest juz flush to rzecz jasna 2hcu strit nie moze byc
                return false;
            }

            if (pokerRank == RankEnum.STRAIGHT) {
                // jak jest juz strit to trzeba sprawdzic czy jest miejsce na 
                // 2 dodatkowe wyzsze karty
                List<Cardinal> rankValues = rank.getRankValues().getTrimmedValues();
                assert rankValues.size() == 1;

                if (rankValues.get(0).ordinal() <= Cardinal.QUEEN.ordinal()) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        HoleCardsSet hcu1StraightHcs = new HoleCardsSet();
        boolean hcu1StraightPossible = this.is1HcuStraightPossible(hcu1StraightHcs);
        // potrzebny do wyeliminowania wszystkich ukladow ktore daja 1hcu straight
        HoleCardsSet tempHcs = new HoleCardsSet();

        // sprawdzamy, ktory mozliwy uklad kart na rece daje strita
        for (HoleCards hc : possibleHcs) {
            HoleWTableCards hctc = new HoleWTableCards(hc, this);

            Rank rank = hctc.getRank();
            RankEnum pokerRank = rank.getPokerRank();

            if (pokerRank == RankEnum.STRAIGHT) {
                tempHcs.add(hc);
            }
        }

        // usuwamy wszystkie hc, ktore daja tez 1kartowego strita
        HoleCardsSet diffHcs = new HoleCardsSet(tempHcs);
        if (hcu1StraightPossible == true) {
            SetView<HoleCards> difference = Sets.difference(diffHcs, hcu1StraightHcs);
            diffHcs = new HoleCardsSet(difference);
        }

        // niezaleznie od wyniku wypelniamy wynikowy zbior kart
        if (handCardsSet != null) {
            handCardsSet.addAll(diffHcs);
        }

        // na podstawie roznicy mozna stwierdzic czy 2hcu straight jest mozliwy
        if (diffHcs.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Czy 1 kartowy strit jest mozliwy.
     * <b>dlatego korzystac z {@link LUTCardinals}, stad tez widocznosc package</b>
     * @param hcs Kolekcja ktora zostanie wypelniona zbiorami kart na rece,
     * ktore ukladaja sie w strita.
     * @return
     */
     boolean is1HcuStraightPossible(HoleCardsSet hcs) {
        
        List<Card> deck = Deck.getInstance().getCards();
        boolean strPossible = false;

        // nie flopie ani preflopie nie moze byc 1hcu strita
        if (this.size() <= 3) {
            return false;
        }

        if (this.size() == 5) {
            final Rank rank = this.getRank();
            final RankEnum pokerRank = rank.getPokerRank();

            if (pokerRank == RankEnum.FLUSH || pokerRank == RankEnum.FULL_HOUSE) {
                // jak jest juz flush lub fh to rzecz jasna 2hcu strit nie moze byc
                return false;
            }
        }

        // czy mamy strita na boardzie
        // mozemy go miec tylko gdy ilosc kart na stole == 5
        boolean straightOnBoard = false;
        if (this.size() == 5) {
            Rank rank = this.getRank();
            RankEnum pokerRank = rank.getPokerRank();
            if (pokerRank == RankEnum.STRAIGHT) {
                straightOnBoard = true;
            }


            // jak tak to jest mozliwy strit jednokartowy od gory, chyba ze jest to
            // strit T-A
            if (straightOnBoard) {
                // najwyzsza karta w sekwencji strita
                Cardinal strCardinal = rank.getRankValues().get(0);
                if (strCardinal == Cardinal.ACE) {
                    // czyli strit T-A
                    return false;
                }

                // strit w takiej sytuacji 1 kartowy to taki, przy ktorym ta karta
                // dajaca strita jest karta o 1 wyzsza o najwyzszej na boardzie
                int strOrdinal = strCardinal.ordinal();
                Cardinal str1HcuCardinal = Cardinal.getCardinalByOrdinal(strOrdinal);
                for (Suit s : Suit.valuesNoX()) {
                    Card str1HcuCard = new Card(str1HcuCardinal, s);

                    if (this.contains(str1HcuCard)) {
                        continue;
                    }

                    HoleCardsSet str1HcuHoleCardsSet = CardsHelper.generate1CardHoleCardsSet(str1HcuCard, this);
                    for (HoleCards str1HcuHc : str1HcuHoleCardsSet) {
                        if (this.contains(str1HcuCard)) {
                            continue;
                        }

                        HoleWTableCards hctc = new HoleWTableCards(str1HcuCard, this);

                        // sprawdzamy czy przsypadkiem dane HC nie daja wyzszego
                        // strita albo koloru np
                        Rank r = hctc.getRank();
                        RankEnum pr = r.getPokerRank();
                        if (pr == RankEnum.STRAIGHT && r.getRankValues().get(0) == str1HcuCardinal) {
                            if (hcs != null) {
                                hcs.add(str1HcuHc);
                            }
                        }
                    }
                }

                return true;
            }
        }
        
        // sprawdzamy czy z ktorakolwiek karta z talii mozna utworzyc
        for (Card c : deck) {
            // czy karta jest w TC
            if (this.contains(c)) {
                continue;
            }

            TableCards tc = this.newInstance();
            tc.add(c);

            // sprawdzamy czy przsypadkiem dane HC nie daja wyzszego
            // strita albo koloru np
            Rank r = tc.getRank();
            RankEnum pr = r.getPokerRank();
            if (pr == RankEnum.STRAIGHT) {
                strPossible = true;

                if (hcs != null) {
                    HoleCardsSet str1HcuHcs = CardsHelper.generate1CardHoleCardsSet(c, this);

                    for (HoleCards hc : str1HcuHcs) {
                        hcs.add(hc);
                    }
                }
            }
        }

        return strPossible;
    }

    /**
     * @param hcs
     * @return True jezeli jest mozliwy flush 2 hcu
     */
    public boolean is2HcuFlushPossible(HoleCardsSet hcs) {
        if (this.size() == 0) {
            return false;
        }

        Suit2NumberMap suit2NumberMap = this.getSuit2NumberMap();
        int maxSuitNumber = suit2NumberMap.getMaxSuitNumber();

        if (maxSuitNumber == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is1HcuFlushPossible(HoleCardsSet hcs) {
        if (this.size() == 0) {
            return false;
        }

        Suit2NumberMap suit2NumberMap = this.getSuit2NumberMap();
        int maxSuitNumber = suit2NumberMap.getMaxSuitNumber();

        if (maxSuitNumber == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGutShotStraightDrawPossible(HoleCardsSet handCardsSet) {
        throw new UnsupportedOperationException("Do zaimplementowania");
    }

    /**
     * @return True, gdy jakikolwiek straight draw jest mozliwy.
     */
    public boolean isStraightDrawPossible() {
        return this.isOpenEndedStraightDrawPossible(null)
                && this.isGutShotStraightDrawPossible(null);
    }

    public boolean isFhOrQuadsPossible() {
        List<Cardinal> cardinalsNonUnique = this.getCardinalsSorted(false);
        List<Cardinal> cardinalsUnique = this.getCardinalsSorted(true);

        // gdy te ilosci sie nie zgadzaja to znaczy ze fh mozliwe 
        if (cardinalsNonUnique.size() != cardinalsUnique.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return True jezeli na stole jest full house.
     */
    public boolean isFullHouseOnBoard() {
        if (this.size() != 5) {
            return false;
        }

        RankEnum pokerRank = this.getRank().getPokerRank();
        if (pokerRank == RankEnum.FULL_HOUSE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return True gdy sa 3 takie same cardinale na stole (ale nie ma FH).
     */
    public boolean isTripsOnBoard() {
        if (this.isFullHouseOnBoard()) {
            return false;
        }

        List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);
        for (Cardinal cardinal : cardinalsSorted) {
            int cardinalCount = this.getCardinalCount(cardinal);

            if (cardinalCount == 3) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return True gdy sa 2 parki na stole.
     */
    public boolean isTwoPairOnBoard() {
        if (this.isTripsOnBoard() || this.isFullHouseOnBoard()) {
            return false;
        }

        boolean pairFound = false;
        List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);
        for (Cardinal cardinal : cardinalsSorted) {
            int cardinalCount = this.getCardinalCount(cardinal);

            if (cardinalCount == 2) {
                if (pairFound == false) {
                    // jest 1 parka, szukamy drugiej
                    pairFound = true;
                    continue;
                } else {
                    // jest druga parka
                    return true;
                }
            }
        }

        // jezeli doszlismy tutaj to znaczy ze nie ma 2 parek
        return false;
    }

    public boolean isPairOnBoard() {
        List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);
        if (this.size() - cardinalsSorted.size() == 1) {
            // 1 cardinal sie powtarza
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return Czy karty na stole sie nie uklada w zaden uklad pokerowy (bez parek,
     * kolorow i stritow).
     */
    public boolean isHighCardOnBoard() {
        if (this.size() == 5) {
            // jak jest 5 kart to mozemy przeliczyc uklad na stole
            RankEnum pokerRank = this.getRank().getPokerRank();

            if (pokerRank == RankEnum.HIGH_CARD) {
                return true;
            } else {
                // jakis wyzszy uklad na stole
                return false;
            }
        }

        if (this.isPairOnBoard() || this.isTwoPairOnBoard() || this.isTripsOnBoard() || this.isFullHouseOnBoard()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return Gdy na stole jest strit.
     */
    public boolean isStraightOnBoard() {
        if (this.size() == 5) {
            RankEnum pokerRank = this.getRank().getPokerRank();

            if (pokerRank == RankEnum.STRAIGHT) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isFlushOnBoard() {
        if (this.size() == 5) {
            RankEnum pokerRank = this.getRank().getPokerRank();

            if (pokerRank == RankEnum.FLUSH) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * @return True gdy sa 4 takie same cardinale na stole (nawet bez 4 kart
     * na stole).
     */
    public boolean isQuadsOnBoard() {
        List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);
        for (int i = 0; i < cardinalsSorted.size(); i++) {
            Cardinal cardinal = cardinalsSorted.get(i);

            int cardinalCount = this.getCardinalCount(cardinal);
            if (cardinalCount == 4) {
                return true;
            }
        }

        return false;
    }

    public boolean isStraightFlushOnBoard() {
        if (this.size() == 5) {
            RankEnum pokerRank = this.getRank().getPokerRank();

            if (pokerRank == RankEnum.STRAIGHT_FLUSH) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Cos jakby RankValues.getTrimmedCardinals. Przy parce na boardzie zwraca
     * cardinal parki, przy dwoch parkach - 2 cardinale (odpowiedniej kolejnosci),
     * przy stricie cardinal najwyzszej karty itp.
     * @return Cos
     */
    public List<Cardinal> getSignificantCardinals() {
        if (this.size() == 5) {
            // jak mamy 5 kart to korzystamy z rankValues.getTrimmedValues
            Rank rank = this.getRank();
            RankValues rankValues = rank.getRankValues();
            List<Cardinal> trimmedValues = rankValues.getTrimmedValues();

            return trimmedValues;
        } else {
            if (this.isTwoPairOnBoard()) {
                List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);

                Iterable<Cardinal> twoPairCardinals = Iterables.filter(cardinalsSorted, new Predicate<Cardinal>() {

                    @Override
                    public boolean apply(Cardinal input) {
                        int cardinalCount = TableCards.this.getCardinalCount(input);
                        if (cardinalCount == 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                List<Cardinal> resultList = new ArrayList<Cardinal>();
                Iterables.addAll(resultList, twoPairCardinals);

                // czy kolejnosc jest dobra czy tez trzeba odwrocic
                if (resultList.get(0).ordinal() < resultList.get(1).ordinal()) {
                    Collections.reverse(resultList);
                }

                return resultList;
            } else if (this.isPairOnBoard()) {
                List<Cardinal> cardinalsSorted = this.getCardinalsSorted(true);

                Iterable<Cardinal> pairCardinal = Iterables.filter(cardinalsSorted, new Predicate<Cardinal>() {

                    @Override
                    public boolean apply(Cardinal input) {
                        int cardinalCount = TableCards.this.getCardinalCount(input);
                        if (cardinalCount == 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                List<Cardinal> resultList = new ArrayList<Cardinal>();
                Iterables.addAll(resultList, pairCardinal);

                return resultList;
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    /**
     * Przycina wektor z kartami AbstractCardSet.cards.
     * Cos jak Vector.setSize(int k) tylko bez efektu, gdy nowy rozmiar jest wiekszy
     * od aktualnego - vector wypelnia sie nullami.
     * @param size Rozmiar do jakiego wektor ma zostac przyciety.
     */
    public void trimCardVector(int size) {
        if (super.cards.size() > size) {
            // usuwamy elementy od konca az do skutku
            while(super.cards.size() > size) {
                super.cards.remove(super.cards.size() - 1);
            }
        }
    }

    /**
     * @return Street na podstawie ilosci kart.
     */
    public StreetName getStreet() {
        int size = this.size();
        if (size == 0) {
            return StreetName.PREFLOP;
        } else if (size == 3) {
            return StreetName.FLOP;
        } else if (size == 4) {
            return StreetName.TURN;
        } else if (size == 5) {
            return StreetName.RIVER;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * @param street
     * @return ilosc kart w tc dla zadanego streeta.
     */
    public static int getNumberOfCards(StreetName street) {
        if(street == StreetName.FLOP) {
            return 3;
        } else if(street == StreetName.TURN) {
            return 4;
        } else if(street == StreetName.RIVER) {
            return 5;
        }
        
        throw new IllegalArgumentException("");
    }
    
    public static void main(String args[]) {
        TableCards tc;
        boolean strPossible;
        String cards;

//        cards = "As3s3c";
//        tc = new TableCards(cards);
//        strPossible = tc.is2HcuStraightPossible(null);
//        System.out.println(cards + "\t" + strPossible);

        cards = "7s4s3c";
        tc = new TableCards(cards);
        strPossible = tc.is2HcuStraightPossible(null);
        System.out.println(cards + "\t" + strPossible);

        cards = "AsKsQcJs";
        tc = new TableCards(cards);
        strPossible = tc.is2HcuStraightPossible(null);
        System.out.println(cards + "\t" + strPossible);

        cards = "AsKs9c";
        tc = new TableCards(cards);
        strPossible = tc.is2HcuStraightPossible(null);
        System.out.println(cards + "\t" + strPossible);

//        boolean drawPossible;
//        HoleCardsSet handCardsSet = new HoleCardsSet();
//        TableCards tableCards = new TableCards();
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.SIX, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.SEVEN, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.EIGHT, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.TWO, Suit.DIAMONDS));
//        Card highestCard = tableCards.getHighestCard();
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.FOUR, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.FIVE, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.TEN, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.ACE, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.THREE, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.EIGHT, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.QUEEN, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.EIGHT, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.NINE, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.TWO, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.ACE, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.TEN, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.FOUR, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.JACK, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.QUEEN, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.FOUR, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.QUEEN, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.KING, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.ACE, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.SEVEN, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.JACK, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.TWO, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
//
//        handCardsSet = new HoleCardsSet();
//        tableCards = new TableCards();
//
//        tableCards.add(new Card(Cardinal.QUEEN, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.KING, Suit.SPADES));
//        tableCards.add(new Card(Cardinal.THREE, Suit.CLUBS));
//        tableCards.add(new Card(Cardinal.SEVEN, Suit.DIAMONDS));
//
//        drawPossible = tableCards.isOpenEndedStraightDrawPossible(handCardsSet);
//        System.out.println("Stol: " + tableCards + " draw mozliwy: " + drawPossible + " karty: " + handCardsSet);
    }
}
