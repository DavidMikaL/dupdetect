package objects;

import main.Tokenizer;

import java.util.ArrayList;

public class StorageDevice {
    private int id;
    private String name;

    private String price;
    private String brand;
    private String description;
    private String category;

    private final ArrayList<Word> tokens = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public ArrayList<Word> getTokens() {
        return tokens;
    }

    public void tokenize() {
        Tokenizer tokenizer = new Tokenizer();
        tokens.addAll(tokenizer.tokenize(name));
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }
}
