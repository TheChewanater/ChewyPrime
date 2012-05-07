package chewyprime.rpg.actions;

import chewyprime.rpg.*;

/**
 * Attack someone while holding a Ranged weapon, adding the constitution
 * modifier to the attack roll.
 */
public class RangedAttackAction extends AttackAction
{
  public boolean canAct(chewyprime.rpg.Character user, Entity target)
  {
    return (user.mWeapon != null) &&
           (user.mWeapon.getType() == Weapon.Type.Ranged) && /* User must be armed with a Ranged weapon*/
           (user.mIncapacitated == false) && /* User cannot be incapacitated */
           (target != null) &&
           (target instanceof chewyprime.rpg.Character); /* User must be targeting a character */
  }
}