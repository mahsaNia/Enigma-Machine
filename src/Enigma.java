import java.io.File;
import java.io.IOException;
import java.util.*;

public class Enigma
{
    public static ArrayList<FileResult> fileResults = new ArrayList<>();
    public static void main(String[] args)
    {

        Scanner input = new Scanner(System.in);
        String result = "";
        String date = input.nextLine();
        String phrase = input.nextLine();
        ReadFile readFile = new ReadFile();
        readFile.read();
        Machine machine = new Machine();
        for (int i = 0; i < Enigma.fileResults.size(); i++)
        {
            if (fileResults.get(i).date.equals(date))
            {
                 result = machine.callFun(Enigma.fileResults.get(i).plugBoard, Enigma.fileResults.get(i).rotor1,
                        Enigma.fileResults.get(i).rotor2, Enigma.fileResults.get(i).rotor3, phrase);
                break;
            }
        }

        System.out.println(result);

    }
}

class ReadFile
{
    String dateName; String date;
    String plugBoardName; String plugBoard;
    String rotor1Name; String rotor1;
    String rotor2Name; String rotor2;
    String rotor3Name; String rotor3;

    public void read()
    {
        File file = new File("D:\\daneshgah T3\\EnigmaFile.txt");
        try(Scanner s = new Scanner(file))
        {
            while (s.hasNext())
            {
                dateName = s.next();
                date = s.nextLine();
                plugBoardName = s.next();
                plugBoard = s.nextLine();
                rotor1Name = s.next();
                rotor1 = s.nextLine();
                rotor2Name = s.next();
                rotor2 = s.nextLine();
                rotor3Name = s.next();
                rotor3 = s.nextLine();

                String d = date.replaceAll("\\s", "");

                String p = plugBoard.replaceAll("\\s", "");
                p = p.replaceAll("," , "");
                p = p.replaceAll("]" , "");
                p = p.replace("[", "");
                String[] newPlugBoard = new String[50];
                newPlugBoard = p.split("");

                String r1 = rotor1.replaceAll("\\s", "");
                r1 = r1.replaceAll("]" , "");
                r1 = r1.replace("[", "");

                String r2 = rotor2.replaceAll("\\s", "");
                r2 = r2.replaceAll("]" , "");
                r2 = r2.replace("[", "");

                String r3 = rotor3.replaceAll("\\s", "");
                r3 = r3.replaceAll("]" , "");
                r3 = r3.replace("[", "");

                FileResult fileResult = new FileResult(d, newPlugBoard, r1, r2,
                        r3);
                Enigma.fileResults.add(fileResult);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

class FileResult
{
    String date;
    String[] plugBoard;
    String rotor1;
    String rotor2;
    String rotor3;

    public FileResult(String date, String[] plugBoard, String rotor1, String rotor2, String rotor3)
    {
        this.date = date;
        this.plugBoard = plugBoard;
        this.rotor1 = rotor1;
        this.rotor2 = rotor2;
        this.rotor3 = rotor3;
    }
}

class Machine
{
    HashMap<String, String> plugBoard = new HashMap<>();
    HashMap<String, String> rotor1 = new HashMap<>();
    HashMap<String, String> rotor2 = new HashMap<>();
    HashMap<String, String> rotor3 = new HashMap<>();
    HashMap<String, String> reflector = new HashMap<>();
    String result = "";

    public String callFun(String[] newPlugBoard, String r1, String r2, String r3, String phrase)
    {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String [] alphabet2;
        alphabet2 = alphabet.split("");

        // initialize
        initializePlugBoard(newPlugBoard, alphabet2);
        initializeRotor1(r1, alphabet2);
        initializeRotor2(r2, alphabet2);
        initializeRotor3(r3, alphabet2);
        initializeReflector(alphabet2);


        //encoding
        int countRotor3 = 0; String newR1 = r1; String newR2 = r2; String newR3 = r3;
        int countRotor2 = 0;
        for (int i = 0; i < phrase.length(); i++)
        {

            String str = Character.toString(phrase.charAt(i));
            result = encoding(str);


            if(countRotor3 < 26)
            {
                newR3 = rightRotate(newR3, 1);
                countRotor3++;
            }

            //  اگر روتور 3 یک دور کامل بچرخد
            if(countRotor3 == 26)
            {
                newR2 = rightRotate(newR2, 1);
                countRotor3 = 0;
                countRotor2++;
            }

            // اگر روتور 2 یک دور کامل بچرخد
            if(countRotor2 == 26)
            {
                newR1 = rightRotate(newR1, 1);
                countRotor2 = 0;
            }

            initializeRotor1(newR1, alphabet2);
            initializeRotor2(newR2, alphabet2);
            initializeRotor3(newR3, alphabet2);
        }

        return result;
    }

    public void initializePlugBoard(String[] newPlugBoard,  String [] alphabet2)
    {
        for (int i = 0; i < alphabet2.length; i++)
        {
            plugBoard.put(alphabet2 [i], alphabet2 [i]);
        }

        for (int j = 0; j < newPlugBoard.length; j = j + 2)
        {
            plugBoard.put(newPlugBoard [j], newPlugBoard [j + 1]);
        }

        for (int k = 0; k < newPlugBoard.length; k = k + 2)
        {
            plugBoard.put(newPlugBoard [k + 1], newPlugBoard [k]);
        }

    }


    public void initializeRotor1(String r1,  String [] alphabet2)
    {
        String[] newRotor1;
        newRotor1 = r1.split("");

        for (int i = 0; i < alphabet2.length; i++)
        {
            rotor1.put(alphabet2 [i], newRotor1 [i]);
        }
    }

    public void initializeRotor2(String r2,  String [] alphabet2)
    {
        String[] newRotor2;
        newRotor2 = r2.split("");

        for (int i = 0; i < alphabet2.length; i++)
        {
            rotor2.put(alphabet2 [i], newRotor2 [i]);
        }
    }

    public void initializeRotor3(String r3,  String [] alphabet2)
    {
        String[] newRotor3;
        newRotor3 = r3.split("");

        for (int i = 0; i < alphabet2.length; i++)
        {
            rotor3.put(alphabet2 [i], newRotor3 [i]);
        }
    }

    public void initializeReflector(String [] alphabet2)
    {
        for (int i = 0, j = alphabet2.length - 1; i < alphabet2.length / 2; i++, j--)
        {
            reflector.put(alphabet2 [i], alphabet2 [j]);
        }

        for (int i = 13, j = 12; i < alphabet2.length ; i++, j--)
        {
            reflector.put(alphabet2 [i], alphabet2 [j]);
        }
    }

    public String encoding(String str)
    {

            // رفت
            String plugBoardResult = plugBoard.get(str);
            String rotor3Result = rotor3.get(plugBoardResult);
            String rotor2Result = rotor2.get(rotor3Result);
            String rotor1Result = rotor1.get(rotor2Result);
            String reflectorResult = reflector.get(rotor1Result);

            //برگشت
            String rotor1Key = "";
            for (Map.Entry<String, String> entry : rotor1.entrySet())
            {
                if (Objects.equals(reflectorResult, entry.getValue()))
                {
                    rotor1Key = entry.getKey();
                    break;
                }
            }

            String rotor2Key = "";
            for (Map.Entry<String, String> entry : rotor2.entrySet())
            {
                if (Objects.equals(rotor1Key, entry.getValue()))
                {
                    rotor2Key = entry.getKey();
                    break;
                }
            }

            String rotor3Key = "";
            for (Map.Entry<String, String> entry : rotor3.entrySet())
            {
                if (Objects.equals(rotor2Key, entry.getValue()))
                {
                    rotor3Key = entry.getKey();
                    break;
                }
            }

            String plugBoardKey = "";
            plugBoardKey = plugBoard.get(rotor3Key);

            result = result + plugBoardKey;
            return result;
    }


    public String leftRotate(String str, int d)
    {
        String ans = str.substring(d) + str.substring(0, d);
        return ans;
    }

    public String rightRotate(String str, int d)
    {
        return leftRotate(str, str.length() - d);
    }

}
