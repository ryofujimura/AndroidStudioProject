// Ryo Fujimura

package com.zybooks.lightsout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children

const val GAME_STATE = "gameState"

class MainActivity : AppCompatActivity() {

    private lateinit var game: LightsOutGame
    private lateinit var lightGridLayout: GridLayout
    private var lightOnColor = 0
    private var lightOffColor = 0
    private var lightOnColorId = 0
//    private lateinit var game: LightsOutGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lightOnColorId = R.color.yellow
        lightGridLayout = findViewById(R.id.light_grid)

        // Add the same click handler to all grid buttons
        for (gridButton in lightGridLayout.children) {
            gridButton.setOnClickListener(this::onLightButtonClick)
        }

        lightOnColor = ContextCompat.getColor(this, R.color.yellow)
        lightOffColor = ContextCompat.getColor(this, R.color.blue_500)

        game = LightsOutGame()

        if (savedInstanceState == null) {
            startGame()
        } else {
            game.state = savedInstanceState.getString(GAME_STATE)!!
            setButtonColors()
        }

        // Find the button at row 0 and column 0
        val topLeftButton = lightGridLayout.getChildAt(0) as TextView

        // Add the long-click callback for the top-left button
        topLeftButton.setOnLongClickListener {
            game.turnOffAllLights()
            setButtonColors()
            true // Return true to indicate that the long-click event is consumed
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GAME_STATE, game.state)
    }

    private fun button(text: String, color: Int): TextView {
        val button = TextView(this)
        button.text = text
        button.setBackgroundColor(color)
        return button
    }

    private fun startGame() {
        game.newGame()
        setButtonColors()
    }

    private fun onLightButtonClick(view: View) {
        // Find the button's row and col
        val buttonIndex = lightGridLayout.indexOfChild(view)
        val row = buttonIndex / GRID_SIZE
        val col = buttonIndex % GRID_SIZE

        game.selectLight(row, col)
        setButtonColors()

        // Congratulate the user if the game is over
        if (game.isGameOver) {
            Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setButtonColors() {
        // Set all buttons' background color and visible text
        for (buttonIndex in 0 until lightGridLayout.childCount) {
            val gridButton = lightGridLayout.getChildAt(buttonIndex) as TextView

            // Find the button's row and col
            val row = buttonIndex / GRID_SIZE
            val col = buttonIndex % GRID_SIZE

            val isLightOn = game.isLightOn(row, col)
            gridButton.apply {
                setBackgroundColor(if (isLightOn) lightOnColor else lightOffColor)
                text = if (isLightOn) getString(R.string.button_on) else getString(R.string.button_off)
            }
        }
    }

    fun onNewGameClick(view: View) {
        startGame()
    }

    fun onHelpClick(view: View) {
        val intent = Intent(this, HelpActivity::class.java)
        startActivity(intent)
    }

    fun onChangeColorClick(view: View) {
        // Send the current color ID to ColorActivity
        val intent = Intent(this, ColorActivity::class.java)
        intent.putExtra(EXTRA_COLOR, lightOnColorId)
        colorResultLauncher.launch(intent)
    }

    private val colorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Create the "on" button color based on the chosen color ID from ColorActivity
            lightOnColorId = result.data!!.getIntExtra(EXTRA_COLOR, R.color.yellow)
            lightOnColor = ContextCompat.getColor(this, lightOnColorId)
            setButtonColors()
        }
    }
}
