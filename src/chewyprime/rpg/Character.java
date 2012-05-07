package chewyprime.rpg;

import java.io.IOException;
import java.util.Set;
import java.util.HashMap;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import chewyprime.rpg.actions.*;

/**
 * Any sort of character, including the player or enemies.
 * 
 * @author Thomas Clark
 */
public class Character extends Entity
{
  public enum Class
  {
    Fighter, Rogue, Magi, Cleric, Monster
  }
  
  public Ability mStr = new Ability(),
                 mDex = new Ability(),
                 mCon = new Ability(),
                 mInt = new Ability(),
                 mWis = new Ability(),
                 mCha = new Ability();
  
  public Class mClass = Class.Fighter;
  public Armor mArmor = null;
  public Weapon mWeapon = null;
  
  public int mLevel = 0;
  public int mHitPoints = 0;
  public int mVitality = 0;
  public boolean mIncapacitated = false;
  
  private HashMap<String, Action> mActions = new HashMap<String, Action>();
  
  public Character()
  {
    this("");
  }
  
  public Character(String name)
  {
    super(name);
    
    mActions.put("Unarmed Attack", new UnarmedAttackAction());
    mActions.put("Melee Attack", new MeleeAttackAction());
    mActions.put("Ranged Attack", new RangedAttackAction());
  }
  
  public Set<String> getUsableActions(Entity target)
  {
    return mActions.keySet();
  }
  
  public ActionResult useAction(String actionName, Entity target)
  {
    Action action = mActions.get(actionName);
    
    if(action == null)
      return new ActionResult(false, "No such action '" + actionName + "'", 0);
    
    if(!action.canAct(this, target))
      return new ActionResult(false, "Cannot use '" + actionName + "'", 0);
    
    return action.act(this, target);
  }
  
  public void damage(int points)
  {
    mVitality -= points;
    
    mIncapacitated = mVitality <= 0;
  }
  
  public void heal(int points)
  {
    mVitality += points;
    
    if(mVitality > mHitPoints)
      mVitality = mHitPoints;
    
    mIncapacitated = mVitality <= 0;
  }
  
  /**
   * Roll to find out how much damage is done if a hit is successful, according
   * to <a href='http://www.d20srd.org/srd/combat/combatStatistics.htm#damage'>
   * http://www.d20srd.org/srd/combat/combatStatistics.htm#damage</a>
   */
  public int getDamage()
  {
    int damage = 0;
    
    if(mWeapon != null)
      {
        damage += mWeapon.getDamage().roll();
        
        /* Melee weapons recieve a strength modifier */
        if(mWeapon.getType() == Weapon.Type.Melee)
          damage += mStr.getModifier();
      }
    else
      {
        /* Unarmed strike */
        damage += new Die("1d3").roll();
      }
    
    if(damage < 1) damage = 1;
    
    return damage;
  }
  
  /**
   * Get the bonus added to attack roll, according to
   * <a href='http://www.d20srd.org/srd/combat/combatStatistics.htm#attackRoll'>
   * http://www.d20srd.org/srd/combat/combatStatistics.htm#attackRoll</a>
   */
  public int getAttackBonus()
  {
    if(mWeapon == null)
      return mLevel;
    else if(mWeapon.getType() == Weapon.Type.Melee)
      return mLevel + mStr.getModifier();
    else /* mWeapon.getType() == Weapon.Type.Ranged */
      return mLevel + mDex.getModifier();
  }
  
  /**
   * Roll to find out what an attack roll must reach for a successful hit
   * according to <a href='http://www.d20srd.org/srd/combat/combatStatistics.htm#damage'>
   * http://www.d20srd.org/srd/combat/combatStatistics.htm#damage</a>
   */
  public int getArmorClass()
  {
    int armorClass = 10 + mDex.getModifier();
    
    if(mArmor != null)
      armorClass += mArmor.getModifier();
    
    return armorClass;
  }
  
  public String toString()
  {
    String str =
      mName + ": " +
      mClass + " level " + mLevel + "\n" +
      "\tHp=" + mVitality + "/" + mHitPoints +
      " Str=" + mStr.getScore() +
      " Dex=" + mDex.getScore() +
      " Con=" + mCon.getScore() +
      " Int=" + mInt.getScore() +
      " Wis=" + mWis.getScore() +
      " Cha=" + mCha.getScore();
    
    if(mArmor != null)
      str += "\n\t" + mArmor.getDescription() + ": Armor Class=" + getArmorClass();
    else
      str += "\n\t" + "Armor Class=" + getArmorClass();
    
    if(mWeapon != null)
      str += "\n\t" + mWeapon.getDescription() + ": Damage=" + mWeapon.getDamage();
    
    return str;
  }
  
  public void write(JmeExporter exporter) throws IOException
  {
    OutputCapsule capsule = exporter.getCapsule(this);
    capsule.write(mName, "name", "");
    capsule.write(mLevel, "level", 0);
    capsule.write(mHitPoints, "hitpoints", 0);
    capsule.write(mVitality, "vitality", 0);
    capsule.write(mStr, "str", null);
    capsule.write(mDex, "dex", null);
    capsule.write(mCon, "con", null);
    capsule.write(mInt, "int", null);
    capsule.write(mWis, "wis", null);
    capsule.write(mCha, "cha", null);
    capsule.write(mClass, "klass", null);
    capsule.write(mArmor, "armor", null);
    capsule.write(mWeapon, "weapon", null);
  }
  
  public void read(JmeImporter importer) throws IOException
  {
    InputCapsule capsule = importer.getCapsule(this);
    mName = capsule.readString("name", "");
    mLevel = capsule.readInt("level", 0);
    mHitPoints = capsule.readInt("hitpoints", 0);
    mVitality = capsule.readInt("vitality", 0);
    mStr = (Ability)capsule.readSavable("str", null);
    mDex = (Ability)capsule.readSavable("dex", null);
    mCon = (Ability)capsule.readSavable("con", null);
    mInt = (Ability)capsule.readSavable("int", null);
    mWis = (Ability)capsule.readSavable("wis", null);
    mCha = (Ability)capsule.readSavable("cha", null);
    mClass = capsule.readEnum("klass", Class.class, null);
    mArmor = (Armor)capsule.readSavable("armor", null);
    mWeapon = (Weapon)capsule.readSavable("weapon", null);
  }
}

