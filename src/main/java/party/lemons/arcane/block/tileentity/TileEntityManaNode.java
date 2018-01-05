package party.lemons.arcane.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcane.energy.EnergyMana;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by Sam on 3/01/2018.
 */
public abstract class TileEntityManaNode extends TileEntityManaWorker
{
	private int maxChildren;
	private ArrayList<BlockPos> children = new ArrayList<>();
	private int currentIndex = 0;
	private int maxExtract;

	public TileEntityManaNode(int maxMana, int maxWorkTime, int maxChildren, int maxExtract)
	{
		super(maxMana, maxWorkTime);

		this.maxChildren = maxChildren;
		this.maxExtract = maxExtract;
	}

	@Override
	boolean canWork()
	{
		return children.size() > 0 && getMana().getEnergyStored() > 0;
	}

	@Override
	public void update()
	{
		for(int i = 0; i < children.size(); i++)
		{
			if(world.getChunkFromBlockCoords(children.get(i)).isLoaded() && !isValidLink(children.get(i)))
				removeLink(children.get(i));
		}

		super.update();
	}

	@Override
	void work()
	{
		if(currentIndex >= maxChildren || currentIndex >= children.size())
			currentIndex = 0;

		BlockPos position =	children.get(currentIndex);

		boolean loaded = world.getChunkFromBlockCoords(position).isLoaded();
		while(!loaded || !isValidLink(position))
		{
			if(loaded)
				removeLink(position);

			currentIndex++;
			if(currentIndex >= maxChildren || currentIndex >= children.size())
				currentIndex = 0;

			position = children.get(currentIndex);
			loaded = world.getChunkFromBlockCoords(position).isLoaded();
		}
		currentIndex++;


		EnergyMana.IEnergyManaStorage mana = world.getTileEntity(position).getCapability(EnergyMana.MANA_ENERGY, null);
		int extractAmount = this.getMana().extractEnergy(maxExtract, false);
		int remainder = extractAmount - mana.receiveEnergy(extractAmount, false);
		this.getMana().receiveEnergy(remainder, false);
	}

	@Override
	int getWorkCost()
	{
		return 0;
	}

	public boolean isValidLink(BlockPos pos)
	{
		return world.getTileEntity(pos) != null && world.getTileEntity(pos).hasCapability(EnergyMana.MANA_ENERGY, null);
	}

	public boolean hasLink(BlockPos pos)
	{
		return children.stream().anyMatch(p -> p.equals(pos));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.currentIndex = compound.getInteger("ind");
		NBTTagList child = (NBTTagList) compound.getTag("children");
		for(int i = 0 ; i < child.tagCount(); i++)
		{
			children.add(NBTUtil.getPosFromTag((NBTTagCompound) child.get(i)));
		}

		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("ind", currentIndex);
		NBTTagList child = new NBTTagList();
		for(BlockPos pos : children)
		{
			child.appendTag(NBTUtil.createPosTag(pos));
		}
		compound.setTag("children", child);

		return super.writeToNBT(compound);
	}

	public boolean hasMaxLinks()
	{
		return children.size() == maxChildren;
	}

	public ArrayList<BlockPos> getChildren()
	{
		return children;
	}

	public void addLink(BlockPos pos)
	{
		children.add(pos);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox()
	{
		if(children.size() > 0)
			return INFINITE_EXTENT_AABB;

		return super.getRenderBoundingBox();
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, -1, writeToNBT(new NBTTagCompound()));
		return packet;
	}

	public void clearChildren()
	{
		children.clear();
		this.markDirty();
	}

	public void removeLink(BlockPos pos)
	{
		children.remove(pos);
	}
}
