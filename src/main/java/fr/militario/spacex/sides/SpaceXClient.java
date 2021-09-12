package fr.militario.spacex.sides;

import java.util.List;

import com.google.common.collect.ImmutableList;

import fr.militario.spacex.F9Constants;
import fr.militario.spacex.blocks.F9Blocks;
import fr.militario.spacex.entity.EntityDragonCapsule;
import fr.militario.spacex.entity.EntityDragonTrunk;
import fr.militario.spacex.entity.EntityF9SecondStage;
import fr.militario.spacex.entity.EntityFalcon9Rocket;
import fr.militario.spacex.items.F9Items;
import fr.militario.spacex.items.ItemSchematic;
import fr.militario.spacex.models.ModelDragon;
import fr.militario.spacex.models.ModelDragonTrunk;
import fr.militario.spacex.models.ModelF9SecondStage;
import fr.militario.spacex.models.ModelFalcon9;
import fr.militario.spacex.models.items.ModelCombustionChamberItem;
import fr.militario.spacex.models.items.ModelDracoItem;
import fr.militario.spacex.models.items.ModelDragonItem;
import fr.militario.spacex.models.items.ModelDragonTrunkItem;
import fr.militario.spacex.models.items.ModelF9SecondStageItem;
import fr.militario.spacex.models.items.ModelFalcon9Item;
import fr.militario.spacex.models.items.ModelGridFinItem;
import fr.militario.spacex.models.items.ModelLegItem;
import fr.militario.spacex.models.items.ModelMerlin1DItem;
import fr.militario.spacex.models.items.ModelNozzleItem;
import fr.militario.spacex.models.items.ModelOxygenTankItem;
import fr.militario.spacex.models.items.ModelRP1TankItem;
import fr.militario.spacex.models.items.ModelSeatItem;
import fr.militario.spacex.models.items.ModelTurboPumpItem;
import fr.militario.spacex.renders.RenderDragon;
import fr.militario.spacex.renders.RenderDragonTrunk;
import fr.militario.spacex.renders.RenderF9SecondStage;
import fr.militario.spacex.renders.RenderFalcon9;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpaceXClient extends CommonProxy {

	public static void addVariant(String modID, String name, String... variants) {
		Item itemBlockVariants = Item.REGISTRY.getObject(new ResourceLocation(modID, name));
		ResourceLocation[] variants0 = new ResourceLocation[variants.length];
		for (int i = 0; i < variants.length; i++) {
			variants0[i] = new ResourceLocation(modID + ":" + variants[i]);
		}
		ModelBakery.registerItemVariants(itemBlockVariants, variants0);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		F9Items.registerRenders();
		F9Blocks.registerRenders();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) {
		replaceModelDefault(event, F9Items.Falcon9RocketItem.getUnlocalizedName().substring(5), F9Items.Falcon9RocketItem.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelFalcon9Item.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.Falcon9RocketItem_used.getUnlocalizedName().substring(5), F9Items.Falcon9RocketItem_used.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelFalcon9Item.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.DragonCapsuleItem.getUnlocalizedName().substring(5), F9Items.DragonCapsuleItem.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelDragonItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.DragonCapsuleItem_used.getUnlocalizedName().substring(5), F9Items.DragonCapsuleItem_used.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelDragonItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.DragonTrunkItem.getUnlocalizedName().substring(5), F9Items.DragonTrunkItem.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelDragonTrunkItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.DragonTrunkItem_used.getUnlocalizedName().substring(5), F9Items.DragonTrunkItem_used.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelDragonTrunkItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.F9SecondStageRocketItem.getUnlocalizedName().substring(5), F9Items.F9SecondStageRocketItem.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelF9SecondStageItem.class, TRSRTransformation.identity(),
				new String[0]);
		replaceModelDefault(event, F9Items.F9SecondStageRocketItem_used.getUnlocalizedName().substring(5), F9Items.F9SecondStageRocketItem_used.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Rocket"), ModelF9SecondStageItem.class, TRSRTransformation.identity(),
				new String[0]);
		replaceModelDefault(event, F9Items.Merlin1D.getUnlocalizedName().substring(5), F9Items.Merlin1D.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Merlin1D"), ModelMerlin1DItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.DracoEngine.getUnlocalizedName().substring(5), F9Items.DracoEngine.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Draco"), ModelDracoItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.GridFin.getUnlocalizedName().substring(5), F9Items.GridFin.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("GridFin"), ModelGridFinItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.Leg.getUnlocalizedName().substring(5), F9Items.Leg.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Leg"), ModelLegItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.seat.getUnlocalizedName().substring(5), F9Items.seat.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Seat"), ModelSeatItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.oxygenTank.getUnlocalizedName().substring(5), F9Items.oxygenTank.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("OxygenTank"), ModelOxygenTankItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.RP1Tank.getUnlocalizedName().substring(5), F9Items.RP1Tank.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("RP1Tank"), ModelRP1TankItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.combustionChamber.getUnlocalizedName().substring(5), F9Items.combustionChamber.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("CombustionChamber"), ModelCombustionChamberItem.class, TRSRTransformation.identity(),
				new String[0]);
		replaceModelDefault(event, F9Items.turboPump.getUnlocalizedName().substring(5), F9Items.turboPump.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("TurboPump"), ModelTurboPumpItem.class, TRSRTransformation.identity(), new String[0]);
		replaceModelDefault(event, F9Items.nozzle.getUnlocalizedName().substring(5), F9Items.nozzle.getUnlocalizedName().substring(5) + ".obj", ImmutableList.of("Nozzle"), ModelNozzleItem.class, TRSRTransformation.identity(), new String[0]);
	}

	@SubscribeEvent
	public void onTextureStitchedPre(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.Falcon9RocketItem.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.Falcon9RocketItem_used.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.DragonCapsuleItem.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.DragonCapsuleItem_used.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.DragonTrunkItem.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.DragonTrunkItem_used.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.F9SecondStageRocketItem.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.F9SecondStageRocketItem_used.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.Merlin1D.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.DracoEngine.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.oxygenTank.getUnlocalizedName().substring(5)));
		event.getMap().registerSprite(new ResourceLocation("spacex:model/" + F9Items.RP1Tank.getUnlocalizedName().substring(5)));
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		ItemSchematic.registerTextures();
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityFalcon9Rocket.class, manager -> new RenderFalcon9(manager, new ModelFalcon9(), "spacex", "falcon9"));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonCapsule.class, manager -> new RenderDragon(manager, new ModelDragon(), "spacex", "dragon"));
		RenderingRegistry.registerEntityRenderingHandler(EntityDragonTrunk.class, manager -> new RenderDragonTrunk(manager, new ModelDragonTrunk(), "spacex", "trunk"));
		RenderingRegistry.registerEntityRenderingHandler(EntityF9SecondStage.class, manager -> new RenderF9SecondStage(manager, new ModelF9SecondStage(), "spacex", "secondstage"));
		ModelLoaderRegistry.registerLoader(SpaceXOBJLoader.instance);
		SpaceXOBJLoader.instance.addDomain("spacex");
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void registerVariants() {
		addVariant("spacex", "schematic", new String[] { "schematic", "schematic_dragon", "schematic_secondstage", "schematic_dragon_trunk" });
		ModelResourceLocation F9ModelResLoc = new ModelResourceLocation("spacex:" + F9Items.Falcon9RocketItem.getUnlocalizedName().substring(5), "inventory");
		for (int i = 0; i < 5; i++) {
			ModelLoader.setCustomModelResourceLocation(F9Items.Falcon9RocketItem, i, F9ModelResLoc);
		}
		ModelLoader.setCustomModelResourceLocation(F9Items.Falcon9RocketItem_used, 0, new ModelResourceLocation("spacex:" + F9Items.Falcon9RocketItem_used.getUnlocalizedName().substring(5), "inventory"));
		ModelResourceLocation DragonModelResLoc = new ModelResourceLocation("spacex:" + F9Items.DragonCapsuleItem.getUnlocalizedName().substring(5), "inventory");
		for (int j = 0; j < 5; j++) {
			ModelLoader.setCustomModelResourceLocation(F9Items.DragonCapsuleItem, j, DragonModelResLoc);
		}
		ModelLoader.setCustomModelResourceLocation(F9Items.DragonCapsuleItem_used, 0, new ModelResourceLocation("spacex:" + F9Items.DragonCapsuleItem_used.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.DragonTrunkItem, 0, new ModelResourceLocation("spacex:" + F9Items.DragonTrunkItem.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.DragonTrunkItem_used, 0, new ModelResourceLocation("spacex:" + F9Items.DragonTrunkItem_used.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.F9SecondStageRocketItem, 0, new ModelResourceLocation("spacex:" + F9Items.F9SecondStageRocketItem.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.F9SecondStageRocketItem, 4, new ModelResourceLocation("spacex:" + F9Items.F9SecondStageRocketItem.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.F9SecondStageRocketItem_used, 0, new ModelResourceLocation("spacex:" + F9Items.F9SecondStageRocketItem_used.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.Merlin1D, 0, new ModelResourceLocation("spacex:" + F9Items.Merlin1D.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.DracoEngine, 0, new ModelResourceLocation("spacex:" + F9Items.DracoEngine.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.GridFin, 0, new ModelResourceLocation("spacex:" + F9Items.GridFin.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.Leg, 0, new ModelResourceLocation("spacex:" + F9Items.Leg.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.seat, 0, new ModelResourceLocation("spacex:" + F9Items.seat.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.oxygenTank, 0, new ModelResourceLocation("spacex:" + F9Items.oxygenTank.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.RP1Tank, 0, new ModelResourceLocation("spacex:" + F9Items.RP1Tank.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.combustionChamber, 0, new ModelResourceLocation("spacex:" + F9Items.combustionChamber.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.turboPump, 0, new ModelResourceLocation("spacex:" + F9Items.turboPump.getUnlocalizedName().substring(5), "inventory"));
		ModelLoader.setCustomModelResourceLocation(F9Items.nozzle, 0, new ModelResourceLocation("spacex:" + F9Items.nozzle.getUnlocalizedName().substring(5), "inventory"));
	}

	private void replaceModelDefault(ModelBakeEvent event, String resLoc, String objLoc, List<String> visibleGroups, Class<? extends ModelTransformWrapper> clazz, IModelState parentState, String... variants) {
		ClientUtil.replaceModel(F9Constants.MODID, event, resLoc, objLoc, visibleGroups, clazz, parentState, variants);
	}

	@Override
	public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo) {
		EffectHandler.spawnParticle(particleID, position, motion, otherInfo);
	}
}
