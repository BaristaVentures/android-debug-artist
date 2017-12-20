# CHANGELOG

# v 0.6.5
- Update kotlin 1.2.10
- Add missing proguard setup.
- Update third party features versions. 

# v 0.6.4
- Add proguard setup.
- Update kotlin to 1.1.51.

# v 0.6.3
- Fix MemoryLeak caused by stetho [pr/48](https://github.com/BaristaVentures/debug-artist/pull/48)
- Update kotlin 1.1.1-5.
- Remove wrong stetho-okhttp dependency.
 
# v 0.6.2
- Remove extra resources on PivotalTracker reporter module.
- Update dependencies.
- Simplify the use of properties view.
- Hide library resources to user.

# v 0.6.1 (~~0.6~~ BROKEN) 
- Update to Kotlin 1.1.1.
- Add bug reporter with pivotal tracker, waiting others!

# v 0.5.11
- Update to Kotlin 1.0.6.
- Allow to set initial state for switch's (true/false).
- Show drawer on first launch now can be set from constructor.

# v 0.5.10 - (~~0.5.9~~ BROKEN) - First public release
- Kotlin `1.0.5-2`
- Features:
  - [Links](https://github.com/pedrovgs/Lynx): show logcat live phone.
  - [Leakcanary](https://github.com/square/leakcanary): track Memory leaks.
  - [Picasso logs](https://github.com/square/picasso): enable debug logs.
  - [Scalpel](https://github.com/JakeWharton/scalpel): see 3d layouts.
  - [Stetho](https://github.com/facebook/stetho): if you want custom interceptors you can add them and it will use them _automatically_.
  - [Process Phoenix](https://github.com/JakeWharton/ProcessPhoenix): restart app/activity.
  - Custom spinners: used sometimes to select from a list of hosts.
  - Custom text input fields: used to set the app api host dynamically.
  - Custom map of properties: map of `title` -> `content` that allow you to show  useful info about the app like version, current host, flavor, etc...

## v 0.5.7
- Kotlin `1.0.4`
- Update Stetho okhttp `1.4.1`

## v 0.5.6
- Update leak canary to `1.4`

## v 0.5.5
- Downgrade support lib to 24.1.1.
Last release broke anchor for Coordinator layouts and FAB.

## v 0.5.4 (Broken)
- Update support lib 24.2.0, build tools 24.0.2, gradle 2.14.1.
- Fix Lynks do nothing.

## v 0.5.3
- Add menu
- Start opened
- Support Library 24.1.1
- Kotlin 1.0.3

## V 0.5.2
- Add features as optionals.

## V 0.5.1
- Update kotlin 1.0.2
- Support library 23.4.0

## V 0.3.0
- Add proguard setup

## V 0.3.0
- Update name to Debug Artist.

## V 0.2.5 (DEPRECATED)
- Remove Toast after ok click on input item dialog.

## V 0.2.4
- Add dont select default value for spinner item if is not found on options array.

## V 0.2.3
- Add Initial selected item for spinner item.

## V 0.2.2
- Fix selected spinner item not reflected on UI.

## V 0.2.1
- Input item with `withInputItem(...)`

## V 0.2.0
- Spinner item with `withSpinnerItem(...)`
- Restart App item
- Restart Activity item

## V 0.1.8
- Unused button "Shake to"

## V 0.1.7
= min SDK version 15.

## V 0.1.6 BROKEN BUILD
- Add scalpel layout with `.withScalpelLayout` instead of using interface.

## V 0.1.5
- Clean Debugger init.
- Proguard setup.

## V 0.1.2 && 0.1.3
- Manifest with needed setup

## V 0.1.1
- Add proguard setup

## V 0.1
- First release
- Debug Drawer.
- ViewServer.
