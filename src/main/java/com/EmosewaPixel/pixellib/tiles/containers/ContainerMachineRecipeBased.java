package com.EmosewaPixel.pixellib.tiles.containers;

import com.EmosewaPixel.pixellib.tiles.AbstractTERecipeBased;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerMachineRecipeBased<T extends AbstractTERecipeBased> extends Container {
    protected T te;
    protected IItemHandler itemHandler;

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    public ContainerMachineRecipeBased(IInventory playerInventory, T te) {
        this.te = te;

        te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> itemHandler = handler);

        addMachineSlots();

        addPlayerSlots(playerInventory);
    }

    protected void addMachineSlots() {
        for (int i = 0; i < te.getRecipeList().getMaxInputs(); i++)
            this.addSlot(new SlotItemHandler(itemHandler, i, te.getRecipeList().getMaxInputs() == 1 ? 56 : 38 + i * 18, 35));

        for (int i = 0; i < te.getRecipeList().getMaxOutputs(); i++)
            this.addSlot(new SlotItemHandler(itemHandler, te.getSlotCount() - i - 1, 116, te.getRecipeList().getMaxOutputs() == 1 ? 35 : 48 - i * 22));
    }

    private void addPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (int k = 0; k < 9; ++k)
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            itemstack = stack1.copy();

            if (index < te.getSlotCount()) {
                if (!this.mergeItemStack(stack1, te.getSlotCount(), this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!this.mergeItemStack(stack1, 0, te.getSlotCount(), false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }

        return itemstack;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener : listeners) {
            listener.sendWindowProperty(this, 0, te.getProgress());
            listener.sendWindowProperty(this, 1, te.getCurrentRecipe().getTime());
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:
                te.setProgress(data);
                break;
            case 1:
                te.getCurrentRecipe().setTime(data);
                break;
        }
    }
}
