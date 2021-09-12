package fr.militario.spacex.items;

import java.util.List;

import javax.annotation.Nullable;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.entities.EntityHangingSchematic;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSchematic extends ItemHangingEntity implements ISchematicItem, ISortableItem {

	public static void registerSchematicItems() {
		SchematicRegistry.registerSchematicItem(new ItemStack(F9Items.schematic, 1, 0));
		SchematicRegistry.registerSchematicItem(new ItemStack(F9Items.schematic, 1, 1));
		SchematicRegistry.registerSchematicItem(new ItemStack(F9Items.schematic, 1, 2));
		SchematicRegistry.registerSchematicItem(new ItemStack(F9Items.schematic, 1, 3));
	}

	@SideOnly(Side.CLIENT)
	public static void registerTextures() {
		SchematicRegistry.registerTexture(new ResourceLocation(F9Constants.MODID, "textures/items/schematic_falcon9.png"));
		SchematicRegistry.registerTexture(new ResourceLocation(F9Constants.MODID, "textures/items/schematic_dragon.png"));
		SchematicRegistry.registerTexture(new ResourceLocation(F9Constants.MODID, "textures/items/schematic_secondstage.png"));
		SchematicRegistry.registerTexture(new ResourceLocation(F9Constants.MODID, "textures/items/schematic_dragon_trunk.png"));
	}

	public ItemSchematic(String assetName) {
		super(EntityHangingSchematic.class);
		setMaxDamage(0);
		setHasSubtypes(true);
		setMaxStackSize(1);
		setUnlocalizedName(assetName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		switch (par1ItemStack.getItemDamage()) {
		case 0:
			tooltip.add(SpaceXUtils.translate("schematic.falcon9.name"));
			break;
		case 1:
			tooltip.add(SpaceXUtils.translate("schematic.dragon.name"));
			break;
		case 2:
			tooltip.add(SpaceXUtils.translate("schematic.secondstage.name"));
			break;
		case 3:
			tooltip.add(SpaceXUtils.translate("schematic.dragon_trunk.name"));
			break;
		}
	}

	private EntityHangingSchematic createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide, int index) {
		return new EntityHangingSchematic(worldIn, pos, clickedSide, index);
	}

	@Override
	public EnumSortCategoryItem getCategory(int meta) {
		return EnumSortCategoryItem.SCHEMATIC;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return SpaceX.SpaceXtab;
	}

	protected int getIndex(int damage) {
		return damage;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if ((tab == SpaceX.SpaceXtab) || (tab == CreativeTabs.SEARCH))
			for (int i = 0; i < 4; i++)
				list.add(new ItemStack(this, 1, i));
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		BlockPos blockpos = pos.offset(facing);
		if ((facing != EnumFacing.DOWN) && (facing != EnumFacing.UP) && playerIn.canPlayerEdit(blockpos, facing, stack)) {
			EntityHangingSchematic entityhanging = createEntity(worldIn, blockpos, facing, getIndex(stack.getItemDamage()));
			if ((entityhanging != null) && entityhanging.onValidSurface()) {
				if (!worldIn.isRemote) {
					worldIn.spawnEntity(entityhanging);
					entityhanging.sendToClient(worldIn, blockpos);
				}
				stack.shrink(1);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}
