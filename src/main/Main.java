package main;

import Experimental.WordImpact;
import objects.StorageDevice;
import objects.Word;
import util.CSVReader;

import java.util.*;

import static java.lang.System.exit;

public class Main {
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
    static final String OUT_1 = CURRENT_DIR + "/dataSample/Z2.csv";
    static final String OUT_2 = CURRENT_DIR + "/dataSample/ZY2.csv";

    public static void main(String[] args) {
        testingMain();
        exit(1);
        // TODO hehe

        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, FILE_1);

//        System.out.println(storageDevices.get(45));

        storageDevices.forEach(StorageDevice::tokenize);
        //ArrayList<ArrayList<Word>> wordList = new ArrayList<>(storageDevices.stream().map(StorageDevice::getTokens).toList());



        Partitioner partitioner = new Partitioner();
        List<List<StorageDevice>> partitions = partitioner.partition(storageDevices);
//
//        DupFinder dupFinder = new DupFinder<>();
//        dupFinder.findDuplicates();//write duplicates into csv
//
//        F1scorer f1scorer = new F1scorer(resultPath);
//        print(f1scorer.score());
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