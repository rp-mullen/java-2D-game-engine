package renderer;

import java.util.*;

import components.SpriteRenderer;
import engine.GameObject;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	private static Shader currentShader;
	
	public Renderer() {
		this.batches = new ArrayList<>();
	}
	
	public void add(GameObject go) {
		SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
		if (spr != null) {
			add(spr);
		}
	}
	
	private void add(SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.zIndex) {
				Texture tex = sprite.getTexture();
				if (tex == null && (batch.hasTexture(tex) || batch.hasTextureRoom())) { 
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}
		
		if (!added ) {
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.zIndex);
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}
	
	public static Shader getBoundShader() {
		return currentShader;
	}
	
	public static void bindShader(Shader shader) {
		currentShader = shader;
	}
	
	public void render() {
		currentShader.use();
		for (RenderBatch batch : batches) {
			batch.render();
		}
	}
	
	
}
