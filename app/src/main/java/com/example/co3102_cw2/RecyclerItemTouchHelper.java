package com.example.co3102_cw2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.co3102_cw2.Adapter.OptionAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private OptionAdapter optionAdapter;

    RecyclerItemTouchHelper(OptionAdapter optionAdapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.optionAdapter = optionAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getBindingAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder alert = new AlertDialog.Builder(viewHolder.itemView.getContext());
            alert.setTitle("Delete Option");
            alert.setMessage("Are you sure you want to delete this option?");
            alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    optionAdapter.deleteOption(position);
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    optionAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                }
            });
            AlertDialog dialog = alert.create();
            dialog.show();
        } else {
            optionAdapter.editOption(position);
        }
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int state, boolean active){
        super.onChildDraw(canvas,recyclerView,viewHolder, dX, dY, state, active);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if(dX>0){
            icon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(Color.parseColor("#213F0C"));
        } else {
            icon = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.ic_baseline_delete_24);
            background = new ColorDrawable(Color.parseColor("#AA2411"));
        }

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());
        }
        else if(dX < 0){
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset,itemView.getTop(),itemView.getRight(), itemView.getBottom());

        } else{
            background.setBounds(0,0,0,0);
        }

        background.draw(canvas);
        icon.draw(canvas);
    }
}
