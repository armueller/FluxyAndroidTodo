#FluxyTodo
FluxyTodo is an Android application that serves to demonstrate a few different things.
- It is an example of how to use [Dagger 2](http://google.github.io/dagger/) and [Butterknife](http://jakewharton.github.io/butterknife/) for DI (dependency injection) in and how to use [Otto](http://square.github.io/otto/) to decouple Android applications.
- It also serves as a proof of concept for a new Android application architecture I came up with.
- It promotes dependency injection, TDD (test driven development), and a unidirectional data-flow, which makes the separation of view and business logic simple and clear.

####If you want to skip around...
- [Motivation](https://github.com/armueller/FluxyAndroidTodo/tree/armueller-readme#motivation-for-those-who-are-interested)
- [Architecture](https://github.com/armueller/FluxyAndroidTodo/tree/armueller-readme#architecture)
- [Implementation](https://github.com/armueller/FluxyAndroidTodo/tree/armueller-readme#implementation)
- [DI Details](https://github.com/armueller/FluxyAndroidTodo/tree/armueller-readme#di-details-dagger-2--butterknife)
- [Future work](https://github.com/armueller/FluxyAndroidTodo/tree/armueller-readme#future-work)

##Motivation (For those who are interested...)
I recently embarked on a journey to make a new android application for a start up that I am a part of.  Being a software contractor, it is rare that I have the opportunity to build something from scratch.  When this opportunity presented itself, I was excited to be able to implement my arsenal of best practices and architecture patterns on the ground floor of a project.

For a large part of the last year I was working on a web project and didn't have much opportunity to keep up with Android.  The team I was on was using [AngularJS](https://angularjs.org/) for the project and we had a heavy focus on making sure that all of our code was like Lego blocks (loose coupling, lots of DI, and minimal side effects). Programming in this way was extremely liberating and I wanted to be able to keep it up after I switched back to Android.  This of course, I knew would be no easy task... as if the absence of functions as first class citizens wasn't depressing enough...

I decided to start with dependency injection.  I knew there were libraries out there to do this in Java and thus android, but I had always struggled with them in the past.  After a bit of searching I found [Dagger 2](http://google.github.io/dagger/).  After watching a some talks **(insert talk here)** and reading a few articles **(articles here)**, Dagger 2 seemed like the right way to go.  After a bit more digging I found [Butterknife](http://jakewharton.github.io/butterknife/) for view injection.  The biggest problem I ran into was the distinct lack of examples on how to use Dagger 2 effectively (probably because it is still in beta).  I did find a few, but they were mostly targeted on how to set it up and not so much on how to use it in an actual android application. **(list refs)**

After I figured out what I wanted to do for dependency injection, I wanted to figure out a good application architecture that would allow for very loose coupling and make it easy to separate view logic from business logic (something I got pretty good at in Angular).  Another benefit of having an architecture like this is that it makes TDD much easier... Mocking activities and views just to be able to test that your models behave as they should is absurd.

The architecture I decided on was [Flux](https://facebook.github.io/flux/).  Obviously, Facebook made the Flux architecture for the web, and thus JavaScript, so I had to do a little bit of adapting in order to get it to work on Android.  With a little bit of tweaking (mainly with the dispatcher and they way the views accessed data) and the assistance of the [Otto library by Square](http://square.github.io/otto/), I came up with a version of Flux that was Android compatible.

So rather than jump right in and start building an application with an untested architecture pattern and minimal familiarity with Dagger 2, Butterknife, and Otto, I decided to do a test run.  I followed Facebook's example and built a simple, but fully functional, todo application (Hence the name FluxyTodo).  On top of wanting to see how this architecture would pan out, I wanted to provide everybody with another example of how to use Dagger 2, Butterknife, and Otto.  Since the examples for these libraries in use are pretty sparse, especially in fully functional applications, I figured it would be a good addition to the community.

*...Plus if more people adopt this architecture, android development might become a little less painful for everybody!*

##Architecture
I came up with this architecture after looking at [Flux](https://facebook.github.io/flux/) and trying to apply those concepts to Android, so if you have worked with Flux this section will be pretty familiar to you.

Here is the basic architecture template
![Architecture Template](/pictures/Architecture-General.png)
As you can see, it is pretty similar to how Flux works with only a few modifications to make it Android Friendly.  The main difference is in how the dispatcher works.  Rather than build a dispatcher to mimic the behavior of the one Facebook built, I used an event bus instead [Otto](http://square.github.io/otto/).  Another minor difference is that I use models and stores.  Stores are dedicated for view state, while models hold other application data and business logic.

###Here is a detailed explanation of each part of the system
####View
This is what controls what is being displayed to the user.  In Android, this means that activities and fragments, along with their xml components, are classified as views.  A view should ONLY be responsible for building and displaying what should be presented to the user and listening for actions taken by the user on that view.  There should be absolutely no data manipulation, business logic, or even logic pertaining to view state in the view layer.

When the user preforms an action on part of the view, the view should delegate handling that action to the Action Creator. If this is done correctly, the view should only ever have to talk to one other thing, the Action Creator.  For example, if a user clicks on an upload button, the view should tell the Action Creator to create an upload action.

As for inflating view elements with data, the view should only have to listen to one source, the data bus.  Once the view is created, the view should subscribe to events on the data bus.  Once data comes in, the view can then take the data and bind it to whichever view component needs it.

More on the data bus later...

####Action Creator
The Action Creator has 2 responsibilities.  It's first responsibility is to provide an API for the View to call upon.  These API endpoints should correspond to possible actions the user can preform on the view. If you had a calculator app for example, your Action Creator should have methods such as 'createAddAction', 'createSubtractAction', and so on.  Depending on how you implemented your calculator, those methods may also take a parameter, for example, 'public void createAddAction(float numberToAdd)'.

The Action Creator's second responsibility is to create action objects and post them to the Action Bus.

####Action Bus
The Action Bus is the event bus that all actions are posted to.  The Stores and Models listen for specific actions posted to this bus in order carry out view logic or business logic, respectively.

####Stores / Models
Models are where data is stored and transformed.  Models in this case are similar to Models in an MVC type framework.  Models will subscribe to events posted onto the action bus.  To continue with the calculator example, if an AddAction object is posted to the ActionBus, the model will receive that event, then most likely add a number to whatever the current number is then post the updated data to the data bus.

Stores are slightly different than models in that the data stored in the store is strictly related to the current view state.  Unlike Models, what ends up in the view and what ends up in the store is more flexible and up to interpretation.  Our goal is to keep the view as dumb as possible; on the other hand, we don't want to make our app unnecessarily complicated by putting too much in the store.  If, for example, you had a button that just toggled on or off, you might want to just keep that in the view, but if you had a view pager and needed to keep track of which fragment is active, you could put that in the store.

####Data Bus
The Data Bus is the event bus that all model updates or view data updates are posted to.  The View subscribes to data posted on this bus in order to keep the view in sync with the data.

###Decoupling!
This architecture decouples all the parts of an Android app which makes development much easier.  Additionally, DI becomes easier, and  practicing TDD becomes much less of a headache!

Following is a diagram illustrating a few approaches to TDD you could take using this architecture.
![Architecture Template with TDD](/pictures/Architecture-General-TDD.png)
- First, you could publish mock data to the data bus and test to make sure the view displays the data correctly.
- Second, you could publish mock data (if needed) to the data bus then simulate a user action and test to make sure the correct action was published to the action bus.
- Third, you could publish mock actions to the action bus and test to make sure the data published to the data bus is correct.


##Implementation
To put this new architecture to the test, I decided to use it for a simple todo app (Really original of me, I know...).

Here is a diagram along with a detailed explanation of how each part works.
(pic)![Architecture Implementation with a ToDo App](/pictures/Architecture-Impl.png)
If you want even more detail, you can look at the actual code provided in this repository.

##DI Details ([Dagger 2](http://google.github.io/dagger/) & [Butterknife](http://jakewharton.github.io/butterknife/))
As far as explaining what, how, and why, there are a few good tutorials already out there that do a pretty good job
(refs)

##Future work
Despite the fact that this app is completely self contained (similar to how the todo app works from Facebook's Flux), I am pretty satisfied with out it turned out.  I came up with a couple different ideas on how server communication might be incorporated into this architecture, but I wasn't really happy with any of them.  Obviously, to be complete, server communication must fit into the equation somewhere, so that's what I'll be working on next.  Once I come up with a solution that im satisfied with, I will update this repo to reflect what I came up with.

###Todo:
- Figure out how to incorporate server communication into architecture
- Set up db at firebase for todos
- Make app sync data to firebase
- Revise readme to incorporate server communication into architecture.
