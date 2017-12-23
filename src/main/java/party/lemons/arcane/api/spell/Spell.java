package party.lemons.arcane.api.spell;

import jline.internal.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;
import party.lemons.arcane.api.capability.PlayerData;

/**
 * Created by Sam on 11/12/2017.
 */
public class Spell extends IForgeRegistryEntry.Impl<Spell> implements Comparable<Spell>
{
	private String unlocName = ":(";
	private SpellPage page;

	private ItemStack drawStack = ItemStack.EMPTY;
	private IconType iconType = IconType.TEXTURE;
	private TextureAtlasSprite atlasSprite;
	private Spell[] parents = {};
	private float castMana = 0;
	private int unlockCost = 1;

	public Spell(SpellPage page)
	{
		this.page = page;
	}

	/**
	 * Called when the player presses the spell keybind
	 * @param spell
	 * @param player
	 */
	public void castSpellPress(Spell spell, EntityPlayer player)
	{

	}

	/**
	 * Called every tick player is holding spell keybind
	 * holdTime is the world time when the key was first pressed
	 * to get the total hold ticks:
	 * player.world.getTotalWorldTime() - holdTime;
	 * @param spell
	 * @param player
	 * @param holdTime
	 */
	public void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{
	}

	/**
	 * Called when the player releaced the spell keybind
	 * holdTime is the world time when the key was first pressed
	 * to get the total hold ticks:
	 * player.world.getTotalWorldTime() - holdTime;
	 * @param spell
	 * @param player
	 * @param holdTime
	 */
	public void castSpellHoldRelease(Spell spell, EntityPlayer player, long holdTime)
	{
	}

	/**
	 * The cost of the spell in the spellbook
	 * @return unlock cost
	 */
	public int getUnlockCost()
	{
		return unlockCost;
	}

	/**
	 * Builder method for setting spell unlock cost
	 * defaults to 1
	 * @param cost
	 * @return spell
	 */
	public Spell setUnlockCost(int cost)
	{
		this.unlockCost = cost;
		return this;
	}

	/**
	 * Gets the spell's spell page
	 * @return spell page
	 */
	public SpellPage getSpellPage()
	{
		return page;
	}

	/**
	 * Required builder method to set a spell's unlocalized name
	 * @param name [recommended "modid.spellname"
	 * @return spell
	 */
	public Spell setUnlocalizedName(String name)
	{
		this.unlocName = "spell." + name + ".name";

		return this;
	}

	/**
	 * Builder method to set the spells display ItemStack
	 * If this method is not specified, the spell will use a texture
	 * @param stack
	 * @return
	 */
	public Spell setDrawStack(ItemStack stack)
	{
		drawStack = stack;
		this.iconType = IconType.ITEMSTACK;

		return this;
	}

	/**
	 * Helper method to use the mana required for the spell.
	 * Creative players can always cast a spell
	 * @param player
	 */
	public void useMana(EntityPlayer player)
	{
		if(player.isCreative())
			return;

		PlayerData da = player.getCapability(PlayerData.CAPABILITY, null);
		float newMana = Math.max(0, da.getMana() - getCastMana());
		da.setMana(newMana);
	}

	/**
	 * Gets the mana cost for the spell
	 * @return mana cost
	 */
	public float getCastMana()
	{
		return castMana;
	}

	/**
	 * Builder method to set the cast mana
	 * defauts to 0
	 * @param castMana
	 * @return spell
	 */
	public Spell setCastMana(float castMana)
	{
		this.castMana = castMana;
		return this;
	}

	/**
	 * Gets the spell's unlocalized name
	 * @return unlocalized name
	 */
	public String getUnlocalizedName()
	{
		return unlocName;
	}

	/**
	 * Client-sided helper method to get the localised name for the spell
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public String getLocalizedName()
	{
		return I18n.format(unlocName);
	}

	/**
	 * Gets the unlocalized description for the spell
	 * this gives the format "spell.[registry_name].desc"
	 * @return unlocalized description
	 */
	public String getUnlocalizedDescription()
	{
		return this.unlocName.replace("name", "desc");
	}

	/**
	 * Gets the parents of the spell, all parents must be unlocked to unlock the spell
	 * @return Spell[]
	 */
	@Nullable
	public Spell[] getParents()
	{
		return parents;
	}

	/**
	 * Builder method to set the parents of the spell
	 * @param parents
	 * @return spell
	 */
	public Spell setParents(Spell... parents)
	{
		this.parents = parents;
		return this;
	}

	/**
	 * Checks if a given player  has the mana to cast the spell
	 * Creative players can alwyas cast the spell
	 * @param player
	 * @return can cast
	 */
	public boolean canCast(EntityPlayer player)
	{
		if(player.isCreative())
			return  true;

		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		return data.getMana() >= castMana;
	}

	/**
	 * Gets the type of icon for the spell, by default this is a texture, but can be set to an ItemStack
	 * @return
	 */
	public IconType getIconType()
	{
		return iconType;
	}

	/**
	 * 	If getIconType() == TEXTURE, this will be called when drawing the icon
	 * @return resource location of texture
	 */
	public ResourceLocation getIconTexture()
	{
		return new ResourceLocation(this.getRegistryName().getResourceDomain(), "spell/" + this.getRegistryName().getResourcePath());
	}


	/**
	 * If getItemType() == ITEMSTACK, this will be called when drawing the icon
	 * @return ItemStack
	 */
	public ItemStack getIconStack()
	{
		return drawStack;
	}

	/**
	 * Sets the spell's AtlasSprite
	 * @param spr
	 */
	public void setAtlasSprite(TextureAtlasSprite spr)
	{
		atlasSprite = spr;
	}

	/**
	 * Gets the spell's AtlasSprite
	 */
	public TextureAtlasSprite getAtlasSprite()
	{
		return atlasSprite;
	}

	/**
	 * Compares this spell to another, used for sorting
	 * @param spell
	 * @return -1 if bigger, 0 if same, 1 if smaller
	 */
	@Override
	public int compareTo(Spell spell)
	{
		return getRegistryName().toString().compareTo(spell.getRegistryName().toString());
	}

	/***
	 * Used for raycasting spells, default 20 block range
	 * @return reach
	 */
	public float getSpellReach()
	{
		return 20;
	}
	enum IconType
	{
		TEXTURE,
		ITEMSTACK
	}
}
