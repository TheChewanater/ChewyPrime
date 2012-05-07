package chewyprime.rpg;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

public class Armor implements Savable
{
  private String mDescription;
  private int mModifier;
  
  public Armor(String description, int modifier)
  {
    mDescription = description;
    mModifier = modifier;
  }
  
  public Armor()
  {
    this("", 0);
  }
  
  public void setDescription(String description)
  {
    mDescription = description;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public void setModifier(int modifier)
  {
    mModifier = modifier;
  }
  
  public int getModifier()
  {
    return mModifier;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(mDescription, "description", "");
    capsule.write(mModifier, "modifier", 0);
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    mDescription = capsule.readString("description", "");
    mModifier = capsule.readInt("modifier", 0);
  }
}

