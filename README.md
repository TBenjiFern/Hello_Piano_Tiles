# Overview

This app is a downgraded version of the popular mobile application game called "Piano Tiles." In essence, you have a screen where tiles are falling down in three or four columns.
It's then your job to tab each of the tiles in order as they fall down as fast as you can within the allotted time to get a high score. If you make a mistake
then you fail the game and it kicks you to home screen. If you choose the correct tile, then the game progresses and a new tile falls down to be pressed.

I decided to try and create this app because I thought it would help teach me how to work with timers, app layouts, variables, kotlin logic, and that
it would be a lot more interesting than just making a stationary single-screened app. I must make it clear here that I followed along a very useful tutorial
on youtube by "Tihomir RAdeff" which greatly helped me conceptualize how to build this project in java (link provided below). Normally, I would elect to create an original project
and avoid borrowing code from outside sources, but I thought that following along this time would help me quickly understand the power Android Studios has
in creating functioning apps. Ultimately, I can admit that I learned a lot about how to write code in Kotlin and how to use Android Studios thanks to this tutorial
and thanks to the documentation included in Kotlin and Android Studios respective websites (those websites also linked below). My purpose was to feel more
comfortable in using Android Studios for future app design, and I feel that I achieved that goal. 


[App Demo Youtube Link](https://youtu.be/8tMjibvZBpo)

# Development Environment

Tools used to create the app:
1. Android Studios v. 4.1
1. Pixel 5 API 30 virtual device emulator
1. R - Android 11.0 x86
1. Android Studio Github Tool


Programming language and imported libraries:
1. Kotlin (Android Installation)
1. android.media.MediaPlayer
1. androidx.appcompat.app.AppCompatActivity
1. android.os.Bundle
1. android.os.CountDownTimer
1. android.view.View
1. android.widget.*
1. java.util.*
1. kotlin.properties.Delegates

# Useful Websites

* [Tihomir RAdeff's Tutorial in Java](https://www.youtube.com/watch?v=tiYGB6utBWU)
* [Android Studios Documentation](https://developer.android.com)
* [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
* [Stack Overflow](https://stackoverflow.com)

# Future Work

* Create a menu screen which will then transition to the game.
* Include options which increase game time, adds zen mode, and which can change music.
* Attach game to firebase to keep high scores saved.
* Remake the grid system so that the movement down the screen is smoother.