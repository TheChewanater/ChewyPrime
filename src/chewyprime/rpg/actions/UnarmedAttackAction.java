package chewyprime.rpg.actions;

import chewyprime.rpg.*;

/**
 * Attack someone without a weapon.
 */
public class UnarmedAttackAction extends AttackAction
{
  public boolean canAct(chewyprime.rpg.Character user, Entity target)
  {
    return (user.mWeapon == null) && /* User is not holding a weapon */
           (user.mIncapacitated == false) && /* User cannot be incapacitated */
           (target != null) &&
           (target instanceof chewyprime.rpg.Character); /* User must be targeting a character */
  }
}
