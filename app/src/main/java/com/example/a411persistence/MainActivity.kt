package com.example.a411persistence

import android.os.Bundle
import android.widget.SeekBar
import android.widget.FrameLayout
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class MainActivity : AppCompatActivity() {

    private val colorViewModel: ColorViewModel by viewModels()

    private lateinit var colorPreview: FrameLayout
    private lateinit var redSlider: SeekBar
    private lateinit var greenSlider: SeekBar
    private lateinit var blueSlider: SeekBar
    private lateinit var redSwitch: SwitchCompat
    private lateinit var greenSwitch: SwitchCompat
    private lateinit var blueSwitch: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorPreview = findViewById(R.id.colorPreview)
        redSlider = findViewById(R.id.redSlider)
        greenSlider = findViewById(R.id.greenSlider)
        blueSlider = findViewById(R.id.blueSlider)
        redSwitch = findViewById(R.id.redSwitch)
        greenSwitch = findViewById(R.id.greenSwitch)
        blueSwitch = findViewById(R.id.blueSwitch)
        val resetButton = findViewById<Button>(R.id.resetButton)

        // Handle configuration changes
        savedInstanceState?.let {
            val savedColor = it.getInt("currentColor", 0xff000000.toInt())
            colorViewModel.setCurrentColor(savedColor)
            updateColorPreview()
        }

        // Observe changes in color from ViewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                colorViewModel.getCurrentColorFlow().collect { color ->
                    updateColorPreview()
                }
            }
        }

        redSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateColorPreview()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle touch events
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle release events
            }
        })

        blueSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateColorPreview()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle touch events
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle release events
            }
        })

        greenSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    updateColorPreview()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle touch events
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle release events
            }
        })

        redSwitch.setOnCheckedChangeListener { _, _ ->
            updateColorPreview()
        }

        greenSwitch.setOnCheckedChangeListener { _, _ ->
            updateColorPreview()
        }

        blueSwitch.setOnCheckedChangeListener { _, _ ->
            updateColorPreview()
        }

        resetButton.setOnClickListener {
            resetUI()
        }
    }

    private fun updateColorPreview() {
        val redValue = redSlider.progress
        val greenValue = greenSlider.progress
        val blueValue = blueSlider.progress
        val isRedChecked = redSwitch.isChecked
        val isGreenChecked = greenSwitch.isChecked
        val isBlueChecked = blueSwitch.isChecked
        val color = calculateColor(redValue, greenValue, blueValue, isRedChecked, isGreenChecked, isBlueChecked)
        colorPreview.setBackgroundColor(color)
        colorViewModel.setCurrentColor(color)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentColor", colorViewModel.getCurrentColor())
    }

    private fun resetUI() {
        redSlider.progress = 0
        greenSlider.progress = 0
        blueSlider.progress = 0
        redSwitch.isChecked = false
        greenSwitch.isChecked = false
        blueSwitch.isChecked = false
    }

    private fun calculateColor(red: Int, green: Int, blue: Int, isRedEnabled: Boolean, isGreenEnabled: Boolean, isBlueEnabled: Boolean): Int {
        val calculatedRed = if (isRedEnabled) red else 0
        val calculatedGreen = if (isGreenEnabled) green else 0
        val calculatedBlue = if (isBlueEnabled) blue else 0
        return 0xff000000.toInt() or (calculatedRed shl 16) or (calculatedGreen shl 8) or calculatedBlue
    }
}

class ColorViewModel : ViewModel() {
    private val currentColor: MutableStateFlow<Int> = MutableStateFlow(0xff000000.toInt())

    fun setCurrentColor(color: Int) {
        currentColor.value = color
    }

    fun getCurrentColor(): Int {
        return currentColor.value
    }

    fun getCurrentColorFlow() = currentColor
}

