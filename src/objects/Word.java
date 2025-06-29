package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Word {
    private String word;

    public int positionValue;

    private int count;
    public int getCount() {return count;}
    public void countUp() {count++;}

    private double inBoth;
    public double getInBoth() {return inBoth;}
    public void setInBoth(double inBoth) {this.inBoth = inBoth;}

    private double inBothHit;
    public double getInBothHit() {return inBothHit;}
    public void setInBothHit(double inBothHit) {this.inBothHit = inBothHit;}

    private double inOne;
    public double getInOne() {return inOne;}
    public void setInOne(double inOne) {this.inOne = inOne;}

    private double inOneHit;
    public double getInOneHit() {return inOneHit;}
    public void setInOneHit(double inOneHit) {this.inOneHit = inOneHit;}


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
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }
}
