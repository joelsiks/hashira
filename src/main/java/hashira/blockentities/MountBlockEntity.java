package hashira.blockentities;

import hashira.Hashira;
import hashira.ImplementedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MountBlockEntity extends BlockEntity implements ImplementedInventory {

    private final DefaultedList<ItemStack> mountedPanelItem = DefaultedList.ofSize(1, ItemStack.EMPTY);

    private static String MOUNTED_NBT_KEY = "mounted";
    public boolean mounted = false;

    // Used to keep track of the first block that obstructs the view of the sky. If
    // this is not null, then at least one block is blocking the view of the sky.
    private BlockPos clearViewBlockingBlock = null;

    public MountBlockEntity(BlockPos pos, BlockState state) {
        super(Hashira.MOUNT_ENTITY, pos, state);
    }

    public static void serverTick(World world, BlockPos pos, BlockState blockState, MountBlockEntity blockEntity) {
        if (!blockEntity.hasWorld()) {
            return;
        }

        blockEntity.provideFuelToFurnaceIfConditionsMet();
    }

    public void provideFuelToFurnaceIfConditionsMet() {
        if (mounted && hasClearViewOfSky()) {
            giveFuelToNearbyFurnaces();
        }
    }

    public boolean hasClearViewOfSky() {
        BlockPos blockPos = this.getPos();
        World world = this.getWorld();

        // Check if the block is already at the highest build limit
        if (world.getTopY() > world.getHeight()) {
            return true;
        }

        if (clearViewBlockingBlock != null && !world.getBlockState(clearViewBlockingBlock).isAir()) {
            return false;
        }

        // Perform ray tracing upwards to check for obstructions
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
        while (mutablePos.getY() < world.getHeight()) {
            BlockState state = world.getBlockState(mutablePos);

            if (!state.isAir()) {
                // If a non-air block is found, there's an obstruction.
                clearViewBlockingBlock = mutablePos.toImmutable();
                return false;
            }

            mutablePos.move(0, 1, 0); // Move the position one block upwards
        }

        return true;
    }

    private void giveFuelToNearbyFurnaces() {
        BlockPos[] neighborOffsets = {
                pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()
        };

        for (BlockPos offset : neighborOffsets) {
            BlockEntity blockEntity = world.getBlockEntity(offset);
            if (blockEntity == null) {
                continue;
            }

            if (blockEntity instanceof FurnaceBlockEntity) {
                System.out.println("Mount next to furnace!");
                // FurnaceBlockEntity furnace = (FurnaceBlockEntity) blockEntity;
                FurnaceBlockEntity furnace = (FurnaceBlockEntity) blockEntity;
            }
        }
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
