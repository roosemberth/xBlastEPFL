package ch.epfl.xblast.tests;

import ch.epfl.xblast.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

public class ListsTest {
	@Test 
	public void permutationTests() {
		List<ListTestClass> testList = new ArrayList<>();

		assertEquals(testList, new ArrayList<ListTestClass>());
		assertEquals(0, Lists.permutations(testList).size());
		
		ListTestClass l1 = new ListTestClass("l1");
		ListTestClass l2 = new ListTestClass("l2");
		ListTestClass l3 = new ListTestClass("l3");
		
		testList.add(l1);
		testList.add(l2);
		testList.add(l3);
		
		List<List<ListTestClass>> perrmControlList = new ArrayList<>(6);
		perrmControlList.add(Arrays.asList(l1,l2,l3));
		perrmControlList.add(Arrays.asList(l1,l3,l2));
		perrmControlList.add(Arrays.asList(l2,l1,l3));
		perrmControlList.add(Arrays.asList(l2,l3,l1));
		perrmControlList.add(Arrays.asList(l3,l2,l1));
		perrmControlList.add(Arrays.asList(l3,l1,l2));
		
		List<List<ListTestClass>> permList = Lists.permutations(testList);
		
		assertEquals(perrmControlList.size(),permList.size());
		assertEquals(true, containSameElements(perrmControlList, permList, (sl1, sl2)->{
			return containSameElements(sl1, sl2, (elem1, elem2)->elem1.equals(elem2));
		}));
	}
	
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