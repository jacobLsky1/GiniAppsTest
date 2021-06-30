package com.jacoblip.andriod.giniappstest.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jacoblip.andriod.giniappstest.R
import com.jacoblip.andriod.giniappstest.data.services.MainViewModel
import com.jacoblip.andriod.giniappstest.utilities.InternetConectivity
import com.jacoblip.andriod.giniappstest.utilities.Util
import com.jacoblip.andriod.giniappstest.views.adapters.NumberAdapter
class NumbersFragment:Fragment() {

    private lateinit var viewModel:MainViewModel
    private lateinit var numbersRV :RecyclerView
    private lateinit var progressBar:ProgressBar
    private lateinit var snackBar:Snackbar
    private var fragmentReady:MutableLiveData<Boolean> = MutableLiveData()
    private var numbers:Array<Int>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_numbers,container,false)
        view.apply {
            numbersRV = findViewById(R.id.number_RV)
            progressBar = findViewById(R.id.progressBar)
            numbersRV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }
        numbers = viewModel.viewModelNumbers
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers(view)
        fragmentReady.postValue(true)
    }


    fun getData(){
        if(numbers!=null){
            Log.i("change","change")
            numbersRV.adapter = NumberAdapter(numbers!!,viewModel.numbersThatArePairs)
            progressBar.visibility = View.GONE
        }
        if(numbers==null){
            viewModel.getNumbersFromWeb()
        }
    }

    fun setUpObservers(view: View){


        snackBar = Snackbar.make(view, "Can't Connect To Web..", Snackbar.LENGTH_INDEFINITE)
            .setAction("GO TO SETTINGS") {
                context?.let { it1 -> InternetConectivity.connectToInternet(requireContext()) }
            }

        fragmentReady.observe(viewLifecycleOwner, Observer {
            if(it){
                getData()
            }
        })

        viewModel.numbersLD.observe(viewLifecycleOwner, Observer {
            if(it.data!=null&&!viewModel.gotDataFromWeb){
                Log.i("change1","change1")
                numbersRV.adapter = NumberAdapter(it.data,viewModel.numbersThatArePairs)
                progressBar.visibility = View.GONE
                viewModel.gotDataFromWeb = true
            }
        })

        Util.hasInternet.observe(viewLifecycleOwner, Observer { it ->
            if (!it) {
                snackBar!!.show()
            } else {
                snackBar!!.dismiss()
            }
        })
    }
    companion object{
        fun newInstance():NumbersFragment{
            return NumbersFragment()
        }
    }
}