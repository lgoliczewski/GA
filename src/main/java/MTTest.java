import java.io.File;

public class MTTest extends Thread{

    String fileName;

    MTTest(String fileName){
        this.fileName = fileName;
    }

    public void run()
    {
        try {
            MainTest mainTest = new MainTest();
            mainTest.test(fileName,"ATSP");
        }
        catch (Exception e) {
            // Throwing an exception
            e.printStackTrace();
        }
    }
}
