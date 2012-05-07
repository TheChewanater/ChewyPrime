package chewyprime.rpg;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

public class Weapon implements Savable
{
  private String mDescription;
  private Type mType;
  private Die mDamage;
  
  public enum Type
  {
    Melee, Ranged
  }
  
  public Weapon(String description, Die damage, Type type)
  {
    mDescription = description;
    mDamage = damage;
    mType = type;
  }
  
  public Weapon()
  {
    this("", new Die(), Type.Melee);
  }
  
  public void setDescription(String description)
  {
    mDescription = description;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public void setDamage(Die damage)
  {
    mDamage = damage;
  }
  
  public Die getDamage()
  {
    return mDamage;
  }
  
  public Type getType()
  {
    return mType;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(mDescription, "description", "");
    capsule.write(mDamage.toString(), "damage", null);
    capsule.write(mType, "type", Type.Melee);
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    mDescription = capsule.readString("description", "");
    mDamage = new Die(capsule.readString("damage", "1d20"));
    mType = capsule.readEnum("type", Type.class, Type.Melee);
  }
}

