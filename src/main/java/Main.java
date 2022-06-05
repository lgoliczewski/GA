
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        File file = new File("data/gr17.tsp");
        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file, instance);

        GA ga = new GA(instance,200);
        Solution solution1 = instance.getSolution();
        solution1.randomOrder();
        solution1.printOrder();

        Solution solution2 = instance.getSolution();
        solution2.randomOrder();
        solution2.printOrder();

        Solution solution3 = ga.OBX(solution1, solution2, 0.5);
        solution3.printOrder();


    }



}
