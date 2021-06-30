package com.jacoblip.andriod.giniappstest

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jacoblip.andriod.giniappstest.data.services.MainViewModel
import com.jacoblip.andriod.giniappstest.data.services.Repository
import com.jacoblip.andriod.giniappstest.data.services.ViewModelProviderFactory
import com.jacoblip.andriod.giniappstest.utilities.Util
import com.jacoblip.andriod.giniappstest.utilities.WifiReceiver
import com.jacoblip.andriod.giniappstest.views.NumbersFragment
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var wifiReceiver: WifiReceiver
    lateinit var fragment :Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GiniAppsTest)
        setContentView(R.layout.activity_main)

        initRealm()
        setUpObservers()
        setUpServices()

        when(savedInstanceState?.getInt("currentFragment", 1) ?: 1){
            1->{fragment = NumbersFragment.newInstance()}
        }
        setTheFragment(fragment)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt("currentFragment",1)}

    fun setTheFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    fun initRealm(){
        Realm.init(this)
        var realmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    fun setUpServices(){
        val repository = Repository()
        val viewModelProviderFactory = ViewModelProviderFactory(repository,applicationContext)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
        wifiReceiver = WifiReceiver()
    }

    fun setUpObservers(){
        Util.requestIsSuccesfull.observe(this, Observer {
            if(!it){
                Util.requestIsSuccesfull.postValue(true)
                val dialogView = layoutInflater.inflate(R.layout.problem_dialog, null)
                val yesButton = dialogView.findViewById(R.id.tryAgainButton) as Button

                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setView(dialogView).setCancelable(false)


                val dialog = alertDialog.create()
                dialog.show()

                yesButton.setOnClickListener {
                    viewModel.getNumbersFromWeb()
                    dialog.dismiss()
                }

            }
        })
    }


    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiReceiver, filter)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiReceiver)
    }
}