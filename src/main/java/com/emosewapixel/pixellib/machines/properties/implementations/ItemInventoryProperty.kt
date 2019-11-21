package com.emosewapixel.pixellib.machines.properties.implementations

import com.emosewapixel.pixellib.machines.capabilities.ImprovedItemStackHandler
import com.emosewapixel.pixellib.machines.properties.IValueProperty
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandlerModifiable

open class ItemInventoryProperty(override val value: ImprovedItemStackHandler) : IValueProperty<IItemHandlerModifiable> {
    override fun createDefault() = ItemInventoryProperty(ImprovedItemStackHandler(value.slots, value.noOutputSlots, value.noInputSlots))

    protected val capability = LazyOptional.of(::value)

    override fun <T> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> =
            if (cap === CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                capability.cast()
            else LazyOptional.empty()
}