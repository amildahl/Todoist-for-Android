/*    
	This file is part of Todoist for Android™.

    Todoist for Android™ is free software: you can redistribute it and/or 
    modify it under the terms of the GNU General Public License as published 
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Todoist for Android™ is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Todoist for Android™.  If not, see <http://www.gnu.org/licenses/>.
    
    This file incorporates work covered by the following copyright and  
 	permission notice:
 	
 	Copyright [2010] pskink <pskink@gmail.com>
 	Copyright [2010] ys1382 <ys1382@gmail.com>
 	Copyright [2010] JonTheNiceGuy <JonTheNiceGuy@gmail.com>

   	Licensed under the Apache License, Version 2.0 (the "License");
   	you may not use this file except in compliance with the License.
   	You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   	Unless required by applicable law or agreed to in writing, software
   	distributed under the License is distributed on an "AS IS" BASIS,
   	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   	See the License for the specific language governing permissions and
   	limitations under the License.
*/

package com.android.applications.todoist.containers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.android.applications.todoist.R;

public class SeparatedListAdapter extends BaseAdapter {

	public final Map<String,Adapter> sections = new LinkedHashMap<String,Adapter>();
	public final ArrayList<HeaderView> heads;
	public final ArrayAdapter<String> headers;
	public final static int TYPE_SECTION_HEADER = 0;
	public Context context;
	
	public SeparatedListAdapter(Context context) {
		this.context = context;
		headers = new ArrayAdapter<String>(context, R.layout.date_header);
		heads = new ArrayList<HeaderView>();
	}

	public void addSection(String section, String text, Adapter adapter) {
		HeaderView temp = new HeaderView(context);
		temp.setText(section, text);
		this.heads.add(temp);
		this.headers.add(section);
		this.sections.put(section, adapter);
	}

	public Object getItem(int position) {
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return section;
			if(position < size) return adapter.getItem(position - 1);

			// otherwise jump into next section
			position -= size;
		}
		return null;
	}

	public int getCount() {
		// total together all sections, plus one for each section header
		int total = 0;
		for(Adapter adapter : this.sections.values())
			total += adapter.getCount() + 1;
		return total;
	}

	public int getViewTypeCount() {
		// assume that headers count as one, then total all sections
		int total = 1;
		for(Adapter adapter : this.sections.values())
			total += adapter.getViewTypeCount();
		return total;
	}

	public int getItemViewType(int position) {
		int type = 1;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) return TYPE_SECTION_HEADER;
			if(position < size) return type + adapter.getItemViewType(position - 1);

			// otherwise jump into next section
			position -= size;
			type += adapter.getViewTypeCount();
		}
		return -1;
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionnum = 0;
		for(Object section : this.sections.keySet()) {
			Adapter adapter = sections.get(section);
			int size = adapter.getCount() + 1;

			// check if position inside this section
			if(position == 0) 
			{
				return heads.get(sectionnum);
				//View someView = headers.getView(sectionnum, convertView, parent);
				//((TextView)someView.findViewById(R.id.date_header_text)).setText("HI");
				//((TextView)someView.findViewById(R.id.date_header_text2)).setText("NO");
				//return someView;
				//return headers.getView(sectionnum, convertView, parent);
			}
			if(position < size) return adapter.getView(position - 1, convertView, parent);

			// otherwise jump into next section
			position -= size;
			sectionnum++;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class HeaderView extends View
	{
		private final int TEXTSPACING = 10;
		private Paint textPaint1;
		private Paint textPaint2;
		private Paint linePaint;
		private String text1;
		private String text2;
		private int ascent;
		
		public HeaderView(Context context) {
			super(context);
			textPaint1 = new Paint();
			textPaint1.setAntiAlias(true);
			textPaint1.setTextSize(16);
			textPaint1.setColor(0xFF000000);
			textPaint2 = new Paint();
			textPaint2.setAntiAlias(true);
			textPaint2.setTextSize(16);
			textPaint2.setColor(0xFF000000);
			linePaint = new Paint();
			linePaint.setColor(0xFF000000);
			setPadding(5, 5, 5, 8);
		}
		
		public void setText(String text1, String text2)
		{
			this.text1 = text1;
			this.text2 = text2;
			requestLayout();
			invalidate();
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
	        return result;
	    }
	    
		@Override
		protected void onDraw(Canvas canvas)
		{
			super.onDraw(canvas);

			canvas.drawLine(0, 0, super.getWidth(), 0, linePaint);

	        //Draw text1
	        int text1y = getPaddingTop() + (int)(-textPaint1.ascent() + textPaint1.descent());
	        canvas.drawText(text1, getPaddingLeft(), text1y, textPaint1); 

	        //Draw text2
	        canvas.drawText(text2,(int)(textPaint1.measureText(text1)) + TEXTSPACING, text1y, textPaint2); 
		}
	}
}
