package fr.militario.spacex.advancement;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import fr.militario.spacex.F9Constants;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CustomTrigger implements ICriterionTrigger<CustomTrigger.Instance> {

	public static class Instance extends AbstractCriterionInstance {

		public Instance(ResourceLocation resourceLocation) {
			super(resourceLocation);
		}

		public boolean test() {
			return true;
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<CustomTrigger.Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements advancements) {
			this.playerAdvancements = advancements;
		}

		public void add(ICriterionTrigger.Listener<CustomTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void remove(ICriterionTrigger.Listener<CustomTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(EntityPlayerMP player) {
			List<ICriterionTrigger.Listener<CustomTrigger.Instance>> list = null;
			for (ICriterionTrigger.Listener<CustomTrigger.Instance> listener : this.listeners)
				if (listener.getCriterionInstance().test()) {
					if (list == null)
						list = Lists.newArrayList();
					list.add(listener);
				}
			if (list != null)
				for (ICriterionTrigger.Listener<CustomTrigger.Instance> listener1 : list)
					listener1.grantCriterion(this.playerAdvancements);
		}
	}

	private final ResourceLocation ID;
	private final Map<PlayerAdvancements, Listeners> listeners = Maps.newHashMap();

	public CustomTrigger(String id) {
		this.ID = new ResourceLocation(F9Constants.MODID, id);
	}

	@Override
	public void addListener(PlayerAdvancements advancements, ICriterionTrigger.Listener<Instance> listener) {
		Listeners trigger = this.listeners.get(advancements);
		if (trigger == null) {
			trigger = new Listeners(advancements);
			this.listeners.put(advancements, trigger);
		}
		trigger.add(listener);
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new Instance(getId());
	}

	@Override
	public ResourceLocation getId() {
		return this.ID;
	}

	@Override
	public void removeAllListeners(PlayerAdvancements advancements) {
		this.listeners.remove(advancements);
	}

	@Override
	public void removeListener(PlayerAdvancements advancements, ICriterionTrigger.Listener<Instance> listener) {
		Listeners trigger = this.listeners.get(advancements);
		if (trigger != null) {
			trigger.remove(listener);
			if (trigger.isEmpty())
				this.listeners.remove(advancements);
		}
	}

	public void trigger(EntityPlayer player) {
		if (!player.world.isRemote)
			trigger((EntityPlayerMP) player);
	}

	public void trigger(EntityPlayerMP player) {
		Listeners trigger = this.listeners.get(player.getAdvancements());
		if (trigger != null)
			trigger.trigger(player);
	}
}
