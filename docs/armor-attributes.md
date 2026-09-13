# Minecraft armor attributes

After launching once, edit `config/adamsarsplus-common.toml`. Under `Armor Configs` → `Minecraft Attributes`, each of `cade`, `ryan`, `nick`, `cam`, `matt`, and `adam` has:

- `helmet_armor`, `chestplate_armor`, `leggings_armor`, `boots_armor`: armor points contributed by that piece.
- `armor_toughness`: toughness contributed by **each** piece.
- `knockback_resistance`: resistance contributed by **each** piece; 0.1 means 10%, so a full set contributes 40%.

These base bonuses apply equally to normal and awakened pieces at every perk tier. Existing Ars mana, spell damage, warding, and perk options still apply independently. Defaults match the current materials. Bonuses also apply to mobs wearing these items, including bosses.

Restart the world/server after editing. Existing items use the updated defaults without recrafting. Ship matching common configs to clients and servers so local tooltips agree with server combat values. Explicit item attribute-component overrides made by commands/data packs retain Minecraft's normal precedence over item defaults.

Example (edit the existing section):

```toml
["Armor Configs"."Minecraft Attributes".cade]
helmet_armor = 3
chestplate_armor = 8
leggings_armor = 6
boots_armor = 3
armor_toughness = 3.0
knockback_resistance = 0.05
```

Minecraft caps effective total armor at 30, toughness at 20, and knockback resistance at 1 unless another mod changes those limits. These options control equipment attributes, not durability, enchantability, or set-bonus effects.
