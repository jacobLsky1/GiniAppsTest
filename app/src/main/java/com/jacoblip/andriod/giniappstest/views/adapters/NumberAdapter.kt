package com.jacoblip.andriod.giniappstest.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jacoblip.andriod.giniappstest.R


class NumberAdapter(var numbers:Array<Int>, var pairedNumbers: Array<Int>): RecyclerView.Adapter<NumberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        when(viewType){
            0->{
                return NumberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.number_not_pair, parent, false))
            }
            1->{
                return NumberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.number_pair, parent, false))
            }
        }
        return NumberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.number_not_pair, parent, false))
    }

    override fun getItemCount():Int {

        return numbers.size
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        var number = numbers[position]
        holder.itemView.apply {
            if(holder.itemViewType==0){
                var TV = findViewById<TextView>(R.id.numberNotPairTV)
                TV.text = number.toString()
            }
            if(holder.itemViewType==1){
                var TV = findViewById<TextView>(R.id.numberPairTV)
                TV.text = number.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(pairedNumbers.contains(numbers[position]))
            1
        else
            0
    }




}