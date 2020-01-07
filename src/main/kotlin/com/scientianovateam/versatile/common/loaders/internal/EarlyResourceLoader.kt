package com.scientianovateam.versatile.common.loaders.internal

import com.google.gson.JsonObject
import net.minecraft.resources.FallbackResourceManager
import net.minecraft.resources.IResource
import net.minecraft.resources.ResourcePackType
import net.minecraft.util.JSONUtils
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo
import net.minecraftforge.fml.packs.ModFileResourcePack
import java.io.InputStreamReader

val earlyResources by lazy { EarlyGameResourceLoader() }

class EarlyGameResourceLoader {
    private val packs = ModList.get().modFiles.filter { it.modLoader != "minecraft" }.map { mf: ModFileInfo -> ModFileResourcePack(mf.file) }

    init {
        packs.forEach { CustomResourceManager.addResourcePack(it) }
    }

    fun resources(path: String): List<List<IResource>> {
        return CustomResourceManager.getAllResourceLocations(path) { it.endsWith(".json") }.map(CustomResourceManager::getAllResources)
    }

    fun loadJsons(path: String): List<JsonObject> = resources(path).flatMap { set ->
        set.map {
            it.use { resource ->
                JSONUtils.fromJson(InputStreamReader(resource.inputStream)).apply {
                    addProperty("name", resource.location.path.let { path -> path.substring(path.length + 1, path.length - jsonExtensionLength) })
                    addProperty("namespace", resource.location.namespace)
                }
            }
        }
    }


    companion object {
        private const val jsonExtensionLength = ".json".length
    }

    object CustomResourceManager : FallbackResourceManager(ResourcePackType.SERVER_DATA, "") {
        override fun getResourceNamespaces(): MutableSet<String> = resourcePacks.flatMap {
            it.getResourceNamespaces(ResourcePackType.SERVER_DATA)
        }.toMutableSet()
    }
}