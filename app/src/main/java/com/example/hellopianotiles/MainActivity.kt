package com.example.hellopianotiles

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    // Declare all variables needed to be shared across every method in the document

    // mMediaPlayer is used to play audio files stored in the raw directory under "res"
    private var mMediaPlayer: MediaPlayer? = null

    // These ImageView variables will each be responsible for a section of the screen's grid
    // The first number indicates row #, the second is column #
    private lateinit var grid11: ImageView
    private lateinit var grid12: ImageView
    private lateinit var grid13: ImageView
    private lateinit var grid21: ImageView
    private lateinit var grid22: ImageView
    private lateinit var grid23: ImageView
    private lateinit var grid31: ImageView
    private lateinit var grid32: ImageView
    private lateinit var grid33: ImageView
    private lateinit var grid41: ImageView
    private lateinit var grid42: ImageView
    private lateinit var grid43: ImageView
    private lateinit var grid51: ImageView
    private lateinit var grid52: ImageView
    private lateinit var grid53: ImageView

    // Store all these ImageView variables into an array for a loop call later
    private lateinit var gridArray: Array<ImageView>

    // Create start button
    private lateinit var startButton: Button

    // These two variables will change text based on score and time
    private lateinit var timer: TextView
    private lateinit var score: TextView

    // This will generate a random number. Later will be used to spawn the tiles in a random row
    private lateinit var r: Random

    // These will keep track of where each object is on the grid (only one per row at a time)
    // Because they are primitive numbers (ints) we must store by Delegates.notNull<>().
    private var objectLocationRow1 by Delegates.notNull<Int>()
    private var objectLocationRow2 by Delegates.notNull<Int>()
    private var objectLocationRow3 by Delegates.notNull<Int>()
    private var objectLocationRow4 by Delegates.notNull<Int>()
    private var objectLocationRow5 by Delegates.notNull<Int>()

    // These will load in the images stored under "drawable"
    private var blackFrame by Delegates.notNull<Int>()
    private var emptyBox by Delegates.notNull<Int>()
    private var frame by Delegates.notNull<Int>()
    private var tap by Delegates.notNull<Int>()

    // This will keep track of the score and update the score id above.
    var currentScore = 0;

    // Create object variable to hold the countdowntimer
    private lateinit var timeCounter: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_screen) // Start screen on the main page layout

        // Attach each grid id to its corresponding grid variable
        grid11 = findViewById<ImageView>(R.id.grid11)
        grid12 = findViewById<ImageView>(R.id.grid12)
        grid13 = findViewById<ImageView>(R.id.grid13)

        grid21 = findViewById<ImageView>(R.id.grid21)
        grid22 = findViewById<ImageView>(R.id.grid22)
        grid23 = findViewById<ImageView>(R.id.grid23)

        grid31 = findViewById<ImageView>(R.id.grid31)
        grid32 = findViewById<ImageView>(R.id.grid32)
        grid33 = findViewById<ImageView>(R.id.grid33)

        grid41 = findViewById<ImageView>(R.id.grid41)
        grid42 = findViewById<ImageView>(R.id.grid42)
        grid43 = findViewById<ImageView>(R.id.grid43)

        grid51 = findViewById<ImageView>(R.id.grid51)
        grid52 = findViewById<ImageView>(R.id.grid52)
        grid53 = findViewById<ImageView>(R.id.grid53)

        // Store all the above grids into this array for a later loop
        gridArray = arrayOf<ImageView>(grid11, grid12, grid13, grid21, grid22, grid23, grid31, grid32, grid33, grid41, grid42, grid43, grid51, grid52, grid53)

        // Attach button id to startButton variable
        startButton = findViewById<Button>(R.id.startButton)

        // Attach score TextView to the score variable, then set initial text to equal the currentScore
        // (which is 0 right now)
        score = findViewById<TextView>(R.id.score)
        score.setText("SCORE: " + currentScore)

        // Set the timer id to the timer variable and set the initial time to 30 seconds.
        timer = findViewById<TextView>(R.id.timer)
        timer.setText("TIME: " + milliToSecond(30000)) // The milliToSecond method is found at the bottom of the document

        // Set random number to "r" variable
        r = Random()
        // Load images to their respective variables
        loadImages()

        // This is a CountDownTimer object which has an onTick and onFinish method found inside.
        // This will start a timer at 30 seconds and decrease it by 1 second intervals. The onTick
        // will update the timer text each second and the onFinish will end the game.
        timeCounter = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                // Update time
                timer.setText("TIME: " + milliToSecond(millisUntilFinished))
            }

            override fun onFinish() {
                // Set time to 0
                timer.setText("TIME: " + milliToSecond(0))
                // Stop the audio
                stopSound()
                // Disable the 3 row's functionality
                grid31.setEnabled(false)
                grid32.setEnabled(false)
                grid33.setEnabled(false)
                // Show the start button
                startButton.setVisibility(View.VISIBLE)

                // Use the gridArray from above to replace each tile with a see-through png
                for (gridBlock in gridArray) {
                    gridBlock.setImageResource(emptyBox)
                }

                // Make a text box appear notifying that the game is over
                Toast.makeText(this@MainActivity, "Game Over", Toast.LENGTH_SHORT).show()

            }
        }

        // Create listeners for each column in the 3rd row and also make the startButton start the game
        grid31.setOnClickListener { gameContinueCheck(1) }
        grid32.setOnClickListener { gameContinueCheck(2) }
        grid33.setOnClickListener { gameContinueCheck(3) }
        startButton.setOnClickListener { setUp() }
    }
        // This will call the initGame function which is the game setup.
        // This will also start the background music
        private fun setUp() {
            initGame()
            playSound()
        }

        // This will check each time a grid in the 3rd row is clicked
        private fun gameContinueCheck(placement : Int) {
            // If an object is in the grid being tapped, then continue the game
            if (objectLocationRow3 == placement) {
                continueGame()
            } else { // Else, end the game because you missed the tile
                endGame()
            }
        }

        // The continueGame function will advance each object down a row.
        // Tiles going off screen will be taken care of here by invoking the setObjectLocation function.
        private fun continueGame() {
            // object in row 4 goes to row 5
            objectLocationRow5 = objectLocationRow4
            setObjectLocation(objectLocationRow5, 5)

            // object in row 3 goes to row 4
            objectLocationRow4 = objectLocationRow3
            setObjectLocation(objectLocationRow4, 4)

            // object in row 2 goes to row 3
            objectLocationRow3 = objectLocationRow2
            setObjectLocation(objectLocationRow3, 3)

            // object in row 1 goes to row 2
            objectLocationRow2 = objectLocationRow1
            setObjectLocation(objectLocationRow2, 2)

            // new object is generated in a random column and goes to row 1
            objectLocationRow1 = r.nextInt(3) + 1
            setObjectLocation(objectLocationRow1, 1)

            // Increase score by one point and update the TextView
            currentScore++
            score.setText("SCORE: " + currentScore)
        }

        // The initGame function will setup the game once "Start Game" is pressed
        private fun initGame() {
            // Turn on functionality in the 3rd row and hide the start button
            grid31.setEnabled(true)
            grid32.setEnabled(true)
            grid33.setEnabled(true)
            startButton.setVisibility(View.INVISIBLE)

            // Set current score to 0 because game just started
            currentScore = 0
            score.setText("SCORE: " + currentScore)

            // Start the countdowntimer object
            timeCounter.start()

            // Start out Row 4 with an object which is already passed
            objectLocationRow4 = 2
            grid42.setImageResource(blackFrame)

            // Start Row 3 with an object to tap
            objectLocationRow3 = 2
            grid32.setImageResource(tap)

            // Select a random column to place another tile into row 2
            objectLocationRow2 = r.nextInt(3) + 1
            setObjectLocation(objectLocationRow2, 2)

            // Select a random column to place another tile into row 1
            objectLocationRow1 = r.nextInt(3) + 1
            setObjectLocation(objectLocationRow1, 1)

        }

        // If user loses the game, endGame will turn off everything
        private fun endGame() {
            // Cancel the countdowntimer object so it stops
            timeCounter.cancel()
            // Turn off the background music
            stopSound()
            // Turn off functionality to third row
            grid31.setEnabled(false)
            grid32.setEnabled(false)
            grid33.setEnabled(false)
            // Reveal the start button so user can start over
            startButton.setVisibility(View.VISIBLE)

            // Loop through gridArray and make every grid space a see-through png
            for (gridBlock in gridArray) {
                gridBlock.setImageResource(emptyBox)
            }

            // Inform user that they "failed" because they mis-clicked the tile
            Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
        }

        // This function will handle swapping each grid space's png.
        // This will make the tile prompt the user to tap it if it's in row 3 and
        // also make tiles return to a empty space.
        private fun setObjectLocation(place: Int, row: Int) {
            // If it's row 1, then set everything to an empty box
            if (row == 1) {
                grid11.setImageResource(emptyBox)
                grid12.setImageResource(emptyBox)
                grid13.setImageResource(emptyBox)

                // In the case that there is an object in one of these columns, make it appear
                when (place) {
                    1 -> grid11.setImageResource(frame)
                    2 -> grid12.setImageResource(frame)
                    3 -> grid13.setImageResource(frame)
                }
            }

            if (row == 2) {
                grid21.setImageResource(emptyBox)
                grid22.setImageResource(emptyBox)
                grid23.setImageResource(emptyBox)

                when (place) {
                    1 -> grid21.setImageResource(frame)
                    2 -> grid22.setImageResource(frame)
                    3 -> grid23.setImageResource(frame)
                }
            }

            if (row == 3) {
                grid31.setImageResource(emptyBox)
                grid32.setImageResource(emptyBox)
                grid33.setImageResource(emptyBox)

                // If there is a tile in the 3rd row, use the "tap" png to prompt the user to tap
                // this tile for points.
                when (place) {
                    1 -> grid31.setImageResource(tap)
                    2 -> grid32.setImageResource(tap)
                    3 -> grid33.setImageResource(tap)
                }
            }

            if (row == 4) {
                grid41.setImageResource(emptyBox)
                grid42.setImageResource(emptyBox)
                grid43.setImageResource(emptyBox)

                // If in the 4th row, make the tile black to show the user just tapped it.
                when (place) {
                    1 -> grid41.setImageResource(blackFrame)
                    2 -> grid42.setImageResource(blackFrame)
                    3 -> grid43.setImageResource(blackFrame)
                }
            }

            if (row == 5) {
                grid51.setImageResource(emptyBox)
                grid52.setImageResource(emptyBox)
                grid53.setImageResource(emptyBox)

                when (place) {
                    1 -> grid51.setImageResource(frame)
                    2 -> grid52.setImageResource(frame)
                    3 -> grid53.setImageResource(frame)
                }
            }
        }

        // As mentioned above, this function will convert the default milliseconds to seconds
        private fun milliToSecond(timeToConvert: Long): Long {
            return timeToConvert / 1000
        }

        // This "loads" the images by assigned each variable its corresponding drawable id
        private fun loadImages() {
            blackFrame = R.drawable.blackframe
            emptyBox = R.drawable.emptybox
            frame = R.drawable.frame
            tap = R.drawable.tap
        }

        // This will load a song into the mMediaPlayer and start it
        // The song is credited to pixelsphere.org and is called "Mysterious Ambience (song21)"
        private fun playSound() {
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.mystic)
                mMediaPlayer!!.isLooping = true
                mMediaPlayer!!.start()
            } else mMediaPlayer!!.start()
        }

        // This will check to see if the mMediaPlayer is running.
        // If it is, then stop it and turn off the media player.
        private fun stopSound() {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
}