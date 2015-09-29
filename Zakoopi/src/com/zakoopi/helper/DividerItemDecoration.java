package com.zakoopi.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import com.zakoopi.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

private final Drawable mDivider;
private final int mHeight;
private int mOrientation;

public DividerItemDecoration( final Context context, final int orientation) {
    mDivider = new ColorDrawable( context.getResources().getColor( R.color.divider) );
    mHeight = dpToPx( 1f, context.getResources() );
    setOrientation(orientation);
}

private int dpToPx( final float dp, final Resources resource ) {
    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics() );
    return Math.max( 1, (int) px );
}

public void setOrientation( final int orientation) {
    if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
        throw new IllegalArgumentException("invalid orientation");
    }
    mOrientation = orientation;
}

@Override
public void onDraw( final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
    if( mOrientation == VERTICAL_LIST ) {
        drawVertical(c, parent);
    } else {
        drawHorizontal(c, parent);
    }
}

public void drawVertical( final Canvas c, final RecyclerView parent ) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();
    final int childCount = parent.getChildCount();

    View child;
    RecyclerView.LayoutParams params;
    int top;
    int bottom;

    for( int position = 0; position < childCount; position++) {
        child = parent.getChildAt(position);
        params = (RecyclerView.LayoutParams) child.getLayoutParams();
        top = child.getBottom() + params.bottomMargin;
        bottom = top + mHeight;
        mDivider.setBounds( left, top, right, bottom );
        mDivider.draw(c);
    }
}

public void drawHorizontal( final Canvas c, final RecyclerView parent ) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getHeight() - parent.getPaddingBottom();
    final int childCount = parent.getChildCount();

    View child;
    RecyclerView.LayoutParams params;
    int left;
    int right;

    for( int position = 0; position < childCount; position++) {
        child = parent.getChildAt( position );
        params = (RecyclerView.LayoutParams) child.getLayoutParams();
        left = child.getRight() + params.rightMargin;
        right = left + mHeight;
        mDivider.setBounds( left, top, right, bottom );
        mDivider.draw( c );
    }
}

@Override
public void getItemOffsets( final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
    if (mOrientation == VERTICAL_LIST) {
        outRect.set( 0 /*left*/, 0 /*top*/, 0 /*right*/, mHeight /*bottom*/ );
    } else {
        outRect.set( 0 /*left*/, 0 /*top*/, mHeight /*right*/, 0 /*bottom*/ );
    }
}
}