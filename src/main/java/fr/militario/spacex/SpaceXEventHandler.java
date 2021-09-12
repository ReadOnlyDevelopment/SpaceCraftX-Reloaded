package fr.militario.spacex;

import fr.militario.spacex.advancement.Triggers;
import fr.militario.spacex.items.F9Items;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@EventBusSubscriber(modid = "spacex")
public class SpaceXEventHandler {

	@SubscribeEvent
	public void craftEvent(PlayerEvent.ItemCraftedEvent event) {
		if ((event.crafting.getItem() == F9Items.Falcon9RocketItem) || (event.crafting.getItem() == F9Items.F9SecondStageRocketItem) || (event.crafting.getItem() == F9Items.DragonTrunkItem) || (event.crafting.getItem() == F9Items.DragonCapsuleItem)) {
			if (event.crafting.getItem() == F9Items.Falcon9RocketItem) {
				Triggers.RECYCLING.trigger(event.player);
			}
			ItemStack wrench = new ItemStack(GCItems.wrench);
			for (int i = 0; i < 9; i++) {
				if ((event.craftMatrix.getStackInSlot(i) != null) && (event.craftMatrix.getStackInSlot(i).getItem() == GCItems.wrench)) {
					wrench = event.craftMatrix.getStackInSlot(i);
				}
			}
			wrench.setItemDamage(wrench.getItemDamage() + 40);
			if (wrench.getItemDamage() < wrench.getMaxDamage()) {
				event.player.inventory.addItemStackToInventory(wrench);
			}
		}
	}

	@SubscribeEvent
	public void dimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
		if ((event.player.getEntityWorld()).provider instanceof WorldProviderMoon) {
			Triggers.ONESMALLSTEPFORMAN.trigger(event.player);
		}
		if ((event.player.getEntityWorld()).provider instanceof WorldProviderMars) {
			Triggers.ONEGIANTLEAPFORMANKIND.trigger(event.player);
		}
	}
}
