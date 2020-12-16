package com.flemmli97.runecraftory.data;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Same as LanguageProvider but with a linked hashmap and reading from old lang file
 */
public class LangGen implements IDataProvider{

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<String, String> data = new LinkedHashMap<>();
    private final DataGenerator gen;
    private final String modid;
    private final String locale;

    private static final Comparator<String> order = Comparator.comparingInt(o -> LangType.get(o).ordinal());

    public LangGen(DataGenerator gen, ExistingFileHelper existing) {
        this.gen = gen;
        this.modid = RuneCraftory.MODID;
        this.locale = "en_us";
        this.setupOldFile(existing);
    }

    protected void addTranslations() {
        for (SpawnEgg egg : SpawnEgg.getEggs())
            this.add(egg, "%s" + " Spawn Egg");
    }

    private void setupOldFile(ExistingFileHelper existing){
        try {
            JsonObject obj = GSON.fromJson(
                    new InputStreamReader(existing.getResource(new ResourceLocation(RuneCraftory.MODID, "en_us"), ResourcePackType.CLIENT_RESOURCES, ".json", "lang").getInputStream()), JsonObject.class);
            obj.entrySet().forEach(e-> data.put(e.getKey(), e.getValue().getAsString()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        addTranslations();
        Map<String, String> sort = data.entrySet().stream().sorted((e,e2)->order.compare(e.getKey(), e2.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(old, v) -> old, LinkedHashMap::new));
        if (!data.isEmpty())
            save(cache, sort, this.gen.getOutputFolder().resolve("assets/" + modid + "/lang/" + locale + ".json"));
    }

    @Override
    public String getName() {
        return "Languages: " + locale;
    }

    private void save(DirectoryCache cache, Object object, Path target) throws IOException {
        String data = GSON.toJson(object);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = IDataProvider.HASH_FUNCTION.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getPreviousHash(target), hash) || !Files.exists(target)) {
            Files.createDirectories(target.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(target)) {
                bufferedwriter.write(data);
            }
        }

        cache.recordHash(target, hash);
    }

    public void addBlock(Supplier<? extends Block> key, String name) {
        add(key.get(), name);
    }

    public void add(Block key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addItem(Supplier<? extends Item> key, String name) {
        add(key.get(), name);
    }

    public void add(Item key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addItemStack(Supplier<ItemStack> key, String name) {
        add(key.get(), name);
    }

    public void add(ItemStack key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void addEnchantment(Supplier<? extends Enchantment> key, String name) {
        add(key.get(), name);
    }

    public void add(Enchantment key, String name) {
        add(key.getName(), name);
    }

    /*
    public void addBiome(Supplier<? extends Biome> key, String name) {
        add(key.get(), name);
    }

    public void add(Biome key, String name) {
        add(key.getTranslationKey(), name);
    }
    */

    public void addEffect(Supplier<? extends Effect> key, String name) {
        add(key.get(), name);
    }

    public void add(Effect key, String name) {
        add(key.getName(), name);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
        add(key.get(), name);
    }

    public void add(EntityType<?> key, String name) {
        add(key.getTranslationKey(), name);
    }

    public void add(String key, String value) {
        if (data.put(key, value) != null)
            throw new IllegalStateException("Duplicate translation key " + key);
    }

    enum LangType {
        ITEM,
        BLOCK,
        ENTITY,
        CONTAINER,
        ELEMENT,
        ATTRIBUTE,
        TOOLTIP,
        SEASON,
        DEATH,
        ITEMGROUP,
        OTHER;

        public static LangType get(String s){
            if(s.startsWith("item."))
                return ITEM;
            if(s.startsWith("block."))
                return BLOCK;
            if(s.startsWith("entity."))
                return ENTITY;
            if(s.startsWith("container."))
                return CONTAINER;
            if(s.startsWith("element_"))
                return ELEMENT;
            if(s.startsWith("attribute.rf."))
                return ATTRIBUTE;
            if(s.startsWith("tooltip."))
                return TOOLTIP;
            if(s.startsWith("season."))
                return SEASON;
            if(s.startsWith("death."))
                return DEATH;
            if(s.startsWith("itemGroup."))
                return ITEMGROUP;
            return OTHER;
        }
    }
}