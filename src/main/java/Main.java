
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.lang.Object;
import java.lang.Runtime;
import static java.lang.Runtime.getRuntime;


public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        long start = System.currentTimeMillis();
        File file = new File("data/br17.atsp");
        Parser parser = new Parser();
        Instance instance = new Instance();
        parser.setParameters(file, instance);
        GA ga = new GA(instance,100);
        ga.geneticAlgorithm(30000);
        long finish = System.currentTimeMillis();
        System.out.println("time = " + (finish - start));

        /*int n = 8;
        for(int i = 0; i<n; i++){
            System.out.println(":)");
            MTTest mtTest = new MTTest();
            mtTest.start();
        }*/

       Tests t = new Tests();
       t.cycleTest();


        /*Solution s1 = instance.getSolution();
        Solution s2 = instance.getSolution();
        s1.randomOrder();
        s2.randomOrder();
        Solution child = ga.OBX(s1, s2, 0.2);
        s1.printOrder();
        s2.printOrder();
        child.printOrder();*/
    }



}
