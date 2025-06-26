package main;

import objects.Word;

import java.util.ArrayList;

public class Tokenizer {
    private static final byte[] LOOKUP_TABLE = new byte[256];

    public Tokenizer() { }

    static {
        for (int i = 0; i < 256; i++) {
            if (i >= 'a' && i <= 'z') {
                LOOKUP_TABLE[i] = (byte) i;
            } else if (i >= 'A' && i <= 'Z') {
                LOOKUP_TABLE[i] = (byte) (i + 0x20);
            } else if (i >= '0' && i <= '9') {
                LOOKUP_TABLE[i] = (byte) (i);
            } else if (i == '.' || i == '-' || i == '/') {
                LOOKUP_TABLE[i] = (byte) (i);
            } else {
                LOOKUP_TABLE[i] = ' ';
            }
        }
    }

    private String sanitize(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append((char) LOOKUP_TABLE[c & 0xFF]);
        }
        return sb.toString();
    }

    public ArrayList<Word> tokenize(String input) {
        ArrayList<Word> tokens = new ArrayList<>();
        for (String s : sanitize(input).split("\\s")) {
            if (!s.isBlank() && s.length() > 1) {
                tokens.add(new Word(s));
            }
        }
        return tokens;
    }
}
