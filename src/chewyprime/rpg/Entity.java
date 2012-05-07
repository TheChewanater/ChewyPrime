package chewyprime.rpg;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

/**
 * Base class for any game entity, including the player, enemies, and inanimate
 * objects.
 * 
 * @author Thomas Clark
 */
public class Entity implements Savable
{
  protected String mName = "";
  
  public Entity(String name)
  {
    mName = name;
  }
  
  public void setName(String name)
  {
    mName = name;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(mName, "name", null);
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    mName = (String)capsule.readString("name", null);
  }
}
