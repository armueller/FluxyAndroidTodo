#FluxyTodo
FluxyTodo is an Android application that serves to demonstrate a few different things.
- It is an example of how to use [Dagger 2](http://google.github.io/dagger/) and [Butterknife](http://jakewharton.github.io/butterknife/) for DI (dependency injection) in and how to use [Otto](http://square.github.io/otto/) to decouple Android applications.
- It also serves as a proof of concept for a new Android application architecture I came up with.
- It promotes dependency injection, TDD (test driven development), and a unidirectional data-flow, which makes the separation of view and business logic simple and clear.

More information about what, why, and how, can be found [here](http://armueller.github.io/android/2015/03/28/flux-and-android.html).

##Running
If you would like to get this project up and running so you can mess around with it, just follow these simple steps.
- Fork
- Clone
- Import project into Android Studio
- Run app

#Testing
If you would like to run the tests included in this project
- Find the src/test/java/com/armueller/fluxytodo directory
- Right click on the package (inside Android Studio)
- Select "Run 'Tests in com.armueller.fluxytodo'"

##Future work
Despite the fact that this app is completely self contained (similar to how the todo app works from Facebook's Flux), I am pretty satisfied with out it turned out.  I came up with a couple different ideas on how server communication might be incorporated into this architecture, but I wasn't really happy with any of them.  Obviously, to be complete, server communication must fit into the equation somewhere, so that's what I'll be working on next.  Once I come up with a solution that im satisfied with, I will update this repo and my blog post to reflect what I came up with.

###Todo:
- Figure out how to incorporate server communication into architecture
- Set up db at firebase for todos
- Make app sync data to firebase
- Revise blog to incorporate server communication into architecture
- Update repo
