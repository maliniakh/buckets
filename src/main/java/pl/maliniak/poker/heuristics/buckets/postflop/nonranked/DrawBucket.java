package pl.maliniak.poker.heuristics.buckets.postflop.nonranked;

import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;

/**
 * Bucket do reprezentacji draw, zapewne tylko straight i flush.
 * @author meliniak
 */
public abstract class DrawBucket extends PostflopBucket implements Comparable<DrawBucket> {

    public DrawBucket() {
    }
    
    public DrawBucket(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    @Override
    public int compareTo(DrawBucket o) {
        // po prostu, na podstawie nazwy klasy, nie zalezy nam tutaj na kolejnosci sennsownej
        return this.getClass().getName().compareTo(o.getClass().getName());
    }
}
