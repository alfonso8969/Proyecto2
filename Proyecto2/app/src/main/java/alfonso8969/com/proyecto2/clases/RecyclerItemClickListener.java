package alfonso8969.com.proyecto2.clases;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Creado por Alfonso en fecha 18/10/2016.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener listener;
    private GestureDetector gestureDetector;

    private View childView;
    private int childViewPosition;


    public  RecyclerItemClickListener(Context context, OnItemClickListener listener){
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.listener = listener;
    }



    @SuppressWarnings("deprecation")
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        childView = rv.findChildViewUnder(e.getX(), e.getY());
        childViewPosition = rv.getChildPosition(childView);

        return childView != null && gestureDetector.onTouchEvent(e);

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


    private interface OnItemClickListener{
         void onItemClick(View childView, int position);
         void onItemLongPress(View childView, int position);


    }



    public static abstract class SimpleOnItemClickListener implements  OnItemClickListener{
        public void onItemClick(View childView, int position){

        }

        public  void onItemLongPress(View childView, int position){

        }
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onSingleTapUp(MotionEvent event){
            if(childView != null){
                listener.onItemClick(childView,childViewPosition);
            }

            return true;
        }


        @Override
        public void onLongPress(MotionEvent event){
            if(childView != null){
                listener.onItemLongPress(childView, childViewPosition);
            }
        }
        @Override
        public boolean onDown(MotionEvent event){
            return true;
        }
    }

}
