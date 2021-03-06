package exnihilocreatio.registries.registries;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import exnihilocreatio.api.registries.IBarrelLiquidBlacklistRegistry;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.registries.registries.prefab.BaseRegistryMap;
import net.minecraftforge.fluids.Fluid;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BarrelLiquidBlacklistRegistry extends BaseRegistryMap<Integer, List<String>> implements IBarrelLiquidBlacklistRegistry {
    public BarrelLiquidBlacklistRegistry() {
        super(new GsonBuilder()
                        .setPrettyPrinting()
                        .create(),
                new com.google.gson.reflect.TypeToken<Map<Integer, List<String>>>() {
                }.getType(),
                ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS);
    }

    @SuppressWarnings("unchecked")
    public boolean isBlacklisted(int level, @NotNull String fluid) {
        return level < 0 || registry.getOrDefault(level, Collections.EMPTY_LIST).contains(fluid);
    }

    public void register(int level, @NotNull String fluid) {
        List<String> list = registry.computeIfAbsent(level, k -> new ArrayList<>());

        list.add(fluid);
    }

    public void register(int level, Fluid fluid) {
        List<String> list = registry.computeIfAbsent(level, k -> new ArrayList<>());

        list.add(fluid.getName());
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<Integer, List<String>> loaded = gson.fromJson(fr, new TypeToken<Map<Integer, List<String>>>() {
        }.getType());

        loaded.forEach((integer, strings) -> {
            for (String string : strings) {
                register(integer, string);
            }
        });
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}
