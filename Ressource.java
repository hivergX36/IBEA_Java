package com.nsga2.nsga2;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Ressource{

    String filepathString;


    Ressource(){


        

        



    }

    public String getfileinressource(String name){

    
    Path path = Paths.get("./nsga2/src/main/resources/instances" + "/" + name);

    this.filepathString = path.toString(); 
    return this.filepathString;



    }

    public String getfileinressourcefortest(String name){
            
    Path path = Paths.get("../nsga2/src/main/resources/instances" + "/" + name);

    this.filepathString = path.toString(); 
    return this.filepathString;



    }



    

}