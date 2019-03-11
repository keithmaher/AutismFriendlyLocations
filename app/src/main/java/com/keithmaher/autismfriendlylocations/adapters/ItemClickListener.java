package com.keithmaher.autismfriendlylocations.adapters;

import android.view.View;

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}