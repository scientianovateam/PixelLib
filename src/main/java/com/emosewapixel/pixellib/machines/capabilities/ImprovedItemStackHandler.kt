package com.emosewapixel.pixellib.machines.capabilities

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraftforge.items.ItemStackHandler

//Improved Item Stack Handlers are Item Stack Handlers that can blacklist slots for insertion and extraction
open class ImprovedItemStackHandler @JvmOverloads constructor(slots: Int, val noOutputSlots: Array<Int> = emptyArray(), val noInputSlots: Array<Int> = emptyArray()) : ItemStackHandler(slots) {
    constructor(slots: Int, noOutput: IntRange, noInput: IntRange) : this(slots, noOutput.toList().toTypedArray(), noInput.toList().toTypedArray())

    constructor(inputCount: Int, outputCount: Int) : this(inputCount + outputCount, 0 until inputCount, inputCount until inputCount + outputCount)

    constructor(stacks: NonNullList<ItemStack>, noOutputSlots: Array<Int> = emptyArray(), noInputSlots: Array<Int> = emptyArray()) : this(stacks.size, noOutputSlots, noInputSlots) {
        super.stacks = stacks
    }

    val invStacks: NonNullList<ItemStack>
        get() = super.stacks

    val inputStacks get() = super.stacks.filterIndexed { index, _ -> index !in noInputSlots }

    val outputStacks get() = super.stacks.filterIndexed { index, _ -> index !in noOutputSlots }

    override fun isItemValid(slot: Int, itemStack: ItemStack) = slot !in noInputSlots

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = if (slot in noOutputSlots) ItemStack.EMPTY else super.extractItem(slot, amount, simulate)

    fun extraItemFromContainer(slot: Int, amount: Int, simulate: Boolean) = super.extractItem(slot, amount, simulate)

    operator fun get(slot: Int) = stacks[slot]
}