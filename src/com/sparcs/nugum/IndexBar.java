package com.sparcs.nugum;

//IndexBar.java
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexBar extends View {
	private String[] keys;
	private SectionIndexer sectionIndex = null;
	private ListView list;

	public IndexBar(Context context) {
		super(context);
	}

	public IndexBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setListView(ListView _list) {
		list = _list;
		sectionIndex = (SectionIndexer) _list.getAdapter();
		keys = (String[]) sectionIndex.getSections();
	}

	// 간단하게 터치위치를 계산해서 섹션의 위치를 가져온다.
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		int size = getMeasuredHeight() / (keys.length + 1);
		int i = (int) event.getY();
		int idx = i / size;
		if (idx >= keys.length) {
			idx = keys.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			if (sectionIndex == null) {
				sectionIndex = (SectionIndexer) list.getAdapter();
			}
			int position = sectionIndex.getPositionForSection(keys[idx].toCharArray()[0]);
			if (position == -1) {
				return true;
			}
			list.setSelection(position);
		}
		return true;
	}

	// 화면에 세로로 표시될 문자들을 그린다.
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(0xFFA6A9AA);
		paint.setTextSize(30);
		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() / 2;
		float size = getMeasuredHeight() / (keys.length + 1);
		for (int i = 0; i < keys.length; i++) {
			canvas.drawText(String.valueOf(keys[i]), widthCenter, size + (i * size), paint);
		}
		super.onDraw(canvas);
	}
}