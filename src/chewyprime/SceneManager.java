package chewyprime;

import java.lang.reflect.Field;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;

import fixedtangentbinormalgenerator.FixedTangentBinormalGenerator;

import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.util.SkyFactory;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.input.*;
import com.jme3.scene.*;
import com.jme3.light.*;
import com.jme3.math.*;
import com.jme3.post.*;
import com.jme3.post.filters.*;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.nullobjects.ButtonNull;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.xml.xpp3.Attributes;

import chewyprime.rpg.Character;

class SceneManager
{
  public Node mRoot = new Node("Root");
  protected Nifty mNifty;
  
  protected AssetManager mAssetManager;
  protected InputManager mInputManager;
  protected ViewPort mViewPort;
  protected ViewPort mGuiViewPort;
  
  protected boolean mPaused = false;
  
  public static SceneManager instance = new SceneManager();
  
  /**
   * loadModel()
   * Utility function that loads a 3d model, sets a material, and adds it to the
   * scene graph.
   */
  public Spatial loadModel(Node parent, String model, String material, Vector3f position)
  {
    Spatial spatial = mAssetManager.loadModel(model);
    FixedTangentBinormalGenerator.generate(spatial);
    spatial.setMaterial(mAssetManager.loadMaterial(material));
    spatial.setLocalTranslation(position);
    
    if(parent == null)
      mRoot.attachChild(spatial);
    else
      parent.attachChild(spatial);
    
    return spatial;
  }
  
  public Spatial loadModel(Node parent, String model, String material)
  {
    return loadModel(parent, model, material, new Vector3f(0, 0, 0));
  }
  
  public Spatial loadModel(Node parent, String model)
  {
    return loadModel(parent, model, "Common/Materials/WhiteColor.j3m");
  }
  
  public void update(float time)
  {
    if(!mPaused)
      mRoot.updateLogicalState(time);
    
    mRoot.updateGeometricState();
  }
  
  /**
   * Set weather the logical state of the scene should not be updated each
   * frame.  If paused, it will still render, but nothing will move.
   */
  public void pause()
  {
    mNifty.gotoScreen("pause");
    mPaused = true;
  }
  
  public void unpause()
  {
    mNifty.gotoScreen("hud");
    mPaused = false;
  }
  
  public void togglePause()
  {
    if(mPaused)
      unpause();
    else
      pause();
  }
  
  public boolean isPaused()
  {
    return mPaused;
  }
  
  /**
   * setupScene()
   * Loads the models and materials for the scene and adds them to the scene graph
   */
  public void setupScene()
  {
    mAssetManager = Main.instance.getAssetManager();
    mInputManager = Main.instance.getInputManager();
    mViewPort = Main.instance.getViewPort();
    mGuiViewPort = Main.instance.getGuiViewPort();
    
    /* Environment */
    Node environment = new Node("Environment");
    
    loadModel(environment, "Models/Floor.mesh.xml", "Materials/Floor.j3m");
    loadModel(environment, "Models/Door.mesh.xml", "Materials/Door.j3m");
    
    mRoot.attachChild(environment);
    
    /* Light */
    DirectionalLight dlight = new DirectionalLight();
    dlight.setDirection(new Vector3f(0.0f, -1.0f, -0.5f));
    
    mRoot.addLight(dlight);
    
    mRoot.attachChild(SkyFactory.createSky(mAssetManager, "Textures/Water256.dds", false));
    mRoot.addLight(new AmbientLight());
    
    mViewPort.attachScene(mRoot);
  }
  
  public void setCameraTarget(Spatial target)
  {
    ChaseCamera camera = new ChaseCamera(Main.instance.getCamera(), target, mInputManager);
    camera.setLookAtOffset(new Vector3f(0.0f, 1.5f, 0.0f));
    camera.setInvertVerticalAxis(true);
    camera.setTrailingEnabled(true);
    camera.setRotationSensitivity(6.0f);
    camera.setTrailingSensitivity(5.0f);
    camera.setMinVerticalRotation(FastMath.PI * -0.25f);
    camera.setMaxVerticalRotation(FastMath.PI * +0.4f);
    camera.setMinDistance(4.0f);
    camera.setMaxDistance(16.0f);
    camera.setDefaultDistance(12.0f);
    
    /* ChaseCamera has a poor default setting for rotation speed, but it's a
      private field, so this mess is kind of necessary */
    try
      {
        Field rotationSpeed = ChaseCamera.class.getDeclaredField("rotationSpeed");
        rotationSpeed.setAccessible(true);
        rotationSpeed.setFloat(camera, 8.0f);
      }
    catch(Exception e) {}
  }
  
  /**
   * setupFilters()
   * Constructs several hardcoded filters and adds them to the main viewport
   */
  public void setupFilters()
  {
    BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
    bloom.setBlurScale(2.0f);
    bloom.setDownSamplingFactor(2.0f);
    
    FilterPostProcessor fpp = new FilterPostProcessor(mAssetManager);
    fpp.addFilter(new FXAAFilter());
    fpp.addFilter(bloom);
    
    mViewPort.addProcessor(fpp);
  }
  
  public void setupGUI()
  {
    Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE); 
    Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE); 
    
    NiftyJmeDisplay display = new NiftyJmeDisplay(
      mAssetManager,
      mInputManager,
      Main.instance.getAudioRenderer(),
      mGuiViewPort);
    
    mGuiViewPort.addProcessor(display);
    
    mNifty = display.getNifty();
    mNifty.fromXml("Interface/GUI.xml", "hud", Main.instance);
    
    mInputManager.setCursorVisible(true);
  }
}