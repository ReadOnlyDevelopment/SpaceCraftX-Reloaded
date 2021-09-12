package fr.militario.spacex.items;

import net.minecraft.util.IStringSerializable;

/**
 * @author ROMVoid95
 */
public enum ConditionState implements IStringSerializable {
	NORMAL, DAMAGED;

	@Override
	public String getName() {
		return toString().toLowerCase();
	}
}
