package com.example.liverinsight.features.home.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.ml.modeldownloader.DownloadType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.File

data class HomeUiState(
    val age: String = "",
    val gender: String = "",
    val totalBilirubin: String = "",
    val directBilirubin: String = "",
    val alkalinePhosphotase: String = "",
    val sgpt: String = "",
    val sgot: String = "",
    val totalProtein: String = "",
    val albumin: String = "",
    val albuminGlobulinRatio: String = "",
    val predictionResult: String? = null,
    val errorMessage: String? = null,
    val modelStatus: String = "Model not ready"
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private var interpreter: Interpreter? = null
    private var isModelReady = false

    // Nilai rata-rata dan deviasi standar untuk normalisasi
    private val means = floatArrayOf(
        44.74614065180103f,
        0.7564322469982847f,
        3.298799313893653f,
        1.486106346483705f,
        290.57632933104634f,
        80.71355060034305f,
        109.91080617495712f,
        6.483190394511149f,
        3.141852487135506f,
        0.9470639032815198f
    )

    private val stdDevs = floatArrayOf(
        16.175942411270466f,
        0.429234787382629f,
        6.204193950230359f,
        2.806087923849646f,
        242.72954813780882f,
        182.46366758318712f,
        288.67063666027326f,
        1.0845201655473806f,
        0.7948362500202801f,
        0.3182186930338783f
    )

    init {
        initializeModel()
    }

    // Mengunduh model dengan FirebaseModelDownloader
    private fun initializeModel() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(modelStatus = "Loading model...")

            try {
                val conditions = CustomModelDownloadConditions.Builder().build()

                FirebaseModelDownloader.getInstance()
                    .getModel("liver-detection2", DownloadType.LOCAL_MODEL, conditions)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("HomeViewModel", "Model download successful.")
                            val modelFile = task.result?.file
                            modelFile?.let {
                                try {
                                    val model = loadModel(it)
                                    interpreter = Interpreter(model)
                                    isModelReady = true
                                    _uiState.value = _uiState.value.copy(modelStatus = "Model ready")
                                    Log.d("HomeViewModel", "Model loaded and ready.")
                                } catch (e: Exception) {
                                    Log.e("HomeViewModel", "Error loading model file: ${e.message}", e)
                                    _uiState.value = _uiState.value.copy(modelStatus = "Model loading failed")
                                }
                            } ?: run {
                                Log.e("HomeViewModel", "Model file not found after download.")
                                _uiState.value = _uiState.value.copy(modelStatus = "Model not found")
                            }
                        } else {
                            Log.e("HomeViewModel", "Model download failed.", task.exception)
                            _uiState.value = _uiState.value.copy(
                                modelStatus = "Model download failed: ${task.exception?.message}"
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Unexpected error during model initialization: ${e.message}", e)
                _uiState.value = _uiState.value.copy(modelStatus = "Model loading failed: ${e.message}")
            }
        }
    }

    // Fungsi untuk memuat model TensorFlow Lite dari file lokal
    private fun loadModel(file: File): MappedByteBuffer {
        val fileInputStream = FileInputStream(file)
        val fileChannel = fileInputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size())
    }

    // Fungsi prediksi penyakit liver dengan model TFLite
    fun predictDisease() {
        viewModelScope.launch {
            if (!isModelReady) {
                Log.e("HomeViewModel", "Model is not ready yet")
                _uiState.value = _uiState.value.copy(errorMessage = "Model is not ready yet")
                return@launch
            }

            try {
                // Validasi dan persiapkan input data
                val inputData = prepareInputData()

                // Prediksi
                val outputData = Array(1) { FloatArray(1) }
                interpreter?.run(inputData, outputData)

                // Log untuk output
                Log.d("ModelOutputShape", "Predicted output structure: ${outputData[0].contentToString()}")

                // Proses output untuk menentukan hasil prediksi
                val prediction = if (outputData[0][0] >= 0.398f) "High Risk" else "Low Risk"
                _uiState.value = _uiState.value.copy(predictionResult = prediction, errorMessage = null)
            } catch (e: IllegalArgumentException) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "Error during prediction: ${e.message}")
            }
        }
    }

    // Fungsi untuk mempersiapkan data input dari UI state dan normalisasi
    private fun prepareInputData(): Array<FloatArray> {
        val inputData = Array(1) { FloatArray(10) }

        inputData[0] = floatArrayOf(
            (_uiState.value.age.toFloatOrNull() ?: 0f),
            (_uiState.value.gender.toFloatOrNull() ?: 0f),
            (_uiState.value.totalBilirubin.toFloatOrNull() ?: 0f),
            (_uiState.value.directBilirubin.toFloatOrNull() ?: 0f),
            (_uiState.value.alkalinePhosphotase.toFloatOrNull() ?: 0f),
            (_uiState.value.sgpt.toFloatOrNull() ?: 0f),
            (_uiState.value.sgot.toFloatOrNull() ?: 0f),
            (_uiState.value.totalProtein.toFloatOrNull() ?: 0f),
            (_uiState.value.albumin.toFloatOrNull() ?: 0f),
            (_uiState.value.albuminGlobulinRatio.toFloatOrNull() ?: 0f)
        )

        // Normalisasi menggunakan rata-rata dan deviasi standar
        for (i in inputData[0].indices) {
            inputData[0][i] = (inputData[0][i] - means[i]) / stdDevs[i]
        }

        return inputData
    }

    // Fungsi setter untuk input parameter
    fun onAgeChange(newValue: String) {
        _uiState.value = _uiState.value.copy(age = newValue)
    }

    fun onGenderChange(newValue: String) {
        _uiState.value = _uiState.value.copy(gender = newValue)
    }

    fun onTotalBilirubinChange(newValue: String) {
        _uiState.value = _uiState.value.copy(totalBilirubin = newValue)
    }

    fun onDirectBilirubinChange(newValue: String) {
        _uiState.value = _uiState.value.copy(directBilirubin = newValue)
    }

    fun onAlkalinePhosphotaseChange(newValue: String) {
        _uiState.value = _uiState.value.copy(alkalinePhosphotase = newValue)
    }

    fun onSgptChange(newValue: String) {
        _uiState.value = _uiState.value.copy(sgpt = newValue)
    }

    fun onSgotChange(newValue: String) {
        _uiState.value = _uiState.value.copy(sgot = newValue)
    }

    fun onTotalProteinChange(newValue: String) {
        _uiState.value = _uiState.value.copy(totalProtein = newValue)
    }

    fun onAlbuminChange(newValue: String) {
        _uiState.value = _uiState.value.copy(albumin = newValue)
    }

    fun onAlbuminGlobulinRatioChange(newValue: String) {
        _uiState.value = _uiState.value.copy(albuminGlobulinRatio = newValue)
    }
}
