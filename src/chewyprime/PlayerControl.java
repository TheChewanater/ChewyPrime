package chewyprime;

import chewyprime.rpg.Character;
import chewyprime.rpg.actions.ActionResult;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.Control;
import com.jme3.scene.Spatial;
import com.jme3.math.*;

/**
 * Controls the human player only, using mouse/keyboard input.
 * 
 * @author Thomas Clark
 */
class PlayerControl extends CharacterControl
{
  protected Camera mCamera;
   
  /* The state of each of the WASD keys, in order */
  protected boolean[] mWASD = { false, false, false, false };
  
  private float mSpeed = 0.0f;
  private static final float maxSpeed = 4.0f;
  private static final float acceleration = 8.0f;
  
  public PlayerControl(InputManager inputManager, Camera cam, Character character)
  {
    super(character);
    
    mCamera = cam;
    
    inputManager.addMapping("PlayerControl::w", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("PlayerControl::a", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("PlayerControl::s", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("PlayerControl::d", new KeyTrigger(KeyInput.KEY_D));
    
    String[] actions = { "PlayerControl::w", "PlayerControl::a", "PlayerControl::s", "PlayerControl::d" };
    inputManager.addListener(mActionListener, actions);
  }
  
  protected ActionListener mActionListener = new ActionListener()
  {
    public void onAction(String name, boolean isPressed, float time)
    {
      if(isPressed)
        {
          if(name == "PlayerControl::w")
            mWASD[0] = true;
          else if(name == "PlayerControl::a")
            mWASD[1] = true;
          else if(name == "PlayerControl::s")
            mWASD[2] = true;
          else if(name == "PlayerControl::d")
            mWASD[3] = true;
        }
      else
        {
          if(name == "PlayerControl::w")
            mWASD[0] = false;
          else if(name == "PlayerControl::a")
            mWASD[1] = false;
          else if(name == "PlayerControl::s")
            mWASD[2] = false;
          else if(name == "PlayerControl::d")
            mWASD[3] = false;
        }
    }
  };
  
  public void controlUpdate(float time)
  {
    /* The player spatial might not have been set yet for some reason */
    if(spatial == null) return;
    
    /* Don't interupt an attack animation */
    if(mAttacking) return;
    
    if(mDead)
      {
        
      }
    
    /* Only update if the player will actually move one way or another */
    if((mWASD[0] != mWASD[2]) || (mWASD[1] != mWASD[3]))
      {
        /* Figure out the direction and rotate it to the camera's local transformation */
        Vector3f velocity = new Vector3f();
        if(mWASD[0]) velocity.z += 1.0f;
        if(mWASD[1]) velocity.x += 1.0f;
        if(mWASD[2]) velocity.z -= 1.0f;
        if(mWASD[3]) velocity.x -= 1.0f;
        mCamera.getRotation().mult(velocity, velocity);
        
        /* Convert it to a 2d vector of the correct magnitude */
        if(mSpeed < maxSpeed)
          mSpeed += time * acceleration;
        
        velocity.y = 0;
        velocity.normalizeLocal();
        velocity.multLocal((float)(mSpeed * time));
        
        /* Set the new transformation */
        Transform transform = spatial.getLocalTransform();
        
        Quaternion newRotation = new Quaternion(transform.getRotation());
        newRotation.lookAt(velocity, new Vector3f(0, 1, 0));
        
        /* Smoothly interpolate to the new rotation so the player doesn't abruptly flip around */
        transform.getRotation().slerp(newRotation, (float)(time * 10.0f));
        transform.getTranslation().addLocal(velocity);
        
        spatial.setLocalTransform(transform);
        
        /* Since the player is moving, switch to the "Walk" animation */
        if(!mChannel.getAnimationName().equals("Walk Forwards"))
          {
            mChannel.setAnim("Walk Forwards", 0.1f);
            mChannel.setLoopMode(LoopMode.Loop);
          }
        
        mChannel.setSpeed(velocity.length() * 30.0f);
    }
    else
      {
        /* The player isn't moving - switch to the "Idle" animation */
        if(!mChannel.getAnimationName().equals("Idle"))
          {
            mChannel.setAnim("Idle", 0.25f);
            mChannel.setLoopMode(LoopMode.Loop);
          }
        
        mChannel.setSpeed(1.0f);
        
        mSpeed = 0.0f;
      }
  }
  
  public ActionResult useAction(String name, Character target)
  {
    if(!mAttacking)
      {
        mAttacking = true;
        
        mChannel.setAnim(name, 0.05f);
        mChannel.setLoopMode(LoopMode.DontLoop);
        mChannel.setSpeed(1.0f);
        
        return mCharacter.useAction(name, target);
      }
    
    /* Player is in the middle of attacking already */
    return new ActionResult(false, "", 0);
  }
  
  public Control cloneForSpatial(Spatial spatial)
  {
    try
      {
        return (PlayerControl)clone();
      }
    catch(CloneNotSupportedException e)
      {
        return null;
      }
  }
}

