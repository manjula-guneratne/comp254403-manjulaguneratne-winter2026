package ex3;
import java.io.File;

public class solution3_3 {

    public static void find(File path, String filename){

        //Base case
        if(path == null || !path.exists())
            return;

        //Get directory contents
        File[] files = path.listFiles();

        //If empty
        if(files == null)
            return;

        //Check each item
        for(File f:files){

            if(f.isDirectory()){
                find(f,filename);
            }
            else if(f.getName().equals(filename)){
                //Found matching file
                System.out.println("Found: "+f.getAbsolutePath());
            }
        }
    }


    public static void main(String[] args) {

        File starPath = new File("C:\\Users\\Owner\\OneDrive\\Desktop\\College");
        String targetFile = "test.txt";

        find(starPath, targetFile);
    }
}
