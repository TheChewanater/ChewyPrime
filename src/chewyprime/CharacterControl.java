package chewyprime;

import chewyprime.rpg.Character;

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
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.Spatial;
import com.jme3.math.*;

/**
 * Base class for controls for any character, including monsters and the player.
 * 
 * @author Thomas Clark
 */
public class CharacterControl extends AbstractControl
{
  protected Character mCharacter;
  
  protected AnimControl mControl = null;  
  protected AnimChannel mChannel = null;
  
  /* If an attack animation is currently playing, controlUpdate needs to return
    early */
  protected boolean mAttacking = false;
  
  protected boolean mDead = false;
  
  public CharacterControl(Character character)
  {
    mCharacter = character;
  }
  
  public void controlUpdate(float time)
  {
  }
  
  public Character getCharacter()
  {
    return mCharacter;
  }
  
  public Control cloneForSpatial(Spatial spatial)
  {
    return new CharacterControl(mCharacter);
  }
  
  public void controlRender(RenderManager rm, ViewPort vp)
  {
  }
  
  public void setSpatial(Spatial spatial)
  {
    super.setSpatial(spatial);
    
    mControl = spatial.getControl(AnimControl.class);
    mControl.addListener(new AnimEventListener() {
      public void onAnimChange(AnimControl control, AnimChannel channel, String name)
      {
      }
      
      public void onAnimCycleDone(AnimControl control, AnimChannel channel, String name)
      {
        if(name.equals("Melee Attack"))
          {
            mAttacking = false;
            
            mChannel.setAnim("Idle", 0.25f);
            mChannel.setLoopMode(LoopMode.Loop);
            mChannel.setSpeed(1.0f);
          }
      }
    });
    
    mChannel = mControl.createChannel();
    mChannel.setAnim("Idle");
    
    SkeletonControl skeletonControl = spatial.getControl(SkeletonControl.class);
    SceneManager.instance.loadModel(skeletonControl.getAttachmentsNode("Left Hand"), "Models/Sword.mesh.xml", "Materials/Metal.j3m");
  }
}