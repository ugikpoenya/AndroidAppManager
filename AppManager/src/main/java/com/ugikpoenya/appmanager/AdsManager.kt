package com.ugikpoenya.appmanager

import android.content.Context
import android.util.Log
import android.widget.RelativeLayout
import com.ugikpoenya.appmanager.ads.AdmobManager
import com.ugikpoenya.appmanager.ads.AppLovin
import com.ugikpoenya.appmanager.ads.FacebookManager
import com.ugikpoenya.appmanager.ads.UnityManager

var intervalCounter = 0
var ORDER_ADMOB: Int = 0
var ORDER_FACEBOOK: Int = 1
var ORDER_UNITY: Int = 2
var ORDER_APPLOVIN: Int = 3

class AdsManager {
    fun initAds(context: Context, function: () -> (Unit)) {
        Log.d("LOG", "Ads Manager initAds")
        if (Prefs(context).ITEM_MODEL.admob_gdpr) {
            AdmobManager().initGdpr(context, function)
        } else {
            AdmobManager().initAdmobAds(context)
            function()
        }
        FacebookManager().initFacebookAds(context)
        UnityManager().initUnityAds(context)
        AppLovin().initAppLovinAds(context)
    }


    fun initBanner(context: Context, view: RelativeLayout, ORDER: Int = 0, PAGE: String = "") {
        var bannerView = true
        if (PAGE.lowercase().trim() == "home") bannerView = Prefs(context).ITEM_MODEL.home_banner
        if (PAGE.lowercase().trim() == "detail") bannerView = Prefs(context).ITEM_MODEL.detail_banner
        if (bannerView) {
            if (view.childCount == 0) {
                Log.d("LOG", "initBanner $ORDER $PAGE")
                var priority: String? = ""
                if (PAGE.lowercase().trim() == "home") priority = Prefs(context).ITEM_MODEL.home_priority
                if (PAGE.lowercase().trim() == "detail") priority = Prefs(context).ITEM_MODEL.detail_priority
                if (priority.isNullOrEmpty()) priority = DEFAULT_PRIORITY

                val array = priority.split(",").map { it.toInt() }
                if (array.contains(ORDER)) {
                    when {
                        array[ORDER] == ORDER_ADMOB -> AdmobManager().initAdmobBanner(context, view, ORDER + 1, PAGE)
                        array[ORDER] == ORDER_FACEBOOK -> FacebookManager().initFacebookBanner(context, view, ORDER + 1, PAGE)
                        array[ORDER] == ORDER_UNITY -> UnityManager().initUnityBanner(context, view, ORDER + 1, PAGE)
                        array[ORDER] == ORDER_APPLOVIN -> AppLovin().initAppLovinBanner(context, view, ORDER + 1, PAGE)
                        else -> initBanner(context, view, ORDER + 1, PAGE)
                    }
                }
            }
        } else {
            Log.d("LOG", "Banner $PAGE disable")
        }
    }

    fun initNative(context: Context, view: RelativeLayout, ORDER: Int = 0, PAGE: String = "") {
        var nativeView = true
        if (PAGE.lowercase().trim() == "home") nativeView = Prefs(context).ITEM_MODEL.home_native
        if (PAGE.lowercase().trim() == "detail") nativeView = Prefs(context).ITEM_MODEL.detail_native

        if (nativeView) {
            if (view.childCount == 0) {
                Log.d("LOG", "initNative $ORDER $PAGE")
                var priority: String? = ""
                if (PAGE.lowercase().trim() == "home") priority = Prefs(context).ITEM_MODEL.home_priority
                if (PAGE.lowercase().trim() == "detail") priority = Prefs(context).ITEM_MODEL.detail_priority
                if (priority.isNullOrEmpty()) priority = DEFAULT_PRIORITY

                val array = priority.split(",").map { it.toInt() }
                if (array.contains(ORDER)) {
                    when {
                        array[ORDER] == ORDER_ADMOB -> AdmobManager().initAdmobNative(context, view, ORDER + 1, PAGE)
                        array[ORDER] == ORDER_FACEBOOK -> FacebookManager().initFacebookNative(context, view, ORDER + 1, PAGE)
                        array[ORDER] == ORDER_APPLOVIN -> AppLovin().initAppLovinNative(context, view, ORDER + 1, PAGE)
                        else -> initNative(context, view, ORDER + 1, PAGE)
                    }
                }
            }
        } else {
            Log.d("LOG", "Native $PAGE disable")
        }

    }

    fun showInterstitial(context: Context, ORDER: Int = 0) {
        Log.d("LOG", "Show Interstitial $ORDER intervalCounter $intervalCounter")
        if (intervalCounter <= 0) {
            var priority: String? = Prefs(context).ITEM_MODEL.interstitial_priority
            if (priority.isNullOrEmpty()) priority = DEFAULT_PRIORITY
            val array = priority.split(",").map { it.toInt() }
            if (array.contains(ORDER)) {
                when {
                    array[ORDER] == ORDER_ADMOB -> AdmobManager().showInterstitialAdmob(context, ORDER + 1)
                    array[ORDER] == ORDER_FACEBOOK -> FacebookManager().showInterstitialFacebook(context, ORDER + 1)
                    array[ORDER] == ORDER_UNITY -> UnityManager().showInterstitialUnity(context, ORDER + 1)
                    array[ORDER] == ORDER_APPLOVIN -> AppLovin().showInterstitialAppLovin(context, ORDER + 1)
                    else -> showInterstitial(context, ORDER + 1)
                }
            }
        } else {
            intervalCounter--
        }
    }

    fun showRewardedAds(context: Context, ORDER: Int = 0) {
        Log.d("LOG", "Show  RewardedAds $ORDER")
        var priority: String? = Prefs(context).ITEM_MODEL.interstitial_priority
        if (priority.isNullOrEmpty()) priority = DEFAULT_PRIORITY
        val array = priority.split(",").map { it.toInt() }
        if (array.contains(ORDER)) {
            when {
                array[ORDER] == ORDER_ADMOB -> AdmobManager().showRewardedAdmob(context, ORDER + 1)
                array[ORDER] == ORDER_FACEBOOK -> FacebookManager().showRewardedFacebook(context, ORDER + 1)
                array[ORDER] == ORDER_UNITY -> UnityManager().showRewardedUnity(context, ORDER + 1)
                array[ORDER] == ORDER_APPLOVIN -> AppLovin().showRewardedAppLovin(context, ORDER + 1)
                else -> showRewardedAds(context, 0)
            }
        } else {
            Log.d("LOG", "All rewarded null")
            showInterstitial(context, 0)
        }
    }

    fun showOpenAds(context: Context) {
        AdmobManager().showOpenAdsAdmob(context)
    }

    fun resetGDPR() {
        AdmobManager().resetGDPR()
    }
}