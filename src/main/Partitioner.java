package main;

import objects.StorageDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Partitioner {

    public ArrayList<String> brands = new ArrayList<>(List.of(new String[]{"sandisk", "sony", "kingston", "lexar", "intenso", "toshiba", "samsung", "pny", "transcend"}));
    public ArrayList<String> sizes = new ArrayList<>(List.of(new String[]{"4GB", "8GB", "10GB", "16GB", "32GB", "64GB", "128GB", "256GB", "512GB"}));

    public  ArrayList<StorageDevice>[][] partition(List<StorageDevice> originalList)
    {
        @SuppressWarnings("unchecked")
        ArrayList<StorageDevice>[][] partitions = new ArrayList[brands.size()+1][sizes.size()+1];
        for (int i = 0; i < brands.size()+1; i++) {
            for (int j = 0; j < sizes.size()+1; j++) {
                partitions[i][j] = new ArrayList<>();
            }
        }

        for (StorageDevice sd : originalList) {
            String brand = sd.getBrand().toLowerCase();
            String name = sd.getName();

            Matcher matcher = Pattern.compile("\b("+String.join("|", brands)+")", Pattern.CASE_INSENSITIVE).matcher(name);
            if (matcher.find()) {
                brand = matcher.group();
            }

            int i = brands.indexOf(brand.toLowerCase());
            if (i == -1) {
                i = brands.size();
            } else {
                sd.setBrand(brand); // TODO Hier wird das Dataset geändert!
            }

            Matcher m = Pattern.compile("(?i)(\\b\\d+\\s?(GB|TB))").matcher(name);
            int j = sizes.size();

            if (m.find()) {
                // TODO Allow multiple sizes? 32GB/128GB (1214075: SanDisk SD SDHC (32GB) - (KSSD10V2/128GB) GB Ultra 32)
                String size = m.group(1).replaceAll("\\s+", "");
                int foundIndex = sizes.indexOf(size);
                if (foundIndex != -1) j = foundIndex;
            }

            partitions[i][j].add(sd);
        }

        return partitions;
    }
}
