package com.tiendas3b.ticketdoctor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.adapters.SectionsPagerAdapter;

/**
 * Created by dfa on 06/06/2016.
 */
public class BuysTabsFragment extends Fragment {

//    private OnFragmentInteractionListener mListener;

    public BuysTabsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_buys_tabs, container, false);
        init(rootView);
//        getActivity().setTitle(R.string.receipt_providers);
        return rootView;
    }

    private void init(View rootView) {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.pgr_tab);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        SmartTabLayout tabLayout = (SmartTabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed() {
//        if (mListener != null) {
//            mListener.onFragmentInteraction();
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction();
//    }

}

