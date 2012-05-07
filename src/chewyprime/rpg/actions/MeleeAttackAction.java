package chewyprime.rpg.actions;

import chewyprime.rpg.*;

/**
 * Attack someone while holding a melee weapon, adding the strength modifier to
 * the attack roll.
 */
public class MeleeAttackAction extends AttackAction
{
  public boolean canAct(chewyprime.rpg.Character user, Entity target)
  {
    return (user.mWeapon != null) &&
           (user.mWeapon.getType() == Weapon.Type.Melee) && /* User must be armed with a melee weapon*/
           (user.mIncapacitated == false) && /* User cannot be incapacitated */
           (target != null) &&
           (target instanceof chewyprime.rpg.Character); /* User must be targeting a character */
  }
}