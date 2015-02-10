package pl.maliniak.poker.cardsets;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author meliniak
 */
public class HoleCardsSet extends HashSet<HoleCards> {
    public static final long serialVersionUID = 299L;

    public HoleCardsSet() {
        super();
    }
    
    public HoleCardsSet(Collection<? extends HoleCards> c) {
        super(c);
    }
}
