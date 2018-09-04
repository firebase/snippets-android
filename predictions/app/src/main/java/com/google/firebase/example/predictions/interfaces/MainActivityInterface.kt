package com.google.firebase.example.predictions.interfaces

/**
 * An interface to be implemented by the Activity.
 * Add any new method to this interface instead of adding it directly to the activity
 */
interface MainActivityInterface {

    fun configShowAds()
    fun executeAdsPolicy()
    fun configPromoStrategy()
    fun getPromotedBundle() : String
    fun configPreventChurn()
    fun executeGiftPolicy()
    fun grantGiftOnLevel2()
    fun grantGiftNow()

}