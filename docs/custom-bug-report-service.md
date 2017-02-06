# Shake to Report Bug

![](img/report-bug.jpg)

When this feature is enabled will be checking if you shake your device, on shake takes an screenshot
and starts a new activity with the provided . This builder enable the menu
to send the repository to the new activity and then build it there.

The idea is to allow custom bug repositories so build your own!

# How to create your custom Bug Report Repository

You will need to create 2 classes that inherit from:
- `BugRepository.Builder`: know how to build the repository with its dependencies. Added to allow
the menu to send the dependencies to the report bug activity, build it and use it there.

- `BugRepository`: the repository itself that know how to use the custom service api to create.

The only service provided is [Pivotal Tracker](https://www.pivotaltracker.com/) but you can build
your own too.

