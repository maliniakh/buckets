package pl.maliniak.poker.heuristics.buckets.postflop.comp;

import java.util.Comparator;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

/**
 * Najsensowniejsze sortowanie. Rankingowane po rankingu karty, draw w uporzadkowany
 * sposob, i obie te grupy oddzielnie.
 * @author meliniak
 */
public class PFBComparatorNaturalImpl implements Comparator<PostflopBucket> {

    public PFBComparatorNaturalImpl() {
    }

    @Override
    public int compare(PostflopBucket o1, PostflopBucket o2) {
        // rozbijamy na przypadki, bo nie mozemy po prostu porownac DrawBucket z
        // RankedPostFlopBucket, a o to nam chodzi
        // ranked sortujemy oddzielnie, draw oddzielnie, a gdy mamy jeden ten a
        // drugi ten to ustawiamy stala roznice
        if(o2 instanceof DrawBucket && o1 instanceof DrawBucket) {
            // arbitralnie, w kolejnosci leksykalnej
            return o2.getClass().getName().compareTo(o1.getClass().getName());
        } else if(o2 instanceof RankedPostFlopBucket && o1 instanceof RankedPostFlopBucket) {
            // rzutujemy i zwracamy roznice w ranku
            RankedPostFlopBucket _o1 = (RankedPostFlopBucket) o1;
            RankedPostFlopBucket _o2 = (RankedPostFlopBucket) o2;

            int diff = _o1.getRank().ordinal() - _o2.getRank().ordinal();
            // jezeli roznica wynosi 10, to musimy sie zatroszczyc o to, zeby
            // kolejnosc i tak byla deterministyczna
            // niech bedzie po alfabecie
            if(diff == 0) {
                return 10 * _o1.getClass().getName().compareTo(_o2.getClass().getName());
            } else {
                return 1000 * diff;
            }
        } else {
            // w ten sposob ranked i draw uporzadkuja sie w oddzielne grupy
            if(o1 instanceof RankedPostFlopBucket) {
                return -100000;
            } else if(o1 instanceof DrawBucket) {
                return +100000;
            } else {
                throw new IllegalStateException("Nie przewidujemy takiej mozliwosci");
            }
        }
    }
}