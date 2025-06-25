package Experimental;

import objects.Duplicate;
import objects.StorageDevice;
import objects.Word;
import util.CSVReader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WordImpact {
    public HashMap<String, Word> calculateWordStats(String groundTruthPath, String storageDevicesPath)
    {
        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, storageDevicesPath);
        //List<Duplicate> groundTruth = csvReader.read(Duplicate.class, groundTruthPath);
        storageDevices.forEach(StorageDevice::tokenize);

        //Setup word hashmap:
        HashMap<String,Word> wordMap = new HashMap<>();

        //count words
        for(StorageDevice storageDevice : storageDevices)
        {
            List<Word> words = storageDevice.getTokens();
            for(Word word : words)
            {
                if(wordMap.containsKey(word.getWord()))
                {
                    wordMap.get(word.getWord()).countUp();
                }
                else {
                    wordMap.put(word.getWord(), word);
                    wordMap.get(word.getWord()).countUp();
                }
            }
        }

        return wordMap;

        //output in lines: Word - Occurrances - positiveImpact - negative impact
    }

}
