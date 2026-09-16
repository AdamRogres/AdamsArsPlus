# General's Wheel worn model

Equipping General's Wheel in a visible Curios slot renders a wheel above the player's head. It follows the head transform, including crouching, and supports both normal and slim player models. It is visible in third person and to other players. Curios' slot visibility toggle hides it. Inventory and held-item rendering continue to use the existing item model.

## Blockbench export

Use a **Modded Entity** project, targeting **Minecraft 1.21 Mojang mappings**, and export **Java**. This renderer uses a vanilla Java model, not GeckoLib `.geo.json` or a `.bbmodel` resource loader.

- Java model: `src/main/java/adamsmods/adamsarsplus/client/entities/WheelModel.java`
- PNG texture: `src/main/resources/assets/adamsarsplus/textures/entity/curio/generals_wheel.png`

The included model already provides a working wheel from Mahoraga and its matching 128×128 texture. Replace the geometry in `createMesh()` with your exported Blockbench geometry. Keep the package, `WheelModel extends Model`, constructor, `createBodyLayer()`, and `renderToBuffer()` wrapper compatible with the renderer. With the current wrapper the bone hierarchy must be `waist` → `wheel`. If you change the hierarchy, update the constructor's `getChild` calls and rendered root too. Match the texture size in `createBodyLayer()` to your PNG.

The model origin is the player's head pivot: the top of the head is Y = -8 in Java model coordinates. The sample wheel center is Y = -14 (waist offset +24, wheel offset -38). Keep the preview player out of your exported model. Save your editable `.bbmodel` wherever you keep source art; Minecraft does not load that file.

For placement tweaks, edit `OFFSET_X`, `OFFSET_Y`, `OFFSET_Z`, and `SCALE` in `src/main/java/adamsmods/adamsarsplus/client/curio/GeneralsWheelRenderer.java`. Offsets are in blocks, while Blockbench/Java cubes use pixels (16 pixels per block). Rebuild and restart for Java geometry changes; texture changes can be resource-reloaded.
