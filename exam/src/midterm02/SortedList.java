package midterm02;

import java.util.ArrayList;
import java.util.Collections;

public class SortedList<T extends Comparable<T>> {
	private ArrayList<T> list;

	public SortedList(ArrayList<T> list) {
		this.list = list;
		filter(list);
		bubbleSort(list);
	}

	void filter(ArrayList<T> list) {
		// REVIEW: implement the filtering criteria designated in the question.
		ArrayList<T> filteredList = new ArrayList<T>();
		for (int i = 1; i < list.size(); i += 3) {
			filteredList.add(list.get(i));
		}
		this.list = filteredList;
	}

	void bubbleSort(ArrayList<T> list) {
		// REVIEW: implement the bubble sort algorithm
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - 1 - i; j++) {
				if (list.get(j).compareTo(list.get(j + 1)) > 0) {
					T temp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, temp);
				}
			}
		}
	}

	public ArrayList<T> getList() {
		return list;
	}
}