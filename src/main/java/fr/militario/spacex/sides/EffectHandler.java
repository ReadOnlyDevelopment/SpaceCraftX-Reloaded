package fr.militario.spacex.sides;

import fr.militario.spacex.entity.fx.ParticleCondensation;
import fr.militario.spacex.entity.fx.ParticleGreenLaunchSmoke;
import fr.militario.spacex.entity.fx.ParticleRocketFlame;
import fr.militario.spacex.entity.fx.ParticleRocketFlameSmoke;
import fr.militario.spacex.entity.fx.ParticleVacuumFlame;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.client.fx.ParticleLaunchSmoke;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EffectHandler {

	public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... otherInfo) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if ((mc != null) && (mc.getRenderViewEntity() != null) && (mc.effectRenderer != null)) {
			Particle particle = null;
			if (particleID.equals("greenSmokeIdle")) {
				particle = new ParticleGreenLaunchSmoke(mc.world, position, motion, 2.5F, false);
			} else if (particleID.equals("vacuumFlameIdle")) {
				particle = new ParticleVacuumFlame(mc.world, position, motion, 2.5F, false);
			} else if (particleID.equals("dragonFlameIdle")) {
				particle = new ParticleVacuumFlame(mc.world, position, motion, 1.0F, false);
			} else if (particleID.equals("rocketFlame")) {
				particle = new ParticleRocketFlame(mc.world, position, motion, (EntityLivingBase) otherInfo[0], true);
			} else if (particleID.equals("rocketFlameSmoke")) {
				particle = new ParticleRocketFlameSmoke(mc.world, position, motion, 2.5F);
			} else if (particleID.equals("launchSmoke")) {
				particle = new ParticleLaunchSmoke(mc.world, position, motion, 5.5F, false);
			} else if (particleID.equals("condensation")) {
				particle = new ParticleCondensation(mc.world, position, motion, 1.0F);
			}
			if (particle != null) {
				mc.effectRenderer.addEffect(particle);
			}
		}
	}
}
