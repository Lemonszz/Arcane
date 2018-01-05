package party.lemons.arcane.item;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.arcane.config.ArcaneConstants;

/**
 * Created by Sam on 12/12/2017.
 */
@Mod.EventBusSubscriber(modid = ArcaneConstants.MODID)
public class ModItems
{
	public static Item spellTome, manaPotion, linker;

	@SubscribeEvent
	public static void initItem(RegistryEvent.Register<Item> event)
	{
		spellTome = new ItemSpellTome().setRegistryName("spell_tome").setUnlocalizedName(ArcaneConstants.MODID + ".spell_tome");
		manaPotion = new ItemManaPotion().setRegistryName("mana_potion").setUnlocalizedName(ArcaneConstants.MODID + ".mana_potion");
		linker = new ItemLinker().setRegistryName("linker").setUnlocalizedName(ArcaneConstants.MODID + ".linker");
		event.getRegistry().registerAll(spellTome, manaPotion, linker);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void initModels(ModelRegistryEvent event)
	{
		ModelLoader.setCustomModelResourceLocation(spellTome, 0, new ModelResourceLocation(ArcaneConstants.MODID + ":spell_tome", "inventory"));
		ModelLoader.setCustomModelResourceLocation(manaPotion, 0, new ModelResourceLocation(ArcaneConstants.MODID + ":mana_potion", "inventory"));

		ModelResourceLocation linker_off = new ModelResourceLocation(linker.getRegistryName(), "inventory");
		ModelResourceLocation linker_on = new ModelResourceLocation(linker.getRegistryName() + "_on", "inventory");
		ModelBakery.registerItemVariants(linker, linker_off, linker_on);

		ModelLoader.setCustomMeshDefinition(linker, stack ->
		{
			NBTTagCompound tags = stack.getTagCompound();
			if(tags != null)
			{
				if(tags.hasKey("pos"))
				{
					return linker_on;
				}
			}
			return linker_off;
		});
	}
}
