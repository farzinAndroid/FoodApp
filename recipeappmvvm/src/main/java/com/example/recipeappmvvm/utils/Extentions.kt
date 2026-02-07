package com.example.recipeappmvvm.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.ui.R


fun Spinner.setupSpinnerListWithAdapter(list: MutableList<out Any>, callback:(String)-> Unit){

    val adapter = ArrayAdapter(context,R.layout.item_spinner,list)
    adapter.setDropDownViewResource(R.layout.item_spinner_list)
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            callback(list[position].toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }

}