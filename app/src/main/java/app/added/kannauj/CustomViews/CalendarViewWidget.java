package app.added.kannauj.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.added.kannauj.Models.CalendarEventModel;
import app.added.kannauj.Models.DateModel;
import app.added.kannauj.R;
import app.added.kannauj.Utils.FeedItemDecoration;

public class CalendarViewWidget extends LinearLayout {

    // for logging
    private static final String TAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    //event handling
    private EventHandler eventHandler = null;

    //DataList
    private List<DateModel> dateList;
    private List<DateModel> dateListAttendence;

    List<CalendarEventModel> calendarEventList;

    // internal components
    private LinearLayout header;
    private LinearLayout lnEventAttendence;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private GridView grid;
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerViewEvents;
    TextView tvAbsent, tvHalfDay, tvPresent;
    private RelativeLayout rlAttendanceDetail;
    private TextView tvAttendanceDetail;

    // seasons' rainbow
    int[] rainbow = new int[]{
            R.color.transparent,
            R.color.transparent,
            R.color.transparent,
            R.color.transparent
    };

    // month-season association (northern hemisphere, sorry australia :)
    int[] monthSeason = new int[]{2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

    public CalendarViewWidget(Context context) {
        super(context);
    }

    public CalendarViewWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarViewWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        loadDateFormat(attrs);
        assignUiElements();
        assignClickHandlers();

        updateCalendar();
    }

    private void loadDateFormat(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            // try to load provided date format, and fallback to default otherwise
            dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
            if (dateFormat == null)
                dateFormat = DATE_FORMAT;
        } finally {
            ta.recycle();
        }
    }

    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        header = findViewById(R.id.calendar_header);
        lnEventAttendence = findViewById(R.id.lnEventAttendence);
        tvPresent = findViewById(R.id.tvPresent);
        tvAbsent = findViewById(R.id.tvAbsent);
        tvHalfDay = findViewById(R.id.tvHalfDay);
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);
        grid = findViewById(R.id.calendar_grid);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        nestedScrollView.setFillViewport(true);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.addItemDecoration(new FeedItemDecoration(0, 70, 9));
        rlAttendanceDetail = findViewById(R.id.rlAttendanceDetail);
        tvAttendanceDetail = findViewById(R.id.tvAttendanceDetail);
    }

    public void setEventsAdapter(List<CalendarEventModel> list) {
        calendarEventList = list;
        List<CalendarEventModel> tempList = filterList(list, currentDate.get(Calendar.MONTH));
        if (tempList.size() > 0) {
            lnEventAttendence.setVisibility(GONE);
            recyclerViewEvents.setVisibility(VISIBLE);
            recyclerViewEvents.setAdapter(new EventsAdapter(getContext(), tempList));
        } else {
            lnEventAttendence.setVisibility(GONE);
            recyclerViewEvents.setVisibility(GONE);//change 23
        }
    }

    private void setMyEventAdapter(int month) {
        List<CalendarEventModel> tempList = filterList(calendarEventList, month);
        if (tempList.size() > 0) {
            lnEventAttendence.setVisibility(GONE);
            recyclerViewEvents.setVisibility(VISIBLE);
            recyclerViewEvents.setAdapter(new EventsAdapter(getContext(), tempList));
        } else {
            lnEventAttendence.setVisibility(GONE);
            recyclerViewEvents.setVisibility(GONE);//change 23
        }
    }

    public void showAttendanceDetail() {
        //rlAttendanceDetail.setVisibility(VISIBLE);
        recyclerViewEvents.setVisibility(GONE);
    }

    public void updateAttendanceDetail(int month) {
        List<DateModel> tempList = new ArrayList<>();
        for (DateModel dateModel : dateList) {
            if (dateModel.getDate().getMonth() == month) {
                tempList.add(dateModel);
            }
        }
        tvAttendanceDetail.setText("Absent : " + tempList.size());
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);

                if (dateListAttendence != null) {
                    updateCalendar(dateListAttendence);
                } else {
                    updateCalendar(dateList);
                }
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                if (dateListAttendence != null) {
                    updateCalendar(dateListAttendence);
                } else {
                    updateCalendar(dateList);
                }
            }
        });

        // long-pressing a day
        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {
                // handle long-press
                if (eventHandler == null)
                    return false;

                eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
                return true;
            }
        });

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // handle click
                //if (eventHandler == null)
                //return false;

                eventHandler.onDayPress((Date) adapterView.getItemAtPosition(i));
                //return true;
            }
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar() {
        updateCalendar(null);
    }

    public void initializeCalendarData(List<DateModel> events) {
        dateList = events;
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();
        //today
        if (calendarEventList != null) {
            //List<CalendarEventModel> monthEventList = filterList(calendarEventList, calendar.get(Calendar.MONTH));
            setMyEventAdapter(calendar.get(Calendar.MONTH));
        }

        if (dateList != null) {
            updateAttendanceDetail(calendar.get(Calendar.MONTH));
        }
        if (dateListAttendence != null) {
            setAttendenceEventsAdapter(dateListAttendence);
        }
        //
        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(List<DateModel> events) {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) currentDate.clone();

        // Filtering recyclerView List
        if (calendarEventList != null) {
            //List<CalendarEventModel> monthEventList = filterList(calendarEventList, calendar.get(Calendar.MONTH));
            setMyEventAdapter(calendar.get(Calendar.MONTH));
        }

        if (dateList != null) {
            updateAttendanceDetail(calendar.get(Calendar.MONTH));//change
            //  setAttendenceEventsAdapter(dateList);
        }
        if (dateListAttendence != null) {
            //   updateAttendanceDetail(calendar.get(Calendar.MONTH));//change
            setAttendenceEventsAdapter(dateListAttendence);
        }


        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        txtDate.setText(sdf.format(currentDate.getTime()));

        // set header color according to current season
        int month = currentDate.get(Calendar.MONTH);
        int season = monthSeason[month];
        int color = rainbow[season];

        header.setBackgroundColor(getResources().getColor(color));
    }

    private List<CalendarEventModel> filterList(List<CalendarEventModel> list, int month) {
        List<CalendarEventModel> tempList = new ArrayList<>();
        for (CalendarEventModel calendarEventModel : list) {
            if (calendarEventModel.getDate().getMonth() == month) {
                tempList.add(calendarEventModel);
            }
        }
        Log.e(TAG, "month : " + month);
        return tempList;
    }


    private class CalendarAdapter extends ArrayAdapter<Date> {
        // days with events
        private List<DateModel> eventDays;

        // for view inflation
        private LayoutInflater inflater;
        TextView text;
        CardView cardCircle;

        public CalendarAdapter(Context context, ArrayList<Date> days, List<DateModel> eventDays) {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            // inflate item if it does not exist yet
            if (view == null) {
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);
                text = view.findViewById(R.id.text);
                cardCircle = view.findViewById(R.id.cardCircle);
            }

            // if this day has an event, specify event image
            cardCircle.setVisibility(GONE);
            // clear styling
            (text).setTypeface(null, Typeface.NORMAL);
            (text).setTextColor(Color.parseColor("#e2e2e2"));

            if (month != today.getMonth() || year != today.getYear()) {
                // if this day is outside current month, grey it out
                (text).setTextColor(Color.parseColor("#6a6e72"));
            } else if (day == today.getDate()) {
                // if it is today, set circle
                cardCircle.setVisibility(VISIBLE);
            }

            if (eventDays != null) {
                for (int i = 0; i < eventDays.size(); i++) {
                    Date eventDate = eventDays.get(i).getDate();
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year) {
                        // mark this day for event
                        text.setTypeface(null, Typeface.NORMAL);
                        if (eventDays.get(i).getStatus().equals("")) {

                            if (eventDays.get(i).getDay() == 0) {
                                text.setTextColor(Color.parseColor("#FF5D5D"));
                            } else {
                                text.setTextColor(getResources().getColor(R.color.today));
                            }
                        } else {
                            if (eventDays.get(i).getStatus().equals("Present")) {
                                text.setTextColor(getResources().getColor(R.color.app_green));
                            } else if (eventDays.get(i).getStatus().equals("Absent")) {
                                text.setTextColor(getResources().getColor(R.color.app_red));
                            } else if (eventDays.get(i).getStatus().equals("HalfDay")) {
                                text.setTextColor(getResources().getColor(R.color.today));
                            } else {
                                text.setTextColor(Color.parseColor("#FF5D5D"));
                            }

                        }
                        break;
                    }
                }
            }

//            if (month != today.getMonth() || year != today.getYear())
//            {
//                // if this day is outside current month, grey it out
//                (text).setTextColor(Color.parseColor("#6a6e72"));
//            }
//            else if (day == today.getDate())
//            {
//                // if it is today, set circle
//                cardCircle.setVisibility(VISIBLE);
//            }

            // set text
            (text).setText(String.valueOf(date.getDate()));

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler {
        void onDayLongPress(Date date);

        void onDayPress(Date date);
    }

    class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

        Context context;
        List<CalendarEventModel> list;

        public EventsAdapter(Context context, List<CalendarEventModel> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new EventViewHolder(LayoutInflater.from(context).inflate(R.layout.single_event_view, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
            CalendarEventModel model = list.get(i);
            eventViewHolder.tvTitle.setText(model.getName());
            String date = DateFormat.getDateInstance(DateFormat.FULL).format(model.getDate());
            eventViewHolder.tvDetail.setText("(" + date + ")\n" + model.getDetail());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle, tvDetail;
            View leftLineView;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvDetail = itemView.findViewById(R.id.tvDetail);
                leftLineView = itemView.findViewById(R.id.leftLineView);
            }
        }


    }

    public void setAttendenceEventsAdapter(List<DateModel> weekOffList) {
        int present = 0;
        int absentPresent = 0;
        int halfDay = 0;
        dateListAttendence = weekOffList;
        List<DateModel> tempList = filterDateList(weekOffList, currentDate.get(Calendar.MONTH));
        if (tempList.size() > 0) {
            lnEventAttendence.setVisibility(VISIBLE);
            recyclerViewEvents.setVisibility(GONE);
            for (int i = 0; i < tempList.size(); i++) {
                DateModel model = tempList.get(i);
                if (model.getStatus().equals("Present")) {
                    present = present + 1;
                } else if (model.getStatus().equals("Absent")) {
                    absentPresent = absentPresent + 1;
                } else {
                    halfDay = halfDay + 1;
                }
            }
            tvPresent.setText("Total Present : " + present);
            tvAbsent.setText("Total Absent : " + absentPresent);
            tvHalfDay.setText("Total Half Day : " + halfDay);
        } else {
            lnEventAttendence.setVisibility(GONE);
            recyclerViewEvents.setVisibility(GONE);//change 23
        }


    }

    private List<DateModel> filterDateList(List<DateModel> list, int month) {
        List<DateModel> tempList = new ArrayList<>();
        for (DateModel calendarEventModel : list) {
            if (calendarEventModel.getDate().getMonth() == month) {
                tempList.add(calendarEventModel);
            }
        }
        Log.e(TAG, "month : " + month);
        return tempList;
    }
}
