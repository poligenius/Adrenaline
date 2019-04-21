package it.polimi.sw2019.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Marks extends Tokens {

    /**
     * Default constructor
     */

    public Marks(List<Character> charactersInGame) {
        super(charactersInGame);
    }

    /* Attributes */

    /**
     * max 3 marks from another player
     */
    private List<Character> markSequence = new ArrayList<>();

    /* Methods */

    public List<Character> getMarkSequence() {
        return markSequence;
    }

    /**
     *
     * @param i number of marks you are adding
     * @param opponent Player who is giving marks
     */
    public void addMark(int i, Character opponent) {

        if (getTokensOfCharacter(opponent)<3){
            int newMarks = charactersMap.get(opponent) + i;
            if (newMarks>3){
                i = 3-charactersMap.get(opponent);
            }
            addTokens(i, opponent);
            for (int eachMark = 0; eachMark<i; eachMark++){
                markSequence.add(opponent);
            }
        }
        //TODO exception
    }

    /**
     * Removes the marks of a player
     * @param opponent Player who gave those marks
     */
    public void removeMarks(Character opponent) {
        charactersMap.put(opponent, 0);
        markSequence.removeAll(Collections.singleton(opponent));
    }

}
