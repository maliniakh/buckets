package pl.maliniak.poker.heuristics.buckets.postflop;

/**
 * Enumeruje sytuacje w kartach, jakie moze miec graczy(?).
 * @author meliniak
 */
public enum PostflopBucketEnum {
    HIGH_CARD,
    PAIR,
    OVER_PAIR,
    HIGH_PAIR,
    MIDDLE_PAIR,
    LOW_PAIR,
    BETWEEN_PAIR,
    TRIPS,
    STRAIGTH_DRAW,
    FLUSH_DRAW,
    ;
}
