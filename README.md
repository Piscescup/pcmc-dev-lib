# PCMC Develop Lib

**PiscesCup Minecraft Develop Library** (**PCMC Dev Lib**, mod namespace: `pcmc-dev-lib`) is a Java library for
Fabric mods. It provides fluent APIs for registering content and generating the
related language, tag, recipe, and villager-trade data for Minecraft 26.2.

> [!WARNING]
> The library is under active development. Public APIs may change before the
> first stable release.

## Requirements

|   Component   |     Version     |
|:-------------:|:---------------:|
|   Minecraft   |      26.2       |
|     Java      |   25 or later   |
| Fabric Loader | 0.19.5 or later |
|  Fabric Loom  |     1.17.20     |

## Supported APIs

- Items and custom item subclasses
- Blocks, custom block subclasses, and their block items
- Creative mode tabs
- Tags for items, blocks, entities, POIs, and other registries
- Shaped and shapeless crafting recipes
- Points of interest (POIs)
- Villager professions and level-based trades
- Language data generation
- Identifier path utilities

## Installation

PCMC Develop Lib does not have a stable public release yet. A public release is
planned for the future.

Once the library is published, you can add it to your project using one of the
following dependency declarations.

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation(
        "io.github.piscescup:pcmc-dev-lib:$pcmc_dev_lib_version"
    )
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.piscescup</groupId>
    <artifactId>pcmc-dev-lib</artifactId>
    <version>${pcmc-dev-lib.version}</version>
</dependency>
```


## Quick start

Create one factory per content type and namespace, declare entries as static
fields, and make sure each declaration class is initialized from your mod
initializer.

```java
public final class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "examplemod";

    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        ModPoiTypes.initialize();
        ModTags.initialize();
        ModRecipes.initialize();
        ModCreativeTabs.initialize();
        ModVillagerProfessions.initialize();
    }
}
```

An `initialize()` method can be empty. Calling it ensures that the class's
static registration declarations have been evaluated:

```java
public static void initialize() {
}
```

When one content type references another, initialize the referenced type first.
For example, initialize items before tabs and recipes, and POIs before villager
professions.

## Registration workflow

Most registry APIs follow the same call order:

```java
FACTORY.path("entry_path")
    // Configure the entry here.
    .register()
    // Add translations or inspect the registered entry here.
    .translate(MCLanguage.EN_US, "Display Name")
    .get();
```

Use configuration methods before `register()`. After `register()`, the common
operations are:

|           Method            |                             Result                              |
|:---------------------------:|:---------------------------------------------------------------:|
| `translate(language, text)` |             Adds text for language data generation              |
|           `get()`           | Returns the registered value while preserving its concrete type |
|       `identifier()`        |               Returns its namespaced `Identifier`               |
|       `resourceKey()`       |               Returns its registry `ResourceKey`                |
|  `collectsTo(collection)`   |            Adds the registered value to a collection            |

Translations declared with `translate(...)` are written when data generation
runs. The call does not edit a language file immediately.

### Items

Create an `ItemRegisterFactory` with your mod ID:

```java
public final class ModItems {
    private static final ItemRegisterFactory ITEMS =
        ItemRegisterFactory.ofNamespace(ExampleMod.MOD_ID);

    public static final Item RAW_SAPPHIRE = ITEMS.path("raw_sapphire")
        .properties(new Item.Properties())
        .register()
        .translate(MCLanguage.EN_US, "Raw Sapphire")
        .translate(MCLanguage.ZH_CN, "粗制蓝宝石")
        .get();

    public static final MagicWand MAGIC_WAND = ITEMS
        .path("magic_wand", MagicWand::new)
        .properties(new Item.Properties().fireResistant())
        .register()
        .translate(MCLanguage.EN_US, "Magic Wand")
        .get();

    public static void initialize() {
    }
}
```

The custom item factory receives the configured `Item.Properties`, so a custom
item can use the normal Minecraft constructor shape:

```java
public final class MagicWand extends Item {
    public MagicWand(Properties properties) {
        super(properties);
    }
}
```

`properties(...)` is optional when the default `Item.Properties` is sufficient.

### Blocks and block items

`BlockRegistryFactory` registers a block together with its item form:

```java
public final class ModBlocks {
    private static final BlockRegistryFactory BLOCKS =
        BlockRegistryFactory.ofNamespace(ExampleMod.MOD_ID);

    public static final Block SAPPHIRE_BLOCK = BLOCKS.path("sapphire_block")
        .blockProperties(BlockBehaviour.Properties.of()
            .strength(3.0F, 6.0F)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops())
        .blockItemProperties(new Item.Properties().fireResistant())
        .register()
        .translate(MCLanguage.EN_US, "Block of Sapphire")
        .get();

    public static final GlowingBlock GLOWING_BLOCK = BLOCKS
        .path("glowing_block", GlowingBlock::new)
        .blockProperties(BlockBehaviour.Properties.of()
            .strength(2.0F)
            .sound(SoundType.ANVIL))
        .blockItemFactory(BlockItem::new)
        .register()
        .translate(MCLanguage.EN_US, "Glowing Block")
        .get();

    public static void initialize() {
    }
}
```

The block and block item use the same path by default. To give them different
IDs, use `path(blockId, itemId)` or
`path(blockId, itemId, customBlockFactory)`.

If you retain the `BlockPostRegistrable` returned by `register()`, use
`blockItem()` and `blockItemId()` to access the matching item and its ID.

### Creative mode tabs

```java
public final class ModCreativeTabs {
    private static final CreativeModeTabRegisterFactory TABS =
        CreativeModeTabRegisterFactory.ofNamespace(ExampleMod.MOD_ID);

    public static final CreativeModeTab MAIN = TABS.path("main")
        .iconFromItem(ModItems.RAW_SAPPHIRE)
        .addItems(ModItems.RAW_SAPPHIRE, ModItems.MAGIC_WAND)
        .add(ModBlocks.SAPPHIRE_BLOCK)
        .register()
        .translate(MCLanguage.EN_US, "Example Mod")
        .get();

    public static void initialize() {
    }
}
```

Useful tab options include:

- `position(row, column)` to choose a menu position. Call this before other tab
  appearance options.
- `icon(supplier)` or `iconFromItem(item)` to set the icon.
- `add(...)`, `addItem(...)`, `addItems(...)`, and `addStack(...)` to add
  contents.
- `noScrollBar()`, `hideTitle()`, and `alignedRight()` to change the layout.
- `background(path)` or `backgroundWithDefaultNamespace(path)` to select a
  background texture.

### Tags

#### Declaring a custom tag

Choose a factory for the registry targeted by the tag:

```java
public final class ModTags {
    private static final TagKeyRegisterFactory<Item> ITEM_TAGS =
        TagKeyRegisterFactory.itemsTagOfNamespace(ExampleMod.MOD_ID);

    public static final TagKey<Item> SAPPHIRES = ITEM_TAGS.path("sapphires")
        .addEntries(ModItems.RAW_SAPPHIRE)
        .register()
        .get();

    public static void initialize() {
    }
}
```

Convenience factories are available for common registries, including
`itemsTagOfNamespace`, `blocksTagOfNamespace`, `entitiesTagOfNamespace`,
`poiTagOfNamespace`, `damageTypesTagOfNamespace`, and
`villagerTradeTagOfNamespace`.

For another registry, use
`TagKeyRegisterFactory.ofNamespace(MOD_ID, registryKey)`.

#### Adding to a vanilla tag

Use `ofVanilla(...)` when the tag key already exists:

```java
public static final TagKey<Block> MINEABLE_WITH_PICKAXE =
    TagKeyRegisterFactory.ofVanilla(BlockTags.MINEABLE_WITH_PICKAXE)
        .addEntries(ModBlocks.SAPPHIRE_BLOCK)
        .register()
        .get();

public static final TagKey<Block> NEEDS_IRON_TOOL =
    TagKeyRegisterFactory.ofVanilla(BlockTags.NEEDS_IRON_TOOL)
        .addEntries(ModBlocks.SAPPHIRE_BLOCK)
        .register()
        .get();
```

Tag members can be supplied in several forms:

|                       Method                       |                    Use                    |
|:--------------------------------------------------:|:-----------------------------------------:|
|    `addEntry(value)` / `addEntries(values...)`     |           Add registered values           |
|                 `add(identifier)`                  |       Add a member by namespaced ID       |
| `addRegistryKey(key)` / `addRegistryKeys(keys...)` |             Add registry keys             |
|         `addTag(tag)` / `addTags(tags...)`         | Include other tags from the same registry |

All keys and nested tags passed to one declaration must target the same
registry as that tag.

### Crafting recipes

Recipes are declarations used by data generation. Every recipe must have at
least one unlock criterion.

### Crafting Recipe: Shapeless recipe & Shaped recipe

```java
public final class ModCraftingRecipes {
  private static final Collection<RecipeRegistrable<?>> RECIPES =
          new ArrayList<>();

  private static final CraftingRecipeRegisterFactory FACTORY =
          CraftingRecipeRegisterFactory.ofNamespace(MOD_ID);

  public static final ShapelessCraftingRecipeRegistrable TEST_ITEM_1_RECIPE = FACTORY
          .shapeless("test_item1", RecipeCategory.MISC, TestItems.ITEM1)
          .requires(Items.DIAMOND)
          .requires(ItemTags.WOOL)
          .unlockedBy("test_item1", Items.DIAMOND)
          .collectsTo(RECIPES);

  public static final ShapedCraftingRecipeRegistrable SAPPHIRE_BLOCK = FACTORY
          .shaped(
                  "sapphire_block",
                  RecipeCategory.BUILDING_BLOCKS,
                  ModBlocks.SAPPHIRE_BLOCK
          )
          .pattern("SSS")
          .pattern("SSS")
          .pattern("SSS")
          .define('S', ModItems.RAW_SAPPHIRE)
          .unlockedBy("has_sapphire", ModItems.RAW_SAPPHIRE)
          .collectsTo(RECIPES);

  public static void registerRecipes() {
    RECIPES.forEach(RecipeRegistrable::register);
  }
}

```

`requires(...)` accepts an item, block, item tag, or `Ingredient`. Overloads are
available for repeated ingredients.

Register this declaration from `initialize()` as well:

```java
SAPPHIRE_BLOCK.register();
```

The path passed to `shaped(...)` or `shapeless(...)` is the recipe ID. It does
not need to match the result item's ID. Use `group(...)` to assign a recipe-book
group and `showNotification(false)` to hide a shaped recipe's unlock
notification.

If you prefer to manage several declarations as a collection, call
`collectsTo(collection)` on each recipe and then call `register()` on every
collected value.

### Points of interest

A POI declaration requires matching block states, a ticket count, and a valid
range:

```java
public final class ModPoiTypes {
    private static final POIRegisterFactory POIS =
        POIRegisterFactory.ofNamespace(ExampleMod.MOD_ID);

    public static final ResourceKey<PoiType> SAPPHIRE_WORKSTATION =
        POIS.path("sapphire_workstation")
            .matchingStatesFrom(ModBlocks.SAPPHIRE_BLOCK)
            .maxTickets(1)
            .validRange(1)
            .register()
            .resourceKey();

    public static void initialize() {
    }
}
```

Use `matchingStates(set)` when only selected `BlockState` values should belong
to the POI. Use `matchingStatesFrom(block)` to include all possible states of a
block.

For a villager to acquire the POI as a job site, add its key to the vanilla
acquirable-job-site tag:

```java
public static final TagKey<PoiType> ACQUIRABLE_JOB_SITES =
    TagKeyRegisterFactory.ofVanilla(PoiTypeTags.ACQUIRABLE_JOB_SITE)
        .addRegistryKey(ModPoiTypes.SAPPHIRE_WORKSTATION)
        .register()
        .get();
```

### Villager trades and professions

Declare trades by villager level first. The `trade(...)` helper parameters are
the input item and count, output item and count, maximum uses, and villager XP:

```java
import static io.github.piscescup.fabricmc.api.trade.VillagerLevelTradeBuilder.trade;

public final class ModVillagerTrades {
    public static final VillagerProfessionTradesPostRegistrable GEM_TRADES =
        VillagerProfessionTradesRegisterFactory.create()
            .novice(level -> level
                .add(
                    "coal_for_emerald",
                    trade(Items.COAL, 16, Items.EMERALD, 1, 16, 2)
                )
                .add(
                    "emerald_for_sapphire",
                    trade(Items.EMERALD, 4, ModItems.RAW_SAPPHIRE, 1, 12, 5)
                )
                .amount(2)
                .allowDuplicates(false)
            )
            .master(level -> level
                .add(
                    "sapphire_for_diamond",
                    trade(ModItems.RAW_SAPPHIRE, 8, Items.DIAMOND, 1, 4, 30)
                )
                .amount(1)
            )
            .register();

    private ModVillagerTrades() {
    }
}
```

The available level methods are `novice`, `apprentice`, `journeyman`, `expert`,
and `master`. Within a level:

- `add(path, trade)` declares a trade.
- `include(tradeKey)` includes an existing trade.
- `includeTag(tagKey)` includes trades from a trade tag.
- `amount(number)` controls how many offers are selected.
- `allowDuplicates(boolean)` controls repeated selection.
- `randomSequence(identifier)` selects the random sequence used for selection.

Then register the profession and attach the completed trade declaration:

```java
public final class ModVillagerProfessions {
    private static final VillagerProfessionRegisterFactory PROFESSIONS =
        VillagerProfessionRegisterFactory.ofNamespace(ExampleMod.MOD_ID);

    public static final VillagerProfession GEM_TRADER = PROFESSIONS
        .path("gem_trader")
        .heldJobSite(ModPoiTypes.SAPPHIRE_WORKSTATION)
        .workSound(SoundEvents.ANVIL_USE)
        .tradeSetsByLevel(ModVillagerTrades.GEM_TRADES)
        .register()
        .translate(MCLanguage.EN_US, "Gem Trader")
        .translate(MCLanguage.ZH_CN, "宝石商人")
        .get();

    public static void initialize() {
    }
}
```

Optional profession settings include `acquirableJobSite(...)`,
`requestedItems(...)`, and `secondaryPoi(...)`.

## Data generation

Run data generation to create the JSON files declared through `translate`, tag,
recipe, and villager-trade APIs.

### 1. Enable Fabric data generation

In the consuming mod's `build.gradle.kts`:

```kotlin
fabricApi {
    configureDataGeneration {
        client = true
    }
}
```

### 2. Add the data-generator entrypoint

Add or merge the `fabric-datagen` entry in `fabric.mod.json`:

```json
{
  "entrypoints": {
    "main": [
      "com.example.examplemod.ExampleMod"
    ],
    "fabric-datagen": [
      "com.example.examplemod.ExampleDataGenerator"
    ]
  }
}
```

### 3. Select the providers

```java
public final class ExampleDataGenerator implements DataGeneratorEntrypoint {
    private final DataGeneratorCollectors collectors =
        DataGeneratorCollectors.configuration()
            .translationHolder(MutableTranslationsHolder.INSTANCE)
            .tagKeyHolder(MutableTagKeysHolder.INSTANCE)
            .recipeHolder(MutableReciperegistrablesHolder.INSTANCE)
            .villagerTradesHolder(MutableVillagerTradeHolder.INSTANCE)
            .build()
            .langProvider(MCLanguage.EN_US)
            .langProvider(MCLanguage.ZH_CN)
            .tagProvider(Registries.ITEM)
            .tagProvider(Registries.BLOCK)
            .tagProvider(Registries.POINT_OF_INTEREST_TYPE)
            .recipesProvider()
            .villagerTradesProvider();

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        collectors.generate(pack);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        collectors.buildRegistry(registryBuilder);
    }
}
```

All four holders are required by `configuration().build()`. Add only the
providers you want to run after `build()`:

|       Provider call        |                Generated data                 |
|:--------------------------:|:---------------------------------------------:|
|  `langProvider(language)`  |      One language file for that language      |
| `tagProvider(registryKey)` |            Tags for that registry             |
|    `recipesProvider()`     | Shaped and shapeless recipes and advancements |
| `villagerTradesProvider()` |  Villager trades, trade sets, and trade tags  |

When using `villagerTradesProvider()`, keep the `buildRegistry(...)` override
and use the same `DataGeneratorCollectors` instance in both entrypoint methods.

### 4. Run data generation

```shell
./gradlew runDatagen
```

In this repository, the example/test mod can be generated with:

```shell
./gradlew :pcmc-dev-lib-test:runDatagen
```
## Utils

### Identifier path utilities

`IdentifierUtils` preserves the namespace while adding path segments:

```java
Identifier id = Identifier.fromNamespaceAndPath("examplemod", "oak_table");

Identifier parent = IdentifierUtils.resolveParentPath(
    id,
    "furniture",
    "tables"
);
// examplemod:furniture/tables/oak_table

Identifier child = IdentifierUtils.resolveSubPath(
    id,
    "models",
    "inventory"
);
// examplemod:oak_table/models/inventory
```

## Common mistakes

- Forgetting to call each declaration class's `initialize()` method from the
  mod initializer.
- Calling `register()` more than once on the same registration declaration.
- Trying to call configuration methods after `register()`.
- Reusing the same namespace and path for two entries in the same registry.
- Creating a recipe without an unlock criterion.
- Passing a registry key or nested tag from a different registry to a tag
  declaration.
- Expecting `translate`, tag, recipe, or trade declarations to create JSON
  without running data generation.
- Registering a villager profession before its POI and trade declarations are
  available.

## Development commands

Build all modules:

```shell
./gradlew buildAll
```

Publish the current version to Maven Local:

```shell
./gradlew publishAllToMavenLocal
```

Run the example/test client:

```shell
./gradlew :pcmc-dev-lib-test:runClient
```

## Development status

|                Area                |  Status   |
|:----------------------------------:|:---------:|
|         Item registration          | Available |
| Block and block-item registration  | Available |
|   Creative mode tab registration   | Available |
|      Language data generation      | Available |
|       Recipe data generation       | Available |
|          POI registration          | Available |
| Villager profession and trade APIs | Available |
|   Tag declaration and generation   | Available |
|          Command helpers           |  Planned  |

The status table describes the current development direction and is not a
stability guarantee.
