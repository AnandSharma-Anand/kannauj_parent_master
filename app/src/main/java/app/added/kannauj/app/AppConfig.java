package app.added.kannauj.app;

/**
 * Created by Adil on 23-12-2016.
 */

public class AppConfig {


    public static String GET_URL = "https://marketing.addedschools.com/mobile_app_services/parent_app_login.php";

    public static String BASE_URL = "";

    //public String BASE_URL = "http://demo.addedschools.com/mobile_app_services/parents_app/";

    //public static String BASE_URL = "http://gms.addedschools.com/mobile_app_services/parents_app/";

    //public static String BASE_URL = "http://saigal.addedschools.com/mobile_app_services/parents_app/";

    //public static String BASE_URL = "http://shivapublic.addedschools.com/mobile_app_services/parents_app/";

    public String URL_LOGIN = BASE_URL + "login_authentication.php";

    public String URL_USER_INFO = BASE_URL + "user_info.php";

    public String URL_GALLERY_EVENTS = BASE_URL + "event_gallery_all_events.php";

    public String URL_ALBUM = BASE_URL + "event_gallery_event_images.php";

    public String URL_FEED = BASE_URL + "news_feeds.php";

    public String URL_ATTENDANCE = BASE_URL + "attendance_list.php";


    public String URL_NOTICES = BASE_URL + "notices_list.php";

    public String URL_TIME_TABLE = BASE_URL + "class_time_table.php";

    public String URL_FEE = BASE_URL + "student_fee_details.php";

    public String URL_SUBJECTS = BASE_URL + "student_subjects.php";

    public String URL_ASSIGNMENTS = BASE_URL + "student_assignments.php";

    public String URL_SYLLABUS_TRACKER = BASE_URL + "student_subject_syllabus_tracking.php";

    public String URL_CHAPTERS_LIST = BASE_URL + "class_subject_chapters_list.php";

    public String URL_TOPICS_LIST = BASE_URL + "chapter_topics_list.php";

    public String URL_ACADEMIC_CALENDAR = BASE_URL + "academic_calendar_holiday_dates.php";

    public String URL_SAVE_TOKEN = BASE_URL + "save_fcm_token.php";

    public String URL_UPDATE_PROFILE_IMAGE = BASE_URL + "save_student_photo.php";

    //vxplore 19/07/19

    public String CHAT_LIST = BASE_URL + "get_teachers_for_chat.php";

    public String FETCH_CHAT = BASE_URL + "fetch_chat.php";

    public String SENT_MESSAGE = BASE_URL + "send_chat_message.php";

    public String FEE_HEAD = BASE_URL + "fee_heads.php";

    public String SAVE_STUDENT_ASSIGNMENT_ANSWER = BASE_URL + "save_student_assignment_answers.php";

    public String STUDENT_ASSIGNMENT_ANSWER = BASE_URL + "student_assignment_answers.php";

    public String STUDENT_DIARIES = BASE_URL + "student_diaries.php";

    public String URL_FEE_PAYMENT = BASE_URL + "student_fee_details_for_payment.php";

    public String SAVE_FEE_TRANSACTION = BASE_URL + "save_fee_transaction.php";

    public String AFTER_PAYMENT = BASE_URL + "gateway_response_after_payment.php";

    public String FEES_RECEIPT = BASE_URL + "student_fee_receipt.php";

    public String SELECT_EXAM_TYPE = "https://onlineexam.addedschools.com/mobile_app_services/parents_app/get_test_type.php";

    public String SELECT_EXAM = "https://onlineexam.addedschools.com/mobile_app_services/parents_app/get_all_test.php";

    public String URL_EXAM = "https://onlineexam.addedschools.com/mobile_app_services/parents_app/test.php";

    public String URL_EXAM_SUBMIT = "https://onlineexam.addedschools.com/mobile_app_services/parents_app/submit_test.php";

    // FCM related stuff
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;

    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String PUSH_NOTIFICATION = "pushNotification";


}