# Shake to Report Bug

![](img/report-bug.jpg)

When this feature is enabled will be checking if you shake your device, on shake takes an screenshot
and starts a new activity with the provided . This builder enable the menu
to send the repository to the new activity and then build it there.

The idea is to allow custom bug repositories so build your own!

Supported third party bug/story/card trackers:

- [Pivotal Tracker](https://www.pivotaltracker.com/)
- Trello (soon)
- Github (soon)
- Any Idea?

# How to use it

1. Add the library

```groovy
debugCompile("com.baristav.debugartist:reporter_pivotal:0.6.1@aar") { transitive = true }
```

2. Create the instance of the `Builder` you need, in this case will be the `PivotalBugRepositoryBuilder`:


- From Kotlin:
```
val repositoryBuilder = PivotalBugRepositoryBuilder("project-key",
        "project-id", 
        propertiesToInclude,
        defaultTags);
```

- From Java:
```java
PivotalBugRepositoryBuilder repositoryBuilder = 
    new PivotalBugRepositoryBuilder("project-key",
            "project-id", 
            propertiesToInclude,
            defaultTags);

```

3. Add the button with the builder to  

- From Kotlin:
```kotlin
val debugDrawer = DebugDrawer(MyApplication.sInstance, this)
        //...
        .withShakeToReportBugSwitch(false, repositoryBuilder)        
```

- From Java:
```java
DebugDrawer debugDrawer = new DebugDrawer(MyApplication.sInstance, this)
        //...
        .withShakeToReportBugSwitch(false, repositoryBuilder)
```

# How to create your custom Bug Report Repository

You will need to create 2 classes that inherit from:
- `BugRepository.Builder`: know how to build the repository with its dependencies. Added to allow
the menu to send the dependencies to the report bug activity, build it and use it there.

- `BugRepository`: the repository itself that know how to use the custom service api to create.