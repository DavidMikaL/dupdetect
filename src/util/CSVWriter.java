package util;

import objects.Duplicate;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CSVWriter {
    public void write(String filename, List<Duplicate> duplicates) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.append("lid,rid").append("\n");

            for (Duplicate d : duplicates) {
                writer.append(d.getLid() + "," + d.getRid()).append("\n");
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
