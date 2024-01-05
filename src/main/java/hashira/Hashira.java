package hashira;

import hashira.items.Cell;
import hashira.items.Module;
import hashira.items.Panel;

import hashira.blocks.Mount;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Hashira implements ModInitializer {

    public static final String HASHIRA_GROUP_NAME = "Hashira";

    public static final Cell HASHIRA_CELL = new Cell(new FabricItemSettings());
    public static final Module HASHIRA_MODULE = new Module(new FabricItemSettings());
    public static final Panel HASHIRA_PANEL = new Panel(new FabricItemSettings());

    public static final Mount HASHIRA_MOUNT = new Mount(FabricBlockSettings.create());
    public static final BlockItem HASHIRA_MOUNT_ITEM = new BlockItem(HASHIRA_MOUNT, new Item.Settings());

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(HASHIRA_PANEL))
            .displayName(Text.translatable(HASHIRA_GROUP_NAME))
            .entries((context, entries) -> {
                entries.add(HASHIRA_CELL);
                entries.add(HASHIRA_MODULE);
                entries.add(HASHIRA_PANEL);
                entries.add(HASHIRA_MOUNT_ITEM);
            })
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("hashira", "cell"), HASHIRA_CELL);
        Registry.register(Registries.ITEM, new Identifier("hashira", "module"), HASHIRA_MODULE);
        Registry.register(Registries.ITEM, new Identifier("hashira", "panel"), HASHIRA_PANEL);

        Registry.register(Registries.BLOCK, new Identifier("hashira", "mount"), HASHIRA_MOUNT);
        Registry.register(Registries.ITEM, new Identifier("hashira", "mount"), HASHIRA_MOUNT_ITEM);

        Registry.register(Registries.ITEM_GROUP, new Identifier("hashira", "hashira_group"), ITEM_GROUP);
    }

}
