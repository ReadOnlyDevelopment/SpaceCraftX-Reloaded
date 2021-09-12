package fr.militario.spacex.entity.fx;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.EntityFXLaunchParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleGreenLaunchSmoke extends EntityFXLaunchParticle {
	float smokeParticleScale;

	public ParticleGreenLaunchSmoke(World par1World, Vector3 position, Vector3 motion, float size, boolean launched) {
		super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		setSize(0.2F, 0.2F);
		this.motionX += motion.x;
		this.motionY += motion.y;
		this.motionZ += motion.z;
		this.particleAlpha = 1.0F;
		this.particleRed = 0.5F;
		this.particleGreen = 1.0F;
		this.particleBlue = 0.5F;
		this.particleScale *= 0.75F;
		this.particleScale *= size * 3.0F;
		this.smokeParticleScale = this.particleScale;
		if (launched) {
			this.particleMaxAge = (int) (this.particleMaxAge * size) + 10;
		} else {
			this.motionX += par1World.rand.nextDouble() / 2.0D - 0.25D;
			this.motionY += par1World.rand.nextDouble() / 20.0D;
			this.motionZ += par1World.rand.nextDouble() / 2.0D - 0.25D;
			this.particleMaxAge = 30 + this.particleMaxAge;
		}
		this.canCollide = true;
	}

	@Override
	public void renderParticle(BufferBuilder worldRenderer, Entity entity, float f0, float f1, float f2, float f3,
			float f4, float f5) {
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glDisable(2929);
		float var8 = (this.particleAge + f0) / this.particleMaxAge * 32.0F;
		if (var8 < 0.0F)
			var8 = 0.0F;
		if (var8 > 1.0F)
			var8 = 1.0F;
		this.particleScale = this.smokeParticleScale * var8;
		super.renderParticle(worldRenderer, entity, f0, f1, f2, f3, f4, f5);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.particleAge++ >= this.particleMaxAge)
			setExpired();
		setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.motionY -= 0.002D;
		move(this.motionX, this.motionY, this.motionZ);
	}
}
