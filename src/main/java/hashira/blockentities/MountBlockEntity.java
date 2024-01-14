package hashira.blockentities;

import java.util.ArrayList;
import hashira.Hashira;
import hashira.ImplementedInventory;
import hashira.Utility;
import hashira.blocks.Mount;
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

        // The logic of the mount emitting fuel to neighboring furnaces is ordered in
        // terms of cheap to expensive checks.
        // 1. Check if the panel is mounted
        // 2. Check if it has any adjacent furnaces
        // 3. Check if it has a clear view of the sky (can be expensive).
        // 4. Give furnace fuel

        if (!blockEntity.mounted) {
            world.setBlockState(pos, blockState.with(Mount.ACTIVE, false));
            world.markDirty(pos);
            return;
        }

        ArrayList<FurnaceBlockEntity> neighboringFurnaces = Utility.getPotentialNeighboringFurnaces(world, pos);
        boolean clearView = blockEntity.hasClearViewOfSky();
        if (neighboringFurnaces.size() != 0 && clearView) {
            // Charge the furnace and skip checks.
            blockEntity.giveFuelToNearbyFurnaces(neighboringFurnaces);
        }

        world.setBlockState(pos, blockState.with(Mount.ACTIVE, clearView));
        world.markDirty(pos);
    }

    public boolean hasClearViewOfSky() {
        BlockPos blockPos = this.getPos();
        World world = this.getWorld();

        if (!world.isDay()) {
            return false;
        }

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

    private void giveFuelToNearbyFurnaces(ArrayList<FurnaceBlockEntity> neighboringFurnaces) {
        ItemStack fuelStack = new ItemStack(Hashira.SOLAR_FUEL);

        for (FurnaceBlockEntity furnace : neighboringFurnaces) {
            furnace.setStack(1, fuelStack);
        }

        System.out.println("Mounted panel fuelled " + neighboringFurnaces.size() + " furnaces");
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
