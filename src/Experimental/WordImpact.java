package Experimental;

import objects.Duplicate;
import objects.StorageDevice;
import objects.Word;
import util.CSVReader;

import java.lang.reflect.Array;
import java.util.*;

public class WordImpact {
    public HashMap<String, Word> calculateWordStats(String groundTruthPath, String storageDevicesPath)
    {
        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, storageDevicesPath);
        List<Duplicate> groundTruth = csvReader.read(Duplicate.class, groundTruthPath);
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

        //TODO check reader index schenannigans
        //maybe wite everything into new list in accordance to Id

        //count hits:
        int n = storageDevices.size();
        for(int i = 0; i < n; i++) {
            if (i % 10 == 0) {
                System.out.println(i);
            }
            for (int j = 0; j < n; j++) {
                {
                    Duplicate possibleDuplicate = new Duplicate(i, j);
                    boolean isDuplicate = groundTruth.contains(possibleDuplicate);
                    //TODO groundtruth has to be hashTree

                    List<Word> iTokens = storageDevices.get(i).getTokens();
                    List<Word> jTokens = storageDevices.get(i).getTokens();
                    List<Word> allTokens = new ArrayList<>();
                    allTokens.addAll(iTokens);
                    allTokens.addAll(jTokens);
                    Set<Word> allWords = new HashSet<>(allTokens);

                    //TODO check if words are still references to actual word objects
                    for (Word word : allWords) {
                        boolean inItokens = iTokens.contains(word);
                        boolean inJtokens = jTokens.contains(word);
                        if (isDuplicate) {
                            if (inItokens && inJtokens) {
                                word.inBothHitUp();
                                word.inBothUp();
                            } else {
                                word.inOneHitUp();
                                word.inOneUp();
                            }
                        } else {
                            if (inItokens && inJtokens) {
                                word.inBothUp();
                            } else {
                                word.inOneUp();
                            }
                        }

                    }
                }
            }
        }

        for(Word word : wordMap.values())
        {
            word.setInBothPercentage(word.getInBothHit() / word.getInBoth());
            word.setInOnePercentage(word.getInOneHit() / word.getInOne());
        }
        return wordMap;
        //output in lines: Word - Occurrances - positiveImpact - negative impact
    }

}
