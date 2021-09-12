package fr.militario.spacex.entity.fx;

import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.militario.spacex.SpaceX;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleRocketFlame extends Particle {

	private float smokeParticleScale;
	private boolean smoke;
	private EntityLivingBase ridingEntity;

	public ParticleRocketFlame(World par1World, Vector3 position, Vector3 motion, EntityLivingBase ridingEntity,
			boolean smoke) {
		super(par1World, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += motion.x;
		this.motionY += motion.y;
		this.motionZ += motion.z;
		this.particleRed = 1.0F;
		this.particleGreen = 0.5882353F + this.rand.nextFloat() / 3.0F;
		this.particleBlue = 0.33333334F;
		this.particleScale *= 2.0F;
		this.particleScale *= 2.0F;
		this.smokeParticleScale = this.particleScale;
		this.particleMaxAge = (int) (this.particleMaxAge * 2.0F);
		this.canCollide = true;
		this.ridingEntity = ridingEntity;
		this.smoke = smoke;
	}

	@Override
	public void renderParticle(BufferBuilder worldRenderer, Entity entity, float f0, float f1, float f2, float f3,
			float f4, float f5) {
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
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.particleAge++ >= this.particleMaxAge) {
			if (this.smoke)
				SpaceX.proxy.spawnParticle("rocketFlameSmoke",
						new Vector3(this.posX, this.posY + this.rand.nextDouble() * 0.5D, this.posZ),
						new Vector3(this.motionX, this.motionY, this.motionZ), new Object[0]);
			setExpired();
		}
		setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.motionY += 0.001D;
		move(this.motionX, this.motionY, this.motionZ);
		if (this.posY == this.prevPosY) {
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}
		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;
		List<?> var3 = this.world.getEntitiesWithinAABB(Entity.class, getBoundingBox().expand(1.0D, 0.5D, 1.0D));
		if (var3 != null)
			for (int var4 = 0; var4 < var3.size(); var4++) {
				Entity var5 = (Entity) var3.get(var4);
				if (var5 instanceof EntityLivingBase)
					if (!var5.isDead && !var5.isBurning() && !var5.equals(this.ridingEntity)) {
						var5.setFire(3);
						GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(
								PacketSimple.EnumSimplePacket.S_SET_ENTITY_FIRE, GCCoreUtil.getDimensionID(var5.world),
								new Object[] { Integer.valueOf(var5.getEntityId()) }));
					}
			}
	}

	@Override
	public int getBrightnessForRender(float par1) {
		return 15728880;
	}
}
