package chewyprime.rpg.actions;

import chewyprime.rpg.*;

/**
 * Abstract "attack" action that handles attack rolls and damage.  The
 * subclasses MeleeAttackAction, UnarmedAttackAction, and RangedAttackAction
 * determine further information, such as the conditions required to use each
 * type of attack.
 */
public abstract class AttackAction implements Action
{
  public Type getType()
  {
    return Type.Standard;
  }
  
  public ActionResult act(chewyprime.rpg.Character user, Entity targetEntity)
  {
    chewyprime.rpg.Character target = (chewyprime.rpg.Character)targetEntity;
    
    int attackBonus = user.getAttackBonus();
    int armorClass = target.getArmorClass();
    int attackRoll = Die.d20.roll();
    
    /* Automatic failure */
    if(attackRoll == 1)
      {
        return new ActionResult(false, "Miss", 0);
      }
    /* Success if the roll is 20, or if the total attack is meets or exceeds the armor class */
    else if(attackRoll == 20 || (attackRoll + attackBonus) >= armorClass)
      {
        int damage = user.getDamage();
        target.damage(damage);
        
        return new ActionResult(true, null, damage);
      }
    
    return new ActionResult(false, "Miss", 0);
  }
}
