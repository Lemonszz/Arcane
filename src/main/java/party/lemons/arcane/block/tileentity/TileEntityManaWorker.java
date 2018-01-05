package party.lemons.arcane.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

/**
 * Created by Sam on 3/01/2018.
 */
public abstract class TileEntityManaWorker extends TileEntityManaHolder implements ITickable
{
	private int workTime;
	private int maxWorkTime;

	public TileEntityManaWorker(int maxMana, int maxWorkTime)
	{
		super(maxMana);

		this.maxWorkTime = maxWorkTime;
		this.workTime = maxWorkTime;
	}

	@Override
	public void update()
	{
		if(canWork())
		{
			workTime--;
			if(workTime <= 0)
			{
				getMana().extractEnergy(getWorkCost(), false);
				work();

				workTime = maxWorkTime;
			}

			markDirty();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.workTime = compound.getInteger("worktime");

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("worktime", workTime);

		return super.writeToNBT(compound);
	}

	abstract boolean canWork();
	abstract void work();
	abstract int getWorkCost();
}
