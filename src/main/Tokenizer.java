package main;

import objects.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            } else if (i == '.' || i == '-' || i == '/' || i == '?') {
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

        Pattern pattern = Pattern.compile("(?i)\\b(\\d+)\\s?(GB|GO)\\b");
        Matcher matcher = pattern.matcher(input);

        // Speichere Positionen der GB-Tokens
        Set<Integer> matchedIndices = new HashSet<>();
        while (matcher.find()) {
            String token = matcher.group(1) + " gb";  // normalisiere zu "xx gb"
            tokens.add(new Word(token));

            // Merke dir die Match-Position, damit wir sie später überspringen
            for (int i = matcher.start(); i < matcher.end(); i++) {
                matchedIndices.add(i);
            }
        }

        // Standard-Tokenisierung, aber überspringe bereits gematchte GB-Tokens
        StringBuilder remaining = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (!matchedIndices.contains(i)) {
                remaining.append(input.charAt(i));
            } else {
                remaining.append(' '); // ersetze matched Bereich durch Leerzeichen
            }
        }

        for (String s : sanitize(remaining.toString()).split("\\s+")) {
            if (!s.isBlank() && s.length() > 1) {
                String domainRegex = "\\b(?:https?://)?(?:www\\.)?[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}\\b";
                if (!s.contains("?") && !s.matches(domainRegex)) {
                    tokens.add(new Word(s));
                }
            }
        }

        return tokens;
    }
}
