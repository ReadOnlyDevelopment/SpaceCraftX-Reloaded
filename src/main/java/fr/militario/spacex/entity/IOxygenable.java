package fr.militario.spacex.entity;

import net.minecraftforge.fluids.FluidStack;

public interface IOxygenable {
	int addOxygen(FluidStack paramFluidStack, boolean paramBoolean);

	FluidStack removeOxygen(int paramInt);
}
