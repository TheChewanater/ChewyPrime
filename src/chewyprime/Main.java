package chewyprime;

import chewyprime.rpg.Character;
import chewyprime.rpg.actions.ActionResult;

import java.io.*;

import com.jme3.app.Application;
import com.jme3.audio.AudioNode;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.export.xml.XMLImporter;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeSystem;


import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.*;
import de.lessvoid.nifty.elements.*;
import de.lessvoid.nifty.elements.render.*;
import de.lessvoid.xml.xpp3.Attributes;

public class Main extends Application implements ScreenController
{
  public static Main instance = new Main();
  public static void main(String[] args) {}
  
  protected SceneManager mSceneManager = SceneManager.instance;
  protected XMLImporter mImporter = new XMLImporter();
  
  protected Spatial mPlayer, mEnemy;
  protected CharacterControl mPlayerControl;
  
  public Main()
  {
    super();
    
    AppSettings settings = new AppSettings(true);
    
    if(JmeSystem.showSettingsDialog(settings, true))
      {
        setSettings(settings);
        super.start();
      }
  }
  
  public void initialize()
  {
    super.initialize();
    
    mSceneManager.setupScene();
    mSceneManager.setupGUI();
    mSceneManager.setupFilters();
    
    try
      {
        /* Player */
        mPlayer = mSceneManager.loadModel(null, "Models/Tux.mesh.xml", "Materials/Tux.j3m", new Vector3f(0, 0, 6));
        Character playerCharacter = (Character)mImporter.load(new File("tux.character"));
        mPlayer.addControl(new PlayerControl(inputManager, cam, playerCharacter));
        
        mSceneManager.setCameraTarget(mPlayer);
        
        /* Enemy */
        mEnemy = mSceneManager.loadModel(null, "Models/Tux.mesh.xml", "Materials/Tux.j3m", new Vector3f(0, 0, 2));
        Character enemyCharacter = (Character)mImporter.load(new File("narwhal.character"));
        mEnemy.addControl(new CharacterControl(enemyCharacter));
        
        updateHealth(playerCharacter, enemyCharacter);
      }
    catch(IOException e)
      {
        stop();
        System.out.println(e);
      }
    
    inputManager.addMapping("Main::escape", new KeyTrigger(KeyInput.KEY_ESCAPE));
    inputManager.addListener(
      new ActionListener()
      {
        public void onAction(String name, boolean isPressed, float time)
        {
          /* If the escape button is being depressed, pause or unpause the game. */
          if(!isPressed && name.equals("Main::escape"))
            mSceneManager.togglePause();
        }
      },
      "Main::escape");
  }
  
  public void update()
  {
    super.update();
    
    float time = timer.getTimePerFrame();
    
    if(!mSceneManager.isPaused())
      {
        try
          {
            Character playerCharacter = mPlayer.getControl(CharacterControl.class).getCharacter();
            Character enemyCharacter = mEnemy.getControl(CharacterControl.class).getCharacter();
            updateHealth(playerCharacter, enemyCharacter);
          }
        catch(NullPointerException e)
          {
          }
      }
    
    mSceneManager.update(time);
    renderManager.render(time, true);
  }
  
  /**
   * updateHealth()
   * Update both health bars and the text on them based on the player's and
   * the target's health.
   */
  protected void updateHealth(Character playerCharacter, Character targetCharacter)
  {
    Element targetHealthBar = mSceneManager.mNifty.getCurrentScreen().findElementByName("target-healthbar");
    targetHealthBar.setWidth(100 * targetCharacter.mVitality / targetCharacter.mHitPoints);
    
    Element targetHealth = mSceneManager.mNifty.getCurrentScreen().findElementByName("target-health");
    targetHealth.getRenderer(TextRenderer.class).setText(
      targetCharacter.getName() + ": " + targetCharacter.mVitality + "/" + targetCharacter.mHitPoints);
    
    Element playerHealthBar = mSceneManager.mNifty.getCurrentScreen().findElementByName("player-healthbar");
    playerHealthBar.setWidth(100 * playerCharacter.mVitality / playerCharacter.mHitPoints);
    
    Element playerHealth = mSceneManager.mNifty.getCurrentScreen().findElementByName("player-health");
    playerHealth.getRenderer(TextRenderer.class).setText(
      playerCharacter.getName() + ": " + playerCharacter.mVitality + "/" + playerCharacter.mHitPoints);
  }
  
  public void action(String screen, String action)
  {
    if(screen.equals("hud"))
      {
        try
          {
            PlayerControl playerControl = mPlayer.getControl(PlayerControl.class);
            Character playerCharacter = playerControl.getCharacter();
            Character enemyCharacter = mEnemy.getControl(CharacterControl.class).getCharacter();
            
            ActionResult result = playerControl.useAction(action, enemyCharacter);
            
            System.out.println(playerCharacter.getName() + " uses '" + action + "' on " + enemyCharacter.getName());
            
            if(result.successful())
              {
                System.out.println("It success: " + result.damage());
              }
            else
              System.out.println("It fails: " + result.why());
          }
        catch(Exception e)
          {
            System.out.println(e.toString());
          }
      }
    else if(screen.equals("pause"))
      {
        if(action.equals("Resume"))
          mSceneManager.unpause();
        else if(action.equals("Quit"))
          mSceneManager.mNifty.gotoScreen("quit");
      }
    else if(screen.equals("quit"))
      {
        if(action.equals("Back"))
          mSceneManager.mNifty.gotoScreen("pause");
        else if(action.equals("Quit"))
          stop();
      }
  }
  
  public void bind(Nifty nifty, Screen screen) {}
  public void onStartScreen() {}
  public void onEndScreen() {}
}

