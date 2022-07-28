package editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import components.NonPickable;
import engine.GameObject;
import engine.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

public class PropertiesWindow {
	private GameObject activeGameObject = null;
	private PickingTexture pickingTexture;
	
	private float debounceTime = 0.2f;
	
	public PropertiesWindow(PickingTexture pickingTexture) {
		this.pickingTexture = pickingTexture;
	}
	
	public void update(float dt, Scene currentScene) {
		debounceTime -=dt;
		if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int gameObjectID = pickingTexture.readPixel(x,y);
            GameObject pickedObj = currentScene.getGameObject(gameObjectID-1);
            if (pickedObj != null && pickedObj.getComponent(NonPickable.class) == null) {
            	activeGameObject = pickedObj;
            } else if( pickedObj == null && !MouseListener.isDragging()) {
            	activeGameObject = null;
            }
            
            this.debounceTime = 0.2f;
		}
	}
	
	public void imgui() {
		if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imgui();
            ImGui.end();
        }

	}
	
	public GameObject getActiveGameObject() {
		return this.activeGameObject;
	}
}