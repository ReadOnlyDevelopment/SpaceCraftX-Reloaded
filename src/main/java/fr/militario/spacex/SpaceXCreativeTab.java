package fr.militario.spacex;

import fr.militario.spacex.items.F9Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class SpaceXCreativeTab extends CreativeTabs {
	public SpaceXCreativeTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(F9Items.Falcon9RocketItem);
	}
}
