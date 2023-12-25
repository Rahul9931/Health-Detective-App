package com.example.healthdetectiveapp

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.example.healthdetectiveapp.ml.AlzimerTfmodel
import com.example.healthdetectiveapp.ml.BrainTumor10Epochs
import com.example.healthdetectiveapp.ml.LungsCancertf
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class Classifier(
    val context:Context,
    val bitmap: Bitmap
) {
    lateinit var status:String
// Brain Tumar Prediction
    fun brainTumar():String{
        val resized: Bitmap = Bitmap.createScaledBitmap(bitmap,64,64,true)
        val model = BrainTumor10Epochs.newInstance(context)

        val tensorimg = TensorImage(DataType.FLOAT32)
        tensorimg.load(resized)
        val byteBuffer = tensorimg.buffer

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 64, 64, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var value = outputFeature0.getIntValue(0)
        if (value==1){
            status = "Brain Tumor H"
        } else{
            status = "Brain Tumor Nahi H"
        }
    // Releases model resources if no longer used.
        model.close()
        return status
    }

// Alzimer Detection Model
    fun alzimerDetection():String{
        val resized:Bitmap = Bitmap.createScaledBitmap(bitmap,128, 128,true)
        val model = AlzimerTfmodel.newInstance(context)

        val tensorimg = TensorImage(DataType.FLOAT32)
        tensorimg.load(resized)
        val byteBuffer = tensorimg.buffer

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        var max = getMax(outputFeature0.floatArray)
        when(max){
            0->{status="MildDemented"}
            1->{status="ModerateDemented"}
            2->{status="NonDemented"}
            else->{status="VeryMildDemented"}
        }

        // Releases model resources if no longer used.
        model.close()
    return status
}

    // Lungs Cancer Detection
    fun lungsCancerDetection():String{
        val resized:Bitmap = Bitmap.createScaledBitmap(bitmap,128, 128,true)
        val model = LungsCancertf.newInstance(context)

        val tensorimg = TensorImage(DataType.FLOAT32)
        tensorimg.load(resized)
        val byteBuffer = tensorimg.buffer

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var max = getMax(outputFeature0.floatArray)
        when(max){
            0->{status="Benign Case"}
            1->{status="Malignant Case"}
            else->{status="Normal Case"}
        }
        // Releases model resources if no longer used.
        model.close()
        return status
    }
    fun getMax(arr:FloatArray) : Int{
        var ind = 0
        var max = 0.0f
        for(i in 0..arr.size-1){
            if(arr[i]>max){
                ind = i
                max = arr[i]
            }
        }
        return ind
    }
}