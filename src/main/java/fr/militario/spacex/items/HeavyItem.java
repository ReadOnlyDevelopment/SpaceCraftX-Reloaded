package fr.militario.spacex.items;

import fr.militario.spacex.SpaceX;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class HeavyItem extends Item implements IHoldableItem {
	private boolean arms;

	public HeavyItem(boolean arms) {
		setMaxStackSize(1);
		this.arms = arms;
	}

	public CreativeTabs getCreativeTab() {
		return (CreativeTabs) SpaceX.SpaceXtab;
	}

	public boolean shouldHoldLeftHandUp(EntityPlayer player) {
		return this.arms;
	}

	public boolean shouldHoldRightHandUp(EntityPlayer player) {
		return this.arms;
	}

	public boolean shouldCrouch(EntityPlayer player) {
		return true;
	}
}
