package com.tiendas3b.ticketdoctor.dialogs;

/**
 * Created by dfa on 22/03/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.tiendas3b.ticketdoctor.R;

import java.util.Calendar;

public class CalendarDialogBuilder {

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    private OnDateSetListener dateSetlistener;
    private Context context;
    private long initialDate;
    private CalendarView mCv;
    private AlertDialog.Builder alertBuilder;

    // To hold the values of year, month and day.
    public int YEAR, MONTH, DAY;

    // Constructor.
    public CalendarDialogBuilder(Context ctx, OnDateSetListener listener) {
        this.context = ctx;
        this.dateSetlistener = listener;
        this.alertBuilder = returnDialog();
    }

    public CalendarDialogBuilder(Context ctx, OnDateSetListener listener, long initialDate) {
        this.context = ctx;
        this.dateSetlistener = listener;
        this.initialDate = initialDate;
        this.alertBuilder = returnDialog();
    }

    public AlertDialog.Builder returnDialog() {
        // Inflating the layout.
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.calendar_dialog, null, false);

        // Getting the CalendarView.
        mCv = (CalendarView) ll.getChildAt(0);

        // Setting the listener.
        mCv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;
            }
        });

        // Configuring the calendar view.
        configCalendarView();

        // Creating the alert dialog that will display our LinearLayout with the calendar view.
        AlertDialog.Builder aDBuilder = new AlertDialog.Builder(context)
                .setView(ll)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(YEAR, MONTH, DAY);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dateSetlistener.onDateSet(0, 0, 0);
                    }
                });

        return aDBuilder;

    }

    // This method will be called from the activity.
    public void showCalendar() {
        alertBuilder.show();
    }

    public void setStartDate(long startDate) {
        mCv.setMinDate(startDate);
    }

    public void setEndDate(long endDate) {
        mCv.setMaxDate(endDate);
    }

    // Here you can configure your CalendarView.
//    @SuppressLint("NewApi")
    private void configCalendarView() {

        // sets whether to show the week number.
        mCv.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here I set Sunday as the first day of the Calendar
        mCv.setFirstDayOfWeek(1);

        // sets whether to show the week number.
        mCv.setShowWeekNumber(false);

        // if there's a initial date, set it.
        if (initialDate != 0) {
            mCv.setDate(initialDate, true, false);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(initialDate);
            DAY = calendar.get(Calendar.DAY_OF_MONTH);
            MONTH = calendar.get(Calendar.MONTH);
            YEAR = calendar.get(Calendar.YEAR);

//            Date dateToSet = new Date(initialDate);
//            YEAR = dateToSet.getYear();
//            MONTH = dateToSet.getMonth();
//            DAY = dateToSet.getDay();

        }

        // sets the background color.
//        mCv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark, context.getTheme()));

//        // theses methods are API 16+ only.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            //The background color for the selected week.
//            mCv.setSelectedWeekBackgroundColor(context.getResources().getColor(R.color.colorPrimary, context.getTheme()));
//
//            //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
//            mCv.setSelectedDateVerticalBar(new ColorDrawable(context.getResources().getColor(R.color.ticket_detail_value, context.getTheme())));
//
//            //sets the color for the dates of an unfocused month.
//            mCv.setUnfocusedMonthDateColor(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
//
//            //sets the color for the separator line between weeks.
//            mCv.setWeekSeparatorLineColor(context.getResources().getColor(R.color.ticket_detail_left, context.getTheme()));
//        }
    }

}