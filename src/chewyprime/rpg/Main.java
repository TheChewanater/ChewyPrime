package chewyprime.rpg;

import java.io.File;
import java.io.IOException;
import com.jme3.export.xml.*;

public class Main
{
  public static void main(String[] args) throws IOException
  {
    XMLImporter importer = new XMLImporter();
    XMLExporter exporter = new XMLExporter();
    
    Character tux = (Character)importer.load(new File("tux.character"));
    Character narwhal = (Character)importer.load(new File("narwhal.character"));
    
    System.out.println(tux);
    System.out.println(narwhal);
  }
}