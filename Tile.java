import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Tile implements Comparable<Tile> {

    private int value, row, col;
    private boolean[] possibilities;
    private boolean assigned, guessed;

    public Tile(int value, int row, int col) {
        this.value = value;
        this.row = row;
        this.col = col;
        this.guessed = false;
        this.assigned = false;

        possibilities = new boolean[10];

        // no tile can have a value of 0
        possibilities[0] = false;

        // if the tile has an assigned value then only one possibility
        if (value != 0) {
            for (int i = 1; i <= 9; i++) {
                if (i == value) possibilities[i] = true;
                else possibilities[i] = false;
            }
        } else {
            // initially any number from 1 - 9 is possible
            for (int i = 1; i <= 9; i++) possibilities[i] = true;
        }
    }


    public int getValue() { return value; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean assigned() {return assigned;}

    public void eliminatePossibility(int number) {
        possibilities[number] = false; 
    }

    /* Returns the number of possible assignments for the tile. */
    public int getNumberPossibilities() {
        int count = 0;

        for (int i = 0; i < possibilities.length; i++) 
            if (possibilities[i]) count++;

        return count;
    }

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public int[] getPossibilities() {

        ArrayList<Integer> values = new ArrayList<Integer>();

        for (int i = 0; i < possibilities.length; i++) {
            if (possibilities[i]) values.add(i);
        }

        return convertIntegers(values);
    }

    public boolean isPossible(int number) {
        return possibilities[number]; 
    }

    public void assignValue(int number) {
        // no number is possible for this tile except for assigned value
        for (int i = 1; i <= 9; i++) possibilities[i] = false;
        possibilities[number] = true;
        this.value = number;
        this.assigned = true;
    }

    public void guessValue(int number) {
        assignValue(number);
        this.guessed = true;
    }

    public String assignmentStatement() {
        String row = String.valueOf(getRow());
        String col = String.valueOf(getCol());
        String value = String.valueOf(getValue());
        return "Assigned " + value + " to Tile " + "(" + row + ", " + col + ")";
    }

    public String toString() {
        String row = String.valueOf(getRow());
        String col = String.valueOf(getCol());
        String value = String.valueOf(getValue());
        String num = String.valueOf(getNumberPossibilities());

        String things = "[ ";
        for (int i = 1; i <= 9; i++) {
            if (possibilities[i]) {
                things = things + String.valueOf(i) + " ";
            }
        }
        things = things + "]";

        String str;

        if (getValue() != 0) {
            str = "Tile (" + row + ", " + col + ") has value " + value;
        } else {
            str = "Tile (" + row + ", " + col + ") has possibilities: " + things;
        }

        return str;
    }

    @Override
    public int compareTo(Tile t) {
        if (this.getNumberPossibilities() > t.getNumberPossibilities()) return 1;
        else if (this.getNumberPossibilities() < t.getNumberPossibilities()) return -1;
        else return 0;
    }

}
