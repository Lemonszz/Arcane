package party.lemons.arcane.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import party.lemons.arcane.block.tileentity.TileEntityManaNodeSplitter;
import party.lemons.arcane.block.tileentity.TileEntityManaNodeStandard;
import party.lemons.arcane.block.tileentity.TileEntitySmelter;

import java.util.ArrayList;

/**
 * Created by Sam on 3/01/2018.
 */
@Mod.EventBusSubscriber
public class ArcaneBlocks
{
	public static ArrayList<Block> blocks = new ArrayList<>();

	public static Block MANA_NODE, MANA_SPLITTER, SMELTER;

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		MANA_NODE = new BlockManaNodeStandard();
		MANA_SPLITTER = new BlockManaSplitter();
		SMELTER = new BlockSmelter();

		for(Block bl : blocks)
		{
			event.getRegistry().register(bl);
		}

		GameRegistry.registerTileEntity(TileEntityManaNodeStandard.class, "ar_mananode");
		GameRegistry.registerTileEntity(TileEntityManaNodeSplitter.class, "ar_manasplitter");
		GameRegistry.registerTileEntity(TileEntitySmelter.class, "ar_manasmelter");
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		for(Block bl : blocks)
		{
			ItemBlock ib = new ItemBlock(bl);
			ib.setRegistryName(bl.getRegistryName());

			event.getRegistry().register(ib);
		}
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for(Block bl : blocks)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(bl), 0, new ModelResourceLocation(bl.getRegistryName(), "inventory"));
		}
	}
}
