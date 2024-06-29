package app.added.kannauj.activities

import app.added.kannauj.Adapters.ExamAdapter
import app.added.kannauj.Models.ExamModel
import app.added.kannauj.R
import app.added.kannauj.Utils.DatabaseHandler
import app.added.kannauj.Utils.DatabaseQuestions
import app.added.kannauj.Utils.Prefs
import app.added.kannauj.app.AppConfig
import app.added.kannauj.app.AppController
import app.added.kannauj.databinding.ActivityExamBinding
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class ExamActivity : AppCompatActivity() {
    lateinit var binding: ActivityExamBinding
    private val TAG = ExamActivity::class.simpleName
    lateinit var progressDialog: ProgressDialog
    lateinit var dbh: DatabaseHandler;
    var str: String = "1";
    lateinit var dbq: DatabaseQuestions;
    private val interval = 1000L // 1 Second
    private var totalSecs: Long = 0
    private val handler: Handler = Handler()
    var timerRunnable: Runnable = object : Runnable {
        override fun run() {
            dbh.setDuration(intent.getStringExtra("id"), "" + totalSecs)
            System.out.println("---- " + totalSecs)
            if (totalSecs != 0L) {
                totalSecs -= 1
                binding.tvTimer.text = getTime(totalSecs.toInt())
                handler.postDelayed(this, interval)
            } else {
                if (examAdapter != null) {
                    System.out.println("Animesh " + examAdapter.getAnswers());
                    val gson = Gson()
                    val json = gson.toJson(examAdapter.getAnswers())
                    Log.e("adil", json.toString())
                    submitExam(json)
                }
            }
        }
    }

    lateinit var examAdapter: ExamAdapter

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbh = DatabaseHandler(this)
        dbq = DatabaseQuestions(this)
        examAdapter = ExamAdapter(this, ArrayList(), intent.getStringExtra("id").toString())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam)
        binding.tvTimer.text = getTime(dbh.getDuration(intent.getStringExtra("id")).toInt())
        System.out.println("Animedsh Dur" + dbh.getDuration(intent.getStringExtra("id")))
        initialize()
    }

    private fun getTime(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val seconds = seconds % 60
        return "$hours : $minutes : $seconds"
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initialize() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        binding.buttonSubmit.setOnClickListener {
            if (examAdapter != null) {
                System.out.println("Animesh " + examAdapter.getAnswers());
                val gson = Gson()
                val json = gson.toJson(examAdapter.getAnswers())
                Log.e("adil", json.toString())
                submitExam(json)
            }
        }
        fetchExam()
    }

    private fun fetchExam() {
        val tag_string_req = "req_login"
        progressDialog.show()
        val strReq: StringRequest = object : StringRequest(Method.POST, AppConfig().URL_EXAM, Response.Listener { response ->
            progressDialog.dismiss()
            Log.e(TAG, "Response: $response")
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("error") == "0") {
                    str = "1";
                    binding.buttonSubmit.visibility = View.VISIBLE
                    val dataObject = jsonObject.getJSONObject("data")
                    val gson = Gson()
                    val model = gson.fromJson(dataObject.toString(), ExamModel::class.java)
                    totalSecs = dbh.getDuration(intent.getStringExtra("id")).toLong()
                    Log.e("adil", dbh.getDuration(intent.getStringExtra("id")))
                    handler.postDelayed(timerRunnable, interval)
                    examAdapter = ExamAdapter(this, model.list, intent.getStringExtra("id").toString())
                    dbq.setQuestions(model.list, intent.getStringExtra("id"))
                    binding.recyclerView.adapter = examAdapter
                } else {
                    str = "0";
                    binding.buttonSubmit.visibility = View.GONE
                    val msg = jsonObject.getString("message")
                    val snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            } catch (e: JSONException) {
                examAdapter = ExamAdapter(this, ArrayList(), intent.getStringExtra("id").toString())
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Log.e(TAG, "Error Response: $error")
            progressDialog.dismiss()
            examAdapter = ExamAdapter(this, ArrayList(), intent.getStringExtra("id").toString())
            val snackbar = Snackbar
                    .make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT)
            snackbar.show()
            showReloadAlert()
        }) {
            override fun getParams(): Map<String, String> {
                // Posting parameters to login url
                val params: MutableMap<String, String> = HashMap()
                params["Token"] = Prefs(this@ExamActivity).token
                params["StudentID"] = Prefs.with(this@ExamActivity).student[0]
                params["SubDomainName"] = Prefs.with(this@ExamActivity).domain
                params["TestID"] = intent.getStringExtra("id").toString()
                return params
            }
        }
        val appController = AppController()
        appController.setContext(this)
        appController.addToRequestQueue(strReq, tag_string_req)
    }

    private fun submitExam(json: String) {
        val tag_string_req = "req_login"
        progressDialog.show()
        val strReq: StringRequest = object : StringRequest(Method.POST,
                AppConfig().URL_EXAM_SUBMIT, Response.Listener { response ->
            progressDialog.dismiss()
            Log.e(TAG, "Response: $response")
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("error") == "0") {
                    val dataObject = jsonObject.getJSONObject("data")
                    val msg = dataObject.getString("Result")
                    val u = dataObject.getString("ViewTestResultURL")
                    dbh.setCompleted(intent.getStringExtra("id"), "1")
                    dbq.removeAllQuestions(intent.getStringExtra("id"))
                    dbh.removeItemFromPapers(intent.getStringExtra("id"))
                    val snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                    val intents = Intent(applicationContext, SolutionActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("URL", u)
                    bundle.putString("TOK", Prefs(this@ExamActivity).token)
                    bundle.putString("SID", Prefs(this@ExamActivity).student[0])
                    bundle.putString("PID", intent.getStringExtra("id"))
                    bundle.putString("SUB", Prefs.with(this@ExamActivity).domain)
                    System.out.println(Prefs(this@ExamActivity).token + "---" + Prefs(this@ExamActivity).student[0] + "---" + intent.getStringExtra("id") + "---" + Prefs.with(this@ExamActivity).domain)
                    intents.putExtras(bundle)
                    startActivity(intents)
                    finish()
                } else {
                    val msg = jsonObject.getString("message")
                    val snackbar = Snackbar.make(binding.parentLayout, msg, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Log.e(TAG, "Error Response: $error")
            progressDialog.dismiss()
            val snackbar = Snackbar.make(binding.parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT)
            snackbar.show()
            showSubmitAlert()
        }) {
            override fun getParams(): Map<String, String> {
                // Posting parameters to login url
                val params: MutableMap<String, String> = HashMap()
                params["Token"] = Prefs(this@ExamActivity).token
                params["StudentID"] = Prefs.with(this@ExamActivity).student[0]
                params["SubDomainName"] = Prefs.with(this@ExamActivity).domain
                params["TestID"] = intent.getStringExtra("id").toString()
                params["AnswersList"] = json
                //Log.e("adil",params.toString())
                return params
            }
        }

        // Adding request to request queue
        val appController = AppController()
        appController.setContext(this)
        appController.addToRequestQueue(strReq, tag_string_req)
    }

    private fun showReloadAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Online Exam")
        builder.setMessage("Connectivity Error")
        builder.setIcon(R.drawable.receipt)
        builder.setPositiveButton("Reload") { dialog, id ->
            fetchExam()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }

    private fun showSubmitAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Online Exam")
        builder.setMessage("Sure to submit")
        builder.setIcon(R.drawable.exam)
        builder.setPositiveButton("Yes") { dialog, id ->
            if (examAdapter != null) {
                System.out.println("Animesh " + examAdapter.getAnswers());
                val gson = Gson()
                val json = gson.toJson(examAdapter.getAnswers())
                Log.e("adil", json.toString())
                submitExam(json)
            } else {
                super.onBackPressed()
            }
        }
        builder.setNegativeButton("No") { dialog, id ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    override fun onBackPressed() {
        if (str.equals("0")) {
            super.onBackPressed()
        } else {
            showSubmitAlert()
        }
    }

}