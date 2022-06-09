import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class MainTest {

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
        Random random = new Random();
        int count = 0;

        for(int i = 1; i<=3; i++){
            for(int j = 2; j<=2; j++){
                for(int k = 1; k<=3; k++){
                    for(int l = 1; l<=3; l++){

                        String crossType = ":)";
                        String selectionType = ":)";
                        String mutationType = ":)";
                        double generation2OPT = -1;

                        if(i==1){
                            crossType = "OX";
                        }

                        if(i == 2){
                            crossType = "OBX";
                        }
                        else if(i == 3){
                            crossType = "PMX";
                        }

                        if(j==1){
                            selectionType = "roulette";
                        }
                        else if(j==2){
                            selectionType = "rouletteRank";
                        }

                        if(k==1){
                            mutationType = "invert";
                        }
                        else if(k==2){
                            mutationType = "insert";
                        }
                        else if(k==3){
                            mutationType = "RSM";
                        }

                        if(l==1){
                            generation2OPT = 0;
                        }
                        else if(l==2){
                            generation2OPT = 0.1;
                        }
                        else if(l==3){
                            generation2OPT = 0.2;
                        }

                        double ppb1 = 0.001 + 0.019*random.nextDouble();
                        int size1 = 50 + random.nextInt(50);

                        double ppb2 = 0.001 + 0.019*random.nextDouble();
                        int size2 = 50 + random.nextInt(50);

                        File outFile = new File(sFile + "/" + crossType + " " +  selectionType  + " " + mutationType + " g2opt = " + generation2OPT + " ppb = " + ((int)(ppb1*1000)) + " size" + size1 + ".csv");
                        GA ga = new GA(instance,size1);
                        ga.geneticAlgorithm(outFile,60000,crossType,selectionType,mutationType,0.1,generation2OPT,ppb1,0);

                        outFile = new File(sFile + "/" +crossType + " " +  selectionType  + " " + mutationType + " g2opt = " + generation2OPT + " ppb = " + ((int)(ppb2*1000)) + " size" + size1 + ".csv");
                        ga = new GA(instance,size1);
                        ga.geneticAlgorithm(outFile,60000,crossType,selectionType,mutationType,0.1,generation2OPT,ppb2,0);

                        outFile = new File(sFile + "/" +crossType + " " +  selectionType  + " " + mutationType + " g2opt = " + generation2OPT + " ppb = " + ((int)(ppb1*1000)) + " size = " + size2 + ".csv");
                        ga = new GA(instance,size2);
                        ga.geneticAlgorithm(outFile,60000,crossType,selectionType,mutationType,0.1,generation2OPT,ppb1,0);

                        outFile = new File(sFile + "/" +crossType + " " +  selectionType  + " " + mutationType + " g2opt = " + generation2OPT + " ppb = " + ((int)(ppb2*1000)) + " size = " + size2 + ".csv");
                        ga = new GA(instance,size2);
                        ga.geneticAlgorithm(outFile,60000,crossType,selectionType,mutationType,0.1,generation2OPT,ppb2,0);

                    }
                }
            }
        }
    }


}