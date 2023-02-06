package com.google.firebase.example.analytics.kotlin

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdRevenueListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.example.analytics.R
import com.google.firebase.ktx.Firebase
import com.ironsource.mediationsdk.impressionData.ImpressionData
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener

class MainActivity :
    AppCompatActivity(),
    // importing libraries to support 3rd party ad_impression snippets
    MaxAdRevenueListener,
    ImpressionDataListener {

    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    // [END declare_analytics]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START shared_app_measurement]
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        // [END shared_app_measurement]

        enhancedEcommerce()

        setUserFavoriteFood("pizza")

        recordImageView()

        logCustomEvent()
    }

    public override fun onResume() {
        super.onResume()
        recordScreenView()
    }

    fun enhancedEcommerce() {
        // [START create_items]
        val itemJeggings = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "jeggings")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "pants")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "black")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 9.99)
        }

        val itemBoots = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_456")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "boots")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "shoes")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "brown")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 24.99)
        }

        val itemSocks = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "SKU_789")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "ankle_socks")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "socks")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "red")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "Google")
            putDouble(FirebaseAnalytics.Param.PRICE, 5.99)
        }
        // [END create_items]

        // [START view_item_list]
        val itemJeggingsWithIndex = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 1)
        }
        val itemBootsWithIndex = Bundle(itemBoots).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 2)
        }
        val itemSocksWithIndex = Bundle(itemSocks).apply {
            putLong(FirebaseAnalytics.Param.INDEX, 3)
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST) {
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            param(
                FirebaseAnalytics.Param.ITEMS,
                arrayOf(itemJeggingsWithIndex, itemBootsWithIndex, itemSocksWithIndex)
            )
        }
        // [END view_item_list]

        // [START select_item]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_LIST_ID, "L001")
            param(FirebaseAnalytics.Param.ITEM_LIST_NAME, "Related products")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
        // [END select_item]

        // [START view_product_details]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 9.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
        // [END view_product_details]

        // [START add_to_cart_wishlist]
        val itemJeggingsWishlist = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 2 * 9.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsWishlist))
        }
        // [END add_to_cart_wishlist]

        // [START view_cart]
        val itemJeggingsCart = Bundle(itemJeggings).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 2)
        }
        val itemBootsCart = Bundle(itemBoots).apply {
            putLong(FirebaseAnalytics.Param.QUANTITY, 1)
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 2 * 9.99 + 1 * 24.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart, itemBootsCart))
        }
        // [END view_cart]

        // [START remove_from_cart]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 1 * 24.99)
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemBootsCart))
        }
        // [END remove_from_cart]

        // [START start_checkout]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END start_checkout]

        // [START add_shipping]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.SHIPPING_TIER, "Ground")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END add_shipping]

        // [START add_payment]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO) {
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.PAYMENT_TYPE, "Visa")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END add_payment]

        // [START log_purchase]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE) {
            param(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            param(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 14.98)
            param(FirebaseAnalytics.Param.TAX, 2.58)
            param(FirebaseAnalytics.Param.SHIPPING, 5.34)
            param(FirebaseAnalytics.Param.COUPON, "SUMMER_FUN")
            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggingsCart))
        }
        // [END log_purchase]

        // [START log_refund]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REFUND) {
            param(FirebaseAnalytics.Param.TRANSACTION_ID, "T12345")
            param(FirebaseAnalytics.Param.AFFILIATION, "Google Store")
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, 9.99)

            // (Optional) for partial refunds, define the item ID and quantity of refunded items
            param(FirebaseAnalytics.Param.ITEM_ID, "SKU_123")
            param(FirebaseAnalytics.Param.QUANTITY, 1)

            param(FirebaseAnalytics.Param.ITEMS, arrayOf(itemJeggings))
        }
        // [END log_refund]

        // [START apply_promo]
        val promoParams = Bundle().apply {
            putString(FirebaseAnalytics.Param.PROMOTION_ID, "SUMMER_FUN")
            putString(FirebaseAnalytics.Param.PROMOTION_NAME, "Summer Sale")
            putString(FirebaseAnalytics.Param.CREATIVE_NAME, "summer2020_promo.jpg")
            putString(FirebaseAnalytics.Param.CREATIVE_SLOT, "featured_app_1")
            putString(FirebaseAnalytics.Param.LOCATION_ID, "HERO_BANNER")
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, arrayOf<Parcelable>(itemJeggings))
        }

        // Promotion displayed
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams)

        // Promotion selected
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams)
        // [END apply_promo]
    }

    private fun setUserFavoriteFood(food: String) {
        // [START user_property]
        firebaseAnalytics.setUserProperty("favorite_food", food)
        // [END user_property]
    }

    private fun recordImageView() {
        val id = "imageId"
        val name = "imageTitle"

        // [START image_view_event]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, id)
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }
        // [END image_view_event]
    }

    private fun recordScreenView() {
        val screenName = "Home Page"

        // [START set_current_screen]
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        }
        // [END set_current_screen]
    }

    private fun logCustomEvent() {
        val name = "customImage"
        val text = "I'd love to hear more about $name"

        // [START custom_event]
        firebaseAnalytics.logEvent("share_image") {
            param("image_name", name)
            param("full_text", text)
        }
        // [END custom_event]
    }

    // [START ad_impression_applovin]
    override fun onAdRevenuePaid(impressionData: MaxAd?) {
        impressionData?.let {
            firebaseAnalytics = Firebase.analytics
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION) {
                param(FirebaseAnalytics.Param.AD_PLATFORM, "appLovin")
                param(FirebaseAnalytics.Param.AD_UNIT_NAME, impressionData.adUnitId)
                param(FirebaseAnalytics.Param.AD_FORMAT, impressionData.format.label)
                param(FirebaseAnalytics.Param.AD_SOURCE, impressionData.networkName)
                param(FirebaseAnalytics.Param.VALUE, impressionData.revenue)
                param(FirebaseAnalytics.Param.CURRENCY, "USD") // All Applovin revenue is sent in USD
            }
        }
    }
    // [END ad_impression_applovin]

    // [START ad_impression_ironsource]
    override fun onImpressionSuccess(impressionData: ImpressionData) {
        // The onImpressionSuccess will be reported when the rewarded video and interstitial ad is
        // opened.
        // For banners, the impression is reported on load success. Log.d(TAG, "onImpressionSuccess" +
        // impressionData)
        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.AD_IMPRESSION) {
            param(FirebaseAnalytics.Param.AD_PLATFORM, "ironSource")
            param(FirebaseAnalytics.Param.AD_SOURCE, impressionData.adNetwork)
            param(FirebaseAnalytics.Param.AD_FORMAT, impressionData.adUnit)
            param(FirebaseAnalytics.Param.AD_UNIT_NAME, impressionData.instanceName)
            param(FirebaseAnalytics.Param.CURRENCY, "USD")
            param(FirebaseAnalytics.Param.VALUE, impressionData.revenue)
        }
    }
    // [END ad_impression_ironsource]
}
