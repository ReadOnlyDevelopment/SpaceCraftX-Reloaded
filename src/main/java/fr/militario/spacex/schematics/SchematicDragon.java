package fr.militario.spacex.schematics;

import fr.militario.spacex.F9ConfigManager;
import fr.militario.spacex.containers.schematics.ContainerSchematicDragon;
import fr.militario.spacex.gui.schematics.GuiSchematicDragon;
import fr.militario.spacex.items.F9Items;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SchematicDragon implements ISchematicPage {
	public int getPageID() {
		return F9ConfigManager.idSchematicDragon;
	}

	public int getGuiID() {
		return 9 + "spacex".hashCode();
	}

	public ItemStack getRequiredItem() {
		return new ItemStack(F9Items.schematic, 1, 1);
	}

	@SideOnly(Side.CLIENT)
	public GuiScreen getResultScreen(EntityPlayer player, BlockPos pos) {
		return (GuiScreen) new GuiSchematicDragon(player.inventory, pos);
	}

	public Container getResultContainer(EntityPlayer player, BlockPos pos) {
		return (Container) new ContainerSchematicDragon(player.inventory, pos);
	}

	public int compareTo(ISchematicPage o) {
		if (getPageID() > o.getPageID())
			return 1;
		return -1;
	}
}
