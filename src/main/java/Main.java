
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        File file = new File("data/eil76.tsp");
        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file, instance);
        GA ga = new GA(instance,30);
        ga.geneticAlgorithm(100000);


    }



}
