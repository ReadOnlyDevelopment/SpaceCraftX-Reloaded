package fr.militario.spacex.sides;

import java.lang.reflect.Method;

import com.google.common.base.Preconditions;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.advancement.CustomTrigger;
import fr.militario.spacex.advancement.Triggers;
import fr.militario.spacex.asm.ASM;
import fr.militario.spacex.gui.GuiHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
	public void init(FMLInitializationEvent event) {
		try {
			Method method = ASM.findMethod(CriteriaTriggers.class, "register", "func_192118_a", new Class[] { ICriterionTrigger.class });
			method.setAccessible(true);
			for (CustomTrigger element : Triggers.TRIGGER_ARRAY) {
				System.out.println("REGISTERING C " + element.getId());
				method.invoke((Object) null, new Object[] { element });
			}
		} catch (SecurityException | IllegalAccessException | IllegalArgumentException | java.lang.reflect.InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public void preInit(FMLPreInitializationEvent event) {
	}

	public void registerGuiHandler() {
		Object instance = Preconditions.checkNotNull(SpaceX.instance);
		IGuiHandler handler = Preconditions.checkNotNull(new GuiHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
	}

	public void registerVariants() {
	}

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo) {
	}
}
