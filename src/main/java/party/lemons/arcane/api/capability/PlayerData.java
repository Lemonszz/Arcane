package party.lemons.arcane.api.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import party.lemons.arcane.api.spell.Spell;
import party.lemons.arcane.api.spell.SpellRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 11/12/2017.
 */
public interface PlayerData
{
	@CapabilityInject(PlayerData.class)
	Capability<PlayerData> CAPABILITY = null;

	List<Spell> getUnlockedSpells();
	Spell[] getSelectedSpells();
	void setSelectedSpells(Spell[] spells);
	void setSelectedSpell(Spell spell, int slot);
	boolean canUnlockSpell(Spell spell);
	void unlockSpell(Spell spell);
	void unlockSpell(String registry);
	boolean hasSpellUnlocked(Spell spell);
	int getSelectedIndex();
	void setSelectedIndex(int ind);
	boolean isHoldingCast();
	void setHoldingCast(boolean casting);
	long getHoldTime();
	void setHoldTime(long time);
	void setUnlockedSpells(List<Spell> spells);
	int getStoredLevels();
	void setStoredLevels(int level);

	void setLookEntity(Entity theEntity);
	Entity getLookEntity();

	float getMaxMana();
	void setMaxMana(float maxMana);
	float getMana();
	void setMana(float mana);

	class Impl implements PlayerData
	{
		private List<Spell> spells = new ArrayList<>();
		private Spell[] selectedSpells = new Spell[6];
		private int selectedIndex = 0;
		private long holdTime = -1;
		private boolean casting = false;
		private Entity lookEntity = null;
		private float maxMana = 100;
		private float mana = maxMana;
		private int storedLevels = 0;

		@Override
		public List<Spell> getUnlockedSpells()
		{
			return spells;
		}

		@Override
		public Spell[] getSelectedSpells()
		{
			return selectedSpells;
		}

		@Override
		public void setSelectedSpells(Spell[] spells)
		{
			this.selectedSpells = spells;
		}

		@Override
		public void setSelectedSpell(Spell spell, int slot)
		{
			selectedSpells[slot] = spell;
		}

		@Override
		public boolean canUnlockSpell(Spell spell)
		{
			Spell[] pars = spell.getParents();
			if(pars != null)
			{
				for(Spell par : pars)
					if(!hasSpellUnlocked(par))
						return false;
			}
			return true;
		}

		@Override
		public void unlockSpell(Spell spell)
		{
			if(!spells.contains(spell))
			{
				spells.add(spell);
			}
		}

		@Override
		public void unlockSpell(String registry)
		{
			Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(registry));
			if(spell != null)
				unlockSpell(spell);
		}

		@Override
		public boolean hasSpellUnlocked(Spell spell)
		{
			return spells.contains(spell);
		}

		@Override
		public int getSelectedIndex()
		{
			return selectedIndex;
		}

		@Override
		public void setSelectedIndex(int ind)
		{
			this.selectedIndex = ind;
		}

		@Override
		public boolean isHoldingCast()
		{
			return casting;
		}

		@Override
		public void setHoldingCast(boolean casting)
		{
			this.casting = casting;
		}

		@Override
		public long getHoldTime()
		{
			return holdTime;
		}

		@Override
		public void setHoldTime(long time)
		{
			this.holdTime = time;
		}

		@Override
		public void setUnlockedSpells(List<Spell> spells)
		{
			this.spells = spells;
		}

		@Override
		public int getStoredLevels()
		{
			return storedLevels;
		}

		@Override
		public void setStoredLevels(int level)
		{
			this.storedLevels = level;
		}

		@Override
		public void setLookEntity(Entity theEntity)
		{
			this.lookEntity = theEntity;
		}

		@Override
		public Entity getLookEntity()
		{
			return lookEntity;
		}

		@Override
		public float getMaxMana()
		{
			return maxMana;
		}

		@Override
		public void setMaxMana(float maxMana)
		{
			this.maxMana = maxMana;
		}

		@Override
		public float getMana()
		{
			return mana;
		}

		@Override
		public void setMana(float mana)
		{
			this.mana = mana;
		}

	}

	class Storage implements Capability.IStorage<PlayerData>
	{
		@Nullable
		@Override
		public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side)
		{
			NBTTagCompound tags = new NBTTagCompound();

			NBTTagInt selectedIndex = new NBTTagInt(instance.getSelectedIndex());

			NBTTagList li = new NBTTagList();
			for(Spell sp : instance.getUnlockedSpells())
			{
				NBTTagString str = new NBTTagString(sp.getRegistryName().toString());
				li.appendTag(str);
			}

			NBTTagList sel = new NBTTagList();
			for(int i = 0; i < instance.getSelectedSpells().length; i++)
			{
				if(instance.getSelectedSpells()[i] != null)
					sel.appendTag(new NBTTagString(instance.getSelectedSpells()[i].getRegistryName().toString()));
				else
					sel.appendTag(new NBTTagString("none"));
			}

			tags.setTag("unlocked", li);
			tags.setTag("selectedind", selectedIndex);
			tags.setTag("selected", sel);
			tags.setFloat("maxmana", instance.getMaxMana());
			tags.setFloat("mana", instance.getMana());
			tags.setInteger("levels", instance.getStoredLevels());
			return tags;
		}

		@Override
		public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbt)
		{
			NBTTagCompound tags = (NBTTagCompound) nbt;
			NBTTagList li = tags.getTagList("unlocked", Constants.NBT.TAG_STRING);
			for(int i = 0; i < li.tagCount(); i++)
			{
				String sp = li.getStringTagAt(i);
				instance.unlockSpell(sp);
			}

			NBTTagList sel = tags.getTagList("selected", Constants.NBT.TAG_STRING);
			for(int i = 0; i < sel.tagCount(); i++)
			{
				String sp = sel.getStringTagAt(i);
				if(!sp.equalsIgnoreCase("none"))
				{
					Spell spell = SpellRegistry.SPELL_REGISTRY.getValue(new ResourceLocation(sp));
					instance.setSelectedSpell(spell, i);
				}
			}

			instance.setSelectedIndex(tags.getInteger("selectedind"));

			instance.setMaxMana(tags.getFloat("maxmana"));
			instance.setMana(tags.getFloat("mana"));
			instance.setStoredLevels(tags.getInteger("levels"));
		}
	}

	class Provider implements ICapabilitySerializable<NBTBase>
	{
		private PlayerData instance = CAPABILITY.getDefaultInstance();

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == CAPABILITY;
		}

		@Nullable
		@Override
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
		{
			return capability == CAPABILITY ? CAPABILITY.cast(instance) : null;
		}

		@Override
		public NBTBase serializeNBT()
		{
			return CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
		}
	}
}
