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
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import android.content.Context


class MainActivity : AppCompatActivity() {

    private val colorViewModel: ColorViewModel by viewModels()

    private lateinit var colorPreview: FrameLayout
    private lateinit var redSlider: SeekBar
    private lateinit var greenSlider: SeekBar
    private lateinit var blueSlider: SeekBar
    private lateinit var redSwitch: SwitchCompat
    private lateinit var greenSwitch: SwitchCompat
    private lateinit var blueSwitch: SwitchCompat
    private lateinit var redEditText: EditText
    private lateinit var greenEditText: EditText
    private lateinit var blueEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get references to your SwitchCompat views
        redSwitch = findViewById(R.id.redSwitch)
        greenSwitch = findViewById(R.id.greenSwitch)
        blueSwitch = findViewById(R.id.blueSwitch)

        redSwitch.trackDrawable = ContextCompat.getDrawable(this, R.drawable.switch_track_custom)
        greenSwitch.trackDrawable = ContextCompat.getDrawable(this, R.drawable.switch_track_custom)
        blueSwitch.trackDrawable = ContextCompat.getDrawable(this, R.drawable.switch_track_custom)


// Set custom track and thumb drawables
        redSwitch.setTrackResource(R.drawable.switch_track_custom)
        redSwitch.setThumbResource(R.drawable.switch_thumb_custom)

        greenSwitch.setTrackResource(R.drawable.switch_track_custom)
        greenSwitch.setThumbResource(R.drawable.switch_thumb_custom)

        blueSwitch.setTrackResource(R.drawable.switch_track_custom)
        blueSwitch.setThumbResource(R.drawable.switch_thumb_custom)



        colorPreview = findViewById(R.id.colorPreview)
        redSlider = findViewById(R.id.redSlider)
        greenSlider = findViewById(R.id.greenSlider)
        blueSlider = findViewById(R.id.blueSlider)
        redSlider.max = 255
        greenSlider.max = 255
        blueSlider.max = 255
        val resetButton = findViewById<Button>(R.id.resetButton)

        redEditText = findViewById(R.id.redEditText)
        greenEditText = findViewById(R.id.greenEditText)
        blueEditText = findViewById(R.id.blueEditText)

        setupTextChangeListeners()

        // Add TextChangedListeners to EditTexts for immediate updates

        redEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateColorPreview()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        greenEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateColorPreview()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        blueEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateColorPreview()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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

        loadSavedColorValues(applicationContext)

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
        val redValue = redSlider.progress / 255f
        val greenValue = greenSlider.progress / 255f
        val blueValue = blueSlider.progress / 255f

        val isRedChecked = redSwitch.isChecked
        val isGreenChecked = greenSwitch.isChecked
        val isBlueChecked = blueSwitch.isChecked

        if (redSwitch.isChecked && redEditText.text.toString() != String.format("%.2f", redValue)) {
            redEditText.setText(String.format("%.2f", redValue))
        }
        if (greenSwitch.isChecked && greenEditText.text.toString() != String.format("%.2f", greenValue)) {
            greenEditText.setText(String.format("%.2f", greenValue))
        }
        if (blueSwitch.isChecked && blueEditText.text.toString() != String.format("%.2f", blueValue)) {
            blueEditText.setText(String.format("%.2f", blueValue))
        }

        val color = calculateColor(
            redValue,
            greenValue,
            blueValue,
            isRedChecked,
            isGreenChecked,
            isBlueChecked
        )

        colorPreview.setBackgroundColor(color)
        colorViewModel.setCurrentColor(color)
    }





    // Set up TextChangedListeners for EditText fields
    private fun setupTextChangeListeners() {
        val textChangeWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateColorPreview()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        redEditText.addTextChangedListener(textChangeWatcher)
        greenEditText.addTextChangedListener(textChangeWatcher)
        blueEditText.addTextChangedListener(textChangeWatcher)
    }




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentColor", colorViewModel.getCurrentColor())
    }

    override fun onDestroy() {
        super.onDestroy()
        saveColorValues(applicationContext)
    }

    private fun resetUI() {
        redSlider.progress = 0
        greenSlider.progress = 0
        blueSlider.progress = 0
        redSwitch.isChecked = false
        greenSwitch.isChecked = false
        blueSwitch.isChecked = false
    }

    private fun calculateColor(
        redValue: Float,
        greenValue: Float,
        blueValue: Float,
        isRedEnabled: Boolean,
        isGreenEnabled: Boolean,
        isBlueEnabled: Boolean
    ): Int {
        val calculatedRed = if (isRedEnabled) (redValue * 255f).toInt() else 0
        val calculatedGreen = if (isGreenEnabled) (greenValue * 255f).toInt() else 0
        val calculatedBlue = if (isBlueEnabled) (blueValue * 255f).toInt() else 0

        return 0xff000000.toInt() or (calculatedRed shl 16) or (calculatedGreen shl 8) or calculatedBlue
    }

    private fun saveColorValues(context: Context) {
        val sharedPreferences = context.getSharedPreferences("ColorValues", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save the values
        editor.putFloat("redValue", redSlider.progress / 255f)
        editor.putFloat("greenValue", greenSlider.progress / 255f)
        editor.putFloat("blueValue", blueSlider.progress / 255f)
        editor.putBoolean("redSwitch", redSwitch.isChecked)
        editor.putBoolean("greenSwitch", greenSwitch.isChecked)
        editor.putBoolean("blueSwitch", blueSwitch.isChecked)

        editor.apply()
    }

    private fun loadSavedColorValues(context: Context) {
        val sharedPreferences = context.getSharedPreferences("ColorValues", Context.MODE_PRIVATE)

        // Load the values or use default values if they don't exist
        val redValue = sharedPreferences.getFloat("redValue", 0f)
        val greenValue = sharedPreferences.getFloat("greenValue", 0f)
        val blueValue = sharedPreferences.getFloat("blueValue", 0f)
        val isRedChecked = sharedPreferences.getBoolean("redSwitch", false)
        val isGreenChecked = sharedPreferences.getBoolean("greenSwitch", false)
        val isBlueChecked = sharedPreferences.getBoolean("blueSwitch", false)

        // Update UI components with loaded values
        redSlider.progress = (redValue * 255f).toInt()
        greenSlider.progress = (greenValue * 255f).toInt()
        blueSlider.progress = (blueValue * 255f).toInt()
        redSwitch.isChecked = isRedChecked
        greenSwitch.isChecked = isGreenChecked
        blueSwitch.isChecked = isBlueChecked
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