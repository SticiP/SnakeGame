package game.snakegame;

import java.util.List;

public class LevelData {
    private String name;
    private List<List<String>> grid;

    // Getters și setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getGrid() {
        return grid;
    }

    public void setGrid(List<List<String>> grid) {
        this.grid = grid;
    }

    public int getWidth() {
        return grid.get(0).size();
    }

    public int getHeight() {
        return grid.size();
    }
}
