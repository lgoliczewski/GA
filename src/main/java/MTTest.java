import java.io.File;

public class MTTest extends Thread{

    MTTest(int populationSize, long time, double generation2OPT, String crossType, String selectionType, String mutationType){

    }

    MTTest(){

    }

    public void run()
    {
        try {
            long start = System.currentTimeMillis();
            File file = new File("data/ch130.tsp");
            Parser parser = new Parser();
            Instance instance = new Instance();
            parser.setParameters(file, instance);
            GA ga = new GA(instance,100);
            ga.geneticAlgorithm(30000);
            long finish = System.currentTimeMillis();
            System.out.println("time = " + (finish - start));
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}
