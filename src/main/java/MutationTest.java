import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MutationTest {

    public void test(String sFile, String type) throws IOException, InterruptedException {
        File file = new File("data/" + sFile);
        String pom;
        if(Objects.equals(type, "TSP")) {
            pom = sFile.substring(0, sFile.length() - 4);
        }
        else{
            pom = sFile.substring(0, sFile.length() - 5);
        }

        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file, instance);

        //File outFile = new File("MutationTest" + pom + "invert.csv");
        //GA ga = new GA(instance, 40);
        //ga.geneticAlgorithm(outFile, 60000, "OX", "roulette", "invert", 0.1,0,0.002,0);

        File outFile = new File("MutationTest" + pom + "insert.csv");
        GA ga = new GA(instance, 40);
        ga.geneticAlgorithm(outFile, 60000, "OX", "roulette", "insert", 0.1,0,0.002,0);

        //outFile = new File("MutationTest" + pom + "RSM.csv");
        //ga = new GA(instance, 40);
        //ga.geneticAlgorithm(outFile, 60000, "OX", "roulette", "RSM", 0.1,0,0.002,0);


    }
}
