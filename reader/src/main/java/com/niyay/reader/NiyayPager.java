package com.niyay.reader;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by APPLE on 1/26/14.
 */
public class NiyayPager extends ViewPager {

    private int pageSave;
    private ArrayList<Integer> stacks = null;
    public NavigationDelegate delegate;

    public NiyayPager(Context context){
        super(context);
    }

    public NiyayPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){
        return false;
    }

    @Override
    public void setCurrentItem (int item, boolean smoothScroll){
        super.setCurrentItem(item, smoothScroll);
    }

    public void setCurrentItem(int item, boolean smoothScroll, boolean save){
        //pageSave = super.getCurrentItem();
        if (stacks == null)
            stacks = new ArrayList<Integer>();
        if (save == true){
            stacks.add(super.getCurrentItem());
            delegate.setBackNavitationShow(true);
        }
        else{
            stacks.clear();
        }
        super.setCurrentItem(item, smoothScroll);
    }

    public void setCurrentPageOnSaved(){
        if (stacks.size()>0){
            Integer page = stacks.get(stacks.size()-1);
            stacks.remove(stacks.size()-1);
            delegate.setBackNavitationShow(stacks.size()>0);
            super.setCurrentItem(page, false);
        }
    }

    public void clearBackStack(){
        if (stacks != null && delegate != null){
            stacks.clear();
            delegate.setBackNavitationShow(false);
        }
    }

    public interface NavigationDelegate {
        public void setBackNavitationShow(boolean show);
    }
}
