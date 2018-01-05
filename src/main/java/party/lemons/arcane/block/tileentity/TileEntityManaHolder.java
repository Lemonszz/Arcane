package party.lemons.arcane.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import party.lemons.arcane.energy.EnergyMana;
import party.lemons.arcane.energy.EnergyManaStorage;
import party.lemons.arcane.energy.IManaHolder;

import javax.annotation.Nullable;

/**
 * Created by Sam on 3/01/2018.
 */
public class TileEntityManaHolder extends TileEntity implements IManaHolder
{
	private EnergyManaStorage energy;

	public TileEntityManaHolder(int maxMana)
	{
		energy = new EnergyManaStorage(maxMana);
	}

	public EnergyMana.IEnergyManaStorage getMana()
	{
		return energy;
	}

	@Override
	public boolean canInsertMana()
	{
		return true;
	}

	@Override
	public boolean canExtractMana()
	{
		return true;
	}

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
	{
		if(capability == EnergyMana.MANA_ENERGY)
			return true;

		return super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
	{
		if(capability == EnergyMana.MANA_ENERGY)
			return (T) energy;

		return super.getCapability(capability, facing);
	}


	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		//EnergyMana.MANA_ENERGY.readNBT( energy, null, compound.getTag("power"));
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
	//	compound.setTag("power",  EnergyMana.MANA_ENERGY.writeNBT(getMana(), null));

		return super.writeToNBT(compound);
	}
}
