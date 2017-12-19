package party.lemons.magicmod.api.spell;

import jline.internal.Nullable;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import party.lemons.magicmod.api.capability.PlayerData;

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

	public void castSpellPress(Spell spell, EntityPlayer player)
	{

	}

	public void castSpellHold(Spell spell, EntityPlayer player, long holdTime)
	{
	}

	public void castSpellHoldRelease(Spell spell, EntityPlayer player, long holdTime)
	{
	}

	public int getUnlockCost()
	{
		return unlockCost;
	}

	public Spell setUnlockCost(int cost)
	{
		this.unlockCost = cost;
		return this;
	}

	public SpellPage getSpellPage()
	{
		return page;
	}

	public Spell setUnlocalizedName(String name)
	{
		this.unlocName = "spell." + name + ".name";

		return this;
	}

	public Spell setDrawStack(ItemStack stack)
	{
		drawStack = stack;
		this.iconType = IconType.ITEMSTACK;

		return this;
	}

	public void useMana(EntityPlayer player)
	{
		if(player.isCreative())
			return;

		PlayerData da = player.getCapability(PlayerData.CAPABILITY, null);
		float newMana = Math.max(0, da.getMana() - getCastMana());
		da.setMana(newMana);
	}

	public float getCastMana()
	{
		return castMana;
	}

	public Spell setCastMana(float castMana)
	{
		this.castMana = castMana;
		return this;
	}

	public String getUnlocalizedName()
	{
		return unlocName;
	}

	public String getLocalizedName()
	{
		return I18n.translateToLocal(unlocName);
	}

	public String getUnlocalizedDescription()
	{
		return this.unlocName.replace("name", "desc");
	}

	@Nullable
	public Spell[] getParents()
	{
		return parents;
	}

	public Spell setParents(Spell... parents)
	{
		this.parents = parents;

		return this;
	}

	public boolean canCast(EntityPlayer player)
	{
		PlayerData data = player.getCapability(PlayerData.CAPABILITY, null);
		return data.getMana() >= castMana;
	}

	public IconType getIconType()
	{
		return iconType;
	}

	//If getIconType() == TEXTURE, this will be called when drawing the icon
	public ResourceLocation getIconTexture()
	{
		return new ResourceLocation(this.getRegistryName().getResourceDomain(), "spell/" + this.getRegistryName().getResourcePath());
	}

	//If getItemType() == ITEMSTACK, this will be called when drawing the icon
	public ItemStack getIconStack()
	{
		return drawStack;
	}

	public void setAtlasSprite(TextureAtlasSprite spr)
	{
		atlasSprite = spr;
	}

	public TextureAtlasSprite getAtlasSprite()
	{
		return atlasSprite;
	}

	@Override
	public int compareTo(Spell o)
	{
		return getRegistryName().toString().compareTo(o.getRegistryName().toString());
	}

	/*
			Used for raycasting
	 */
	public float getSpellReach()
	{
		return 20;
	}

	public enum IconType
	{
		TEXTURE,
		ITEMSTACK;
	}
}
