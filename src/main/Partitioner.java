package main;

import objects.StorageDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Partitioner {

    public ArrayList<String> brands = new ArrayList<>(List.of(new String[]{"sandisk", "sony", "kingston", "lexar", "intenso", "toshiba", "samsung", "pny", "transcend"}));
    public ArrayList<String> keywords1 = new ArrayList<>(List.of(new String[]{"extreme", "ultra", "ultimate"}));
    public ArrayList<String> keywords2 = new ArrayList<>(List.of(new String[]{"sdhc", "sdxc",  "usb"})); // TODO Micro Löschen
    public ArrayList<String> storageSizes = new ArrayList<>(List.of(new String[]{"4gb", "8gb", "10gb", "16gb", "32gb", "64gb", "128gb", "256gb", "512gb", // TODO 2-er Potenz am nähesten an GB/GO
                                                                                "4go", "8go", "10go", "16go", "32go", "64go", "128go", "256go", "512go"}));
    public ArrayList<String> wordCounts = new ArrayList<>(List.of(new String[]{"1-8", "7-11", "10-14", "12-16", "15-99"}));
    private final int[][] wordCountRanges;

    public Partitioner() {
        wordCountRanges = new int[wordCounts.size()][2];
        for (int i = 0; i < wordCounts.size(); i++) {
            String[] parts = wordCounts.get(i).split("-");
            wordCountRanges[i][0] = Integer.parseInt(parts[0]);
            wordCountRanges[i][1] = Integer.parseInt(parts[1]);
        }
    }

    public  ArrayList<StorageDevice>[][][][][] partition(List<StorageDevice> originalList)
    {
        @SuppressWarnings("unchecked")
        ArrayList<StorageDevice>[][][][][] partitions = new ArrayList[brands.size()+1][keywords1.size()+1][keywords2.size()+1][storageSizes.size()+1][wordCounts.size()+1];
        for (int i = 0; i < brands.size()+1; i++) {
            for (int j = 0; j < keywords1.size()+1; j++) {
                for (int k = 0; k < keywords2.size()+1; k++) {
                    for (int l = 0; l < storageSizes.size()+1; l++) {
                        for (int o = 0; o < wordCounts.size()+1; o++) {
                            partitions[i][j][k][l][o] = new ArrayList<>();
                        }
                    }
                }
            }
        }

        for (StorageDevice sd : originalList) {
            String brand = sd.getBrand().toLowerCase();
            String name = sd.getName();
            Matcher matcher;

            // Brands
            matcher = Pattern.compile("\b("+String.join("|", brands)+")", Pattern.CASE_INSENSITIVE).matcher(name);
            if (matcher.find()) {
                brand = matcher.group();
            }

            int i = brands.indexOf(brand.toLowerCase());
            if (i == -1) {
                i = brands.size();
            } else {
                sd.setBrand(brand); // TODO Hier wird das Dataset geändert!
            }

            String sortedName = String.join(" ", Arrays.stream(sd.getName().split("\\s+")).sorted(String.CASE_INSENSITIVE_ORDER).toArray(String[]::new));

            // Keywords 1
            String keyword1 = "";
            matcher = Pattern.compile("("+String.join("|", keywords1)+")", Pattern.CASE_INSENSITIVE).matcher(sortedName);
            if (matcher.find()) {
                keyword1 = matcher.group();
            }

            int j = keywords1.indexOf(keyword1.toLowerCase());
            if (j == -1) {
                j = keywords1.size();
            }

            // Keywords 2
            String keyword2 = "";
            matcher = Pattern.compile("("+String.join("|", keywords2)+")", Pattern.CASE_INSENSITIVE).matcher(sortedName);
            if (matcher.find()) {
                keyword2 = matcher.group();
            }

            int k = keywords2.indexOf(keyword2.toLowerCase());
            if (k == -1) {
                k = keywords2.size();
            }

            // Gigabytes
            matcher = Pattern.compile("(?i)(\\b\\d+\\s?(GB|GO))").matcher(name);
            int l = storageSizes.size();

            if (matcher.find()) {
                // TODO Allow multiple sizes? 32GB/128GB (1214075: SanDisk SD SDHC (32GB) - (KSSD10V2/128GB) GB Ultra 32)
                String size = matcher.group(1).replaceAll("\\s+", "");
                int foundIndex = storageSizes.indexOf(size.toLowerCase());
                if (foundIndex != -1) l = foundIndex;
            }

            int wordCountInName = name.trim().split("\\s+").length;

            // Alle passenden WordCount-Partitionen finden (wegen Überschneidung)
            for (int o = 0; o < wordCountRanges.length; o++) {
                int min = wordCountRanges[o][0];
                int max = wordCountRanges[o][1];
                if (wordCountInName >= min && wordCountInName <= max) {
                    partitions[i][j][k][l][o].add(sd);
                }
            }
        }

        return partitions;
    }
}
