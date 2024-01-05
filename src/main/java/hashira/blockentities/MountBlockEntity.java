package hashira.blockentities;

import hashira.Hashira;
import hashira.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class MountBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> mountedPanelItem = DefaultedList.ofSize(1, ItemStack.EMPTY);

    private static String MOUNTED_NBT_KEY = "mounted";
    public boolean mounted = false;

    public MountBlockEntity(BlockPos pos, BlockState state) {
        super(Hashira.MOUNT_ENTITY, pos, state);
    }

    // Deserialize the BlockEntity
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        mounted = nbt.getBoolean(MOUNTED_NBT_KEY);
        Inventories.readNbt(nbt, mountedPanelItem);
    }

    // Serialize the BlockEntity
    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putBoolean(MOUNTED_NBT_KEY, mounted);
        Inventories.writeNbt(nbt, mountedPanelItem);
        super.writeNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return mountedPanelItem;
    }

}
