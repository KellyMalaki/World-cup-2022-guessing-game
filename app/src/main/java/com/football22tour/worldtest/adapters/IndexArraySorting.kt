package com.football22tour.worldtest.adapters

class IndexArraySorting(private val array: IntArray): Comparator<Int> {

    fun createIndexArray(): Array<Int>{
        val indexes = arrayOf(0,0,0,0)
        for (i in array.indices){
            indexes[i] = i
        }
        return indexes
    }
    override fun compare(p0: Int?, p1: Int?): Int {
        return array[p0!!].compareTo(array[p1!!])
    }
}