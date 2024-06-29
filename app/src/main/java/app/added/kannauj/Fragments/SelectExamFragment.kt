package app.added.kannauj.Fragments

import app.added.kannauj.Adapters.SelectExamTypeAdapter
import app.added.kannauj.Models.ExamDataModel
import app.added.kannauj.R
import app.added.kannauj.Utils.DatabaseHandler
import app.added.kannauj.Utils.Prefs
import app.added.kannauj.app.AppConfig
import app.added.kannauj.app.AppController
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SelectExamFragment : Fragment() {

    private val TAG = SelectExamFragment::class.simpleName
    private lateinit var fragmentView: View
    lateinit var parentLayout: ConstraintLayout
    lateinit var recyclerView: RecyclerView
    lateinit var progressDialog: ProgressDialog
    lateinit var dbh: DatabaseHandler;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_select_exam_type
                , container, false)
        dbh = DatabaseHandler(activity);
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        recyclerView = fragmentView.findViewById(R.id.recyclerView)
        parentLayout = fragmentView.findViewById(R.id.parentLayout)
        progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        fetchFeeReceipt()
    }

    private fun fetchFeeReceipt() {
        val tag_string_req = "req_login"
        progressDialog.show()
        val strReq: StringRequest = object : StringRequest(Method.POST,
                AppConfig().SELECT_EXAM, Response.Listener { response ->
            progressDialog.dismiss()
            Log.e(TAG, "Response: $response")
            try {
                val jsonObject = JSONObject(response)
                if (jsonObject.getString("error") == "0") {
                    val dataObject = jsonObject.getJSONObject("data")
                    val gson = Gson()
                    val model = gson.fromJson(dataObject.toString()
                            , ExamDataModel::class.java)
                    Log.e("adil", model.list.size.toString())
                    dbh.setPapers(model.list)
                    recyclerView.adapter = context?.let { SelectExamTypeAdapter(it, null, model.list) }
                } else {
                    val msg = jsonObject.getString("message")
                    val snackbar = Snackbar
                            .make(parentLayout, msg, Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error ->
            Log.e(TAG, "Error Response: $error")
            progressDialog.dismiss()
            val snackbar = Snackbar
                    .make(parentLayout, "Connectivity Error", Snackbar.LENGTH_SHORT)
            snackbar.show()
            showReloadAlert()
        }) {
            override fun getParams(): Map<String, String> {
                // Posting parameters to login url
                val params: MutableMap<String, String> = HashMap()
                params["Token"] = Prefs(context).token
                params["StudentID"] = Prefs.with(context).student[0]
                params["SubDomainName"] = Prefs.with(context).domain
                params["TestTypeID"] = arguments!!.getString("id")!!
                return params
            }
        }

        // Adding request to request queue
        val appController = AppController()
        appController.setContext(context)
        appController.addToRequestQueue(strReq, tag_string_req)
    }

    private fun showReloadAlert() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Online Exam")
        builder.setMessage("Connectivity Error")
        builder.setIcon(R.drawable.exam)
        builder.setPositiveButton("Reload") { dialog, id ->
            fetchFeeReceipt()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
    }

}