package fr.militario.spacex.gui.schematics;

import org.lwjgl.opengl.GL11;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.containers.schematics.ContainerSchematicDragonTrunk;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicResultPage;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiPositionedContainer;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class GuiSchematicDragonTrunk extends GuiPositionedContainer implements ISchematicResultPage {
  private static final ResourceLocation rocketBenchTexture = new ResourceLocation(F9Constants.MODID, "textures/gui/dragon_trunkbench.png");
  
  private int pageIndex;
  
  public GuiSchematicDragonTrunk(InventoryPlayer par1InventoryPlayer, BlockPos pos) {
    super((Container)new ContainerSchematicDragonTrunk(par1InventoryPlayer, pos), pos);
    this.ySize = 221;
  }
  
  public void initGui() {
    super.initGui();
    this.buttonList.clear();
    this.buttonList.add(new GuiButton(0, this.width / 2 - 130, this.height / 2 - 110, 40, 20, GCCoreUtil.translate("gui.button.back.name")));
    this.buttonList.add(new GuiButton(1, this.width / 2 - 130, this.height / 2 - 110 + 25, 40, 20, GCCoreUtil.translate("gui.button.next.name")));
  }
  
  protected void actionPerformed(GuiButton par1GuiButton) {
    if (par1GuiButton.enabled)
      switch (par1GuiButton.id) {
        case 0:
          SchematicRegistry.flipToLastPage((GuiScreen)this, this.pageIndex);
          break;
        case 1:
          SchematicRegistry.flipToNextPage((GuiScreen)this, this.pageIndex);
          break;
      }  
  }
  
  protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    this.fontRenderer.drawString(SpaceXUtils.translate("schematic.dragon_trunk.name"), 7, 7, 4210752);
    this.fontRenderer.drawString(GCCoreUtil.translate("container.inventory"), 8, 127, 4210752);
  }
  
  protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(rocketBenchTexture);
    int var5 = (this.width - this.xSize) / 2;
    int var6 = (this.height - this.ySize) / 2;
    drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
  }
  
  public void setPageIndex(int index) {
    this.pageIndex = index;
  }
}
