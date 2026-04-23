import java.util.*;

// ─── Student class ────────────────────────────────────────────────────────────
class Student {
    int id; String name; double gpa;
    Student(int id, String name, double gpa) {
        this.id = id; this.name = name; this.gpa = gpa;
    }
    public String toString() { return name + "(" + gpa + ")"; }
}

// ─── Inline QuickSort (same algorithm as course QuickSort.java) ──────────────
class QSort {
    /**
     * In-place array QuickSort using a Comparator.
     * Partition strategy: last element as pivot, two-pointer scan.
     */
    public static <T> void sort(List<T> list, Comparator<T> cmp) {
        sortRange(list, cmp, 0, list.size() - 1);
    }

    private static <T> void sortRange(List<T> list, Comparator<T> cmp, int lo, int hi) {
        if (lo >= hi) return;
        int p = partition(list, cmp, lo, hi);
        sortRange(list, cmp, lo, p - 1);
        sortRange(list, cmp, p + 1, hi);
    }

    private static <T> int partition(List<T> list, Comparator<T> cmp, int lo, int hi) {
        T pivot = list.get(hi);
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (cmp.compare(list.get(j), pivot) <= 0) {
                i++;
                T tmp = list.get(i); list.set(i, list.get(j)); list.set(j, tmp);
            }
        }
        T tmp = list.get(i + 1); list.set(i + 1, list.get(hi)); list.set(hi, tmp);
        return i + 1;
    }
}

// ═════════════════════════════════════════════════════════════════════════════
//  Exercise 8 — Main class
// ═════════════════════════════════════════════════════════════════════════════
public class Exercise8_SortStudents {

    public static void main(String[] args) {

        System.out.println("════ Exercise 8 — Comparator-Driven Sort on Student ════\n");

        // ── Dataset ───────────────────────────────────────────────────────────
        List<Student> students = new ArrayList<>(Arrays.asList(
            new Student(1, "Aman",  3.2),
            new Student(2, "Bea",   3.2),
            new Student(3, "Chin",  3.9),
            new Student(4, "Dia",   2.8)
        ));
        System.out.println("Original: " + students);

        // ── Comparator 1: GPA ascending, then name alphabetical (tie-break) ───
        Comparator<Student> byGpaThenName = (s1, s2) -> {
            int c = Double.compare(s1.gpa, s2.gpa);    // primary: GPA ascending
            if (c != 0) return c;
            return s1.name.compareTo(s2.name);          // tie-break: name A-Z
        };

        // ── Sort using course-style QuickSort ─────────────────────────────────
        List<Student> qsList = new ArrayList<>(students);
        QSort.sort(qsList, byGpaThenName);
        System.out.println("QuickSort (GPA asc, name):  " + qsList);
        // [Dia(2.8), Aman(3.2), Bea(3.2), Chin(3.9)]

        // ── Sort using Collections.sort (also acceptable) ─────────────────────
        List<Student> colList = new ArrayList<>(students);
        Collections.sort(colList, byGpaThenName);
        System.out.println("Collections.sort (same cmp): " + colList);

        // PASS check
        System.out.println("\n--- PASS check ---");
        System.out.println("Expected: [Dia(2.8), Aman(3.2), Bea(3.2), Chin(3.9)]");
        System.out.println("Got:      " + colList);
        System.out.println("PASS: " + (colList.toString().equals("[Dia(2.8), Aman(3.2), Bea(3.2), Chin(3.9)]")));

        // ── Comparator 2: GPA descending ──────────────────────────────────────
        System.out.println("\n--- Sort by GPA descending ---");
        Comparator<Student> byGpaDesc = (s1, s2) -> Double.compare(s2.gpa, s1.gpa);
        List<Student> descList = new ArrayList<>(students);
        Collections.sort(descList, byGpaDesc);
        System.out.println("GPA desc: " + descList);
        // [Chin(3.9), Aman(3.2), Bea(3.2), Dia(2.8)]

        // ── Comparator 3: name alphabetical only ──────────────────────────────
        System.out.println("\n--- Sort by name A-Z ---");
        Comparator<Student> byName = Comparator.comparing(s -> s.name);
        List<Student> nameList = new ArrayList<>(students);
        Collections.sort(nameList, byName);
        System.out.println("By name: " + nameList);
        // [Aman(3.2), Bea(3.2), Chin(3.9), Dia(2.8)]

        // ── Comparator 4: ID descending ───────────────────────────────────────
        System.out.println("\n--- Sort by ID descending ---");
        Comparator<Student> byIdDesc = (s1, s2) -> Integer.compare(s2.id, s1.id);
        List<Student> idList = new ArrayList<>(students);
        Collections.sort(idList, byIdDesc);
        System.out.println("By ID desc: " + idList);
        // [Dia(2.8), Chin(3.9), Bea(3.2), Aman(3.2)]

        // ── Comparator 5: chained with Comparator API ─────────────────────────
        System.out.println("\n--- Sort using Comparator.comparingDouble chain ---");
        Comparator<Student> chained = Comparator
            .comparingDouble((Student s) -> s.gpa)
            .thenComparing(s -> s.name);
        List<Student> chainedList = new ArrayList<>(students);
        Collections.sort(chainedList, chained);
        System.out.println("Chained comparator: " + chainedList);
        // [Dia(2.8), Aman(3.2), Bea(3.2), Chin(3.9)]  — same as Exercise 8

        // ── Key concept ───────────────────────────────────────────────────────
        System.out.println("\n--- Key concept: Comparator ---");
        System.out.println("A Comparator<T> defines an EXTERNAL ordering.");
        System.out.println("compare(a, b) < 0  => a comes before b");
        System.out.println("compare(a, b) == 0 => equal (tie-break used)");
        System.out.println("compare(a, b) > 0  => b comes before a");
        System.out.println("Swapping s1 and s2 in Double.compare() reverses the order.");
    }
}
