import java.io.File;

public class MTTest extends Thread{

    String fileName;

    MTTest(String fileName){
        this.fileName = fileName;
    }

    public void run()
    {
        try {
            GPTest test = new GPTest();
            test.test(fileName,"TSP");
        }
        catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}
