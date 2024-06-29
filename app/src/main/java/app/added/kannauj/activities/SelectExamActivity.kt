package app.added.kannauj.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.added.kannauj.Fragments.SelectExamFragment
import app.added.kannauj.Fragments.SelectExamTypeFragment
import app.added.kannauj.R
import app.added.kannauj.databinding.ActivitySelectExamBinding


class SelectExamnationActivity: AppCompatActivity() {

    lateinit var binding: ActivitySelectExamBinding
    lateinit var broadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_exam)
        initialize()
        addExamTypeFragment()
    }

    private fun initialize() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getStringExtra("id")
                addExamFragment(id!!)
            }
        }
    }

    private fun addExamTypeFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(binding.containerLayout.id
                ,SelectExamTypeFragment()
                ,"select_exam_type_fragment")
                .addToBackStack("fragment1")
                .commit()
    }

    private fun addExamFragment(id: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("id",id)
        val fragment = SelectExamFragment()
        fragment.arguments = bundle
        fragmentTransaction.add(binding.containerLayout.id
                , fragment
                ,"select_exam_fragment")
                .addToBackStack("fragment2")
                .commit()
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, IntentFilter("app.parent.fragment1"))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount
        if (count==0)
            finish()
    }

}