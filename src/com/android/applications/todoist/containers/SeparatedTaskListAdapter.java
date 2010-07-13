package com.android.applications.todoist.containers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.applications.todoist.Constants;
import com.android.applications.todoist.R;
import com.android.applications.todoist.containers.TaskListAdapter.PriorityDisplayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

public class SeparatedTaskListAdapter extends SeparatedListAdapter {

	public SeparatedTaskListAdapter(Context context) {
		super(context);
	}
	
	public void addList(PriorityDisplayList priorityList)
	{
		Date tempDate;
		SimpleAdapter newAdapter;
		while(priorityList.getListSize() != 0)
		{
			//Important Note! priorityList.getLowestPriorityDate() removes the date from the list!
			//priorityList.getLowestPriority() removes the List AND Priority Integer from their lists!
			//SO! They need to be run in THAT order, else you'll get really wonky results.
			tempDate = priorityList.getHighestPriorityDate();
			newAdapter = new SimpleAdapter(this.context, priorityList.getHighestPriority(), R.layout.task, 
					new String[] {Constants.ADAPTER_TITLE, Constants.ADAPTER_PROJECT}, 
					new int[] { R.id.TextView01, R.id.TextView02} );
			
			if(newAdapter.isEmpty())
			{
				this.addSection(this.getView(tempDate,priorityList.findDateDifference(tempDate),true) , newAdapter);
			}
			else
			{
				this.addSection(this.getView(tempDate,priorityList.findDateDifference(tempDate),false) , newAdapter);
			}
		}
	}
	
	public void addSection(String text1, String text2, Adapter adapter)
	{
		HeaderView view = new HeaderView(this.context);
		view.setText(text1, text2);
		this.heads.add(view);
		this.sections.put(text1, adapter);
	}

	public void addSection(HeaderView view, Adapter adapter)
	{
		this.heads.add(view);
		this.sections.put(view.getText1(),adapter);
	}
	
	private HeaderView getView(Date date, Integer diff, boolean isEmpty)
	{
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_STRING_SHORT);
		HeaderView view = new HeaderView(this.context);
		String dateString = "";
		int type, type2;
		
		if(isEmpty)
			type2 = HeaderView.TYPE_NOTASKS;
		else
			type2 = HeaderView.TYPE_NORMAL;
			
		switch(diff)
		{
			case -1:
				dateString = "Yesterday";
				type = HeaderView.TYPE_OVERDUE;
				break;
			case 0:
				dateString = "Today";
				type = HeaderView.TYPE_TODAY;
				break;
			case 1:
				dateString = "Tomorrow";
				type = HeaderView.TYPE_TOMORROW;
				break;
			default:
				if(diff > 1)
				{
				SimpleDateFormat format2 = new SimpleDateFormat("EEEEE");
				dateString = format2.format(date);
				if(isEmpty)
					type = HeaderView.TYPE_NOTASKS;
				else
					type = HeaderView.TYPE_NORMAL;
				}
				else
				{
					dateString = (diff*(-1)) + " days ago";	
					type = HeaderView.TYPE_OVERDUE;
				}
				break;
		}
		
		view.setType(type, type2);
		view.setText(dateString, format.format(date));
		
		return view;
	}
	
	public class HeaderView extends View
	{
		public static final int TYPE_TODAY = 0;
		public static final int TYPE_TOMORROW = 1;
		public static final int TYPE_NORMAL = 2;
		public static final int TYPE_NOTASKS = 3;
		public static final int TYPE_OVERDUE = 4;
		private static final int TEXTSPACING = 10;
		private Paint textPaint1;
		private Paint textPaint2;
		private Paint linePaint;
		private String text1;
		private String text2;
		private int ascent;
		private int type;
		private int type2;
		private boolean noTasks;
		
		public HeaderView(Context context) {
			super(context);
			textPaint1 = new Paint();
			textPaint1.setAntiAlias(true);
			textPaint1.setTextSize(20);
			textPaint1.setFakeBoldText(true);
			textPaint2 = new Paint();
			textPaint2.setAntiAlias(true);
			textPaint2.setTextSize(16);
			textPaint2.setFakeBoldText(true);
			linePaint = new Paint();
			linePaint.setAntiAlias(true);
			linePaint.setColor(Color.GRAY);
			noTasks = false;
			setPadding(5, 5, 5, 8);
		}
		
		public void setType(int type, int type2)
		{
			if(type >= TYPE_TODAY && type <= TYPE_OVERDUE)
			{
				this.type = type;
			}
			else
			{
				this.type = TYPE_NORMAL;
			}
			
			if(type2 == TYPE_NOTASKS)
				noTasks = true;
			
			this.type2 = type2;
		}
		
		public void setText(String text1, String text2)
		{
			this.text1 = text1;
			this.text2 = text2;
			requestLayout();
			invalidate();
		}
		
		public String getText1()
		{
			return this.text1;
		}
		
		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
		{
			setMeasuredDimension(measureScreenWidth(widthMeasureSpec),measureTextHeight(heightMeasureSpec));
		}
		
		/**
		 * private int measureScreenWidth(int measureSpec)
		 * <p>
		 * Returns the width of the screen
		 * @param measureSpec
		 * @return
		 * <li> The width of the screen as an Integer
		 */
		private int measureScreenWidth(int measureSpec)
		{
			return MeasureSpec.getSize(measureSpec);
		}
		
		/**
		 * private int measureTextWidth(int measureSpec)
		 * <p>
		 * Returns the width of the Text
		 * @param measureSpec
		 * @return
		 * <li> The width of the text as an Integer
		 */
	    private int measureTextWidth(int measureSpec) {
	        int result = 0;
	        int specMode = MeasureSpec.getMode(measureSpec);
	        int specSize = MeasureSpec.getSize(measureSpec);

	        if (specMode == MeasureSpec.EXACTLY) {
	            // We were told how big to be
	            result = specSize;
	        } else {
	            // Measure the text
	            result = (int) textPaint1.measureText(text1) + (int) textPaint2.measureText(text2) + getPaddingLeft()
	                    + getPaddingRight();
	            if (specMode == MeasureSpec.AT_MOST) {
	                // Respect AT_MOST value if that was what is called for by measureSpec
	                result = Math.min(result, specSize);
	            }
	        }

	        return result;
	    }
	    
	    /**
		 * private int measureTextHeight(int measureSpec)
		 * <p>
		 * Returns the height of the Text
		 * @param measureSpec
		 * @return
		 * <li> The height of the text as an Integer
		 */
	    private int measureTextHeight(int measureSpec) {
	        int result = 0;
	        int specMode = MeasureSpec.getMode(measureSpec);
	        int specSize = MeasureSpec.getSize(measureSpec);

	        ascent = (int) textPaint1.ascent();
	        if (specMode == MeasureSpec.EXACTLY) {
	            // We were told how big to be
	            result = specSize;
	        } else {
	            // Measure the text (beware: ascent is a negative number)
	            result = (int) (-ascent + textPaint1.descent()) + getPaddingTop()
	                    + getPaddingBottom();
	            if (specMode == MeasureSpec.AT_MOST) {
	                // Respect AT_MOST value if that was what is called for by measureSpec
	                result = Math.min(result, specSize);
	            }
	        }
	        if(noTasks)
	        	result *= 2;
	        
	        return result;
	    }
	    
	    private void setColors()
	    {
	    	int color = 0;
	    	switch(this.type)
	    	{
	    	case TYPE_TODAY:
	    		//Green
	    		color = Color.GREEN;
	    		break;
	    	case TYPE_TOMORROW:
	    		//Blue
	    		color = Color.BLUE;
	    		break;
	    	case TYPE_NORMAL:
	    		//Black
	    		color = Color.BLACK;
	    		break;
	    	case TYPE_NOTASKS:
	    		//Grey
	    		color = Color.GRAY;
	    		break;
	    	case TYPE_OVERDUE:
	    		//Bright Red
	    		color = Color.RED;
	    		break;
	    	}
	    	textPaint1.setColor(color);
	    	
	    	switch(this.type2)
	    	{
	    	case TYPE_NORMAL:
	    		//Black
	    		color = Color.BLACK;
	    		break;
	    	case TYPE_NOTASKS:
	    		//Gray
	    		color = Color.GRAY;
	    		break;
	    	}
	    	
	    	textPaint2.setColor(color);
	    }
	    
		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);

			setColors();
			canvas.drawLine(0, 0, super.getWidth(), 0, linePaint);

	        //Draw text1
	        int text1y = getPaddingTop() + (int)(-textPaint1.ascent() + textPaint1.descent());
	        canvas.drawText(text1, getPaddingLeft(), text1y, textPaint1); 

	        //Draw text2
	        canvas.drawText(text2,(int)(textPaint1.measureText(text1)) + TEXTSPACING, text1y, textPaint2); 
	        
	        if(noTasks)
	        {
	        	canvas.drawText("No Tasks", getPaddingLeft() + 4, text1y + 26, textPaint2);
	        }
		}
		

	}
}
