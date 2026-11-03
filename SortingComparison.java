

package com.mycompany.sortingcomparison;

import java.util.Arrays;
import java.util.Random;

public class SortingComparison {

    public static void main(String[] args) {
        int dataSize = 100000;
        
        System.out.println("Generating data arrays for N = " + dataSize + " ...");
        
        // إنشاء المصفوفات الأربع بالحالات المطلوبة للبيانات
        int[] randomArr = makeRandomArray(dataSize);
        int[] reversedArr = makeReversedArray(dataSize);
        int[] partialArr = makePartialSortedArray(dataSize);
        int[] sortedArr = makeSortedArray(dataSize);

        System.out.println("\n---------------------------------------------------------------------------------------------------------");
        System.out.printf("%-18s | %-16s | %-18s | %-18s | %-16s%n", "Algorithm", "Random", "Reversely Sorted", "Partially Sorted", "Fully Sorted");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        // اختبار وتشغيل كل خوارزميات الترتيب بالترتيب
        testAndPrint("Selection Sort", randomArr, reversedArr, partialArr, sortedArr, 1);
        testAndPrint("Insertion Sort", randomArr, reversedArr, partialArr, sortedArr, 2);
        testAndPrint("Bubble Sort",    randomArr, reversedArr, partialArr, sortedArr, 3);
        testAndPrint("Merge Sort",     randomArr, reversedArr, partialArr, sortedArr, 4);
        testAndPrint("Quick Sort",     randomArr, reversedArr, partialArr, sortedArr, 5);
        
        System.out.println("---------------------------------------------------------------------------------------------------------");
    }

    public static void testAndPrint(String name, int[] rand, int[] rev, int[] part, int[] sort, int type) {
        // قياس وقت التنفيذ لكل حالة من حالات المصفوفة بشكل مستقل
        long time1 = computeTime(rand, type);
        long time2 = computeTime(rev, type);
        long time3 = computeTime(part, type);
        long time4 = computeTime(sort, type);

        System.out.printf("%-18s | %-12d us | %-14d us | %-14d us | %-12d us%n", 
                name, time1, time2, time3, time4);
    }

    // دالة مساعدة لنسخ المصفوفات وقياس وقت التنفيذ الدقيق بالميكروثانية
    public static long computeTime(int[] originalArray, int type) {
        int[] arrCopy = Arrays.copyOf(originalArray, originalArray.length);
        
        long t1 = System.nanoTime(); // بدء حساب الوقت
        
        switch (type) {
            case 1: doSelectionSort(arrCopy); break;
            case 2: doInsertionSort(arrCopy); break;
            case 3: doBubbleSort(arrCopy); break;
            case 4: doMergeSort(arrCopy, 0, arrCopy.length - 1); break;
            case 5: doQuickSort(arrCopy, 0, arrCopy.length - 1); break;
        }
        
        long t2 = System.nanoTime(); // انتهاء حساب الوقت
        return (t2 - t1) / 1000; // تحويل الوقت من نانوثانية إلى ميكروثانية
    }

    // 1. خوارزمية الترتيب الاختياري 
    public static void doSelectionSort(int[] a) {
        int len = a.length;
        for (int x = 0; x < len - 1; x++) {
            int minIdx = x;
            for (int y = x + 1; y < len; y++) {
                if (a[y] < a[minIdx]) {
                    minIdx = y;
                }
            }
            int tempVal = a[minIdx];
            a[minIdx] = a[x];
            a[x] = tempVal;
        }
    }

    // 2. خوارزمية الإدراج 
    public static void doInsertionSort(int[] a) {
        int len = a.length;
        for (int x = 1; x < len; ++x) {
            int keyVal = a[x];
            int y = x - 1;
            while (y >= 0 && a[y] > keyVal) {
                a[y + 1] = a[y];
                y--;
            }
            a[y + 1] = keyVal;
        }
    }

    // 3. خوارزمية الفقاعة 
    public static void doBubbleSort(int[] a) {
        int len = a.length;
        boolean swapped;
        for (int x = 0; x < len - 1; x++) {
            swapped = false;
            for (int y = 0; y < len - x - 1; y++) {
                if (a[y] > a[y + 1]) {
                    int tempVal = a[y];
                    a[y] = a[y + 1];
                    a[y + 1] = tempVal;
                    swapped = true;
                }
            }
            if (!swapped) break; // الخروج مبكراً إذا تم ترتيب العناصر
        }
    }

    // 4. خوارزمية الترتيب بالدمج 
    public static void doMergeSort(int[] a, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            doMergeSort(a, left, mid);
            doMergeSort(a, mid + 1, right);
            mergeParts(a, left, mid, right);
        }
    }

    public static void mergeParts(int[] a, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(a, left, L, 0, n1);
        System.arraycopy(a, mid + 1, R, 0, n2);

        int x = 0, y = 0, k = left;
        while (x < n1 && y < n2) {
            if (L[x] <= R[y]) {
                a[k++] = L[x++];
            } else {
                a[k++] = R[y++];
            }
        }
        while (x < n1) a[k++] = L[x++];
        while (y < n2) a[k++] = R[y++];
    }

    // 5. خوارزمية الترتيب السريع 
    public static void doQuickSort(int[] a, int low, int high) {
        while (low < high) {
            int pivotIndex = partitionArray(a, low, high);
            if (pivotIndex - low < high - pivotIndex) {
                doQuickSort(a, low, pivotIndex - 1);
                low = pivotIndex + 1;
            } else {
                doQuickSort(a, pivotIndex + 1, high);
                high = pivotIndex - 1;
            }
        }
    }

    public static int partitionArray(int[] a, int low, int high) {
        int mid = low + (high - low) / 2;
        int pivot = a[mid];
        int tempVal = a[mid];
        a[mid] = a[high];
        a[high] = tempVal;

        int x = low - 1;
        for (int y = low; y < high; y++) {
            if (a[y] < pivot) {
                x++;
                int t = a[x];
                a[x] = a[y];
                a[y] = t;
            }
        }
        int t = a[x + 1];
        a[x + 1] = a[high];
        a[high] = t;
        return x + 1;
    }

    // دوال مساعدة لإنشاء حالات المصفوفات المختلفة للاختبار
    public static int[] makeRandomArray(int size) {
        Random rnd = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = rnd.nextInt(1000000);
        return arr;
    }

    public static int[] makeReversedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = size - i;
        return arr;
    }

    public static int[] makeSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = i;
        return arr;
    }

    public static int[] makePartialSortedArray(int size) {
        int[] arr = makeSortedArray(size);
        Random rnd = new Random();
        int swaps = (int) (size * 0.2); 
        for (int i = 0; i < swaps; i++) {
            int idx1 = rnd.nextInt(size);
            int idx2 = rnd.nextInt(size);
            int tempVal = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = tempVal;
        }
        return arr;
    }
}
