package fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import adsforcharity.deanbangera.dmbangera.com.adsforcharity.R;
import fragments.CharitiesFragment.OnListFragmentInteractionListener;
import adsforcharity.deanbangera.dmbangera.com.adsforcharity.Charities.Charity.CharityItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CharityItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyCharitiesRecyclerViewAdapter extends RecyclerView.Adapter<MyCharitiesRecyclerViewAdapter.ViewHolder> {

    private final List<CharityItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    MyCharitiesRecyclerViewAdapter(List<CharityItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_charities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mNameView.setText(mValues.get(position).name);
        holder.mDescriptionView.setText(mValues.get(position).description);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mNameView;
        final TextView mDescriptionView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.card_name);
            mDescriptionView = view.findViewById(R.id.card_description);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
