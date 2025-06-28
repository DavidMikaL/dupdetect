package main;

import Experimental.WordImpact;
import objects.Duplicate;
import objects.StorageDevice;
import objects.Word;
import util.CSVReader;
import util.CSVWriter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    static final int nTHREADS = 16;
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
    static final String OUT_1 = CURRENT_DIR + "/dataSample/Z2.csv";
    static final String OUT_2 = CURRENT_DIR + "/dataSample/ZY2.csv";

    public static void main(String[] args) {
//        testingMain();
//        exit(1);
        // TODO hehe
//
        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, FILE_1);

        storageDevices.forEach(StorageDevice::tokenize);
        //ArrayList<ArrayList<Word>> wordList = new ArrayList<>(storageDevices.stream().map(StorageDevice::getTokens).toList());



        Partitioner partitioner = new Partitioner();
        ArrayList<StorageDevice>[][][][][] partitions = partitioner.partition(storageDevices);

        // 2. Vergleichsschleife mit schnellerem Score & weniger Objekten
        ExecutorService executor = Executors.newFixedThreadPool(16);

        List<Future<List<Duplicate>>> futures = new ArrayList<>();

        for (int i = 0; i < partitioner.brands.size() + 1; i++) {
            for (int j = 0; j < partitioner.keywords1.size() + 1; j++) {
                for (int k = 0; k < partitioner.keywords2.size() + 1; k++) {
                    for (int l = 0; l < partitioner.storageSizes.size() + 1; l++) {
                        for (int o = 0; o < partitioner.wordCounts.size() + 1; o++) {
                            ArrayList<StorageDevice> partition = partitions[i][j][k][l][o];

//                            if (partition.size() > 5_000) {
//                                final int fi = i, fj = j, fk = k, fl = l, fo = o;
//                                String brand = fi < partitioner.brands.size() ? partitioner.brands.get(fi) : "unknown";
//                                String keyword1 = fj < partitioner.keywords1.size() ? partitioner.keywords1.get(fj) : "??";
//                                String keyword2 = fk < partitioner.keywords2.size() ? partitioner.keywords2.get(fk) : "??";
//                                String storageSize = fl < partitioner.storageSizes.size() ? partitioner.storageSizes.get(fl) : "?? GB";
//                                String wordCount = fo < partitioner.wordCounts.size() ? partitioner.wordCounts.get(fo) : " 99+";
//                                System.out.println(brand + " " + keyword1 + " " + keyword2 + " " + storageSize + " " + wordCount + " (" + partition.size() + ")");
//                            }


                            if (partition.size() < 17_000) {
                                final int fi = i, fj = j, fk = k, fl = l, fo = o;
                                Future<List<Duplicate>> future = executor.submit(() -> {
                                    List<Duplicate> localDuplicates = new ArrayList<>();

                                    // Token-Caching
                                    Map<Integer, Set<Word>> tokenCache = new HashMap<>();
                                    for (StorageDevice sd : partition) {
                                        tokenCache.put(sd.getId(), new HashSet<>(sd.getTokens()));
                                    }

                                    for (int a = 0; a < partition.size(); a++) {
                                        StorageDevice device1 = partition.get(a);
                                        Set<Word> tokens1 = tokenCache.get(device1.getId());

                                        for (int b = a + 1; b < partition.size(); b++) {
                                            StorageDevice device2 = partition.get(b);
                                            Set<Word> tokens2 = tokenCache.get(device2.getId());

                                            int intersection = 0, union = tokens1.size();
                                            for (Word w : tokens2) {
                                                if (tokens1.contains(w)) {
                                                    intersection++;
                                                } else {
                                                    union++;
                                                }
                                            }

                                            int min = Math.min(tokens1.size(), tokens2.size());
                                            double score = (double) intersection / union;

                                            if (score >= 0.75) {
                                                localDuplicates.add(new Duplicate(device1.getId(), device2.getId()));
                                            }
                                        }
                                    }

                                    String brand = fi < partitioner.brands.size() ? partitioner.brands.get(fi) : "unknown";
                                    String keyword1 = fj < partitioner.keywords1.size() ? partitioner.keywords1.get(fj) : "??";
                                    String keyword2 = fk < partitioner.keywords2.size() ? partitioner.keywords2.get(fk) : "??";
                                    String storageSize = fl < partitioner.storageSizes.size() ? partitioner.storageSizes.get(fl) : "?? GB";
                                    String wordCount = fo < partitioner.wordCounts.size() ? partitioner.wordCounts.get(fo) : " 99+";
                                    System.out.println(brand + " " + keyword1 + " " + keyword2 + " " + storageSize + " " + wordCount + " (" + partition.size() + ")");

                                    return localDuplicates;
                                });

                                futures.add(future);
                            }
                        }
                    }
                }
            }
        }

        // Ergebnisse sammeln
        Set<Duplicate> duplicates = ConcurrentHashMap.newKeySet();
        for (Future<List<Duplicate>> future : futures) {
            try {
                duplicates.addAll(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();

        CSVWriter writer = new CSVWriter();
        writer.write(CURRENT_DIR+"/dupes.csv", new ArrayList<>(duplicates));

        Validator validator = new Validator(FILE_1, FILE_2, CURRENT_DIR+"/dupes.csv");
        double f1 = validator.evaluateF1();
        System.out.println("F1 score: " + f1);
        validator.writeTruePositivesFile(CURRENT_DIR+"/truePositives.txt");
        validator.writeFalsePositivesFile(CURRENT_DIR+"/falsePositives.txt");
        validator.writeFalseNegativeFile(CURRENT_DIR+"/falseNegatives.txt");

    }


    public static void testingMain()
    {
        WordImpact wordImpact = new WordImpact();
        HashMap<String,Word> wordMap = wordImpact.calculateWordStats(FILE_2, FILE_1);

        List<HashMap.Entry<String, Word>> sortierteListe = new ArrayList<>(wordMap.entrySet());
        sortierteListe.sort(Comparator.comparing(entry -> entry.getValue().getCount()));

        for (Map.Entry<String, Word> eintrag : sortierteListe) {
            System.out.println(eintrag.getKey() + ": " + eintrag.getValue().getCount());
        }
    }
}