import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import com.Ibea;

import java.util.*;
import java.lang.reflect.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.LinkOption; 

public class Main{

public static void main(String[] args) throws IOException {
   Ressource ressource = new Ressource();
    String path_file = ressource.getfileinressource("knapsack.txt");
    System.out.println(path_file);

Ibea IbeaAlgorithm = new Ibea(path_file,50,20);

System.out.println(IbeaAlgorithm.NbVariable);
IbeaAlgorithm.resolve(100);
}
}







