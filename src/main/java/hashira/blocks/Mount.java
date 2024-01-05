package hashira.blocks;

import hashira.blockentities.MountBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mount extends Block implements BlockEntityProvider {

    public static final BooleanProperty MOUNTED = BooleanProperty.of("mounted");

    public Mount(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(MOUNTED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MOUNTED);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {

        if (world.isClient)
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        Inventory inventoryEntity = (Inventory) blockEntity;
        MountBlockEntity mountBlockEntity = (MountBlockEntity) blockEntity;

        if (!player.getStackInHand(hand).isEmpty()) {
            if (inventoryEntity.getStack(0).isEmpty()) {
                // If the item is not a panel, ignore it.
                System.out.println("Player holding: " + player.getStackInHand(hand).getTranslationKey());
                if (!player.getStackInHand(hand).getTranslationKey().equals("item.hashira.panel"))
                    return ActionResult.PASS;

                inventoryEntity.setStack(0, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);
                mountBlockEntity.mounted = true;
                world.setBlockState(pos, state.with(MOUNTED, true));
            }
        } else {
            if (!inventoryEntity.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(inventoryEntity.getStack(0));
                inventoryEntity.removeStack(0);
                mountBlockEntity.mounted = false;
                world.setBlockState(pos, state.with(MOUNTED, false));
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MountBlockEntity(pos, state);
    }

}
