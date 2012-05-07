package chewyprime.rpg.actions;

public class ActionResult
{
  protected boolean mSuccessful;
  protected String mWhy;
  protected int mDamage;
  
  public ActionResult(boolean successful, String why, int damage)
  {
    mSuccessful = successful;
    mWhy = why;
    mDamage = damage;
  }
  
  public boolean successful()
  {
    return mSuccessful;
  }
  
  /**
   * If the action was not successful, why not.  If it was, this should return
   * null.
   */
  public String why()
  {
    return mWhy;
  }
  
  /**
   * The damage dealt, healed, or whatever the action did.
   */
  public int damage()
  {
    return mDamage;
  }
}