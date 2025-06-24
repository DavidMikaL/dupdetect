package Main;

public class Main {
    public static void main(String[] args) {
        CSVreader csvReader = new CSVreader();
        List<String> strings = csvReader.read(path);

        Tokenizer tokenizer = new Tokenizer();
        List<List<Words>> wordLists = new ArrayList<>();

        Partitioner partitioner = new Partitioner();
        List<List<Words>> partitions = partitioner.partition();

        DupFinder dupFinder = new DupFinder<>();
        dupFinder.findDuplicates();//write duplicates into csv

        F1scorer f1scorer = new F1scorer();
        print(f1scorer.score());
    }
}