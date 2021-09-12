package fr.militario.spacex.renders;

import fr.militario.spacex.entity.EntityFalcon9Rocket;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFalcon9 extends Render<EntityFalcon9Rocket> {
	private ResourceLocation spaceshipTexture;
	private ResourceLocation spaceship_usedTexture;
	protected ModelBase modelSpaceship;

	public RenderFalcon9(RenderManager manager, ModelBase spaceshipModel, String textureDomain, String texture) {
		this(manager, new ResourceLocation(textureDomain, "textures/model/" + texture + ".png"), new ResourceLocation(textureDomain, "textures/model/" + texture + "_used.png"));
		this.modelSpaceship = spaceshipModel;
	}

	private RenderFalcon9(RenderManager manager, ResourceLocation texture, ResourceLocation texture_used) {
		super(manager);
		this.spaceshipTexture = texture;
		this.spaceship_usedTexture = texture_used;
		this.shadowSize = 0.9F;
	}

	@Override
	public void doRender(EntityFalcon9Rocket entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		float pitch = entity.prevRotationPitch + ((entity.rotationPitch - entity.prevRotationPitch) * partialTicks) + 180;
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
		bindEntityTexture(entity);
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		this.modelSpaceship.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFalcon9Rocket entity) {
		if (entity.F9launchPhase > EntityFalcon9Rocket.EnumF9LaunchPhase.ENTRYBURN.ordinal())
			return this.spaceship_usedTexture;
		return this.spaceshipTexture;
	}

	@Override
	public boolean shouldRender(EntityFalcon9Rocket rocket, ICamera camera, double camX, double camY, double camZ) {
		AxisAlignedBB axisalignedbb = rocket.getEntityBoundingBox().expand(1.5D, 1.0D, 1.5D);
		return (rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb));
	}
}
