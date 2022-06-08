import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GPTest {

    GPTest(){

    }

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
        int k = 0;
        while(k<=20) {
            File outFile = new File("GPTest" + pom + k + ".csv");
            GA ga = new GA(instance, 80);
            ga.geneticAlgorithm(outFile, 60000, "PMX", "roulette", "invert", 0.1,(double)k/100,0.002,0);
            k = k + 5;
        }
    }

}
