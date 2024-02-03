package com.example.healthdetectiveapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthdetectiveapp.adapter.DieasesAccuracyAdapter
import com.example.healthdetectiveapp.adapter.SymptomsAdapter
import com.example.healthdetectiveapp.databinding.ActivityDieasesDetectionBinding
import com.example.healthdetectiveapp.ml.Dieasesinfloat
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Dieases_Detection_Activity : AppCompatActivity() {
    private val binding:ActivityDieasesDetectionBinding by lazy {
        ActivityDieasesDetectionBinding.inflate(layoutInflater)
    }
    val symList = mutableListOf("itching", "skin_rash", "nodal_skin_eruptions", "continuous_sneezing", "shivering", "chills", "joint_pain", "stomach_pain", "acidity", "ulcers_on_tongue", "muscle_wasting", "vomiting", "burning_micturition", "spotting_ urination", "fatigue", "weight_gain", "anxiety", "cold_hands_and_feets", "mood_swings", "weight_loss", "restlessness", "lethargy", "patches_in_throat", "irregular_sugar_level", "cough", "high_fever", "sunken_eyes", "breathlessness", "sweating", "dehydration", "indigestion", "headache", "yellowish_skin", "dark_urine", "nausea", "loss_of_appetite", "pain_behind_the_eyes", "back_pain", "constipation", "abdominal_pain", "diarrhoea", "mild_fever", "yellow_urine", "yellowing_of_eyes", "acute_liver_failure", "fluid_overload", "swelling_of_stomach", "swelled_lymph_nodes", "malaise", "blurred_and_distorted_vision", "phlegm", "throat_irritation", "redness_of_eyes", "sinus_pressure", "runny_nose", "congestion", "chest_pain", "weakness_in_limbs", "fast_heart_rate", "pain_during_bowel_movements", "pain_in_anal_region", "bloody_stool", "irritation_in_anus", "neck_pain", "dizziness", "cramps", "bruising", "obesity", "swollen_legs", "swollen_blood_vessels", "puffy_face_and_eyes", "enlarged_thyroid", "brittle_nails", "swollen_extremeties", "excessive_hunger", "extra_marital_contacts", "drying_and_tingling_lips", "slurred_speech", "knee_pain", "hip_joint_pain", "muscle_weakness", "stiff_neck", "swelling_joints", "movement_stiffness", "spinning_movements", "loss_of_balance", "unsteadiness", "weakness_of_one_body_side", "loss_of_smell", "bladder_discomfort", "foul_smell_of urine", "continuous_feel_of_urine", "passage_of_gases", "internal_itching", "toxic_look_(typhos)", "depression", "irritability", "muscle_pain", "altered_sensorium", "red_spots_over_body", "belly_pain", "abnormal_menstruation", "dischromic _patches", "watering_from_eyes", "increased_appetite", "polyuria", "family_history", "mucoid_sputum", "rusty_sputum", "lack_of_concentration", "visual_disturbances", "receiving_blood_transfusion", "receiving_unsterile_injections", "coma", "stomach_bleeding", "distention_of_abdomen", "history_of_alcohol_consumption", "fluid_overload.1", "blood_in_sputum", "prominent_veins_on_calf", "palpitations", "painful_walking", "pus_filled_pimples", "blackheads", "scurring", "skin_peeling", "silver_like_dusting", "small_dents_in_nails", "inflammatory_nails", "blister", "red_sore_around_nose", "yellow_crust_ooze")
    val symListInFloat = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    val usersymList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnResult.isEnabled = false
        // Fetching dieases from dieaseslabels.txt
        val fileName = "dieaseslabels.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        var finalDieases = inputString.split("\n")

        // Set AutocompleteTextView
        val autocompleteAdapter = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,symList)
        val autoComplete = binding.autoComplete
        autoComplete.setAdapter(autocompleteAdapter)

        // Set Symptoms Buttom
        binding.btnSymptoms.setOnClickListener {
            usersymList.clear()
            val symptoms = autoComplete.text.toString().trim()
            if (symptoms.equals("")){
                Toast.makeText(this, "Please Select Symptoms", Toast.LENGTH_SHORT).show()
            } else{
                usersymList.add(symptoms)
                setAdapter()
                autoComplete.text.clear()
                binding.btnResult.isEnabled = true
            }
        }

        // Set Show Result Button
        binding.btnResult.setOnClickListener {
            if (usersymList.isNotEmpty()){
                // Creating Symptoms List
                for (k in 0..symList.size-1){
                    for( z in usersymList){
                        if(symList[k].equals(z)){
                            symListInFloat[k] = 1
                        }
                    }
                }
                // Put Symptoms List into byteBuffer
                var byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(symListInFloat.size*4)
                byteBuffer.order(ByteOrder.nativeOrder())
                for (i in symListInFloat){
                    byteBuffer.putFloat(i.toFloat())
                }

                // Create Object of Dieasesinfloat TFLite Model
                val model = Dieasesinfloat.newInstance(this)

                // Creates inputs for reference.
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 132), DataType.FLOAT32)
                inputFeature0.loadBuffer(byteBuffer)

                // Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                val mapKey = outputFeature0.map { "%.2f".format(it*100).toFloat() }
                Log.d("Dieases_mapkey","$mapKey")
                // Create mapof
                val mapResult = mapKey.zip(finalDieases).toMap()
                // sorting keys and values
                val sortedKeys = mapResult.keys.sortedDescending()
                val sortedValues = mutableListOf<String>()
                sortedKeys.forEach {key->
                    sortedValues.add(mapResult[key]!!)
                }
                Log.d("Dieases_sortedkey","$sortedKeys")
                Log.d("Dieases_sortedvalue","$sortedValues")

                setAdapterForDieasesAccuracy(sortedKeys,sortedValues)

                // Releases model resources if no longer used.
                model.close()
                for (zero in 0..symListInFloat.size-1){
                    symListInFloat[zero] = 0
                }
            }
            else{
                Toast.makeText(this, "Please Select Symptoms", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAdapterForDieasesAccuracy(sortedKey:List<Float>,sortedValue:MutableList<String>) {
        binding.rvDieasesAccuracy.layoutManager = LinearLayoutManager(this)
        val dieasesAccuracyAdapter = DieasesAccuracyAdapter(this,sortedKey,sortedValue)
        binding.rvDieasesAccuracy.adapter = dieasesAccuracyAdapter
    }

    private fun setAdapter() {
        val rvSymptoms = binding.rvSymptoms
        rvSymptoms.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val symptomsAdapter = SymptomsAdapter(this,usersymList)
        rvSymptoms.adapter = symptomsAdapter
        symptomsAdapter.notifyDataSetChanged()
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