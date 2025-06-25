package main;

import objects.Duplicate;
import objects.StorageDevice;
import util.CSVReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Validator {

    private final List<StorageDevice> storageDevices;
    private final TreeSet<Duplicate> trueDupes;
    private final TreeSet<Duplicate> declaredDupes;

    public Validator(String storageDevicesFilePath, String trueDupesFilePath, String declaredDupesFilePath) {
        CSVReader reader = new CSVReader();
        storageDevices = reader.read(StorageDevice.class, storageDevicesFilePath);
        trueDupes = new TreeSet<>(reader.read(Duplicate.class, trueDupesFilePath));
        declaredDupes = new TreeSet<>(reader.read(Duplicate.class, declaredDupesFilePath));
    }

    public double evaluateF1()
    {
        System.out.println("calculating F1 score...");

        // count[0] := true positives
        // count[1] := false positives
        // count[2] := false negatives
        double[] counts = new double[3];

        ArrayList<Duplicate> allDupesList = new ArrayList<>(trueDupes);
        allDupesList.addAll(declaredDupes);

        HashSet<Duplicate> allDupes = new HashSet<>(allDupesList);

        for (Duplicate d : allDupes) {
            boolean trueDupe = trueDupes.contains(d);
            boolean declaredDupe = declaredDupes.contains(d);

            if (declaredDupe) {
                if (trueDupe) {
                    counts[0]++;
                } else {
                    counts[1]++;
                }
            } else if (trueDupe) {
                counts[2]++;
            }
        }


        double precision =  counts[0] / ( counts[0] +  counts[1]); // true positive / (true positive + false positive)
        double recall =  counts[0] / ( counts[0] +  counts[2]); // true positive / (true positive + false negative)

        System.out.println("true positives: " + counts[0]);
        System.out.println("false positives: " + counts[1]);
        System.out.println("false negatives: " + counts[2]);

        if (counts[0] +  counts[1] == 0)
            precision = 0;

        if (counts[0] +  counts[2] == 0)
            recall = 0;

        double f1 = (2 * precision * recall)/(precision + recall);

        if (precision + recall == 0)
            f1 = 0;

        return f1;
    }

    public void writeTruePositivesFile(String filename) {
        Set<Duplicate> trueSet = new HashSet<>(trueDupes);
        Set<Duplicate> declaredSet = new HashSet<>(declaredDupes);
        trueSet.retainAll(declaredSet);
        writeToFile(filename, trueSet);
    }

    public void writeFalsePositivesFile(String filename) {
        Set<Duplicate> declaredSet = new HashSet<>(declaredDupes);
        declaredSet.removeAll(trueDupes);
        writeToFile(filename, declaredSet);
    }

    public void writeFalseNegativeFile(String filename) {
        Set<Duplicate> trueSet = new HashSet<>(trueDupes);
        trueSet.removeAll(declaredDupes);
        writeToFile(filename, trueSet);
    }

    private void writeToFile(String filename, Set<Duplicate> duplicates) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Duplicate d : duplicates) {
                StorageDevice device1 = storageDevices.get(d.getLid());
                StorageDevice device2 = storageDevices.get(d.getRid());
                writer.write(d.getLid() + "," + d.getRid() + "\n");
                writer.write(device1.getName() + "\n" + device2.getName() + "\n");
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
