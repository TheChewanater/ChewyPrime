package chewyprime.rpg;

import java.io.IOException;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

/**
 * A simple wrapper for an integer that clamps its value to and lets you easily
 * compute the ability modifer.
 * 
 * @author Thomas Clark
 */
public class Ability implements Savable
{
  private int mScore;
  
  public Ability(int score)
  {
    setScore(score);
  }
  
  public Ability()
  {
    this(10);
  }
  
  public void setScore(int score)
  {
    mScore = score;
    
    if(mScore < 0) mScore = 0;
    else if(mScore > 50) mScore = 20;
  }
  
  public int getScore()
  {
    return mScore;
  }
  
  /**
   * Calculate the ability modifier, which is equal to (score - 10) / 2,
   * rounded down.
   */
  public int getModifier()
  {
    return (mScore - 10) / 2;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(mScore, "score", 10);
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    mScore = capsule.readInt("score", 10);
  }
}
