package me.levitate.levitategenerators.generator;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class GeneratorTypes {
    private static final Map<Integer, GeneratorType> generatorTypeMap = new HashMap<>();

    public static void addGeneratorType(int tier, GeneratorType generatorType) {
        generatorTypeMap.put(tier, generatorType);
    }

    public static GeneratorType getGeneratorTypeByMaterial(Material blockMaterial) {
        for (GeneratorType generatorType : generatorTypeMap.values()) {
            if (generatorType.getGeneratorBlock() == blockMaterial) {
                return generatorType;
            }
        }

        return null;
    }

    public static GeneratorType getGeneratorTypeByTier(int tier) {
        return generatorTypeMap.get(tier);
    }

    public static Map<Integer, GeneratorType> getGeneratorTypes() {
        return generatorTypeMap;
    }

    public static int getMaxTier() {
        return generatorTypeMap.size();
    }
}
