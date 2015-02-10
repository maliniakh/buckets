package pl.maliniak.poker.cardsets;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.RankValues;
import pl.maliniak.poker.street.StreetName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author meliniak
 */
public class HoleWTableCards extends AbstractCardSet {

    public static final long serialVersionUID = 831L;
    private static Logger logger = LoggerFactory.getLogger(HoleWTableCards.class);
    /**
     * Karty na rece.
     */
    private HoleCards holeCards;
    /**
     * Karty na stole.
     */
    private TableCards tableCards;

    protected HoleWTableCards() {
    }
    
    public HoleWTableCards(HoleCards hc, TableCards tc) {
        super();

        Preconditions.checkNotNull(hc, "hc == null");
        Preconditions.checkNotNull(hc, "tc == null");

        this.holeCards = hc;
        this.tableCards = tc;

        this.add(hc);
        this.add(tc);
    }

    /**
     * used for checking hand when one of the card is not relevant.
     * @param card
     * @param tableCards
     */
    public HoleWTableCards(Card card, TableCards tableCards) {
        super();

        this.tableCards = tableCards;

        this.add(card);
        this.add(tableCards);
    }

    @Override
    public int capacity() {
        return 7;
    }

    @Override
    public <T extends AbstractCardSet> T newInstance() {
        HoleWTableCards newInstance = super.newInstance();
        newInstance.holeCards = holeCards.newInstance();
        newInstance.tableCards = tableCards.newInstance();
        return (T) newInstance;
    }

    /**
     * number of hole cards used to make hand rank.
     * @return 0-2.
     */
    public int getHoleCardsUsed() {
        Rank rank = super.getRank();
        RankEnum pokerRank = rank.getPokerRank();
        RankValues rankValues = rank.getRankValues();

        Card card1 = holeCards.get(0);
        Card card2 = holeCards.get(1);

        Cardinal c1 = card1.getCardinal();
        Cardinal c2 = card2.getCardinal();

        if (pokerRank == RankEnum.HIGH_CARD) {
            int hcu = 0;

            if (rankValues.contains(c1)) {
                hcu++;
            }

            if (rankValues.contains(c2)) {
                hcu++;
            }

            return hcu;
        } else if (pokerRank == RankEnum.PAIR) {
            if (holeCards.isPocketPair()) {
                return 2;
            }

            Cardinal pairCardinal = rankValues.get(0);
            if (holeCards.contains(pairCardinal) == true) {
                return 1;
            } else {
                return 0;
            }
        } else if (pokerRank == RankEnum.TWO_PAIRS) {
            if (holeCards.isPocketPair()) {
                return 2;
            }

            if (tableCards.contains(c1) && tableCards.contains(c2)) {
                return 2;
            }

            if (tableCards.contains(c1) && !tableCards.contains(c2)
                    || !tableCards.contains(c1) && tableCards.contains(c2)) {
                return 1;
            }

            if (!tableCards.contains(c1) && !tableCards.contains(c2)) {
                return 0;
            }

            throw new IllegalStateException("");
        } else if (pokerRank == RankEnum.TRIPS) {
            Cardinal tripsCardinal = rankValues.get(0);
            int cardinalCount = holeCards.getCardinalCount(tripsCardinal);

            return cardinalCount;
        } else if (pokerRank == RankEnum.STRAIGHT
                || pokerRank == RankEnum.STRAIGHT_FLUSH) {
            Cardinal hiStrCardinal = rankValues.get(0);

            int hiStrOrdinal = hiStrCardinal.ordinal();
            int loStrOrdinal;

            List<Cardinal> straightList;

            if (hiStrCardinal == Cardinal.FIVE) {

                straightList = new ArrayList<>();
                straightList.add(Cardinal.ACE);
                straightList.add(Cardinal.TWO);
                straightList.add(Cardinal.THREE);
                straightList.add(Cardinal.FOUR);
                straightList.add(Cardinal.FIVE);
            } else {
                loStrOrdinal = hiStrOrdinal - 4; // like 6 - 4 == 2

                // array and list with cards forming a straight
                Cardinal[] straightArr = Arrays.copyOfRange(Cardinal.valuesNoX(), loStrOrdinal, hiStrOrdinal + 1);
                straightList = Arrays.asList(straightArr);
            }


            int hcu = 0;
            // hcu++ if card is in hole cards but on table
            if (straightList.contains(c1) && tableCards.contains(c1) == false) {
                hcu++;
            }

            // condition for pair in hole cards
            if ((straightList.contains(c2) && tableCards.contains(c2) == false)
                    && holeCards.isPocketPair() == false) {
                hcu++;
            }

            return hcu;
        } else if (pokerRank == RankEnum.FLUSH) {
            int hcu = 0;

            Suit flushSuit = this.getFlushSuit();

            if (rankValues.contains(c1) && card1.getSuit() == flushSuit) {
                hcu++;
            }

            if (rankValues.contains(c2) && card2.getSuit() == flushSuit) {
                hcu++;
            }

            return hcu;
        } else if (pokerRank == RankEnum.FULL_HOUSE) {
            Cardinal fhTripsCardinal = rankValues.get(0);
            Cardinal fhPairCardinal = rankValues.get(1);

            int tripsCardinalTCCount = tableCards.getCardinalCount(fhTripsCardinal);
            int pairCardinalTCCount = tableCards.getCardinalCount(fhPairCardinal);

            if (tableCards.size() == 4) {
                // sytuacje np kkjj
                if (tripsCardinalTCCount == 2 && pairCardinalTCCount == 2) {
                    return 1;
                }
            } else if (tableCards.size() == 5) {
                if (tripsCardinalTCCount == 3 && pairCardinalTCCount == 2) {
                    if (holeCards.isPocketPair() == true) {
                        // like 88@22333
                        if (c1.ordinal() > fhPairCardinal.ordinal()) {
                            return 2;
                        } else {
                            // like 88@99333
                            return 0;
                        }
                    }

                    // like 39@99888 -> hcu == 1
                    if (fhPairCardinal.ordinal() > fhTripsCardinal.ordinal()) {
                        return 1;
                    } else {
                        // like38@99988 -> hcu == 0
                        return 0;
                    }
                }
            }

            int hcu = 0;

            if (c1 == fhTripsCardinal || c1 == fhPairCardinal) {
                hcu++;
            }

            if (c2 == fhTripsCardinal || c2 == fhPairCardinal) {
                hcu++;
            }

            return hcu;
        } else if (pokerRank == RankEnum.QUADS) {
            Cardinal quadsCardinal = rankValues.get(0);
            int cardinalCount = holeCards.getCardinalCount(quadsCardinal);

            return cardinalCount;
        } else {
            throw new IllegalStateException("");
        }
    }

    public TableCards getTableCards() {
        return tableCards;
    }

    /**
     * @return
     */
    public boolean isOpenEndedStraightDraw() {
        Cardinal[] cardinalsStraight = Cardinal.valuesStraight();
        int numberOfEnclosedCards = 0;
        boolean containsBorderCards;
        int bottomOrdinal, topOrdinal;
        Cardinal bottomCardinal, topCardinal;
        for (int i = 0; i < cardinalsStraight.length - 5; i++) {
            bottomCardinal = cardinalsStraight[i];
            topCardinal = cardinalsStraight[i + 5];
            bottomOrdinal = (cardinalsStraight[i] == Cardinal.ACE && i == 0) ? -1 : bottomCardinal.ordinal();
            topOrdinal = topCardinal.ordinal();
            numberOfEnclosedCards = 0;
            for (Cardinal c : this.getCardinalsSorted(true)) {
                if (c.ordinal() > bottomOrdinal && c.ordinal() < topOrdinal) {
                    numberOfEnclosedCards++;
                }
            }

            containsBorderCards = ((cardinalsStraight[i] == Cardinal.ACE) ? false : this.contains(cardinalsStraight[i])) || this.contains(cardinalsStraight[i + 5]);

            if (numberOfEnclosedCards == 4 && containsBorderCards == false) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return
     */
    public boolean isGutShotStraightDraw() {
        if (tableCards.size() == 5) {
            throw new IllegalStateException("draw on river?");
        }

        RankEnum pokerRank = this.getRank().getPokerRank();
        // ignore if there is already a straight
        if (pokerRank == RankEnum.STRAIGHT) {
            return false;
        }
        // there is already a higher rank => false
        if (pokerRank == RankEnum.FLUSH || pokerRank == RankEnum.FULL_HOUSE || pokerRank == RankEnum.STRAIGHT_FLUSH) {
            return false;
        }
        if (this.isOpenEndedStraightDraw() == true) {
            return false;
        }

        Suit2NumberMap s2nm = this.getSuit2NumberMap();
        Suit suit = s2nm.getMinSuitSet().iterator().next();

        int cadinalsToStreetCount = 0;
        for (Cardinal c : Cardinal.valuesNoX()) {
            if(this.contains(c) == true) {
                continue;
            }
            
            HoleWTableCards newHctc = this.newInstance();
            newHctc.add(new Card(c, suit));

            RankEnum rankEnum = newHctc.getRank().getPokerRank();
            if (rankEnum == RankEnum.STRAIGHT) {
                cadinalsToStreetCount++;
            }
        }

        if (cadinalsToStreetCount == 1) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @return
     */
    public boolean is1CardFlushDraw() {
        if (tableCards.size() == 5) {
            throw new IllegalStateException("draw on river?");
        }

        Suit2NumberMap tcSuitMap = this.tableCards.getSuit2NumberMap();
        Suit2NumberMap totalSuitMap = this.getSuit2NumberMap();

        if (tcSuitMap.getMaxSuitNumber() == 3 && totalSuitMap.getMaxSuitNumber() == 4) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is2CardFlusDraw() {
        if (tableCards.size() == 5) {
            throw new IllegalStateException("draw on river?");
        }

        Suit2NumberMap tcSuitMap = this.tableCards.getSuit2NumberMap();
        Suit2NumberMap totalSuitMap = this.getSuit2NumberMap();

        if (tcSuitMap.getMaxSuitNumber() == 2 && totalSuitMap.getMaxSuitNumber() == 4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if there is any kind of draw.
     */
    public boolean isFlushDraw() {
        return this.is1CardFlushDraw() || this.is2CardFlusDraw();
    }

    /**
     * @return
     */
    public boolean isBackdoorFlushDraw() {
        if (tableCards.getStreet() != StreetName.FLOP) {
            throw new IllegalArgumentException();
        }

        Suit2NumberMap tcSuit2NumberMap = tableCards.getSuit2NumberMap();
        Suit2NumberMap hcSuit2NumberMap = holeCards.getSuit2NumberMap();

        for (Suit suit : Suit.valuesNoX()) {
            Integer tcSuitNo = tcSuit2NumberMap.get(suit);
            Integer hcSuitNo = hcSuit2NumberMap.get(suit);

            if (tcSuitNo + hcSuitNo == 3 && hcSuitNo > 0) {
                return true;
            }
        }

        return false;
    }
}
