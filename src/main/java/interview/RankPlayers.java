package interview;

/*
// You have N players which needs to be ranked. Ranking can be done by organising tournaments.
// Due to limited time and availability for all the N players participating in the tournament,
// you decided to conduct M different tournaments, each seeing participation from K (out of N) players.
// A player might participate in multiple such small tournaments. You have the results of these M tournaments
// in the form of M ordered lists (in a list, player with lower index getting a better rank in the tournament than others).
// Based on these M tournaments, rank all the N players. Any ambiguity results in a “No ranking possible” situation. Return -1 in that case.

/*
i/p
N = 5, M = 3, K = 4
1,0,3,4 => p1 > p0 > p3 > p4
2,0,3,4
1,2,0,3

o/p
1,2,0,3,4

i/p
N = 5, M = 4, K = 4
1,0,3,4
2,0,3,4
1,2,0,3
2,1,3,4

o/p
-1
*/

/*
Iterate over the tournaments

Lets have first array as it's and start with second array
1, 0 , 3, 4
1, 2, 0, 3, 4
return -1


1,0,3,4
2,0,3,4
1,2,0,3

Processing:
1, 2, 0, 3, 4

N = 8
1 0 3 4
1 2 5 6
7 1 3 6

 5 < 2 < 1

 1,0,3,4
 2,0,3,4
 1,2,0,3
  BetterMap:
    1 -> null
    0 -> 1, 2
    2 -> 1
    3 -> 0, 1, 2
    4 -> 0, 1, 2, 3
  LowerMap:
    1 -> 0, 3, 4
    0 -> 3 , 4
    3 -> 4
    2 -> 0 ,3, 4
    4 -> null
Check in lower map which does not exist that means there is no player on top of it
If you have more than one player or there is no player that means it's a ambiguity

1 -> 2 ->  -> 4
Now i have first player or root


Iterate based on size of set
      Iterate over better map
      first key is : 2
      2 ,5

     7
     1
    0 2  3
    3  5  6
    4  6



*/

import java.util.*;

public class RankPlayers {

    public static void main(String[] args) {
        RankPlayers sol = new RankPlayers();
        List<List<Integer>> tournaments = new ArrayList<>();
        tournaments.add(Arrays.asList(new Integer[]{1, 0, 3, 4}));
        tournaments.add(Arrays.asList(new Integer[]{2, 0, 3, 4}));
        tournaments.add(Arrays.asList(new Integer[]{1, 2, 0, 3}));
        List<Integer> result = sol.rankPlayers(5, tournaments);
        System.out.println(result);
    }

    private List<Integer> rankPlayers(Integer noOfPlayers, List<List<Integer>> tournaments) {
        List<Integer> result = new ArrayList<>();
        if (noOfPlayers == null) {
            return null;
        } else if (noOfPlayers == 1) {
            result.add(0);
            return result;
        }
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (final List<Integer> players : tournaments) {
            for (int i = 0; i < players.size(); i++) {
                Integer player = players.get(i);
                for (int j = i + 1; j < players.size(); j++) {
                    map.computeIfAbsent(player, (k) -> new HashSet<>()).add(players.get(j));
                }
            }
        }
        Set<Integer> done = new HashSet<>();
        for (int i = 0; i < noOfPlayers; i++) {
            boolean isAmbiguity = isAmbiguity(map, noOfPlayers, done);
            if (isAmbiguity) {
                return null;
            }
            Integer lastPlayer = getLastPlayer(map, noOfPlayers, done);
            System.out.println(lastPlayer);
            result.add(lastPlayer);
            removePlayer(map, lastPlayer);
            done.add(lastPlayer);
            System.out.println(map);
        }
        Collections.reverse(result);
        return result;
    }

    private void removePlayer(Map<Integer, Set<Integer>> map, Integer prevPlayer) {
        for (final Map.Entry<Integer, Set<Integer>> entry : map.entrySet()) {
            entry.getValue().remove(prevPlayer);
        }
    }

    private Integer getLastPlayer(Map<Integer, Set<Integer>> map, Integer noOfPlayers, Set<Integer> done) {
        for (int i = 0; i < noOfPlayers; i++) {
            if (!done.contains(i) && !map.containsKey(i)) {
                return i;
            }
        }
        //Ideally this should not happen because i am checking isAmbiguity before calling this method
        return null;
    }

    private boolean isAmbiguity(Map<Integer, Set<Integer>> map, Integer noOfPlayers, Set<Integer> done) {
        int noHigherPlayers = 0;
        for (int i = 0; i < noOfPlayers; i++) {
            if (!done.contains(i) || !map.containsKey(i) || map.get(i).size() == 0) {
                noHigherPlayers++;
            }
        }
        return noHigherPlayers == 0 || noHigherPlayers > 1;
    }
}
