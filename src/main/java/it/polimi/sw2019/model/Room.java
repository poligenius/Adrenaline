package it.polimi.sw2019.model;

import java.util.ArrayList;
import java.util.List;

public class Room {

    /**
     * Default constructor
     */
    public Room(){

    }

    /* Attributes */

    private Colors color;

    private SpawnCell spawnCell;

    private List<Cell> roomCells = new ArrayList<>();

    private List<Player> players = new ArrayList<>(); /* added to use playersInside() */




    /* Methods */

    public Colors getColor() {
        return color;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public SpawnCell getSpawnCell() {
        return spawnCell;
    }

    public void setSpawnCell(SpawnCell spawnCell) {
        this.spawnCell = spawnCell;
    }

    public void setRoomCells(List<Cell> roomCells) {
        this.roomCells = roomCells;
    }

    public List<Cell> getRoomCells() {
        return roomCells;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> playersInside(){

    List<Player> playersInside = new ArrayList<>();

        for (int i = 0; i < roomCells.size(); i++) {

            playersInside.addAll(roomCells.get(i).playersInCell());

        }

        return playersInside;
    }

    public void addCell(Cell cell) {

        if(cell == null) {
            throw new NullPointerException("addCell parameter can't be null");}

        roomCells.add(cell);

    }
}
