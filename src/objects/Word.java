package objects;

import java.util.Objects;

public class Word {
    private String word;

    private int count;
    public int getCount() {return count;}
    public void countUp() {count++;}

    private double inBoth;
    public double getInBoth() {return inBoth;}
    public void inBothUp() {inBoth++;}

    private double inBothHit;
    public double getInBothHit() {return inBothHit;}
    public void inBothHitUp() {inBothHit++;}

    private double inOne;
    public double getInOne() {return inOne;}
    public void inOneUp() {inOne++;}

    private double inOneHit;
    public double getInOneHit() {return inOneHit;}
    public void inOneHitUp() {inOneHit++;}

    double inOnePercentage;
    public double getInOnePercentage() {return inOnePercentage;}
    public void setInOnePercentage(double inOnePercentage) {this.inOnePercentage = inOnePercentage;}

    double inBothPercentage;
    public double getInBothPercentage() {return inBothPercentage;}
    public void setInBothPercentage(double inBothPercentage) {this.inBothPercentage = inBothPercentage;}

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word word1)) return false;
        return Objects.equals(word, word1.word);
    }
}
