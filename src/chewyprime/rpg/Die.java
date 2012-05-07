package chewyprime.rpg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Integer;
import java.io.IOException;

import com.jme3.math.FastMath;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

/**
 * A PRNG utility class supporting D&D-style dice notation (like <code>1d20+5</code>)
 */
public class Die implements Savable
{
  private int mNumDice = 0,
              mNumSides = 0,
              mOffset = 0;
  
  private static final Pattern diePattern = Pattern.compile("[\\d]+d[\\d]+(\\+(-)?[\\d]+)?");
  
  public static Die d4 = new Die("1d4");
  public static Die d6 = new Die("1d6");
  public static Die d8 = new Die("1d8");
  public static Die d10 = new Die("1d10");
  public static Die d12 = new Die("1d12");
  public static Die d20 = new Die("1d20");
  
  /**
   * Construct a die based on a string representation.
   * See <a href='http://www.d20srd.org/srd/theBasics.htm#dice'>d20srd.org</a> for details.
   */
  public Die(String str)
  {
    parseString(str);
  }
  
  /**
   * Construct a die given raw integer values.
   * @param numDice The number of dice to roll.  Their values are all added together.
   * @param numSides The number of sides of each dice (usually 6 or 20).
   * @param offset A constant added to the final total.
   */
  public Die(int numDice, int numSides, int offset)
  {
    mNumDice = numDice;
    mNumSides = numSides;
    mOffset = offset;
  }
  
  public Die()
  {
    this(1, 20, 0);
  }
  
  private void parseString(String str)
  {
    Matcher matcher = diePattern.matcher(str);
    
    if(matcher.find())
      {
        String match = matcher.group();
        
        /* Set anything after the plus sign as the offset, if it's present */
        if(match.contains("+"))
          {
            String[] split = match.split("\\+");
            
            match = split[0];
            mOffset = Integer.parseInt(split[1]);
          }
        
        /* Set the number left of the "d" as the number of dice, and the number
          to the right of the "d" as the number of sides on each die */
        String[] split = match.split("d");
        mNumDice = Integer.parseInt(split[0]);
        mNumSides = Integer.parseInt(split[1]);
      }
    else
      {
        System.out.println("Warning: invalid die string \"" + str + "\"");
      }
  }
  
  public int roll()
  {
    int total = 0;
    
    for(int i = 0; i < mNumDice; i++)
      total += FastMath.nextRandomInt(1, mNumSides);
    
    return total + mOffset;
  }
  
  public int getNumDice() { return mNumDice; }
  public int getNumSides() { return mNumSides; }
  public int getOffset() { return mOffset; }
  
  public String toString()
  {
    if(mOffset == 0)
      return mNumDice + "d" + mNumSides;
    else
      return mNumDice + "d" + mNumSides + "+" + mOffset;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(toString(), "value", "1d20");
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    parseString(capsule.readString("value", "1d20"));
  }
}

