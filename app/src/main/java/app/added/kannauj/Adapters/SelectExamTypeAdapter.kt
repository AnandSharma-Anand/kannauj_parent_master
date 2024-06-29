package app.added.kannauj.Adapters

import app.added.kannauj.Models.SelectExamModel
import app.added.kannauj.Models.SelectExamTypeModel
import app.added.kannauj.R
import app.added.kannauj.Utils.Prefs
import app.added.kannauj.activities.ExamActivity
import app.added.kannauj.activities.SolutionActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat


class SelectExamTypeAdapter(val context: Context, val list: ArrayList<SelectExamTypeModel>?, private val examList: ArrayList<SelectExamModel>?) :
        RecyclerView.Adapter<SelectExamTypeAdapter.ViewHolder>() {
    var itemList: ArrayList<Any> = if (list != null) {
        this.list as ArrayList<Any>
    } else {
        this.examList as ArrayList<Any>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.single_row_exam2, parent, false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list != null) {
            val model = itemList[holder.adapterPosition] as SelectExamTypeModel
            holder.tvText.text = model.examTypeName
            holder.btnDownload.visibility = INVISIBLE
            holder.itemView.setOnClickListener {
                val i = Intent("app.parent.fragment1")
                        .putExtra("id", model.id)
                holder.itemView.layoutParams.height = 100;
                context.sendBroadcast(i)
            }
        } else {
            val model = itemList[holder.adapterPosition] as SelectExamModel
            holder.tvText.text = model.examName
            holder.tvDate.visibility = View.VISIBLE
            if (model.IsCompleted == 1) {
                holder.btnDownload.visibility = View.VISIBLE
            } else {
                holder.btnDownload.visibility = View.INVISIBLE
            }
            holder.tvDate.text = model.FromDate + " to " + model.ToDate
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm")
                val output1: String = formatter.format(parser.parse(model.FromDate))
                val output2: String = formatter.format(parser.parse(model.ToDate))
                holder.tvDate.text = output1 + " to " + output2
            } catch (e: Exception) {
                holder.tvDate.text = model.FromDate + " to " + model.ToDate
            }

            holder.dummyView.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                if (model.IsCompleted == 1) {
                    Toast.makeText(context, "This Exam has been submitted already", Toast.LENGTH_SHORT).show()
                } else {
                    context.startActivity(Intent(context, ExamActivity::class.java)
                            .putExtra("id", model.id)
                            .putExtra("duration", model.duration))
                }
            }
            /*if (model.IsTestTaken == 0) {
                holder.btnDownload.visibility = View.GONE
            } else {
                holder.dummyView.visibility = View.VISIBLE
                holder.btnDownload.visibility = View.VISIBLE
            }*/

            holder.btnDownload.setOnClickListener {
                val intent = Intent(context, SolutionActivity::class.java)
                val bundle = Bundle()
                bundle.putString("URL", "")
                bundle.putString("TOK", Prefs(context).token)
                bundle.putString("SID", Prefs(context).student[0])
                bundle.putString("PID", model.id)
                bundle.putString("SUB", Prefs.with(context).domain)
                intent.putExtras(bundle)
                context.startActivity(intent)
                /* val url = model.downloadUrl
                 if (url.isNotEmpty()) {
                     if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                         // this will request for permission when user has not granted permission for the app
                         ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                     } else {
                         //Download Script
                         val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                         val uri: Uri = Uri.parse(url)
                         val request = DownloadManager.Request(uri)
                         request.setVisibleInDownloadsUi(true)
                         request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                         request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment())
                         downloadManager!!.enqueue(request)
                     }
                 }*/
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText: TextView = itemView.findViewById(R.id.tvText)
        val dummyView: View = itemView.findViewById(R.id.dummy_view);
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val btnDownload: Button = itemView.findViewById(R.id.btnDownload)

        init {
            checkBox.visibility = View.GONE
        }
    }
}

