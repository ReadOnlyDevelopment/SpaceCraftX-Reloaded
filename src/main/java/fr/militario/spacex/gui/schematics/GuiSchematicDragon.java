package fr.militario.spacex.gui.schematics;

import org.lwjgl.opengl.GL11;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.containers.schematics.ContainerSchematicDragon;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiPositionedContainer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiSchematicDragon extends GuiPositionedContainer implements ISchematicResultPage {
	private static final ResourceLocation rocketBenchTexture = new ResourceLocation(F9Constants.MODID, "textures/gui/dragonbench.png");
	private int pageIndex;

	public GuiSchematicDragon(InventoryPlayer par1InventoryPlayer, BlockPos pos) {
		super(new ContainerSchematicDragon(par1InventoryPlayer, pos), pos);
		this.ySize = 221;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled)
			switch (par1GuiButton.id) {
			case 0:
				SchematicRegistry.flipToLastPage(this, this.pageIndex);
				break;
			case 1:
				SchematicRegistry.flipToNextPage(this, this.pageIndex);
				break;
			}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(rocketBenchTexture);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRenderer.drawString(SpaceXUtils.translate("schematic.dragon.name"), 7, 7, 4210752);
		this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, 127, 4210752);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, (this.width / 2) - 130, (this.height / 2) - 110, 40, 20, GCCoreUtil.translate("gui.button.back.name")));
		this.buttonList.add(new GuiButton(1, (this.width / 2) - 130, ((this.height / 2) - 110) + 25, 40, 20, GCCoreUtil.translate("gui.button.next.name")));
	}

	@Override
	public void setPageIndex(int index) {
		this.pageIndex = index;
	}
}
