package com.sadi.toor.recommend.view.adapter.genre;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sadi.toor.recommend.R;
import com.sadi.toor.recommend.model.data.genre.Genre;
import com.sadi.toor.recommend.model.data.genre.SelectableGenre;

import java.util.ArrayList;
import java.util.List;

public class SelectableAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {

    private final List<SelectableGenre> mValues;
    private boolean isMultiSelectionEnabled = false;
    private SelectableViewHolder.OnItemSelectedListener listener;


    public SelectableAdapter(SelectableViewHolder.OnItemSelectedListener listener,
                             List<Genre> items, boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (Genre item : items) {
            mValues.add(new SelectableGenre(item, false));
        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.genre_item, parent, false);

        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableGenre selectableItem = mValues.get(position);
        String name = selectableItem.getGenreName();
        holder.textView.setText(name);
        if (isMultiSelectionEnabled) {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorMultiple, value, true);
        } else {
            TypedValue value = new TypedValue();
            holder.textView.getContext().getTheme().resolveAttribute(android.R.attr.listChoiceIndicatorSingle, value, true);
        }

        holder.item = selectableItem;
        holder.setChecked(holder.item.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<Genre> getSelectedItems() {

        List<Genre> selectedItems = new ArrayList<>();
        for (SelectableGenre item : mValues) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (isMultiSelectionEnabled) {
            return SelectableViewHolder.MULTI_SELECTION;
        } else {
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableGenre item) {
        if (!isMultiSelectionEnabled) {

            for (SelectableGenre selectableItem : mValues) {
                if (!selectableItem.equals(item)
                        && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item)
                        && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}
