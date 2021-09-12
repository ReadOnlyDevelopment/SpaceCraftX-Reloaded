package fr.militario.spacex.sides;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;

public class SpaceXOBJLoader implements ICustomModelLoader {

	public static final SpaceXOBJLoader instance = new SpaceXOBJLoader();

	static {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(instance);
	}
	private IResourceManager manager;
	private final Set<String> enabledDomains = new HashSet<>();
	private final Map<ResourceLocation, IModel> cache = new HashMap<>();

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return (this.enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj"));
	}

	public void addDomain(String domain) {
		this.enabledDomains.add(domain.toLowerCase());
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws IOException {
		IModel model = null;
		if (this.cache.containsKey(modelLocation)) {
			model = this.cache.get(modelLocation);
		} else {
			try {
				String prefix = modelLocation.getResourcePath().contains("models/") ? "" : "models/";
				ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), prefix + modelLocation.getResourcePath());
				IResource resource = this.manager.getResource(file);
				if (resource != null) {
					OBJModel.Parser parser = new OBJModel.Parser(resource, this.manager);
					try {
						model = parser.parse().process(ImmutableMap.of("flip-v", "true"));
					} finally {
						resource.getInputStream().close();
						this.cache.put(modelLocation, model);
					}
				}
			} catch (IOException e) {
				throw e;
			}
		}
		if (model == null)
			return ModelLoaderRegistry.getMissingModel();
		return model;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		this.manager = resourceManager;
		this.cache.clear();
	}
}
