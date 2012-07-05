package com.sparcs.nugum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ResultAdapter extends ArrayAdapter<Person> implements SectionIndexer {
	public ArrayList<Person> items;
	public ArrayList<Person> filtered;
	private Context context;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections;
	private Filter filter;
	private int sortConfig;
	
	public ResultAdapter(Context context, int textViewResourceId, ArrayList<Person> filtered, int sortConfig) {
		super(context, textViewResourceId, filtered);
		this.filtered = filtered;
		this.items = (ArrayList<Person>) filtered.clone();
		this.context = context;
		this.filter = new SearchFilter();
		this.sortConfig = sortConfig;
		this.alphaIndexer = new HashMap<String, Integer>();
		
		for (int i = items.size() - 1; i >= 0; i--) {
			Person element = items.get(i);
			
			if (sortConfig == 1) {
				char firstChar = element.name.substring(0, 1).toUpperCase().toCharArray()[0];
				if (!HangulChecker.isInitialSound(firstChar) && HangulChecker.isHangul(firstChar)) {
					firstChar = HangulChecker.getInitialSound(firstChar);
				}
				String firstString = Character.toString(firstChar);
				alphaIndexer.put(firstString, i);
			}
			if (sortConfig == 2) {
				if (!element.num.equals("0") && Integer.parseInt(element.num)%2==0)
					alphaIndexer.put(element.num, i);
			}
		}

		Set<String> keys = alphaIndexer.keySet();
		ArrayList<String> keyList = new ArrayList<String>(keys);
		if (sortConfig == 1) Collections.sort(keyList, new alphabetComparator());
		if (sortConfig == 2) Collections.sort(keyList, new studentNumberComparator());
		sections = new String[keyList.size()];
		keyList.toArray(sections);
	}

	private void setSection(LinearLayout header, String label) {
		TextView text = new TextView(context);
		header.setBackgroundColor(0xffaabbcc);
		text.setTextColor(Color.GRAY);
		String sectionChar = "";
		
		if (sortConfig == 1) {
			char firstChar = label.substring(0,1).toUpperCase().toCharArray()[0];
			if (!HangulChecker.isInitialSound(firstChar) && HangulChecker.isHangul(firstChar)) {
				firstChar = HangulChecker.getInitialSound(firstChar);
			}
			sectionChar = Character.toString(firstChar);
		}
		if (sortConfig == 2) {
			sectionChar = label; 
		}
		text.setText(sectionChar);
		text.setTextSize(16);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);
		header.addView(text);
	}
	
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		LayoutInflater inflate = ((Activity) context).getLayoutInflater();
		View view = (View)inflate.inflate(R.layout.result_adapter, null);
		LinearLayout header = (LinearLayout) view.findViewById(R.id.section);
		String label = "";
		
		if (sortConfig == 1) {
			label = filtered.get(position).name;
			char firstChar = label.substring(0,1).toUpperCase().toCharArray()[0];
			if (!HangulChecker.isInitialSound(firstChar) && HangulChecker.isHangul(firstChar)) {
				firstChar = HangulChecker.getInitialSound(firstChar);
			}
			if(position == 0) {
				setSection(header, label);
			}
			else {
				String preLabel = filtered.get(position-1).name;
				char prefirstChar = preLabel.substring(0,1).toUpperCase().toCharArray()[0];
				if (!HangulChecker.isInitialSound(prefirstChar) && HangulChecker.isHangul(prefirstChar)) {
					prefirstChar = HangulChecker.getInitialSound(prefirstChar);
				}
				if(firstChar != prefirstChar){
					setSection(header, label);
				}else{
					header.setVisibility(View.GONE);
				}
			}
		}
		if (sortConfig == 2) {
			label = filtered.get(position).num;
			if(position == 0) {
				setSection(header, label);
			}
			else {
				String preLabel = filtered.get(position-1).num;
				if (!label.equals(preLabel)) {
					setSection(header, label);
				}else{
					header.setVisibility(View.GONE);
				}
			}
		}
		TextView textView =(TextView)view.findViewById(R.id.textView);
		textView.setText(filtered.get(position).name);
		textView.setPadding(10, 25, 0, 25);
		return view;
	}
	
    @Override
    public void notifyDataSetInvalidated()
    {
        for (int i = filtered.size() - 1; i >= 0; i--) {
			Person element = filtered.get(i);
			
			if (sortConfig == 1) {
				char firstChar = element.name.substring(0, 1).toUpperCase().toCharArray()[0];
				if (!HangulChecker.isInitialSound(firstChar) && HangulChecker.isHangul(firstChar)) {
					firstChar = HangulChecker.getInitialSound(firstChar);
				}
				String firstString = Character.toString(firstChar);
				alphaIndexer.put(firstString, i);
			}
			if (sortConfig == 2) {
				if (!element.num.equals("0") && Integer.parseInt(element.num)%2==0)
					alphaIndexer.put(element.num, i);
			}
        }

        Set<String> keys = alphaIndexer.keySet();
		ArrayList<String> keyList = new ArrayList<String>(keys);
		if (sortConfig == 1) Collections.sort(keyList, new alphabetComparator());
		if (sortConfig == 2) Collections.sort(keyList, new studentNumberComparator());
		sections = new String[keyList.size()];
		keyList.toArray(sections);

        super.notifyDataSetInvalidated();
    }
		  
	public int getSectionForIndex(String index) {
		if (sortConfig==1) return index.toCharArray()[0];
		if (sortConfig==2) return Integer.parseInt(index);
		return -1;
	}
	
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < filtered.size(); i++) {
			Person element = filtered.get(i);
			if (sortConfig == 1) {
				char firstChar = element.name.substring(0, 1).toUpperCase().toCharArray()[0];
				if (!HangulChecker.isInitialSound(firstChar) && HangulChecker.isHangul(firstChar)) {
					firstChar = HangulChecker.getInitialSound(firstChar);
				}
				if (firstChar == section) return i;
			}
			if (sortConfig == 2) {
				if (Integer.parseInt(element.num) == section) return i;
			}
		}
		return -1;
	}
	
	@Override
	public int getSectionForPosition(int position) {
        return 0;
	}

	@Override
	public Object[] getSections() {
		return sections;
	}
	
	@Override
	public Filter getFilter() {
		if (this.filter==null) filter = new SearchFilter();
		return filter;
	}
	
	private class SearchFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			filtered = (ArrayList<Person>)items.clone();
			constraint = constraint.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (constraint != null && constraint.toString().length() > 0) {
				ArrayList<Person> filt = new ArrayList<Person>();
				ArrayList<Person> lItems = new ArrayList<Person>();
				synchronized (this) {
					lItems.addAll(items);
				}
				for (int i = 0, l = lItems.size(); i < l; i++) {
					Person m = lItems.get(i);
					if (HangulChecker.matchString(m.name, constraint.toString()))
						filt.add(m);
				}
				result.count = filt.size();
				result.values = filt;
			} else {
				synchronized (this) {
					result.values = items;
					result.count = items.size();
				}
			}
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (ArrayList<Person>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0;i<filtered.size();i++)
                add(filtered.get(i));
            notifyDataSetInvalidated();
		}
	}
	
	private static class HangulChecker {
		// www.roter.pe.kr
		private static char HANGUL_BEGIN_UNICODE = 44032; // 가
		private static char HANGUL_LAST_UNICODE = 55203; // 힣
		private static char HANGUL_BASE_UNIT = 588;// 각자음 마다 가지는 글자수
		// 자음
		private static char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
				'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

		public static boolean isInitialSound(char searchar) {
			for (char c : INITIAL_SOUND) {
				if (c == searchar) {
					return true;
				}
			}
			return false;
		}

		public static char getInitialSound(char c) {
			int hanBegin = (c - HANGUL_BEGIN_UNICODE);
			int index = hanBegin / HANGUL_BASE_UNIT;
			return INITIAL_SOUND[index];
		}

		public static boolean isHangul(char c) {
			return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
		}

		public static boolean matchString(String value, String search) {
			int t = 0;
			int seof = value.length() - search.length();
			int slen = search.length();
			if (seof < 0) return false; 
			for (int i = 0; i <= seof; i++) {
				t = 0;
				while (t < slen) {
					if (isInitialSound(search.charAt(t)) == true && isHangul(value.charAt(i + t))) {
						if (getInitialSound(value.charAt(i + t)) == search.charAt(t)) t++;
						else break;
					} else {
						if (value.charAt(i + t) == search.charAt(t)) t++;
						else break;
					}
				}
				if (t == slen) return true; 
			}
			return false; 
		}
	}


}