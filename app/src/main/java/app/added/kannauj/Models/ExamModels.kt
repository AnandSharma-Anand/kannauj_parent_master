package app.added.kannauj.Models

import com.google.gson.annotations.SerializedName

data class ExamTypeDataModel(
        @SerializedName("TestTypeList")
        val list: ArrayList<SelectExamTypeModel>
)

data class SelectExamTypeModel(

        @SerializedName("id")
        val id: String,

        @SerializedName("TestTypeName")
        val examTypeName: String
)

data class ExamDataModel(
        @SerializedName("TestType")
        val list: ArrayList<SelectExamModel>
)

data class SelectExamModel(
        @SerializedName("id")
        val id: String,

        @SerializedName("TestName")
        val examName: String,

        @SerializedName("FromDate")
        val FromDate: String,

        @SerializedName("ToDate")
        val ToDate: String,

        @SerializedName("TestDuration")
        val duration: Int,

        @SerializedName("IsTestTaken")
        val IsTestTaken: Int,

        @SerializedName("download")
        val downloadUrl: String = "",

        @SerializedName("IsCompleted")
        val IsCompleted: Int
)

data class ExamModel(
        @SerializedName("TestQuestions")
        val list: ArrayList<ExamQuestionModel>
)

data class ExamQuestionModel(
        @SerializedName("id")
        val id: String,

        @SerializedName("Question")
        val question: String,

        @SerializedName("img")
        val img: String = "",

        @SerializedName("Answers")
        val answers: AnswersModel
)

data class AnswersModel(
        @SerializedName("0")
        val answer0: AnswerModel,
        @SerializedName("1")
        val answer1: AnswerModel,
        @SerializedName("2")
        val answer2: AnswerModel,
        @SerializedName("3")
        val answer3: AnswerModel

)

data class AnswerModel(
        @SerializedName("AnswerID")
        val id: String,

        @SerializedName("Answer")
        val answer: String
)