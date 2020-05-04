package com.scientianova.versatile.items

import com.scientianova.versatile.materialsystem.lists.Forms
import com.scientianova.versatile.materialsystem.lists.MaterialItems
import com.scientianova.versatile.materialsystem.lists.Materials
import net.minecraft.item.Item
import net.minecraftforge.event.RegistryEvent

//This class is used for generating Items for all the possible Material-Object Type combinations
fun registerItems(e: RegistryEvent.Register<Item>) {
    MaterialItems.additionSuppliers.cellSet()
            .forEach { MaterialItems.addItem(it.rowKey!!, it.columnKey!!, it.value!!()) }
    Materials.all.forEach { mat ->
        Forms.all.forEach { form -> form[mat]?.item?.let { e.registry.register(it) } }
    }
}