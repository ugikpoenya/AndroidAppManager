package com.ugikpoenya.sampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ugikpoenya.appmanager.AdsManager
import com.ugikpoenya.appmanager.AppManager
import com.ugikpoenya.appmanager.ServerManager
import com.ugikpoenya.appmanager.ads.AdmobManager
import com.ugikpoenya.appmanager.holder.AdsViewHolder
import com.ugikpoenya.sampleapp.databinding.ActivityMainBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val appManager = AppManager()
    val adsManager = AdsManager()
    val serverManager = ServerManager()
    val groupAdapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        serverManager.getPosts(this) { response: ArrayList<PostModel?>? -> null }
//        serverManager.getAssetFiles(this) { response: ArrayList<String?>? -> null }
//        serverManager.getAssetFiles(this, "Dian Piesesha") { response: ArrayList<String?>? -> null }
//        serverManager.getAssetFolders(this) { response: Map<String?, ArrayList<String?>?>? -> null }
//        serverManager.getAssetFolders(this, "Muchsin Alatas") { response: Map<String?, ArrayList<String?>?>? -> null }
        appManager.initPrivacyPolicy(this)
        appManager.initDialogRedirect(this)
        adsManager.initBanner(this, binding!!.lyBannerAds, 0, "home")

        val listLayoutManager = LinearLayoutManager(this)
        listLayoutManager.orientation = RecyclerView.VERTICAL
        listLayoutManager.generateDefaultLayoutParams()
        val dividerItemDecoration = DividerItemDecoration(binding.recyclerView.context, listLayoutManager.orientation)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.layoutManager = listLayoutManager
        binding.recyclerView.adapter = groupAdapter

        groupAdapter.add(ItemViewHolder("Privacy Policys") {
            appManager.showPrivacyPolicy(this)
        })

        groupAdapter.add(AdsViewHolder(this, 0, "home"))


        groupAdapter.add(ItemViewHolder("Rate App") {
            appManager.rateApp(this)
        })

        groupAdapter.add(AdsViewHolder(this, 0, "detail"))

        groupAdapter.add(ItemViewHolder("More App") {
            appManager.nextApp(this)
        })

        groupAdapter.add(AdsViewHolder(this, 0, "small"))

        groupAdapter.add(ItemViewHolder("Share") {
            appManager.shareApp(this, getString(R.string.app_name))
        })

        groupAdapter.add(AdsViewHolder(this, 0, "medium"))

        groupAdapter.add(ItemViewHolder("Interstitial") {
            adsManager.showInterstitial(this, 0)
        })

        groupAdapter.add(ItemViewHolder("Rewarded Ads") {
            adsManager.showRewardedAds(this, 0) { response: Boolean? ->
                Log.d("LOG", "Rewarded ads result " + response.toString())
                null
            }
        })

        groupAdapter.add(ItemViewHolder("Reset GDPR") {
            AdmobManager().resetGDPR()
        })
    }

    override fun onBackPressed() {
        AppManager().exitApp(this)
    }

    override fun onStart() {
        super.onStart()
        AdmobManager().showOpenAdsAdmob(this)
    }
}