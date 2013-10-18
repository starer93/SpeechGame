import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Testing
{
    
    public Testing()
    {
        try
        {
            TextImporter importer = new TextImporter("words.txt");
            testOne(importer);
            testNumberOfWord(importer);
        }
        catch(IOException e)
        {
            System.out.println("ERROR!! Cannot read the file");
        }

    }
    
    public static void main(String[] args)
    {
        System.out.println("Unit testing for TextImporter.java");
        System.out.println("--------------------------------------------------------------");
        System.out.println("");
        Testing test = new Testing();
    }
    
    private void testOne(TextImporter importer)
    {
        importer.makeCharArray(10);
        importer.getCharList();
    }
    
    private void testGrid(TextImporter importer)
    {
        importer.makeCharArray(10);
        importer.getCharList();
    }
    
    private void testNumberOfWord(TextImporter importer)
    {
        importer.makeCharArray(10);
        if( importer.getNumberOfWordsInList() == 20)
        {
            System.out.println("o Number of word in the word list is correct");
        }
        else
        {
             System.out.println("x Number if word in the word list is incorrect");
        }
    }
}
