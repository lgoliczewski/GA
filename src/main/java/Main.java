
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

       Tests t = new Tests();
       t.symetricGenerationTest();
       //t.asymetricGenerationTest();

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
