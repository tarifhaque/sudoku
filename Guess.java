public class Guess implements Comparable<Guess> {
    int row;
    int col;
    int guess;
    double likelihood;

    public Guess(int row, int col, int guess, double likelihood) {
        this.row = row;
        this.col = col;
        this.guess = guess;
        this.likelihood = likelihood;
    }

    public String toString() {
        String r = String.valueOf(row);
        String c = String.valueOf(col);
        return "GUESS " + String.valueOf(guess) + " for Tile " + "(" + r + ", " + c + ")";
    }

    @Override
    public int compareTo(Guess g) {
        if (this.likelihood > g.likelihood) return -1;
        else if (this.likelihood < g.likelihood) return 1;
        else return 0;
    }
}
