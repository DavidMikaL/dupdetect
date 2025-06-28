package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVReader {
    /**
     * Provides functionality to read a .csv file
     */
    public CSVReader() { }

    /**
     * Reads a .csv file and converts each row into an instance of the specified entity type.
     * <p>
     * The entity class must have fields that match the column names of the CSV file.
     *
     * @param <T>        The type of the entity.
     * @param entityType The class object of the entity type. Its attributes must match the CSV columns.
     * @return           A {@code ArrayList} containing objects of the specified entity type.
     */
    public <T> ArrayList<T> read(Class<T> entityType, String fileName) {
        ArrayList<T> items = new ArrayList<>();

        if (fileName == null) {
            System.err.println("No file specified");
            System.exit(1);
        }

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String[] headers = null;

            if ((line = br.readLine()) != null) {
                headers = line.split(",", -1);
            }

            if (headers == null) {
                System.err.println("Error reading .csv headers. Is the file empty?");
                System.exit(1);
            }

            StringBuilder logicalLine = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (!logicalLine.isEmpty()) {
                    logicalLine.append("\n");
                }
                logicalLine.append(line);

                long quoteCount = logicalLine.chars().filter(ch -> ch == '"').count();

                if (quoteCount % 2 == 0) {
                    String fullLine = logicalLine.toString();
                    logicalLine.setLength(0);

                    String[] values = new String[headers.length];
                    Arrays.fill(values, "");
                    int i = 0;
                    boolean inQuotes = false;
                    StringBuilder field = new StringBuilder();

                    for (int idx = 0; idx < fullLine.length(); idx++) {
                        char c = fullLine.charAt(idx);
                        if (c == '"') {
                            if (inQuotes && idx + 1 < fullLine.length() && fullLine.charAt(idx + 1) == '"') {
                                field.append('"');
                                idx++;
                            } else {
                                inQuotes = !inQuotes;
                            }
                        } else if (c == ',' && !inQuotes) {
                            values[i++] = field.toString();
                            field.setLength(0);
                        } else {
                            field.append(c);
                        }
                    }
                    if (i < headers.length) {
                        values[i] = field.toString();
                    }

                    try {
                        T entity = createEntity(entityType, headers, values);
                        items.add(entity);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // TODO
                        System.exit(1);
                    } catch (NumberFormatException e) {
                        // TODO
                        System.exit(1);
                    }
                }
            }
        }

        catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        catch (Exception e) {
            System.err.println("Error parsing entity: " + e);
            System.err.println("Do the fields match the .csv header?");
            e.printStackTrace();
            System.exit(1);
        }

        return items;
    }

    private <T> T createEntity(Class<T> entityType, String[] headers, String[] values) throws Exception {
        T instance = entityType.getDeclaredConstructor().newInstance();

        for (int i = 0; i < headers.length; i++) {
            String fieldName = headers[i].trim();
            String value = values[i].trim();

            Field field = entityType.getDeclaredField(fieldName);
            field.setAccessible(true);

            if (field.getType() == int.class) {
                try {
                    field.set(instance, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException();
                }
            } else {
                field.set(instance, value);
            }
        }

        return instance;
    }

}
