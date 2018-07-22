package fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import adsforcharity.deanbangera.dmbangera.com.adsforcharity.Charities.Charity;
import adsforcharity.deanbangera.dmbangera.com.adsforcharity.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CharitiesFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharitiesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charities_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Activity mainActivity = getActivity();
            if (mainActivity != null) {
                mainActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            }
            final int columnCount;
            if (displayMetrics.widthPixels < (getResources().getDimension(R.dimen.card_width) + getResources().getDimension(R.dimen.card_margin)) * 2) {
                columnCount = 1;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                columnCount = 2;
                recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
            }
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    int totalWidth = parent.getWidth();
                    int cardWidth = getResources().getDimensionPixelOffset(R.dimen.card_width);
                    int sidePadding;
                    if (columnCount <= 1) {
                        sidePadding = (totalWidth - cardWidth) / 2;
                    } else {
                        sidePadding = (totalWidth - cardWidth * 2) / 4;
                    }
                    sidePadding = Math.max(0, sidePadding);
                    outRect.set(sidePadding, 0, sidePadding, 0);
                }
            });
            recyclerView.setAdapter(new MyCharitiesRecyclerViewAdapter(Charity.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        void onListFragmentInteraction();
    }
}
