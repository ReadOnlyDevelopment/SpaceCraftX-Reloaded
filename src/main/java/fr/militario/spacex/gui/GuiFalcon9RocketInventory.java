package fr.militario.spacex.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.containers.ContainerFalcon9Rocket;
import fr.militario.spacex.entity.EntityFalcon9Rocket;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFalcon9RocketInventory extends GuiContainerGC {

	private static ResourceLocation rocketTextures = new ResourceLocation(F9Constants.MODID, "textures/gui/falcon9.png");
	private final IInventory upperChestInventory;
	private final EntityFalcon9Rocket rocket;

	public GuiFalcon9RocketInventory(InventoryPlayer inventoryPlayer, EntityFalcon9Rocket rocket) {
		super(new ContainerFalcon9Rocket(inventoryPlayer, rocket));
		this.upperChestInventory = rocket;
		this.rocket = rocket;
		this.allowUserInput = false;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		this.mc.getTextureManager().bindTexture(rocketTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = ((this.height - this.ySize) / 2) - 31;
		drawTexturedModalRect(var5, var6, 0, 0, 176, this.ySize);
		if ((this.mc.player != null) && (this.rocket != null) && (this.rocket instanceof EntitySpaceshipBase)) {
			int fuelLevel = this.rocket.getScaledFuelLevel(38);
			drawTexturedModalRect(((this.width - this.xSize) / 2) + 35, ((((this.height - this.ySize) / 2) - 18) + 45) - fuelLevel, 176, 38 - fuelLevel, 34, fuelLevel);
		}
		if ((this.mc.player != null) && (this.rocket != null) && (this.rocket instanceof EntitySpaceshipBase)) {
			int oxygenLevel = this.rocket.getScaledOxygenLevel(38);
			drawTexturedModalRect(((this.width - this.xSize) / 2) + 107, ((((this.height - this.ySize) / 2) - 18) + 45) - oxygenLevel, 210, 38 - oxygenLevel, 34, oxygenLevel);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.fuel.name"), 25, -23, 4210752);
		this.fontRenderer.drawString(SpaceXUtils.translate("gui.message.oxygen.name"), 90, -23, 4210752);
		this.fontRenderer.drawString(GCCoreUtil.translate(this.upperChestInventory.getName()), 8, 39, 4210752);
		if ((this.mc.player != null) && (this.rocket != null) && (this.rocket instanceof EntitySpaceshipBase)) {
			double fuel_percentage = this.rocket.getScaledFuelLevel(100);
			String fuel_color = (fuel_percentage > 80.0D) ? EnumColor.BRIGHT_GREEN.getCode() : ((fuel_percentage > 40.0D) ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode());
			String fuel_str = fuel_percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
			this.fontRenderer.drawString(fuel_color + fuel_str, 117 - (fuel_str.length() / 2) - 85, 30, 4210752);
			double oxygen_percentage = this.rocket.getScaledOxygenLevel(100);
			String oxygen_color = (oxygen_percentage > 80.0D) ? EnumColor.BRIGHT_GREEN.getCode() : ((oxygen_percentage > 40.0D) ? EnumColor.ORANGE.getCode() : EnumColor.RED.getCode());
			String oxygen_str = oxygen_percentage + "% " + GCCoreUtil.translate("gui.message.full.name");
			this.fontRenderer.drawString(oxygen_color + oxygen_str, 117 - (oxygen_str.length() / 2) - 10, 30, 4210752);
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		List<String> fuelTankDesc = new ArrayList<>();
		fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.0"));
		fuelTankDesc.add(GCCoreUtil.translate("gui.fuel_tank.desc.1"));
		List<String> oxygenTankDesc = new ArrayList<>();
		oxygenTankDesc.add(SpaceXUtils.translate("gui.oxygen_tank.desc.0"));
		oxygenTankDesc.add(SpaceXUtils.translate("gui.oxygen_tank.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion(((this.width - this.xSize) / 2) + 34, ((this.height - this.ySize) / 2) - 12, 36, 40, fuelTankDesc, this.width, this.height, this));
		this.infoRegions.add(new GuiElementInfoRegion(((this.width - this.xSize) / 2) + 106, ((this.height - this.ySize) / 2) - 12, 36, 40, oxygenTankDesc, this.width, this.height, this));
	}
}
