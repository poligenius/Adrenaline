package it.polimi.sw2019.network.messages;

import java.util.List;

public class Players {

    /**
     * Default constructor
     */
    public Players(){}

    /* Attributes */

    private List<Character> characters;

    /* Methods */

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }
}
