
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Instance instance = new Instance();
        //instance.generateRandomInstanceEUC_2D(100,1000); //generowanie 10 losowych punktow o wspolrzednych z przedzialu [0,40]

        Parser parser = new Parser();
        File file = new File("data/ch150.tsp");
        parser.setParameters(file,instance);

        Solution solution1, solution2, solution3, solution4;
        solution1 = instance.getSolution();
        //parser.parseSolution(file, solution);

        AlgorithmHolder alg = new AlgorithmHolder();

        long start, end;
        solution1 = alg.ExNearestNeighbor(instance);
        solution2 = solution1.copy();
        solution3 = solution1.copy();
        solution4 = solution1.copy();

        solution1.printOrder();
        start = System.currentTimeMillis();
        solution1 = alg.AccelTwoOptAlgorithm(instance, solution1);
        end = System.currentTimeMillis();
        System.out.println("total distance: " + solution1.totalDistance());
        System.out.println("time : " + (end - start) + "ms");

        solution2.printOrder();
        start = System.currentTimeMillis();
        solution2 = alg.TabuSearchAlgorithm(instance, solution2);
        end = System.currentTimeMillis();
        System.out.println("total distance: " + solution2.totalDistance());
        System.out.println("time : " + (end - start) + "ms");

        solution3.printOrder();
        start = System.currentTimeMillis();
        solution3 = alg.AccelTwoOptAlgorithm(instance, solution3);
        end = System.currentTimeMillis();
        System.out.println("total distance: " + solution3.totalDistance());
        System.out.println("time : " + (end - start) + "ms");

        solution4.printOrder();
        start = System.currentTimeMillis();
        solution4 = alg.TabuSearchAlgorithm(instance, solution4);
        end = System.currentTimeMillis();
        System.out.println("total distance: " + solution4.totalDistance());
        System.out.println("time : " + (end - start) + "ms");

        //solution.visualize();

        //instance.setName("instance1");
        //ToFileWriter writer = new ToFileWriter();
        //writer.saveToFile(solution);

        //Tests t = new Tests();
        //t.runTests();

    }
}
