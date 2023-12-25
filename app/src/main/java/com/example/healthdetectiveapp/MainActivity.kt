package com.example.healthdetectiveapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.healthdetectiveapp.databinding.ActivityMainBinding
import com.example.healthdetectiveapp.fragment.BMICalculatorFragment
import com.example.healthdetectiveapp.fragment.DieasesInformationFragment
import com.example.healthdetectiveapp.fragment.DoctorAppointmentFragment
import com.example.healthdetectiveapp.fragment.Appointment_Status_Fragment
import com.example.healthdetectiveapp.fragment.HomeFragment
import com.example.healthdetectiveapp.fragment.ProfileFragment
import com.example.healthdetectiveapp.fragment.UploadDataFragment
import com.example.healthdetectiveapp.model.UserModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var request:String?=null
    private var dieasesLabel: String?=null
    private val database= FirebaseDatabase.getInstance()
    private val auth= FirebaseAuth.getInstance()
    private lateinit var profile:CircleImageView
    private lateinit var welcomeMsg:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //binding.drawer.setScrimColor(getColor(R.color.skyblue9))
        val headerView = binding.navigationView.getHeaderView(0)
        profile = headerView.findViewById<CircleImageView>(R.id.profilepalceholder)
        welcomeMsg = headerView.findViewById(R.id.txt_welcome)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        val tv = TypedValue()
        if (this.theme.resolveAttribute(com.google.android.material.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            val marginHeight = getStatusBarHeight() + actionBarHeight
            val param = binding.navigationView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,marginHeight,0,0)
            binding.navigationView.layoutParams = param
        }

        // Fetch User Profile Data
        //fetchUserProfileData()
        // Load Fragment After Request
        request = intent.getStringExtra("OpenScreen")
        dieasesLabel = intent.getStringExtra("dieases")
        Log.d("Main_dieasesLabel","$dieasesLabel")
        when(request){
            "doctorAppointment" ->{
                loadFragment(DoctorAppointmentFragment(),false)
            }
            else ->{loadFragment(HomeFragment(),true)}
        }
        setSupportActionBar(toolbar) // set custom toolbar
        // Ham Burger toggle button on toolbar
        val toggle = ActionBarDrawerToggle(this,binding.drawer,toolbar,R.string.openDrawer,R.string.closeDrawer)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        /*if(savedInstanceState == null){
            loadFragment(HomeFragment())
        }*/

        binding.navigationView.setNavigationItemSelectedListener{
            it.isChecked = true
            when(it.itemId){
                R.id.home ->{
                    loadFragment(HomeFragment(),true)
                    true
                }
                R.id.Dieasesinfo ->{
                    loadFragment(DieasesInformationFragment(),false)
                    true
                }
                R.id.appointment ->{
                    loadFragment(DoctorAppointmentFragment(),false)
                    true
                }
                R.id.bmi ->{
                    loadFragment(BMICalculatorFragment(),false)
                    true
                }
                R.id.appointstatus ->{
                    loadFragment(Appointment_Status_Fragment(),false)
                    true
                }
                R.id.savepatientsdata ->{
                    loadFragment(UploadDataFragment(),false)
                    true
                }

            }
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }
        profile.setOnClickListener {
            loadFragment(ProfileFragment(),false)
            binding.drawer.closeDrawer(GravityCompat.START)
        }
    }

    private fun fetchUserProfileData() {
        val userId = auth.currentUser?.uid
        val userRef = database.getReference("UsersProfile").child(userId!!)
        userRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(UserModel::class.java)
                    if (user!=null){
                        // Set Data On Header
                        welcomeMsg.setText("Hi ${user.name}")
                        Glide.with(this@MainActivity)
                            .load(Uri.parse(user.userimage))
                            .placeholder(R.drawable.account)
                            .error(R.drawable.account)
                            .into(profile)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun loadFragment(fragment:Fragment,flag:Boolean){
        val fm = supportFragmentManager
        val tr = fm.beginTransaction()
        val bundle = Bundle()
        Log.d("dieasesInMainActivity", "$dieasesLabel")
        bundle.putString("dieases",dieasesLabel)
        fragment.arguments = bundle
        /*if (){
            Toast.makeText(this, "equal", Toast.LENGTH_SHORT).show()
        }*/
        if (flag==true){
            tr.add(R.id.container,fragment)
            fm.popBackStack("root_fragment",FragmentManager.POP_BACK_STACK_INCLUSIVE)
            tr.addToBackStack("root_fragment")
        }
        else{
            tr.replace(R.id.container, fragment)
            tr.addToBackStack(null)
        }

        tr.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
        }
        else if (fragment is HomeFragment){
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            },1000)
        }
        else{
            super.onBackPressed()
        }
    }

    fun getStatusBarHeight():Int{
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height","dimen","android")
        if (resourceId>0){
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}