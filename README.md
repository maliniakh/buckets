The project was done as a part of my M.Sc (learning by imitation applied to poker) thesis. Buckets in poker AI terminology stand for a way of grouping card evaluations. Consider such two example hands:

AT @ AJ4
A9 @ AJ4

In terms of pure hand evaluations, those hands have different values. Since they are very similar, they might be treated as simply the same. Hence idea of 'bucketing' hands. In this case, both of them could be assigned to bucket called, say, TopPairBucket. Such buckets not only make some computation easier, but they are far way similar to way people perceive card values.

In this implementation, each bucket is assosiated with a single class, like StraightDraw, TwoPairs, Trips and so on. One might add any bucket he likes, by adding a new class and implementing the belongsTo method, which return true, if provided hole cards and table cards belong to this bucket.

It supports buth preflop and postflop buckets. Postflop buckets are parameterless, preflop ones come with constructor with arguments, like:

new PairBucket(new CardinalRange(Cardinals.6, Cardinals.TEN))

which stands for following hole cards: 66, 77, 88, 99, TT.

One can extend existing implementation of PostflopBucketSetProvider, for example to use some kind of look-up table to speed up computations.

Buckets hierarchy:

Example usage:

PostflopBucketSetProvider pfbsProvider = new PostflopBucketSetProviderGPImpl();
TableCards tc = new TableCards("As2h4c");
HoleCards hc = new HoleCards("4sKs");
PostflopBucketSet pfbs = pfbsProvider.getBuckets(tc, hc); // returns SecondHighPair_BackdoorFlushDraw

See unit tests for more examples.

Please note this project uses this code:

https://code.google.com/p/cspoker/source/browse/trunk/common/handeval/src/main/java/org/cspoker/common/handeval/stevebrecher/HandEval.java

for pure hand evaluation (i.e. assigning numerical values to card combinations).

It requires Java 1.8 or higher.

Unfortunately, at least for now, most comments (and the comment coverege in the code is pretty high) are non-english.