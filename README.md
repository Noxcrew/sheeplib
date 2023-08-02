SheepLib
---
A Fabric library for interactable HUD components, written in Kotlin

# What is SheepLib?

SheepLib is a library for adding stuff to Minecraft's in-game HUD, in the form of dialogs.
Dialogs are small windows that appear on screen, and can be interacted with through the chat screen.

### What is SheepLib _not_?

- It's not a general-purpose UI library. While many of its components could theoretically be used in regular vanilla
  UI (`Screen`s and the like), this is not what they're designed to do.
- It's first and foremost a Kotlin library. It will likely work fine from Java code, but YMMV.

# Using SheepLib

### Including it as a dependency

Before you use SheepLib, ensure that [Fabric](https://fabricmc.net/wiki/tutorial:setup)
and [Fabric Language Kotlin](https://github.com/FabricMC/fabric-language-kotlin#usage) are set up for your project.
SheepLib does not depend on the Fabric API.

Once you've done that, add the library to the `modImplementation` dependency configuration.
Add it to `include` too to JiJ it.

```kotlin
// TODO: we haven't published the library to a maven repo anywhere yet
// for now just go through maven local

dependencies {
    include(modImplementation("com.noxcrew.sheeplib:api:<version>")!!)
}
```

### Create dialogs

[The wiki](https://github.com/Noxcrew/sheeplib/wiki) contains some more in-depth documentation, but basic use is:

- Subclass `Dialog`
    - Add widgets through a layout (see `Themed.grid` extension function)
    - Add a theme (delegate `Themed` to `Theme.Active` if you don't have a theme already)
- Instantiate your dialog and add it to the dialog container `DialogContainer += MyDialog()`
- Close dialogs by calling `Dialog.close()`

# Test mod

The `test-mod` module is exactly what it says on the tin - it's a test mod. Its main function is to test the library,
but it's a nice environment to explore how the library works. The test dialogs are nice examples for the library's
functionality.
