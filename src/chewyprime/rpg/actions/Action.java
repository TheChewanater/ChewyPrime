package chewyprime.rpg.actions;

/**
 * Interface for combat actions such as attacking or using an item
 */
public interface Action
{
  public enum Type
  {
    Standard, Move, Immediate
  }
  
  public Type getType();
  
  public boolean canAct(chewyprime.rpg.Character user, chewyprime.rpg.Entity target);
  
  /**
   * Perform the action, returning and ActionResult to indicate if it was
   * succesful, how much damage was dealth, etc.
   */
  public ActionResult act(chewyprime.rpg.Character user, chewyprime.rpg.Entity arget);
}
