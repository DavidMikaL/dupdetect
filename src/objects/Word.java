package objects;

public class Word {
    private String word;

    private int count;

    public int getCount() {return count;}
    public void countUp()
    {
        count++;
    }

    private double inBoth;
    private double inBothHit;
    private double inOne;
    private double inOneHit;

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
}
