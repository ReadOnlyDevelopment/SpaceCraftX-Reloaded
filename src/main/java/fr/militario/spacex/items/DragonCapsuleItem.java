package fr.militario.spacex.items;

import java.util.List;

import javax.annotation.Nullable;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.entity.EntityDragonCapsule;
import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.item.IHoldableItem;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.items.ISortableItem;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryItem;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DragonCapsuleItem extends Item implements IHoldableItem, ISortableItem {

	public static PropertyEnum<ConditionState> condition = PropertyEnum.create("condition", ConditionState.class);

	public DragonCapsuleItem(String assetName) {
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
		this.setUnlocalizedName(assetName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		IRocketType.EnumRocketType type = IRocketType.EnumRocketType.values()[par1ItemStack.getItemDamage()];
		if (!type.getTooltip().isEmpty())
			tooltip.add(type.getTooltip());
		if (type.getPreFueled())
			tooltip.add(EnumColor.RED + "§o" + GCCoreUtil.translate("gui.creative_only.desc"));
		if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("RocketFuel")) {
			EntityDragonCapsule rocket = new EntityDragonCapsule(FMLClientHandler.instance().getWorldClient(), 0.0D, 0.0D, 0.0D, IRocketType.EnumRocketType.values()[par1ItemStack.getItemDamage()], 0.0F);
			tooltip.add(GCCoreUtil.translate("gui.message.fuel.name") + ": " + par1ItemStack.getTagCompound().getInteger("RocketFuel") + " / " + rocket.fuelTank.getCapacity());
		}
		if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("RocketOxygen")) {
			EntityDragonCapsule rocket = new EntityDragonCapsule(FMLClientHandler.instance().getWorldClient(), 0.0D, 0.0D, 0.0D, IRocketType.EnumRocketType.values()[par1ItemStack.getItemDamage()], 0.0F);
			tooltip.add(SpaceXUtils.translate("gui.message.oxygen.name") + ": " + par1ItemStack.getTagCompound().getInteger("RocketOxygen") + " / " + rocket.oxygenTank.getCapacity());
		}
	}

	@Override
	public EnumSortCategoryItem getCategory(int meta) {
		return EnumSortCategoryItem.ROCKET;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return SpaceX.SpaceXtab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if ((tab == SpaceX.SpaceXtab) || (tab == CreativeTabs.SEARCH)) {
			list.add(new ItemStack(this, 1, 0));
			list.add(new ItemStack(this, 1, 4));
		}
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem(hand);
		boolean padFound = false;
		TileEntity tile = null;
		if (worldIn.isRemote && (playerIn instanceof EntityPlayerSP)) {
			ClientProxyCore.playerClientHandler.onBuild(8, (EntityPlayerSP) playerIn);
			return EnumActionResult.PASS;
		}
		float centerX = -1.0F;
		float centerY = -1.0F;
		float centerZ = -1.0F;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				BlockPos pos1 = pos.add(i, 0, j);
				IBlockState state = worldIn.getBlockState(pos1);
				Block id = state.getBlock();
				int meta = id.getMetaFromState(state);
				if ((id == GCBlocks.landingPadFull) && (meta == 0)) {
					padFound = true;
					tile = worldIn.getTileEntity(pos.add(i, 0, j));
					centerX = (pos.getX() + i) + 0.5F;
					centerY = pos.getY() + 0.4F;
					centerZ = (pos.getZ() + j) + 0.5F;
					break;
				}
			}
			if (padFound)
				break;
		}
		if (padFound) {
			if (!placeRocketOnPad(stack, worldIn, tile, centerX, centerY, centerZ))
				return EnumActionResult.FAIL;
			if (!playerIn.capabilities.isCreativeMode)
				stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}

	private boolean placeRocketOnPad(ItemStack stack, World worldIn, TileEntity tile, float centerX, float centerY, float centerZ) {
		if (tile instanceof TileEntityLandingPad) {
			if (((TileEntityLandingPad) tile).getDockedEntity() != null)
				return false;
		} else
			return false;
		EntityDragonCapsule spaceship = new EntityDragonCapsule(worldIn, centerX, centerY, centerZ, IRocketType.EnumRocketType.values()[stack.getItemDamage()], 0.0F);
		spaceship.setPosition(spaceship.posX, spaceship.posY + spaceship.getOnPadYOffset(), spaceship.posZ);
		worldIn.spawnEntity(spaceship);
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("RocketFuel"))
			spaceship.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, stack.getTagCompound().getInteger("RocketFuel")), true);
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("RocketOxygen"))
			spaceship.oxygenTank.fill(new FluidStack(GCFluids.fluidOxygenGas, stack.getTagCompound().getInteger("RocketOxygen")), true);
		if (spaceship.rocketType.getPreFueled()) {
			spaceship.fuelTank.fill(new FluidStack(GCFluids.fluidFuel, spaceship.getMaxFuel()), true);
			spaceship.oxygenTank.fill(new FluidStack(GCFluids.fluidOxygenGas, spaceship.getMaxOxygen()), true);
		}
		return true;
	}

	@Override
	public boolean shouldCrouch(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean shouldHoldLeftHandUp(EntityPlayer player) {
		return true;
	}

	@Override
	public boolean shouldHoldRightHandUp(EntityPlayer player) {
		return true;
	}
}
