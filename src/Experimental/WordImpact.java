package Experimental;

import objects.Duplicate;
import objects.StorageDevice;
import objects.Word;
import util.CSVReader;

import java.lang.reflect.Array;
import java.util.*;

public class WordImpact {
    public HashMap<String, Word> efficientWordStats(String groundTruthPath, String storageDevicesPath)
    {
        CSVReader csvReader = new CSVReader();
        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, storageDevicesPath);
        List<Duplicate> groundTruth = csvReader.read(Duplicate.class, groundTruthPath);
        storageDevices.forEach(StorageDevice::tokenize);

        //Setup word hashmap:
        HashMap<String,Word> wordMap = new HashMap<>();

        System.out.println("storagedevices.size: " + storageDevices.size());

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
                    //wordMap.get(word.getWord()).stringIndexes.add(storageDevice.getId());
                }
            }
        }

        //count up on duplicates:

        for(Duplicate duplicate : groundTruth)
        {
            Set<Word> iTokens = new HashSet<>(storageDevices.get(duplicate.getLid()).getTokens());
            Set<Word> jTokens = new HashSet<>(storageDevices.get(duplicate.getRid()).getTokens());

            Set<Word> iMinusJ = new HashSet<>(iTokens);
            iMinusJ.removeAll(jTokens);

            Set<Word> jMinusI = new HashSet<>(jTokens);
            jMinusI.removeAll(iTokens);

            Set<Word> intersection = new HashSet<>(iTokens);
            intersection.removeAll(iMinusJ);

            for(Word word : intersection)
            {
                wordMap.get(word.getWord()).setInBothHit(wordMap.get(word.getWord()).getInBothHit() + 1);
            }
            for(Word word : iMinusJ)
            {
                wordMap.get(word.getWord()).setInOneHit(wordMap.get(word.getWord()).getInOneHit() + 1);
            }
            for(Word word : jMinusI)
            {
                wordMap.get(word.getWord()).setInOneHit(wordMap.get(word.getWord()).getInOneHit() + 1);
            }
        }

        //TODO check reader index schenannigans
        //maybe wite everything into new list in accordance to Id

        //count hits:
        for(Word word : wordMap.values())
        {
            word.setInBoth(binomial2(word.getCount()));
            word.setInOne(word.getCount()/100000.0 * (storageDevices.size() - word.getCount()));
        }

        for(Word word : wordMap.values())
        {
            word.setInOnePercentage((word.getInOneHit()) / word.getInOne());
            if(word.getInBoth() > 0 && word.getInBothHit() > 0)
            {
                word.setInBothPercentage((word.getInBothHit() * 1000) / word.getInBoth());
            }
            else
            {
                word.setInBothPercentage(1.5);//median if
            }
        }
        return wordMap;
        //output in lines: Word - Occurrances - positiveImpact - negative impact
    }

    private static double binomial2(double n)
    {
        return (n * (n - 1)) / 2;
    }


    public static void testingMain(String groundTruthPath, String storageDevicesPath)
    {
        WordImpact wordImpact = new WordImpact();
        HashMap<String,Word> wordMap = wordImpact.efficientWordStats(groundTruthPath, storageDevicesPath);

        List<HashMap.Entry<String, Word>> sortierteListe = new ArrayList<>(wordMap.entrySet());

        sortierteListe.sort(Comparator.comparing(entry -> entry.getValue().getInOnePercentage()));

        for(int i = 0; i < sortierteListe.size(); i++)
        {
            sortierteListe.get(i).getValue().positionValue += i;
        }

        sortierteListe.sort(Comparator.comparing(entry -> entry.getValue().getInBothPercentage()));


        for(int i = 0; i < sortierteListe.size(); i++)
        {
            sortierteListe.get(i).getValue().positionValue += sortierteListe.size() - i;
        }

        sortierteListe.sort(Comparator.comparing(entry -> -entry.getValue().positionValue));

        int i = 0;
        for (Map.Entry<String, Word> eintrag : sortierteListe) {
            //System.out.print("\"" + eintrag.getKey() + "\", ");

            System.out.println(eintrag.getKey() + ": Positionvalue: " + eintrag.getValue().positionValue);
            System.out.println(eintrag.getKey() + ": count: " + eintrag.getValue().getCount());
            System.out.println(eintrag.getKey() + ": in Both: " + eintrag.getValue().getInBoth() + " , in both hit: " + eintrag.getValue().getInBothHit() + " , (adjusted)in one: " + eintrag.getValue().getInOne() + " , in one hit: " + eintrag.getValue().getInOneHit());
            System.out.println(eintrag.getKey() + ": percentage if in Both: " + eintrag.getValue().getInBothPercentage() + " , (percentage) if in one: " + eintrag.getValue().getInOnePercentage());
            System.out.println();
        }
    }


    String[] trashArray = {"a1", "android", "ffp", "y", "version", "nouvelle", "98mb/s", "a1/class", "inform", "tica", "100", "80mb/s", "98", "imaging", "amazon.es", "app", "u1", "e", "lectura", "action", "mobile", "48mb/s", "kit", "per", "micro-sdxc", "adattatore", "100mb/s", "d", "allant", "extrem", "deluxe", "sd-adapter", "jusqu", "pour", "bestellen", "micro-sd", "a2", "48", "adaptador", "mb/sec", "zone", "hasta", "v30", "speicherkarte", "cam", "lecture", "geheugenkaart", "karta", "da", "cl10", "adapter", "fino", "gris", "tarjeta", "80", "cartes", "vitesse", "msd", "classe", "ov", "sports", "une", "bis", "moire", "velocidad", "pam", "micro-sdhc", "reg", "up", "sd.", "80mb", "adap", "r", "microsdxc-card", "wex", "amazon.de", "mb/s", "uhs", "scheda", "den", "rtya", "100mb/sec", "mb/sek.", "amazon", "adaptateur", "zu", "to", "90mb/s", "met", "con", "micro", "for", "en", "technische", "/s", "m", "adapt.", "f", "100mb", "microsd", "80mb/sec", "les", "camera", "di", "cameras", "u3/class", "aacute", "flashgeheugenkaart", "extr", "daten", "smartphone", "video", "adapt", "30", "k", "adaptor", "microsdhc-card", "-i", "with", "mb/sek", "mo/s", "xc", "ras", "class10", "sd-hc", "533x", "c10", "avec", "gb", "memoria", "actioncam", "ntarjeta", "msdhc", "class", "kaart", "super", "frustfreie", "275mb/s", "10", "performances", "hc", "flash-speicherkarte", "original", "60", "clase", "90", "o", "80r/10w", "product", "achat", "sd-xc", "fnac.es", "verpackung", "sd", "ern", "microsdhc/microsdxc", "flashgeheugen", "tablet", "duo", "connect", "160", "/common2/js/libs/jquery", "nhead.js", "ultraandroid", "stick", "90mb", "memory", "applicatives", "bei", "warenkorb", ".", "275", "microsdxc", "abfoto", "microsdxc-an-sd-adapter", "90mb/sec", "gelegt", "uhs-i", "sds", "sin", "rescue", "actionsc", "doodigital", "ci", "klasse", "photographic", "mem", "na", "microsdhc/sd-adapter", "sdsquar-032g-gn6ia", "carte", "card", "microsdhc", "40z", "tesco", "u3", "u3/uhs-i", "photo", "direct", "riak"};











//    public HashMap<String, Word> calculateWordStats(String groundTruthPath, String storageDevicesPath)
//    {
//        CSVReader csvReader = new CSVReader();
//        List<StorageDevice> storageDevices = csvReader.read(StorageDevice.class, storageDevicesPath);
//        List<Duplicate> groundTruth = csvReader.read(Duplicate.class, groundTruthPath);
//        storageDevices.forEach(StorageDevice::tokenize);
//
//        TreeSet<Duplicate> duplicateTree = new TreeSet<>(groundTruth);
//
//        //Setup word hashmap:
//        HashMap<String,Word> wordMap = new HashMap<>();
//
//        //count words
//        for(StorageDevice storageDevice : storageDevices)
//        {
//            List<Word> words = storageDevice.getTokens();
//            for(Word word : words)
//            {
//                if(wordMap.containsKey(word.getWord()))
//                {
//                    wordMap.get(word.getWord()).countUp();
//                }
//                else {
//                    wordMap.put(word.getWord(), word);
//                    wordMap.get(word.getWord()).countUp();
//                }
//            }
//        }
//
//        //TODO check reader index schenannigans
//        //maybe wite everything into new list in accordance to Id
//
//        //count hits:
//        int n = storageDevices.size();
//        for(int i = 0; i < n; i++) {
//            if (i % 10 == 0) {
//                System.out.println(i);
//            }
//            for (int j = i + 1; j < n; j++) {
//                {
//                    Duplicate possibleDuplicate = new Duplicate(i, j);
//                    boolean isDuplicate = duplicateTree.contains(possibleDuplicate);
//                    //TODO groundtruth has to be hashTree
//
//                    List<Word> iTokens = storageDevices.get(i).getTokens();
//                    List<Word> jTokens = storageDevices.get(j).getTokens();
//                    List<Word> allTokens = new ArrayList<>();
//                    allTokens.addAll(iTokens);
//                    allTokens.addAll(jTokens);
//                    Set<Word> allWords = new HashSet<>(allTokens);
//
//                    //TODO check if words are still references to actual word objects
//                    for (Word word : allWords) {
//                        boolean inItokens = iTokens.contains(word);
//                        boolean inJtokens = jTokens.contains(word);
//                        if (isDuplicate) {
//                            //System.out.println("happens 0");
//                            if (inItokens && inJtokens) {
//                                //System.out.println("happens 1");
//                                //word.inBothHitUp();
//                                //word.inBothUp();
//                            } else {
//                                //System.out.println("happens 2");
//                                //word.inOneHitUp();
//                                //word.inOneUp();
//                            }
//                        } else {
//                            if (inItokens && inJtokens) {
//                                //System.out.println("happens 3");
//                                //word.inBothUp();
//                            } else {
//                                //System.out.println("happens 4");
//                                //word.inOneUp();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        for(Word word : wordMap.values())
//        {
//            word.setInBothPercentage(word.getInBothHit() / word.getInBoth());
//            word.setInOnePercentage(word.getInOneHit() / word.getInOne());
//        }
//        return wordMap;
//        //output in lines: Word - Occurrances - positiveImpact - negative impact
//    }
}
