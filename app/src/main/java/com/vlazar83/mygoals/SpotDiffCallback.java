package com.vlazar83.mygoals;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class SpotDiffCallback extends DiffUtil.Callback {

    private List<CardShape> old;
    private List<CardShape> newList;

    SpotDiffCallback(List<CardShape> old, List<CardShape> newList){
        this.old = old;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (old.get(oldItemPosition).getCardGoal() == newList.get(newItemPosition).getCardGoal() && old.get(oldItemPosition).getClass() == newList.get(newItemPosition).getClass());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == newList.get(newItemPosition);
    }
}
