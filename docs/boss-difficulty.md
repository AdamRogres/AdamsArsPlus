# Boss difficulty configuration

Edit `config/adamsarsplus-common.toml` after launching the mod once. Pack makers can ship this file in their pack's `config` directory. Restart the world/server after editing; multiplayer uses the server values.

`["Boss Configs".ryan]` (and `cade`, `nick`, `cam`, `matt`, `josh`, `adam`, `mahoraga`) contains every attribute in the current boss attribute supplier, including inherited living-entity attributes. Mahoraga has no flying speed. Defaults preserve the existing base values. Armor/equipment and potion modifiers still apply. AI goals may impose their own ranges and speed multipliers; attack animations and scripted cooldowns are not controlled by the player attack-speed attribute.

For example, change entries in Ryan's existing section:

```toml
["Boss Configs".ryan]
max_health = 750.0
attack_damage = 16.0
spell_damage_bonus = 3.0
spell_damage_multiplier = 1.5
```

Ars Nouveau adds `spell_damage_bonus` to each supported damage-glyph hit. The multiplier then scales that hit: a 10-damage hit with +3 and 1.5 becomes 19.5 before target defenses and other event handlers. A multiplier below 1 weakens spells; 0 removes their direct damage. Neither setting changes glyph amplification, radius, duration, domain clashes, or status effects. Damage bypassing Ars' spell-damage path (such as fire ticks or separate summoned creatures) is not multiplied.

Values apply when bosses spawn or load, including bosses saved before this feature. A maximum-health change preserves the fraction of health remaining; loading does not heal a damaged boss. The last applied settings are saved with the entity, so subsequent loads with unchanged config do not erase base-attribute adjustments made by commands or scripts. Attribute modifiers are never removed.

Minecraft clamps effective attribute values. Vanilla maximum health is capped at 1024, even though some existing bosses declare higher base values. The config permits higher health bases for packs using a mod that raises that cap; this feature does not globally change Minecraft's limits. Armor, toughness and other options use their normal supported ranges. Attributes only affect behaviors that consume them: for example, gravity has no effect while a boss disables gravity, and fall-damage settings do not remove a boss's fall immunity.
