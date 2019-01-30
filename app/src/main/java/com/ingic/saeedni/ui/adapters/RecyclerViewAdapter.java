package com.ingic.saeedni.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ingic.saeedni.entities.ServiceEnt;
import com.ingic.saeedni.ui.viewbinders.abstracts.RecyclerViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/10/2017.
 */

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewBinder.BaseViewHolder> implements Filterable {
    private List<T> collections;
    private List<T> defaultCollections;
    private List<T> originalCollection;
    private RecyclerViewBinder<T> viewBinder;
    private Context mContext;


    public RecyclerViewAdapter(List<T> collections, RecyclerViewBinder<T> viewBinder, Context context) {
        this.collections = collections;
        this.originalCollection = collections;
        this.defaultCollections = collections;
        this.viewBinder = viewBinder;
        this.mContext = context;

    }
    public void setSearchOriginalList(List<T> list){
        this.originalCollection = list;
    }
    @Override
    public RecyclerViewBinder.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return (RecyclerViewBinder.BaseViewHolder) this.viewBinder.createViewHolder(this.viewBinder.createView(this.mContext));
    }

    @Override
    public void onBindViewHolder(RecyclerViewBinder.BaseViewHolder holder, int position) {
        T entity = (T) this.collections.get(position);
        this.viewBinder.bindView(entity, position, holder, this.mContext);
    }

    @Override
    public int getItemCount() {
        return this.collections.size();
    }

    public T getItemFromList(int index) {
        return collections.get(index);
    }

    public List<T> getList() {
        return collections;
    }

    /**
     * Clears the internal list
     */
    public void clearList() {
        collections.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a entity to the list and calls {@link #notifyDataSetChanged()}.
     * Should not be used if lots of NotificationDummy are added.
     *
     * @see #addAll(List)
     */
    public void add(T entity) {
        collections.add(entity);
        notifyDataSetChanged();
    }

    /**
     * Adds a NotificationDummy to the list and calls
     * {@link #notifyDataSetChanged()}. Can be used {
     * {@link List#subList(int, int)}.
     *
     * @see #addAll(List)
     */
    public void addAll(List<T> entityList) {
        collections.addAll(entityList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                collections = (List<T>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<T> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = defaultCollections;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    protected List<T> getFilteredResults(String constraint) {
        List<T> results = new ArrayList<>();
        for (T item : originalCollection) {
            if (item instanceof ServiceEnt) {
                if (((ServiceEnt) item).getTitle().toLowerCase().contains(constraint) || ((ServiceEnt) item).getArTitle().toLowerCase().contains(constraint)) {
                    results.add(item);
                }
            }

        }
        return results;
    }
}
