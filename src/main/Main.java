package main;

import objects.StorageDevice;
import objects.Word;
import util.CSVReader;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static final String CURRENT_DIR = System.getProperty("user.dir");
    static final String FILE_1 = CURRENT_DIR + "/data/Z2.csv";
    static final String FILE_2 = CURRENT_DIR + "/data/ZY2.csv";
    static final String OUT_1 = CURRENT_DIR + "/dataSample/Z2.csv";
    static final String OUT_2 = CURRENT_DIR + "/dataSample/ZY2.csv";

    public static void main(String[] args) {
        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, FILE_1);

        System.out.println(storageDevices.get(45));

//        Tokenizer tokenizer = new Tokenizer();
//        List<List<Word>> wordLists = new ArrayList<>();
//
//        Partitioner partitioner = new Partitioner();
//        List<List<Word>> partitions = partitioner.partition();
//
//        DupFinder dupFinder = new DupFinder<>();
//        dupFinder.findDuplicates();//write duplicates into csv
//
//        F1scorer f1scorer = new F1scorer();
//        print(f1scorer.score());
    }
}