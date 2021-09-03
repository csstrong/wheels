package T20210902;

import java.util.HashSet;
import java.util.Set;

public class ListToArray {
    public static void main(String[] args) {
        Set<String> set=new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set.add(i*2+"dog.");
        }
        String[] keys = new String[set.size()];
        String[] strings = set.toArray(keys);
        System.out.println(strings);
    }
}
