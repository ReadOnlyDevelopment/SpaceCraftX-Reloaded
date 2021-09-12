package fr.militario.spacex.blocks;

import fr.militario.spacex.SpaceX;
import fr.militario.spacex.SpaceXUtils;
import fr.militario.spacex.tiles.TileEntityOxygenUnloader;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties.MachineSidesModel;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOxygenUnloader extends BlockAdvancedTile implements IShiftDescription, ISortableBlock {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
	public static final PropertyEnum<MachineSidesModel> SIDES = MACHINESIDES_RENDERTYPE.asProperty;

	public BlockOxygenUnloader(String assetName) {
		super(Material.ROCK);
		setHardness(1.0F);
		setSoundType(SoundType.METAL);
		setUnlocalizedName(assetName);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, SIDES);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityOxygenUnloader();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
	}

	@Override
	public EnumSortCategoryBlock getCategory(int meta) {
		return EnumSortCategoryBlock.MACHINE;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return SpaceX.SpaceXtab;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public String getShiftDescription(int meta) {
		return SpaceXUtils.translate(getUnlocalizedName() + ".description");
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		WorldUtil.markAdjacentPadForUpdate(worldIn, pos);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		int angle = MathHelper.floor(((placer.rotationYaw * 4.0F) / 360.0F) + 0.5D) & 0x3;
		worldIn.setBlockState(pos, getStateFromMeta(EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex()), 3);
		WorldUtil.markAdjacentPadForUpdate(worldIn, pos);
	}

	@Override
	public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		entityPlayer.openGui(SpaceX.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean onSneakUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof IMachineSides) {
			((IMachineSides) tile).nextSideConfiguration(tile);
			return true;
		}
		return false;
	}

	@Override
	public boolean showDescription(int meta) {
		return true;
	}
}
