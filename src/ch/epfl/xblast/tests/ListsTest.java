package ch.epfl.xblast.tests;

import ch.epfl.xblast.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

public class ListsTest {

	
	private <T> boolean containSameElements(List<T> l1, List<T> l2, BiPredicate<T, T> equalityComparator){
		if (l1.size()!=l2.size()) return false;
		Elements: for (T elemL1 : l1){
			for (T elemL2 : l2){
				if (equalityComparator.test(elemL1, elemL2)) continue Elements;
			}
			return false;
		}
		return true;
	}

	class ListTestClass {
		private final String identifier;
		public ListTestClass(String identifier) {
			this.identifier = identifier;
		}
		@Override public int hashCode() { return identifier.hashCode(); };
		@Override public boolean equals(Object o){
			if (o == null) return false;
			if (!(o instanceof ListTestClass)) return false;
			boolean ret = identifier.equals(((ListTestClass)o).identifier);
			return ret;
		}
	}
}