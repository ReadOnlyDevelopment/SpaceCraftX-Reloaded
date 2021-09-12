package fr.militario.spacex.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.containers.ContainerOxygenUnloader;
import fr.militario.spacex.tiles.TileEntityOxygenUnloader;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiOxygenUnloader extends GuiContainerGC {

	private static final ResourceLocation oxygenLoaderTexture = new ResourceLocation(F9Constants.MODID, "textures/gui/oxygen_loader.png");
	private final TileEntityOxygenUnloader oxygenLoader;
	private GuiButton buttonLoadOxygen;
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(((this.width - this.xSize) / 2) + 112, ((this.height - this.ySize) / 2) + 65, 56, 9, null, this.width, this.height, this);

	public GuiOxygenUnloader(InventoryPlayer par1InventoryPlayer, TileEntityOxygenUnloader par2TileEntityAirDistributor) {
		super(new ContainerOxygenUnloader(par1InventoryPlayer, par2TileEntityAirDistributor));
		this.oxygenLoader = par2TileEntityAirDistributor;
		this.ySize = 180;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		switch (par1GuiButton.id) {
		case 0:
			GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(this.oxygenLoader.getWorld()), new Object[] { this.oxygenLoader.getPos(), Integer.valueOf(0) }));
			break;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(oxygenLoaderTexture);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		drawTexturedModalRect(var5, var6 + 5, 0, 0, this.xSize, 181);
		int fuelLevel = this.oxygenLoader.getScaledFuelLevel(38);
		drawTexturedModalRect(((this.width - this.xSize) / 2) + 7, (((this.height - this.ySize) / 2) + 17 + 54) - fuelLevel, 176, 38 - fuelLevel, 16, fuelLevel);
		List<String> electricityDesc = new ArrayList<>();
		electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
		EnergyDisplayHelper.getEnergyDisplayTooltip(this.oxygenLoader.getEnergyStoredGC(), this.oxygenLoader.getMaxEnergyStoredGC(), electricityDesc);
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		if (this.oxygenLoader.getEnergyStoredGC() > 0.0F) {
			drawTexturedModalRect(var5 + 99, var6 + 65, 192, 7, 11, 10);
		}
		drawTexturedModalRect(var5 + 113, var6 + 66, 192, 0, Math.min(this.oxygenLoader.getScaledElecticalLevel(54), 54), 7);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(this.oxygenLoader.getName(), 60, 10, 4210752);
		this.buttonLoadOxygen.enabled = (this.oxygenLoader.disableCooldown == 0);
		this.buttonLoadOxygen.displayString = !this.oxygenLoader.getDisabled(0) ? SpaceXUtils.translate("gui.button.stopunloading.name") : SpaceXUtils.translate("gui.button.unloadoxygen.name");
		this.fontRenderer.drawString(GCCoreUtil.translate("gui.message.status.name") + ": " + getStatus(), 28, 22, 4210752);
		this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, (this.ySize - 118) + 2 + 11, 4210752);
	}

	private String getStatus() {
		if (this.oxygenLoader.getDisabled(0))
			return EnumColor.DARK_RED + SpaceXUtils.translate("gui.status.nooxygenunload.name");
		return this.oxygenLoader.getGUIstatus();
	}

	@Override
	public void initGui() {
		super.initGui();
		List<String> fuelTankDesc = new ArrayList<>();
		fuelTankDesc.add(SpaceXUtils.translate("gui.oxygen_tank.desc.2"));
		fuelTankDesc.add(SpaceXUtils.translate("gui.oxygen_tank.desc.3"));
		this.infoRegions.add(new GuiElementInfoRegion(((this.width - this.xSize) / 2) + 7, ((this.height - this.ySize) / 2) + 33, 16, 38, fuelTankDesc, this.width, this.height, this));
		List<String> batterySlotDesc = new ArrayList<>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion(((this.width - this.xSize) / 2) + 50, ((this.height - this.ySize) / 2) + 54, 18, 18, batterySlotDesc, this.width, this.height, this));
		List<String> electricityDesc = new ArrayList<>();
		electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
		electricityDesc.add(EnumColor.YELLOW + GCCoreUtil.translate("gui.energy_storage.desc.1") + (int) Math.floor(this.oxygenLoader.getEnergyStoredGC()) + " / " + (int) Math.floor(this.oxygenLoader.getMaxEnergyStoredGC()));
		this.electricInfoRegion.tooltipStrings = electricityDesc;
		this.electricInfoRegion.xPosition = ((this.width - this.xSize) / 2) + 112;
		this.electricInfoRegion.yPosition = ((this.height - this.ySize) / 2) + 65;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		this.buttonList.add(this.buttonLoadOxygen = new GuiButton(0, (this.width / 2) + 2, (this.height / 2) - 49, 76, 20, SpaceXUtils.translate("gui.button.unloadoxygen.name")));
	}
}
