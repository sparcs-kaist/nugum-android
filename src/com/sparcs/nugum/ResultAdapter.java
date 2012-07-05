package com.sparcs.nugum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.SectionIndexer;

public class ResultAdapter extends ArrayAdapter<Person> implements SectionIndexer {
	public ArrayList<Person> items;
	public ArrayList<Person> filtered;
	private Context context;
	private HashMap<String, Integer> alphaIndexer;
	private String[] sections = new String[0];
	private Filter filter;
	private boolean enableSections;
	
	public ResultAdapter(Context context, int textViewResourceId, ArrayList<Person> items, boolean enableSections) {
		super(context, textViewResourceId, items);
		this.filtered = items;
		this.items = (ArrayList<Person>) items.clone();
		
		this.context = context;
		this.filter = new SearchFilter();
		this.enableSections = enableSections;
		
		if (enableSections) {
			this.alphaIndexer = new HashMap<String, Integer>();
			for (int i=items.size()-1;i>=0;i--) {
				Person element = items.get(i);
				String firstChar = element.name.substring(0,1).toUpperCase();
				alphaIndexer.put(firstChar, i);
			}
			
            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while(it.hasNext())
                keyList.add(it.next());

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);
		}
	}
	
    @Override
    public void notifyDataSetInvalidated()
    {
        if(enableSections)
        {
            for (int i = items.size() - 1; i >= 0; i--)
            {
                Person element = items.get(i);
                String firstChar = element.name.substring(0, 1).toUpperCase();
                if(firstChar.charAt(0) > 'Z' || firstChar.charAt(0) < 'A')
                    firstChar = "@";
                alphaIndexer.put(firstChar, i);
            }

            Set<String> keys = alphaIndexer.keySet();
            Iterator<String> it = keys.iterator();
            ArrayList<String> keyList = new ArrayList<String>();
            while (it.hasNext())
            {
                keyList.add(it.next());
            }

            Collections.sort(keyList);
            sections = new String[keyList.size()];
            keyList.toArray(sections);

            super.notifyDataSetInvalidated();
        }
    }
    
	@Override
	public int getPositionForSection(int section) {
        if(!enableSections) return 0;
        String letter = sections[section];

        return alphaIndexer.get(letter);
	}

	@Override
	public int getSectionForPosition(int position) {
        if(!enableSections) return 0;
        int prevIndex = 0;
        for(int i = 0; i < sections.length; i++)
        {
            if(getPositionForSection(i) > position && prevIndex <= position)
            {
                prevIndex = i;
                break;
            }
            prevIndex = i;
        }
        return prevIndex;
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
			// TODO Auto-generated method stub
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
					if (matchString(m.name, constraint.toString()))
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
            for(int i = 0, l = filtered.size(); i < l; i++)
                add(filtered.get(i));
            notifyDataSetInvalidated();
		}

		// www.roter.pe.kr
		private char HANGUL_BEGIN_UNICODE = 44032; // 가
		private char HANGUL_LAST_UNICODE = 55203; // 힣
		private char HANGUL_BASE_UNIT = 588;// 각자음 마다 가지는 글자수
		// 자음
		private char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
				'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

		private boolean isInitialSound(char searchar) {
			for (char c : INITIAL_SOUND) {
				if (c == searchar) {
					return true;
				}
			}
			return false;
		}

		private char getInitialSound(char c) {
			int hanBegin = (c - HANGUL_BEGIN_UNICODE);
			int index = hanBegin / HANGUL_BASE_UNIT;
			return INITIAL_SOUND[index];
		}

		private boolean isHangul(char c) {
			return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
		}

		private boolean matchString(String value, String search) {
			int t = 0;
			int seof = value.length() - search.length();
			int slen = search.length();
			if (seof < 0)
				return false; // 검색어가 더 길면 false를 리턴한다.
			for (int i = 0; i <= seof; i++) {
				t = 0;
				while (t < slen) {
					if (isInitialSound(search.charAt(t)) == true
							&& isHangul(value.charAt(i + t))) {
						// 만약 현재 char이 초성이고 value가 한글이면
						if (getInitialSound(value.charAt(i + t)) == search
								.charAt(t))
							// 각각의 초성끼리 같은지 비교한다
							t++;
						else
							break;
					} else {
						// char이 초성이 아니라면
						if (value.charAt(i + t) == search.charAt(t))
							// 그냥 같은지 비교한다.
							t++;
						else
							break;
					}
				}
				if (t == slen)
					return true; // 모두 일치한 결과를 찾으면 true를 리턴한다.
			}
			return false; // 일치하는 것을 찾지 못했으면 false를 리턴한다.
		}
	}


}