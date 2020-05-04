package com.tiendas3b.ticketdoctor.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tiendas3b.ticketdoctor.fragments.TicketsFragment;

/**
 * Created by dfa on 04/04/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_TABS = 2;
//    private final SimpleDateFormat df;
//    private ArrayList<Date> tittles;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
//        createTittles();
//        df = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT, Locale.getDefault());
    }

//    private void createTittles() {
//        tittles = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        Date today = new Date();
//        cal.setTime(today);
//        cal.add(Calendar.DATE, 1);
//        Date tomorrow = cal.getTime();
//        cal.add(Calendar.DATE, -2);
//        Date yesterday = cal.getTime();
////        SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT, Locale.getDefault());
////        tittles.add(df.format(yesterday));
////        tittles.add(df.format(today));
////        tittles.add(df.format(tomorrow));
//        tittles.add("Abiertos");
//        tittles.add("Cerrados");
//        tittles.add(tomorrow);
//    }

    @Override
    public Fragment getItem(int position) {
        return TicketsFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Abiertos" : "Cerrados";
    }
}
