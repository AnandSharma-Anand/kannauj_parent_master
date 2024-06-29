package app.added.kannauj.Adapters

import app.added.kannauj.Models.ExamQuestionModel
import app.added.kannauj.R
import app.added.kannauj.Utils.DatabaseQuestions
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.util.regex.Matcher
import java.util.regex.Pattern


class ExamAdapter(val context: Context, val list: ArrayList<ExamQuestionModel>, val pid: String) : RecyclerView.Adapter<ExamAdapter.ViewHolder>() {

    private var answerArray = Array(list.size) { 0 }
    var dbq: DatabaseQuestions = DatabaseQuestions(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_exam_row, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[holder.adapterPosition]
        holder.tvQuestion.text = android.text.Html.fromHtml(model.question).toString()
        holder.tvOption1.text = android.text.Html.fromHtml(model.answers.answer0.answer).toString()
        holder.tvOption2.text = android.text.Html.fromHtml(model.answers.answer1.answer).toString()
        holder.tvOption3.text = android.text.Html.fromHtml(model.answers.answer2.answer).toString()
        holder.tvOption4.text = android.text.Html.fromHtml(model.answers.answer3.answer).toString()
        val imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"
        val s: Pattern = Pattern.compile(imgRegex)
        val m: Matcher = s.matcher(model.question)
        var imgSrc: String = "";
        while (m.find()) {
            imgSrc = m.group(1)
            holder.ivImg.visibility = View.VISIBLE
        }
        if (imgSrc == "") {
            holder.ivImg.visibility = View.GONE
        } else {
            holder.ivImg.visibility = View.VISIBLE
            val builder = Picasso.Builder(context)
            builder.listener { picasso, uri, exception -> exception.printStackTrace() }
            builder.build().load(imgSrc).into(holder.ivImg)
        }
        //answerArray[holder.adapterPosition]
        System.out.println("Animesh Ans"+dbq.getAns(pid, model.id))
        when (dbq.getAns(pid, model.id).toInt()) {
            0 -> {
                holder.checkbox1.isChecked = false
                holder.checkbox2.isChecked = false
                holder.checkbox3.isChecked = false
                holder.checkbox4.isChecked = false
            }
            1 -> {
                holder.checkbox1.isChecked = true
                holder.checkbox2.isChecked = false
                holder.checkbox3.isChecked = false
                holder.checkbox4.isChecked = false
            }
            2 -> {
                holder.checkbox1.isChecked = false
                holder.checkbox2.isChecked = true
                holder.checkbox3.isChecked = false
                holder.checkbox4.isChecked = false
            }
            3 -> {
                holder.checkbox1.isChecked = false
                holder.checkbox2.isChecked = false
                holder.checkbox3.isChecked = true
                holder.checkbox4.isChecked = false
            }
            4 -> {
                holder.checkbox1.isChecked = false
                holder.checkbox2.isChecked = false
                holder.checkbox3.isChecked = false
                holder.checkbox4.isChecked = true
            }
        }

        holder.checkbox1.setOnClickListener {
            disableAllBoxes(holder)
            System.out.println("animesh1")
            if (answerArray[holder.adapterPosition] != 1) {
                System.out.println("animesh2")
                holder.checkbox1.isChecked = true
                dbq.setAns(model.id, pid, "1")
                answerArray[holder.adapterPosition] = 1
            } else {
                answerArray[holder.adapterPosition] = 0
                System.out.println("animesh3")
                dbq.setAns(model.id, pid, "0")
                holder.checkbox1.isChecked = false
            }
        }
        holder.checkbox2.setOnClickListener {
            disableAllBoxes(holder)
            System.out.println("animesh1")
            if (answerArray[holder.adapterPosition] != 2) {
                System.out.println("animesh2")
                dbq.setAns(model.id, pid, "2")
                holder.checkbox2.isChecked = true
                answerArray[holder.adapterPosition] = 2
            } else {
                answerArray[holder.adapterPosition] = 0
                System.out.println("animesh3")
                dbq.setAns(model.id, pid, "0")
                holder.checkbox2.isChecked = false
            }
        }
        holder.checkbox3.setOnClickListener {
            disableAllBoxes(holder)
            System.out.println("animesh1")
            if (answerArray[holder.adapterPosition] != 3) {
                System.out.println("animesh2")
                dbq.setAns(model.id, pid, "3")
                holder.checkbox3.isChecked = true
                answerArray[holder.adapterPosition] = 3
            } else {
                answerArray[holder.adapterPosition] = 0
                System.out.println("animesh3")
                dbq.setAns(model.id, pid, "0")
                holder.checkbox3.isChecked = false
            }
        }
        holder.checkbox4.setOnClickListener {
            disableAllBoxes(holder)
            System.out.println("animesh1")
            if (answerArray[holder.adapterPosition] != 4) {
                System.out.println("animesh2")
                holder.checkbox4.isChecked = true
                dbq.setAns(model.id, pid, "4")
                answerArray[holder.adapterPosition] = 4
            } else {
                answerArray[holder.adapterPosition] = 0
                System.out.println("animesh3")
                dbq.setAns(model.id, pid, "0")
                holder.checkbox4.isChecked = false
            }
        }

        /* if (!model.img.isNullOrBlank()) {
             holder.ivImg.visibility = View.VISIBLE
             Picasso.get().load(model.img).into(holder.ivImg)
         } else {
             holder.ivImg.visibility = View.GONE
         }*/

    }

    private fun disableAllBoxes(holder: ViewHolder) {
        holder.checkbox1.isChecked = false
        holder.checkbox2.isChecked = false
        holder.checkbox3.isChecked = false
        holder.checkbox4.isChecked = false
    }

    fun getAnswers(): HashMap<String, String> {
        var hashMap = HashMap<String, String>()
        for (i in 0 until list.size) {
            when (dbq.getAns(pid, list[i].id).toInt()) {
                0 -> {
                }
                1 -> hashMap[list[i].id] = list[i].answers.answer0.id
                2 -> hashMap[list[i].id] = list[i].answers.answer1.id
                3 -> hashMap[list[i].id] = list[i].answers.answer2.id
                4 -> hashMap[list[i].id] = list[i].answers.answer3.id
            }
        }
        return hashMap
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val checkbox1: CheckBox = itemView.findViewById(R.id.checkbox1)
        val checkbox2: CheckBox = itemView.findViewById(R.id.checkbox2)
        val checkbox3: CheckBox = itemView.findViewById(R.id.checkbox3)
        val checkbox4: CheckBox = itemView.findViewById(R.id.checkbox4)
        val tvOption1: TextView = itemView.findViewById(R.id.tvOption1)
        val tvOption2: TextView = itemView.findViewById(R.id.tvOption2)
        val tvOption3: TextView = itemView.findViewById(R.id.tvOption3)
        val tvOption4: TextView = itemView.findViewById(R.id.tvOption4)
        val ivImg: ImageView = itemView.findViewById(R.id.ivImg)
    }

}